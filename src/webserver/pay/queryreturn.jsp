<%@ page language="java" contentType="text/html; charset=utf-8"	pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file='tool.jsp' %>
<!-- 查询缴费结果页面 -->
<%
request.setCharacterEncoding("utf-8");
URL url = new URL("http://cpay.wisedu.com/pay/queryPR3.html");
URLConnection conn = url.openConnection();
conn.setDoInput(true);
conn.setDoOutput(true);
OutputStream ops = conn.getOutputStream();
StringBuffer params = new StringBuffer();

String sysCert = "YOjfuOXlYogD50Sanebc5FSOiBSujqkR0Tgxq3GK5ejjhAHn2Ud2bT1P18Gkjc500n1sAJwgkvDkGBgRQRTc2RsVRGgDTYPbZ444vYaB1Wi0FqliIbseMx3NEDKPmmpW";
String firstSC = sysCert.charAt(0) + "";
String sysId = "xgxt";
String itemId = "xgxt-01";

String batch = request.getParameter("batch");
System.out.println("batch:" + batch);

String objId ="";
String otherId ="";
String projectId ="";
String status ="";

objId = request.getParameter("objId");
otherId = request.getParameter("otherId");
projectId = request.getParameter("projectId");
status = request.getParameter("status");

String sign=createSignString(new String[] { firstSC, sysId,
		firstSC, itemId, firstSC, objId, firstSC,otherId, firstSC,projectId,
		firstSC, batch, firstSC,status, sysCert });
params.append("sign=").append(sign);
params.append("&sysId=").append(sysId);
params.append("&itemId=").append(itemId);
params.append("&objId=").append(objId);
params.append("&otherId=").append(otherId);
params.append("&projectId=").append(projectId);
params.append("&batch=").append(batch);
params.append("&status=").append(status);
ops.write(params.toString().getBytes("UTF-8"));
System.out.println("params:"+params.toString());

InputStream in = conn.getInputStream();
InputStreamReader isr = new InputStreamReader(in, "UTF-8");
BufferedReader br =  new BufferedReader(isr);
String result="";
String str = "";
while ((str = br.readLine()) != null) {
	result += str+"\n";
}
// 当读取的一行不为空时,把读到的str的值赋给result
//System.out.println("result:"+result);// 打印出result

List<Map<String, String>> list = new ArrayList<Map<String, String>>();
Map<String, String> mapt = new HashMap<String, String>();
Map<String, String> map_dan = new HashMap<String, String>();
if ("Y".equals(batch)) 
{
	String[] param_array = result.split("\n");
	for (int i = 0; i < param_array.length; i++) 
	{
		JSONObject jsonObj = new JSONObject(new JSONTokener(param_array[i]));
		// 如果是批量查询
			// 摘要信息
		if (i == 0) 
		{
			String json_returnCode = jsonObj.getString("returnCode");
			if(json_returnCode=="11"){
				//页面提示、日志记录或业务逻辑代码区
				System.out.println("错误11：签名信息不正确");
				return;
			}
			if(json_returnCode=="13"){
				//页面提示、日志记录或业务逻辑代码区
				System.out.println("错误13：错误的系统编号");
				return;
			}
			if(json_returnCode=="99"){
				//页面提示、日志记录或业务逻辑代码区
				System.out.println("错误99：其他异常错误");
				return;
			}
			String json_sign = jsonObj.getString("sign");
			String json_sysId = jsonObj.getString("sysId");
			String json_itemId = jsonObj.getString("itemId");
			String json_projectId = jsonObj.getString("projectId");
			String json_count = jsonObj.getString("count");
			String json_msg = jsonObj.getString("msg");

			mapt.put("returnCode", json_returnCode);
			mapt.put("sign", json_sign);
			mapt.put("sysId", json_sysId);
			mapt.put("itemId", json_itemId);
			mapt.put("projectId", json_projectId);
			mapt.put("count", json_count);
			mapt.put("msg", json_msg);
		}
		// 记录信息
		else 
		{

			Map<String, String> map = new HashMap<String, String>();
			String json_sign = jsonObj.getString("sign");
			String json_objId = jsonObj.getString("objId");
			String json_otherId = jsonObj.getString("otherId");
			String json_objName = jsonObj.getString("objName");
			String json_amount = jsonObj.getString("amount");
			String json_paid = jsonObj.getString("paid");
			String json_refund = jsonObj.getString("refund");
			String json_overTime = jsonObj.getString("overTime");
			String json_status = jsonObj.getString("status");
			String json_payId = jsonObj.getString("payId");
			String json_payPassword = jsonObj.getString("payPassword");
			String json_specialValue = jsonObj.getString("specialValue");
			String json_payType = jsonObj.getString("payType");

			map.put("sign", json_sign);
			map.put("objId", json_objId);
			map.put("otherId", json_otherId);
			map.put("objName", json_objName);
			map.put("amount", json_amount);
			map.put("paid", json_paid);
			map.put("refund", json_refund);
			map.put("overTime", json_overTime);
			map.put("status", json_status);
			map.put("payId", json_payId);
			map.put("payPassword", json_payPassword);
			map.put("specialValue", json_specialValue);
			map.put("payType", json_payType);
			list.add(map);
		}
	}
} else 
{
	//单记录查询
	JSONObject jsonObj = new JSONObject(new JSONTokener(result));
	String json_returnCode = jsonObj.getString("returnCode");
	if(json_returnCode=="11"){
		//页面提示、日志记录或业务逻辑代码区
		System.out.println("错误11：签名信息不正确");
		return;
	}
	if(json_returnCode=="13"){
		//页面提示、日志记录或业务逻辑代码区
		System.out.println("错误13：错误的系统编号");
		return;
	}
	if(json_returnCode=="21"){
		//页面提示、日志记录或业务逻辑代码区
		System.out.println("错误21：收费记录不存在");
		return;
	}
	if(json_returnCode=="99"){
		//页面提示、日志记录或业务逻辑代码区
		System.out.println("错误99：其他异常错误");
		return;
	}
	if(json_returnCode.equals("02")||json_returnCode.equals("03"))
	{
		//页面提示、日志记录或业务逻辑代码区
		String json_sign = jsonObj.getString("sign");
		String json_sysId = jsonObj.getString("sysId");
		String json_itemId = jsonObj.getString("itemId");
		String json_otherId = jsonObj.getString("otherId");
		String json_objId = jsonObj.getString("objId");
		String json_amount = jsonObj.getString("amount");
		String json_objName = jsonObj.getString("objName");
		String json_projectId = jsonObj.getString("projectId");
		String json_paid = jsonObj.getString("paid");
		String json_refund = jsonObj.getString("refund");
		String json_overTime = jsonObj.getString("overTime");
		String json_status = jsonObj.getString("status");
		String json_payId = jsonObj.getString("payId");
		String json_payPassword = jsonObj.getString("payPassword");
		String json_specialValue = jsonObj.getString("specialValue");
		String json_payType = jsonObj.getString("payType");
		
		map_dan.put("sign", json_sign);
		map_dan.put("sysId", json_sysId);
		map_dan.put("itemId", json_itemId);
		map_dan.put("objId", json_objId);
		map_dan.put("otherId", json_otherId);
		map_dan.put("objName", json_objName);
		map_dan.put("amount", json_amount);
		map_dan.put("paid", json_paid);
		map_dan.put("refund", json_refund);
		map_dan.put("overTime", json_overTime);
		map_dan.put("status", json_status);
		map_dan.put("projectId", json_projectId);
		map_dan.put("payId", json_payId);
		map_dan.put("payPassword", json_payPassword);
		map_dan.put("specialValue", json_specialValue);
		map_dan.put("payType", json_payType);
	}
	map_dan.put("returnCode", json_returnCode);
}%>
<html>
<head>
<title>缴费结果显示</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<link href="${ctx}/resources/pay/css/style.css" type="text/css" rel="stylesheet" />
<body>
<a href="${ctx}/pay/queryform.jsp">返回</a>
<%if(batch==""){ %>
	<center style=" font-size:23px">单记录缴费结果显示</center>
	<table align="center" border="1">
			<tr><td><div align="right">信息提示：</div></td><td>
			<%if(map_dan.get("returnCode").equals("02")){%>
			<span style="color: red">已付</span>
			<%}else if(map_dan.get("returnCode").equals("03")){%>
			<span style="color: red">未付</span>
			<%}else if(map_dan.get("returnCode").equals("11")){%>
			<span style="color: red">签名信息不正确</span>
			<%}else if(map_dan.get("returnCode").equals("13")){%>
			<span style="color: red">错误的系统编号</span>
			<%}else if(map_dan.get("returnCode").equals("21")){%>
			<span style="color: red">收费记录不存在</span>
			<%}else{%>
			<span style="color: red">其他异常错误</span>
			<%}%>
			</td></tr>
			<tr>
				<td  align="center"><div align="right">收费对象id：</div></td>
				<td  align="left">
				<%=map_dan.get("objId") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">收费对象姓名：</div></td>
				<td  align="left">
				<%=map_dan.get("objName") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">收费项目id：</div></td>
				<td  align="left">
				<%=map_dan.get("projectId") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">扩展id：</div></td>
				<td  align="left">
				<%=map_dan.get("otherId") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">金额：</div></td>
				<td  align="left">
				<%=map_dan.get("amount") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">已付金额：</div></td>
				<td  align="left">
				<%=map_dan.get("paid") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">退款金额：</div></td>
				<td  align="left">
				<%=map_dan.get("refund") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">订单号：</div></td>
				<td  align="left">
				<%=map_dan.get("payId") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">支付码：</div></td>
				<td  align="left">
				<%=map_dan.get("payPassword") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">付款完成时间：</div></td>
				<td  align="left">
				<%=map_dan.get("overTime") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">付款状态：</div></td>
				<td  align="left">
				<%if(map_dan.get("status")!=null){
				if(Integer.parseInt(map_dan.get("status"))==0){%>
				未完成缴费
				<%}else if(Integer.parseInt(map_dan.get("status"))==1){%>
					已完成缴费
				<%}else if(Integer.parseInt(map_dan.get("status"))==2){%>
					结单
				<%}
				}%>
				</td>
			</tr>
			<tr>
				<td  align="center"><div align="right">付款方式：</div></td>
				<td  align="left">
				<%=map_dan.get("payType") %></td>
			</tr>
			<tr>
				<td  align="center"><div align="right">业务系统特殊文字：</div></td>
				<td  align="left">
				<%=map_dan.get("specialValue") %></td>
			</tr>
		</table>
<%}else{%>
	<center style=" font-size:23px">多记录缴费结果显示</center>
	<h3 align="center">信息提示：
	<%if(mapt.get("returnCode").equals("05")){%>
			<span style="color: red">批量查询完成</span>
	<%}else if(mapt.get("returnCode").equals("11")){%>
			<span style="color: red">签名信息不正确</span>
			<%}else if(mapt.get("returnCode").equals("13")){%>
			<span style="color: red">错误的系统编号</span>
			<%}else{%>
			<span style="color: red">其他异常错误</span>
			<%}%>
			</h3>
		<h3 align="center">查询出的记录总数:<span style="color: blue"><%=mapt.get("count") %></span>条，收费项目id：<%=mapt.get("projectId") %></h3>
	
	<table align="center" border="1" style="width:100%">
			<tr>
				<td align="center">序号</td>
				<td align="center">收费对象id</td>
				<td align="center">扩展id</td>
				<td  align="center">用户姓名</td>
				<td  align="center">金额</td>
				<td  align="center">已付金额</td>
				<td  align="center">退款金额</td>
				<td  align="center">订单号</td>
				<td  align="center">付款完成时间</td>
				<td  align="center">付款状态</td>
				<td  align="center">付款方式</td>
				<td  align="center">支付码</td>
				<td  align="center">业务系统特殊文字</td>
			</tr>
			<c:forEach items="<%=list %>" var="re" varStatus="curr" >
			<tr>	
				<td  align="left">
				${curr.index+1}</td>
				<td  align="left">
				${re.objId}</td>
				<td  align="left">
				${re.otherId}</td>
				
				<td  align="left">
				${re.objName}</td>
				<td  align="left">
				${re.amount}</td>
				<td  align="left">
				${re.paid}</td>
				<td  align="left">
				${re.refund}</td>
				<td  align="left">
				${re.payId}</td>
				<td  align="left">
				${re.overTime}</td>
				<td  align="left">
				<c:choose>
					<c:when test="${re.status == 0}">
					未完成缴费
					</c:when>
					<c:when test="${re.status == 1}">
					已完成缴费
					</c:when>
					<c:when test="${re.status == 2}">
					结单
					</c:when>
				</c:choose>
				</td>
				<td  align="left">
				${re.payType}</td>
				<td  align="left">
				${re.payPassword}</td>
				<td  align="left">
				${re.specialValue}</td>
			</tr>
		</c:forEach>
		</table>
<%} %>
<center><a href="${ctx}/pay/queryform.jsp">返回</a></center>
</body>
</html>
