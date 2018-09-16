package com.jfs.rolemanager.common.persistance.dao;

import com.jfs.rolemanager.common.persistance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);

   // @Query("select u from User u where u.userId=:userId and u.appName=:appName")
    //User findByAppNameAndUser(@Param(value = "userId") String userId,@Param(value = "appName") String appName);

    @Override
    void delete(User user);

}
