package org.ratchetgx.projectname.widgets.attachment.impl.yjs.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwException;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.util.AndOrEnum;
import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.Condition;
import org.ratchetgx.orion.common.util.ConditionGroup;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.common.util.IPreparedResultSetProcessor;
import org.ratchetgx.orion.common.util.RelOperEnum;
import org.ratchetgx.projectname.widgets.attachment.impl.yjs.GetSysConfitWjsc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;




import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * 
 * @author SFNIE
 *
 */
@Repository
public class WaterMark {
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
     * 
     * @param attachWid
     * @param attachInfo
     * @param waterMarkInfo
     */
	public void AddWatermark(String attachWid, String attachInfo,WaterMarkInfo waterMarkInfo) throws Exception{         
        
        	//待添加水印的文件
            //String dirPath = waterMarkInfo.getDirPath();
            //添加水印图片
        	String imgPath = waterMarkInfo.getImgPath();
            //添加水印的x位置
        	int xPosition = waterMarkInfo.getXPosition();
        	//添加水印的y位置
        	int yPosition = waterMarkInfo.getYPosition();
        	//添加图片水印的透明度
        	String transparency ="";
        	if(waterMarkInfo.getTransparency()!=null){
        		transparency = waterMarkInfo.getTransparency();
            }else{
           	 	transparency = "1";
            }         	
        	
        	//添加水印的临时文件路径
        	HashMap hmDbScfs = new HashMap();  //数据库中对于这个上传标识的配置信息
        	String sTempFilePath = "";   //添加水印的临时文件路径
    		String stTempFileName ="";   //添加水印的临时文件名称
    		String sTempDirPath ="";      //添加水印的临时文件全路径
    		
    		hmDbScfs =(HashMap) getWjscConfig("GGGL_FJGL_TEMPDIR");    		
    		if(hmDbScfs.get("SCLJ") == null || "".equals(hmDbScfs.get("SCLJ").toString())){
        		throw new SsfwException("未获取到GGGL_FJGL_TEMPDIR的数据库中的SCLJ参数!");
        	}
    		
    		sTempFilePath = hmDbScfs.get("SCLJ").toString();  
    		
    		File temp_file = new File(sTempFilePath);
    	    if (!temp_file.exists())
    	    {  
    	    	temp_file.mkdirs();            
    	    }
    		
    		stTempFileName = SsfwUtil.getDbUtil().getSysguid();				
			
    		sTempDirPath = sTempFilePath + File.separatorChar + stTempFileName+".pdf";
    		
			File tempFile = new File(sTempDirPath);
			if(tempFile.exists()){
				tempFile.delete();				
			}
			
			//待添加水印的文件信息
			Map attachmentMap =  getAttachments(attachWid);
	    	String dirPath =(String) attachmentMap.get("file_path");	    	 
			
        	//将图片写入临时pdf中
    		PdfReader reader = new PdfReader(dirPath, "PDF".getBytes());  
    	    PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(sTempDirPath));  
    	    Image img = Image.getInstance(imgPath);// 插入水印      
    	    img.setAbsolutePosition(xPosition, yPosition);     	    
    	    img.setAlignment(Image.UNDERLYING);
    	    
    	    int pageSize =reader.getNumberOfPages();
    	    //添加水印起始页
        	int startPage = 0;        	
        	//添加水印结束页
        	int endPage = 0;
    	    
    	    if(waterMarkInfo.getStartPage()!=-1){
    	    	startPage = waterMarkInfo.getStartPage();
    	    }else{
    	    	startPage =1;
    	    }
    	    if(waterMarkInfo.getStartPage()!=-1){
    	    	endPage = waterMarkInfo.getStartPage();
    	    }else{
    	    	endPage =pageSize;
    	    }    	   
    	    //添加水印
    	    PdfContentByte over;
    	    for(int i = startPage; i <= endPage; i++) {  
    	    	 over = stamp.getOverContent(i);
             	 //设置透明
             	 PdfGState gs1 = new PdfGState();    
             	 gs1.setFillOpacity(Float.valueOf(transparency));
                 over.setGState(gs1);
                 over.addImage(img);  
    	    }  
    	    stamp.close();// 关闭   
    	    
    	    //删除源文件    	    
    	    File file = new File(dirPath);
    	    if(file.exists()){
    	    	file.delete();    	    	
    	    }    
    	    //将临时文件写copy到原文件路径下   	   
            File inputFile = new File(sTempDirPath);
            File outputFile = new File(dirPath);
            FileInputStream is = new FileInputStream(inputFile);
    		FileOutputStream fos = new FileOutputStream(outputFile);
    		byte[] buf = new byte[1024];
    		int len = -1;
    		while ((len = is.read(buf)) > 0) {
    			fos.write(buf, 0, len);
    		}
    		is.close();
    		fos.close();
    	    //删除临时文件
    		inputFile.delete();        
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
	
	public Map getAttachments(String attachWid){
		
		Map attachmentMap = new HashMap();
		List<Map> attachmentList;		
		try {
			//只是为了取个连接符，and 或 or
	        ConditionGroup cg = new ConditionGroup(AndOrEnum.AND);
	        //“wid”列名，RelOperEnum.EQUAL比较运算符
	        cg.addCondition(new Condition("attachment_wid", RelOperEnum.EQUAL, attachWid));          
	       	
	        attachmentList = bizobjUtil.query("SS_ATTACHMENT_FILE", cg, null);
	      
		    if(attachmentList.size()>0){
		    	attachmentMap = attachmentList.get(0);		    	
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        return attachmentMap;
        
	}
}
