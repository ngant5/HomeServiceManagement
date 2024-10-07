package net.codejava.config;

import net.codejava.interceptor.AdminInterceptor;
import net.codejava.interceptor.CustomerInterceptor;
import net.codejava.interceptor.EmployeeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CustomerInterceptor())
            .addPathPatterns("/customer/**")
            .excludePathPatterns(
                "/customer/login",
                "/customer/chklogin",
                "/customer/register",
                "/customer/register/**",
                "/customer/verify",
                "/css/**",
                "/js/**",
                "/images/**",
                "/error"
            );

        registry.addInterceptor(new AdminInterceptor())
            .addPathPatterns("/admin/**")
            .excludePathPatterns(
                "/admin/login",
                "/admin/chklogin",
                "/admin/register",
                "/admin/register/**",
                "/admin/verify"
            );


        registry.addInterceptor(new EmployeeInterceptor())
            .addPathPatterns("/employee/**")
            .excludePathPatterns(
                "/employee/login",
                "/employee/chklogin",
                "/employee/register",
                "/employee/register/**",
                "/employee/verify"
            );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}
