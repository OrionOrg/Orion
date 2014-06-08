<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>自助服务平台 - 自助服务设置</title>
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/themes/icon.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/demo.css" />
<script type="text/javascript"
	src="${ctx}/resources/js/jquery.easyui.min.js"></script>

<script type="text/javascript" defer="true"><!-- 
	var gPageSize = 10;
	var sDylx = "";
	var sStartDate="";
	var sEndDate="";
	var pSize="";
	var pNum="";
	var dg = $('#dg');
	var pager = dg.datagrid().datagrid('getPager');
	//实例化
	$(document).ready(function(){
		
		var dztjWin = $('#dztj-window').window({  
		    closed:true  
		});
		var pager1 = dg.datagrid().datagrid('getPager');
			pager1.pagination({
	        pageSize: gPageSize,//每页显示的记录条数，默认为10  
		    pageList: [10,20,30,40,50,100],//可以设置每页记录条数的列表  
		    beforePageText: '第',//页数文本框前显示的汉字  
		    afterPageText: '页    共 {pages} 页',  
		    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
		    loading:true
	    });
});
function searchDztj(){

		dg.datagrid('loadData', { total: 0, rows: [] }); 
		sDylx =$("#dylx").combobox("getValue");
		sDylx = encodeURI(sDylx);
		sDylx = encodeURI(sDylx);
		sStartDate= $("#startDate").datebox("getValue");
		sEndDate= $("#endDate").datebox("getValue");
		pSize = $('#rows').val();
		pNum= $('#page').val();
		dg.datagrid('options').url='${ctx}/admin/getDztjList.do';
	    dg.datagrid('options').method='post';
	    dg.datagrid('options').dataType='json';

		var queryParams = dg.datagrid('options').queryParams;  
        queryParams.rows = pSize;  
        queryParams.page = 1;  
        queryParams.dylx = sDylx;
        queryParams.startDate = sStartDate;
        queryParams.endDate = sEndDate;
        //重新加载datagrid的数据  
        dg.datagrid('reload');  
}
	

function showDztj(){
	//
	$('#dztj-window').window({ 
	 			closed:false  
		});
	var row = $('#dg').datagrid('getSelected'); 
    if (row){
    	$("#fwlx").val(row.fwxmmc);
		$("#yhbh").val(row.bh);
		$("#yhxm").val(row.name);
		$("#jfsj").val(row.jfsj);
		$("#dyfs").val(row.dyfs);
		$("#dyfy").val(row.dyfy);
		$("#mffs").val(row.mffs);
		$("#price").val(row.price);
		$("#jykh").val(row.jykh);
		$("#jylsh").val(row.yjlsh);
		$("#bz").val(row.bz);
		$("#dysbxx").val(row.dysbxx);
		
	}
	
}
function closeXtrz(){ 
   $('#dztj-window').window({ 
  			closed:true  
	});    
} 
--></script>
</head>
<body>
<div align="center">
<div
	style="font-family: 微软雅黑;font-size: 16px;font-weight: bold;padding-bottom:20px;">自助服务对账统计</div>
<div id="queryContent"
	style="width: auto;height: auto; padding-bottom: 20px;">
<form id="serachForm" action=""><input id="page" name="page"
	type="hidden" value="1" /> <input id="rows" name="rows" type="hidden"
	value="10" />
<table style="width:80%;height: auto;" border="0">
	<tr>
		<td width="20px;"></td>
		<td width="80px" align="right">打印类型:&nbsp;</td>
		<td width="80px" align="left"><input class="easyui-combobox"
			id="dylx" name="dylx" style="width: 150px"
			data-options="
				  method:'post',
				  valueField:'id',
				  textField:'text',
				  url:'${ctx}/admin/getDylx.do'" /></td>
		<td>&nbsp;</td>
		<td width="60px" align="right">开始时间:&nbsp;</td>
		<td width="120px" align="left"><input id="startDate"
			name="startDate" class="easyui-datebox" /></td>
		<td>&nbsp;</td>
		<td width="60px" align="right">结束时间:&nbsp;</td>
		<td width="120px" align="left"><input id="endDate" name="endDate"
			class="easyui-datebox" /></td>
		<td width="20px;"></td>
		<td align="left"><a href="javascript:void(0)"
			class="easyui-linkbutton" onclick="searchDztj()" style="width: 80px;">查询</a></td>
	</tr>

</table>
</form>
</div>


<div style="padding-left: 200px">
<table id="dg" title="" class="easyui-datagrid"
	style="width:auto;height:auto"
	data-options="
            rownumbers:true,
            singleSelect:true,
            pagination:true,
            fitColumns:'true',
            loadMsg:'数据加载中请稍后……',  
            url:'${ctx}/admin/getDztjList.do',
            method:'post'
            ">
	<thead>
		<tr>
			<th data-options="field:'fwxmmc',width:100,align:'center'">打印类型</th>
			<th data-options="field:'bh',width:100,align:'center'">用户编号</th>
			<th data-options="field:'name',width:100,align:'center'">用户姓名</th>
			<th data-options="field:'dyfs',width:100,align:'center'">打印份数</th>
			<th data-options="field:'dyfy',width:100,align:'center'">打印费用</th>
			<th data-options="field:'jylsh',width:100,align:'center'">交易流水号</th>
			<th data-options="field:'jfsj',width:120,align:'center'">缴费时间</th>
			<th data-options="field:'cz',width:60,align:'center'">查看</th>
		</tr>
	</thead>
</table>
</div>

<div id="dztj-window" title="对账信息" style="width:600px;height:500px;">
<div style="padding:20px 20px 40px 80px;">
<form method="post" id="">
<table>
	<tr>
		<td align="right">自助服务类型：</td>
		<td colspan="4"><input id="fwlx" type="text"
			class="easyui-validatebox textbox" style="width:310px"
			disabled="disabled"></td>
	</tr>
	<tr>
		<td align="right">用户编号：</td>
		<td><input id="yhbh" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>
		<td>&nbsp;</td>
		<td align="right">用户姓名：</td>
		<td><input id="yhxm" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>

	</tr>
	<tr>
		<td align="right">打印份数：</td>
		<td><input id="dyfs" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>
		<td>&nbsp;</td>
		<td align="right">免费份数：</td>
		<td><input id="mffs" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>
	</tr>
	<tr>
		<td align="right">单份费用：</td>
		<td><input id="price" type="text"
			class="easyui-validatebox textbox" style="width:100px"
			disabled="disabled"> <font color="red">元</font></td>
		<td>&nbsp;</td>
		<td align="right">打印费用：</td>
		<td><input id="dyfy" type="text"
			class="easyui-validatebox textbox" style="width:100px"
			disabled="disabled"> <font color="red">元</font></td>
	</tr>
	<tr>
		<td align="right">交易流水号：</td>
		<td><input id="jylsh" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>
		<td>&nbsp;</td>
		<td align="right">交易卡号：</td>
		<td><input id="jykh" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>
	</tr>
	<tr>
		<td align="right">缴费时间：</td>
		<td colspan="4"><input id="jfsj" type="text"
			class="easyui-validatebox textbox" style="width:120px"
			disabled="disabled"></td>
	</tr>
	<tr>
		<td valign="top" align="right">打印失败信息：</td>
		<td colspan="4"><textarea id="dysbxx" rows="" cols=""
			class="easyui-validatebox textbox" style="width:310px;height: 100px;"
			disabled="disabled"></textarea></td>
	</tr>
	<tr>
		<td valign="top" align="right">备注：</td>
		<td colspan="4"><textarea id="bz" rows="" cols=""
			class="easyui-validatebox textbox" style="width:310px;height: 100px;"
			disabled="disabled"></textarea></td>
	</tr>
</table>
</form>
</div>
<div style="text-align:center;padding:5px;"><a
	href="javascript:void(0)" onclick="closeXtrz()"
	class="easyui-linkbutton" id="cancel" icon="icon-cancel">关闭</a></div>
</div>
</div>
</body>
</html>
