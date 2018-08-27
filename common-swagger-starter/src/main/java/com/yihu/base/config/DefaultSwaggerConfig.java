package com.yihu.jw.config;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class DefaultSwaggerConfig {
    public static final String default_API = "default";

    @Bean
    public Docket defaultAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(default_API)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select()
                .paths(or(
                        regex("/.*")
                ))
                .build()
                .apiInfo(defaultApiInfo());
    }

    private ApiInfo defaultApiInfo() {
        ApiInfo apiInfo = new ApiInfo("API",
                "API，提供基础服务。",
                "1.0",
                "No terms of service",
                "wenfujian@jkzl.com",
                "The Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html"
        );

        return apiInfo;
    }
    /**
     * 生成html文章专用
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String groupName="system";
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