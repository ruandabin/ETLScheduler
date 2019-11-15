package top.ruandb.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 下载帮助文档
 * @author rdb
 *
 */
@Controller
@RequestMapping("/help")
public class HelpController {
	
	@RequestMapping(value = "/getHelp", produces = "text/html;charset=UTF-8")
	public void getHelp(HttpServletResponse response) throws IOException {
		/*ClassPathResource resource = new ClassPathResource("help.pdf");
		File f = resource.getFile();
		String fileName = f.getName();
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		if (f.exists()) {
			FileInputStream is = new FileInputStream(f);
			byte[] buf = new byte[2048];
			int temp = -1;
			while ((temp = is.read(buf)) != -1) {
				response.getOutputStream().write(buf, 0, temp);
			}
			response.getOutputStream().flush();
			is.close();
		}*/
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("help.pdf", "UTF-8"));
		InputStream is = getClass().getClassLoader().getResourceAsStream("help.pdf");
		byte[] buf = new byte[2048];
		int temp = -1;
		while ((temp = is.read(buf)) != -1) {
			response.getOutputStream().write(buf, 0, temp);
		}
		response.getOutputStream().flush();
		is.close();
	}
}
