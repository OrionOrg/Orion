package org.ratchetgx.projectname.widgets.photo.impl.yjs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwException;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.widgets.photo.PhotoHandler;
import org.ratchetgx.projectname.widgets.attachment.impl.yjs.GetSysConfitWjsc;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
*
* @author sfnie
*/
@Component(value = "YjsPhotoHandlerImpl")
public class YjsPhotoHandlerImpl implements PhotoHandler {

    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
	private GetSysConfitWjsc wjscConfig;    
    
    @Autowired
    private DbUtil dbUtil;
    
    public String upload(InputStream is,Map params) throws Exception {
    	Object value = params.get("value");
        String bh = (String) params.get("bh");
        String suffix = (String) params.get("suffix");
    	
        //权限验证
        if(!SsfwUtil.getCurrentBh().equals(bh)){
            throw new Exception("无权限上传该照片。");
        }       
        
        HashMap hmDbScfs = new HashMap();  //数据库中对于这个上传标识的配置信息 
		hmDbScfs = wjscConfig.getWjscConfig("XJGL_YJSJBXX_EXT"); 
		String sFilePath = "";   //文件的路径在数据库中配置的
		if(hmDbScfs.get("SCLJ") == null || "".equals(hmDbScfs.get("SCLJ").toString())){
    		throw new SsfwException("未获取到XJGL_YJSJBXX_EXT的数据库中的SCLJ参数!");
    	}	

    	sFilePath = hmDbScfs.get("SCLJ").toString();    
    	
        //照片以学号命名   
    	String retrunValue= bh + "." + suffix;
        String photoName =sFilePath + File.separatorChar +retrunValue;
        File f = new File(photoName);
        
        if (f.exists()) {
            f.delete();
        }
        
        try {
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            is.close();
            fos.close();
        } catch (IOException ex) {
            log.error("", ex);
            retrunValue = null;
        }
       
        return retrunValue;
    }

    public InputStream download(Map params) throws Exception {
	
    	String value = (String)params.get("value");
        String bh = (String) params.get("bh");
        
        /** ------------权限验证------------ */
        /** 验证学生权限 */
        boolean bQxpp_xs = SsfwUtil.getCurrentBh().equals(bh); /** 学生权限匹配 */
        log.debug("bQxpp_xs:" + bQxpp_xs);
        
        if (!bQxpp_xs) {	/** 学生权限不匹配 */
        	
        	/** 验证学生权限 */
        	boolean bQxpp_ds = false; /** 导师权限匹配 */
            final List dsDyXhList = new ArrayList(); /** 导师对应学号列表 */
            String sql = "SELECT XH FROM T_XJGL_XJXX_YJSJBXX WHERE DSZGH=? "
            	       + " UNION SELECT XH FROM T_XJGL_XJXX_YJSJBXX_FZJ WHERE DSZGH=? ";
            
            dbUtil.execute(sql, new IPreparedResultSetProcessor() {
                public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
                    pstmt.setString(1, SsfwUtil.getCurrentBh());
                    pstmt.setString(2, SsfwUtil.getCurrentBh());
                }
                
                public void processResultSet(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                    	dsDyXhList.add(rs.getString("XH"));
                    }
                }
            });
            if (dsDyXhList.contains(bh)) {
            	bQxpp_ds = true;
            }
            log.debug("bQxpp_ds:" + bQxpp_ds);
            
            if(!bQxpp_ds){
                throw new Exception("无权限浏览该照片。");
            }
        }
        
        String photo_name ="";
        HashMap hmDbScfs = new HashMap();  //数据库中对于这个上传标识的配置信息 
		hmDbScfs = wjscConfig.getWjscConfig("XJGL_YJSJBXX_EXT"); 
		String sFilePath = "";   //文件的路径在数据库中配置的
		if(hmDbScfs.get("SCLJ") == null || "".equals(hmDbScfs.get("SCLJ").toString())){
    		throw new SsfwException("未获取到XJGL_YJSJBXX_EXT的数据库中的SCLJ参数!");
    	}				
		
    	sFilePath = hmDbScfs.get("SCLJ").toString();    	
        
    	//文件
		photo_name = bh+".jpg";
		
        File f = new File(sFilePath + File.separatorChar + photo_name);
        
    	  
        if (!f.exists()) {
            f = new File((String)SsfwUtil.getValue(SsfwUtil.WEBAPP_ABSOLUTE_PATH) + File.separatorChar  + "resources" + File.separatorChar + "image" + File.separatorChar + "noPhoto.gif");
        }
        
        if (!f.exists()) {
            throw new Exception(f.getAbsolutePath() + "不存在");
        }
        
        String fileName = f.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        params.put("suffix", suffix);
        
        try {
            FileInputStream fis = new FileInputStream(f);
            return fis;
        } catch (IOException ex) {
            throw ex;
        }
    }
}
