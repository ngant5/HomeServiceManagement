package net.codejava.interceptor;

import net.codejava.model.Customers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CustomerInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CustomerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Customers customer = (session != null) ? (Customers) session.getAttribute("customer") : null;

        logger.info("Checking if customer is logged in...");

        
        if (request.getRequestURI().startsWith("/customer/cus_mypage")) {
            if (customer != null) {
                logger.info("Customer is logged in: " + customer.getEmail());
                return true; 
            }

            logger.warn("Customer not logged in, redirecting to login page. Requested URI: " + request.getRequestURI());
            response.sendRedirect("/customer/login");
            return false;

        }

        return true; 
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
        logger.info("Post handle for request: " + request.getRequestURI());
        
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       
        logger.info("After completion for request: " + request.getRequestURI());
        if (ex != null) {
            logger.error("Exception occurred: ", ex);
        }
    }
}
