<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>自助服务</title>
 <%@ include file="/common/widgets.jsp"%>
 <link href="${ctx}/resources/css/zzfw.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript">          
            
    	 function gotoFunc(fwxmid){ 
         	//异步调用doOnclick 事件
	    	$.ajax({
					async : true,
					type : 'POST',
					url : '${ctx}/zzfw/framework/fwdl/doOnclick.do',
					data : {fwdlId : fwxmid},
					dataType : 'json',
					success : function(data){
                      if (data.jsonStr) {    
                      		var obj = eval( "(" + data.jsonStr + ")" );                       	 
                      		if(obj.permission==1){   //有权限访问                   		 
                      		   window.location = "${ctx}/zzfw/framework/fwxm/show.do?ffwxmid="+fwxmid;   
                      		}else{ 	 //没有权限访问，给出提示信息
                           		var Message = obj.promptMsg;
                       			$("#message").html(Message);
                             	$( "#navtab1" ).dialog({
							        width:400,
							        height:200,
							        modal:true,   
							        buttons: {						           
							            '关闭': function() {
							                $( this ).dialog( "close" );
							            }
							        }
						   		 });  
                      		}
                       }
                   }					
	    	});
    	 }
    </script>
</head>
<body>
<div class="huanying"><strong>请选择项目：</span></strong></div>
<div align="center" style="width: 100%;height: 100%"> 
     <c:forEach items="${fwdlList}" var="fwdlList" varStatus="s">    
	      <c:choose>
               <c:when test="${fwdlList.permission==1}"> <div class="IconContainer"></c:when>
               <c:otherwise><div class="IconContainerUnable"></c:otherwise>
	      </c:choose>
	     
			<div class="IconImage"><img alt="${fwdlList.fwxmmc}" src="${ctx}/resources/image/zzfw/${fwdlList.icon}"  onclick="gotoFunc('${fwdlList.fwxmid}')" /></div>
			
			<div class="IconName"><a onclick="gotoFunc('${fwdlList.fwxmid}')" >${fwdlList.fwxmmc}</a></div>
		</div>		 
  	  </c:forEach> 
</div>
 <!-- 弹出修改窗口 -->
 <div id="navtab1"  style="width: 100%; overflow: hidden; display: none" title="提示信息:">
     <div style="height: 30px">
         <div></div>
     </div>
     <div style="height: 80px">
         <table>                   
             <tr>
                 <td align="right"><div id="message"></div></td>                       
             </tr>
         </table>
     </div>
 </div>
</body>
</html>