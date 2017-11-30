package my.edu.umk.smart.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@ComponentScan(basePackages = {
        // internals
        "my.edu.umk.smart.api",
})

@Import({
})
@PropertySource("classpath:application.properties")
public class TestApplicationConfig {


}
