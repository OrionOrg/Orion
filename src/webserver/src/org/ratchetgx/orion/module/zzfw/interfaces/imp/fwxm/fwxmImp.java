package org.ratchetgx.orion.module.zzfw.interfaces.imp.fwxm;

import org.json.JSONObject;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.ProcedureParameterDataType;
import org.ratchetgx.orion.common.util.ProcedureParameterDirection;
import org.ratchetgx.orion.common.util.ProcedureParameterList;
import org.ratchetgx.orion.module.zzfw.interfaces.IFwxm;
import org.ratchetgx.orion.widgets.progressIndicator.INodeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
/**
*实现服务项目接口类
* @author sfnie
*/
public class fwxmImp implements IFwxm{
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private JdbcTemplate jdbcTemplate;
	
	private DbUtil dbUtil;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}	
	
	public void setDbUtil(DbUtil dbUtil) {
		this.dbUtil = dbUtil;
	}
	
	/**
	 * 检查用户权限
	 * @param userDetails	 
	 * @param fwxmid
	 * @return 用户权限信息(JSON格式)
	 * @throws Exception
	 */
	public String checkPremission(String fwxmid,String userID) throws Exception{
		return null;
	}

	/**
	 * 检查用户权限
	 * @param userDetails	 
	 * @param fwxmid
	 * @return 用户权限信息(JSON格式)
	 * @throws Exception
	 */
	public String checkPremission(String fwxmid,String userID,String procedureName) throws Exception{
		StringBuffer returnStr = new StringBuffer();// 返回的字符串
		
		ProcedureParameterList ppl = new ProcedureParameterList();
		ppl.add(ProcedureParameterDirection.IN,ProcedureParameterDataType.STRING, "in_userid", userID);
		ppl.add(ProcedureParameterDirection.IN,ProcedureParameterDataType.STRING, "in_fwxmid", fwxmid);
		ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_permission", null);
		ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_promptMsg", null); 
		ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_message", null);
		
		int callReturn = dbUtil.executeProcedure(procedureName, ppl);
		String outMessage = "";
		String outPermission = "";
		String outPromptMsg = "";
		if(callReturn>0){
			if(ppl.getParamterValue(2)!=null){
				outPermission = (String) ppl.getParamterValue(2);				
			}
			
			if(ppl.getParamterValue(3)!=null){
				outPromptMsg = (String) ppl.getParamterValue(3);
				
			}			
			if(ppl.getParamterValue(4)!=null){
				outMessage = (String) ppl.getParamterValue(4);				
			}
			
		}else {
			log.debug("调用存储过程失败");
	    }	
		
		JSONObject object = new JSONObject();  
		object.put("permission", outPermission);  
		object.put("promptMsg", outPromptMsg);		 
		return object.toString();		
	}
	
	/**
	 * 点击前置校验
	 * @param userDetails	 
	 * @param fwxmid
	 * @return 前置条件信息(JSON格式)
	 * @throws Exception
	 */
	public String doOnClick(String fwxmid,String userID) throws Exception{
		return null;
	}
	/**
	 * 点击前置校验
	 * @param userDetails	 
	 * @param fwxmid
	 * @return 前置条件信息(JSON格式)
	 * @throws Exception
	 */
	public String doOnClick(String fwxmid,String userID,String procedureName) throws Exception{
		
		StringBuffer returnStr = new StringBuffer();// 返回的字符串
		
		ProcedureParameterList ppl = new ProcedureParameterList();
		ppl.add(ProcedureParameterDirection.IN,ProcedureParameterDataType.STRING, "in_userid", userID);
		ppl.add(ProcedureParameterDirection.IN,ProcedureParameterDataType.STRING, "in_fwxmid", fwxmid);
		ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_permission", null);
		ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_promptMsg", null); 
		ppl.add(ProcedureParameterDirection.OUT,ProcedureParameterDataType.STRING, "out_message", null);
		
		int callReturn = dbUtil.executeProcedure(procedureName, ppl);
		String outMessage = "";
		String outPermission = "";
		String outPromptMsg = "";
		if(callReturn>0){
			if(ppl.getParamterValue(2)!=null){
				outPermission = (String) ppl.getParamterValue(2);				
			}
			
			if(ppl.getParamterValue(3)!=null){
				outPromptMsg = (String) ppl.getParamterValue(3);
				
			}			
			if(ppl.getParamterValue(4)!=null){
				outMessage = (String) ppl.getParamterValue(4);				
			}
			
		}else {
			log.debug("调用存储过程失败");
	    }	
		
		JSONObject object = new JSONObject();  
		object.put("permission", outPermission);  
		object.put("promptMsg", outPromptMsg);		 
		return object.toString();		
	}	 
}
