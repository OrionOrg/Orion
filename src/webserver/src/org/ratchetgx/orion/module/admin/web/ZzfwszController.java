package org.ratchetgx.orion.module.admin.web;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.module.admin.dao.ZzfwszDao;
import org.ratchetgx.orion.module.admin.service.ZzfwszService;
import org.ratchetgx.orion.module.common.service.BizobjService;
import org.ratchetgx.orion.security.SsfwUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/admin")
public class ZzfwszController {

	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());  	
    @Autowired
    private ZzfwszService zzfwszService;   
    @Autowired
    private BizobjService bizobjService;
    @Autowired
    private ZzfwszDao zzfwszDao;
    
    /**
     * 自助服务设置页面跳转
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET,value="zzfwTree.do")
    public String zzfwIndex(ModelMap model,HttpServletRequest request){	
		return "/admin/zzfwTree";
    }
    /**
     * 自助服务系统日志页面跳转
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET,value="zzfwXtrz.do")
    public String zzfwXtrz(ModelMap model,HttpServletRequest request){	
		return "/admin/zzfwXtrz";
    }
    /**
     * 自助服务对账信息页面跳转
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET,value="zzfwDztj.do")
    public String zzfwDztj(ModelMap model,HttpServletRequest request){	
		return "/admin/zzfwDztj";
    }
    /**
     * 获得树形结构
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="getTreeList.do")
    public void getTreeList(HttpServletRequest request,HttpServletResponse response) throws IOException,JSONException {	

    	String nodeID = request.getParameter("id");//父节点id
    	String revStr = "[{\"id\":1,\"text\":\"自助打印\",\"children\":";
    	String jsonstr = "[";
		try{
			List<Map> treeInfo = zzfwszService.getTreeDetail();
			
			
			Map<String, List> parentidMap = new HashMap();  
		    //Map<String, String> containMap = new HashMap();  
		      
		    String parentid = "";  
		    String lastparentid = "";  
		    List listTree = null;  
		    for(Iterator it=treeInfo.iterator(); it.hasNext();)  
		    {  
		          
		        Map map = (Map) it.next();  
		        parentid = map.get("ffwxmid").toString();  
		        if(parentid==null||"".equals(parentid)){  
		            parentid = "0";  
		        }
		        if(!lastparentid.equals(parentid))  
		        {  
		            if(listTree!=null)  
		                parentidMap.put(lastparentid, listTree);  
		            listTree = new ArrayList();  
		            lastparentid = parentid;  
		        }  
		        listTree.add(map);  
		    }  
		    parentidMap.put(lastparentid, listTree);  
		    log.debug("parentIDmap:"+parentidMap.toString());
		    
		    
		    String tree = getTreeNodeJson(parentidMap,"0"); 
			log.debug("=========================json:"+tree);

			
//			//组装json对象
//			log.debug("一级项目 list："+treeInfo.toString());
//			if (nodeID != null &&"0".equals(nodeID)){
//				for (int i = 0; i < treeInfo.size(); i++) {
//					Map map = treeInfo.get(i);
//					
//					JSONObject  rev = new JSONObject();
//					rev.put("fwxmid", map.get("fwxmid"));
//					String ffwxmid = "";
//					if(map.get("ffwmxid") != null){
//						ffwxmid = map.get("ffwmxid").toString();
//					}
//					rev.put("ffwxmid", ffwxmid);
//					rev.put("clcx", map.get("clcx"));
//					rev.put("icon", map.get("icon"));
//					rev.put("bz", map.get("bz"));
//					rev.put("wid", map.get("wid"));
//					jsonstr = jsonstr
//							+ "{\n"
//							+ "\"id\":"+ i+ ",\n"
//							+ "\"text\":\""+ map.get("fwxmmc")+"\",\n"
//							+ "\"attributes\":"+ rev.toString()+",\n"
//							+ "\"state\":\"closed\"\n" + " },";
//				}
//			}else{
//				List<Map> treeNodeInfo = zzfwszService.getTreeNodeDetail(nodeID);
//				log.debug("nodeID = "+nodeID);
//				log.debug("二级项目 list :" +treeNodeInfo);
//				for (int i = 0; i < treeNodeInfo.size(); i++) {
//					Map map1 = treeNodeInfo.get(i);
//					JSONObject  rev = new JSONObject();
//					String czStr = "";
//					rev.put("px", map1.get("px"));
//					rev.put("fwxmid", map1.get("fwxmid"));
//					rev.put("ffwxmid", nodeID);
//					rev.put("sfqy", map1.get("sfqy"));
//					rev.put("cjsj", map1.get("cjsj"));
//					rev.put("cz", czStr);
//					rev.put("wid", map1.get("wid"));
//					jsonstr = jsonstr
//					+ "{\n"
//					+ "\"id\":"+ i+ ",\n"
//					+ "\"text\":\""+ map1.get("fwxmmc") +"\",\n"
//					+ "\"attributes\":"+ rev.toString()+ ",\n"
//					+ "\"state\":\"closed\"\n" + " },";
//				}
//			}
//			int end=jsonstr.length()-1;// 去掉最后一个逗号
//            String json=jsonstr.substring(0,end);  
//            json=json+"]";
//            revStr = revStr +json +"}]";
//			log.debug("json:"+json);
			response.setContentType("application/json;charset=UTF-8");
//			if (nodeID != null && "0".equals(nodeID)) {
//				response.getWriter().print(revStr);
//			} else {
//				response.getWriter().print(json);
//			}
			response.getWriter().print(tree);
		}catch (Exception e){	 
			log.error("", e);
		} 

		
    }
    /** 
     * 通过parentidMap 迭代树 
     * @param parentidMap 
     * @param parentid 
     * @return 
     */  
    private String getTreeNodeJson(Map parentidMap,String parentid){  
        StringBuilder builder = new StringBuilder("[");  
        List list =  (List) parentidMap.get(parentid); 
        log.debug("list--------:"+list.toString());
        try {
        	for (int i=0; i < list.size(); ++i) {  
                HashMap map = (HashMap)list.get(i);  
                String id=map.get("fwxmid")==null ?"":map.get("fwxmid").toString().trim();  
                String text=map.get("fwxmmc")==null ?"":map.get("fwxmmc").toString().trim();
                String clcx=map.get("clcx")==null ?"":map.get("clcx").toString().trim();  
                String ffwxmid=map.get("ffwxmid")==null ?"":map.get("ffwxmid").toString().trim();  
                String cjsj=map.get("cjsj")==null ?"":map.get("cjsj").toString().trim();  
                String sfqy=map.get("sfqy")==null ?"":map.get("sfqy").toString().trim();  
                String px=map.get("px")==null ?"":map.get("px").toString().trim();
                String bz=map.get("bz")==null ?"":map.get("bz").toString().trim();
                String icon=map.get("icon")==null ?"":map.get("icon").toString().trim();
                String wid=map.get("wid")==null ?"":map.get("wid").toString().trim();
                String fwlb=map.get("fwlb")==null ?"":map.get("fwlb").toString().trim();
                String iconcls="foler";  
                JSONObject rev = new JSONObject();
                rev.put("px", px);
    			rev.put("fwxmid", id);
    			rev.put("ffwxmid", ffwxmid);
    			rev.put("sfqy", sfqy);
    			rev.put("cjsj", cjsj);
    			rev.put("icon", icon);
    			rev.put("clcx", clcx);
    			rev.put("bz", bz);
    			rev.put("wid", wid);
    			rev.put("fwlb", fwlb);
                if (builder.length() > 1)  
                    builder.append(",");   
                	builder.append("{\"id\":\"").append(id).append("\"")  
                          .append(",\"text\":\"").append(text).append("\"")
                          .append(",\"attributes\":").append(rev.toString()).append("")
                          .append(",\"cls\":\"").append(iconcls).append("\"");  
                             
                ////////////////递规子类 start  
                 if(parentidMap.containsKey(id))  
                 {  
                     builder.append(",\"children\":");  
                     builder.append(getTreeNodeJson(parentidMap,id));  
                 }  
                 else  
                     builder.append(",\"leaf\":true");  
                ////////////////递规子类 end  
                builder.append("}");  
                  
            }  
            builder.append("]");  
		} catch (Exception e) {
			log.error(e.toString());
		}
        
        return builder.toString();  
    }  
    /**
     * 获得树形结构
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="getFwxmList.do")
    public void getFwxmList(HttpServletRequest request,HttpServletResponse response) throws IOException,JSONException {	
    	String nodeID = request.getParameter("id");//父节点id
    	try {
    		String jsonstr = "{";
    		List<Map> treeNodeInfo = zzfwszService.getTreeNodeDetail(nodeID);
    		jsonstr = jsonstr + "\"total\":"+treeNodeInfo.size()+ ",\n";
    		String rows = "\"rows\":[";
    		for (int i = 0; i < treeNodeInfo.size(); i++) {
				Map map1 = treeNodeInfo.get(i);
				JSONObject  rev = new JSONObject();
				String czStr = "<a href ='javascript:editFwxm();' >基本信息</a>&nbsp;&nbsp;<a href ='javascript:showUGroup();' >用户组</a>&nbsp;&nbsp;<a href ='javascript:showSfbz();' >收费标准</a>&nbsp;&nbsp;<a href ='javascript:deleteRecord()' >删除</a>";
				rev.put("fwxmmc",map1.get("fwxmmc"));
				rev.put("fwxmid",map1.get("fwxmid"));
				rev.put("ffwxmid",map1.get("ffwxmid"));
				rev.put("wid",map1.get("wid"));
				rev.put("icon",map1.get("icon"));
				rev.put("clcx",map1.get("clcx"));
				rev.put("bz",map1.get("bz"));
				rev.put("px", map1.get("px"));
				String sfqy = map1.get("sfqy").toString();
				rev.put("sfqy", sfqy);
				if ("1".equals(sfqy)) {
					rev.put("sfqymc", "是");
				} else {
					rev.put("sfqymc", "否");
				}
				rev.put("cjsj", map1.get("cjsj"));
				rev.put("cz", czStr);
				rows = rows + rev.toString()+",";
				
			}
    		int end=rows.length()-1;// 去掉最后一个逗号
            String json=rows.substring(0,end); 
            json = json +"]";
            jsonstr = jsonstr + json +"}";
			log.debug("fwxmjson:"+jsonstr);
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(jsonstr);
    		
		} catch (Exception e) {
			log.error("", e);
		}
    	
    }
    @RequestMapping(value="getFwxmById.do")
    public void getFwxmById(HttpServletRequest request,HttpServletResponse response) throws IOException,JSONException {	
    	String nodeID = request.getParameter("id");//父节点id
    	try {
    		JSONObject rev = new JSONObject();
    		StringBuffer sb = new StringBuffer();
    		boolean flag = false;
    		List<Map> treeNodeInfo = zzfwszService.getTreeNodeDetail(nodeID);
    		if (treeNodeInfo.isEmpty()) {
    			flag = true;
			}else{
				
				for (int i = 0; i < treeNodeInfo.size(); i++) {
					Map map = treeNodeInfo.get(i);
					sb = sb.append(map.get("fwxmmc")).append(",");
				}
			}
    		String fwxmList = "";
    		if(sb.length()>=1){
    			fwxmList = sb.substring(0, sb.length()-1);
    		}
    		 
    		if(flag){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", fwxmList);
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
    		
		} catch (Exception e) {
			log.error("", e);
		}
    	
    }
    /**
     * 修改或者保存服务项目信息
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="saveFwxm.do")
    public String saveFxwm(ModelMap model,HttpServletRequest request,HttpServletResponse response){	    	   
		try { 
			List<Map<String, String>> fwxmList = bizobjService.getDatasOfBizobj(request, "fwxm");
			
			
	         Map fwxmxx = fwxmList.get(0); 
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String time = sdf.format((Date) new Date());		
			    
	    	 String cjsj = time.toString();
	    	 fwxmxx.put("cjsj", cjsj);
	    	 fwxmxx.put("fwdlid", "zzdy");
	         log.debug("FWXMXX:"+fwxmxx.toString());
	         zzfwszService.saveFwxm(fwxmxx);
	         

    	} catch (Exception e) {
    		log.error(e.toString());
			e.printStackTrace();
		}    	 
		return "";
	}
    
    /**
     * 保存服务大类相关信息
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="saveFwdl.do")
    public String saveFxdl(ModelMap model,HttpServletRequest request,HttpServletResponse response){	    	   
		try { 
			List<Map<String, String>> fwxmList = bizobjService.getDatasOfBizobj(request, "fwdl");
			
	         Map fwxmxx = fwxmList.get(0); 
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String time = sdf.format((Date) new Date());		
			    
	    	 String cjsj = time.toString();
	    	 fwxmxx.put("cjsj", cjsj);
	         log.debug("FWDLXX:"+fwxmxx.toString());
	         zzfwszService.saveFwxm(fwxmxx);
	         

    	} catch (Exception e) {
    		log.error(e.toString());
			e.printStackTrace();
		}    	 
		return "";
	}
    /**
     * 保存或者修改二级服务项目信息
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="saveEjfwxm.do")
    public String saveEjfwxm(ModelMap model,HttpServletRequest request,HttpServletResponse response){	    	   
		try { 
			List<Map<String, String>> fwxmList = bizobjService.getDatasOfBizobj(request, "ejfwxm");
			
			
	         Map fwxmxx = fwxmList.get(0); 
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String time = sdf.format((Date) new Date());
			 String wid = SsfwUtil.getDbUtil().getSysguid();
			 log.debug("--"+fwxmxx.get("sfqy") + fwxmxx.get("fwxmid"));
			 if(fwxmxx.get("wid")=="" || fwxmxx.get("fwxmid")==""){
				 fwxmxx.put("fwxmid", wid);
				 fwxmxx.put("wid", wid);
			 }
//			 if(fwxmxx.get("fwdlid")==""){
//				 fwxmxx.put("fwdlid", "zzdy");
//			 }
//			 if(fwxmxx.get("sfqy")==""){
//				 fwxmxx.put("sfqy", "1");
//			 }
	    	 String cjsj = time.toString();
	    	 fwxmxx.put("cjsj", cjsj);
	         log.debug("FWXMXX:"+fwxmxx.toString());
	         zzfwszService.saveFwxm(fwxmxx);
	         

    	} catch (Exception e) {
    		log.error(e.toString());
			e.printStackTrace();
		}    	 
		return "";
	}
    
    /**
     * 保存或修改收费标准信息
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="saveSfbz.do")
    public String saveSfbz(ModelMap model,HttpServletRequest request,HttpServletResponse response){	    	   
		try { 
			List<Map<String, String>> sfbzList = bizobjService.getDatasOfBizobj(request, "sfbz");
	         Map sfbzxx = sfbzList.get(0); 
	         log.debug("sfbzxx:"+sfbzxx.toString());
	         zzfwszService.saveSfbz(sfbzxx);

    	} catch (Exception e) {
    		log.error(e.toString());
			e.printStackTrace();
			return "error";
		}    	 
		return "";
	}
    
    /**
     * 获得用户组列表
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="getUgroupList.do")
    public void getUgroupList(ModelMap model,HttpServletRequest request,HttpServletResponse response){
    	try {
    		JSONArray revArray = new JSONArray();
    		
			List list = new ArrayList();
    		List<Map> groupList = zzfwszDao.getUgroupList();
    		model.put("", groupList);
    		for (int i = 0; i < groupList.size(); i++) {
    			JSONObject  rev = new JSONObject();
    			Map map = groupList.get(i);
    			rev.put("wid", i);
    			rev.put("roleName", map.get("role"));
    			revArray.put(i,rev);
    		}
    		
    		response.setContentType("application/json;charset=UTF-8");
    		JSONObject json = new JSONObject();
    		log.debug(revArray.toString());
    	    response.getWriter().print(revArray.toString());
    		
    	} catch (Exception e) {
    		log.error(e.toString());
			e.printStackTrace();
		}    	 
    	
    }
    
    @RequestMapping(value="getFwxmGroupList.do")
    public void getFwxmGroupList(ModelMap model,HttpServletRequest request,HttpServletResponse response){
    	try {
    		JSONArray revArray = new JSONArray();
    		String fwxmid = request.getParameter("fwxmid").toString();
    		List<Map> groupList = zzfwszDao.getFwxmGroupList(fwxmid);
    		model.put("", groupList);
    		for (int i = 0; i < groupList.size(); i++) {
    			JSONObject  rev = new JSONObject();
    			Map map = groupList.get(i);
    			rev.put("groupId", map.get("ugroupid"));
    			rev.put("fwxmId", map.get("fwxmid"));
    			revArray.put(i,rev);
    		}
    		response.setContentType("application/json;charset=UTF-8");
    		log.debug(revArray.toString());
    	    response.getWriter().print(revArray.toString());
    		
    	} catch (Exception e) {
    		log.error(e.toString());
			e.printStackTrace();
		}    	 
    	
    }
    /**
     * 根据fwxmid删除服务项目
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="deleteFwxm.do")
    public void deleteFwxm (ModelMap model,HttpServletRequest request,HttpServletResponse response){
    	JSONObject rev = new JSONObject();
		try {
			String fwxmid = request.getParameter("id");
			log.debug("删除服务项目 ，fwxmid = "+fwxmid);
			boolean flag = zzfwszDao.deleteFwxm(fwxmid);
			if(flag){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", "删除服务项目失败！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());

		} catch (Exception e) {
			log.error(e.toString());
			e.printStackTrace();
		}    	 
    }
    /**
     * 获取系统参数列表
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="getXtcsList.do")
    public void getXtcsList(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String jsonstr = "{";
    		List<Map> treeNodeInfo = zzfwszService.getXtcsList();
    		jsonstr = jsonstr + "\"total\":"+treeNodeInfo.size()+ ",\n";
    		String rows = "\"rows\":[";
    		for (int i = 0; i < treeNodeInfo.size(); i++) {
				Map map1 = treeNodeInfo.get(i);
				JSONObject  rev = new JSONObject();
				String czStr = "<a href ='javascript:editXtcs();' >编辑</a>&nbsp;&nbsp;<a href ='javascript:deleteXtcs();' >删除</a>";
				rev.put("csbs",map1.get("csbs"));
				rev.put("csz", map1.get("csz"));
				rev.put("cssm", map1.get("cssm"));
				rev.put("cz", czStr);
				rows = rows + rev.toString()+",";
				
			}
    		int end=rows.length()-1;// 去掉最后一个逗号
            String json=rows.substring(0,end); 
            json = json +"]";
            jsonstr = jsonstr + json +"}";
			log.debug("xtcsjson:"+jsonstr);
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(jsonstr);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
    /**
     * 修改或者保存系统参数
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="saveXtcs.do")
    public void saveXtcs(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject rev = new JSONObject();
		try {
			log.debug("-----------保存系统参数。");
			String csbs = request.getParameter("csbs");
			String csz = request.getParameter("csz");
			String cssm = request.getParameter("cssm");
			Map<String,String> xtcsMap = new HashMap();
			xtcsMap.put("csbs", csbs);
			xtcsMap.put("csz", csz);
			xtcsMap.put("cssm", cssm);
			log.debug("saveXtcs_data:"+xtcsMap);
			List<Map> xtcsList = zzfwszDao.getXtcsById(csbs);
			String errMessage = "";
			if (!xtcsList.isEmpty()) {
				errMessage = "增加系统参数失败,该参数标识已经存在！";
			}
			
			int count = zzfwszService.saveXtcs(xtcsMap);
			if(count>0){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", errMessage);
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
    /**
     * 根据ID获取系统参数
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="getXtcs.do")
    public void getXtcs(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject rev = new JSONObject();
		try {
			log.debug("-----------保存系统参数。");
			String csbs = request.getParameter("csbs");
			List<Map> xtcsList = zzfwszDao.getXtcsById(csbs);
			if (!xtcsList.isEmpty()) {
				rev.put("success", false);
				rev.put("msg", "增加系统参数失败,该参数标识已经存在！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
    /**
     * 根据fwxmid获取收费标准
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="getSfbz.do")
    public void getSfbz(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject rev = new JSONObject();
		try {
			String fwxmid = request.getParameter("id");
			log.debug("-----------获得收费标准。fwxmid = " +fwxmid);
			List<Map> sfbzInfo = zzfwszDao.getSfbzById(fwxmid);
			if (!sfbzInfo.isEmpty()) {
				Map map = sfbzInfo.get(0);
				log.debug(sfbzInfo.toString());
				rev.put("fwxmid", map.get("fwxmid"));
				rev.put("price", map.get("price"));
				rev.put("clcx", map.get("clcx"));
				rev.put("dcdysx", map.get("dcdysx"));
				rev.put("mfdyfs", map.get("mfdyfs"));
				rev.put("sfbzsm", map.get("sfbzsm"));
				rev.put("dyjmc", map.get("dyjmc"));
				rev.put("zysx", map.get("zysx"));
				rev.put("report", map.get("report"));
				rev.put("bz", map.get("bz"));
				rev.put("sfmf", map.get("sfmf"));
				rev.put("wid", map.get("wid"));
			}
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
		
    }
    /**
     * 更新系统参数信息
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="updateXtcs.do")
    public void updateXtcs(ModelMap model, HttpServletRequest request,
			HttpServletResponse response){
    	JSONObject rev = new JSONObject();
		try {
			
			Map<String,String> xtcsMap = new HashMap();
			String csbs = request.getParameter("id");
			String csz = request.getParameter("csz");
			String cssm = request.getParameter("cssm");
			xtcsMap.put("csbs", csbs);
			xtcsMap.put("csz", csz);
			xtcsMap.put("cssm", cssm);
			log.debug("saveXtcs_data:"+xtcsMap);
			int count = zzfwszService.updateXtcs(xtcsMap);
			if(count>0){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", "更新系统参数失败！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    /**
     * 获取系统参数列表
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="getCwzdList.do")
    public void getCwzdList(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String jsonstr = "{";
    		List<Map> treeNodeInfo = zzfwszService.getCwzdList();
    		jsonstr = jsonstr + "\"total\":"+treeNodeInfo.size()+ ",\n";
    		String rows = "\"rows\":[";
    		for (int i = 0; i < treeNodeInfo.size(); i++) {
				Map map1 = treeNodeInfo.get(i);
				JSONObject  rev = new JSONObject();
				String czStr = "<a href ='javascript:editUser();' >编辑</a>&nbsp;&nbsp;<a href ='javascript:deleteRow();' >删除</a>";
				rev.put("errorcode",map1.get("errorcode"));
				rev.put("errormessage", map1.get("errormessage"));
				rev.put("cz", czStr);
				rows = rows + rev.toString()+",";
				
			}
    		int end=rows.length()-1;// 去掉最后一个逗号
            String json=rows.substring(0,end); 
            json = json +"]";
            jsonstr = jsonstr + json +"}";
			log.debug("cwzdjson:"+jsonstr);
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(jsonstr);
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
    /**
     * 修改或者保存系统参数
     * @param model
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="saveCwzd.do")
    public void saveCwzd(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject rev = new JSONObject();
		try {
			log.debug("-----------保存错误字典。");
			String errorCode = request.getParameter("errorcode");
			String errorMessage = request.getParameter("errormessage");
			Map<String,String> cwzdMap = new HashMap();
			cwzdMap.put("errorcode", errorCode);
			cwzdMap.put("errormessage", errorMessage);
			log.debug("saveCwzd_data:"+cwzdMap);
			List<Map> cwzdList = zzfwszDao.getCwzdById(errorCode);
			String errMessage = "";
			if (!cwzdList.isEmpty()) {
				errMessage = "增加错误字典失败,该错误码已经存在！";
			}
			int count = zzfwszService.saveCwzd(cwzdMap);
			if(count>0){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", errMessage);
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}

		
	}
    /**
     * 更新系统参数信息
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="updateCwzd.do")
    public void updateCwzd(ModelMap model, HttpServletRequest request,
			HttpServletResponse response){
    	JSONObject rev = new JSONObject();
		try {
			
			Map<String,String> cwzdMap = new HashMap();
			String errorCode = request.getParameter("errorcode");
			String errorMessage = request.getParameter("errormessage");
			cwzdMap.put("errorcode", errorCode);
			cwzdMap.put("errormessage", errorMessage);
			log.debug("savecwzd_data:"+cwzdMap);
			int count = zzfwszService.updateCwzd(cwzdMap);
			if(count>0){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", "更新错误字典失败！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    /**
     * 删除错误字典
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="deleteCwzd.do")
    public void deleteCwzd(ModelMap model, HttpServletRequest request,
			HttpServletResponse response){
    	JSONObject rev = new JSONObject();
		try {
			
			String errorCode = request.getParameter("errorcode");
			String ss = new String(errorCode.getBytes("iso-8859-1"),"UTF-8");
			String param = "'"+ss+"'";
			log.debug("删除错误字典 ，code = "+param);
			int count = zzfwszDao.deleteCwzd(ss);
			log.debug("count:"+count);
			if(count>0){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", "删除错误字典失败！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    /**
     * 删除系统参数
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="deleteXtcs.do")
    public void deleteXtcs(ModelMap model, HttpServletRequest request,
			HttpServletResponse response){
    	JSONObject rev = new JSONObject();
		try {
			
			String csbs = request.getParameter("csbs");
			String ss = new String(csbs.getBytes("iso-8859-1"),"UTF-8");
			log.debug("删除系统参数 ，code = "+ss);
			int count = zzfwszDao.deleteXtcs(ss);
			if(count>0){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", "删除系统参数失败！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    /**
     * 获得系统日志
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="getXtrzList.do")
    public void getXtrzList(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String jsonstr = "{";
			
//			//当前页  
//	        int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
//	        //每页显示条数  
//	        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
//	        //每页的开始记录  第一页为1  第二页为number +1   
//	        int start = (intPage-1)*number;  
			//filter条件
			String filter = "  ";
			String czlx = request.getParameter("czlx");
			
			String czr = request.getParameter("czr");
			String ip = request.getParameter("ip");
			String startDate = request.getParameter("startdate");
			String endDate = request.getParameter("enddate");
			//log.debug(startDate+","+endDate);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (!"".equals(czlx) && czlx != null) {
				log.debug("czlx--："+czlx);
				String ss = URLDecoder.decode(czlx,"UTF-8");
				log.debug("czlx---："+ss);
				filter = filter + " and czlx = '" + ss+"'";
			}
			if (!"".equals(czr) && czr != null) {
				String name = URLDecoder.decode(czr,"UTF-8");
				log.debug(czr);
				filter = filter + " and (t.userid like '%" + name+"' or u.name like '%"+name+"%')";
			}
			if (!"".equals(ip) && ip != null) {
				filter = filter + " and ip = '" + ip+"'";
			}
			if (!"".equals(startDate) && startDate != null) {
				log.debug("startDate--："+startDate);
//				String time = sdf.format(new Date(startDate));
//				log.debug("time--："+time);
				filter = filter + " and to_date(cssj,'YYYY-MM-DD HH24:MI:SS') >= to_date('" + startDate+"','YYYY-MM-DD')";
			}
			if (!"".equals(endDate) && endDate != null) {
				log.debug("endDate--："+endDate);
//				Date date = new Date(endDate);
//				Calendar c = Calendar.getInstance();
//				c.setTime(date);  
//		        int day = c.get(Calendar.DATE);  
//		        c.set(Calendar.DATE, day + 1);  
//				String time = sdf.format(c.getTime());
				filter = filter + " and to_date(cssj,'YYYY-MM-DD HH24:MI:SS') <=  to_date('" + endDate +"','YYYY-MM-DD')+1 ";
			}
			log.debug("---filter:" +filter);
			//页码数
			String page = request.getParameter("page");
			int pageNum= 1;
			if (!"".equals(page) || page != null) {
				pageNum = Integer.valueOf(page);
			}
			int pageSize = 15;
			String row = request.getParameter("rows");
			if (!"".equals(row) || row != null) {
				pageSize = Integer.valueOf(row);
			}
			 //每页显示记录数 
			 log.debug(page+","+row);
			 log.debug("pageNum："+pageNum+",pageSize:"+pageSize);
			 int vBegin = (pageNum -1)*pageSize;
			 int vEnd = vBegin + pageSize;
			 log.debug("vBegin："+vBegin+",vEnd:"+vEnd);
			List<Map> treeNodeInfo = zzfwszService.getXtrzList(vBegin,vEnd,filter);
			int total = zzfwszDao.getXtrzList(filter);
    		jsonstr = jsonstr + "\"total\":"+total+ ",\n";
    		String rows = "\"rows\":[";
    		SsfwUserDetails ssfwUserDetails = (SsfwUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		for (int i = 0; i < treeNodeInfo.size(); i++) {
				Map map1 = treeNodeInfo.get(i);
				JSONObject  rev = new JSONObject();
				String czStr = "<a href ='javascript:showXtrz();' >查看</a>";
				rev.put("czlx",map1.get("czlx"));
				String userid = map1.get("userid").toString();
				String userName = "";
				if(!"".equals(userid)){
					List<Map> userInfo =zzfwszDao.getUserInfoById(userid);
					if(!userInfo.isEmpty()){
						Map map = userInfo.get(0);
						userName = map.get("name").toString();
					}
				}
				rev.put("userid", userName);
				rev.put("czms", map1.get("czms"));
				rev.put("cssj", map1.get("cssj"));
				rev.put("sbbh", map1.get("sbbh"));
				rev.put("ip", map1.get("ip"));
				rev.put("cz", czStr);
				rows = rows + rev.toString()+",";
				
			}
    		int end=rows.length()-1;// 去掉最后一个逗号
            String json=rows.substring(0,end); 
            json = json +"]";
            jsonstr = jsonstr + json +"}";
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(jsonstr);
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    
    @RequestMapping(value="getDztjList.do")
    public void getDztjList(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			String jsonstr = "{";
			String filter = "  ";
			String dylx = request.getParameter("dylx");
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			//log.debug(startDate+","+endDate);

			if (!"".equals(dylx) && dylx != null) {
				String ss = URLDecoder.decode(dylx,"UTF-8");
				log.debug("dylx："+ss);
				filter = filter + " and xm.fwxmid = '" + ss+"'";
			}
			if (!"".equals(startDate) && startDate != null) {

				filter = filter + " and to_date(jfsj,'YYYY-MM-DD HH24:MI:SS') >=  to_date('" + startDate+"','YYYY-MM-DD')";
			}
			if (!"".equals(endDate) && endDate != null) {

				filter = filter + " and to_date(jfsj,'YYYY-MM-DD HH24:MI:SS')<=  to_date('" + endDate +"','YYYY-MM-DD') ";
			}
			log.debug("---filter:" +filter);
			//页码数
			String page = request.getParameter("page");
			int pageNum= 1;
			if (!"".equals(page) || page != null) {
				pageNum = Integer.valueOf(page);
			}
			int pageSize = 15;
			String row = request.getParameter("rows");
			if (!"".equals(row) || row != null) {
				pageSize = Integer.valueOf(row);
			}
			 //每页显示记录数 
			 log.debug(page+","+row);
			 log.debug("pageNum："+pageNum+",pageSize:"+pageSize);
			 int vBegin = (pageNum -1)*pageSize;
			 int vEnd = vBegin + pageSize;
			 log.debug("vBegin："+vBegin+",vEnd:"+vEnd);
			List<Map> treeNodeInfo = zzfwszService.getDztjList(vBegin,vEnd,filter);
			int total = zzfwszDao.getDztjList(filter);
    		jsonstr = jsonstr + "\"total\":"+total+ ",\n";
    		String rows = "\"rows\":[";
    		for (int i = 0; i < treeNodeInfo.size(); i++) {
				Map map1 = treeNodeInfo.get(i);
				JSONObject  rev = new JSONObject();
				String czStr = "<a href ='javascript:showDztj();' >查看</a>";
				rev.put("bh", map1.get("bh"));
				rev.put("name", map1.get("name"));
				rev.put("fwxmmc", map1.get("fwxmmc"));
				rev.put("dyfs", map1.get("dyfs"));
				rev.put("dyfy", map1.get("dyfy"));
				rev.put("jylsh", map1.get("yjlsh"));
				rev.put("jfsj", map1.get("jfsj"));
				rev.put("mffs", map1.get("mffs"));
				rev.put("price", map1.get("price"));
				rev.put("jykh", map1.get("jykh"));
				rev.put("dysbxx", map1.get("dysbxx"));
				rev.put("bz", map1.get("bz"));
				rev.put("cz", czStr);
				rows = rows + rev.toString()+",";
				
			}
    		int end=rows.length()-1;// 去掉最后一个逗号
            String json=rows.substring(0,end); 
            json = json +"]";
            jsonstr = jsonstr + json +"}";
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(jsonstr);
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    
    /**
     * 获得打印类型列表
     * @param model
     * @param request
     * @param response
     */
    @RequestMapping(value="getDylx.do")
    public void getDylx(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject rev = new JSONObject();
    	String jsonStr = "[{\"id\":\"\",\"text\":\"---请选择---\"},";
		try {
			log.debug("-----------获得打印类型列表。");
			List<Map> dylxList = zzfwszDao.getDylx();
			for (int i = 0; i < dylxList.size(); i++) {
				Map map = dylxList.get(i);
				jsonStr = jsonStr
				+ "{"
				+ "\"id\":\""+map.get("fwxmid")+ "\","
				+ "\"text\":\""+ map.get("fwxmmc")+"\""
				+ " },";
			}
			int end=jsonStr.length()-1;// 去掉最后一个逗号
            String json=jsonStr.substring(0,end);  
            json=json+"]";   
			log.debug("json:"+json);
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(json);	
		} catch (Exception e) {
			log.error(e.toString());
		}
    }
    @RequestMapping(value="saveUgroupList.do")
    public void saveUgroupList(ModelMap model, HttpServletRequest request,
			HttpServletResponse response) {
    	JSONObject rev = new JSONObject();
    	List<Map> list = new ArrayList<Map>();
		try {
			log.debug("-----------设置用户组列表。");
			String fwxmId = request.getParameter("fwxmid");
			String tmp = request.getParameter("groupList");
			String groupInfo = new String(tmp.getBytes("iso-8859-1"),"UTF-8");
			String[] groupList = groupInfo.split(",");
			for (int i = 0; i < groupList.length; i++) {
				Map<String,String> groupMap = new HashMap();
				log.debug("groupList["+i+"] = " +groupList[i]);
				groupMap.put("groupId", groupList[i]);
				groupMap.put("fwxmId",fwxmId);
				list.add(groupMap);
			}
			boolean flag = zzfwszDao.batchSaveUgroup(groupInfo,fwxmId);
			if(flag){
				rev.put("success", true);
			}else{
				rev.put("success", false);
				rev.put("msg", "设置用户组失败！");
			}
			response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
}
