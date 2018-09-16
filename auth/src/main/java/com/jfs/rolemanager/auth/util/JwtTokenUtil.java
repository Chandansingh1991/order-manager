package com.jfs.rolemanager.auth.util;

import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import com.jfs.rolemanager.auth.persistance.model.AppConfig;
import com.jfs.rolemanager.auth.service.eao.TokenCacheStoreService;
import com.jfs.rolemanager.auth.service.eao.TokenKeyService;
import com.jfs.rolemanager.common.constant.ResponseCode;
import com.jfs.rolemanager.common.exception.AuthException;
import com.jfs.rolemanager.common.exception.BaseApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_AUTHORITIES = "authorities";
    static final String APP_NAME = "app_name";
    @Autowired
    private TokenCacheStoreService tokenCacheService;
    @Autowired
    private TokenKeyService tokenKeyService;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * This method generated token from the user object and pass device details
     * <p>
     * if any.
     *
     * @param userId
     * @param selectedRole
     * @return
     */

    public String generateToken(String userId, String selectedRole, AppConfig appConfig, String device) throws BaseApplicationException {
        LOGGER.debug("Generating token from the user {}", userId);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userId);
        claims.put(CLAIM_KEY_AUDIENCE, device);
        claims.put(APP_NAME, appConfig.getAppName());
        claims.put(CLAIM_KEY_AUTHORITIES, selectedRole == null ? "" : selectedRole);
        Date createdDate = new Date();
        claims.put(CLAIM_KEY_CREATED, createdDate);
        return generateToken(claims, appConfig.getTokenKey());

    }

    /**
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token, String secret) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            LOGGER.debug("Claims from the token {} ", claims);
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token, String secret) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token, secret);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * @param token
     */
    public void expireTokenFromCache(String token, String appName) throws BaseApplicationException {
        String secret = tokenKeyService.fetchTokenKeyFromCache(appName);
        if (StringUtils.isEmpty(secret)) {
            LOGGER.error("Secret key is empty for App :{}", appName);
            throw new AuthException(ResponseCode.TOKEN_INTEGRITY_FAILED);
        }
        boolean isTokeNotModified = checkIftokenIsModified(token, secret);

        if (isTokeNotModified) {
            throw new AuthException(ResponseCode.TOKEN_INTEGRITY_FAILED);
        }
        String userId = fetchUserFromToken(token, secret);
        String cachedToken = tokenCacheService.fetchTokenFromCache(userId, appName);
        if (StringUtils.isEmpty(cachedToken)) {
            throw new AuthException(ResponseCode.TOKEN_NOT_FOUND);
        }
        tokenCacheService.removeTokenFromCache(userId, appName);

    }


    /**
     * This validates the token with the cache token store and authentication
     * <p>
     * and authorized the user. Read the inline comments for the details how
     * <p>
     * token is validated.
     *
     * @param requestToken
     * @param appName
     * @return
     */

    public boolean validateToken(String requestToken, String appName) throws BaseApplicationException {

        boolean isTokenValid = true;
        String secret = tokenKeyService.fetchTokenKeyFromCache(appName);
        if (StringUtils.isEmpty(secret)) {
            LOGGER.error("Secret key is empty for App :{}", appName);
            throw new AuthException(ResponseCode.TOKEN_INTEGRITY_FAILED);
        }
        boolean isTokeNotModified = checkIftokenIsModified(requestToken, secret);

        if (isTokeNotModified) {
            throw new AuthException(ResponseCode.TOKEN_INTEGRITY_FAILED);
        }
        String userId = fetchUserFromToken(requestToken, secret);
        String cachedToken = tokenCacheService.fetchTokenFromCache(userId, appName);
        boolean isTokenMatched = requestToken.equals(cachedToken);
        if (!isTokenMatched) {
            throw new AuthException(ResponseCode.TOKEN_INVALID);
        }
        Date tokenValid = getExpirationDateFromToken(cachedToken, secret);
        Calendar tokenValidupto = GregorianCalendar.getInstance();
        tokenValidupto.setTime(tokenValid);
        Calendar currentTime = Calendar.getInstance();
        boolean isTokenValidByExpirationDate = !tokenValidupto.after(currentTime);

        if (isTokenValidByExpirationDate) {
            tokenCacheService.removeTokenFromCache(userId, appName);
            throw new AuthException(ResponseCode.TOKEN_EXPIRED);
        }
        return isTokenValid;

    }

    /**
     * @param token
     * @param appName
     * @return
     */
    public String fetchUserRolesFromToken(String token, String appName) throws BaseApplicationException {
        String secret = tokenKeyService.fetchTokenKeyFromCache(appName);
        boolean isTokenModified = false;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Object roles = claims.getBody().get(CLAIM_KEY_AUTHORITIES);
            if (roles == null) {
                throw new AuthException(ResponseCode.ROLE_NOT_DEFINED);
            }
            return roles.toString();
        } catch (JwtException | IllegalArgumentException e) {
            LOGGER.error("Exception while parsing token for app :{} anda cause is :{}", appName, e);
            throw new AuthException(ResponseCode.TOKEN_INTEGRITY_FAILED);
        }
    }


    /**
     * Generating expiration date of the token. As per requirement token is
     * <p>
     * valid for current day only.
     *
     * @return expiration date
     */

    private Date generateExpirationDate() {
        Calendar expirationDateForToken = Calendar.getInstance();
        expirationDateForToken.add(Calendar.DATE, 1);
        expirationDateForToken.set(Calendar.HOUR_OF_DAY, 0);
        expirationDateForToken.set(Calendar.MINUTE, 0);
        expirationDateForToken.set(Calendar.SECOND, 0);
        expirationDateForToken.set(Calendar.MILLISECOND, 0);
        Date validUpto = expirationDateForToken.getTime();
        LOGGER.debug("Expiration date for the token {}", validUpto);
        return validUpto;

    }

    /**
     * @param token
     * @return
     */
    private boolean checkIftokenIsModified(String token, String secret) {

        boolean isTokenModified = false;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Object user = claims.getBody().get(CLAIM_KEY_USERNAME);
            LOGGER.info("Token integrity checked and passed for user: {}", user);
        } catch (Exception exception) {
            LOGGER.error("Error while checking the integrity of the token ", exception.getMessage());
            isTokenModified = true;
        }
        return isTokenModified;

    }


    /**
     * @param token
     * @return
     */
    private String fetchUserFromToken(String token, String secret) {

        boolean isTokenModified = false;
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Object user = claims.getBody().get(CLAIM_KEY_USERNAME);
            return user.toString();
        } catch (JwtException | IllegalArgumentException exception) {
            LOGGER.error("Error while fetching user from token ", exception);
            isTokenModified = true;
        }
        return "";

    }


    /**
     * This method generates and sign the token based on given secret key and
     * <p>
     * algorithm
     *
     * @param claims
     * @return
     */

    private String generateToken(Map<String, Object> claims, String secret) throws BaseApplicationException {

        String token = Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        tokenCacheService.saveTokenInCache(claims.get(CLAIM_KEY_USERNAME).toString(), claims.get(APP_NAME).toString(), token);
        return token;

    }


}
