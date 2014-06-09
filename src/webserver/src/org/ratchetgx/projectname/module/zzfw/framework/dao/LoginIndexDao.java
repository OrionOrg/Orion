package org.ratchetgx.projectname.module.zzfw.framework.dao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sfnie
 */
@Repository
public class LoginIndexDao {	
	 private Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DbUtil dbUtil;
    
    /**
     * 获取系统公共配置参数信息
     * @return
     */
    public List getSystemParameters(){
		
		final  List<Map> sysParmList  = new ArrayList();
   		String querySql = "SELECT CSBS,CSZ FROM T_ZZFW_XTCS";
   		
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   sysParmList.addAll(dbUtil.resultSet2List(rs));
			   }
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   		
		return sysParmList;
	}   
     
    
    /**
     * 获取用户登陆信息
     * @return
     */
    public Map getUserLoginInfo(final String userid){
		
		final  List<Map> userLoginList  = new ArrayList();
   		String querySql = "SELECT * FROM V_ZZFW_USER_XS where userid=?";
   		
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor(){
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1, userid);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   userLoginList.addAll(dbUtil.resultSet2List(rs));
			   }
			});
		}catch(SQLException e){
			e.printStackTrace();
		}   
		
		Map userLoginInfo = new HashMap();
		if(userLoginList.size()>0){
			userLoginInfo = userLoginList.get(0);
			
		}
		return userLoginInfo;
	}   

}
