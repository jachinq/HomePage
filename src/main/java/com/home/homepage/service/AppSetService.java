package com.home.homepage.service;

import com.home.homepage.entity.AppSet;
import com.home.homepage.repository.AppSetRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Result list(AppSet dto) {
        List<AppSet> all = null;
        if (dto.getName() != null) {
            all = appSetRepository.findByNameIsContainingOrDescriptionIsContaining(dto.getName(), dto.getName());
        } else {
            all = appSetRepository.findAll();
        }
        return Result.success(all, all.size());
    }
}
