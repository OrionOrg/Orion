package org.ratchetgx.orion.module.zzfw.framework.web;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.module.zzfw.framework.service.FwdlService;
import org.ratchetgx.orion.module.zzfw.framework.service.LoginIndexService;
import org.ratchetgx.orion.security.SsfwGrantedAuthority;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/zzfw/framework/fwdl")
public class FwdlController {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());     
    @Autowired
	private FwdlService fwdlService;
   
    /**
     * 服务大类显示
     * @param model
     * @param httpRequest
     * @return
     */
	@RequestMapping(value="show")
    public String show(final ModelMap model,HttpServletRequest httpRequest){  
		try{
			//获取服务大类初始化信息
			 List<Map> fwdlList = (List<Map>)fwdlService.initFwdl();
			 model.put("fwdlList", fwdlList); 	 
		}catch (Exception e){
			e.printStackTrace();
		}  
		return "zzfw/framework/fwdlList";
    }
	
	/**
	 * 前置条件判断
	 * @param model
	 * @param httpRequest
	 * @return
	 */
	@RequestMapping(value="doOnclick")
    public void doOnclick(final ModelMap model,HttpServletRequest httpRequest, HttpServletResponse response){  	
		try{
			String fwdlid = httpRequest.getParameter("fwdlid");				 
			String jsonStr =  fwdlService.doOnClick(fwdlid);	 
			JSONObject rev = new JSONObject();
			rev.put("jsonStr",jsonStr);
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());			 	 
		}catch (Exception e){
			e.printStackTrace();
		}  		 
    }
}