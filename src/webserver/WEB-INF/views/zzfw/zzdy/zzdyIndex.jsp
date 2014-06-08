<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自助打印</title>
 <%@ include file="/common/widgets.jsp"%>
 <link href="${ctx}/resources/css/zzfw.css" type="text/css" rel="stylesheet"/>
 <script type="text/javascript" src="${ctx}/qz-print/js/deployJava.js"></script>
 <script type="text/javascript" src="${ctx}/qz-print/common.js"></script>
 <script type="text/javascript" src="${ctx}/qz-print/js/html2canvas.js"></script>
 <script type="text/javascript" src="${ctx}/qz-print/js/jquery.plugin.html2canvas.js"></script>
 <style type="text/css">
 	.showNumber{
    	width:400px;
    	margin-top:255px;    	
    	padding-left:20px;
    	height:300px;
    	background-color: #fffcf4;
    }
      
 </style>
 
<script src="${ctx}/resources/js/zzfw/simplefoucs.js" type="text/javascript"></script>
<script type="text/javascript"><!--


  $(document).ready(function(){
	   	//样式控制
	    $(".dytop").css({display:'block',top:879,left:1150});
	   	$('#but_printer').css({display:'block',top:895,left:1115});
	   	
	   	//控制报表预览样式
	   	var W = $('.bannerbox').css('width'); 
		var H = $('.bannerbox').css('height'); 		    
		$(".reportShow").css({'width':W,'height':H});
	});
	
	//显示灰色JS遮罩层             
    function showDialog(objid){  
       	var bH = 1050;//document.documentElement.clientHeight;
		var bW = 1680;//document.documentElement.clientWidth;		
		var objWH=getObjWh(objid);		 
		$("#fullbg").css({width:bW,height:bH,display:"block"});	
		var tbT=objWH.split("|")[0]+"px";
		var tbL=objWH.split("|")[1]+"px"; 
		$("#"+objid).css({top:tbT,left:tbL,display:"block"}); 
		$(window).scroll(function(){resetBg(objid)});
		$(window).resize(function(){resetBg(objid)});
	} 
    
    //获取对象高度
	function getObjWh(obj){                  
		var st=document.documentElement.scrollTop;//滚动条距顶部的距离                  
		var sl=document.documentElement.scrollLeft;//滚动条距左边的距离                  
		var ch=document.documentElement.clientHeight;//屏幕的高度                  
		var cw=document.documentElement.clientWidth;//屏幕的宽度                  
		var objH=$("#"+obj).height();//浮动对象的高度                  
		var objW=$("#"+obj).width();//浮动对象的宽度                  
		var objT=Number(st)+(Number(ch)-Number(objH))/2;                  
		var objL=Number(sl)+(Number(cw)-Number(objW))/2;                  
		return objT+"|"+objL;              
	}      
	
	//刷新窗口高度        
	function resetBg(obj){                  
		var fullbg=$("#fullbg").css("display");                  
		if(fullbg=="block"){  
			var bH2 = 1050;//document.documentElement.clientHeight;
			var bW2 = 1680;//document.documentElement.clientWidth;		                     
			$("#fullbg").css({width:bW2,height:bH2});                      
			var objV=getObjWh(obj);                      
			var tbT=objV.split("|")[0]+"px";                      
			var tbL=objV.split("|")[1]+"px";                      
			$("#"+obj).css({top:tbT,left:tbL});                  
		}              
	} 
	
	//关闭窗口方法
	function closeDialog(did){
		$("#fullbg").css("display","none");                 
		$(".zzfw_dialog").css("display","none");   
	}	
			 
	//将数值四舍五入(保留2位小数)后格式化成金额形式 		
	function formatCurrency(num){
	    num = num.toString().replace(/\$|\,/g,'');
	    if(isNaN(num))
		    num = "0";
		    sign = (num == (num = Math.abs(num)));
		    num = Math.floor(num*100+0.50000000001);
		    cents = num%100;
		    num = Math.floor(num/100).toString();
	    if(cents<10)
		    cents = "0" + cents;
		    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		    num = num.substring(0,num.length-(4*i+3))+','+
		    num.substring(num.length-(4*i+3));
	    return (((sign)?'':'-') + num + '.' + cents);
	}   
        
	/*** 根据选择份数计算打印费用
 	   * @param num 数值 
	*/
 	function  changeNumber(obj){
 	   var that  = obj;
 	   var num = that.value;
	   $('#dyfs').val(num);	
	   var dyfs = $('#dyfs').val();
	   var fwxmid =$('#fwxmid').val(); 	   
	   
	   //修改样式
	   $('.numberBox li').removeClass(); 
	   $(that).addClass('click');
	   
	   //异步调用事件
	    $.ajax({
				async : true,
				type : 'POST',
				url : '${ctx}/zzfw/zzdy/getTotalFee.do',
				data : {fwxmid:fwxmid,dyfs:dyfs},
				dataType : 'json',
				success :function(data){   
					if(data.success){
						var dyfs = data.dyfs;                    //打印份数
						var mffs = data.mffs;                    //可免费打印份数
						var jffs = dyfs - mffs;		             //交费份数				    
						if( dyfs - mffs <0 ){
						   jffs = 0;
						 }
						var totalFee = formatCurrency(data.totalFee);  //总费
						 	
						var jfpzInfo  = eval('('+data.jfpzInfo+')'); 
						var price =  formatCurrency(jfpzInfo.price);    //单价   
					 	var totalFee = formatCurrency(data.totalFee);                    		
	                   	$('#fee').val(totalFee);   
	                   	$('#number').html(num);
	                   	
	                   	var str ="<div >打印 "+num+" 份，共需花费 "+totalFee+" 元  "+jffs+"*"+price+" = "+totalFee+"元。<br/>其中 "+mffs+" 份免费！</div>";
		                    $('#dyMessage').html(str);
					}                              	
				}					
	    });
	 }
		
	/*确认打印功能	*/  
    function printConfirm(){   
      	   var dyfs = $('#dyfs').val();
	  	   var fwxmid =$('#fwxmid').val(); 
	  	   
	  	   if(dyfs=="" || dyfs ==null || dyfs ==0){
	  	   		
	  	   		showDialog('warn_dialog');
	  	   		var str ="<div style='text-align:center'>请先选择份数再进行打印！</div>";               		
		        $('#warnmessage').html(str);
	  	  		return;
	  	   }
	  	   
	  	   var dcdysx =  $('#dcdysx').val();	  	   
	  	   if(dcdysx*1 < dyfs*1){
	  	   		showDialog('warn_dialog');
	  	   		var str = "<div style='text-align:center'>您当前选择"+dyfs+"份，超过上限"+dcdysx+"份，请重新选择！</div>";   
	  	   		$('#warnmessage').html(str);
	  	   		return; 
	  	   }	  	   
	  	   	  	   
	  	   //异步调用事件
		   $.ajax({
					async : true,
					type : 'POST',
					url : '${ctx}/zzfw/zzdy/zzdyComfirm.do',
					data : {fwxmid:fwxmid,dyfs:dyfs},
					dataType : 'json',
					success :function(data){   
						if(data.success){
						    var dyfs = data.dyfs;                    //打印份数
						    var mffs = data.mffs;                    //可免费打印份数
						    var jffs = dyfs - mffs;		             //交费份数				    
						    if( dyfs - mffs <0 ){
						    	jffs = 0;
						    }
						 	var totalFee = formatCurrency(data.totalFee);  //总费
						 	
						 	var jfpzInfo  = eval('('+data.jfpzInfo+')'); 
						 	var price =  formatCurrency(jfpzInfo.price);    //单价       
						 	
						 	var fwxmInfo  = eval('('+data.fwxmInfo+')'); 	
						 	var fwxmid = fwxmInfo.fwxmid;	 //服务项目 id		 	        
						 	var fwxmmc = fwxmInfo.fwxmmc;    //服务项目名称
						 	//重新赋值
						 	$("#fwxmid").val(fwxmid);
						 	$("#fwxmmc").val(fwxmmc);
						 	
						 	$("#dyfs").val(dyfs);
						 	$("#mffs").val(mffs);
						 	$("#dyfy").val(totalFee);
						 	
						 	//以下处理弹出层信息						 
						 	var str ="<div style='text-align:center'>打印<font color='red'>"+dyfs+"</font>份《"+fwxmmc+"》，一共需要<font color='red'>"+jffs+"</font>x<font color='red'>"+price+"</font> =<font color='red'>"+totalFee+"</font>元。<br>其中<font color='red'>"+mffs+"</font>份免费！</div>";               		
		                    $('#comfirm_dialog_content').html(str);
						}                              	
				   }					
		    });   
           showDialog("comfirm_dialog"); 
	} 
	
	function goNext(obj){
		//如果费用为0,则直接打印
		var totalFee = $("#dyfy").val();
		//var totalFee = '0.00';
		if(obj=='payment_dialog' && totalFee =='0.00'){
			print();
			return;
		}
		//如果支付失败
		var flag =  true;
		if(obj=='print_dialog' && !flag){
			//以下处理弹出层信息						 
		 	var str ="<div style='text-align:center;font-family:微软雅黑;font-size:16px;font-weight: bold;'>对不起，您的支付有误，无法为您打印，请充值！</div>";               		
	        $('#pay_dialog_content').html(str);
	        $('#pay_error').css('display','block');
	        $('#pay_success').css('display','none');
			showDialog("payresult_dialog");
			return;
		}
		if(obj =='payment_dialog'){
			showDialog(obj);
		}
		if(obj =='ykt_dialog'){
			showDialog(obj);
		}
		if(obj=='print_dialog' && flag){
		 	 showDialog(obj);
		 	var str ="<div style='text-align:center;font-family:微软雅黑;font-size:16px;font-weight: bold;'>恭喜您，打印成功，查看出纸口并及时取走您的打印结果！</div>";               		
	        $('#pay_dialog_content').html(str);
	        //$('#pay_error').css('display','none');
	        //$('#pay_success').css('display','block');
			//setTimeout("showDialog('payresult_dialog')",3000); ;
		 	setTimeout("print()",3000); 
		 }
		 
	}
	
	function payment_select(fwxmid,dyfs){		
		showDialog("payment_dialog");     
	}
	
		
	/*支付功能*/  
    function pay_before(fwxmid,dyfs){     
       var dyfs = $('#dyfs').val();
  	   var fwxmid =$('#fwxmid').val(); 
	  	   
	   //以下处理弹出层信息
	   $('#dialog_title').html("支付确认");
	   var str ="<div tyle='text-align:center'><img src='${ctx}/resources/image/zzfw/paying.png' height='120' width='120'/></div>";               		
       $('#dialog_content').html(str);
       $('#dialog_button').html('');
       showDialog("dialog");       
       setTimeout("pay_sucess()",3000);    
       
       
	} 
	
	function pay_fail(){
		
	   var dyfs = $('#dyfs').val();
  	   var fwxmid =$('#fwxmid').val(); 
	  	   
	   //以下处理弹出层信息
	   $('#dialog_title').html("支付结果");
	    var str ="<div tyle='text-align:center'><img src='${ctx}/resources/image/zzfw/fail.png' height='107' width='180'/></div>";               		
	        str +="<div tyle='text-align:center'>支付失败，卡内余额不足！</div>";               		
       $('#dialog_content').html(str);
       var buttonContainer = $('#dialog_button');
	   $('#dialog_button').html('sdfasfasdfasdfasdfasdfasdfasdfasdfasdf');
		                    
		                
       showDialog("dialog");      
	
	}
	
	function pay_sucess(){		
	 //取值
	 var fwxmid = $("#fwxmid").val();
	 var fwxmmc = $("#fwxmmc").val();
	 var dyfs = $("#dyfs").val();
	 var Totalfee = $("#dyfy").val();
	  	   
	   //以下处理弹出层信息
	   $('#dialog_title').html("支付结果");
	    var str ="<div tyle='text-align:center'><img src='${ctx}/resources/image/zzfw/sucess.png'  height='120' width='120'/></div>";               		
	        str +="<div tyle='text-align:left'>您共花费"+Totalfee+"元，打印"+dyfs+"张《"+fwxmmc+"》报表，点击确定为您打印！</div>";               		
       $('#dialog_content').html(str);
       //生成按钮
       var buttonContainer = $('#dialog_button');
        $('#dialog_button').html('');
       
       //生成确认支付按钮
       var qrzf_button =$('<a/>').addClass('zzfw_button').click(function (){
			print();
	   }),							
	   qrzf_buttonspan = $('<span/>',{html : '确定'});
	   qrzf_buttonspan.appendTo(qrzf_button);
	   qrzf_button.appendTo(buttonContainer);
		                
       showDialog("dialog");      	  
	}	
	
	 //初始化打印机
	 window.setInterval("isprint()", 1000);
    
	 function isprint(){
	 	var dyjmc = $("#dyjmc").val();
	 	findPrinter(dyjmc);
	 }
     
     
	function print(){    
		showDialog('print_dialog'); 
        //取值
	 	var fwxmid = $("#fwxmid").val();
	 	var fwxmmc = $("#fwxmmc").val();
	 	var dyfs   = $("#dyfs").val() * 1;
	 	var Totalfee = $("#Totalfee").val();
	   
	    //调用打印事件
	   	var report = $("#report").val();
        printPDF(report,dyfs);
      
	}  
	   
	function print_success(flag,message){
		var dyfs   = $("#dyfs").val();
		var ydyfs   = dyfs;		
		if(flag==1){
	 		$('#dycgbs').val("1");	
			$('#ydyfs').val(ydyfs);	
			$('#dysbxx').val("");		 
		}else{
			$('#dycgbs').val("0");
			$('#ydyfs').val(ydyfs-1);	
			$('#dysbxx').val(message);   			
		}
		
		$('#jfmxform').attr('action', '${ctx}/zzfw/zzdy/finish.do');
   		$('#jfmxform').submit();   			 
	}    
	
	function add(obj)
	{
	 	//修改样式
	 	var str = obj.value;
	 	var that = obj;
	    $('.numberBox1 li').removeClass(); 
	    $(that).addClass('click');
  		eval('document.getElementById("cardPassword").value = document.getElementById("cardPassword").value + str');
	}
	
	function init(){
		document.getElementById("cardPassword").focus();
	}
	  function back(obj){
	  	var that = obj;
		$('.numberBox1 li').removeClass(); 
		$(that).addClass('click');
	  	var numbers = document.getElementById("cardPassword").value;
	  	var len=numbers.length;
	  	document.getElementById("cardPassword").value=numbers.slice(0,len-1);
		return false;
	  }
  /*清除选择的份数*/
  function clean(){
  	$('.numberBox li').removeClass();
  	$("#dyfs").val(0);
  	$('#dyMessage').html('请先选择份数再进行打印！<br/>&nbsp;');
  }
  
--></script> 
</head>
<body>
	<!-- qz-print开始 -->
	<input type="hidden" name="ctx" value="${ctx}">
	<script>deployQZ("${ctx}");</script>
	<input id="printer" type="hidden" value="zebra" size="15"> 
	<canvas id="hidden_screenshot" style="display:none;"></canvas>
	<!-- qz-print结束 -->
	
	<div class="zzdyContainer">
		<form action="" id="jfmxform" name="jfmxform" method="post">	
			<input type="hidden" name="bizobjs" value="jfmx:T_ZZFW_ZZDY_ZZDYMX"/>
			
			<input  id = "wid"    name="jfmx.wid"    type="hidden" value=""/>  				
			
			<input  id = "fwxmid" name="jfmx.fwxmid"  type="hidden" value="${fwxmInfo.fwxmid}">
				 
			<input  id = "userid" name="jfmx.userid"  type="hidden" value="">
		 
			<input  id = "dycgbs" name="jfmx.dycgbs"  type="hidden" value="">
		 
			<input  id = "dysbxx" name="jfmx.dysbxx"  type="hidden" value="">	
			
			<input  id = "dyfs"   name="jfmx.dyfs"    type="hidden" value="">	
			
			<input  id = "dyfy"   name="jfmx.dyfy"    type="hidden" value="">	
			
			<input  id = "price"  name="jfmx.price"   type="hidden" value="${jfxxInfo.price}">	
			
			<input  id = "mffs"   name="jfmx.mffs"    type="hidden" value="">	
			
			<input  id = "jykh"   name="jfmx.jykh"    type="hidden" value="">	
			
			<input  id = "jylsh"  name="jfmx.jylsh"   type="hidden" value="">	
			
			<input  id = "ydyfs"  name="jfmx.ydyfs"   type="hidden" value="">			 
		</form>
	
		<!-- 报表显示区域 -->
			<!-- 代码 开始 -->
		<div class="reportShow">			
			
			<div class="bannerbox">
			    <div id="focus">
			        <ul>	
			        	<c:forEach var="a" items="${reportInfo}">

							<li><img src="${ctx}/zzfw/zzdy/getImages.do?name=${a}&fwxmId=${fwxmInfo.fwxmid}" height="841" width="595" alt="" /></li>

 						</c:forEach>		        
			            
			             
			        </ul>
			    </div>
			</div>
			<!-- 代码 结束 -->

		</div>
		<!-- 操作区域 -->	
		<div class="optionShow">
					 
				<div class="showNumber" id="fysm"> 
					<!-- 服务项目名称 -->
					<input  id = "fwxmmc"   name="fwxmmc"      type="hidden" value="${fwxmInfo.fwxmmc}">
				 	<!-- 打印上限 -->
					<input  id = "dcdysx"   name="dcdysx"     type="hidden" value="${jfxxInfo.dcdysx}">					
					<!-- 注意事项 -->
					<input  id = "zysx"     name="zysx"     type="hidden" value="${jfxxInfo.zysx}">	
					
					<!-- 打印机名称 -->
					<input  id = "dyjmc"     name="dyjmc"     type="hidden" value="${jfxxInfo.dyjmc}">	
					
					<!-- 报表信息 -->
					<input  id = "report"     name="report"     type="hidden" value="${jfxxInfo.report}">	
				 	
				 	<div>
						 <ul>  
					       <li><div id="dyMessage" style='padding-top:10px;color:#b9194c;font-family:宋体;font-size:16px;'>&nbsp;请先选择份数再进行打印！<br/>&nbsp;</div></li>
					       <li style=""><div  style="color:#3e3e3e;font-family: 宋体;font-size: 14px;height: 100px;">*费用说明：<br>&nbsp;&nbsp;<font style="color:#828282;font-family: 微软雅黑;font-size: 12px;">${jfxxInfo.sfbzsm}</font></div></li>  
						 </ul>				
					</div>
				
					 <div class="numberBox" id="box"> 								
					    <ul>  
					        <li  value="1"  onclick="changeNumber(this);">1</li>  
					        <li  value="2"  onclick="changeNumber(this);">2</li>  
					        <li  value="3"  onclick="changeNumber(this);">3</li>  
					        <li  value="4"  onclick="changeNumber(this);">4</li>  
					        <li  value="5"  onclick="changeNumber(this);">5</li>  
					        <li  value="6"  onclick="changeNumber(this);">6</li> 
					        <li  value="7"  onclick="changeNumber(this);">7</li>  
					        <li  value="8"  onclick="changeNumber(this);">8</li>  
					        <li  value="9"  onclick="changeNumber(this);">9</li>
					        <li  value="10" onclick="changeNumber(this);">10</li>
					        <li  value="11" onclick="clean();" style="width: 90px;text-align: center;">C</li>
					    </ul>  
					<div>
					
				</div>
			
			<div class="dytop"></div>
	        
		</div>
		
			
	</div>
	<div id="but_printer" style="position:absolute;float:left; cursor: pointer;">
	            <img src="${ctx}/resources/image/zzfw/printer.png" onclick="printConfirm()" />
	        </div>	
	<!-- JS遮罩层 -->
	<div id="fullbg" class="fullbg"></div>
	<!-- end JS遮罩层 -->
	
	<!-- 确认对话框 -->
	
	<div  class="zzfw_dialog" id="comfirm_dialog" style="display: none">
		<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td align="center"  colspan="2"  class="dialog_title">打印确认</td>
			</tr>
			<tr>
				<td align="center" valign="middle" colspan="2" id="comfirm_dialog_content" class="dialog_content"></td>
			</tr>
			<tr>
				<td align="center"  class="dialog_bottom"> 
                  <img onclick="goNext('payment_dialog')" src="${ctx}/resources/image/zzfw/paynow.gif"/>
                  </td>
                  <td style="padding-bottom: 20px;">
                  <img onclick="closeDialog('comfirm_dialog')"  src="${ctx}/resources/image/zzfw/cancelback.gif"/>
				</td>
			</tr>		
		</table>
	</div>
	</div>
	<!-- 支付方式选择对话框 -->
	<div  class="zzfw_dialog" id="payment_dialog" style="display: none">
	<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" class="dialog_title">支付确认</td>
			</tr>
			<tr>
				<td align="center" valign="top"  >
					<div style="padding-left: 120px;padding-top: 30px;float: left;cursor: pointer;"><img src="${ctx}/resources/image/zzfw/pay_ykt.gif" onclick="goNext('ykt_dialog')"></div>
					<div style="padding-left: 60px;padding-top: 30px;float: left;cursor: pointer;"><img src="${ctx}/resources/image/zzfw/pay_zft.gif" onclick="alert('暂时不支持支付通！')"></div>				
				</td>
			</tr>
			<tr>
				<td align="center" style="padding-bottom: 20px;cursor: pointer;"> 
                  <img onclick="closeDialog('payment_dialog')"  src="${ctx}/resources/image/zzfw/cancelback.gif"/>
				</td>
			</tr>		
		</table>
	</div>
	</div>
	
	<!-- 一卡通支付对话框 -->
	<div  class="zzfw_dialog" id="ykt_dialog" style="display: none">
	<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" class="dialog_title">一卡通支付</td>
			</tr>
			<tr>
				<td align="left"  class="dialog_content">
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="left"><img src="${ctx}/resources/image/zzfw/pay_ykt.gif" /></td>
							<td valign="middle" style="margin-right: -2px;"><img src="${ctx}/resources/image/zzfw/passwordArrow.gif" style=""/></td>
							<td valign="top" style="background-color: #F6EEE8">
							<table cellspacing="0" cellpadding="0" border="0">
								<tr>
									<td >&nbsp;&nbsp;&nbsp;</td>
									<td valign="middle" style="font-size: 14px;font-weight: lighter">支付密码&nbsp;</td>
									<td valign="middle"><input id="cardPassword" type="password"  value="" style="width:150px; height:30px; border:1px solid gray"/></td>
									<td valign="middle">&nbsp;&nbsp;<img  src="${ctx}/resources/image/zzfw/pay_ok.gif" onclick="goNext('print_dialog');" style="padding-top:5px;cursor: pointer;"/></td>
									<td >&nbsp;&nbsp;&nbsp;</td>
								</tr>
								<tr>
									<td colspan="5" style="padding-left:20px;">
										<div class="numberBox1"> 								
									    <ul>  
									        <li  value="1"  onclick="add(this);">1</li>  
									        <li  value="2"  onclick="add(this);">2</li>  
									        <li  value="3"  onclick="add(this);">3</li>  
									        <li  value="4"  onclick="add(this);">4</li>  
									        <li  value="5"  onclick="add(this);">5</li>  
									        <li  value="6"  onclick="add(this);">6</li> 
									        <li  value="7"  onclick="add(this);">7</li>  
									        <li  value="8"  onclick="add(this);">8</li>  
									        <li  value="9"  onclick="add(this);">9</li>
									        <li  value="0"  onclick="add(this);">0</li>
									        <li  value="回退" onclick="back(this)" style="width: 68px;">回退</li>
									    </ul>  
										<div>
									</td>
								</tr>
							</table>
								
							</td>
						</tr>
					</table>		
				</td>
			</tr>
			<tr>
				<td align="center" class="dialog_bottom"> 
                  <img onclick="closeDialog('ykt_dialog')"  src="${ctx}/resources/image/zzfw/cancelback.gif"/>
				</td>
			</tr>		
		</table>
	</div>
	</div>
	
	<!-- 打印中对话框 -->
	<div  class="zzfw_dialog" id="print_dialog" style="display: none">
	<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0" >
			
			<tr>
				<td align="center" valign="middle"  >
					<div style="padding-left: 240px;padding-top: 10px;float: left;"><img src="${ctx}/resources/image/zzfw/paying.gif"></div>
				</td>
			</tr>
			<tr>
				<td align="center" style="font-size: 16px;">打印中，请稍后...</td>
			</tr>
				
		</table>
	</div>
	</div>
	
	<!-- 支付结果对话框 -->
	
	<div  class="zzfw_dialog" id="payresult_dialog" style="display: none">
		<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td align="center"  colspan="2"  class="dialog_title">支付结果</td>
			</tr>
			<tr>
				<td align="center" valign="middle"  >
					<div id="pay_error" style="padding-left: 240px;padding-top: 30px;float: left;display: none"><img src="${ctx}/resources/image/zzfw/pay_error.gif"></div>
					<div id="pay_success" style="padding-left: 240px;padding-top: 30px;float: left;display: none"><img src="${ctx}/resources/image/zzfw/pay_success.gif"></div>
				</td>
			</tr>
			<tr>
				<td align="center" valign="middle" colspan="2" id="pay_dialog_content" style="height: 100px;"></td>
			</tr>
			<tr>
                  <td align="center"  style="padding-bottom:60px;cursor: pointer;" >
                  <img onclick="closeDialog('comfirm_dialog')"  src="${ctx}/resources/image/zzfw/back.gif"/>
				</td>
			</tr>		
		</table>
	</div>
	</div>
	
	
	<div  class="zzfw_dialog" id="warn_dialog" style="display: none">
	<div class="dialog_border" >
		<table   width="100%" height="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center"  class="dialog_title">提示信息</td>
			</tr>
			<tr>
				<td align="center" valign="middle" class="dialog_content"><div id="warnmessage"></div></td>
			</tr>
			<tr>
				<td align="center" style="cursor: pointer;"> 
                  <img onclick="closeDialog('warn_dialog')"  src="${ctx}/resources/image/zzfw/cancelback.gif"/>
				</td>
			</tr>		
		</table>
	</div>
	</div>
</body>
</html>