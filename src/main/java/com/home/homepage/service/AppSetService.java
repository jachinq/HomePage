package com.home.homepage.service;

import com.home.homepage.define.AppType;
import com.home.homepage.entity.AppSet;
import com.home.homepage.entity.modal.AppSetListModal;
import com.home.homepage.repository.AppSetRepository;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.PinyinUtil;
import com.home.homepage.utils.Result;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Result save(AppSet appSet) {
        appSet.setUserId(Core.getUid());
        appSet.setPinyin(appSet.generatePinyin());
        AppSet save = appSetRepository.save(appSet);
        System.out.println(save);
        return Result.success(save);
    }

    public Result batchSave(List<AppSet> appSet) {
        appSet.forEach(app -> app.setUserId(Core.getUid()));
        appSetRepository.saveAll(appSet);
        return Result.success();
    }

    public Result delete(AppSet dto) {
        appSetRepository.delete(dto);
        return Result.success(dto);
    }

    @SneakyThrows
    public Result list(AppSetListModal dto) {
        dto.setSort("sort");
        dto.setDesc(false);
        Pageable pageable = dto.getPageable();

        Page<AppSet> all;
        if (dto.getName() != null) {
            all = appSetRepository.findByKeyword(Core.getUid(), dto.getType(), PinyinUtil.generate(dto.getName()), pageable);
        } else {
            all = appSetRepository.findByUserIdAndType(Core.getUid(), dto.getType(), pageable);
        }
        return Result.success(all);
    }

    public Result importData(List<AppSet> list) {
        List<Integer> types = AppType.valueList();

        list.forEach(appSet -> {
            appSet.setId(null); // 将 id 移除
            appSet.setUserId(Core.getUid()); // 关联用户
            appSet.setPinyin(appSet.generatePinyin());
            Integer type = appSet.getType();
            if (type == null || !types.contains(type)) {
                appSet.setType(AppType.Bookmark.getValue()); // 默认为书签

            }
        });

        return Result.success();
    }
}
