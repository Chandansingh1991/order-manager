package com.jfs.rolemanager.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import java.util.Hashtable;

@Component
public class LDAPUtil {
    @Value(value = "${ldap.url}")
    private String ldapUrl;
    @Value(value = "${ldap.context,factory}")
    private String ldapContextFactory;
    @Value(value = "${ldap.security.auth}")
    private String ldapSecurityAuth;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private LDAPUtil() {

    }

    /**
     * @param username Username of User
     * @param password Password of User
     * @param prefix
     * @return false if any of the input parameter is null or blank
     * if any exception accurs during execution
     * <p>
     * true  if validation success from LDAP server
     */
    public boolean authenticate(String username, String password, String prefix) {

        logger.info("Authenticating user {} prefix {} - {}", username, prefix, prefix + username);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }
        Hashtable authEnv = getAuthEnv(username, password, prefix);
        try {
            DirContext authContext = new InitialDirContext(authEnv);
            SearchControls searchCtrls = new SearchControls();
            searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration values = authContext.search("", "samaccountname=chandan6.singh",searchCtrls);
            logger.info("Authentication Success for user {} - {}", username, authContext);
            return true;
        } catch (AuthenticationException authEx) {
            logger.error("Authentication Failed for user {}", username, authEx);
        } catch (NamingException namEx) {
            logger.error("Authentication Failed due to Naming exception for user {}", username, namEx);
        }
        return false;
    }

    /**
     * @param username
     * @param password
     * @param prefix
     * @return
     */
    private Hashtable getAuthEnv(String username, String password, String prefix) {
        Hashtable authEnv = new Hashtable(11);
        authEnv.put(Context.INITIAL_CONTEXT_FACTORY, ldapContextFactory);
        authEnv.put(Context.PROVIDER_URL, ldapUrl);
        authEnv.put(Context.SECURITY_AUTHENTICATION, ldapSecurityAuth);
        authEnv.put(Context.SECURITY_PRINCIPAL, prefix + username);
        authEnv.put(Context.SECURITY_CREDENTIALS, password);
        return authEnv;
    }
}

