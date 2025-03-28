package com.example.taskListManager.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customerOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager API")
                        .version("1.0")
                        .description("Documentation API for manage task")
                        .contact(new Contact()
                                .name("Alexey")
                                .email("osipovalexey123@mail.ru")
                                .url("https://github.com/alexey1110/My-Projects/tree/main")));
    }
}
