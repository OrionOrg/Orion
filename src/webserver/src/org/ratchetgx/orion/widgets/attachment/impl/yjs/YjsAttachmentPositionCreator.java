package org.ratchetgx.orion.widgets.attachment.impl.yjs;

import java.util.HashMap;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwException;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.widgets.attachment.AttachmentPosition;
import org.ratchetgx.orion.widgets.attachment.AttachmentPositionCreator;
import org.ratchetgx.orion.widgets.attachment.impl.FileAttachmentPosition;
import org.ratchetgx.orion.widgets.attachment.impl.yjs.GetSysConfitWjsc;
import org.ratchetgx.orion.widgets.attachment.impl.yjs.yjsAttachmentHandler.YjsAttachmentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 上传功能
 *
 * @author sfnie
 *
 */
@Component("yjsAttachmentPositionCreator")
public class YjsAttachmentPositionCreator  implements AttachmentPositionCreator {
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass()); 
	@Autowired
	private GetSysConfitWjsc wjscConfig;
	@Autowired
	private GetAttachInfo getAttachInfo;	
	/** 业务对象 */
	@Autowired
	private BizobjUtil bizobjUtil;

	/** 数据访问对象 */
	@Autowired
	private DbUtil dbUtil;

	
	public AttachmentPosition positionCreator(String attachInfo) {		
			FileAttachmentPosition positionimpl = new FileAttachmentPosition();		
			FileAttachmentPosition customPositionimpl = new FileAttachmentPosition();	
			
			//获取上传的配置信息
			Map attachInfoMap = getAttachInfo.getAttachInfo(attachInfo);			
			//获取上传标识
			String sSCBS ="";
			if(attachInfoMap.get("SCBS")!=null){				 				
				sSCBS = (String)attachInfoMap.get("SCBS");
			}	
			
			//文件的名字在上传的模块中传入的
			String sFileName ="";
			if(attachInfoMap.get("FILE_NAME")!=null){				 				
				sFileName = (String)attachInfoMap.get("FILE_NAME");
			}	
		 
			String sFilePath = "";   //文件的路径在数据库中配置的
			if(attachInfoMap.get("FILEPATH")!=null){				 				
				sFilePath = (String)attachInfoMap.get("FILEPATH");
			}
			
			if("".equals(sFilePath)){
				sFilePath = String.valueOf(SsfwUtil.getValue(SsfwUtil.WEBAPP_ABSOLUTE_PATH));				
			}	    	
	    	positionimpl.setDirPath(sFilePath);
	    	
			if("".equals(sFileName)){
				sFileName = SsfwUtil.getDbUtil().getSysguid();				
			}
			
			positionimpl.setFileName(sFileName);// 指定服务端文件名称
			
			 
			//各个模块的自定义部分，可扩展			
			try {
				Class PostUploadHandlerClass = Class.forName("com.wisedu.ssfw.widgets.attachment.impl.yjs.yjsAttachmentHandler.Attachment_"+sSCBS);
				
				YjsAttachmentHandler postuploadhandler = (YjsAttachmentHandler) PostUploadHandlerClass.newInstance();				
				postuploadhandler.setBizobjUtil(bizobjUtil);
				postuploadhandler.setDbUtil(dbUtil);
				customPositionimpl = (FileAttachmentPosition) postuploadhandler.positionCreator(attachInfo);
				if(customPositionimpl!=null){
					positionimpl = customPositionimpl;					
				}				
				 
			}catch(ClassNotFoundException e){//未实例化类
				log.debug("YjsAttachmentPostUploadHandler未找到"+e.getMessage());
			 
			}catch (Exception e) {
				log.debug("出现错误!"+e.getMessage());
				e.printStackTrace();
			} 
			
			return positionimpl;
	}

}

