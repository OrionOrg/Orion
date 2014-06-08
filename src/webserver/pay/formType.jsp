<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@include file='tool.jsp' %>
<!-- 付款页面 form -->
<%
//事先准备好的数据
String sysCert = "YOjfuOXlYogD50Sanebc5FSOiBSujqkR0Tgxq3GK5ejjhAHn2Ud2bT1P18Gkjc500n1sAJwgkvDkGBgRQRTc2RsVRGgDTYPbZ444vYaB1Wi0FqliIbseMx3NEDKPmmpW";
String firstSC = sysCert.charAt(0) + "";
String sysId = "xgxt";
String itemId = "xgxt-01";

StringBuffer params = new StringBuffer();
//从请求中提取参数
request.setCharacterEncoding("utf-8");
String objId=request.getParameter("objId");  // 一般为学号或报名号
String objName = request.getParameter("objName");//用户姓名
String otherId = request.getParameter("otherId");//拓展编号
//拓展编号,取当前时间毫秒数/10min毫秒数，在转化为32进制
int tenMin=10*60*1000;
otherId=Long.toString(System.currentTimeMillis()/tenMin, 32);
System.out.println(otherId);
String amount = request.getParameter("amount");//金额 
String returnType="form";//返回方式
String autoSubmit="Y";//是否自动提交
String specialValue = request.getParameter("specialValue");//业务系统的特殊文字
String returnURL = request.getParameter("returnURL");//页面返回地址
String sign = "";
sign = createSignString(
	      new String[]{
	 	         firstSC, sysId, firstSC, itemId, firstSC, objId, firstSC, otherId, firstSC, objName,
	 	         firstSC, amount, firstSC, firstSC, returnType, firstSC, specialValue, firstSC, returnURL,
	 	         sysCert
	 	      });

String content="";

content="<html><head><title>付款结果</title><meta http-equiv='Content-Type'content='text/html; charset=UTF-8'>"+
"<link href='css/style.css' type='text/css' rel='stylesheet' />"+
"<script>function load(){document.myform.submit();}</script>"+
"</head><body onload='load()'><center>付款</center>"+
"<form name='myform' action='http://cpay.wisedu.com/pay/itemDeal3.html' method='post'>"+
"<table align='center'><tr><td align='right'>签名：</td><td>"+
"<input type='text' name='sign' value="+sign+">"+
"</td></tr>"+
"<tr><td  align='right'>业务系统id：</td><td>"+
"<input type='text' name='sysId' value="+sysId+">"+
"</td></tr>"+
"<tr><td  align='right'>项目分类id：</td><td>"+
"<input type='text' name='itemId' value="+itemId+">"+
"</td></tr>"+
"<tr><td  align='right'>付费对象编号：</td><td>"+
"<input type='text' name='objId' value="+objId+">"+
"</td></tr>"+
"<tr><td  align='right'>付费对象名称：</td><td>"+
"<input type='text' name='objName' value="+objName+">"+
"</td></tr>"+
"<tr><td  align='right'>拓展编号：</td><td>"+
"<input type='text' name='otherId' value="+otherId+">"+
"</td></tr>"+
"<tr><td  align='right'>金额：</td><td>"+
"<input type='text' name='amount' value="+amount+">"+
"</td></tr>"+
"<tr style='display:none'><td  align='right'>返回类型：</td><td>"+
"<input type='text' name='returnType' value="+returnType+">"+
"</td></tr>"+
"<tr><td  align='right'>是否自动提交：</td><td>"+
"<select name='autoSubmit'><option value=''>否</option><option value='Y'>是</option></select>"+
"</td></tr>"+
"<tr><td  align='right'>特殊描述：</td><td>"+
"<input type='text' name='specialValue' value="+specialValue+">"+
"</td></tr>"+
"<tr><td align='right'>返回地址：</td><td>"+
"<input type='text' name='returnURL' value="+returnURL+">"+
"</td></tr>"+
"<tr><td align='right'></td><td>"+
"<input type='submit' value='支付'>"+
"</td><td>"+
"<input type='button' value='返回' onclick='location=\"${ctx}/pay/index.jsp\"'>"+
"</td></tr>"+
"</table>"+
"</form>"+
"</body></html>";
response.reset();
out.clear();
out=pageContext.pushBody();
response.getOutputStream().write(content.getBytes("UTF-8"));
%>