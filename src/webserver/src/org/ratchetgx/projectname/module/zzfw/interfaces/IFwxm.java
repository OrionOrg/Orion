package org.ratchetgx.projectname.module.zzfw.interfaces;

import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.springframework.jdbc.core.JdbcTemplate;

public interface IFwxm {

	/**
	 * 检查用户权限，特殊处理逻辑
	 * @param userDetails	 
	 * @param fwxmid
	 * @return 用户权限信息(JSON格式)
	 * @throws Exception
	 */
	public String checkPremission(String fwxmid,String userID) throws Exception;
	
	/**
	 * 检查用户权限,根据存储过程判断
	 * @param fwxmid
	 * @param userID
	 * @param procedureName
	 * @return
	 * @throws Exception
	 */
	public String checkPremission(String fwxmid,String userID,String procedureName) throws Exception;
	
	
	/**
	 * 点击前置校验
	 * @param userDetails	 
	 * @param fwxmid
	 * @return 前置条件信息(JSON格式)
	 * @throws Exception
	 */
	public String doOnClick(String fwxmid,String userID) throws Exception;
	
	/**
	 * 点击前置校验
	 * @param fwxmid
	 * @param userID
	 * @param procedureName
	 * @return
	 * @throws Exception
	 */
	public String doOnClick(String fwxmid,String userID,String procedureName) throws Exception;
	
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
