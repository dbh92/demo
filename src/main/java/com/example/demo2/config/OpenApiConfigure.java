package com.example.demo2.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Open Api configuration
 */
@Configuration
public class OpenApiConfigure {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
//                .servers(Lists.newArrayList(
//                        new Server().url("http://localhost:8080")
//                ))
                // info
                .info(new Info().title("Arthralgia Application API")
                        .description("Sample OpenAPI 3.0.\n" +
                                "Note: The user identity is passed to the Header parameter(uid)")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                        .version("1.0.0"));
    }
}
