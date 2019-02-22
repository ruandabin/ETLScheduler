package top.ruandb.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import top.ruandb.utils.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController {

	@RequestMapping("login")
	@ResponseBody
	public Map<String,Object> login(String userName,String password,HttpServletRequest request,RedirectAttributes attr){
		Map<String, Object> resultMap = new HashMap<>();
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(userName,StringUtils.md5(password,"winning"));
		try {
			subject.login(token); // 登录验证
			resultMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorInfo", "用户名或密码错误！");
			resultMap.put("success", false);
		}
		return resultMap ;
	}
	
	
}
