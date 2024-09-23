package net.codejava.interceptor;

import net.codejava.model.Employees;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Employees employee = (session != null) ? (Employees) session.getAttribute("employee") : null;
        
        if (employee != null) {
            return true; // Người dùng đã đăng nhập
        }
        
        // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        response.sendRedirect("/admin/login");
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