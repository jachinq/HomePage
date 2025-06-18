package com.home.homepage.service;

import com.home.homepage.entity.AppConfig;
import com.home.homepage.entity.modal.ListModal;
import com.home.homepage.repository.AppConfigRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Result save(AppConfig data) {
        AppConfig save = appConfigRepository.save(data);
        return Result.success(save);
    }

    public Result list(ListModal dto) {
        Pageable pageable = dto.getPageable();
        Page<AppConfig> all = appConfigRepository.findAll(pageable);
        return Result.success(all);
    }

    public Result update(AppConfig dto) {
        AppConfig save = appConfigRepository.save(dto);
        return Result.success(save);
    }

    public Result delete(AppConfig dto) {
        appConfigRepository.delete(dto);
        return Result.success(dto);
    }
}
