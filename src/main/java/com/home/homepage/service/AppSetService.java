package com.home.homepage.service;

import com.home.homepage.entity.AppSet;
import com.home.homepage.entity.modal.AppSetListModal;
import com.home.homepage.repository.AppSetRepository;
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
public class AppSetService {

    private final AppSetRepository appSetRepository;

    @Autowired
    public AppSetService(AppSetRepository habitLogRepository) {
        this.appSetRepository = habitLogRepository;
    }

    public Result save(AppSet sexDaily) {
        AppSet save = appSetRepository.save(sexDaily);
        System.out.println(save);
        return Result.success(save);
    }

    public Result update(AppSet sexDaily) {
        AppSet save = appSetRepository.save(sexDaily);
        return Result.success(save);
    }

    public Result delete(AppSet dto) {
        appSetRepository.delete(dto);
        return Result.success(dto);
    }

    public Result list(AppSetListModal dto) {
        Pageable pageable = dto.getPageable();
        Page<AppSet> all;
        if (dto.getName() != null) {
            all = appSetRepository.findByUserIdAndNameIsContainingOrDescriptionIsContaining(dto.getUserId(), dto.getName(), pageable);
        } else {
            all = appSetRepository.findByUserId(dto.getUserId(), pageable);
        }

        return Result.success(all);
    }
}
