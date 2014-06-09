package org.ratchetgx.projectname.module.zzfw.interfaces.imp.zzdy;

import org.json.JSONObject;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.projectname.module.zzfw.interfaces.IZzdy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class zzdyImp implements IZzdy {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private JdbcTemplate jdbcTemplate;

	private DbUtil dbUtil;

	public int getKmfdyfs(String fwxmid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTotalFee(String userid, String fwxmid, int dyfs)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setDbUtil(DbUtil dbUtil) {
		this.dbUtil = dbUtil;

	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;

	}

	/* 
	 * 获取项目分项list
	 * (non-Javadoc)
	 * 
	 */
	public String getXmfxList(String userID, String fwxmID) throws Exception {
		/*
		 *  根据接口获取项目分项的list。存在以下三种情况：
		 *  1、接口返回 null,则说明该服务项目不需要展示项目分项，界面继续走正常流程。
		 *  2、接口返回空的json数组，则说明该服务项目的项目分项为空，界面需给出提示。
		 *  3、接口返回非空json格式数组，则需要返回该格式的字符串，并跳转到展示界面。
		 */
		JSONObject rev = new JSONObject();
		
		/* 
		 * 根据接口，获得项目分项的信息
		 * # 输入：在URL中应设置“学号”输入参数供自助打印系统传入实际学号；URL形如:http://xxxx/xxxxx?...&xh=11023345....
		 * # 输出：返回的内容为JSON格式的字符串，格式如下：
         *      {‘xmfxList’:[{ ‘fxmc’:’2011年上半年英语四级成绩’,’bburl’:’http://…’},{‘fxmc’:’2012年上半年英语四级成绩’ ,’bburl’:’http://…’},…]}
		 */
		String tmpStr = "{'xmfxList':[{'fxmc':'2011年上半年英语四级成绩','bburl':'http://...'},{'fxmc':'2012年上半年英语四级成绩' ,'bburl':'http://..'}]}";
		
		
		
		return tmpStr;
	}

}
