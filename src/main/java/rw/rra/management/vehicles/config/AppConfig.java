package rw.rra.management.vehicles.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rw.rra.management.vehicles.utils.Utility;

@Configuration
public class AppConfig {

    @Bean
    public String someString() {
        return "YourFixedStringValue";
    }
    @Bean
    public Utility utility() {
        return new Utility();
    }
}
