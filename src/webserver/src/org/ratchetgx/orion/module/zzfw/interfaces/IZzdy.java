package org.ratchetgx.orion.module.zzfw.interfaces;
import org.ratchetgx.orion.common.util.DbUtil;
import org.springframework.jdbc.core.JdbcTemplate;

public interface IZzdy {
	/**
	 * 获取可免费打印分数
	 * @param fwxmid
	 * @return
	 * @throws Exception
	 */
	public  int  getKmfdyfs (String fwxmid) throws Exception;
	/**
	 * 获取总费用
	 * @param userid
	 * @param fwxmid
	 * @param dyfs
	 * @return
	 * @throws Exception
	 */
	public  double  getTotalFee (String userid,String fwxmid,int  dyfs) throws Exception;
	
	/**
	 * 获取项目分项list
	 * @param userID
	 * @param fwxmID
	 * @return
	 * @throws Exception
	 */
	public String getXmfxList(String userID,String fwxmID) throws Exception;
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
