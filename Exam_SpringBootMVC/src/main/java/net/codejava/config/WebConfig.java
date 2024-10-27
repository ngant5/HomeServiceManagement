package net.codejava.config;

import net.codejava.interceptor.AdminInterceptor;
import net.codejava.interceptor.CustomerInterceptor;
import net.codejava.interceptor.EmployeeInterceptor;
import net.codejava.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor cho Login
        registry.addInterceptor(new LoginInterceptor())
            .excludePathPatterns(
                "/admin/login",
                "/admin/chklogin",
                "/admin/forgot-password",
                "/admin/reset-password",
                "/admin/register",
                "/employees/login",
                "/employees/chklogin",
                "/employees/forgot-password",
                "/employees/reset-password",
                "/customer/login",
                "/customer/chklogin",
                "/customer/forgot-password",
                "/customer/reset-password",
                "/css/**",
                "/js/**",
                "/images/**",
                "/assets_admin/**",
                "/error"
            );

        // Interceptor cho Customer
        registry.addInterceptor(new CustomerInterceptor())
            .addPathPatterns("/customer/**")
            .excludePathPatterns(
                "/customer/login",
                "/customer/chklogin",
                "/customer/forgot-password",
                "/customer/reset-password",
                "/customer/register",
                "/css/**",
                "/js/**",
                "/images/**",
                "/assets/**", 
                "/error"
            );

        // Interceptor cho Admin
        registry.addInterceptor(new AdminInterceptor())
            .addPathPatterns("/admin/**")
            .excludePathPatterns(
                "/admin/login",
                "/admin/chklogin",
                "/admin/forgot-password",
                "/admin/reset-password",
                "/admin/register",
                "/css/**",
                "/js/**",
                "/images/**",
                "/assets_admin/**", 
                "/error"
            );

        // Interceptor cho Employee
        registry.addInterceptor(new EmployeeInterceptor())
            .addPathPatterns("/employees/**")
            .excludePathPatterns(
                "/employees/login",
                "/employees/chklogin",
                "/employees/forgot-password",
                "/employees/reset-password",
                "/employees/register",
                "/css/**",
                "/js/**",
                "/images/**",
                "/assets_admin/**",
                "/error"
            );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("admin/css/**")
            .addResourceLocations("classpath:/static/assets_admin/css/");
        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/assets_admin/js/");
        registry.addResourceHandler("/fonts/**")
            .addResourceLocations("classpath:/static/assets_admin/fonts/");
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:uploads/")
            .setCachePeriod(3600)
            .resourceChain(true);
        
        registry.addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/assets/css/");
    }       
}
