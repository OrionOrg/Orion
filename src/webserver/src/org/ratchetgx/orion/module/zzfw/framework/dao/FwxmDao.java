package org.ratchetgx.orion.module.zzfw.framework.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.SsfwUtilExt;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.module.zzfw.interfaces.IFwdl;
import org.ratchetgx.orion.module.zzfw.interfaces.IFwxm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
*
* @author sfnie
*/
@Repository
public class FwxmDao {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());    
    @Autowired
    private DbUtil dbUtil;     
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    /**
     * 初始化服务项目
     * @param fwdlId
     * @param ffwxmId
     * @return
     * @throws Exception
     */  
    public List<Map>  initFwxm(String ffwxmId) throws Exception{
    	final  List<Map> fwxmList  = new ArrayList<Map>();
    	try {
	    	//根据用户获取用户可访问服务项目信息
	    	List userFwxmList = getSFwxListByUser(ffwxmId);
			Iterator userFwxmIter = userFwxmList.iterator();		
			while(userFwxmIter.hasNext()){			
			   //获取服务项目id
			   Map userFwxmMap = (Map)userFwxmIter.next();	
			   String fwxmid = (String) userFwxmMap.get("fwxmid");
			   //根据服务项目id获取服务项目详细信息
			   Map fwxmInfoMap = (Map)getFwxmInfo(fwxmid);	
			   //获取服务项目的权限信息
			   String permissionJsonStr = checkPermission(fwxmid);
			   JSONObject permissionJsonObj = new JSONObject(new JSONTokener(permissionJsonStr));
			   String permission = permissionJsonObj.getString("permission");
			   String promptMsg = permissionJsonObj.getString("promptMsg");
			   fwxmInfoMap.put("permission", permission);
			   fwxmInfoMap.put("promptMsg", promptMsg);	  
			   
			   fwxmList.add(fwxmInfoMap);
			}
		
    	} catch (JSONException e) {
			e.printStackTrace();
		}
		return fwxmList;	     
    }
    
   /**
    * 根据用户角色获取用户可访问的服务项目信息
    * @param fwdlId
    * @param ffwxmId
    * @return
    */
    public List<Map> getSFwxListByUser(final String ffwxmId){    	
    	//获取用户角色组
        List roleList = SsfwUtilExt.getUserRoles();	
        
        //遍历用户角色
		String roles ="";		
		Iterator iter = roleList.iterator();			
		while (iter.hasNext()){			 
			String role = (String)iter.next();				
			if("".equals(roles) ||roles == null){
				roles = roles+"'"+role+"'";				
			}else{
				roles = roles+",'"+role+"'";
			}
		}	
		
    	final  List<Map> fwxmList  = new ArrayList<Map>();
    	final String allRoles = roles;     
    	
    	//根据用户id 获取用户可访问的服务项目
   		String querySql = "SELECT u2fwxm.ugroupid,u2fwxm.fwxmid "
   						+ " FROM  t_Zzfw_Yhzdyfwxm u2fwxm "
   						+ " LEFT JOIN  t_zzfw_fwxm fwxm ON u2fwxm.fwxmid = fwxm.fwxmid "
                        + " where u2fwxm.ugroupid in("+allRoles+")";
   		
   		if(!"".equals(ffwxmId) && ffwxmId !=null ){   			
   			querySql += " AND fwxm.ffwxmid=?";  
   		}else{ 
   			querySql += " and fwxm.ffwxmid IS NULL"; 			 
   		} 	
   		
   		log.debug("getSFwxListByUser:querySql:"+querySql);
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {				 
				   if(!"".equals(ffwxmId) && ffwxmId !=null ){ 		
					   pstmt.setString(1,ffwxmId);
			   	   }	
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   fwxmList.addAll(dbUtil.resultSet2List(rs));	
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return fwxmList;
    }
    
   /**
    * 根据服务项目id获取服务项目详细信息
    * @param fwxmid
    * @return
    */
    public Map getFwxmInfo(final String fwxmid){    	
    	final  List<Map> fwxmInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_fwxm where fwxmid=? order by px";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   fwxmInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
        Map  fwxmInfo = new HashMap();
		if(fwxmInfoList.size()>0){			
			fwxmInfo = fwxmInfoList.get(0);
		}		
    	return fwxmInfo;
    }
    
    
	 /**
	  * 判断是否跳转至子项
	  * @param fwdlid
	  * @return
	  */
	  public boolean isGotoSubitem(final String fwxmid){    	
	 	final  List<Map> fwxmList  = new ArrayList();
	 	String querySql = "SELECT * FROM t_zzfw_fwxm where ffwxmid=?";   		
			try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   fwxmList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
		if(fwxmList.size()>0){			
			return true;
		}		
	 	return false;
	 }
    
    
   /**
    * 检查用户对各个服务项目使用权限，控制界面上的操作
    * @param fwxmid
    * @return
    * @throws Exception
    */
    public String checkPermission(String fwxmid) throws Exception{
    	//获取用户id
    	String loginuserid = SsfwUtil.getCurrentBh();
    	//获取服务大类详细信息
    	Map fwxmInfoMap = (Map)getFwxmInfo(fwxmid);
    	//获取服务大类的处理程序
    	String clcx = (String) fwxmInfoMap.get("clcx");
    	
    	String returnInfo = "";
    	JSONObject object = new JSONObject();  
		object.put("permission", "1");  
		object.put("promptMsg", "");
		returnInfo = object.toString();
		
		if (clcx != null) {
			
			if (clcx.startsWith("java:")) {
				//如果是JAVA类实现 
				String classStr = clcx.substring(5,clcx.length());
				Class IFwxmClass = Class.forName(classStr);			
				IFwxm fwxm = (IFwxm) IFwxmClass.newInstance();				
				fwxm.setJdbcTemplate(jdbcTemplate);
				fwxm.setDbUtil(dbUtil);			
				returnInfo = fwxm.checkPremission(fwxmid,loginuserid);
				
			}else if(clcx.startsWith("procedure:")){
				//如果存储过程实现
				String	procedureName = clcx.substring(10,clcx.length());
				Class IFwxmClass = Class.forName("org.ratchetgx.orion.module.zzfw.interfaces.imp.fwxm.fwxmImp");				
				IFwxm fwxm = (IFwxm) IFwxmClass.newInstance();				
				fwxm.setJdbcTemplate(jdbcTemplate);
				fwxm.setDbUtil(dbUtil);			
				returnInfo = fwxm.checkPremission(fwxmid,loginuserid,procedureName);
			}			 
    	}
		
    	return returnInfo;
	}
    
    /**
     * 获取服务项目点击的前置条件，用于点击判断
     * @param fwxmid
     * @return
     * @throws Exception
     */
    public String  doOnClick(String fwxmid) throws Exception{
    	//获取用户id
    	String loginuserid = SsfwUtil.getCurrentBh();
    	//获取服务大类详细信息
    	Map fwxmInfoMap = (Map)getFwxmInfo(fwxmid);
    	//获取服务大类的处理程序
    	String clcx = (String) fwxmInfoMap.get("clcx");
    	
    	String returnInfo = "";
    	JSONObject object = new JSONObject();  
		object.put("permission", "1");  
		object.put("promptMsg", "");
		returnInfo = object.toString();
		
		if (clcx != null) {
			
			if (clcx.startsWith("java:")){
				//如果是JAVA类实现 
				String classStr = clcx.substring(5,clcx.length());
				Class IFwxmClass = Class.forName(classStr);			
				IFwxm fwxm = (IFwxm) IFwxmClass.newInstance();				
				fwxm.setJdbcTemplate(jdbcTemplate);
				fwxm.setDbUtil(dbUtil);			
				returnInfo = fwxm.checkPremission(fwxmid,loginuserid);
				
			}else if(clcx.startsWith("procedure:")){
				//如果存储过程实现
				String	procedureName = clcx.substring(10,clcx.length());
				Class IFwxmClass = Class.forName("org.ratchetgx.orion.module.zzfw.interfaces.imp.fwxm.fwxmImp");				
				IFwxm fwxm = (IFwxm) IFwxmClass.newInstance();				
				fwxm.setJdbcTemplate(jdbcTemplate);
				fwxm.setDbUtil(dbUtil);			
				returnInfo = fwxm.checkPremission(fwxmid,loginuserid,procedureName);
			}
    	}
		
    	return returnInfo;
  	}	 
}