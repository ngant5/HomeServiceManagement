//package net.codejava.interceptor;
//
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import net.codejava.model.Employees;
//
//public class EmployeeInterceptor implements HandlerInterceptor{
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//    throws Exception{
//    	HttpSession session = request.getSession();
//    	Employees user = (Employees) session.getAttribute("user");
//
//        if (user == null) {
//            response.sendRedirect("/employee/login");
//            return false;
//        }
//           
//        Integer userType = user.getUserType();
//        if (userType != 0) { 
//            response.sendRedirect("/access-denied");
//            return false;
//        }
//        return true;
//    }
//    
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//            ModelAndView modelAndView) throws Exception {
//        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
//    }
//    
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//    throws Exception {
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
//    }
//}
//
