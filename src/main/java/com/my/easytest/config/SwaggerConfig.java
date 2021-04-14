package com.my.easytest.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger文档
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		ParameterBuilder builder = new ParameterBuilder();
		builder.parameterType("header").name("token")
				.description("token值")
				.required(true)
				.modelRef(new ModelRef("string")); // 在swagger里显示header

		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.globalOperationParameters(Lists.newArrayList(builder.build()))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.my.easytest.controller"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("easytest系统")
				.description("easytest-api接口文档")
				.contact(new Contact("G_ALLEN", "", "G_ALLEN@163.com"))
				.version("1.0")
				.build();
	}

}
