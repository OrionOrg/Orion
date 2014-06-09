package org.ratchetgx.projectname.widgets.attachment.impl.yjs;

import java.util.Map;

import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.widgets.attachment.AttachmentPreUploadValidator;
import org.ratchetgx.projectname.widgets.attachment.impl.yjs.yjsAttachmentHandler.YjsAttachmentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("yjsAttachmentPreUploadValidator")
public class YjsAttachmentPreUploadValidator implements 	AttachmentPreUploadValidator {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private GetAttachInfo getAttachInfo;	
	/** 业务对象 */
	@Autowired
	private BizobjUtil bizobjUtil;

	/** 数据访问对象 */
	@Autowired
	private DbUtil dbUtil;

	public void validator(MultipartFile multfile, String attachinfo) {
		boolean isClassFound = false;
		// 根据上传的配置信息attachinfo，执行上传完毕后的操作
		Map attachInfoMap = getAttachInfo.getAttachInfo(attachinfo);
		
		String sSCBS =(String) attachInfoMap.get("SCBS");
		//各个模块的自定义部分，可扩展			
		try {
			Class PostUploadHandlerClass = Class.forName("com.wisedu.ssfw.widgets.attachment.impl.yjs.yjsAttachmentHandler.Attachment_"+sSCBS);
			
			YjsAttachmentHandler preUploadHandler = (YjsAttachmentHandler) PostUploadHandlerClass.newInstance();				
			preUploadHandler.setBizobjUtil(bizobjUtil);
			preUploadHandler.setDbUtil(dbUtil);
			
			preUploadHandler.PreUploadValidator(multfile, attachinfo);
			isClassFound = true;
			
		}catch(ClassNotFoundException e){
			log.debug("YjsAttachmentPreUploadValidator未找到"+e.getMessage());
			isClassFound = false;
		}catch (Exception e) {
			log.debug("出现错误!"+e.getMessage());
			e.printStackTrace();
		} 
		log.debug("附件校验成功");
	}

}

