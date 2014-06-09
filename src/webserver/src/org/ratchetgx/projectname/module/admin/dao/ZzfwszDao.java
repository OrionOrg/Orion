package org.ratchetgx.projectname.module.admin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.common.util.ProcedureParameterDataType;
import org.ratchetgx.orion.common.util.ProcedureParameterDirection;
import org.ratchetgx.orion.common.util.ProcedureParameterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

@Repository
public class ZzfwszDao {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());    
    @Autowired
    private DbUtil dbUtil;     
    @Autowired
	private JdbcTemplate jdbcTemplate;   
    @Autowired
    private BizobjUtil bizobjUtil;
    
	/**
	 * 根据条件获得自助服务项目二级项目信息
	 * @return
	 */
	public List<Map> getTreeNodeInfo(final String fwxmid) {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_fwxm where ffwxmid = ? order by px"; 
    	log.debug("dao层 ：fwxmid = "+fwxmid);
    	log.debug("querySql =" +querySql);
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  
    	return treeInfoList;
	}
	/**
	 * 获得自助服务项目所有父项目信息
	 * @return
	 */
	public List<Map> getTreeInfo() {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT fwxmid,fwxmmc,nvl(ffwxmid,'0') as ffwxmid,clcx,px,icon,cjsj,sfqy,bz,wid,fwlb FROM t_zzfw_fwxm order by ffwxmid desc,fwlb,px";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	/**
	 * 增加或者更新服务项目信息
	 * @param data
	 * @throws SQLException
	 */
	public void saveFwxm(Map data) throws SQLException {
		
		String wid=(String)data.get("wid");
		log.debug("-------wid:"+wid);
		if (isNewRecord(wid)) {
			bizobjUtil.insert("T_ZZFW_FWXM", data);
		}else{
			log.debug(data.toString());
			bizobjUtil.update("T_ZZFW_FWXM", wid, data);
		}
	}
	
	/**
	 * 增加或更新收费标准信息
	 * @param data
	 * @throws SQLException
	 */
	public void saveSfbz(Map data) throws SQLException {
		
		String wid=(String)data.get("wid");
		log.debug("--------------wid:"+wid);
		if (isNewSfbz(wid)) {
			bizobjUtil.save("T_ZZFW_ZZDY_DYSFBZ", data);
		}else{
			log.debug(data.toString());
			bizobjUtil.update("T_ZZFW_ZZDY_DYSFBZ", wid, data);
		}
	}
	/**
	 * 判断服务项目是否存在
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public boolean isNewRecord(final String wid) throws SQLException{
    	boolean isSucess=true;
    	final List<Map> fwxmList = new ArrayList<Map>();
    	String sql = " select *  FROM T_ZZFW_FWXM WHERE wid=? ";
        dbUtil.execute(sql, new IPreparedResultSetProcessor() {
		   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
		       pstmt.setString(1, wid);
		   }
		   public void processResultSet(ResultSet rs) throws SQLException {
			   fwxmList.addAll(dbUtil.resultSet2List(rs));		        
		   }
		});
       
       if(fwxmList.size()>0){
    	   log.debug("--------------");
    	   isSucess = false;
       }  
       log.debug("--------------是否新纪录:"+isSucess);
       return isSucess;    	
	}
	
	/**
	 * 判断是否是新的收费标准
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public boolean isNewSfbz(final String wid) throws SQLException{
    	boolean isSucess=true;
    	final List<Map> fwxmList = new ArrayList<Map>();
    	String sql = " select *  FROM T_ZZFW_ZZDY_DYSFBZ WHERE wid=? ";
    	log.debug("--------------wid:"+wid);
        dbUtil.execute(sql, new IPreparedResultSetProcessor() {
		   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
		       pstmt.setString(1, wid);
		   }
		   public void processResultSet(ResultSet rs) throws SQLException {
			   fwxmList.addAll(dbUtil.resultSet2List(rs));		        
		   }
		});
       
       if(fwxmList.size()>0){
    	   log.debug("-------");
    	   isSucess = false;
       }  
       
       return isSucess;    	
	}
	/**
	 * 增加或者更新系统参数信息
	 * @param data
	 * @throws SQLException
	 */
	public int saveXtcs(Map data) throws SQLException {
		
		String csbs = data.get("csbs").toString();
		String csz = data.get("csz").toString();
		String cssm = data.get("cssm").toString();
		log.debug("xtcsdata:"+data.toString());
		String sqlStr = "insert into T_ZZFW_XTCS  VALUES (?,?,?)";		
		int count = 0;
		try {
			count = jdbcTemplate.update(sqlStr, new Object[] {csbs,csz,cssm});
		} catch (Exception e) {
			log.error("增加系统参数异常。异常信息 :"+e.getMessage());
			return count;
		}
		
		return count;
		
	}
	/**
	 * 是否是新的系统参数
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public boolean isNewXtcs(final String wid) throws SQLException{
    	boolean isSucess=true;
    	final List<Map> fwxmList = new ArrayList<Map>();
    	String sql = " select *  FROM T_ZZFW_FWXM WHERE fwxmid=? ";
    	log.debug("------------------------------fwxmid:"+wid);
        dbUtil.execute(sql, new IPreparedResultSetProcessor() {
		   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
		       pstmt.setString(1, wid);
		   }
		   public void processResultSet(ResultSet rs) throws SQLException {
			   fwxmList.addAll(dbUtil.resultSet2List(rs));		        
		   }
		});
       
       if(fwxmList.size()>0){
    	   log.debug("------------------------------");
    	   isSucess = false;
       }  
       
       return isSucess;    	
	}
	
	/**
	 * 增加或者更新错误字典信息
	 * @param data
	 * @throws SQLException
	 */
	public int saveCwzd(Map data) throws SQLException {
		
		String errorCode = data.get("errorcode").toString();
		String errorMessage = data.get("errormessage").toString();
		log.debug("cwzddata:"+data.toString());
		String sqlStr = "insert into T_ZZFW_CWZD  VALUES (?,?)";		
		int count = 0;
		try {
			count = jdbcTemplate.update(sqlStr, new Object[] {errorCode,errorMessage});
		} catch (Exception e) {
			log.error("增加错误字典异常。异常信息 :"+e.getMessage());
			return count;
		}
		
		return count;
	}
	
	/**
	 * 是否是新的错误字典
	 * @param wid
	 * @return
	 * @throws SQLException
	 */
	public boolean isNewCwzd(final String wid) throws SQLException{
    	boolean isSucess=true;
    	final List<Map> fwxmList = new ArrayList<Map>();
    	String sql = " select *  FROM T_ZZFW_FWXM WHERE fwxmid=? ";
    	log.debug("------------------------------fwxmid:"+wid);
        dbUtil.execute(sql, new IPreparedResultSetProcessor() {
		   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
		       pstmt.setString(1, wid);
		   }
		   public void processResultSet(ResultSet rs) throws SQLException {
			   fwxmList.addAll(dbUtil.resultSet2List(rs));		        
		   }
		});
       
       if(fwxmList.size()>0){
    	   log.debug("------------------------------");
    	   isSucess = false;
       }  
       
       return isSucess;    	
	}
	
	/**
	 * 获得系统参数列表
	 * @return
	 */
	public List<Map> getXtcsInfo() {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_xtcs ";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	/**
	 * 获得系统参数列表
	 * @return
	 */
	public List<Map> getXtcsById(final String csbs) {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_xtcs where csbs = ?";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, csbs);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	/**
	 * @param errorcode
	 * @return
	 */
	public List<Map> getCwzdById(final String errorcode) {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_cwzd where errorcode = ?";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, errorcode);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	/**
	 * 根据FWXMID删除服务项目
	 * @param fwxmid
	 * @return
	 */
	public boolean deleteFwxm(String fwxmId){
		try {
			ProcedureParameterList ppl = new ProcedureParameterList();
			ppl.add(ProcedureParameterDirection.IN,
					ProcedureParameterDataType.STRING, "in_fwxmid", fwxmId);
			ppl.add(ProcedureParameterDirection.OUT,
					ProcedureParameterDataType.STRING, "out_message", null);

			int callReturn = dbUtil
					.executeProcedure("P_ZZFW_ZZDY_DELFWXM", ppl);
			String outMessage = null;
			if (callReturn > 0) {
				outMessage = (String) ppl.getParamterValue(1);
				if ("1".equals(outMessage)) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error("调用存储过程异常！");
			return false;
		}
		return true;
	}
	/**
	 * 根据fwxmid获取收费标准
	 * @param fwxmid
	 * @return
	 */
	public List<Map> getSfbzById(final String fwxmid) {
		
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM T_ZZFW_ZZDY_DYSFBZ where fwxmid = ?";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	
	/**
	 * 更新系统参数
	 * @param data
	 * @return
	 */
	public int updateXtcs(Map data){
		
		String csbs = data.get("csbs").toString();
		String csz = data.get("csz").toString();
		String cssm = data.get("cssm").toString();
		log.debug("xtcsdata - dao:"+data.toString());
		String sqlStr = "update T_ZZFW_XTCS  set csz=?, cssm=? where csbs =?";		
		int count = 0;
		try {
			count = jdbcTemplate.update(sqlStr, new Object[] {csz,cssm,csbs});
		} catch (Exception e) {
			log.error("更新系统参数异常。异常信息 :"+e.getMessage());
			return count;
		}
		
		return count;
		
	}
	public List<Map> getCwzdInfo() {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_cwzd ";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	
	public int updateCwzd(Map data){
		
		String errorCode = data.get("errorcode").toString();
		String errorMessage = data.get("errormessage").toString();
		log.debug("cwzddata:"+data.toString());
		String sqlStr = "update t_zzfw_cwzd  set errormessage = ? where errorcode =?";		
		int count = 0;
		try {
			count = jdbcTemplate.update(sqlStr, new Object[] {errorMessage,errorCode});
		} catch (Exception e) {
			log.error("更新错误字典异常。异常信息 :"+e.getMessage());
			return count;
		}
		
		return count;
		
	}
	
	/**
	 * 根据错误代码删除错误字典信息
	 * @param errorCode
	 * @return
	 */
	public int deleteCwzd(final String errorCode){
		int count = 1;
		String sqlStr = "delete from t_zzfw_cwzd  where errorcode =?";
		try {
	        jdbcTemplate.execute(sqlStr, new PreparedStatementCallback<Object>() {
	            public Object doInPreparedStatement(PreparedStatement pstmt)
	                    throws SQLException, DataAccessException {

	                pstmt.setString(1, errorCode);

	                return pstmt.executeUpdate();
	            }
	        });

		} catch (Exception e) {
			count = 0;
			log.error("删除错误字典异常。异常信息 :"+e.getMessage());
			return count;
		}
		return count;
	}
	
	/**
	 * 根据错误代码删除系统参数信息
	 * @param csbs
	 * @return
	 */
	public int deleteXtcs(String csbs){
		int count = 0;
		String sqlStr = "delete from t_zzfw_xtcs where csbs = ?";
		try {
			count = jdbcTemplate.update(sqlStr, new Object[] {csbs});
		} catch (Exception e) {
			log.error("删除系统参数异常。异常信息 :"+e.getMessage());
			return count;
		}
		return count;
	}
	
	/**
	 * 获得系统日志
	 * @return
	 */
	public List<Map> getXtrzList(final int begin,final int end,final String filter) {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
		log.debug("----"+filter);
    	String querySql = "SELECT * FROM (SELECT a.userid,a.czlx,a.czms,a.cssj,a.sbbh,a.ip,ROWNUM AS RN FROM (SELECT t.* FROM t_zzfw_czrz t left join ss_user u on u.bh = t.userid where 1=1 "
				+ filter
				+ " ORDER BY userid) a WHERE ROWNUM <= ?) WHERE RN > ?";   
   		log.debug("ssss:"+querySql);
    	try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setInt(1, end);
				   pstmt.setInt(2, begin);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	
	/**
	 * 获得系统日志总记录数
	 * @param filter
	 * @return
	 */
	public int getXtrzList(String filter) {
		String querySql = "SELECT count(1) FROM t_zzfw_czrz t left join ss_user u on t.userid = u.bh where 1=1 "
				+ filter;
		int total = 0;
		log.debug("querySql:" + querySql);
		total = jdbcTemplate.queryForInt(querySql);
		return total;
	}
	
	/**
	 * 获得对账信息
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<Map> getDztjList(final int begin,final int end,String filter) {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM (SELECT a.*,ROWNUM AS RN FROM (select t.*,xm.fwxmmc,u.bh,u.name from t_zzfw_zzdy_zzdymx t"
				+ " left join ss_v_user u on t.userid = u.bh  left join t_zzfw_fwxm xm on xm.fwxmid = t.fwxmid where 1=1"
				+ filter
				+ "ORDER BY t.userid) a WHERE ROWNUM <= ?) WHERE RN > ?";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setInt(1, end);
				   pstmt.setInt(2, begin);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	
	/**
	 * 获得系统日志总记录数
	 * @param filter
	 * @return
	 */
	public int getDztjList(String filter) {
		String querySql = "select count(1) from t_zzfw_zzdy_zzdymx t"
			+ " left join ss_v_user u on t.userid = u.bh  left join t_zzfw_fwxm xm on xm.fwxmid = t.fwxmid where 1=1 "+ filter;   
		int total = 0;
		log.debug("querySql:" + querySql);
		total = jdbcTemplate.queryForInt(querySql);
		return total;
	}
	
	/**
	 * 获得用户组列表
	 * @return
	 */
	public List<Map> getUgroupList() {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM ss_role ";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	/**
	 * 根据fwxmid获取该服务项目的用户组列表
	 * @param fwxmid
	 * @return
	 */
	public List<Map> getFwxmGroupList(final String fwxmid) {
		final  List<Map> treeInfoList  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM t_zzfw_yhzdyfwxm where fwxmid = ? ";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, fwxmid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   treeInfoList.addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return treeInfoList;
	}
	public List<Map> getUserInfoById(final String userid){
		
		 final List<Map> userMap  = new ArrayList<Map>();
    	String querySql = "SELECT * FROM ss_user where bh = ? ";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, userid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   userMap .addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}  	
    	return userMap;
		
	}
	/**
	 * 获得打印类型(所有不含子节点的服务项目)
	 * @return
	 */
	public List<Map> getDylx(){
		
		final List<Map> dylxMap  = new ArrayList<Map>();
		String querySql = "select t.fwxmid,t.fwxmmc from t_zzfw_fwxm t where t.ffwxmid is not null and not exists (select * from t_zzfw_fwxm a where a.ffwxmid = t.fwxmid) order by  t.ffwxmid";   
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() { 
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   dylxMap .addAll(dbUtil.resultSet2List(rs));				   
			   }
			});
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return dylxMap;
	}
	/**
	 * 批量保存用户组信息
	 * @param list
	 * @return
	 */
	public boolean batchSaveUgroup(String list,String fwxmId){
//        String sql="INSERT INTO t_zzfw_yhzdyfwxm (ugroupid,fwxmid) VALUES (?,?)";
//        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter(){
//            public void setValues(PreparedStatement ps,int i)throws SQLException
//               {
//                String groupId=groupList.get(i).get("groupId").toString();
//                String fwxmId = groupList.get(i).get("fwxmId").toString();
//                ps.setString(1, groupId);
//                ps.setString(2, fwxmId);
//               }
//               public int getBatchSize()
//               {
//                return groupList.size();
//               }
//        });
        //调用存储过程批量修改用户组信息
        try {
			ProcedureParameterList ppl = new ProcedureParameterList();
			ppl.add(ProcedureParameterDirection.IN,ProcedureParameterDataType.STRING, "in_fwxmid",fwxmId);
			ppl.add(ProcedureParameterDirection.IN,ProcedureParameterDataType.STRING, "in_ugroupid",list);
			ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_message", null);

			int callReturn = dbUtil.executeProcedure("P_ZZFW_ZZDY_SZYHZ", ppl);
			String outMessage = null;
			if (callReturn > 0) {
				outMessage = (String) ppl.getParamterValue(2);
				if ("1".equals(outMessage)) {
					return true;
				}
			}
		} catch (Exception e) {
			log.error("调用存储过程异常！");
			return false;
		}
		return true;
    }
}


