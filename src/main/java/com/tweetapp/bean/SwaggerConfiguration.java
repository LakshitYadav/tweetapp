package com.tweetapp.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiDetails())
                .select()
                .paths(PathSelectors.ant("/api/v1.0/tweets/**"))
                .apis(RequestHandlerSelectors.basePackage("com.tweetapp"))
                .build();
    }

    private ApiInfo apiDetails() {
        return new ApiInfoBuilder()
                .title("Tweet Application")
                .description("APIs for Tweet App")
                .termsOfServiceUrl("http://www.cognizant.com")
                .version("1.0")
                .build();
    }
}
