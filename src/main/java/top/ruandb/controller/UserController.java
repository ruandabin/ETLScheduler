package top.ruandb.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.winning.jcpt.sdk.common.util.Des3;

import top.ruandb.entity.SkResult;
import top.ruandb.entity.User;
import top.ruandb.utils.StringUtils;

@Controller
public class UserController {

	@RequestMapping("/user/login")
	@ResponseBody
	public Map<String,Object> login(String userName,String password,HttpServletRequest request,RedirectAttributes attr){
		Map<String, Object> resultMap = new HashMap<>();
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(userName,StringUtils.md5(password,"winning"));
		try {
			subject.login(token); // 登录验证
			resultMap.put("success", true);
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", "1002");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorInfo", "用户名或密码错误！");
			resultMap.put("success", false);
		}
		return resultMap ;
	}
	
	@RequestMapping("/userLoginT")
	//public SkResult userLogin(@RequestBody User user) {
	public String userLoginT(String userName,String password,HttpServletRequest request) {
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("1002",StringUtils.md5("123456","winning"));
		
		try {
			subject.login(token); // 登录验证
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  "admin/main" ;
	}
	
	@RequestMapping("/jcfw/v1.0/services/permissions/userLogin")
	//public SkResult userLogin(@RequestBody User user) {
	public String userLogin(String sec) {
		SkResult skResult = new SkResult();
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken("1002",StringUtils.md5("123456","winning"));
		
		try {
			subject.login(token); // 登录验证
			skResult.setRepMsg("用户登录成功");
			skResult.setStatuscode(200);
			String ryxm = Des3Decode(sec);
			SecurityUtils.getSubject().getSession().setAttribute("currentUser", ryxm);
		} catch (Exception e) {
			e.printStackTrace();
			skResult.setRepMsg("用户名或密码错误");
			skResult.setStatuscode(500);
			
		}
		return  "admin/main" ;
	}
	
	
//	public static void main(String[] args) {
//		String sec = "Ak6o38Lph7EsjgIP06IvxhHgPaR4bxA2HanMzPNoaUxdph0XiiUw49YmIIh9+u76mqs1Yoe2cdzoYqv9v8I2CxnN1JZWz0qPBjso%2BqPkfTrnNJi4JuCvWH46S+pbYrM%2BnQmIVUNAs89eRSWFX80P7hVgPR3ykJ98LhlTEzOx6d0secm62hDd8x+BUhTiF8l2KBVjoh3opXUhijEQQvvekS4lhGGgyTe0TFPJfCbU75aFxwt7XfE+ViwAmtC7XMQN%2BrImoYVdUNmDJPb1We5wM2gLy8z6ZsyJWprk";
//		try {
//			String result=Des3.decode(URLDecoder.decode(sec, "UTF-8"));
//			System.out.println(URLDecoder.decode(sec, "UTF-8"));
//			String[] results = result.split("&");
//			
//			for(String str : results) {
//				if(str.startsWith("ryxm")) {
//					System.out.println(str.split("=")[1]);
//				}	
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private String Des3Decode(String sec) {
		String resultStr = null;
		try {
			String result=Des3.decode(sec);
			String[] results = result.split("&");
			
			for(String str : results) {
				if(str.startsWith("ryxm")) {
					resultStr = str.split("=")[1];
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;
	}
	
	
}
