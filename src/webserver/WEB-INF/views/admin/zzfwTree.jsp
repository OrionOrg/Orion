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
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/css/showdialog.css" />
<script type="text/javascript"
	src="${ctx}/resources/js/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${ctx}/resources/js/zzfw/common.showdialog.js"></script>
<script>  
//实例化树菜单
$(document).ready(function(){
	$('#systemdg').datagrid({
		url:'${ctx}/admin/getXtcsList.do'
	});
	$('#errordg').datagrid({
		url:'${ctx}/admin/getCwzdList.do'
	});
	$('#dlg').dialog('close');
	$('#errdlg').dialog('close');
	$('#xmdlg').dialog('close');
	$('#treedlg').dialog('close');
	$('#treedlg2').dialog('close');
	var fwdlWin = $('#fwdl-window').window({  
	    	closed:true  
		});
	var sfbzWin = $('#sfbz-window').window({  
	    	closed:true  
		});	
	var ugroupWin = $('#ugroup-window').window({  
			closed:true  
		});
	var fwxmWin = $('#fwxm-window').window({ 
			closed:true  
		});
	var ffwxmWin = $('#ffwxm-window').window({  
	    	closed:true 
		});
	var cwzdWin = $('#cwzd-window').window({  
	    	closed:true 
		});
	var xtcsWin = $('#xtcs-window').window({  
    	    closed:true 
	});
	
	 $("#tt").tree({
     	onClick : function (node){
     		var id = "";
     		if(node.attributes.fwxmid){
     			id = node.attributes.fwxmid;
     		}
    		$('#dg').datagrid({
    			title:node.text,  
    			iconCls:'icon-edit',
	    	url:'${ctx}/admin/getFwxmList.do?id='+id,
	    	onLoadSuccess:function(data){
	    		$('#dgPanel').css('display','block');
	    	},
			onLoadError:function(){
				$('#dgPanel').css('display','none');
			}
	    });
			$('#fwxmmc').val(node.text);
		    $('#fwxmmc').focus();
		    $('#clcx').val(node.attributes.clcx);
		    $('#tblj').val(node.attributes.icon);
		    $('#bz').val(node.attributes.bz);
		    $('#wid').val(node.attributes.wid);
		    $('#fwxmid').val(node.attributes.fwxmid);
			$('#fwlb').val(node.attributes.fwlb);
			$('#ffpx').numberbox('setValue',node.attributes.px);
			$("#ffqy").combobox('select',node.attributes.sfqy);
     	}
  	}); 
	
});

   	function reloadTree(){
	 $('#tt').tree({
          url: '${ctx}/admin/getTreeList.do?id=0',
          method:'post',
          lines:true,
   		  onContextMenu: function(e,node){
              e.preventDefault();
              $(this).tree('select',node.target);
              $('#mm').menu('show',{
                  left: e.pageX,
                  top: e.pageY
              });
          }
      }); 
	} 

</script>

<script>
    function submitForm(){
	  	var clcx = $('#clcx').val();
	  	if(clcx!=null && clcx!=""){
	  		var str = clcx.substr(0, 5); // 获取子字符串。
	   	var str2= clcx.substr(0,10);
	   	if(str!="java:" && str2!="procedure:"){
	   		$.messager.alert('提示','<font color="red">处理程序字段需要符合输入格式！</font>','warn');
	   		return;
	   	}
	  	}
	   $('#ff').form('submit', {  
	       url:$('#ff').url,
	       method:'post',  
	       success:function(data){ 
	      	   setTimeout("reloadTree()",500);
	          var message = "<div class='message' >设置服务项目信息成功!<br/><br/></div>";
		   cwxbox.box.show(message,2);
	       }  
	    });  
     }
     
     function clearForm(){
         $('#ff').form('clear');
     }
</script>
</head>
<body>
<script type="text/javascript">

	function submitXmForm(){
    	var clcx = $('#xmclcx').val();
    	if(clcx!=null && clcx!=""){
    		var str = clcx.substr(0, 5); // 获取子字符串。
	    	var str2= clcx.substr(0,10);
	    	if(str!="java:" && str2!="procedure:"){
	    		$.messager.alert('提示','<font color="red">处理程序字段需要符合输入格式！</font>','warn');
	    		return;
	    	}
    	}
    	//提交form表单
           $('#xm').form('submit', {  
        url:$('#xm').url,
        method:'post',  
        success:function(data){ 
       		 //刷新树形结构
            setTimeout("reloadTree()",500);
            $('#ffwxm-window').window({  
		    	closed:true  
			});
			//操作成功提示信息
            var message = "<div class='message' >设置服务项目信息成功!<br/><br/></div>";
			cwxbox.box.show(message,2);
  			setTimeout("$('#dg').datagrid('reload')",500);
        	}  
    	});  
       }
       
       function clearXmForm(){
           $('#xm').form('clear');
       }
        /**
         *获得32位GUID
         */
		function newGuid() {
        	var guid = "";
        	for (var i = 1; i <= 28; i++) {
            var n = Math.floor(Math.random() * 16.0).toString(16);
            	guid += n;
            	if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
               	 guid += "-";
       		 }
        	return guid;
    	}
    	/**
         *树形结构新增节点
         */
        function treeappend(){
            var t = $('#tt');
            var node = t.tree('getSelected');
            var guid =newGuid();
           	 $('#ffwxm-window').window({  
			    	closed:false 
				});
			$('#xm').form('clear');
			var attributes = node.attributes;
			if(attributes){
				var ffwxmid = node.attributes.ffwxmid;
				if(ffwxmid!=""){
					$('#fxmid').val(node.attributes.fwxmid);
				}else{
					var message = "<div class='message' >警告!<br/><br/></div>";
					cwxbox.box.show(message,2);
					return;
				}
			}
			$('#xmwid').val(guid);
			$('#xmid').val(guid);
			$("#qy").combobox('select',"1");
			$('#ffwlb').val(node.attributes.fwlb);
        }
        
        function confirmTreeDel2(){
        	$('#treedlg2').dialog('close');
        	$('#treedlg').dialog('open');
			var node = $('#tt').tree('getSelected');
			$('#messager3').html("您将删除 <font color='red'>"+node.text+"</font> 以及下面的子项目。您确认继续删除吗?");
        }
        
        function confirmTreeDel(){
			$('#treedlg').dialog('close');
			var node = $('#tt').tree('getSelected');
			var fwxmid = "";
			var attributes = node.attributes;
			if(attributes){
				fwxmid = node.attributes.fwxmid;
				var ffwxmid = node.attributes.ffwxmid;
				$.ajax({
					type:'post',
					url:'${ctx}/admin/deleteFwxm.do?id='+fwxmid,
					dataType:'text',
					success:function(data){
						//$('#tt').tree('reload');
						var message = "<div class='message' >删除记录成功!<br/><br/></div>";
						cwxbox.box.show(message,2);
					},
					error:function(){
					 alert();
					}
				});
			}
			$('#tt').tree('remove', node.target);
		}
		
		function cancelTreeDel(){
			$('#treedlg').dialog('close');
		}
		
		function cancelTreeDel2(){
			$('#treedlg2').dialog('close');
		}
        /**
         *删除树结构
         */   
        function treeremoveit(){
            var node = $('#tt').tree('getSelected');
            var fwxmid = "";
			var attributes = node.attributes;
			if(attributes){
				fwxmid = node.attributes.fwxmid;
				var ffwxmid = node.attributes.ffwxmid;
				
				//查找是否有子服务项目
				var flag = true;
				$.ajax({
					async:false,
					type:'post',
					url:'${ctx}/admin/getFwxmById.do?id='+fwxmid,
					dataType:'json',
					success:function(data){
						//$('#tt').tree('reload');
						if(data.success){
							$('#treedlg2').dialog('close');
        					$('#treedlg').dialog('open');
							var node = $('#tt').tree('getSelected');
							$('#messager3').html("您将删除 <font color='red'>"+node.text+"</font> 。您确认继续删除吗?");
						}else{
							$('#treedlg2').dialog('open');
							$('#messager4').html("该服务项目下存在子项目，删除该项目将一并删除其下的子项目。您确认本次操作吗？");
							flag = false;
						}
					},
					error:function(){
					}
				});
			}
        }
        
        function addNode(){
        	var node = $('#tt').tree('getSelected');
        	if(node){
        		treeappend();
        	}else{
        		var message = "<div class='message' >请先选中节点!<br/><br/></div>";
				cwxbox.box.show(message,2);
        	}
        }
        
        function deleteNode(){
        	var node = $('#tt').tree('getSelected');
        	if(node){
        		treeremoveit();
        	}else{
        		var message = "<div class='message' >请先选中节点!<br/><br/></div>";
				cwxbox.box.show(message,2);
        	}
        }
        
        function addTopNode(){
        	$('#fwdl').form('clear');
        	var guid =newGuid();
        	$('#fwdlid').val(guid);
        	$('#fwdlwid').val(guid);
        	$('#fwdl-window').window({  
		    	closed:false 
			});
        }
</script>
<script type="text/javascript">
/**
 *编辑服务项目
 */  
function editFwxm(){  
    var row = $('#dg').datagrid('getSelected'); 
    if (row){  
       $('#fwxm-window').window({ 
  			closed:false  
		});  
        $("#fwxmmc1").val(row.fwxmmc);
        //$("#csbs").attr('disabled','true');
        $("#clcx1").val(row.clcx);
        $("#clcx1").focus();
        $("#fwxmmc1").focus();
   	    $("#tblj1").val(row.icon);
   	    $("#bz1").val(row.bz);
   	    $("#fwxmid1").val(row.fwxmid);
   	    $("#ffwxmid1").val(row.ffwxmid);
   	    $("#wid1").val(row.wid);
   	    $("#qy2").combobox('select',row.sfqy);
   	    $('#px2').numberbox('setValue',row.px);
   	    $('#fwlb').val(row.fwlb);
        $('#fwxmForm').url = '${ctx}/admin/saveFwxm.do?id='+row.wid;  
    } else {  
        $.messager.show({  
            title:'警告',   
            msg:'请先选择记录行。'  
        });  
    }  
}
  
function submitFwxmForm(){
   	
   	var clcx = $('#clcx1').val();
   	if(clcx!=null && clcx!=""){
   		var str = clcx.substr(0, 5); // 获取子字符串。
    	var str2= clcx.substr(0,10);
    	if(str!="java:"&& str2!="procedure:"){
    		$.messager.alert('提示','<font color="red">处理程序字段需要符合输入格式！</font>','warn');
    		return;
    	}
   	}
  		$('#fwxmForm').form('submit', {  
        url:$('#fwxmForm').url,
        method:'post',  
        success:function(data){ 
        	$('#fwxm-window').window({ 
	   			closed:true  
			}); 
			setTimeout("$('#dg').datagrid('reload')",500);
	        var message = "<div class='message' >设置服务项目信息成功!<br/><br/></div>";
			cwxbox.box.show(message,2);
        }  
    });  
} 
 
function clearFwxmForm(){ 
   $('#fwxm-window').window({ 
  			closed:true  
	});    
}
/**
 *显示并编辑收费标准
 */
function showSfbz(){
	$('#sfbzForm').form('clear');
	//根据wid获取收费标准信息并展示
	var row = $('#dg').datagrid('getSelected'); 
    if (row){  
		$('#fwmc').html(row.fwxmmc+"收费标准");
		$('#fwxmid2').val(row.fwxmid); 
	} 
	
	$.ajax({
		  async:false,   
          type:"post",   
          url:"${ctx}/admin/getSfbz.do",   
          dataType:"json",   
          data:"id="+row.fwxmid,   
          success:function(msg){
          	if(msg.wid){
	            $("#price").val(msg.price);
	            $("#dcdysx").val(msg.dcdysx);
	            $("#mfdyfs").val(msg.mfdyfs); 
	            $("#fyclcx").val(msg.clcx);
	            if(msg.report){
	            	$("#report").val(msg.report);
	            }else{
	            	 $("#report").val('http://'); 
	            }
	            $("#dyjmc").val(msg.dyjmc); 
	            $("#sfbzsm").val(msg.sfbzsm);
	            $("#zysx").val(msg.zysx); 
	            $("#sfbzwid").val(msg.wid);
	            $('#fwxmid2').val(msg.fwxmid);
            }
          }   
       });   
	$('#sfbz-window').window({ 
 			closed:false  
	});
	$('#sfbzForm').url = '${ctx}/admin/saveSfbz.do';
}
  
function submitSfbzForm(){
	var price = $("#price").val();
	var dcdysx = $("#dcdysx").val();
	var mfdyfs = $("#mfdyfs").val();
	var reg = new RegExp("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
	if(!reg.test(price)){
        $.messager.alert('提示','<font color="red" >单价字段输入格式不正确，请输入非负整数或小数！</font>','warn');
        return;
    };
	var reg2 = new RegExp("^[0-9]*$");
	if(!reg2.test(dcdysx)){
        $.messager.alert('提示','<font color="red" >单次打印上限字段输入格式不正确，请输入正整数！</font>','warn');
        return;
    };
	if(!reg2.test(mfdyfs)){
        $.messager.alert('提示','<font color="red" >免费打印次数字段输入格式不正确，请输入正整数！</font>','warn');
        return;
    };
	 $('#sfbzForm').form('submit', {  
	        url:sfbzForm.url,
	        method:'post',  
	        success:function(data){ 
	       
	        	$('#sfbz-window').window({ 
	   			closed:true  
			}); 
           var message = "<div class='message' >设置收费标准信息成功!<br/><br/></div>";
			cwxbox.box.show(message,2);
	        }  
	    });  
} 
 
function clearSfbzForm(){ 
   $('#sfbz-window').window({ 
  			closed:true  
	});    
}
 
 /**
  *展示并编辑用户组
  */        
function showUGroup(){
	$('#ugroup-window').window({ 
 			closed:false  
	});
	var row = $('#dg').datagrid('getSelected'); 
	$('#fwxmid_ug').val(row.fwxmid);
    $.ajax({
		  async:false,   
          type:"post",   
          url:"${ctx}/admin/getUgroupList.do",   
          dataType:"json",   
          data:"id="+row.fwxmid,   
          success:function(msg){
          		//构建html
          		var obj = eval(msg);
          		var html = "<tr>"
          		for(var i=1 ;i<obj.length+1;i++){
          			html=html+"<td width='180px'><input type='checkbox' value='"+obj[i-1].wid
          				+"' id='"+obj[i-1].roleName+"'></input>"+obj[i-1].roleName
          				+"</td>";
          			if(i%2==0){
          				html=html+"</tr><tr>";
          			}
          		}
          		$('#yhz').html(html);
          }   
       });
       
       $.ajax({
		  async:false,   
          type:"post",   
          url:"${ctx}/admin/getFwxmGroupList.do",   
          dataType:"json",   
          data:"fwxmid="+row.fwxmid,   
          success:function(msg){
          		//构建html
          		var obj = eval(msg);
          		for(var i=0 ;i<obj.length;i++){
          			document.getElementById(obj[i].groupId).checked = true;
          		}
          }   
       }); 
}

function submitUgroupForm(){
	var data = new Array();
	var j = 0;
       $("input[type='checkbox']").each(function(i) {
           if ($(this).attr("checked")) {
               data[j] = $(this).attr('id');
               j++;
           }
       })
       var fwxmid = $('#fwxmid_ug').val();
      $.ajax({
	  	 async:false,   
         type:"post",   
         url:'${ctx}/admin/saveUgroupList.do?groupList='+data+'&fwxmid='+fwxmid,   
         dataType:"json",   
         success:function(msg){
         		if(msg.success){
         			$('#ugroup-window').window({ 
	   				closed:true  
				}); 
	         	var message = "<div class='message' >设置用户组信息成功!<br/><br/></div>";
				cwxbox.box.show(message,2); 
         		}else{
         			$.messager.alert('错误','<font color="red">设置用户组信息失败！</font>','error');
         		}
         }   
      }); 
}
  
function clearUgroupForm(){ 
   $('#ugroup-window').window({ 
  			closed:true  
	});    
}

/**
  *删除确认
  */
function confirmXmDel(){
	var row = $('#dg').datagrid('getSelected');
	$('#xmdlg').dialog('close');
	$.ajax({
		type:'post',
		url:'${ctx}/admin/deleteFwxm.do?id='+row.fwxmid,
		dataType:'text',
		success:function(data){
			$('#dg').datagrid('loadData', { total: 0, rows: [] }); 
			$('#dg').datagrid('reload');
			var message = "<div class='message' >删除记录成功!<br/><br/></div>";
			cwxbox.box.show(message,2);
		},
		error:function(){
			var error = "<div class='message' >操作失败!<br/><br/></div>";
			cwxbox.box.show(error,2);
		}
	});
}
	
function cancelXmDel(){
	$('#xmdlg').dialog('close');
}

function deleteRecord(){
	var row = $('#dg').datagrid('getSelected');
	if(row){
	    //弹出确认删除的对话框
	    $('#xmdlg').dialog('open');
	    $('#messager').html("您确认删除 <font color='red'>"+row.fwxmmc+"</font> 吗?");
	 }else {  
	    var message = "<div class='message' >请先选中记录行!</div>";
		cwxbox.box.show(message,2);   
	 }  
}
</script>
<script type="text/javascript">
		
	function submitFwdlForm(){
	    var fwxmmc = $('#fwdlmc').val();
	   	if(fwxmmc==""){
	   		$.messager.alert('提示','<font color="red">项目名称,该字段为必填项！</font>','warn');
	   		return;
	   	}
	   	//提交form表单
        $('#fwdl').form('submit');
        //刷新树形结构
        setTimeout("reloadTree()",500);
        $('#fwdl-window').window({  
	    	closed:true  
		});
		//操作成功提示信息
        var message = "<div class='message' >设置服务项目信息成功!<br/><br/></div>";
		cwxbox.box.show(message,2);
		setTimeout("$('#dg').datagrid('reload')",500);
	}
	
    function clearFwdlForm(){
        $('#fwdl').form('clear');
    }

</script>

<div class="easyui-tabs" style="width:auto;height:auto">
<div title="自助服务项目设置">
<div style="float:left;positon:relative;">
<div class="easyui-panel" style="width:220px;height:509px;padding:10px;">
<div onclick="addTopNode()" class="easyui-linkbutton">新增顶层</div>
<div onclick="addNode()" class="easyui-linkbutton">新增下级</div>
<div onclick="deleteNode()" class="easyui-linkbutton">删除</div>
<ul id="tt" class="easyui-tree"
	data-options="
                url: '${ctx}/admin/getTreeList.do?id=0',
                method: 'post',
                animate: true,
                lines: true,
         		onContextMenu: function(e,node){
                    e.preventDefault();
                    $(this).tree('select',node.target);
                    $('#mm').menu('show',{
                        left: e.pageX,
                        top: e.pageY
                    });
                }
     	"></ul>
</div>
<div id="mm" class="easyui-menu" style="width:120px;">
	<div onclick="treeappend()" data-options="iconCls:'icon-add'">增加</div>
	<div onclick="treeremoveit()" data-options="iconCls:'icon-remove'">删除</div>
</div>
<div id="ffwxm-window"  title="服务项目信息维护" style="width:580px;">
<div style="padding:10px 60px 20px 60px">
<form id="xm" method="post" action="${ctx}/admin/saveFwxm.do">
	<input type="hidden" name="bizobjs" value="fwxm:t_zzfw_fwxm" /> 
	<input type="hidden" name="fwxm.fwxmid" id="xmid" /> 
	<input type="hidden" name="fwxm.ffwxmid" id="fxmid" /> 
	<input id="xmwid" name="fwxm.wid" type="hidden" value="" /> 
	 <input name="fwxm.fwlb" type="hidden" id="ffwlb" value="" />
<table cellpadding="5">
	<tr>
		<td>项目名称:</td>
		<td colspan="3"><input id="xmmc" name="fwxm.fwxmmc"
			class="easyui-validatebox textbox" style="width:320px" type="text"
			data-options="required:true"></input><font color="red">(必填)</font></td>
	</tr>
	<tr>
		<td>处理程序:</td>
		<td colspan="3"><input id="xmclcx" name="fwxm.clcx"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="#333333">(可选)</font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="3"><font color="#333333">如果为Java程序处理，则输入格式为
		java:XXX.XXX.处理类名称。<br />
		如果为存储过程处理，则输入格式为 procedure:存储过程名称。</font></td>
	</tr>
	<tr>
		<td>图标路径:</td>
		<td colspan="3"><input id="xmtblj" name="fwxm.icon"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="#333333">(可选)</font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="3"><font color="#333333">提示：图标文件放在/resources/image/zzfw/路径下。</font></td>
	</tr>
	<tr>
		<td>是否启用:</td>
		<td><select id="qy" size="2" name="fwxm.sfqy"
			class="easyui-combobox" style="width: 60px;height: auto;">
			<option value="1">是</option>
			<option value="2">否</option>
		</select></td>
		<td align="right">排序:</td>
		<td><input name="fwxm.px" class="easyui-numberbox"
			style="width: 40px"></input></td>
	</tr>
	<tr>
		<td valign="top">备注说明:</td>
		<td colspan="3"><textarea id="xmbz" name="fwxm.bz"
			style="width:320px;height:60px;">
                        	
                        </textarea></td>
	</tr>
	<tr>
		<td colspan="4" align="center">
		<div style="text-align:center;padding:5px"><a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="submitXmForm()">保存</a> &nbsp;&nbsp;&nbsp; <a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="clearXmForm()">清空</a></div>
		</td>
	</tr>
</table>
</form>
</div>
</div>
</div>
<div style="float:left;position:relative;">
<div class="easyui-panel" title="服务项目信息维护" style="width:750px;">
<div style="padding:10px 60px 20px 60px">
<form id="ff" method="post" action="${ctx}/admin/saveFwxm.do">
	<input type="hidden" name="bizobjs" value="fwxm:t_zzfw_fwxm" /> 
	<input type="hidden" name="fwxm.fwxmid" id="fwxmid" /> 
	<input id="wid" name="fwxm.wid" type="hidden" value="" /> 
<table cellpadding="5">
	<tr>
		<td>项目名称:</td>
		<td colspan="3"><input id="fwxmmc" name="fwxm.fwxmmc"
			data-options="required:true"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="red">(必填)</font></td>
	</tr>
	<tr>
		<td>处理程序:</td>
		<td colspan="3"><input id="clcx" name="fwxm.clcx"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="#333333">(可选)</font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="3"><font color="#333333">如果为Java程序处理，则输入格式为
		java:XXX.XXX.处理类名称。<br />
		如果为存储过程处理，则输入格式为 procedure:存储过程名称。</font></td>
	</tr>
	<tr>
		<td>图标路径:</td>
		<td colspan="3"><input id="tblj" name="fwxm.icon"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="#333333">(可选)</font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="3"><font color="#333333">提示：图标文件放在/resources/image/zzfw/路径下。</font></td>
	</tr>
	<tr>
		<td>是否启用:</td>
		<td><select id="ffqy" size="2" name="fwxm.sfqy"
			class="easyui-combobox" style="width: 60px;height: auto;">
			<option value="1">是</option>
			<option value="2">否</option>
		</select></td>
		<td align="right">排序:</td>
		<td><input name="fwxm.px" class="easyui-numberbox" id="ffpx"
			style="width: 40px"></input></td>
	</tr>
	<tr>
		<td valign="top">备注说明:</td>
		<td colspan="3"><textarea id="bz" name="fwxm.bz"
			style="width:320px;height:60px;">
		 </textarea></td>
	</tr>
	<tr>

		<td colspan="4" align="center">
		<div style="text-align:center;padding:5px"><a
			href="javascript:void(0)" id="saveBtn" class="easyui-linkbutton"
			onclick="submitForm()">保存</a> <a href="javascript:void(0)"
			id="cancelBtn" class="easyui-linkbutton" onclick="clearForm()">清空</a>
		</div>
		</td>
	</tr>
</table>
</form>

</div>

</div>
</div>

<div style="float:left;position:relative;" id="dgPanel">
<div style="margin:20px 0;"><!--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="newFwxm()">新增</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="save()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancel()">取消</a>
        --></div>
<table id="dg" class="easyui-datagrid" style="width:750px;height:auto;"
	data-options="singleSelect:true">
	<thead>
		<tr>
			<th data-options="field:'fwxmmc',width:180" align="center">项目名称</th>
			<th data-options="field:'sfqymc',width:60" align="center">是否启用</th>
			<th data-options="field:'px',width:80" align="center">排序</th>
			<th data-options="field:'cjsj',width:80 " align="center">创建时间</th>
			<th data-options="field:'cz',width:250" align="center">操作</th>
		</tr>
	</thead>
</table>

<div id="fwxm-window" title="服务项目信息" style="width:550px;height:420px;">
<div style="padding:20px 20px 40px 80px;">
<form id="fwxmForm" method="post" action="${ctx}/admin/saveEjfwxm.do">
	<input type="hidden" name="bizobjs" value="ejfwxm:t_zzfw_fwxm" /> 
	<input type="hidden" name="ejfwxm.fwxmid" id="fwxmid1" /> 
	<input type="hidden" name="ejfwxm.ffwxmid" id="ffwxmid1" /> 
	<input id="wid1" name="ejfwxm.wid" type="hidden" value="" /> 
<table cellpadding="5">
	<tr>
		<td>项目名称:</td>
		<td colspan="3"><input id="fwxmmc1" name="ejfwxm.fwxmmc"
			class="easyui-validatebox textbox" style="width:320px" type="text"
			data-options="required:true"></input><font color="red">(必填)</font></td>
	</tr>
	<tr>
		<td>处理程序:</td>
		<td colspan="3"><input id="clcx1" name="ejfwxm.clcx"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="#333333">(可选)</font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="3"><font color="#333333">如果为Java程序处理，则输入格式为
		java:XXX.XXX.处理类名称。<br />
		如果为存储过程处理，则输入格式为 procedure:存储过程名称。</font></td>
	</tr>
	<tr>
		<td>图标路径:</td>
		<td colspan="3"><input id="tblj1" name="ejfwxm.icon"
			class="easyui-validatebox textbox" style="width:320px" type="text"></input><font color="#333333">(可选)</font></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td colspan="3"><font color="#333333">提示：图标文件放在/resources/image/zzfw/路径下。</font></td>
	</tr>
	<tr>
		<td>是否启用:</td>
		<td><select id="qy2" name="ejfwxm.sfqy" class="easyui-combobox"
			style="width: 60px;">
			<option value="1">是</option>
			<option value="2">否</option>
		</select></td>
		<td align="right">排序:</td>
		<td><input id="px2" name="ejfwxm.px" class="easyui-numberbox"
			style="width: 40px"></input></td>
	</tr>
	<tr>
		<td valign="top">备注说明:</td>
		<td colspan="3"><textarea id="bz1" name="ejfwxm.bz"
			style="width:320px;height:100px;">

                        </textarea></td>
	</tr>
</table>
</form>
</div>
<div style="text-align:center;padding:5px;">
	<a href="javascript:void(0)" onclick="submitFwxmForm()" id="save" icon="icon-save" class="easyui-linkbutton">保存</a>
	<a href="javascript:void(0)" onclick="clearFwxmForm()" id="cancel" icon="icon-cancel" class="easyui-linkbutton">取消</a></div>
</div>
<div id="sfbz-window" title="收费标准信息" style="width:750px;height:520px;">
<div
	style="width: 600px;padding:5px 5px ;font-size: 18px;font-weight: bold"
	align="center"><span id="fwmc"></span></div>
<div style="padding:20px 20px 40px 80px;">
<form id="sfbzForm" method="post" action="${ctx}/admin/saveSfbz.do">
<input type="hidden" name="bizobjs" value="sfbz:t_zzfw_zzdy_dysfbz" />
<input type="hidden" name="sfbz.fwxmid" id="fwxmid2" /> 
<input id="sfbzwid" name="sfbz.wid" type="hidden" value="" />
<table cellpadding="5">
	<tr>
		<td>单价<font color="red">(*)</font>:</td>
		<td><input id="price" name="sfbz.price"
			class="easyui-validatebox numberbox"  precision="2"  style="width:50px" type="text"
			data-options="required:true"></input></td>
		<td>单次打印上限:</td>
		<td><input id="dcdysx" name="sfbz.dcdysx"
			class="easyui-validatebox numberbox" style="width:50px" type="text"></input></td>
		<td>免费打印次数:</td>
		<td><input id="mfdyfs" name="sfbz.mfdyfs"
			class="easyui-validatebox numberbox" style="width:45px" type="text"></input></td>
	</tr>
	<tr>
		<td colspan="1">费用计算处理程序:</td>
		<td colspan="5"><input id="fyclcx" name="sfbz.clcx"
			class="easyui-validatebox textbox" style="width:380px" type="text"></input></td>
		<td><font color="red">(如无，则不填)</font></td>
	</tr>
	<tr>
		<td>报表名称:</td>
		<td colspan="5"><input id="report" name="sfbz.report"
			class="easyui-validatebox textbox" style="width:380px" type="text"></input></td>
	</tr>
	<tr>
		<td>打印机名称:</td>
		<td colspan="5"><input id="dyjmc" name="sfbz.dyjmc"
			class="easyui-validatebox textbox" style="width:380px" type="text"></input></td>
	</tr>
	<tr>
		<td>标准说明:</td>
		<td colspan="5" align="left"><textarea id="sfbzsm"
			name="sfbz.sfbzsm" style="width:380px;height:100px;">
                        	
                        </textarea></td>
	</tr>
	<tr>
		<td>注意事项:</td>
		<td colspan="5" align="left"><textarea id="zysx" name="sfbz.zysx"
			style="width:380px;height:100px;">
                        
                        </textarea></td>
	</tr>
</table>
</form>
</div>
<div style="text-align:center;padding:5px;">
	<a href="javascript:void(0)" onclick="submitSfbzForm()" id="save" icon="icon-save" class="easyui-linkbutton" >保存</a>
	<a href="javascript:void(0)" onclick="clearSfbzForm()" id="cancel" icon="icon-cancel" class="easyui-linkbutton">取消</a>	
</div>
</div>

<div id="ugroup-window" title="用户组信息" style="width:450px;height:auto;">
<div style="padding:10px 0 0 10px;">服务项目向以下用户组开放：</div>
<div style="padding:10px 20px 40px 80px;">
<form id="ugroupForm"><input type="hidden" id='fwxmid_ug' />
<table cellpadding="5" id="yhz">

</table>
</form>
</div>
<div style="text-align:center;padding-bottom:15px;">
	<a href="javascript:void(0)" onclick="submitUgroupForm()" id="save" class="easyui-linkbutton" icon="icon-save">保存</a>
	<a href="javascript:void(0)" onclick="clearUgroupForm()" id="cancel" icon="icon-cancel" class="easyui-linkbutton">取消</a>
	</div>
</div>
<div id="fwdl-window" title="服务项目信息" style="width:560px;height:280px;">
<div style="padding:10px 60px 20px 60px">
<form id="fwdl" method="post" action="${ctx}/admin/saveFwdl.do">
<input type="hidden" name="bizobjs" value="fwdl:t_zzfw_fwxm" /> 
<input type="hidden" name="fwdl.fwxmid" id="fwdlid" /> 
<input type="hidden" name="fwdl.ffwxmid" id="" /> 
<input id="fwdlwid" name="fwdl.wid" type="hidden" value="" /> 
<input name="fwdl.fwlb" type="hidden" value="100" />
<table cellpadding="5">
	<tr>
		<td>服务类别:</td>
		<td colspan="3"><input id="fwdlmc" name="fwdl.fwxmmc"
			class="easyui-validatebox textbox" style="width:320px" type="text"
			validtype="length[0,60]" invalidMessage="有效长度0-60"></input><font color="red">(必填)</font></td>
	</tr>
	<tr>
		<td valign="top">备注说明:</td>
		<td colspan="3"><textarea id="xmbz" name="fwxm.bz"
			style="width:320px;height:100px;">
                        	
      </textarea></td>
	</tr>
	<tr>

		<td colspan="4" align="center">
		<div style="text-align:center;padding:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitFwdlForm()" class="easyui-linkbutton">保存</a> &nbsp;&nbsp;&nbsp; 
			<a href="javascript:void(0)" class="easyui-linkbutton"	onclick="clearFwdlForm()" class="easyui-linkbutton">清空</a>
		</div>
		</td>
	</tr>
</table>
</form>
</div>

</div>

<div align="center" id="xmdlg" class="easyui-dialog" title="是否删除"
	data-options="iconCls:'icon-save'"
	style="width:400px;height:180px;padding:20px 20px 40px 20px;">

<div align="center" id="messager"
	style="font-size: 14px;font-weight: bold;height: 40px;"></div>

<div align="center"><a href="javascript:void(0)"
	onclick="confirmXmDel()" id="save" class="easyui-linkbutton"
	icon="icon-save">确认</a>&nbsp;&nbsp;<a href="javascript:void(0)"
	onclick="cancelXmDel()" id="cancel" class="easyui-linkbutton"
	icon="icon-cancel">取消</a></div>

</div>
<div align="center" id="treedlg" class="easyui-dialog" title="是否删除"
	data-options="iconCls:'icon-save'"
	style="width:400px;height:200px;padding:20px 20px 40px 20px;">

	<div align="center" id="messager3"
		style="font-size: 14px;height: 60px;"></div>
	
	<div align="center"><a href="javascript:void(0)"
		onclick="confirmTreeDel()" id="save" class="easyui-linkbutton"
		icon="icon-save">确认</a>&nbsp;&nbsp;<a href="javascript:void(0)"
		onclick="cancelTreeDel()" id="cancel" class="easyui-linkbutton"
		icon="icon-cancel">取消</a></div>

</div>

<div align="center" id="treedlg2" class="easyui-dialog" title="是否删除"
	data-options="iconCls:'icon-save'"
	style="width:400px;height:auto;padding:20px 20px 40px 20px;">

	<div align="center" id="messager4"
		style="font-size: 14px;;height: 40px;height: auto;padding-bottom: 20px;"></div>
	
	<div align="center"><a href="javascript:void(0)"
		onclick="confirmTreeDel2()" id="save" class="easyui-linkbutton"
		icon="icon-save">确认</a>&nbsp;&nbsp;<a href="javascript:void(0)"
		onclick="cancelTreeDel2()" id="cancel" class="easyui-linkbutton"
		icon="icon-cancel">取消</a></div>

</div>

  </div>
</div>
<div title="系统参数">
	<jsp:include flush="true" page="zzfwSysparam.jsp"></jsp:include>
</div>
<div title="错误字典代码">
	<jsp:include flush="true" page="zzfwErrorcode.jsp"></jsp:include>
</div>
</div>

</body>
</html>
