package org.ratchetgx.orion.module.zzfw.zzdy.web;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ratchetgx.orion.common.SsfwUtil;
import org.ratchetgx.orion.module.common.service.BizobjService;
import org.ratchetgx.orion.module.zzfw.util.MyPdfPrintable;
import org.ratchetgx.orion.module.zzfw.util.ZzfwLogUtil;
import org.ratchetgx.orion.module.zzfw.zzdy.service.ZzdyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;


@Controller
@RequestMapping(value = "/zzfw/zzdy")
public class ZzdyController{
	//日志
    private Logger log = LoggerFactory.getLogger(this.getClass());  	
    @Autowired
    private ZzdyService zzdyService;   
    @Autowired
    private BizobjService bizobjService;
    @Autowired
    private ZzfwLogUtil zzfwLogUtil;
    
    /**
     * 自助打印初始化
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="zzdyIndex")
    public String zzdyIndex(ModelMap model,HttpServletRequest request){	
    	String returnUrl="";
    	HttpSession session = request.getSession();	    	
    	String fwxmId = request.getParameter("fwxmId");
    	String xmfxmc = request.getParameter("xmfxmc");
    	String bburl = request.getParameter("bburl");
    	String userId = SsfwUtil.getCurrentBh();
    	
		try{
			//获取服务项目信息
			Map fwxmInfo = zzdyService.getFwxmDetail(fwxmId);
			model.put("fwxmInfo", fwxmInfo);
			
			//导航信息，获取服务项目id和服务项目名称，存入session中
			String currentFwxmid = (String)fwxmInfo.get("fwxmid");			
			String currentFwxmmc = (String)fwxmInfo.get("fwxmmc");
			 
			session.removeAttribute("currentNavigation");
			session.setAttribute("currentNavigation","2");			 
			session.removeAttribute("currentFwxmid");
			session.removeAttribute("currentFwxmmc");
			session.setAttribute("currentFwxmid",currentFwxmid);
			session.setAttribute("currentFwxmmc",currentFwxmmc);
			
			/*  1、根据userId,fwxmId获取项目分项信息列表
	    	 *  2、如果为null,则继续正常流程;如果为空的json格式数组,则提示;如果有,则跳转到展示页面
	    	 *  3、
	    	 */	
			log.debug("xmfxmc="+xmfxmc);
			if ("".equals(xmfxmc) || xmfxmc == null) {
				String jsonStr = zzdyService.getXmfxList(userId, fwxmId);
				
				//List<Map> xmfxList = new ArrayList();
				//把json格式的字符串，转为list
				
				JSONObject jsonObj = new JSONObject(new JSONTokener(jsonStr));
				
				String xmfxStr = jsonObj.getString("xmfxList");

				log.debug("jsonStr:---" +xmfxStr);
				out:if (jsonStr == null) {
					break out;
				}else if ("".equals(jsonStr)) {
					session.setAttribute("warnMessage", "没有项目分项信息！");
					returnUrl = "zzfw/zzdy/xmfxList";
					return returnUrl;
				}else if(!"".equals(jsonStr) && jsonStr != null){
					//把json格式的String转为List
					model.put("jsonStr", jsonStr);
					returnUrl = "zzfw/zzdy/xmfxList";
					return returnUrl;
				}
			}
			
			//获取交费配置信息
			Map jfpzInfo = zzdyService.getJfpzDetail(fwxmId);
			String  report = (String)jfpzInfo.get("report"); 
			    
			report = report.replace("@userid", userId);
			if (!"".equals(bburl) && bburl != null) {
				jfpzInfo.put("report", bburl);
			}else{
				jfpzInfo.put("report", report);
			}
			model.put("jfxxInfo", jfpzInfo);
			log.debug("bburl="+bburl);
			//获取报表信息			 
	    	Map  sysParams = (Map)session.getAttribute("sysParams");
	    	String REPORTURL = (String)sysParams.get("REPORTURL");
			String[]  reportInfo = zzdyService.getReportDetail(fwxmId,userId,REPORTURL);	
			if (!"".equals(bburl) && bburl != null) {
				model.put("reportInfo", bburl);
			}else{
				model.put("reportInfo", reportInfo);	
			}
				
		    
		    //获取用户基本信息
		    Map userInfo = zzdyService.getUserDetail();
		    model.put("userInfo", userInfo);
		    
		    //获取学生可免费打印份数
		    int kmfdyfs = zzdyService.getKmfdyfs(fwxmId); 
		    model.put("kmfdyfs", kmfdyfs);	
		    
		    returnUrl = "zzfw/zzdy/zzdyIndex";
		    
		}catch (Exception e){	 
			log.error("", e);
			return "error";
		}     
		 		 
		return returnUrl;
    }
    
    @RequestMapping(value="xmfxOnclick")
    public void xmfxOnclick(ModelMap model,HttpServletRequest request,HttpServletResponse response){	
    	JSONObject rev = new JSONObject();	
    	HttpSession session = request.getSession();	    	
    	String fwxmId = request.getParameter("fwxmId");
    	String xmfxmc = request.getParameter("xmfxmc");
    	String bburl = request.getParameter("bburl");
    	String userId = SsfwUtil.getCurrentBh();
    	
		try{
			//获取服务项目信息
			Map fwxmInfo = zzdyService.getFwxmDetail(fwxmId);
			model.put("fwxmInfo", fwxmInfo);
			
			//导航信息，获取服务项目id和服务项目名称，存入session中
			String currentFwxmid = (String)fwxmInfo.get("fwxmid");			
			String currentFwxmmc = (String)fwxmInfo.get("fwxmmc");
			 
			session.removeAttribute("currentNavigation");
			session.setAttribute("currentNavigation","2");			 
			session.removeAttribute("currentFwxmid");
			session.removeAttribute("currentFwxmmc");
			session.setAttribute("currentFwxmid",currentFwxmid);
			session.setAttribute("currentFwxmmc",currentFwxmmc);

			//获取交费配置信息
			Map jfpzInfo = zzdyService.getJfpzDetail(fwxmId);
			String  report = (String)jfpzInfo.get("report"); 
			    
			report = report.replace("@userid", userId);
			if (!"".equals(bburl) && bburl != null) {
				jfpzInfo.put("report", bburl);
			}else{
				jfpzInfo.put("report", report);
			}
			model.put("jfxxInfo", jfpzInfo);
			
			//获取报表信息			 
	    	Map  sysParams = (Map)session.getAttribute("sysParams");
	    	String REPORTURL = (String)sysParams.get("REPORTURL");
			String[]  reportInfo = zzdyService.getReportDetail(fwxmId,userId,REPORTURL);
			log.debug("reportInfo="+reportInfo);
			if (!"".equals(bburl) && bburl != null) {
				model.put("reportInfo", bburl);
			}else{
				model.put("reportInfo", reportInfo);	
			}
		    //获取用户基本信息
		    Map userInfo = zzdyService.getUserDetail();
		    model.put("userInfo", userInfo);
		    
		    //获取学生可免费打印份数
		    int kmfdyfs = zzdyService.getKmfdyfs(fwxmId); 
		    model.put("kmfdyfs", kmfdyfs);	
		    
		    rev.put("success", true);
		    response.setContentType("application/json;charset=UTF-8");
		    response.getWriter().print(rev.toString());	
		}catch (Exception e){	 
			log.error("", e);
			
		}     
		 		 
		
    }
    /**
     * 报表图片显示
     * @param model
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value="getImages")
    public void getImages(ModelMap model,HttpServletRequest request,HttpServletResponse response) throws IOException{	
    	String name = request.getParameter("name");
    	String fwxmId = request.getParameter("fwxmId");
    	String userId = SsfwUtil.getCurrentBh();   	 
    	//获取session中的系统参数信息
    	HttpSession session = request.getSession();	    	
    	Map  sysParams = (Map)session.getAttribute("sysParams");
    	String REPORTURL = (String)sysParams.get("REPORTURL");
    	
    	String sFilePath = REPORTURL+"/"+userId+"/"+fwxmId+"/images/"+name;
    	log.debug("sFilePath:"+sFilePath);
    	
		OutputStream os = null;
		FileInputStream fis = null;
		File file = new File(sFilePath);
		if (file.exists() && file.canRead()) {
			os = response.getOutputStream();
			fis = new FileInputStream(file);
			try {
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = fis.read(b, 0, 1024)) > 0) {
					os.write(b, 0, len);
				}
				os.flush();
			}catch(Exception ex){
				ex.printStackTrace();
			} finally {
				fis.close();
				os.close();
			}
		}
    }
    
    /**
     * 打印确认异步调用
     * @param request
     * @param response
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping(value="zzdyComfirm")
    public void zzdyComfirm(HttpServletRequest request,HttpServletResponse response) throws IOException,JSONException {
		String fwxmId = request.getParameter("fwxmid");
		String dyfs = (String)request.getParameter("dyfs");
		int xzdyfs =Integer.valueOf(dyfs);
		JSONObject rev = new JSONObject();		
		try{
			rev.put("success", true);
			
			rev.put("dyfs", dyfs);			
			//总费用
			double totalFee =  zzdyService.getTotalFee(fwxmId,xzdyfs);	
			rev.put("totalFee",totalFee);	
			
			//获取学生免费打印份数
		    int mffs = zzdyService.getMffs(fwxmId,xzdyfs);
		    rev.put("mffs",mffs);	    
		     
		    //获取交费配置信息
			Map jfpzInfo = zzdyService.getJfpzDetail(fwxmId);
			JSONObject jfpz = new JSONObject();
			for (Iterator iter = jfpzInfo.keySet().iterator(); iter.hasNext();) {
				String key = (String)iter.next();
				jfpz.put(key, jfpzInfo.get(key));
			}
			rev.put("jfpzInfo",jfpz.toString());	
			
			//获取服务项目信息
			Map fwxmInfo = zzdyService.getFwxmDetail(fwxmId);
			JSONObject fwxm = new JSONObject();
			for (Iterator iter = fwxmInfo.keySet().iterator(); iter.hasNext();) {
				String key = (String)iter.next();
				fwxm.put(key, fwxmInfo.get(key));
			}
			rev.put("fwxmInfo",fwxm.toString());
			
			//获取用户基本信息
		    Map userInfo = zzdyService.getUserDetail();
		    JSONObject user = new JSONObject();
			for (Iterator iter = userInfo.keySet().iterator(); iter.hasNext();) {
				String key = (String)iter.next();
				user.put(key, userInfo.get(key));
			}
			rev.put("userInfo",user.toString());			
				
		}catch (Exception e){			 
	          rev.put("success", false);
	          rev.put("errorMessage", e.getMessage());	         
	          log.error("", e);
		} 
		
		response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(rev.toString());	
    }
    
    /**
     * 计算费用信息
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="getTotalFee")
    public void getTotalFee(HttpServletRequest request,HttpServletResponse response) throws IOException,JSONException {
		String fwxmId = request.getParameter("fwxmid");
		String dyfs = (String)request.getParameter("dyfs");
		int xzdyfs =Integer.valueOf(dyfs);
		
		JSONObject rev = new JSONObject();		
		try{
			rev.put("success", true);
			rev.put("dyfs", dyfs);			
			
			//获取学生免费打印份数
		    int mffs = zzdyService.getMffs(fwxmId,xzdyfs);
		    rev.put("mffs",mffs);	  
			
			double totalFee =  zzdyService.getTotalFee(fwxmId,xzdyfs);	
			
			rev.put("totalFee",totalFee);		
			
			 //获取交费配置信息
			Map jfpzInfo = zzdyService.getJfpzDetail(fwxmId);
			JSONObject jfpz = new JSONObject();
			for (Iterator iter = jfpzInfo.keySet().iterator(); iter.hasNext();) {
				String key = (String)iter.next();
				jfpz.put(key, jfpzInfo.get(key));
			}
			rev.put("jfpzInfo",jfpz.toString());	
			
					 	 
		}catch (Exception e){			 
	          rev.put("success", false);
	          rev.put("errorMessage", e.getMessage());	         
	          log.error("", e);
		} 
		
		response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(rev.toString());	
    }
    
    /**
     * 自助打印确认页面处理
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="printReoport")
    public void printReoport(HttpServletRequest request,HttpServletResponse response) throws IOException,JSONException {
		String fwxmId = request.getParameter("fwxmid");
		
		String dyfs = (String)request.getParameter("dyfs");	
		
		int pageNum = Integer.valueOf(dyfs);		
		
		JSONObject rev = new JSONObject();		
		try{
			rev.put("success", true);	
			//获取交费配置信息
			Map jfpzInfo = zzdyService.getJfpzDetail(fwxmId);
			
			String sPrinter =(String) jfpzInfo.get("dyjmc");
			
	    	String userId = SsfwUtil.getCurrentBh();   	 
	    	//获取session中的系统参数信息
	    	HttpSession session = request.getSession();	    	
	    	Map  sysParams = (Map)session.getAttribute("sysParams");
	    	String REPORTURL = (String)sysParams.get("REPORTURL");
	    	
	    	String sFilePath = REPORTURL+"/"+userId+"/"+fwxmId+"/"+userId+"_"+fwxmId+".pdf";
	    	
			String sPdfPath = sFilePath;			
				
			printPdf(sPrinter, sPdfPath,pageNum);	
		}catch (Exception e){			 
	          rev.put("success", false);
	          rev.put("errorMessage", e.getMessage());	         
	          log.error("", e);
		} 
		
		response.setContentType("application/json;charset=UTF-8");
	    response.getWriter().print(rev.toString());	
    }
    
    
    public static boolean printPdf(String sPrinter, String sPdfPath,int pageNum){
		try {
			/**
			 * 命令行注意事项：  
			 * 1. gsprint.exe文件的保存路径不能带空格
			 * 2. cmd.exe /C 后要用""整体引起来 
			 */
			
			String commondLineStr = "cmd.exe /C \"c:\\gsview\\gsprint.exe";
			commondLineStr += " " + "-printer \"" + sPrinter + "\"";
			commondLineStr += " " + "\"" + sPdfPath + "\" ";
			commondLineStr += " " + "-copies " + pageNum + " ";			
			commondLineStr += "\"";
			
			//commondLineStr = "cmd.exe /C \"dir /w\"";
			System.out.println("commondLineStr:" + commondLineStr);
			Process p = Runtime.getRuntime().exec(commondLineStr);
			InputStream in = p.getInputStream();
			String cmdEchoStr = "";
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b)) > 0) {
			   System.out.println(len);
			   cmdEchoStr += new String(b);
			}
			System.out.println(cmdEchoStr);
			System.out.println("bbb");
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
    
    /**
     * 自助打印确认页面处理
     * @param model
     * @param request
     * @return
     * @throws IOException 
     * @throws Exception
     */
    @RequestMapping(value="showReport")
    public String showReport(HttpServletRequest request,HttpServletResponse response) throws Exception{	    	
        //查询安装打印机信息
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null); 
		//获取默认打印机
		//PrintService defaultservices = PrintServiceLookup.lookupDefaultPrintService();
		//没有查到打印机信息，返回没有找到相关打印机的错误提示
		if(services.length == 0){ 
			System.out.println("not found printer"); 
		} 
		
		//将PrinterJob 与指定的PrintService 关联
		PrinterJob job = PrinterJob.getPrinterJob();
		for(int j=0;j < services.length;j++){ 
			PrintService ps = services[j];
			if(ps.getName().equals("Foxit PDF Printer")){
				job.setPrintService(services[j]); 
		    } 
		}   
		 
		FileInputStream fis = new FileInputStream("F:/ZZFW/08011183/xjzm-yw/08011183_xjzm-yw.pdf");   
	    byte[] pdfContent = new byte[fis.available()];   
	    fis.read(pdfContent, 0, fis.available());   
	    ByteBuffer buf=ByteBuffer.wrap(pdfContent);

	    PDFFile pdfFile = new PDFFile(buf);   
	    Book bk = new Book();   	           
	    int num = pdfFile.getNumPages();   
	    for(int i=0; i<num; i++){
            PDFPage page = pdfFile.getPage(i+1);   
            PageFormat pf = job.defaultPage();  
            MyPdfPrintable by= new MyPdfPrintable();
            by.setPdf(page);
            bk.append(by, pf);   
            
            Paper paper = pf.getPaper();   
            double x = 0;   
            double y = 0;    
               
            if(page.getAspectRatio()<1){   
                double width = page.getBBox().getWidth();   
                double height = page.getBBox().getHeight();   
                   
                paper.setImageableArea(x, y, width, height);   
                   
                pf.setOrientation(PageFormat.PORTRAIT);   
            }else{   
                   
                double width = page.getBBox().getHeight();   
                double height = page.getBBox().getWidth();   
                   
                paper.setImageableArea(x, y, width, height);   
                   
                pf.setOrientation(PageFormat.LANDSCAPE);   
            }   
            pf.setPaper(paper);   
	    }
	     
	     //设置打印副本数量
		 job.setCopies(2);
		 job.setPageable(bk);   
	     job.setJobName("My book");   
	 	 job.print();	
	 	 
    	return "zzfw/zzdy/printReport";
	}
    
    /**
     * 打印完成
     * @param model
     * @param request
     * @return
     * @throws IOException 
     * @throws Exception
     */
    @RequestMapping(value="finish")
    public String finish(ModelMap model,HttpServletRequest request,HttpServletResponse response){	    	
    	HttpSession session = request.getSession();	 
    	
    	String userId = SsfwUtil.getCurrentBh();
		String ip = request.getRemoteAddr()!=null ? request.getRemoteAddr():"";    
		//记录日志信息
		try { 
    	
	    	 List<Map<String, String>> jfmxxxList = bizobjService.getDatasOfBizobj(request, "jfmx");            
	         Map jfmxxx = jfmxxxList.get(0);         
	         String userid = SsfwUtil.getCurrentBh(); 
	         jfmxxx.put("userid", userid);
			 
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 String time = sdf.format((Date) new Date());		
			    
	    	 String jfsj = time.toString();
	         jfmxxx.put("jfsj", jfsj);
	         jfmxxx.put("sfyx", "1");
	         String wid = zzdyService.saveJfmx(jfmxxx);   
	         
	         Map dymxxx = zzdyService.getDymxById(wid);   
	         
	         model.put("dymxxx", dymxxx);
	         
	         Map logMap = new HashMap();
			 logMap.put("userid",userId);
			 logMap.put("czlx","完成服务项目");
			 logMap.put("czms","完成服务项目:"+dymxxx.get("fwxmmc"));

	    	 String cssj = time.toString();
	    	 
			 logMap.put("cssj",cssj);	
			 logMap.put("sbbh",ip);
			 logMap.put("ip",ip);				 
			 zzfwLogUtil.addLogs(logMap); 
         
	         session.removeAttribute("currentNavigation");
			 session.setAttribute("currentNavigation","3");	
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	 
		return "zzfw/zzdy/zzdyFinish";
	}
    
    
}  
    
    