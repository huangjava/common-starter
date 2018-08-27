package com.demo.config;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    public static final String system_API = "system"; //系统相关



    @Bean
    public Docket systemAPI() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(system_API)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.any()) // 对所有api进行监控
                .paths(or(
                        regex("/system/.*"),
                        regex("/demo/.*"),
                        regex("/route/.*"),
                        regex("/security/.*")
                ))
                .build()
                .apiInfo(systemApiInfo());
    }

    private ApiInfo systemApiInfo() {
        ApiInfo iotInfo = new ApiInfo("基卫2.0API",
                "EHR API，提供开放平台相关服务。",
                "1.0",
                "No terms of service",
                "wenfujian@jkzl.com",
                "The Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
        );

        return iotInfo;
    }

    /**
     * 生成html文章专用
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String groupName="openAPI";
       // String groupName="iot";

        URL remoteSwaggerFile = new URL("http://127.0.0.1:8080//v2/api-docs?group="+groupName);
        Path outputFile = Paths.get("open-api/build/"+groupName);

        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .withBasePathPrefix()
                .build();

        Swagger2MarkupConverter converter = Swagger2MarkupConverter.from(remoteSwaggerFile)
                .withConfig(config)
                .build();

        converter.toFile(outputFile);
    }
}