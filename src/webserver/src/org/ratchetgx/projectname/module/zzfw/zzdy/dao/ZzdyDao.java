package org.ratchetgx.projectname.module.zzfw.zzdy.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.util.AndOrEnum;
import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.Condition;
import org.ratchetgx.orion.common.util.ConditionGroup;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.common.util.RelOperEnum;
import org.ratchetgx.orion.module.common.service.BizobjService;
import org.ratchetgx.projectname.module.zzfw.interfaces.IZzdy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ZzdyDao {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());    
    @Autowired
    private DbUtil dbUtil;     
    @Autowired
	private JdbcTemplate jdbcTemplate;   
    @Autowired
    private BizobjUtil bizobjUtil;   
  
    
    
    /**
     *根据服务项目id，获取服务项目信息
     * @param fwdlid
     * @return
     */
    public Map getFwxmById(final String fwxmid){    	
        final  List<Map> fwxmInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_fwxm where fwxmid=?";   
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
     *根据用户登录信息获取用户基本信息
     * @param fwdlid
     * @return
     */
    public Map getUserDetail(){    	
    	String userId = SsfwUtil.getCurrentBh();   	 
		//待实现，需提供数据
        Map  userInfo = new HashMap();	 	
        userInfo.put("xh", userId);
    	return userInfo;
    }
    
    /**
     *根据服务项目id获取报表信息
     * @param fwdlid
     * @return
     */
    public Map getReportDetail(String fwxmid){    	
    	//获取收费标准信息
    	Map fwxmInfoMap = (Map)getSfbzByFwxmid(fwxmid);
    	//获取报表信息
    	String report = (String)fwxmInfoMap.get("report");
		//待实现，需提供数据
        Map  reportInfo = new HashMap();	
        reportInfo.put("report", report);
    	return reportInfo;
    }
    
    /**
     * 根据项目id获取项目缴费标准信息
     * @param fwdlid
     * @return
     */
    public Map getSfbzByFwxmid(final String fwxmid){    
    	final  List<Map> sfbzInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT fwxmid,price,dcdysx,mfdyfs,sfbzsm,clcx,dyjmc,zysx,report,sfmf FROM t_zzfw_zzdy_dysfbz WHERE fwxmid=?";   		
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   while(rs.next()){
					   Map sfbzMap = new HashMap();
					   sfbzMap.put("fwxmid", rs.getString("fwxmid"));
					   sfbzMap.put("price",  rs.getDouble("price"));
					   sfbzMap.put("dcdysx", rs.getInt("dcdysx"));
					   sfbzMap.put("mfdyfs",  rs.getInt("mfdyfs"));
					   sfbzMap.put("sfbzsm",  rs.getString("sfbzsm"));
					   sfbzMap.put("clcx",  rs.getString("clcx"));
					   sfbzMap.put("dyjmc", rs.getString("dyjmc"));
					   sfbzMap.put("zysx",  rs.getString("zysx"));
					   sfbzMap.put("report",  rs.getString("report"));
					   sfbzMap.put("sfmf",  rs.getString("sfmf"));
					   sfbzInfoList.add(sfbzMap);		
				   }
				   		   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
        Map  sfbzInfo = new HashMap();
		if(sfbzInfoList.size()>0){			
			sfbzInfo = sfbzInfoList.get(0);
		}		
    	return sfbzInfo; 
    }
    
	/**
	 * 获取可免费打印份数
	 * @param fwxmid
	 * @return
	 * @throws Exception
	 */
	public  int  getKmfdyfs (String fwxmid) throws Exception{
		//获取收费标准信息
    	Map fwxmInfoMap = (Map)getSfbzByFwxmid(fwxmid);
    	//获取收费处理程序
    	String clcx = (String) fwxmInfoMap.get("clcx");
    	
    	int kmfdyfs = 0;
		if("".equals(clcx) || clcx == null){    		
			kmfdyfs =getKmfdyfs(fwxmid,"normal");			
    	}else{	
    		clcx = clcx.trim();
			try{
				Class IZzdyClass = Class.forName(clcx);			
				IZzdy zzdy = (IZzdy) IZzdyClass.newInstance();				
				zzdy.setJdbcTemplate(jdbcTemplate);
				zzdy.setDbUtil(dbUtil);
				kmfdyfs = zzdy.getKmfdyfs(fwxmid);
			}catch(ClassNotFoundException e){
				log.debug("没有找到IZzdy的实现类");
				e.printStackTrace();
			}catch (Exception e) {
				log.debug("出现错误!"+e.getMessage());
				e.printStackTrace();
			} 
		}
    	
		return kmfdyfs;		
	}
	
	/**
	 * 获取可免费打印分数
	 * @param fwxmid
	 * @return
	 * @throws Exception
	 */
	public  int  getKmfdyfs (String fwxmid,String flag) throws Exception{		
    	// 学生可免费打印份数
		int kmfdyfs = 0; 		
		
		//获取收费标准信息
    	Map fwxmInfoMap = (Map)getSfbzByFwxmid(fwxmid);
    	if(fwxmInfoMap !=null){    		
    		//项目配置的免费打印份数
        	int pzmffs = 0;
        	Integer mfdyfs =(Integer) fwxmInfoMap.get("mfdyfs");  
        	pzmffs = mfdyfs.valueOf(mfdyfs);   	 

        	//获取学生已免费打印份数
    		String userId = SsfwUtil.getCurrentBh();	
    		String sql="SELECT nvl(SUM(dyfs),0) AS ydyfs from t_zzfw_zzdy_zzdymx WHERE userid=? AND fwxmid=? AND dycgbs ='1'";
    		//获取打印份数
    		int ydyfs = jdbcTemplate.queryForInt(sql,new Object[]{userId,fwxmid});

    		
    		kmfdyfs = pzmffs - ydyfs;
        	if(kmfdyfs < 0){    		
        		kmfdyfs = 0;
        	}
    	}    	
		return kmfdyfs;		
	}
	
	/**
	 * 获取总费用
	 * @param userid
	 * @param fwxmid
	 * @param dyfs
	 * @return
	 * @throws Exception
	 */
	public  double  getTotalFee (String fwxmid,int  dyfs) throws Exception{		
		String userid = SsfwUtil.getCurrentBh(); 
		double totalFee = 0;
		//检查是否免费
		//if(checkFree(fwxmid)){    		
		//	return totalFee;
		//}
		
		//获取收费标准信息
    	Map fwxmInfoMap = (Map)getSfbzByFwxmid(fwxmid);
    	//获取收费处理程序
    	String clcx = (String) fwxmInfoMap.get("clcx");
		if("".equals(clcx) || clcx == null){    		
			totalFee =getTotalFee(userid,fwxmid,dyfs,"normal");
    	}else{		
    		clcx = clcx.trim();
			try{
				Class IZzdyClass = Class.forName(clcx);			
				IZzdy zzdy = (IZzdy) IZzdyClass.newInstance();				
				zzdy.setJdbcTemplate(jdbcTemplate);
				zzdy.setDbUtil(dbUtil);
				totalFee = zzdy.getTotalFee(userid, fwxmid, dyfs);
			}catch(ClassNotFoundException e){
				log.debug("没有找到实现类");
				e.printStackTrace();
			}catch (Exception e) {
				log.debug("出现错误!"+e.getMessage());
				e.printStackTrace();
			} 
		}
    	
		return totalFee;
	}
	
	/**
	 * 获取总费用
	 * @param userid
	 * @param fwxmid
	 * @param dyfs
	 * @return
	 * @throws Exception
	 */
	public  double  getTotalFee (String userid,String fwxmid,int dyfs,String flag) throws Exception{
		double totalFee = 0;  
		//if(checkFree(fwxmid)){    		
		//	return totalFee;
		//}
		
		//可免费打印份数
		int kmfcs = getKmfdyfs(fwxmid);	 
	    //选择打印份数
		int xzdyfs = dyfs;
		//获取服务项目配置信息
    	Map fwxmInfoMap = (Map)getSfbzByFwxmid(fwxmid);
    	
    	Double price = (Double)fwxmInfoMap.get("price"); 
		
		double dfjg = price.doubleValue();
    	
    	int jffs = xzdyfs - kmfcs;   //交费份数
		if(jffs < 0){
			jffs = 0;			
		} 
		
    	totalFee = jffs * dfjg;
    	
		return totalFee;
	}
	
	
	/**
	 * 获取总费用
	 * @param userid
	 * @param fwxmid
	 * @param dyfs
	 * @return
	 * @throws Exception
	 */
	public  int  getMffs(String fwxmid,int dyfs) throws Exception{
		//可免费打印份数
		int kmfcs = getKmfdyfs(fwxmid);	 
	    //选择打印份数
		int xzdyfs = dyfs;
		
		//本次打印的免费份数计算
		int curMffs = 0;
		
		if(kmfcs >= xzdyfs){
			curMffs = xzdyfs;			
		}else{
			
			curMffs = kmfcs;
		}
    	
		return curMffs;
	}
	
	/**
	 * 校验服务项目是否免费打印
	 * @param fwxmid
	 * @return
	 */
	public boolean checkFree(String fwxmid){		
		//获取服务项目配置信息
    	Map fwxmInfoMap = (Map)getSfbzByFwxmid(fwxmid);
    	
    	//判断项目是否免费打印
    	String sfmfsz =(String)fwxmInfoMap.get("sfmf");
    	
    	//如果没有配置是否免费信息，则默认不免费打印，需要计算总费用
    	if(sfmfsz ==null || "".equals(sfmfsz)){    		
    		sfmfsz = "0";
    	}    	

    	if("0".equals(sfmfsz)){    		
    		return false;
    	}
    	
    	return true;
	}
	
	
	/**
	 * 获取学生预交费打印名单信息
	 * @param fwxmid
	 * @param userid
	 * @return
	 */
	public Map getYjfdyInfo(final String fwxmid,final String userid){
		
		final  List<Map> yjfdyInfoList  = new ArrayList<Map>();
    	String querySql = "select userid,fwxmid,dyfs,price,totalprice,jfsj,bz,sfydy from v_zzfw_zzdy_yhyjfmd WHERE userid =? and fwxmid=? and sfydy='0'";   		
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, userid);
				   pstmt.setString(2, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   while(rs.next()){
					   Map sfbzMap = new HashMap();
					   sfbzMap.put("userid", rs.getString("userid"));
					   sfbzMap.put("fwxmid", rs.getString("fwxmid"));
					   sfbzMap.put("dyfs",   rs.getInt("dyfs"));
					   sfbzMap.put("price",  rs.getDouble("price"));
					   sfbzMap.put("totalprice", rs.getDouble("totalprice"));
					   sfbzMap.put("jfsj",   rs.getString("jfsj"));
					   sfbzMap.put("bz",      rs.getString("bz"));
					   sfbzMap.put("sfydy",   rs.getString("sfydy"));
					   yjfdyInfoList.add(sfbzMap);		
				   }
				   		   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		
        Map  yjfdyInfo = new HashMap();
		if(yjfdyInfoList.size()>0){			
			yjfdyInfo = yjfdyInfoList.get(0);
		}		
    	return yjfdyInfo; 
	}
	
	 public void saveJfmxxx(Map data) throws SQLException {
	        bizobjUtil.save("T_ZZFW_ZZDY_ZZDYMX", data);
	 }
	 
	 /*
	  * 获取打印明细信息
	 */
	public Map getDymxById(String wid) throws SQLException {
        ConditionGroup cg = new ConditionGroup(AndOrEnum.AND);
        cg.addCondition(new Condition("wid", RelOperEnum.EQUAL,wid));
        List<Map> dymxList = bizobjUtil.query("V_ZZFW_ZZDY_DYMX", cg, null);
        
        Iterator itr = dymxList.iterator();
        while (itr.hasNext()) {
            Map dymx = (Map) itr.next();
        	Map<String,String> patterns = new HashMap<String,String>();
            String defaultPattern = "yyyy-MM-dd";
            DbUtil.convertDateToString(dymx, patterns, defaultPattern);
        }
        
        Map dymxMap = new 	HashMap();
        if(dymxList.size()>0){
        	dymxMap = dymxList.get(0);
        	
        }        
        return dymxMap;
	}
	    
	/**
	 * 获取项目分项的list
	 * @param userID
	 * @param fwxmID
	 * @return
	 */
	public String getXmfxList(String userID,String fwxmID)throws Exception{
		Map fwxmInfoMap = getFwxmById(fwxmID);
		// 获取服务项目的处理程序
    	String clcx = (String) fwxmInfoMap.get("clcx");
    	log.debug("clcx="+clcx);
    	String jsonStr =  "";
		if("".equals(clcx) || clcx == null){    		
			log.debug("处理程序为空！");			
    	}else{	
    		clcx = clcx.trim();
    		try{
				if (clcx.startsWith("java:")) {
					//如果是JAVA类实现 
					String classStr = clcx.substring(5,clcx.length());
					Class IZzdyClass = Class.forName(classStr);			
					IZzdy zzdy = (IZzdy) IZzdyClass.newInstance();			
					zzdy.setJdbcTemplate(jdbcTemplate);
					zzdy.setDbUtil(dbUtil);			
					jsonStr = zzdy.getXmfxList(userID, fwxmID);
					
				}else if(clcx.startsWith("procedure:")){
					//如果存储过程实现
					String	procedureName = clcx.substring(10,clcx.length());
					Class IZzdyClass = Class.forName("org.ratchetgx.projectname.module.zzfw.interfaces.imp.zzdy.zzdyImp");
					IZzdy zzdy = (IZzdy) IZzdyClass.newInstance();				
					zzdy.setJdbcTemplate(jdbcTemplate);
					zzdy.setDbUtil(dbUtil);			
					jsonStr = zzdy.getXmfxList(userID, fwxmID);
				}
    		}catch(ClassNotFoundException e){
				log.debug("没有找到IZzdy的实现类");
				e.printStackTrace();
			}catch (Exception e) {
				log.debug("出现错误!"+e.getMessage());
				e.printStackTrace();
			} 
    		
    		
//			try{
//				Class IZzdyClass = Class.forName(clcx);			
//				IZzdy zzdy = (IZzdy) IZzdyClass.newInstance();				
//				zzdy.setJdbcTemplate(jdbcTemplate);
//				zzdy.setDbUtil(dbUtil);
//				jsonStr = zzdy.getXmfxList(userID, fwxmID);
//			}catch(ClassNotFoundException e){
//				log.debug("没有找到IZzdy的实现类");
//				e.printStackTrace();
//			}catch (Exception e) {
//				log.debug("出现错误!"+e.getMessage());
//				e.printStackTrace();
//			} 
		}
		return jsonStr;
	}
	
}
