package org.ratchetgx.projectname.module.zzfw.interfaces;

import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.springframework.jdbc.core.JdbcTemplate;

public interface IFwdl {
	/**
	 * 检查用户权限，特殊处理逻辑
	 * @param userDetails	 
	 * @param fwdlid
	 * @return 用户权限信息(JSON格式)
	 * @throws Exception
	 */
	public String checkPremission(String fwdlid,String userID) throws Exception;
	
	/**
	 * 检查用户权限,根据存储过程判断
	 * @param fwdlid
	 * @param userID
	 * @param procedureName
	 * @return
	 * @throws Exception
	 */
	public String checkPremission(String fwdlid,String userID,String procedureName) throws Exception;
	
	
	/**
	 * 点击前置校验
	 * @param userDetails	 
	 * @param fwdlid
	 * @return 前置条件信息(JSON格式)
	 * @throws Exception
	 */
	public String doOnClick(String fwdlid,String userID) throws Exception;
	
	/**
	 * 点击前置校验
	 * @param fwdlid
	 * @param userID
	 * @param procedureName
	 * @return
	 * @throws Exception
	 */
	public String doOnClick(String fwdlid,String userID,String procedureName) throws Exception;
	
	/**
	 * 
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate);
	
	/**
	 * @param dbUtil
	 */
	public void setDbUtil(DbUtil dbUtil);
}
