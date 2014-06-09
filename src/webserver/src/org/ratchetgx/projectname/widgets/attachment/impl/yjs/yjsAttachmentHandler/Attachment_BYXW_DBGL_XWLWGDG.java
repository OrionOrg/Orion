package org.ratchetgx.projectname.widgets.attachment.impl.yjs.yjsAttachmentHandler;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwException;
import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.ratchetgx.orion.widgets.attachment.AttachmentPosition;
import org.ratchetgx.projectname.widgets.attachment.impl.yjs.GetSysConfitWjsc;
import org.ratchetgx.projectname.widgets.attachment.impl.yjs.utils.WaterMark;
import org.ratchetgx.projectname.widgets.attachment.impl.yjs.utils.WaterMarkInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.org.barcode.BarCode;
import com.org.barcode.encoder.barCodeEncoder;

@Repository
public class Attachment_BYXW_DBGL_XWLWGDG implements YjsAttachmentHandler{		   
	/** 日志对象 */
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/** 业务对象 */
	private BizobjUtil bizobjUtil;

	/** 数据访问对象 */
	private DbUtil dbUtil;

	public void setBizobjUtil(BizobjUtil bizobjUtil) {
		this.bizobjUtil = bizobjUtil;
	}

	public void setDbUtil(DbUtil dbUtil) {
		this.dbUtil = dbUtil;
	}
	
    /**
     * 附件上传前校验，校验上传文件个数，格式等
     * 
     * @param multfile
     * @param attachinfo
     * @return
     * @throws Exception
     */
	public void PreUploadValidator( MultipartFile multfile, String attachinfo) throws Exception{		
		log.debug("asdfasdfasdfasdfasdfffffff");
	}
	
	/**
	 * 附件位置创建，定义附件的上传位置已经上传后的文件成
	 *  
     * @param attachInfo
     * @return FileAttachmentPosition
	 */	 
	public AttachmentPosition positionCreator(String attachInfo){		
		return null;
	}
	
	/**
	 * 上传成功后调用，上传成功后，添加条形码
	 * 
	 *@param attachWid
	 *@param attachinfo
	 *@return
	 */	 	 
	public String PostUploadHandle(String attachWid, String attachinfo) throws Exception {		
		//根据学号，生成条形码		 
   		String sFilePath = "";   //生成条形码的文件的路径，公共的临时文件路径   		 
   		Map hmDbScfs =(HashMap) getWjscConfig("GGGL_FJGL_TEMPDIR");    		
		
   		if(hmDbScfs.get("SCLJ") == null || "".equals(hmDbScfs.get("SCLJ").toString())){
    		throw new SsfwException("未获取到GGGL_FJGL_TEMPDIR的数据库中的SCLJ参数!");
    	}		
   		sFilePath = hmDbScfs.get("SCLJ").toString(); 
       
        File file = new File(sFilePath);
        if (!file.exists())
        {  
          file.mkdirs();            
        }
    	
        //定义条形码内容
       	String message = null;
       	SsfwUserDetails ssfwUserDetails = (SsfwUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	String xh = ssfwUserDetails.getBh();
        message = xh;
        if ( message == null )
          return null;  
        //生成条形码
        BarCode bc=new BarCode();
        bc.code=message;
        bc.barType=bc.CODE128;
        bc.resolution = 38;
    	File ff = new File( sFilePath+  File.separatorChar+ message + ".jpeg");
    	//在指定的位置生成条形码图片
    	if(!ff.exists()){    		
    		barCodeEncoder bce = new barCodeEncoder(bc, "JPEG", ff.getPath() ); 
        }
    	
    	//添加水印
    	WaterMarkInfo waterMarkInfo = new WaterMarkInfo();    	
    	waterMarkInfo.setImgPath(sFilePath+  File.separatorChar+ message + ".jpeg"); //设置添加水印的照片
    	waterMarkInfo.setTransparency("1f");  //设置透明度，0~1之间的浮点型数字
    	waterMarkInfo.setXPosition(450);      //设置x坐标的位置
    	waterMarkInfo.setYPosition(750);      //设置y坐标的位置
    	
    	WaterMark waterMark = new WaterMark();
    	waterMark.setBizobjUtil(bizobjUtil);
    	waterMark.setDbUtil(dbUtil);
    	waterMark.AddWatermark(attachWid, attachinfo,waterMarkInfo);
    	
		return "sucess";
	}	
	 
	/**
	 * 下载前调用
	 * 
	 *@param attachWid
     *@param attachinfo
	 *@return
	 */
	public String PreDownloadHandle(String attachWid , String attachinfo){	
		return null;
	}
	
	public Map getWjscConfig(String sSCBS){
		final  List<Map> listWjsc  = new ArrayList();
		final  String scbsValue = sSCBS;
   		String querySql = "SELECT * FROM T_YJS_CONFIG_WJSCFS WHERE SCBS=?";
   		
   		try {
			dbUtil.execute(querySql, new IPreparedResultSetProcessor() {
			   public void processPreparedStatement(PreparedStatement pstmt) throws SQLException {
				   pstmt.setString(1,scbsValue);
			   }
			   public void processResultSet(ResultSet rs) throws SQLException {
				   listWjsc.addAll(dbUtil.resultSet2ListToUpperCase(rs));
			   }
			});
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		HashMap WjscConfig = new HashMap();
		if (listWjsc.size()>0){
			WjscConfig = (HashMap)listWjsc.get(0);			
		}
		return WjscConfig;		
	}
}
