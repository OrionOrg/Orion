<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>自助服务</title>
 <%@ include file="/common/widgets.jsp"%>
 <link href="${ctx}/resources/css/zzfw.css" type="text/css" rel="stylesheet" />
 <style type="text/css">
.ejxmContainer{
	display:none;
	text-align:center;
	-moz-border-radius: 5px;
	-webkit-border-radius: 5px;
	border-radius: 5px;
	position:absolute;
	padding:30px;
	z-index:500;
	width:330px;
	height:80px;
	background-color: #fffcf4;

}
 </style>
 
	<script type="text/javascript">
	
	//显示灰色JS遮罩层             
    function showDialog(objid,message){  
       	var bH = 1050;//document.documentElement.clientHeight;
		var bW = 1680;//document.documentElement.clientWidth;		
		var objWH=getObjWh(objid);		 
		$("#fullbg").css({width:bW,height:bH,display:"block"});	
		var tbT=objWH.split("|")[0]+"px";
		var tbL=objWH.split("|")[1]+"px"; 
		$("#"+objid).css({top:tbT,left:tbL,display:"block"}); 
		$("#message").html(message);
		$(window).scroll(function(){resetBg(objid)});
		$(window).resize(function(){resetBg(objid)});
		
	} 
    
    //获取对象高度
	function getObjWh(obj){                  
		var st=document.documentElement.scrollTop;//滚动条距顶部的距离                  
		var sl=document.documentElement.scrollLeft;//滚动条距左边的距离                  
		var ch=document.documentElement.clientHeight;//屏幕的高度                  
		var cw=document.documentElement.clientWidth;//屏幕的宽度                  
		var objH=$("#"+obj).height();//浮动对象的高度                  
		var objW=$("#"+obj).width();//浮动对象的宽度                  
		var objT=Number(st)+(Number(ch)-Number(objH))/2;                  
		var objL=Number(sl)+(Number(cw)-Number(objW))/2;                  
		return objT+"|"+objL;              
	}      
	
	//刷新窗口高度        
	function resetBg(obj){                  
		var fullbg=$("#fullbg").css("display");                  
		if(fullbg=="block"){  
			var bH2 = 1050;//document.documentElement.clientHeight;
			var bW2 = 1680;//document.documentElement.clientWidth;		                     
			$("#fullbg").css({width:bW2,height:bH2});                      
			var objV=getObjWh(obj);                      
			var tbT=objV.split("|")[0]+"px";                      
			var tbL=objV.split("|")[1]+"px";                      
			$("#"+obj).css({top:tbT,left:tbL});                  
		}              
	} 
	
	//关闭窗口方法
	function closeDialog(did){
		$("#fullbg").css("display","none");                 
		$(".zzfw_dialog").css("display","none");   
	}
	
         $(document).ready(function(){
           //实现点击二级项目以外区域时，隐藏二级项目展示区
            $(document).click(function(e){
		        if(e.target.id!="ejxmContainer"&&!$(e.target).parents("div").is(".ejxmContainer")){
		            $("#ejxmContainer").css({display:"none"});
		            $("#ejtop").css({display:"none"});
		        }
		    });            
        });
        
        //显示二级信息
    	function showEjxm(fwdlid,fwxmid){    	     		
		    var W = $('#yjxmContainer').width(); 
		    var H = $('#yjxmContainer').height();
		    var Y = $('#yjxmContainer').offset().left;
		    var X = $('#yjxmContainer').offset().top; 
		    var fwxm_left = $('#'+fwxmid).offset().left;
		    
		    X = H+20;  //一级高度+小箭头高度
  
		   	$("#ejxmContainer").css({top:X,left:0,width:W - 45});
		   	
    	 	$.ajax({
				async : true,
				type : 'POST',
				url : '${ctx}/zzfw/framework/fwxm/showejxm.do',
				data : {fwxmId:fwxmid},
				dataType : 'json',
				success : function(data){
                	if (data.success) {    
	                	  var divStr = "";
	                	  //如果没有子项，则直接跳转的打印系统
	                	  if(data.ejfwxmList.length==0){
	                	  	  gotoFunc(fwdlid,fwxmid);                	  
	                	  }else{
			                  for (var i = 0; i < data.ejfwxmList.length; i++) {
			                     var ejxmObj = data.ejfwxmList[i];	                    
			                    	
			                    	if((i+1)%7==0){
			                    	 	 divStr += "<div class='clear'></div>";
			                    	}
			                    	
			                    	divStr +="<div  class='ejxm_IconContainer'>";
			       
									divStr +="<div class='ejxm_IconImage'><img alt='"+ejxmObj.fwxmmc+"' src='${ctx}/resources/image/zzfw/"+ejxmObj.icon+"'  onclick=gotoFunc(\'"+ejxmObj.ffwxmid+"\',\'"+ejxmObj.fwxmid+"\') /></div>";
						
									divStr +="<div class='ejxm_IconName'><a onclick=gotoFunc(\'"+ejxmObj.ffwxmid+"\',\'"+ejxmObj.fwxmid+"\')>"+ejxmObj.fwxmmc+"</a></div>";
									
									if(ejxmObj.permission !=1){
										divStr +="<div class='mask'></div>";
									}
									
									divStr +="</div>";							 
			                  }
			                  $("#ejxmContainer").html(divStr);
		                      $("#ejxmContainer").css({display:"block"});
		                      $("#ejtop").css({display:'block',top:X-6,left:fwxm_left - Y +143/2});
	                	  }                  	 
                	}
				}					
	    	});
    	 }
        
        //二级打印点击实现 
        function gotoFunc(ffwxmid,fwxmid){         
         	//异步调用doOnclick 事件
	    	$.ajax({
				async : true,
				type : 'POST',
				url : '${ctx}/zzfw/framework/fwxm/doOnclick.do',
				data : {fwxmId:fwxmid},
				dataType : 'json',
				success : function(data){
                	if (data.jsonStr) {    
                   		var obj = eval( "(" + data.jsonStr + ")" ); 
                    	if(obj.permission==1){                     	 
                    		  showDialog('tsxx_dialog'); 
                     		  window.location = "${ctx}/zzfw/framework/fwxm/gotoResult.do?fwxmId="+fwxmid;
                    	}else{                      			 
                       		var message = obj.promptMsg;
                       		var obj = "comfirm_dialog";
                       		alert(1);
                       		$("#ejtop").css({display:"none"});
                      		showDialog(obj,message);
                    	}
                	}
				}					
	    	});
    	 }
    	 
    	 
    </script>   
</head>
<body>
	<div class="huanying" align="center"><strong>选择打印项目</strong></div>
	
	<div class="fwxmContainer">	
		<div id="yjxmContainer" class="yjxmContainer">				 
		     <c:forEach items="${fwxmList}" var="fwxmList" varStatus="status">    
		     	  <c:choose>
		               <c:when  test="${status.count % 7 == 0}">	
		               		<!-- 清除，达到换行效果 -->	               
		               		<div class="clear"></div>
		               </c:when>		              
			      </c:choose>
			      <div class="IconContainer" id ="${fwxmList.fwxmid}">
					<div class="IconImage" onclick="showEjxm('${fwxmList.ffwxmid}','${fwxmList.fwxmid}')"><img alt="${fwxmList.fwxmmc}" src="${ctx}/resources/image/zzfw/${fwxmList.icon}"/></div>
					<div class="IconName"  onclick="showEjxm('${fwxmList.ffwxmid}','${fwxmList.fwxmid}')">${fwxmList.fwxmmc}</div>
					<!-- 遮盖层 -->
					<c:choose>
		               <c:when test="${fwxmList.permission!=1}"> 
		                	<div class="mask" onclick="showDialog('comfirm_dialog','${fwxmList.promptMsg}')"></div>
		               </c:when>
				    </c:choose> 
				  </div>
		  	  </c:forEach> 
		</div>
		<div class="clear"></div>
		<div class="ejtop" id="ejtop"></div>		 
		<div id="ejxmContainer" class="ejxmContainer">
		     
			 <!-- 内容异步加载 -->
			
		</div>
    </div>
	<!-- 弹出修改窗口 -->
    <div id="navtab1"  style="width: 100%; overflow: hidden; display: none" title="提示信息:">
         <div style="height: 30px">
             <div></div>
         </div>
         <div style="height: 80px">
             <table>                   
                 <tr>
                     <td align="right"></td>                       
                 </tr>
             </table>
         </div>
    </div>
    <!-- JS遮罩层 -->
	<div id="fullbg" class="fullbg"></div>
	<!-- end JS遮罩层 -->
	
	<!-- 确认对话框 -->
	<div  class="zzfw_dialog" id="comfirm_dialog" style="display: none">
	<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center"  class="dialog_title">提示信息</td>
			</tr>
			<tr>
				<td align="center" valign="middle" class="dialog_content"><div id="message"></div></td>
			</tr>
			<tr>
				<td align="center" style="cursor: pointer;"> 
                  <img onclick="closeDialog('comfirm_dialog')"  src="${ctx}/resources/image/zzfw/cancelback.gif"/>
				</td>
			</tr>		
		</table>
	</div>
	</div>
	
	<!-- 打印中对话框 -->
	<div  class="zzfw_dialog" id="tsxx_dialog" style="display: none">
	<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0" >
			
			<tr>
				<td align="center" valign="middle"  >
					<div style="padding-left: 240px;padding-top: 10px;float: left;"><img src="${ctx}/resources/image/zzfw/loading.gif"></div>
				</td>
			</tr>
			<tr>
				<td align="center" style="font-size: 16px;">信息读取中，请稍后...</td>
			</tr>
				
		</table>
	</div>
	</div>
</body>
</html>