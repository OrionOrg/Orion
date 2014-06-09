package org.ratchetgx.projectname.module.zzfw.util;

import java.sql.PreparedStatement; 
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap; 
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

@Component 
public class ZzfwLogUtil { 	 
    private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void addLogs(final Map logmap) throws SQLException {   
	    //记录t_zzfw_czrz表日志
		final String logsql = "insert into t_zzfw_czrz(userid,czlx,czms,cssj,sbbh,ip) values(?,?,?,?,?,?)";
		
		jdbcTemplate.execute(logsql, new PreparedStatementCallback<Object>(){
			public Object doInPreparedStatement(PreparedStatement pstmt)
			throws SQLException, DataAccessException {
				String userid = (String) logmap.get("userid");
				String czlx = (String) logmap.get("czlx");
				String czms = (String) logmap.get("czms");
				String cssj = (String) logmap.get("cssj");	
				String sbbh = (String) logmap.get("sbbh");
				String ip = (String) logmap.get("ip");
				pstmt.setString(1, userid);
				pstmt.setString(2, czlx);
				pstmt.setString(3, czms);				 
				pstmt.setString(4, cssj);
				pstmt.setString(5, ip);
				pstmt.setString(6, sbbh);		
				pstmt.executeUpdate();
				return null;
			}
		});
		
   }
}
