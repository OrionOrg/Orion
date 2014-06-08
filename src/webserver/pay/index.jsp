<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<!-- 生成订单页面 -->
<html>
	<head>
		<title>付款输入项</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link href="${ctx}/resources/pay/css/style.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="${ctx}/resources/pay/js/jquery-1.4.min.js"></script>
		<script>
		function check()
		{
			var amount  = $("#amount").val();
			if ($("#objId").val() == "")
			{
				alert("用户编号不能为空！");
				return false;
			}
			else if ($("#objName").val() == "")
			{
				alert("用户姓名不能为空！");
				return false;
			}
			else if ($("#amount").val() == ""||isNaN(amount))
			{
				alert("金额不正确！");
				return false;
			}
			else
				return true;
		}
		function check2()
		{
			var amount  = $("#amount2").val();
			if ($("#objId2").val() == "")
			{
				alert("用户编号不能为空！");
				return false;
			}
			else if ($("#objName2").val() == "")
			{
				alert("用户姓名不能为空！");
				return false;
			}
			else if ($("#amount2").val() == ""||isNaN(amount))
			{
				alert("金额不正确！");
				return false;
			}
			else
				return true;
		}
		</script>
	</head>
	<body>
		<h4><a href="${ctx}/pay/queryform.jsp">查询缴费结果</a></h4>
		<table align='center'><tr><td>
		<center style=" font-size:23px">付款输入项data形式</center>
		<form action="${ctx}/pay/cepayto.jsp" method="post" onsubmit="return check();">
		<table border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td><div align="right">用户编号：</div></td><td><input type="text" id="objId" name="objId"/><span>*</span></td>
				</tr>
			<tr>
				<td><div align="right">用户姓名：</div></td><td><input type="text" id="objName" name="objName" /><span>*</span></td>
				</tr>
			<tr>
				<td><div align="right">金额：</div></td><td><input type="text" id="amount" name="amount"/><span>*</span></td>
				</tr>
			<tr>
				<td><div align="right">业务系统的特殊文字：</div></td><td><input type="text" id="specialValue" name="specialValue"/></td>
				</tr>
			<tr>
				<td><div align="right">页面返回地址：</div></td><td><input type="text" id="returnURL" name="returnURL" value="http://127.0.0.1:8080/ssfw/pay/return_url.jsp"/><span>*</span></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="生成订单"></td>
			</tr>
			<tr>			</tr>
		</table>
		</form>
		</td>
		<td width="100px"></td>
		<td>
		<center style=" font-size:23px">付款输入项form形式</center>
		<form action="${ctx}/pay/formType.jsp" method="post" onsubmit="return check2();">
		<table border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td><div align="right">用户编号：</div></td><td><input type="text" id="objId2" name="objId"/><span>*</span></td>
				</tr>
			<tr>
				<td><div align="right">用户姓名：</div></td><td><input type="text" id="objName2" name="objName" /><span>*</span></td>
				</tr>
			<tr>
				<td><div align="right">金额：</div></td><td><input type="text" id="amount2" name="amount"/><span>*</span></td>
				</tr>
			<tr>
				<td><div align="right">业务系统的特殊文字：</div></td><td><input type="text" id="specialValue2" name="specialValue"/></td>
				</tr>
			<tr>
				<td><div align="right">页面返回地址：</div></td><td><input type="text" id="returnURL2" name="returnURL" value="http://127.0.0.1:8080/ssfw/pay/return_url.jsp"/><span>*</span></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input type="submit" value="提交"></td>
			</tr>
		</table>
		</form>
		</td></tr></table>
	</body>
</html>
