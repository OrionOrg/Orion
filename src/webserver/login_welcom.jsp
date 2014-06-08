<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>天津理工大学自助打印系统</title>
 <%@ include file="/common/widgets.jsp"%>
 <script type="text/javascript"> 
	
	 $(document).ready(function(){        
       		var window_width = 1680;   //22寸宽
	 		var window_height = 1050;   //22寸高
	 		$('body').css({width:window_width,height:window_height});
	 		$("#bg").css({width:window_width,height:window_height});	
     });
	
	function gotoLginPage(){
	  window.location.href = "${ctx}/login_zzfw.jsp";	
	}        
	     
</script>
</head>
<body>
<div  style="z-index:999;position: absolute;padding-left: 800px;padding-top: 768px;cursor: pointer;"><img src="resources/image/zzfw/touch.gif" onclick="gotoLginPage()"/></div>
<div id = "bg" ><img src="resources/image/zzfw/welcome.jpg" style="width: 100%;height: 100% ;cursor: pointer;" onclick="gotoLginPage()"/></div>
	
</body>
</html>