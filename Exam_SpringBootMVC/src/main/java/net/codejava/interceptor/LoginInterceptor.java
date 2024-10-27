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
        
        if (request.getRequestURI().startsWith("/admin")) {
            HttpSession session = request.getSession(false);
            Employees employee = (session != null) ? (Employees) session.getAttribute("employee") : null;

            if (employee != null) {
                return true; 
            }

           
            response.sendRedirect("/admin/login");
            return false;
        }

        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
    }
}
