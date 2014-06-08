<%@page language="java" pageEncoding="UTF-8"%>
<%
			StringBuffer ctx = new StringBuffer(request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath());
%>
<script type="text/javascript">
	
	var xtcsWin = $('#xtcs-window').window({  
    	    closed:true 
	}); 
	
	var xtcsForm = xtcsWin.find('form');
	
	function newXtcs(){
   		$('#xtcs-window').window({ 
   			closed:false  
		});   
   		$('#xtcsForm').form('clear');
   		xtcsForm.url = '<%=ctx%>/admin/saveXtcs.do';
	}  
	
	function editXtcs(){  
	    var row = $('#systemdg').datagrid('getSelected'); 
	    if (row){  
	       $('#xtcs-window').window({ 
   				closed:false  
			});  
	        $("#csbs").val(row.csbs);
	        $("#csz").val(row.csz);
	        $("#csz").focus();
	        $("#csbs").focus();
    	    $("#cssm").val(row.cssm); 
	        xtcsForm.url = '<%=ctx%>/admin/updateXtcs.do?id='+row.csbs;  
	    } else {  
	         var message = "<div class='message' >请先选中记录行!</div>";
			 cwxbox.box.show(message,2);   
	    }  
	}  
	
	function saveXtcs(){
	
		var csbs = $('#csbs').val();
		if(csbs==''){
			$.messager.alert('提示','<font color="red" >参数标识为必填项！</font>','warn');
			return ;
		}
		var csz = $('#csz').val();
		if(csz==''){
			$.messager.alert('提示','<font color="red" >参数值为必填项！</font>','warn');
			return ;
		}	
		 var options = {
				type: "post",
				url: xtcsForm.url,
				dataType: "json",
				success: function(data){
		            if (data.success){  
		                $('#systemdg').datagrid('reload');  
		                $('#xtcs-window').window({  
			    	    	closed:true 
						});   
		                 var message = "<div class='message' >设置系统参数成功!</div>";
						 cwxbox.box.show(message,2); 
		            } else {  
		                $.messager.alert('错误',data.msg,'error');  
		            } 
				}
		}
		$('#xtcsForm').ajaxSubmit(options); 	
	}
	  
	function closeXtcs(){ 
	   $('#xtcs-window').window({ 
   			closed:true  
		});    
	}
	
	function confirmDel(){
	 var row = $('#systemdg').datagrid('getSelected');

	 $('#dlg').dialog('close');
		$.ajax({
			type:'post',
			url:'<%=ctx%>/admin/deleteXtcs.do?csbs='+row.csbs,
			dataType:'text',
			success:function(data){
				setTimeout("$('#systemdg').datagrid('reload')",500);
				var message = "<div class='message' >删除记录成功!</div>";
				cwxbox.box.show(message,2);
			},
			error:function(){
			 alert();
			}
		})
	}
	
	function cancelDel(){
		$('#dlg').dialog('close');
	}
	
	function deleteXtcs(){  
	    var row = $('#systemdg').datagrid('getSelected');
	    if(row){
	    	//弹出确认删除的对话框
	     	$('#dlg').dialog('open');
	     	$('#sysmessager').html("您确认删除 <font color='red'>"+row.csbs+"</font> 吗?");
	    }else {  
	        var message = "<div class='message' >请先选中记录行!</div>";
			cwxbox.box.show(message,2);   
	    }  
	}
	
</script>
<table id="systemdg" class="easyui-datagrid" title="系统参数设置"
	style="width:auto;height:auto"
	data-options="
				iconCls: 'icon-edit',
				singleSelect: true,
				toolbar: '#system_tb'
			">
	<thead>
		<tr>
			<th field="csbs" width="100" align="center">参数标识</th>
			<th field="csz" width="250" align="center">参数值</th>
			<th data-options="field:'cssm',width:500" align="center">参数说明</th>
			<th data-options="field:'cz',width:120" align="center">操作</th>
		</tr>
	</thead>
</table>

<div id="system_tb" style="height:auto"><a
	href="javascript:void(0)" class="easyui-linkbutton"
	data-options="iconCls:'icon-add',plain:true" onclick="newXtcs()">新增</a>
</div>
<div id="xtcs-window" title="系统参数" style="width:500px;height:350px;">
<div style="padding:20px 20px 40px 80px;">
<form method="post" id="xtcsForm">
<table>
	<tr>
		<td align="right">参数标识<font color="red">(*)</font>：</td>
		<td><input id="csbs" name="csbs" type="text"
			class="easyui-validatebox textbox" style="width:220px"
			data-options="required:true"></input></td>
	</tr>
	<tr>
		<td align="right">参数值<font color="red">(*)</font>：</td>
		<td><input id="csz" name="csz" type="text"
			class="easyui-validatebox textbox" style="width:220px"
			data-options="required:true"></input></td>
	</tr>
	<tr>
		<td align="right" valign="top">参数说明：</td>
		<td><textarea id="cssm" name="cssm" rows="" cols=""
			class="easyui-validatebox textbox" style="width:220px;height: 150px;"></textarea></td>
	</tr>
</table>
</form>
</div>
<div style="text-align:center;padding:5px;">
	<a href="javascript:void(0)" onclick="saveXtcs()" class="easyui-linkbutton" icon="icon-save">保存</a>
	<a href="javascript:void(0)" onclick="closeXtcs()" class="easyui-linkbutton" icon="icon-cancel">取消</a>
</div>
</div>

<div align="center" id="dlg" class="easyui-dialog" title="是否删除"
	data-options="iconCls:'icon-save'"
	style="width:400px;height:180px;padding:20px 20px 40px 20px;">

	<div align="center" id="sysmessager"
	style="font-size: 14px;font-weight: bold;height: 40px;"></div>

	<div align="center"><a href="javascript:void(0)" onclick="confirmDel()" id="save" class="easyui-linkbutton"
	icon="icon-save">确认</a>&nbsp;&nbsp;<a href="javascript:void(0)"
	onclick="cancelDel()" id="cancel" class="easyui-linkbutton" icon="icon-cancel">取消</a>
	</div>

</div>
