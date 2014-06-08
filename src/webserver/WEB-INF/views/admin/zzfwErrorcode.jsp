<%@page language="java" pageEncoding="UTF-8"%>
<%
	StringBuffer ctx = new StringBuffer(request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ request.getContextPath());
%>
<script type="text/javascript">
	var cwzdWin = $('#cwzd-window').window({  
    	    closed:false 
		}); 
	var cwzdForm = cwzdWin.find('form');
	
	function newCwzd() {
   		$('#cwzd-window').window({  
    	    closed:false 
		}); 
   		$('#cwzdForm').form('clear');
   		cwzdForm.url = '<%=ctx%>/admin/saveCwzd.do';
   		
	}
	  
	function editUser() {  
	    var row = $('#errordg').datagrid('getSelected'); 
	    if (row){  
	        $('#cwzd-window').window({  
    	    	closed:false 
			});
	        $("#errorcode").val(row.errorcode);
	        $("#errorcode").focus();
	        $("#errormessage").val(row.errormessage);
	        cwzdForm.url = '<%=ctx%>/admin/updateCwzd.do?id='+row.errorcode; 
	    } else {  
	         var message = "<div class='message' >请先选中记录行!</div>";
			 cwxbox.box.show(message,2);   
	    }  
	} 
	
	function saveCwzd() {
		var errorcode = $('#errorcode').val();
		if(errorcode==''){
			$.messager.alert('提示','<font color="red" >错误码为必填项！</font>','warn');
			return ;
		}
		var options = {
			type: "post",
			url: cwzdForm.url,
			dataType: "json",
			success: function(data){
	            if (data.success){  
	                $('#errordg').datagrid('reload');  
	                $('#cwzd-window').window({  
		    	    	closed:true 
					});   
	                var message = "<div class='message' >设置错误字典成功!</div>";
					cwxbox.box.show(message,2); 
	            } else {  
	                $.messager.alert('错误',data.msg,'error');  
	            } 
			}
		}
	
		$('#cwzdForm').ajaxSubmit(options); 	
	}
	  
	function closeCwzd(){  
	    $('#cwzd-window').window('close');  
	}
	function confirmDelErr(){
	 	var row = $('#errordg').datagrid('getSelected');
		$.ajax({
			type:'post',
			url:'<%=ctx%>/admin/deleteCwzd.do?errorcode='+row.errorcode,
			dataType:'text',
			success:function(msg){
				$('#errordg').datagrid('loadData', { total: 0, rows: [] }); 
				setTimeout("$('#errordg').datagrid('reload')",500);
			 	var message = "<div class='message' >删除记录成功!</div>";
				cwxbox.box.show(message,2);
				}
			});
		 $('#errdlg').dialog('close');
	}
	
	function cancelDelErr(){
		$('#errdlg').dialog('close');
	}
	
	function deleteRow(){  
	   var row = $('#errordg').datagrid('getSelected');
	    if(row){
	    	//弹出确认删除的对话框
	     $('#errdlg').dialog('open');
	     $('#errmessager').html("您确认删除 <font color='red'>"+row.errorcode+"</font> 吗?");
	    }else {  
	         var message = "<div class='message' >请先选中记录行!</div>";
			 cwxbox.box.show(message,2); 
	    }  
	}
</script>
<table id="errordg" class="easyui-datagrid" title="错误字典设置" style="width:auto;height:auto"
			data-options="
				iconCls: 'icon-edit',
				singleSelect: true,
				toolbar: '#error_tb',
				url: '',
				method: 'post'
			">
		<thead>
			<tr>
           		<th data-options="field:'errorcode',width:220" align="center">错误码</th>
				<th data-options="field:'errormessage',width:580" align="center">错误描述</th>
				<th data-options="field:'cz',width:120" align="center">操作</th>
			</tr>
		</thead>
	</table>
	<div id="error_tb" style="height:auto">
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="newCwzd()">新增</a>

	</div>
	<div id="cwzd-window" title="错误字典" style="width:500px;height:350px;">  
    <div style="padding:20px 20px 40px 80px;">  
        <form method="post" id="cwzdForm">  
            <table>  
                <tr>  
                    <td align="right">错误码<font color="red">(*)</font>：</td>  
                    <td><input id="errorcode" name="errorcode" type="text" class="easyui-validatebox textbox" style="width:220px" data-options="required:true"></input></td>  
                </tr>  
                <tr>  
                    <td align="right" valign="top">错误描述：</td>  
                    <td><textarea id="errormessage" name="errormessage" rows="" cols="" class="easyui-validatebox textbox" style="width:220px;height: 150px;"></textarea></td>  
                </tr>  
            </table>  
        </form>  
    </div>  
    <div style="text-align:center;padding:5px;">  
        <a href="javascript:void(0)" onclick="saveCwzd()"  class="easyui-linkbutton" icon="icon-save">保存</a>  
        <a href="javascript:void(0)" onclick="closeCwzd()"  class="easyui-linkbutton" icon="icon-cancel">取消</a>  
    </div>  
</div> 
<div align="center" id="errdlg" class="easyui-dialog" title="是否删除"
	data-options="iconCls:'icon-save'"
	style="width:400px;height:180px;padding:20px 20px 40px 20px;">
	<div align="center" id="errmessager"
	style="font-size: 14px;font-weight: bold;height: 40px;"></div>
	<div align="center">
		<a href="javascript:void(0)" onclick="confirmDelErr()" class="easyui-linkbutton" icon="icon-edit">确认</a>&nbsp;&nbsp;
		<a href="javascript:void(0)" onclick="cancelDelErr()"  class="easyui-linkbutton"  icon="icon-cancel">取消</a>
	</div>
</div>
	
 	