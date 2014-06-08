<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Set,java.util.Iterator"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.wiscom.is.IdentityFactory"%>
<%@page import="com.wiscom.is.IdentityManager"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>自助服务登录</title>

<link href="${ctx}/resources/css/zzfwlogin.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/resources/css/zzfw.css" type="text/css" rel="stylesheet" />
<script type="text/javascript"
	src="${ctx}/resources/js/jquery/jquery-1.8.2.min.js"></script>
<!-- js框架 -->
<script
	src="${ctx}/resources/js/jquery-plugins/validate/jquery.metadata.js"
	type="text/javascript"></script>
<script
	src="${ctx}/resources/js/jquery-plugins/validate/jquery.validate.js"
	type="text/javascript"></script>
<script src="${ctx}/resources/js/jquery-plugins/validate/messages_cn.js"
	type="text/javascript"></script>
<script src="${ctx}/resources/js/jquery-plugins/validate/validate_ex.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/resources/js/zzfw/vkboard.js"></script>
<script type="text/javascript">
       $(document).ready(function() {
      	   var window_width = 1680;   //22寸宽
	 	   var window_height = 1050;   //22寸高
	 	   $('body').css({width:window_width,height:window_height});	
       	   var buttonContaner_width = $(".zzdy_buttonContaner").width();//底部样式  
	  	   var button_left = window_width - buttonContaner_width -80;
	       $(".zzdy_buttonContaner").css({top:900,left:button_left});	
	       $(".welcom_bottom").css({top:900,left:0});
          
           //登陆验证
            $("#loginForm").validate({
               rules : {
                   j_username : {
                       required : true
                   },
                   j_password : {
                       required : true
                   }
               },
               messages : {
                   j_username : '<font color=red>&nbsp;必填项！</font>',
                   j_password : '<font color=red>&nbsp;必填项！</font>'
               }
           });
          
            //验证码
            $("#captcha").click(function(){  
                $('#captcha').hide().attr('src','${ctx}/captcha.do'+ '?'+ Math.floor(Math.random() * 100)).fadeIn();  
            });  
            
           
           $("#btn_login").click(function(){
	           	if(document.getElementById("j_password_1") && document.getElementById("j_password_1").value !=''){
	           		document.getElementById("j_password").value = document.getElementById("j_password_1").value;
	           	}	           
           }); 
           
           $("#btn_reset").click(function(){
           		document.getElementById("loginForm").reset();
           });             
            
	       //返回功能
	       $("#button_return").click(function(){
	       		window.location.href = "${ctx}/j_spring_security_logout";
	       });  
	         	
		   //退出功能
	       $("#button_exit").click(function(){
	       		window.location.href = "${ctx}/j_spring_security_logout";
	       });
	       setTimeout("selectLoginType('1')",100);         	
       });        
      
</script>

</head>
<body>
<form action="${ctx}/j_spring_ids_security_check" method="post"	id="loginForm">
<input type="hidden" name="mode" value="db" />
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td style="height:180px; background-image:url(resources/image/zzfw/logo.png); background-repeat:no-repeat; background-position:20px 60px;">
		<table width="40%" border="0" align="right" cellpadding="0"
			cellspacing="0">
			<tr>
				<td align="right" valign="bottom"
					style="height:90px; padding-right:20px;">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td valign="top">
		<table width="40%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
						<table width="550" border="0" align="center" cellpadding="0" cellspacing="0"> 
							<tr>
								<td colspan="3" align="center" style="font-family:微软雅黑;font-size: 24px;padding-bottom: 40px;">用户登录</td>
							</tr>
							<tr>
								<td>
								<img alt="一卡通登陆" id="cardImage" src="resources/image/zzfw/card_login.png" onclick="javascript:selectLoginType('1');" style="cursor: pointer;"/></td>

								<td style="width: 150px;"></td>

								<td> <img alt="身份证登陆" id="xhImage" src="resources/image/zzfw/xh_login.png" onclick="javascript:selectLoginType('2');" style="cursor: pointer;"/></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
				<td>&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<div style="padding-left: 80px;">
<table id="keyBoardArrow" style="display:none;" border="0" cellpadding="0" cellspacing="0">
	<tr >
		<td width="100%">
		<table border="0" cellpadding="0" cellspacing="0" frame="void">
			<tr>
				<td id="keyBoardArrow_left"  style="width:200px" ></td>
				<td id="keyBoardArrow_tmp" >&nbsp;</td>
				<td id="keyBoardArrow_center" ></td>
				<td id="keyBoardArrow_right" style="width: 100px;"></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table id="showKeyBoard" style="display:none;" cellpadding="0" cellspacing="0" border="0"  frame="void" bgcolor="#fffcf4">
			 
			<tr>
				<td style="width: 10px">&nbsp;</td>
				<td style="color:#8A1636;font-weight: bold;font-size: 22px" align="center" valign="middle">学号&nbsp;</td>
				<td>
					<input type="text" name="j_username" id="j_username" value="" style="width:220px; height:30px; border:1px solid #F40248"
					onclick="javascript:keyb_change('j_username');" />
				</td>
				<td style="width: 120px">&nbsp;</td>
				<td style="color:#8A1636;font-weight: bold;font-size: 22px;display: none;width:100px;" id="mima" align="center" valign="middle">密&nbsp;码&nbsp;</td>
				<td	style="color:#8A1636;font-weight: bold;font-size: 22px;display: none;width:100px;" id="sfzh" align="center" valign="middle">身份证号&nbsp;</td>
				<td>
					<input type="password" name="j_password" id="j_password" value="" style="width:220px; height:30px; border:1px solid #F40248;display:none;"
					onclick="javascript:keyb_change('j_password');" />
			    </td>
				<td>
					<input type="text" name="j_password_1" id="j_password_1" value="" style="width:220px; height:30px; border:1px solid #F40248;display:none;"
					onclick="javascript:keyb_change('j_password_1');" />
				</td>
				<td style="width: 180px">
						<font color="red" style="font-size: 14px;font-weight: bold; padding-left: 10px">
		                          <c:if test="${param.errorMsg != null}">
		                               用户名或密码错误
		                          </c:if>
		                </font>
		         </td>
				<td>
					<input type="submit" id="btn_login" value="" style="cursor: pointer;border:0px;width:120px;height:42px;background:url(${ctx}/resources/image/zzfw/but_submit.gif);" />
				</td>
				<td style="width: 70px">&nbsp;</td>
				<td>
					<input type="button" id="btn_reset" value="" style="cursor: pointer;border:0px;width:120px;height:42px;background:url(${ctx}/resources/image/zzfw/but_reset.gif);" />
				</td>
				<td style="width: 357px">&nbsp;</td>
			</tr>
			
			<tr valign="top">
				<td style="width: 10px;height: 280px;">&nbsp;</td>
				<td colspan="12" height="350px">
					<div id="keyboard" style="padding-top: 10px"></div>
				</td>
				<td style="width: 10px">&nbsp;</td>
			</tr>
		</table>

		</td>
	</tr>
</table>
</div>
</form>
<div class="welcom_bottom">
		<img src="${ctx}/resources/image/zzfw/welcome-bottom.gif" alt="" />
</div>

<!-- 按钮区域 -->
<div class="zzdy_buttonContaner">
          
          <div class="zzdy_button">
             <img id="button_return" src="${ctx}/resources/image/zzfw/back.png"/>
          </div>
          
          <div class="zzdy_button">
             <img id="button_exit"  src="${ctx}/resources/image/zzfw/exit.png"/>
          </div>
</div> 

<div id="over" class="over"></div>
<div id="layout" class="layout"><img src="./images/loading.gif" /></div>

<script type="text/javascript">
<!--
	function userOnclick()
	{
		document.getElementById("over").style.display = "block";
		document.getElementById("layout").style.display = "block";
	}
	//刷一卡通获取学号等信息

	
	
	
	//屏蔽右键菜单
	document.oncontextmenu = function (event){
	    if(window.event){
	        event = window.event;
	    }try{
	        var the = event.srcElement;
	        if (!((the.tagName == "INPUT" && the.type.toLowerCase() == "text") || the.tagName == "TEXTAREA")){
	            return false;
	        }
	        return true;
	    }catch (e){
	        return false; 
	    } 
	}

   // This example shows the very basic installation
   // of the Virtual Keyboard.
   // 
   // 'keyb_change' and 'keyb_callback' functions
   // do all the job here.

   var opened = false, vkb = null, text = null;

	function getLeft(e){
	   var offset=document.getElementById(e).offsetLeft;
	   if(document.getElementById(e).offsetParent!=null){
	   	  var parent = document.getElementById(e).offsetParent;
	   	  alert(parent.value);
	      offset+=getLeft(parent);
	   } 
	   return offset;
	}
	
   function selectLoginType(type){
   	$('#keyBoardArrow').css('display','block');
   	$('#showKeyBoard').css('display','block');

   	var cardImageWidth = "";
   	var cardImageleft = "";
   	var left_width = 200;
   	$('#j_username').focus();
   	$('#j_password').val('');
   	$('#j_password_1').val('');
   	$('#j_username').val('');
   	if(type=="1"){
   		document.getElementById("mima").style.display = "inline";
   		document.getElementById("sfzh").style.display = "none";
   		document.getElementById("j_password").style.display = "inline";
   		document.getElementById("j_password_1").style.display = "none";
   		cardImageWidth = $('#cardImage').width();
   	    cardImageleft = $('#cardImage').offset().left -280;
   	}else if(type=="2"){
   		document.getElementById("sfzh").style.display = "inline";
   		document.getElementById("mima").style.display = "none";
   		document.getElementById("j_password").style.display = "none";
   		document.getElementById("j_password_1").style.display = "inline";
   		cardImageWidth = $('#xhImage').width();
   	    cardImageleft = $('#xhImage').offset().left -280;
   	}
   	keyb_change("j_username");
   	//计算位置
   	var keyBoradWidth = $('#keyboard').width()+11;
   	var center = cardImageWidth/2 + cardImageleft - 10 ;
   	var postion = -235 + center;
   	var tmp_width = "";
   	var center_width = "";
  
	postion = -220;
	tmp_width = center   ;
	center_width = keyBoradWidth -  tmp_width - 380;
   
   	$('#keyBoardArrow_left').css({'background-image':'url(/zzfw/resources/image/zzfw/keyBoardArrow.gif)','background-repeat':'no-repeat','background-position':'0px 0px','display':'','width':left_width+'px'});
   	$('#keyBoardArrow_tmp').css({'background-image':'url(/zzfw/resources/image/zzfw/keyBoardArrow.gif)','background-repeat':'no-repeat','background-position':'-300px 0px','display':'','width':tmp_width+'px'});
  
   	$('#keyBoardArrow_center').css({'background-image':'url(/zzfw/resources/image/zzfw/keyBoardArrow.gif)','background-repeat':'no-repeat','background-position': postion+'px 0px','display':'','width':center_width+'px','font-size':'22px'});
   	$('#keyBoardArrow_right').css({'background-image':'url(/zzfw/resources/image/zzfw/keyBoardArrow.gif)','background-repeat':'no-repeat','background-position':'-850px 0px','display':'','width':'200px','font-size':'22px'});
		
	} 
	
	
   function keyb_change(obj)
   {
   	
     if(!vkb)
     {
       // Note: all parameters, starting with 3rd, in the following
       // expression are equal to the default parameters for the
       // VKeyboard object. The only exception is 15th parameter
       // (flash switch), which is false by default.
	
       vkb = new VKeyboard("keyboard",    // container's id
                           keyb_callback, // reference to the callback function
                           true,          // create the arrow keys or not? (this and the following params are optional)
                           true,          // create up and down arrow keys? 
                           false,         // reserved
                           true,          // create the numpad or not?
                           "",            // font name ("" == system default)
                           "36px",        // font size in px
                           "#295976",        // font color
                           "red",        // font color for the dead keys
                           "#fffcf4",        // keyboard base background color
                           "#A7CADE",        // keys' background color
                           "#DDD",        // background color of switched/selected item
                           "#fffcf4",        // border color
                           "#CCC",        // border/font color of "inactive" key (key with no value/disabled)
                           "#fffcf4",        // background color of "inactive" key (key with no value/disabled)
                           "#F77",        // border color of the language selector's cell
                           true,          // show key flash on click? (false by default)
                           "#32789F",     // font color during flash
                           "#32789F",     // key background color during flash
                           "#32789F",     // key border color during flash
                           false,         // embed VKeyboard into the page?
                           true,          // use 1-pixel gap between the keys?
                           0);            // index(0-based) of the initial layout
     }
	if(document.getElementById(obj)){
		text = document.getElementById(obj);
     	text.value="";
     	text.focus();
	 
     	if(document.attachEvent)
       		text.attachEvent("onblur", backFocus);
	}
    
   }

   function backFocus()
   {
     if(opened)
     {
       var l = text.value.length;

       setRange(text, l, l);

       text.focus();
     }
   }

   // Callback function:
   function keyb_callback(ch)
   {
     var val = text.value;

     switch(ch)
     {
       case "BackSpace":
         var min = (val.charCodeAt(val.length - 1) == 10) ? 2 : 1;
         text.value = val.substr(0, val.length - min);
         break;

       case "Enter":
         text.value += "\n";
         break;

       default:
         text.value += ch;
     }
   }

   function setRange(ctrl, start, end)
   {
     if(ctrl.setSelectionRange) // Standard way (Mozilla, Opera, ...)
     {
       ctrl.setSelectionRange(start, end);
     }
     else // MS IE
     {
       var range;

       try
       {
         range = ctrl.createTextRange();
       }
       catch(e)
       {
         try
         {
           range = document.body.createTextRange();
           range.moveToElementText(ctrl);
         }
         catch(e)
         {
           range = null;
         }
       }

       if(!range) return;

       range.collapse(true);
       range.moveStart("character", start);
       range.moveEnd("character", end - start);
       range.select();
     }
   }
 //-->
</script>
</body>
</html>
