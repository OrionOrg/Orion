package org.ratchetgx.orion.widgets.attachment.impl.yjs.yjsAttachmentHandler;

import org.ratchetgx.orion.common.util.BizobjUtil;
import org.ratchetgx.orion.common.util.DbUtil;
import org.ratchetgx.orion.widgets.attachment.AttachmentPosition;
import org.springframework.web.multipart.MultipartFile;

public interface YjsAttachmentHandler {
	
	public void setBizobjUtil(BizobjUtil bizobjUtil);
	
	public void setDbUtil(DbUtil dbUtil);
	//	上传前校验
	public void PreUploadValidator( MultipartFile multfile, String attachinfo) throws Exception;
	//附件位置创建
	public AttachmentPosition positionCreator(String attachInfo) throws Exception;
	//上传成功后调用
	public String PostUploadHandle(String attachWid, String attachinfo) throws Exception;
	//下载前调用
	public String PreDownloadHandle(String attachWid , String attachinfo) throws Exception;	
}
