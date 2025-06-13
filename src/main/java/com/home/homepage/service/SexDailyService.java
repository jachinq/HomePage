package com.home.homepage.service;

import com.home.homepage.entity.SexDaily;
import com.home.homepage.repository.SexDailyRepository;
import com.home.homepage.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jachin
 * @since 25-06-13 17:23
 */
@Service
public class SexDailyService {
    @Autowired
    private SexDailyRepository sexDailyRepository;

    public void save(SexDaily sexDaily) {
        SexDaily save = sexDailyRepository.save(sexDaily);
        System.out.println(save);
    }

    public Result list() {
        List<SexDaily> all = sexDailyRepository.findAll();
        return Result.success(all, all.size());
    }
}
