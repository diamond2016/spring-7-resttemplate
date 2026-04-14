package guru.springframework.spring7resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.boot.restclient.autoconfigure.RestTemplateBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {
    
    @Value("${rest.template.rootUrl}") // see application.properties
    private String rootUrl;

    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {

        assert rootUrl != null;
        
        // starts with predefinted defaults, then we can customize it as needed
        RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());
        // set api server (not this spring boot)
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(rootUrl);
        builder = builder.uriTemplateHandler(factory);
        return builder;
    }
}
