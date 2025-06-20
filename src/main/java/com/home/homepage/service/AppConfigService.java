package com.home.homepage.service;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.repository.AppConfigRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Slf4j
@Service
public class AppConfigService {
    private final AppConfigRepository appConfigRepository;

    @Autowired
    public AppConfigService(AppConfigRepository habitLogRepository) {
        this.appConfigRepository = habitLogRepository;
    }

    public Result save(AppConfig data) {
        data.setUserId(Core.getUid());
        AppConfig save = appConfigRepository.save(data);
        return Result.success(save);
    }

    public Result info() {
        Long userId = Core.getUid();
        log.info("get by userId {}", userId);
        AppConfig appConfig = appConfigRepository.findByUserId(userId);
        log.info("appConfig {}", appConfig);
        return Result.success(appConfig);
    }
}
