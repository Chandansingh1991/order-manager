package com.jfs.rolemanager.common.persistance.dao;

import com.jfs.rolemanager.common.persistance.model.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigRepositry extends JpaRepository<AppConfig, Long> {
    AppConfig findByAppName(String appName);
}
