package org.ratchetgx.projectname.widgets.attachment.impl.yjs;

import java.util.HashMap;
import java.util.Map;

import org.ratchetgx.orion.common.SsfwException;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.widgets.attachment.AttachmentPosition;
import org.ratchetgx.orion.widgets.attachment.impl.FileAttachmentPosition;
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
public class GetAttachInfo {
    //日志
    private Logger log = LoggerFactory.getLogger(this.getClass()); 
	@Autowired
	private GetSysConfitWjsc wjscConfig;
	
	public Map getAttachInfo(String attachInfo) {	 
		//获取上传配置信息，格式:{SCBS:BYXW_LYZXPY_LWSCLC,FILE_IN_PATH:2007/0909887,FILE_NAME:''}
		
		Map attachInfoMap = new HashMap();
		int nStart = 0;
		int nEnd   = 0;
		
		//获取{}里面的内容
		nStart = attachInfo.indexOf("{");			
		nEnd = attachInfo.indexOf("}");			
		String sScConfig =attachInfo.substring(nStart+1,nEnd);
		
		//上传标志
		String sSCBS ="";
		log.debug("SCBScouint"+sScConfig.indexOf("SCBS"));
		if(attachInfo.indexOf("SCBS")>0){
			 nStart = sScConfig.indexOf("SCBS") ;
			 nEnd = sScConfig.indexOf("$",nStart+5) ;
			if(nEnd>0){					
				sSCBS = sScConfig.substring(nStart+5,nEnd) ;
			}else{					
				sSCBS = sScConfig.substring(nStart+5) ;
			}	
		}
		attachInfoMap.put("SCBS", sSCBS);
		
		//文件的内部路径在上传的模块中传入的
		String sFileInpath ="";
		if(attachInfo.indexOf("FILE_IN_PATH")>0){
			 nStart = sScConfig.indexOf("FILE_IN_PATH") ;
			 nEnd = sScConfig.indexOf("$",nStart+13) ;
			if(nEnd>0){					
				sFileInpath = sScConfig.substring(nStart+13,nEnd) ;
			}else{					
				sFileInpath = sScConfig.substring(nStart+13) ;
			}	
			
		}
		
		attachInfoMap.put("FILE_IN_PATH", sFileInpath);
		
		
		//文件的名字在上传的模块中传入的
		String sFileName ="";
		if(attachInfo.indexOf("FILE_NAME")>0){
			 nStart = sScConfig.indexOf("FILE_NAME") ;
			 nEnd = sScConfig.indexOf("$",nStart+10) ;
			if(nEnd>0){					
				sFileName = sScConfig.substring(nStart+10,nEnd);
			}else{					
				sFileName = sScConfig.substring(nStart+10);
			}				
		}	
		
		attachInfoMap.put("FILE_NAME", sFileName);
		
		
		HashMap hmDbScfs = new HashMap();  //数据库中对于这个上传标识的配置信息 
		hmDbScfs = wjscConfig.getWjscConfig(sSCBS); 
		String sFilePath = "";   //文件的路径在数据库中配置的
		if(hmDbScfs.get("SCLJ") == null || "".equals(hmDbScfs.get("SCLJ").toString())){
    		throw new SsfwException("未获取到"+ sSCBS+"的数据库中的SCLJ参数!");
    	}
		
    	sFilePath = hmDbScfs.get("SCLJ").toString();	    	
    	
    	if(!"".equals(sFileInpath)){
    		sFilePath = sFilePath +"/"+ sFileInpath;
    	}
    	
    	attachInfoMap.put("FILEPATH", sFilePath);
		 
		return attachInfoMap;
	}
}
