<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.ratchetgx.orion.common.SsfwUtil"%>
<%@ page import="org.ratchetgx.orion.common.SsfwUtilExt" %>
<%@ page import="org.ratchetgx.orion.security.ids.IdsAuthenticationFilter" %>
<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@include file="/common/taglibs.jsp"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>自助打印系统</title>
        <%@ include file="/common/widgets.jsp"%>
        <link href="${ctx}/resources/css/zzfwdecorator.css" type="text/css" rel="stylesheet" />  
        <decorator:head></decorator:head>       
        <script type="text/javascript">        
         //初始化操作
         $(document).ready(function(){
			   	//样式控制
			   	csscontrol();	
			   	$(window).scroll(function(){csscontrol()});
				$(window).resize(function(){csscontrol()});
				
				//调用计时器
				showTime(); 
              
            	//返回功能
            	$("#button_return").click(function(){
            		history.go(-1);
            	});  
            	
				//退出功能
            	$("#button_exit").click(function(){
            		window.location.href = "${ctx}/j_spring_security_logout";
            	});  
		 });
		 
		 
		 function csscontrol(){
		       //定义高度
		        //var ch=document.documentElement.clientHeight;//屏幕的高度 
		 		//var cw=document.documentElement.clientWidth;//屏幕的高度		
		 		var window_width = 1680;   //22寸宽
		 		var window_height = 1050;   //22寸高
		 		
		 		$('body').css({width:window_width,height:window_height});			
		 		
		 		var bottom = 56;	
			   		   	
			    var welcom_height=$(".welcom_bottom").height();//底部样式 
			       
			   	var welcome_top = window_height - bottom - welcom_height;
			   	
			   	$(".welcom_bottom").css({top:welcome_top,left:0});
			   	
			   	var time_top = window_height - bottom - welcom_height -70;
			   	
		  	    $(".timecount").css({top:time_top,left:130}); 	  	    
		  	    
		  	     
		  	    var buttonContaner_width = $(".zzdy_buttonContaner").width();//底部样式  
		  	    
		  	  
		  	    var button_left = window_width - buttonContaner_width -80;
		  	    
		        $(".zzdy_buttonContaner").css({top:welcome_top,left:button_left});		        
		        
		        var userInfo_width = $(".zzdy_userInfo").width(); 
		         
		        var userInfo_left = window_width - userInfo_width -45;
		  	    
		        $(".zzdy_userInfo").css({top:0,left:userInfo_left});		
		        	 
		 }
		 
		 //屏蔽右键菜单
		document.oncontextmenu = function (event){
		    if(window.event){
		        event = window.event;
		    }try{
		        var the = event.srcElement;
		        if (!((the.tagName == "INPUT" && the.type.toLowerCase() == "text") || the.tagName == "TEXTAREA")){
		            return false;
		        }
		        return true;
		    }catch (e){
		        return false; 
		    } 
		}		
		
		//倒计时功能 
		function run(){
		    var s = document.getElementById("time");
		    if(s.innerHTML == 0){
		    	//清空session以及cookies
		    	window.location.href = "${ctx}/j_spring_security_logout";
		    }
		    s.innerHTML = s.innerHTML * 1 - 1;
		}
		
		//自动调用run方法，判断登陆是否超时，超时自动退出
		window.setInterval("run()", 1000);
		
		</script>
		<script>
		<!--
		var dn
		c1=new Image(); c1.src="${ctx}/resources/image/time/c1.gif";
		c2=new Image(); c2.src="${ctx}/resources/image/time/c2.gif";
		c3=new Image(); c3.src="${ctx}/resources/image/time/c3.gif"; 
		c4=new Image(); c4.src="${ctx}/resources/image/time/c4.gif";
		c5=new Image(); c5.src="${ctx}/resources/image/time/c5.gif"; 
		c6=new Image(); c6.src="${ctx}/resources/image/time/c6.gif"; 
		c7=new Image(); c7.src="${ctx}/resources/image/time/c7.gif"; 
		c8=new Image(); c8.src="${ctx}/resources/image/time/c8.gif"; 
		c9=new Image(); c9.src="${ctx}/resources/image/time/c9.gif"; 
		c0=new Image(); c0.src="${ctx}/resources/image/time/c0.gif"; 
		cb=new Image(); cb.src="${ctx}/resources/image/time/cb.gif";
		
		var time = null; 
		function extract(s){
			if (!document.images)
				return;
			var bw = 0;
			var sw = 0;
			var gw = 0;
				
			if(Math.floor(s/100)<=9){//3位数字
				 bw = Math.floor(s/100);
				 sw = Math.floor((s%100)/10);
				 gw = ((s%100)%10);				
			}else if(Math.floor(s/10)<=9){//2位数字
				 bw = 0;
				 sw = Math.floor(s/10);
				 gw = s%10;
			}else if (s<=9){//1位数字
				 bw = 0;
				 sw = 0;
				 gw = s;
			}			
			document.b.src=eval("c"+bw+".src");
			document.s.src=eval("c"+sw+".src");
			document.images.g.src=eval("c"+gw+".src");
		}
		
		function showTime(){
			if (!document.images){
				return;
			}
			
			var Digital=document.getElementById("time").innerHTML;
			extract(Digital);
			time = setTimeout("showTime()",1000);
		}
		
		function stopTimer(){
			window.clearTimeout(time);
		}
		//-->
		</script>	
		<style>		
		
	</style>	 
</head>
<body>
<!-- 主区域 -->
<div class="zzdy_contaner">
    <div id="main" class="zzdy_main">
       <!-- 左边树形展示区 -->
        <div id="mainLeft" class="zzdy_mainLeft">        
            <div style="float: left">
            	<ul class="menu-nav">       
            	  	    
					<li <c:if test="${sessionScope.currentNavigation==1}">class="current"</c:if>>
						<div class="li_div"><span class="num">1</span><a href="#" class="links">选择打印项目</a></div>
						<c:if test="${!empty sessionScope.currentFwxmid}">
							<div class="depth-menu">
								<ul>
									<li class="last">
										<span class="icon-line"></span><a href="#">${sessionScope.currentFwxmmc}</a>	
									</li>
								</ul>
							</div>
						</c:if>
					</li>
					<li <c:if test="${sessionScope.currentNavigation==2}">class="current"</c:if>>
						<div class="li_div"><span class="num">2</span><a href="#" class="links">确认内容</a></div>
					</li>
					<li <c:if test="${sessionScope.currentNavigation==3}">class="current"</c:if>>
						<div class="li_div"><span class="num">3</span><a href="#" class="links">完成</a></div>
					</li>					
				</ul> 
            </div>
            
            <div class="zzdy_clear"></div>
            
            <div class="timecount">
	            <div style="display: none;"><span id="time">${sessionScope.sysParams.DLSJSX}</span></div>
				<div style="">
				<table cellspacing=0 cellpadding=0 border=0>
				  <tbody>
					  <tr>
						  <td> &nbsp;</td>
						    <td >
						      <p align=left>
						      <img src="${ctx}/resources/image/time/cb.gif" name=b>
						      <img src="${ctx}/resources/image/time/cb.gif" name=s>
						      <img src="${ctx}/resources/image/time/cb.gif" name=g>
						    </p></td>
						    <td>&nbsp;&nbsp;秒后自动注销</td>
					    </tr>
				   </tbody>
				 </table>
				 </div> 
			</div> 
			
			<div class="zzdy_clear"></div>
			
			<div class="welcom_bottom">
				<img src="${ctx}/resources/image/zzfw/welcome-bottom.gif" alt="" />
			</div>
        </div>       
        
        <!-- 右边自助打印编辑区 --> 
        <div id="mainRight" class="zzdy_mainRight">
             <!-- 校标显示 -->
      		 <div class="zzdy_showxb">
      		 	  <img  src="${ctx}/resources/image/zzfw/logo.png" />
      		 </div>  
      		 <div class="zzdy_clear"></div>  
      		 <!-- 自助打印内容区 -->
             <div class="zzdy_zzfwInfo" >	                
		           <decorator:body></decorator:body>
             </div>
        </div>
    </div>
    
     <!-- 用户信息 -->
	 <div class="zzdy_userInfo">			 
		 <table style="margin-top: 35px;" align="center" width="95%" height="85px" cellpadding="0" cellspacing="0">
		    <tr>
		    	<td rowspan="2" width="60%"><img src="${ctx}/zzfw/framework/login/getXsImages.do?xh=${sessionScope.userLoginInfo.xh}" width="80px" height="90px"/></td>
		    	<td width="40%"  align="right" style="font-family: 微软雅黑;font-size: 14px;font-weight: bold">${sessionScope.userLoginInfo.xm}</td>
		    </tr>
		    <tr>
		    	<td  align="right">${sessionScope.userLoginInfo.xh}</td>
		    </tr>
		 </table>	
		  <table  style="margin-top: 15px;" align="center" width="95%" height="80px" cellpadding="0" cellspacing="0">
		 	 <tr>
		    	<td align="right" width="60px;">性别：</td>
		    	<td align="left">${sessionScope.userLoginInfo.xbmc}</td>
		    	<td align="right">班级：</td>
		    	<td align="left">${sessionScope.userLoginInfo.szbj}</td>
		    </tr>
		    <tr>
		    	<td align="right">入学时间：</td>
		    	<td colspan="3"  align="left">${sessionScope.userLoginInfo.rxny}</td>
		    </tr>
		 </table>		 
	 </div>   
 	
  	<!-- 按钮区域 -->
	<div class="zzdy_buttonContaner">
           
           <div class="zzdy_button">
              <img id="button_return" src="${ctx}/resources/image/zzfw/back.png"/>
           </div>
           
           <div class="zzdy_button">
              <img id="button_exit"  src="${ctx}/resources/image/zzfw/exit.png"/>
           </div>
	</div>  
</div>

</body>
</html>
