package top.ruandb.filter;
import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

/**
 * 自定义拦截器,Session过期AJAX处理
 * @author rdb
 *
 */
public class LoginFormFilter extends PathMatchingFilter {
	//https://blog.csdn.net/u011919791/article/details/53785330
	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)throws Exception {
		  HttpServletRequest req = (HttpServletRequest) request;
		  HttpServletResponse resp = (HttpServletResponse) response;
		Object obj =  SecurityUtils.getSubject().getSession().getAttribute("currentUser");//req.getSession();
		if (obj == null) {
			// System.out.println("session过期");
			//判断是否ajax请求
			if ("XMLHttpRequest".equalsIgnoreCase((req).getHeader("X-Requested-With"))) {
				//ajax的session处理 返回状态码
				onLoginFail(response);
				
				return false;
			}else {
				//非ajax的session过期，直接到登陆页面
				resp.sendRedirect("/");
				return false;
			}
		}
		return true;
	}

	// session过期给403状态码
	private void onLoginFail(ServletResponse response) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(403);
	}

}
