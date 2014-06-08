/**
 * Xwlwlbxx
 * 自定义学位论文类别级联下拉框选择范围
 */
package org.ratchetgx.orion.widgets.selectrange.impl.yjs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.common.util.IResultSetProcessor;
import org.ratchetgx.orion.widgets.selectrange.ComboboxDefine;
import org.slf4j.LoggerFactory;

/**
 * @author  liuwei
 * @version 1.0
 * @since   2013-03-21
 * @describe 自定义学位论文类别下拉框选择范围
 */
public class Xwlwlbxx implements ComboboxDefine{

	private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
    private Object cascadeValue;
    private String cascade;
    private DbUtil dbUtil;
    
	public Map getData() {
		final Map data = new HashMap();
        try {
            //由cascade是否有值判断是否是级联取值
            if (cascade == null) {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT LWLBID,CASE WHEN XWLWLBMC IS NULL OR XWLWLBMC='' OR XWLWLBMC=' ' THEN '('||LWLBID||')空' ELSE '('||LWLBID||')'||XWLWLBMC END AS XWLWLBMC FROM T_BYXW_LWCJGL_ZYXWLWLB");
                dbUtil.execute(sql.toString(), new IResultSetProcessor() {
                    
                    public void process(ResultSet rs) throws SQLException {
                        while (rs.next()) {
                            data.put(rs.getString(1), rs.getString(2));
                        }
                    }
                });
            } else {
                //支持的级联属性
                if (getSupportedCascades().contains(cascade)) {
                	final String[] count = new String[1];
                	StringBuilder sql1 = new StringBuilder();
                	sql1.append("SELECT COUNT(1) AS COUNT FROM T_BYXW_LWCJGL_ZYXWLWLB WHERE ZYXWLB = ? ");
                	log.debug("cascadeValue====="+cascadeValue);
                	dbUtil.execute(sql1.toString(), new IPreparedResultSetProcessor() {
                        public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
                            pstmt.setObject(1, cascadeValue, Types.VARCHAR);
                        }
                        public void processResultSet(ResultSet rs) throws SQLException {
                        	while (rs.next()) {
                        		count[0] = rs.getString(1);
                            }
                        }
                    });
                	
                	if("0".equals(count[0])){
                		data.put("0", "(0)空");
                	}else{
	                	StringBuilder sql2 = new StringBuilder();
	                    sql2.append("SELECT LWLBID,CASE WHEN XWLWLBMC IS NULL OR XWLWLBMC='' OR XWLWLBMC=' ' THEN '('||LWLBID||')空' ELSE '('||LWLBID||')'||XWLWLBMC END AS XWLWLBMC FROM T_BYXW_LWCJGL_ZYXWLWLB ");
	                    sql2.append("WHERE ZYXWLB = ? ");
	                    
	                    dbUtil.execute(sql2.toString(), new IPreparedResultSetProcessor() {
	                        public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
	                            pstmt.setObject(1, cascadeValue, Types.VARCHAR);
	                        }
	                        public void processResultSet(ResultSet rs) throws SQLException {
	                        	while (rs.next()) {
	                        		data.put(rs.getString(1), (rs.getString(2)== null || "".equals(rs.getString(2)) || " ".equals(rs.getString(2)))?"空":rs.getString(2));
	                            }
	                        }
	                    });
                	}
                }
            }
        } catch (Exception ex) {
            log.error("", ex);
        }
        log.debug("data====="+data);
        return data;
	}

	public List<String> getSupportedCascades() {
		List<String> supportedCascades = new ArrayList<String>();
        supportedCascades.add("zyxwlb");
        return supportedCascades;
	}

	public Map<Object, String> getVLs() {
		final Map<Object, String> vfls = new HashMap<Object, String>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT LWLBID,CASE WHEN XWLWLBMC IS NULL OR XWLWLBMC='' OR XWLWLBMC=' ' THEN '('||LWLBID||')空' ELSE '('||LWLBID||')'||XWLWLBMC END AS XWLWLBMC FROM T_BYXW_LWCJGL_ZYXWLWLB ");//WHERE ZYXWLB = ?
        //sql.append(" UNION ");
        //sql.append("SELECT '0' AS LWLBID,'空' AS XWLWLBMC FROM DUAL");
        try {
            dbUtil.execute(sql.toString(), new IResultSetProcessor() {
            	//public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
                //    pstmt.setObject(1, cascadeValue, Types.VARCHAR);
                //}
            	public void process(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        vfls.put(rs.getObject(1), rs.getString(2));
                    }
                }
            });
        } catch (Exception ex) {
            log.error("", ex);
        }
        return vfls;
		//throw new UnsupportedOperationException("Not supported yet.");
	}

	public void setCascade(String cascade) {
		this.cascade = cascade;
		
	}

	public void setCascadeValue(Object cascadeValue) {
		this.cascadeValue = cascadeValue;
		
	}

	public void setDbUtil(DbUtil dbUtil) {
		this.dbUtil = dbUtil;
		
	}

}
