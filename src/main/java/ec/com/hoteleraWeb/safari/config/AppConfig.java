package ec.com.hoteleraWeb.safari.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@Import({ PersistenceConfig.class, SecurityConfig.class })
public class AppConfig {

}