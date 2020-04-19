package com.yorath.booksearch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class OpenApiRestTemplateConfig {

    @Value("${open-api.kakao.domain}")
    private String OPEN_API_KAKAO_DOMAIN;

    @Value("${open-api.naver.domain}")
    private String OPEN_API_NAVER_DOMAIN;

    /**
     * 카카오 오픈API RestTemplate 객체
     * 책검색 API 뿐만이 아니라, 추가 API가 필요한 경우에 참조할 수 있도록 별도 빈으로 등록
     *
     * @param restTemplateBuilder
     * @return
     */
    @Bean("kakaoOpenApiRestTemplate")
    public RestTemplate kakaoOpenApiRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.rootUri(OPEN_API_KAKAO_DOMAIN)
                .setConnectTimeout(Duration.ofSeconds(5))
                .build();
    }

    /**
     * 네이버  오픈API RestTemplate 객체
     * 책검색 API 뿐만이 아니라, 추가 API가 필요한 경우에 참조할 수 있도록 별도 빈으로 등록
     *
     * @param restTemplateBuilder
     * @return
     */
    @Bean("naverOpenApiRestTemplate")
    public RestTemplate naverOpenApiRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.rootUri(OPEN_API_NAVER_DOMAIN)
                .setConnectTimeout(Duration.ofMinutes(10))
                .build();
    }

}
