package org.ratchetgx.orion.common.web;
import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;


public class Log4jInit extends HttpServlet {

    public void init() throws ServletException {
        
        String webAppRoot = getServletContext().getRealPath("/");
        System.setProperty ("webAppRoot", webAppRoot);
        
        String log4jConfigFilePath = webAppRoot + "/WEB-INF/classes/log4j.xml";
        System.out.println("log4j配置文件路径为：" + log4jConfigFilePath);
		File log4jFile = new File(log4jConfigFilePath);
		if (log4jFile.exists()) {
			System.out.println("日志配置文件存在!");
			DOMConfigurator.configure(log4jConfigFilePath);
		} else {
			System.out.println("警告：日志配置文件不存在，请检查${WebAppRoot}/WEB-INF/classes/log4j.xml文件!");
		}

    }


}
