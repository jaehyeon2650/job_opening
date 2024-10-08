package project.general_project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.general_project.web.argumentResolver.SecurityLoginArgumentResolver;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor()).order(1).addPathPatterns("/**").excludePathPatterns("/", "/css/**", "/*.ico", "/error", "/login", "/logout", "/join", "/post/*");
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SecurityLoginArgumentResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("classpath:/static/img/");
    }

}
