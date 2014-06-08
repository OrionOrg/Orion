<%@page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>

<head>

<title>自助服务平台 - 自助服务设置</title>
<link rel="stylesheet" type="text/css" href="${ctx}/resources/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/resources/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/demo.css" />
<script type="text/javascript" src="${ctx}/resources/js/jquery.easyui.min.js"></script>

<script>  

	var gPageSize = 10;
	var sCzlx = "";
	var sCzr = "";
	var sIp = "";
	var sStartDate="";
	var sEndDate="";
	var pSize="";
	var pNum="";
	var dg = $('#dg');

	//实例化
	$(document).ready(function(){
		var xtrzWin = $('#xtrz-window').window({  
		    closed:true  
		});
		var pager1 = $('#dg').datagrid().datagrid('getPager');    // get the pager of datagrid
	    pager1.pagination({
	         	pageSize: gPageSize,//每页显示的记录条数，默认为10  
			    pageList: [10,20,30,40,50,100], 
			    beforePageText: '第', 
			    afterPageText: '页    共 {pages} 页',  
			    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',
			    loading:true
	      });
	});

	function showXtrz(){
		var row = $('#dg').datagrid('getSelected'); 
	    if (row){
			$("#userid").val(row.userid);
			$("#czlx2").val(row.czlx);
			$("#czms2").val(row.czms);
			$("#czsj").val(row.cssj);
			$("#sbbh").val(row.sbbh);
			$("#ip2").val(row.ip);
			$('#xtrz-window').window({ 
		 			closed:false  
			});
		}
	}
	
	function closeXtrz(){ 
	   $('#xtrz-window').window({ 
	  			closed:true  
		});    
	}
	
	function searchXtrz(){
		$('#dg').datagrid('loadData', { total: 0, rows: [] });
		sCzlx =$("#czlx").combobox("getValue");
		sCzlx = encodeURI(sCzlx);
		//sCzlx = encodeURI(sCzlx);
		sCzr= $('#czr').val();
		sCzr = encodeURI(sCzr);
		//sCzr = encodeURI(sCzr);
		sIp= $('#ip').val();
		sStartDate= $("#startDate").datebox("getValue");
		sEndDate= $("#endDate").datebox("getValue");
		pSize = $('#rows').val();
		pNum= $('#page').val();
		
		$('#dg').datagrid('options').url="${ctx}/admin/getXtrzList.do";  
	    $('#dg').datagrid('options').method='post';
	    $('#dg').datagrid('options').dataType='json';
	    var queryParams = $('#dg').datagrid('options').queryParams;
	    queryParams.czlx = sCzlx; 
	    queryParams.rows = pSize;  
	    queryParams.page = 1;  
	    queryParams.czr = sCzr;
	    queryParams.ip = sIp;
	    queryParams.startdate = sStartDate;
	    queryParams.enddate = sEndDate;
	    $('#dg').datagrid('reload');
	} 
</script>
</head>
<body>
<div align="center">
<div style="font-family: 微软雅黑;font-size: 16px;font-weight: bold;padding-bottom:20px;">自助服务系统日志</div>
<div id="queryContent" style="width: auto;height: auto; padding-bottom: 20px;">
<form id="serachForm" action="${ctx}/admin/getXtrzList.do">
	<input id = "page" name="page"  type="hidden" value="1"/>
	<input id = "rows" name="rows"  type="hidden" value="10"/>
<table style="width: 80%;height: auto;" border="0">
	<tr>
		<td width="60px" align="right">操作类型:&nbsp;</td>
		<td><select class="easyui-combobox" name="czlx" id="czlx" style="width:130px;">
			<option value="">---请选择---</option>
			<option value="登陆">用户登陆</option>
			<option value="进入服务项目">进入服务项目</option>
			<option value="完成服务项目">完成服务项目</option>
			<option value="退出">用户退出</option>
		</select></td>
		<td width="60px" align="right">操作人:&nbsp;</td><td><input id="czr" name="czr"  type="text" value="" class="easyui-validatebox textbox"/></td>
		<td width="60px" align="right">IP地址:&nbsp;</td><td><input id="ip" name="ip" type="text" value="" class="easyui-validatebox textbox"/></td>
	</tr>
	<tr>
		<td align="right">开始时间:&nbsp;</td><td><input id="startDate" name="startDate" class="easyui-datebox" /></td>
		<td align="right">结束时间:&nbsp;</td><td><input id="endDate" name="endDate" class="easyui-datebox"/></td>
		<td></td>
		<td style="padding-left: 20px;" align="left"><a href="javascript:void(0)" class="easyui-linkbutton" onclick="searchXtrz()" style="width: 80px;">查询</a></td>
	</tr>
</table>
</form>
</div>
<div style="padding-left: 200px">
<table id="dg" title=""
	sortName="czlx" sortOrder="asc"
	data-options="
            rownumbers:true,
            singleSelect:true,
            pagination:true,
            method:'post',
            fitColumns:'true',
            loadMsg: '正在加载数据...',
            url:'${ctx}/admin/getXtrzList.do'
            ">
	<thead>
		<tr>
			<th data-options="field:'czlx',width:100" align="center">操作类型</th>
			<th data-options="field:'czms',width:420,align:'left'" align="center">操作描述</th>
			<th data-options="field:'userid',width:120" align="center">操作人</th>
			<th data-options="field:'ip',width:100,align:'center'" align="center">IP地址</th>
			<th data-options="field:'cssj',width:150,align:'left'" align="center">操作时间</th>
			<th data-options="field:'cz',width:60,align:'center'" align="center">查看</th>
		</tr>
	</thead>
</table>
    </div>
    <div id="xtrz-window" title="系统操作日志" style="width:500px;height:450px;">  
    <div style="padding:20px 20px 40px 80px;">  
        <form method="post" id="">  
            <table>  
                <tr>  
                    <td>操作人：</td>  
                    <td><input id="userid" name="userid" type="text" class="easyui-validatebox textbox" style="width:220px" disabled="disabled" ></td>  
                </tr>  
                <tr>  
                    <td>操作类型：</td>  
                    <td><input id="czlx2" name="czlx2" type="text" class="easyui-validatebox textbox" style="width:220px" disabled="disabled" ></td>  
                </tr>  
                
                <tr>
                	<td>操作时间</td>
                	<td><input id="czsj" name="czsj" type="text" class="easyui-validatebox textbox" style="width:220px" disabled="disabled" ></td>
                </tr>
                <tr>
                	<td>设备编号</td>
                	<td><input id="sbbh" name="sbbh" type="text" class="easyui-validatebox textbox" style="width:220px" disabled="disabled" ></td>
                </tr>
                <tr>
                	<td>IP地址</td>
                	<td><input id="ip2" name="ip2" type="text" class="easyui-validatebox textbox" style="width:220px" disabled="disabled" ></td>
                </tr>
                <tr>  
                    <td valign="top">操作描述：</td>  
                    <td><textarea id="czms2" name="czms2" rows="" cols="" class="easyui-validatebox textbox" style="width:220px;height: 150px;" disabled="disabled"></textarea></td>  
                </tr>  
            </table>  
        </form>  
    </div>  
    <div style="text-align:center;padding:5px;">  
        <a href="javascript:void(0)" onclick="closeXtrz()" class="easyui-linkbutton"  icon="icon-cancel">关闭</a>  
    </div>  
</div> 
</div>
</body>
