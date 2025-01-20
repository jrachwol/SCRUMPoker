package hasebo.scrumpoker.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi apiEndpoints() {
        return GroupedOpenApi.builder()
            .group("api-group")
            .pathsToMatch("/api/**")
            .build();
    }
}