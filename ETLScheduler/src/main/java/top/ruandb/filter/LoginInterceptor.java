package top.ruandb.filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
/**
 * 	拦截期，登陆session过期问题
 * @author rdb
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
        // 从session中获取用户信息
        Object user = session.getAttribute("currentUser");
        System.err.println(request.getHeader("X-Requested-With"));
        // session过期
        if(user == null){ 
        	
            response.sendRedirect("/login.html"); // 通过接口跳转登录页面, 注:重定向后下边的代码还会执行 ;
            return false;
        }else{
        	System.err.println("kkkkkkk");
        	return true;
        }
    }
}
