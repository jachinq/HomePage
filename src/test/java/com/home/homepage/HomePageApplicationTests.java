package com.home.homepage;

import com.home.homepage.entity.AppSet;
import com.home.homepage.entity.User;
import com.home.homepage.entity.modal.AppSetListModal;
import com.home.homepage.service.AppSetService;
import com.home.homepage.utils.Core;
import com.home.homepage.utils.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class HomePageApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    AppSetService appSetService;

    @Test
    void pinyin() {

        User user = new User();
        user.setId(1L);
        Core.setUser(user);


        Result result = appSetService.list(new AppSetListModal().setType(3));
        @SuppressWarnings("unchecked")
        List<AppSet> list = (List<AppSet>) result.getData();

        list.forEach(appSet -> {
            String name = appSet.getName();
            String description = appSet.getDescription();
            String pinyin = appSet.generatePinyin();
            System.out.printf("pinyin: %s\nname=%s\ndes=%s\n", pinyin,  name, description);

//            appSet.setPinyin(pinyin);
//            appSetService.save(appSet);
        });
    }

}
