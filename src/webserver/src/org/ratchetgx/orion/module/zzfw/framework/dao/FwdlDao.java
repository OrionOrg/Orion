package org.ratchetgx.orion.module.zzfw.framework.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.SsfwUtilExt;
import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.module.zzfw.interfaces.IFwdl;
import org.ratchetgx.orion.module.zzfw.interfaces.IFwxm;
import org.ratchetgx.orion.security.SsfwGrantedAuthority;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.ui.ModelMap;

/**
*
* @author sfnie
*/
@Repository
public class FwdlDao {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());    
    @Autowired
    private DbUtil dbUtil;     
    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    /**
     * 初始化服务大类列表
     * @param model
     * @param httpRequest
     * @return
     */	       
    public List<Map>  initFwdl() throws Exception{
    	final  List<Map> fwdlList  = new ArrayList<Map>();
    	 
	    	//根据用户获取用户可访问服务大项信息
	    	List userFwdlList = getFwdlListByUser();
			Iterator userFwdlIter = userFwdlList.iterator();		
			while(userFwdlIter.hasNext()){			
			   //获取服务大类id
			   Map userFwdlMap = (Map)userFwdlIter.next();	
			   String fwxmid = (String) userFwdlMap.get("fwxmid");
			   //根据服务大类id获取服务大类详细信息
			   Map fwdlInfoMap = (Map)getFwxmInfo(fwxmid);	
			   //获取服务大类的权限信息
			   String permissionJsonStr = checkPermission(fwxmid);
			   JSONObject permissionJsonObj = new JSONObject(new JSONTokener(permissionJsonStr));
			
			   String permission = permissionJsonObj.getString("permission");
			   String promptMsg = permissionJsonObj.getString("promptMsg");
			   fwdlInfoMap.put("permission", permission);
			   fwdlInfoMap.put("promptMsg", promptMsg);	  
			   
			   fwdlList.add(fwdlInfoMap);
			}
		
    	 
		return fwdlList;	     
    }
    
    /**
     * 根据用户角色获取用户可访问的服务大项信息
     * @return
     */
    public List<Map> getFwdlListByUser(){    	
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
		
    	final  List<Map> fwdlList  = new ArrayList<Map>();
    	final String allRoles = roles;     
    	//根据用户id 获取用户可访问的服务大类
   		String querySql = "SELECT u2fwxm.ugroupid,u2fwxm.fwxmid as fwxmid "
   						+ " FROM  t_Zzfw_Yhzdyfwxm u2fwxm "
   						+ " LEFT JOIN  t_zzfw_fwxm fwxm ON u2fwxm.fwxmid = fwxm.fwxmid "
                        + " where u2fwxm.ugroupid in("+allRoles+")  and fwxm.ffwxmid IS NULL ";
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				 
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   fwdlList.addAll(dbUtil.resultSet2List(rs));	
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return fwdlList;
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