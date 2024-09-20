package net.codejava.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import net.codejava.inteceptor.AdminInterceptor;
import net.codejava.inteceptor.CustomerInterceptor;
import net.codejava.inteceptor.EmployeeInterceptor;
import net.codejava.inteceptor.LoginInterceptor;

import org.springframework.context.annotation.Configuration;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("Adding interceptors to the registry");

        registry.addInterceptor(new LoginInterceptor())
        				.excludePathPatterns(
        						"/admin/login",
        						"/admin/chklogin",
        						"/employee/login",
        						"/employee/chklogin",
        						"/customer/login",
        						"/customer/chklogin",
        						"/",
        						"/error",
        						"/css/**", 
                                "/js/**", 
                                "/images/**");
        System.out.println("LoginInterceptor registered");

        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login", "/admin/chklogin");
        System.out.println("AdminInterceptor registered");
        
        registry.addInterceptor(new CustomerInterceptor())
        .addPathPatterns("/customer/**")
        .excludePathPatterns("/customer/login", "/customer/chklogin"); 
System.out.println("Customer Interceptor registered");

        registry.addInterceptor(new EmployeeInterceptor())
                .addPathPatterns("/employee/**")
                .excludePathPatterns("/employee/login", "/employee/chklogin"); 
        System.out.println("Employee Interceptor registered");
    }
}
