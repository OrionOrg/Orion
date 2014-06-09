package org.ratchetgx.projectname.module.zzfw.framework.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.projectname.module.zzfw.framework.service.LoginIndexService;
import org.ratchetgx.projectname.module.zzfw.util.ZzfwLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/zzfw/framework/login")
public class LoginIndex {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());     
    @Autowired
	private LoginIndexService loginIndexService;
    @Autowired
    private ZzfwLogUtil zzfwLogUtil;
    @Autowired
    private DbUtil dbUtil;
    
    /**
     * 判断自助服务终端登陆成功的跳转
     * @param model
     * @param httpRequest
     * @return
     */
	@RequestMapping(value="index")
    public String loginIndex(final ModelMap model,HttpServletRequest httpRequest){ 
		HttpSession session = httpRequest.getSession();	
		log.debug("-------------300000202-------------------------------");
		String userId = SsfwUtil.getCurrentBh();
		String ip = httpRequest.getRemoteAddr()!=null ? httpRequest.getRemoteAddr():"";      
		//记录日志信息
		try {
			Map logMap = new HashMap();
			logMap.put("userid",userId);
			logMap.put("czlx","登陆");
			logMap.put("czms","用户登录");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time = sdf.format((Date) new Date());			
	    	String cssj = time.toString();
	    	 
			logMap.put("cssj",cssj);	
			logMap.put("sbbh",ip);
			logMap.put("ip",ip);				 
			zzfwLogUtil.addLogs(logMap);
		}catch (SQLException e){
			  e.printStackTrace();
		}
		
		//获取公共配置项目信息 
		Map sysParmMap = loginIndexService.getSystemParameters();
		String loginIndex = (String)sysParmMap.get("LOGININDEX");
		
		//将公共配置信息保存至session 中
		session.removeAttribute("sysParams");
		session.setAttribute("sysParams",sysParmMap);
		
		//获取用户信息保存至session 中
		Map userInfo = loginIndexService.getUserLoginInfo(userId);
		session.removeAttribute("userLoginInfo");
		session.setAttribute("userLoginInfo",userInfo );
		
		//获取导航信息保存至session 中		
		Map  dhxx = new HashMap();
		dhxx.put("1", "选择打印项目");
		dhxx.put("2", "确认内容");
		dhxx.put("3", "完成");		
		session.removeAttribute("zzfwNavigation");
		session.setAttribute("zzfwNavigation",dhxx);
		//定义当前currentNavigation为第一个
		session.setAttribute("currentNavigation","1");
		
		return "redirect:" + loginIndex;
    }
	
	/**
     * 学生照片显示
     * @param model
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="getXsImages")
    public void getXsImages(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws IOException{	  	 
    	//获取session中的系统参数信息
    	HttpSession session = request.getSession();	   
    	final String userId = SsfwUtil.getCurrentBh();
		try {
			final List  listBlob = new ArrayList();
			String querySql = "select zp from V_ZZFW_USER_XS  where userid=? and zp is not null";
			
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   	  pstmt.setString(1, userId);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   if(rs.next()){
						listBlob.add(rs.getBlob(1).getBinaryStream());
					}
			   }
			});
			
			if(listBlob.size()>0){
				InputStream is = (InputStream) listBlob.get(0);
				OutputStream os = null;
				os = response.getOutputStream();
				try {
					byte[] b = new byte[1024];
					int len = 0;
					while ((len = is.read(b, 0, 1024)) > 0) {
						os.write(b, 0, len);
					}
					os.flush();
				}catch(Exception ex){
					ex.printStackTrace();
				} finally {
					is.close();
					os.close();
				}
			}else{

		    	String ctx= session.getServletContext().getRealPath("/");
		    	String sFilePath = ctx+"\\resources\\image\\noPhoto.gif";
		    	log.debug("sFilePath:"+sFilePath);
		    	OutputStream os = null;
				FileInputStream fis = null;
				File file = new File(sFilePath);
				if (file.exists() && file.canRead()) {
					os = response.getOutputStream();
					fis = new FileInputStream(file);
					try {
						byte[] b = new byte[1024];
						int len = 0;
						while ((len = fis.read(b, 0, 1024)) > 0) {
							os.write(b, 0, len);
						}
						os.flush();
					}catch(Exception ex){
						ex.printStackTrace();
					} finally {
						fis.close();
						os.close();
					}
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   		
		
    	
	}
	
}
