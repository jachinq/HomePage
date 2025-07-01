package com.home.homepage.service;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.GlobalConfig;
import com.home.homepage.repository.GlobalConfigRepository;
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
public class GlobalConfigService {
    private final GlobalConfigRepository globalConfigRepository;

    @Autowired
    public GlobalConfigService(GlobalConfigRepository habitLogRepository) {
        this.globalConfigRepository = habitLogRepository;
    }

    public Result save(GlobalConfig data) {
        data.setUserId(Core.getUid());
        GlobalConfig save = globalConfigRepository.save(data);
        return Result.success(save);
    }

    public Result info() {
        Long userId = Core.getUid();
        GlobalConfig appConfig = globalConfigRepository.findByUserId(userId);
        return Result.success(appConfig);
    }
}
