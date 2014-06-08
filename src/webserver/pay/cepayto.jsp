<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@include file='tool.jsp' %>
<!-- 付款页面 data-->
<%
	URL url = new URL("http://cpay.wisedu.com/pay/itemDeal3.html");
	URLConnection conn = url.openConnection();
	conn.setDoInput(true);
	conn.setDoOutput(true);
	OutputStream ops= conn.getOutputStream();
	StringBuffer params = new StringBuffer();
	
	
	//事先准备好的数据
	String sysCert = "YOjfuOXlYogD50Sanebc5FSOiBSujqkR0Tgxq3GK5ejjhAHn2Ud2bT1P18Gkjc500n1sAJwgkvDkGBgRQRTc2RsVRGgDTYPbZ444vYaB1Wi0FqliIbseMx3NEDKPmmpW";
	String firstSC = sysCert.charAt(0) + "";
	String sysId = "xgxt";
	String itemId = "xgxt-01";
	
	//从请求中提取参数
	request.setCharacterEncoding("utf-8");
	String objId=request.getParameter("objId");  // 一般为学号或报名号
	String objName = request.getParameter("objName");//用户姓名
	//拓展编号,取当前时间毫秒数/10min毫秒数，在转化为32进制
	int tenMin=10*60*1000;
	String otherId=Long.toString(System.currentTimeMillis()/tenMin, 32);
	System.out.println(otherId);
	
	String amount = request.getParameter("amount");//金额 
	String returnType="data";//返回方式
	String autoSubmit="";//是否自动提交
	String specialValue = request.getParameter("specialValue");//业务系统的特殊文字
	String returnURL = request.getParameter("returnURL");//页面返回地址
	
	//签名，组装要发送的数据
	params.append("sign=").append(createSignString(
		      new String[]{
		         firstSC, sysId, firstSC, itemId, firstSC, objId, firstSC, otherId, firstSC, objName,
		         firstSC, amount, firstSC, firstSC, returnType, firstSC, specialValue, firstSC, returnURL,
		         sysCert
		      }
		));
		
	params.append("&sysId=").append(sysId);
	params.append("&itemId=").append(itemId);
	params.append("&objId=").append(objId);
	params.append("&objName=").append(URLEncoder.encode(objName, "UTF-8"));
	params.append("&otherId=").append(URLEncoder.encode(otherId, "UTF-8"));
	params.append("&amount=").append(amount);
	params.append("&returnType=").append(returnType);
	params.append("&autoSubmit=").append(autoSubmit);
	params.append("&specialValue=").append(URLEncoder.encode(specialValue, "UTF-8"));
	params.append("&returnURL=").append(URLEncoder.encode(returnURL, "UTF-8"));
	
	ops.write(params.toString().getBytes("UTF-8"));
	InputStream in = conn.getInputStream();
	InputStreamReader isr = new InputStreamReader(in, "UTF-8");
	char[] buf = new char[1024];
	int count = isr.read(buf);
	String result = new String(buf, 0, count);
	JSONObject jsonObj;
	Map<String, String> map = new HashMap<String, String>();
	jsonObj = new JSONObject(new JSONTokener(result));
	System.out.println("-----------"+jsonObj.toString());
	
	String json_returnCode = jsonObj.getString("returnCode");
	if(json_returnCode.equals("11")){
		//页面提示、日志记录或业务逻辑代码区	
		System.out.println("错误11：签名信息不正确");
		return;
	}
	if(json_returnCode.equals("12")){
		//页面提示、日志记录或业务逻辑代码区	
		System.out.println("错误12：两次信息不匹配，当收费记录已存在时会出现此错误提示");
		return;
	}
	if(json_returnCode.equals("13")){
		//页面提示、日志记录或业务逻辑代码区	
		System.out.println("错误13：错误的系统编号");
		return;
	}
	if(json_returnCode.equals("21")){
		//页面提示、日志记录或业务逻辑代码区	
		System.out.println("错误21：收费记录不存在，在删除时会出现此错误提示");
		return;
	}
	if(json_returnCode.equals("22")){
		//页面提示、日志记录或业务逻辑代码区	
		System.out.println("错误22：收费项目不存在");
		return;
	}
	if(json_returnCode.equals("23")){
		//页面提示、日志记录或业务逻辑代码区	
		System.out.println("错误23：收费记录无法删除，在删除时会出现此错误提示");
		return;
	}
	if(json_returnCode.equals("31")){
		//页面提示、日志记录或业务逻辑代码区	
				System.out.println("错误31：金额格式不正确");
		return;
	}
	if(json_returnCode.equals("32")){
		//页面提示、日志记录或业务逻辑代码区	
				System.out.println("错误32：otherId长度超出");
		return;
	}
	if(json_returnCode.equals("99")){
		//页面提示、日志记录或业务逻辑代码区	
				System.out.println("错误99：其他异常错误");
		return;
	}
	if(json_returnCode.equals("00") || json_returnCode.equals("01"))
	{
		//页面提示、日志记录或业务逻辑代码区	
		String json_sign = jsonObj.getString("sign");
		String json_sysId = jsonObj.getString("sysId");
		String json_itemId = jsonObj.getString("itemId");
		String json_objId = jsonObj.getString("objId");
		String json_otherId = jsonObj.getString("otherId");
		String json_objName = jsonObj.getString("objName");
		String json_amount = jsonObj.getString("amount");
		String json_projectId = jsonObj.getString("projectId");
		String json_payId = jsonObj.getString("payId");
		String json_payPassword = jsonObj.getString("payPassword");
		String json_specialValue = jsonObj.getString("specialValue");
		String json_returnURL = jsonObj.getString("returnURL");
	
		map.put("returnCode", json_returnCode);
		map.put("sign", json_sign);
		map.put("sysId", json_sysId);
		map.put("itemId", json_itemId);
		map.put("objId", json_objId);
		map.put("otherId", json_otherId);
		map.put("objName", json_objName);
		map.put("amount", json_amount);
		map.put("projectId", json_projectId);
		map.put("payId", json_payId);
		map.put("payPassword", json_payPassword);
		map.put("specialValue", json_specialValue);
		map.put("returnURL", json_returnURL);
	}
%>
<html>
<head>
<title>付款订单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${ctx}/resources/pay/css/style.css" type="text/css" rel="stylesheet" />
</head>

<body>
<center style=" font-size:23px">付款订单</center>
<br />
<table align="center">
	<tr>
		<td><div align="right">业务系统id:</div></td>
		<td><input type="text" name="sysId" id="sysId" readonly="readonly" value='<%=map.get("sysId") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">项目分类id:</div></td>
		<td ><input type="text" name="itemId" id="itemId" readonly="readonly" value='<%=map.get("itemId") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">收费对象id:</div></td>
		<td><input type="text" name="objId" id="objId" readonly="readonly" value='<%=map.get("objId") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">扩展id:</div></td>
		<td><input type="text" name="otherId" id="otherId" readonly="readonly" value='<%=map.get("otherId") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">收费对象姓名:</div></td>
		<td><input type="text" name="objName" id="objName" readonly="readonly"
			value='<%=map.get("objName") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">金　额:</div></td>
		<td ><input type="text" name="amount" id="amount" readonly="readonly" value='<%=map.get("amount") %>' /></td>
	</tr>
	<tr>
		<td ><div align="right">收费项目id:</div></td>
		<td><input type="text" name="projectId" id="projectId" readonly="readonly" value='<%=map.get("projectId") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">收费订单id:</div></td>
		<td><input type="text" name="payId" id="payId" readonly="readonly" value='<%=map.get("payId") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">支付码:</div></td>
		<td><input type="text" name="payPassword" id="payPassword" readonly="readonly" value='<%=map.get("payPassword") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">业务系统的特殊文字:</div></td>
		<td ><input type="text" name="specialValue" id="specialValue" readonly="readonly" value='<%=map.get("specialValue") %>' /></td>
	</tr>
	<tr>
		<td><div align="right">页面返回地址:</div></td>
		<td ><input type="text" name="returnURL" id="returnURL" readonly="readonly" value='<%=map.get("returnURL") %>' /></td>
	</tr>
	<tr>
		<td >
		  <div align="right"><a href="http://cpay.wisedu.com/pay/dealPay.html?pwd=<%=map.get("payPassword") %>">付款</a></div></td>
		<td ><div align="center"><a href="${ctx}/pay/index.jsp">返回</a></div></td>
	</tr>
</table>
</body>
</html>
