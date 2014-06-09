package org.ratchetgx.projectname.module.zzfw.zzdy.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ratchetgx.projectname.module.zzfw.util.PdfPosition;
import org.ratchetgx.projectname.module.zzfw.util.PdfUtil;
import org.ratchetgx.projectname.module.zzfw.zzdy.dao.ZzdyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ZzdyService {
	  private Logger log = LoggerFactory.getLogger(this.getClass());
	  @Autowired
	  private ZzdyDao zzdyDao;
	  
	  /**
	  * 获取交费配置信息
	  * @param fwxmId
	  * @return
	  * @throws Exception
	  */
	  public Map getFwxmDetail(String fwxmId) throws Exception{			 
		  Map fwxmInfo = zzdyDao.getFwxmById(fwxmId);
		  return fwxmInfo;
	  }
	    
	 /**
	  * 获取交费配置信息
	  * @param fwxmId
	  * @return
	  * @throws Exception
	  */
	  public Map getJfpzDetail(String fwxmId) throws Exception{			 
		  Map jfpzInfo = zzdyDao.getSfbzByFwxmid(fwxmId);
		  return jfpzInfo;
	  }
	
	
	 /**
	  * 获取免费打印份数
	  * @param fwxmId
	  * @return
	  * @throws Exception
	  */
	  public int getKmfdyfs(String fwxmId) throws Exception{			 
		  int kmfdyfs = zzdyDao.getKmfdyfs(fwxmId);
		  return kmfdyfs;
	  }
	
	  
	  /**
		  * 根据打印份数，计算当前免费打印份数
		  * @param fwxmId
		  * @return
		  * @throws Exception
		  */	  
	  public int  getMffs(String fwxmId,int dyfs) throws Exception{			 
		  int curMfdyfs = zzdyDao.getMffs(fwxmId,dyfs);
		  return curMfdyfs;
	  }
	
	/**
	 * 获取用户信息
	 * @return
	 * @throws Exception
	 */
	  public Map getUserDetail() throws Exception{			 
		  Map	userInfo =(Map)zzdyDao.getUserDetail();		
		  return userInfo;
	  }
	
	/**
	 * 获取报表信息
	 * @param fwxmid
	 * @return
	 * @throws Exception
	 */
	  public String[] getReportDetail(String fwxmid,String userid,String reporturl) throws Exception{			 
		    Map	reportInfo =(Map)zzdyDao.getReportDetail(fwxmid);			  
		    String  urlstr = (String)reportInfo.get("report"); 
		    
		    urlstr = urlstr.replace("@userid", userid);		     
		    
	    	PdfPosition postion = new PdfPosition();
	    	postion.setPdfPath(reporturl+"/"+userid+"/"+fwxmid);
	    	postion.setFileName(userid+"_"+fwxmid);
	    	postion.setImagePath(reporturl+"/"+userid+"/"+fwxmid+"/images");	    	
	    	PdfUtil pd = new PdfUtil();	    	
	        pd.getPdfByUrl(urlstr,postion);
	        String [] images = pd.getImageNames(postion);
	        return images;
	  }	
	  
	 /**
	 * 计算费用总和
	 * @param fwxmid
	 * @return
	 * @throws Exception
	 */
	  public double getTotalFee(String fwxmid,int dyfs) throws Exception{			 
		  double	totalFee =(double)zzdyDao.getTotalFee(fwxmid,dyfs);		
		  return totalFee;
	  }
	  
	  @Transactional
	  public String saveJfmx(Map<String, String> jfmxMap)throws SQLException {
			// 保存基本信息
			for (Iterator iter = jfmxMap.keySet().iterator(); iter.hasNext();) {
				String key = (String)iter.next();
				log.debug("keykey1111-->:"+key);
				log.debug("vlaue111-->:"+jfmxMap.get(key));
			}
			
			zzdyDao.saveJfmxxx(jfmxMap);
			
			return jfmxMap.get("wid");
	  }
	  
	  @Transactional
	  public Map getDymxById(String wid)throws SQLException {
			 
			Map dymxMap = zzdyDao.getDymxById(wid);			 
			
			return dymxMap;
	  }
	  
	/**
	 * 获取项目分项的list
	 * 
	 * @param userID
	 * @param fwxmID
	 * @return
	 * @throws Exception
	 */
	public String getXmfxList(String userID, String fwxmID) throws Exception {
		String jsonStr = zzdyDao.getXmfxList(userID, fwxmID);
		return jsonStr;
	}
	  
}
