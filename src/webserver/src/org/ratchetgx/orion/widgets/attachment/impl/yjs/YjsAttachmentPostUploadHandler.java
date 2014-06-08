package org.ratchetgx.orion.widgets.attachment.impl.yjs;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwException;
import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.widgets.attachment.AttachmentPostUploadHandler;
import org.ratchetgx.orion.widgets.attachment.impl.yjs.GetSysConfitWjsc;
import org.ratchetgx.orion.widgets.attachment.impl.yjs.yjsAttachmentHandler.YjsAttachmentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 上传完毕后，处理类
 *
 * @author sfnie
 *
 */
@Component("yjsAttachmentPostUploadHandler")
public class YjsAttachmentPostUploadHandler  implements AttachmentPostUploadHandler {
		private Logger log = LoggerFactory.getLogger(this.getClass());
		@Autowired
		private GetAttachInfo getAttachInfo;	
		/** 业务对象 */
		@Autowired
		private BizobjUtil bizobjUtil;

		/** 数据访问对象 */
		@Autowired
		private DbUtil dbUtil;

		public String handle(String attachWid, String attachInfo){
			boolean isClassFound = false;
			//根据上传的配置信息attachinfo，执行上传完毕后的操作
			Map attachInfoMap = getAttachInfo.getAttachInfo(attachInfo);			
			String sSCBS =(String) attachInfoMap.get("SCBS");
			
			//各个模块的自定义部分，可扩展			
			try {
				Class PostUploadHandlerClass = Class.forName("com.wisedu.ssfw.widgets.attachment.impl.yjs.yjsAttachmentHandler.Attachment_"+sSCBS);
				
				YjsAttachmentHandler postuploadhandler = (YjsAttachmentHandler) PostUploadHandlerClass.newInstance();				
				postuploadhandler.setBizobjUtil(bizobjUtil);
				postuploadhandler.setDbUtil(dbUtil);
				String retrunValue = postuploadhandler.PostUploadHandle(attachWid, attachInfo);
				isClassFound = true;
			}catch(ClassNotFoundException e){//未实例化类
				log.debug("YjsAttachmentPostUploadHandler未找到"+e.getMessage());
				isClassFound = false;
			}catch (Exception e) {
				log.debug("出现错误!"+e.getMessage());
				e.printStackTrace();
			} 
			
			log.debug("附件上传后被调用处理");
			return null;
		}

}
