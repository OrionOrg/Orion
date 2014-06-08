package org.ratchetgx.orion.module.zzfw.framework.web;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.module.zzfw.framework.dao.FwxmDao;
import org.ratchetgx.orion.module.zzfw.framework.service.FwdlService;
import org.ratchetgx.orion.module.zzfw.framework.service.FwxmService;
import org.ratchetgx.orion.module.zzfw.util.ZzfwLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/zzfw/framework/fwxm")
public class FwxmController{
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());     
    @Autowired
	private FwxmService fwxmService;
    @Autowired
    private ZzfwLogUtil zzfwLogUtil;
    @Autowired
	private FwxmDao fwxmDao;
   
    /**
     * 获取服务项目信息
     * @param fwdlId
     * @param ffwxmId
     * @param model
     * @param httpRequest
     * @return
     */
	@RequestMapping(value="show")
    public String show(ModelMap model,HttpServletRequest httpRequest){  	 
		HttpSession session = httpRequest.getSession();		
		String ffwxmid = httpRequest.getParameter("ffwxmid");
		try{		
			//获取服务项目初始化信息			 
			 List<Map> fwxmList = (List<Map>)fwxmService.initFwxm(ffwxmid);
			 session.removeAttribute("currentFwxmid");
			 session.removeAttribute("currentFwxmmc");
			 session.removeAttribute("currentNavigation");
			 session.setAttribute("currentNavigation","1");		
			 model.put("fwxmList", fwxmList);			  
		}catch (Exception e){
			e.printStackTrace();
		}  
		
		return "zzfw/framework/fwxmList";
    }
	
	
	/**
	 * 二级项目获取
	 * @param model
	 * @param httpRequest
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value="showejxm")
    public void showejxm(ModelMap model,HttpServletRequest httpRequest, HttpServletResponse response) throws IOException{  	
		String fwxmId = httpRequest.getParameter("fwxmId");	
		JSONObject rev = new JSONObject();		
		try{
			rev.put("success", true);			
			//获取服务项目初始化信息			 
			 List<Map> ejfwxmList = (List<Map>)fwxmService.initFwxm(fwxmId);	            
			 
			 //转化json对象
			 JSONArray jsArray_ejxm = new JSONArray();
			 for (Iterator ejxmIter = ejfwxmList.iterator(); ejxmIter.hasNext();) {
				Map ejxmMap = (Map)ejxmIter.next();
				JSONObject ejxmxx = new JSONObject();
				for (Iterator iter = ejxmMap.keySet().iterator(); iter.hasNext();) {
					String key = (String)iter.next();
					ejxmxx.put(key, ejxmMap.get(key));
				}
				jsArray_ejxm.put(ejxmxx);
			}
				 
			rev.put("ejfwxmList", jsArray_ejxm);
		}catch (Exception e){	 		 
	          try {
				rev.put("success", false);
				rev.put("errorMessage", e.getMessage());
				log.error("", e);
	           } catch (JSONException e1) {
				  // TODO Auto-generated catch block
				  e1.printStackTrace();
	           }
		} 
		
		response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(rev.toString());	
    }
	
	
	/**
	 * 前置条件判断
	 * @param model
	 * @param httpRequest
	 * @return
	 */
	@RequestMapping(value="doOnclick")
    public void doOnclick(HttpServletRequest httpRequest, HttpServletResponse response) throws IOException{  	 	
		JSONObject rev = new JSONObject();		
		
		try{
			String fwxmId = httpRequest.getParameter("fwxmId");	
			String userId = SsfwUtil.getCurrentBh();
			String ip = httpRequest.getRemoteAddr()!=null ? httpRequest.getRemoteAddr():"";    
			rev.put("success", true);			
			//记录日志信息		 
			Map logMap = new HashMap();
			logMap.put("userid",userId);
			logMap.put("czlx","进入服务项目");
		    Map fwxmMap = fwxmDao.getFwxmInfo(fwxmId);
		    String fwxmmc =(String) fwxmMap.get("fwxmmc");
			logMap.put("czms","进入服务项目:"+fwxmmc);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    String time = sdf.format((Date) new Date());			
	    	String cssj = time.toString();	    	 
			logMap.put("cssj",cssj);	
			logMap.put("sbbh",ip);
			logMap.put("ip",ip);					
			zzfwLogUtil.addLogs(logMap);	
			
			String jsonStr =  fwxmService.doOnClick(fwxmId);	 
			rev.put("jsonStr",jsonStr);	
			
		}catch (Exception e){
			 try {
				rev.put("success", false);
				rev.put("errorMessage", e.getMessage());
				log.error("", e);
	           } catch (JSONException e1) {
				  // TODO Auto-generated catch block
				  e1.printStackTrace();
	           }
		}
		
		response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(rev.toString());	
    }
	
	/**
	 * 跳转结果
	 * @param model
	 * @param httpRequest
	 * @return
	 */
	@RequestMapping(value="gotoResult")
    public String gotoResult(final ModelMap model,HttpServletRequest httpRequest, HttpServletResponse response){  	
		 
		 	String fwxmId = httpRequest.getParameter("fwxmId");	
			
			String returnUrl="";
			try {
				boolean isSubitem = fwxmService.isSubitem(fwxmId);
		
				if(isSubitem){
					returnUrl = "redirect:/zzfw/framework/fwxm/show.do?ffwxmId="+fwxmId;   				
				}else{
					 Map fwdlInfo = (Map)fwxmDao.getFwxmInfo(fwxmId);			  
					String fwlb=(String)fwdlInfo.get("fwlb");
					if("100".equals(fwlb)){
						returnUrl = "redirect:/zzfw/zzdy/zzdyIndex.do?1=1&fwxmId="+fwxmId;					
					}
					//后续添加类型				
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}				
	 		 return returnUrl;
    }
}
