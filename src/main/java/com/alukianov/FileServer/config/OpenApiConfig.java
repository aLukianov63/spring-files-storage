package com.alukianov.FileServer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${openapi.dev-url}")
    public String devUrl;

    @Bean
    public OpenAPI openApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);

        Contact contact = new Contact();
        contact.setEmail("alukianov@proton.me");
        contact.setName("email");

        Info info = new Info()
                .title("Spring files server")
                .version("1.0")
                .contact(contact)
                .description("Example of file storage REST API, with local machine implementation and S3 bucket");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }

}
