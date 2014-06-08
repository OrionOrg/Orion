package org.ratchetgx.orion.module.admin.service;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.module.admin.dao.ZzfwszDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ZzfwszService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ZzfwszDao zzfwszDao;
	
	
	/**
	  * 
	  * @param fwxmId
	  * @return
	  * @throws Exception
	  */
	  public List<Map> getTreeNodeDetail(String nodeId) throws Exception{			 
		  List<Map> TreeInfo = zzfwszDao.getTreeNodeInfo(nodeId);
		  return TreeInfo;
	  }
	  /**
	  * 
	  * @param fwxmId
	  * @return
	  * @throws Exception
	  */
	  public List<Map> getTreeDetail() throws Exception{			 
		  List<Map> TreeInfo = zzfwszDao.getTreeInfo();
		  return TreeInfo;
	  }
	 /**
	 * @param fwxmMap
	 * @return
	 * @throws SQLException
	 */
	  @Transactional
	  public String saveFwxm(Map<String, String> fwxmMap)throws SQLException {
		  
		//保存服务项目信息
//		for (Iterator iter = fwxmMap.keySet().iterator(); iter.hasNext();) {
//			String key = (String)iter.next();
//			log.debug("keykey1111-->:"+key);
//			log.debug("vlaue111-->:"+fwxmMap.get(key));
//		}
		zzfwszDao.saveFwxm(fwxmMap);
		return fwxmMap.get("wid");
	  }
	  
	  /**
	   * 保存服务项目的收费标准信息
	   * @param sfbzMap
	   * @return
	   * @throws SQLException
	   */
	public String saveSfbz(Map<String, String> sfbzMap)throws SQLException {
			zzfwszDao.saveSfbz(sfbzMap);
			return sfbzMap.get("wid");
		  }
	  /**
	   * 保存系统参数信息
	 * @param xtcsMap
	 * @return
	 * @throws SQLException
	 */
	@Transactional
	  public int saveXtcs(Map<String, String> xtcsMap)throws SQLException {
		  
		//保存服务项目信息
		for (Iterator iter = xtcsMap.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			log.debug("keykey1111-->:"+key);
			log.debug("vlaue111-->:"+xtcsMap.get(key));
		}
		 int  count = zzfwszDao.saveXtcs(xtcsMap);
		return count;
	  }
	  @Transactional
	  public int saveCwzd(Map<String, String> cwzdMap)throws SQLException {
		  
		//保存服务项目信息
		for (Iterator iter = cwzdMap.keySet().iterator(); iter.hasNext();) {
			String key = (String)iter.next();
			log.debug("keykey1111-->:"+key);
			log.debug("vlaue111-->:"+cwzdMap.get(key));
		}
		 int  count = zzfwszDao.saveCwzd(cwzdMap);
			return count;
	  }
	  
	  /**
	 * @return
	 * @throws Exception
	 */
	public List<Map> getXtcsList() throws Exception{			 
		  List<Map> TreeInfo = zzfwszDao.getXtcsInfo();
		  return TreeInfo;
	  } 
	
	public int updateXtcs(Map<String, String> xtcsMap) throws Exception{
		return zzfwszDao.updateXtcs(xtcsMap);
	}
	
	public List<Map> getCwzdList() throws Exception{			 
		  List<Map> TreeInfo = zzfwszDao.getCwzdInfo();
		  return TreeInfo;
	  } 
	
	public int updateCwzd(Map<String, String> xtcsMap) throws Exception{
		return zzfwszDao.updateCwzd(xtcsMap);
	}
	
	public List<Map> getXtrzList(int begin,int end,String filter) throws Exception{
		return zzfwszDao.getXtrzList(begin,end,filter);
	}
	public List<Map> getDztjList(int begin,int end,String filter) throws Exception{
		return zzfwszDao.getDztjList(begin,end,filter);
	}
}
