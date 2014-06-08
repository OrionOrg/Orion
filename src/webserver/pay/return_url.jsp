<%@page contentType="text/html; charset=utf-8" %>
<%@include file='tool.jsp' %>
<!-- 付款返回结果页面(此为页面返回,服务器返回参见return_url_server.jsp) -->
<%
request.setCharacterEncoding("utf-8");
String sign = request.getParameter("sign");
String version = request.getParameter("version");
String sysId = request.getParameter("sysId");
String itemId = request.getParameter("itemId"); 
String objId = request.getParameter("objId");
String otherId = request.getParameter("otherId");
String objName = request.getParameter("objName");
String amount = request.getParameter("amount");
String paid = request.getParameter("paid");
String refund = request.getParameter("refund");
String overTime = request.getParameter("overTime");
String status = request.getParameter("status");
String projectId = request.getParameter("projectId");
String payId = request.getParameter("payId");
String payPassword = request.getParameter("payPassword");
String specialValue = request.getParameter("specialValue");
String payType = request.getParameter("payType");
String sysCert = "27ysZAzp5sHI21Tp6jSg2an41Br0F1gFEqvgnk2A2FpLqcNLnoPDhExwuTrPmrHkktHVXZVk6LTMCq98YdduD1pWGScp1Irnvhjs8Hk4pbKV8laH2ExhltiLorzEY2IY";
String firstSC = sysCert.charAt(0) + "";
String mySign = null;
	mySign = createSignString(
	      new String[]{
	         firstSC, version, firstSC, sysId, firstSC, itemId, firstSC, objId, firstSC, otherId,
	         firstSC, objName, firstSC, amount, firstSC, paid, firstSC, refund, firstSC, overTime,
	         firstSC, status, firstSC, projectId, firstSC, payId, firstSC, payPassword,
	         firstSC, specialValue, firstSC, payType, sysCert
	      }
	);
// 核对签名
System.out.println("mySign:"+mySign);
System.out.println("  sign:"+sign);
String content="";
String result = "";
	String fanhui="<a href='/pay/index.jsp'>返回</a>";
	if (sign.equals(mySign))
	{
	   // 核对金额 pay_Item.getAmount() == Double.parseDouble(amount)
	   if (1==1)
	   {
	      // 返回信息，页面提示、日志记录或业务逻辑代码区
	      result = "支付成功！";
	   }
	   else
	   {
		   //页面提示、日志记录或业务逻辑代码区
		   result = "金额不匹配！";
	   }
	}
	else
	{
		//页面提示、日志记录或业务逻辑代码区
		result = "签名不正确！";
	}
	
	content="<html><head><title>付款结果</title><meta http-equiv='Content-Type'content='text/html; charset=UTF-8'>"+
	"<link href='../css/style.css' type='text/css' rel='stylesheet' />"+
	"</head><body><center>付款结果</center><table align='center'>"+
	"<tr><td><div align='right'>付款结果：</div></td><td>"+result+"</td></tr>"+
	"<tr><td><div align='right'>拓展id：</div></td><td>"+otherId+"</td></tr>"+
	"<tr><td><div align='right'>项目编号：</div></td><td>"+projectId+"</td></tr>"+
	"<tr><td><div align='right'>订单号：</div></td><td>"+payId+"</td></tr>"+
	"<tr><td><div align='right'>用户ID：</div></td><td>"+objId+"</td></tr>"+
	"<tr><td><div align='right'>用户名：</div></td><td>"+objName+"</td></tr>"+
	"<tr><td><div align='right'>金额：</div></td><td>"+amount+"</td></tr>"+
	"<tr><td><div align='right'>付款完成时间：</div></td><td>"+overTime+"</td></tr>"+
	"<tr><td><div align='right'>业务系统的特殊文字：</div></td><td>"+specialValue+"</td></tr>"+
	"<tr><td><div align='right'>支付方式：</div></td><td>"+payType+"</td></tr></table>"+
	"<center>"+fanhui+"<center></body></html>";
	response.reset();
    out.clear();
    out=pageContext.pushBody();
	response.getOutputStream().write(content.getBytes("UTF-8"));
%>
