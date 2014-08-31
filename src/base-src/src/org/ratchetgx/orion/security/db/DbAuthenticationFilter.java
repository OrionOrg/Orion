/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ratchetgx.orion.security.db;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.security.mixjaccount.MixJAccountConstants;
import org.ratchetgx.orion.security.mixjaccount.MixJaccountAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 
 * @author hrfan
 */
public class DbAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	public static String DB_IDENTIFIER = "_db_identifier";
	
	private String loginUrl;

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	@Autowired
	private DbUtil dbUtil;

	public DbAuthenticationFilter() {
		super("/j_spring_db_security_check");
		setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler());
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {

		final String username = (String) request.getParameter("j_username");
		final String password = (String) request.getParameter("j_password");

		final String[] identifier = {DbAuthenticationFilter.DB_IDENTIFIER};
		
		  
		identifier[0] = "";

		String sql = "SELECT * FROM ss_user WHERE bh = ? AND password = ?";

		try {
			dbUtil.execute(sql, new IPreparedResultSetProcessor() {

				public void processResultSet(ResultSet rs)
						throws SQLException {
					if (rs.next()) {
						identifier[0] = DbAuthenticationFilter.DB_IDENTIFIER;
					}
				}

				public void processPreparedStatement(PreparedStatement pstmt)
						throws SQLException {
					pstmt.setString(1, username);
					pstmt.setString(2, password);
				}
			});
		} catch (SQLException e) {
			log.error("", e);
		}

		if ("".equals(identifier[0])) {
			sql = "SELECT * FROM ss_v_user t WHERE bh = ? and exists (select * from ss_v_user_password p where lower(p.pwdtype)='md5' and p.bh = t.bh and p.PASSWORD = md5(?))";

			try {
				dbUtil.execute(sql, new IPreparedResultSetProcessor() {

					public void processResultSet(ResultSet rs)
							throws SQLException {
						if (rs.next()) {
							identifier[0] = DbAuthenticationFilter.DB_IDENTIFIER;
						}
					}

					public void processPreparedStatement(
							PreparedStatement pstmt) throws SQLException {
						pstmt.setString(1, username);
						pstmt.setString(2, password);
					}
				});
			} catch (SQLException e) {
				log.error("", e);
			}
		}
			
		if ("".equals(identifier[0])) {
			sql = "SELECT * FROM ss_v_user t WHERE bh = ? and exists (select * from ss_v_user_password p where lower(p.pwdtype)='plain' and p.bh = t.bh and p.PASSWORD = ?)";

			try {
				dbUtil.execute(sql, new IPreparedResultSetProcessor() {

					public void processResultSet(ResultSet rs)
							throws SQLException {
						if (rs.next()) {
							identifier[0] = DbAuthenticationFilter.DB_IDENTIFIER;
						}
					}

					public void processPreparedStatement(
							PreparedStatement pstmt) throws SQLException {
						pstmt.setString(1, username);
						pstmt.setString(2, password);
					}
				});
			} catch (SQLException e) {
				log.error("", e);
			}
		}
	    
		//如果验证失败则抛出异常
		if (identifier[0].length() == 0) {
			throw new DbAuthenticationException("认证失败。");
		}		
        
		//凭证
		UsernamePasswordAuthenticationToken authRequest;
		 
	    authRequest = new UsernamePasswordAuthenticationToken(
					DB_IDENTIFIER, new String[] { username, password });
		 

		authRequest.setDetails(authenticationDetailsSource
				.buildDetails(request));

		request.getSession().setAttribute(DbConstants.DB_SESSION_LOGINED,true);
		
		return this.getAuthenticationManager().authenticate(authRequest); 
	} 
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
}
