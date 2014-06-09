package org.ratchetgx.projectname.module.zzfw.framework.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.ratchetgx.projectname.module.zzfw.framework.dao.FwxmDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FwxmService {
	  private Logger log = LoggerFactory.getLogger(this.getClass());
	  @Autowired
	  private FwxmDao fwxmDao;
	    
	/**
	 * 初始化服务项目
	 * @param fwdlId
	 * @param ffwxmId
	 * @return
	 * @throws Exception
	 */
	public List<Map> initFwxm(String ffwxmId) throws Exception{			 
		List<Map> 	fwxmList =(List<Map>) fwxmDao.initFwxm(ffwxmId);	
		//表格界面排序，根据课程类别代码排序
        Collections.sort(fwxmList, new Comparator(){
           public int compare(Object o1, Object o2) {
               // TODO Auto-generated method stub
               Map m1 = (Map) o1;
               Map m2 = (Map) o2;

               Integer px1 = (Integer) m1.get("px");
               Integer px2 = (Integer) m2.get("px");

               if (px1 == null || px2 == null) {
                   return 0;
               }

               return px1.compareTo(px2);
           }
       });
		return fwxmList;
	}
	
	
	/**
	 * 获取服务大类信息
	 * @param fwdlId
	 * @return
	 * @throws Exception
	 */
	public boolean isSubitem(String fwxmid) throws Exception{			 
		boolean	isSubitem =fwxmDao.isGotoSubitem(fwxmid);			
		return isSubitem;
	}
	
	/**
	 * 获取前置条件信息
	 * @param Fwxmid
	 * @return
	 * @throws Exception
	 */
	public String doOnClick(String fwxmid) throws Exception{			 
		String  doOnClickJsonDtr =fwxmDao.doOnClick(fwxmid);		
		return doOnClickJsonDtr;
	}
}
