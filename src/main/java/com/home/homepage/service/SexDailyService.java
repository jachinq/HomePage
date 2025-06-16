package com.home.homepage.service;

import com.home.homepage.entity.SexDaily;
import com.home.homepage.repository.SexDailyRepository;
import com.home.homepage.utils.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class SexDailyService {
    private final SexDailyRepository sexDailyRepository;

    public SexDailyService(SexDailyRepository sexDailyRepository) {
        this.sexDailyRepository = sexDailyRepository;
    }

    public void save(SexDaily data) {
        SexDaily save = sexDailyRepository.save(data);
        System.out.println(save);
    }

    public Result list() {
        List<SexDaily> all = sexDailyRepository.findAll();
        return Result.success(all, all.size());
    }
}
