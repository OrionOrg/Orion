package org.ratchetgx.projectname.module.zzfw.framework.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.ratchetgx.projectname.module.zzfw.framework.dao.FwdlDao;
import org.ratchetgx.projectname.module.zzfw.framework.dao.LoginIndexDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class FwdlService {
	  private Logger log = LoggerFactory.getLogger(this.getClass());
	  @Autowired
	  private FwdlDao fwdlDao;
	    
	/**
	 * 初始化服务大类
	 * @param ssfwUserDetails
	 * @return
	 */
	public List<Map> initFwdl() throws Exception{	
		List<Map> FwdlList =(List<Map>) fwdlDao.initFwdl();			
		return FwdlList;
	}
	
	/**
	 * 获取前置条件信息
	 * @param fwdlid
	 * @return
	 * @throws Exception
	 */
	public String doOnClick(String fwdlid) throws Exception{			 
		String  doOnClickJsonDtr =fwdlDao.doOnClick(fwdlid);		
		return doOnClickJsonDtr;
	}
}
