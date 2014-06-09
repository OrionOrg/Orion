package org.ratchetgx.projectname.widgets.attachment.impl.yjs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 根据scbz配置信息获取上传配置信息
 * @author sfnie
 *
 */
@Repository
public class GetSysConfitWjsc  {
	/** 日志对象 */
    private Logger log = LoggerFactory.getLogger(this.getClass());
    /** 业务对象 */
    @Autowired
    private BizobjUtil bizobjUtil;
    /** 数据访问对象 */
    @Autowired
    private DbUtil dbUtil;
    
	private List getWjscAllConfig(){
			
		final  List<Map> listWjsc  = new ArrayList();
   		String querySql = "SELECT * FROM T_YJS_CONFIG_WJSCFS";
   		
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   listWjsc.addAll(dbUtil.resultSet2ListToUpperCase(rs));
			   }
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   		
		return listWjsc;
	}
	
	public HashMap getWjscConfig(String SCBS){
		List templist = getWjscAllConfig();
   		for(int i=0;i<templist.size();i++){
   			HashMap hm = new HashMap();
   			hm = (HashMap) templist.get(i);
   			if (hm.get("SCBS").toString().equals(SCBS)){
   				return hm;
   			}
   		}
		return null;
	}
	
	public String getWjscfs(String SCBS){
		List templist = getWjscAllConfig();
   		for(int i=0;i<templist.size();i++){
   			HashMap hm = new HashMap();
   			hm = (HashMap) templist.get(i);
   			if (hm.get("SCBS").toString().equals(SCBS)){
   				return hm.get("SCFS").toString();
   			}
   		}
		return "1";
	}
}
