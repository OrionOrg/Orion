<%@page contentType="text/html; charset=utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!-- 查询页面 -->
<html>
<head>
<title>查询缴费</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
	body{filter:progid:DXImageTransform.microsoft.gradient(gradienttype=0,startColorStr=#EEF6F6,endColorStr=#84E4F9);}
	table {border-color: #000000;text}
	tr {height: 30px}
	td {height: 30px;}
	span {color:red;}
</style>
<script type="text/javascript" src="${ctx}/resources/pay/js/jquery-1.4.min.js"></script>
</head>
<body>
<center style="font-size: 23px">查询缴费</center>
<script>
$(document).ready(function(){
	var result = false;
	$("#hidden").hide();
	$("#batch").change(function (){
		if($("#batch").val() == "")
		{
			$("#requestobjId").html("<span>*</span>收费对象id：");
			$("#hidden").hide();
		}
		if($("#batch").val() == "Y")
		{
			$("#requestobjId").html("收费对象id：");
			$("#hidden").show();
		}
	});
	$("#issubmit").click(function() {
		if($("#batch").val() == "")
		{
			if ($("#objId").val() == "")
			{
				alert("收费对象id不能为空！");
			}
			else
				result = true;
		}
		if($("#batch").val() == "Y")
		{
			result = true;
		}
		if (result == true)
		{
			$("form").submit();
		}
	});
});
</script>

<form action="${ctx}/pay/queryreturn.jsp" method="post">
<table align="center">
	<tr>
		<td><div align="right">查询类型：</div></td>
		<td><select id="batch" name="batch">
		<option value="">单记录查询</option>
		<option value="Y">批量查询</option>
		</select></td>
	</tr>
	<tr>
		<td><div align="right" id='requestobjId'><span>*</span>收费对象id：</div></td>
		<td><input type="text" name="objId" id="objId"/></td>
	</tr>
	<tr>
		<td><div align="right" id='requestprojectId'>收费项目id：</div></td>
		<td><input type="text" name="projectId" id="projectId"/></td>
	</tr>
	<tr>
		<td><div align="right">扩展id：</div></td>
		<td><input type="text" name="otherId" id="otherId"/></td>
	</tr>
	<tr id="hidden">
		<td><div align="right">缴费状态：</div></td>
		<td><select id="status" name="status">
		<option value=""></option>
		<option value="0">未完成缴费</option>
		<option value="1">已完成缴费</option>
		</select></td>
	</tr>
	<tr>
		<td><input type="button" id="issubmit" value="查询"></td>
		<td><input type="button" value="返回 " onclick="location='${ctx}/pay/index.jsp'" /></td>
	</tr>
</table>
<p align="center">注：填写的收费项目id不存在将默认为当前收费项目id；</p>
</form>
</body>
</html>
