package org.ratchetgx.projectname.module.zzfw.framework.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ratchetgx.projectname.module.zzfw.framework.dao.LoginIndexDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginIndexService {
	  private Logger log = LoggerFactory.getLogger(this.getClass());
	  @Autowired
	  private LoginIndexDao loginIndexDao;
	    
	  /**
	   * 获取系统参数信息
	   * @return
	   */	  
	  public Map getSystemParameters(){
		  //获取系统参数信息,重新组装成MAP,形式：（参数标识，参数值）
	        List sysParmList = (List) loginIndexDao.getSystemParameters();
	        
	        Iterator<Map> sysParmItr = sysParmList.iterator();        
	        Map sysParmMap = new HashMap();
	        while (sysParmItr.hasNext()) {
	            Map sysParm = sysParmItr.next();  	            
	            sysParmMap.put(sysParm.get("csbs"), sysParm.get("csz"));
	        } 
			return sysParmMap;	 
	  }
	  
	  /**
	   * 获取用户信息
	   * @return
	   */	  
	  public Map getUserLoginInfo(String userid){
		  Map userLoginInfo = (Map) loginIndexDao.getUserLoginInfo(userid);	        
	        
		  return userLoginInfo;	 
	  } 
}
