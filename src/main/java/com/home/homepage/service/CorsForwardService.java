package com.home.homepage.service;

import com.home.homepage.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CorsForwardService {
    public Result getBing(String keyword) {
        String bingUrl = "https://api.bing.com/qsonhs.aspx?q=";
        String url = bingUrl + keyword;

        log.info("start request: {}", url);
        // 发起请求
        try {
            RestTemplate restTemplate = new RestTemplate();
            String forObject = restTemplate.getForObject(url, String.class);
            log.info("end request, data: {}", forObject);
            return Result.success("ok", forObject);
        } catch (Exception e) {
            log.error("请求" + url + "发生异常", e.getMessage());
            e.printStackTrace();
            return Result.error(500, "转发错误" +  e.getMessage());
        }
    }
}
