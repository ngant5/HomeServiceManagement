package net.codejava.interceptor;

import net.codejava.model.Employees;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class EmployeeInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Employees employee = (session != null) ? (Employees) session.getAttribute("employee") : null;
        
        if (employee != null && "EMPLOYEE".equalsIgnoreCase(employee.getUserType())) {
            return true; // Người dùng là admin
        }
        
        // Chuyển hướng đến trang đăng nhập nếu không phải admin
        response.sendRedirect("/employee/login");
        return false;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // Xử lý sau khi controller xử lý nhưng trước khi view được render (tùy chọn)
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Xử lý sau khi view được render (tùy chọn)
    }
}