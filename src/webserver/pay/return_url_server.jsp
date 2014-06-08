<%@page contentType="text/html; charset=utf-8" %>
<%@include file='tool.jsp' %>
<!-- 服务器返回处理 -->
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
	if (mySign.equals(sign))
	{
	   // 核对金额
	   if (1==1)
	   {
	      // 返回信息
	      out.clear();
	      out.print("0");
	   }
	   else
	   {
	   		out.clear();
	      out.print("2");
	   }
	}
	else
	{
		 out.clear();
	   out.print("3");
	}

%>
