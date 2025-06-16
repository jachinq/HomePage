package com.home.homepage.service;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.repository.AppConfigRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class AppConfigService {
    private final AppConfigRepository appConfigRepository;

    @Autowired
    public AppConfigService(AppConfigRepository habitLogRepository) {
        this.appConfigRepository = habitLogRepository;
    }

    public void save(AppConfig data) {
        AppConfig save = appConfigRepository.save(data);
        System.out.println(save);
    }

    public Result list() {
        List<AppConfig> all = appConfigRepository.findAll();
        return Result.success(all, all.size());
    }
}
