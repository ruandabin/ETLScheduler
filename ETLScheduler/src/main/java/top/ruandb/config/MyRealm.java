package top.ruandb.config;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class MyRealm extends AuthorizingRealm {

	// 仿数据库中存储的用户密码
	public static String USERNAME = "1002";
	public static String PASSWORD = "53e16556abee1b2a927c838ef63c52ae";

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// TODO Auto-generated method stub
		String userName = (String) token.getPrincipal();
		SecurityUtils.getSubject().getSession().setAttribute("currentUser", USERNAME);
		//SecurityUtils.getSubject().getSession().setTimeout(10000);
		AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(USERNAME, PASSWORD, "xx");
		return authcInfo;
	}
}
