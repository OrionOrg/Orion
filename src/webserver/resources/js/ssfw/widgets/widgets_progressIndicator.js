/**
 * 进度导航组件
*/
(function ($) {
	//
    $.widget("ui.progressIndicator",{
		options : {
			width : '1', 
			height : '5',
			userid : '',
			flowID : ''
		},
		destroy : function (){
			//TODO 销毁
		},
		_create : function() {
			var element = this.element,
			    container = this.container = $('<div />').css({'overflow-x':'auto','overflow-y':'hidden','white-space':'nowrap'}),
				nodeContainer = this.nodeContainer = $('<div/>'),
				that = this,
				userid = element.attr('userid');
			element.hide().parent().append(container);
			if(userid){
				this.options.userid = userid;
			}
			var loadTip = $.ui.util.loadingTip('加载中...');
			container.before(loadTip);
			var xwjddh = $('<table border="0" cellspacing="0" cellpadding="0" id="xwjddh"><tr><td colspan="3" id="xwjddh_left">&nbsp;</td><td colspan="80" id="xwjddh_right">&nbsp;</td></tr></table>');
			container.before(xwjddh);
			//$('#xwjddh').css({'margin-top':'-12px'});
			$('#xwjddh_left').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/jddh.jpg)'
							,'background-repeat':'no-repeat','background-position':'-190px -90px','height':'44px','width':'135px'});
			$('#xwjddh_right').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/jddh.jpg)'
							,'background-repeat':'repeat-x','background-position':'-324px -90px','height':'44px','width':'860px'});
			
			//加载图例
			
			var icon_explan = $('<div/>');
			var htmlStr  = '<div id="show" style="font-size:14px;font-family:微软雅黑;color: blue;">显示图例</div><br/>'
						+'<div><table style="display:none;" id="tuli">'
						+'<tr><td id="icon_ywc" width="120"></td><td align="left">已完成的流程节点</td></tr>'
						+'<tr><td id="icon_ydd"></td><td align="left" >当前进行中的流程节点</td></tr>'
						+'<tr><td id="icon_zjd_wdd"></td><td align="left">尚未到达的流程节点</td></tr>'
						+'<tr><td id="icon_zjd_ydd"></td><td align="left">待本人处理的流程节点</td></tr>'
						+'<tr><td id="icon_zjd_yj"></td><td align="left">产生告警的流程节点</td></tr>'
						+'<tr><td id="icon_zjd_gj"></td><td align="left">当前应开展但尚未开展的流程节点</td></tr>'
						+'<tr><td id="icon_zjd_cw"></td><td align="left">系统异常提醒（一般为软硬件或网络故障引起）</td></tr>'
						+'</table></div>';
			icon_explan.html(htmlStr);
			container.after(icon_explan);
			$('#icon_ywc').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-722px -311px','height':'35px'});
			$('#icon_ydd').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-722px -443px','height':'35px'});
			$('#icon_zjd_ydd').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-722px -352px','height':'35px'});
			$('#icon_zjd_wdd').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-722px -398px','height':'35px'});
			$('#icon_zjd_cw').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-845px -310px','height':'35px'});
			$('#icon_zjd_yj').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-845px -352px','height':'35px'});
			$('#icon_zjd_gj').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/tuli.gif)','background-repeat':'no-repeat',
								'background-position':'-845px -398px','height':'35px'});
			
			$('#tuli').css({'color':'#666666','filter':'alpha(opacity=90)','opacity':'0.9'});
			$('#show').bind({
				click:function(){
					if($('#show').text()=='显示图例'){
						$('#tuli').css({'display':'block','border':'solid 1px #CCCCCC','border-style': 'dashed','width':'400px'});
						$('#show').text('隐藏图例');
					}else{
						$('#tuli').css('display','none');
						$('#show').text('显示图例');
					}
				},
				mouseenter: function() {
			       this.style.cursor='pointer';
			       $('#show').attr('title','显示/隐藏图例');
				}	
			});
			$.ajax({
				async : false,
				type : 'POST',
				url : ssfwConfig.contextPath + '/progerssIndicator/init.widgets',
				data : {flowID : this.options.flowID, userID : userid},
				dataType : 'json',
				cache : false,
				success : function (data){
					if (data.jsonStr) {
					//解析json对象	
						var nodeList = eval( "(" + data.jsonStr + ")" );
						if(!nodeList.parentNodeList){
							loadTip.remove();
							return ;
						}
						
						var lczxx = $('<table border="0" cellspacing="0" cellpadding="0" id="lczxx"/>');
						var lczxx_tr = $('<tr/>');
						
						//开始节点
						lczxx_tr.append($('<td style="width:60px;height:53px;font-size:14px;font-family:微软雅黑;text-align:center;vertical-align:middle;" id="start_node">&nbsp;&nbsp;&nbsp;开始&nbsp;&nbsp;&nbsp;&nbsp;</td>')
						.attr("title","开始")
						.css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
						,'background-repeat':'no-repeat','background-position':'0 -65px'})
						);
						lczxx.append(lczxx_tr);
						nodeContainer.append(lczxx);
						nodeContainer.appendTo(container);
						// 初始化子流程节点信息
						var lcjdxx = $('<div id=""/>').css({'overflow-x':'hidden','overflow-y':'hidden','white-space':'nowrap'});
						var parentNode = nodeList.parentNodeList;
						var childNode = nodeList.childNodeList;
						var count = parentNode.length;
						var add = 0;
						var array = [];
						for(var i=0;i<parentNode.length;i++){
							var arr = $('<td style="font-size:18px;vertical-align:middle;"><span id="arr_'+parentNode[i].lcjdid+'">&nbsp;&nbsp;&nbsp;&nbsp;</span></td>');
						 	lczxx_tr.append(arr);
						 	lczxx_tr.append($('<td id="node_'+parentNode[i].lcjdid+'" style="height:45px;font-size:14px;text-align:center;vertical-align:middle;font-family:微软雅黑;" nodeid="'+parentNode[i].lcjdid+'"><span style="font-size:20px;"  id="img_'+parentNode[i].lcjdid+'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'+parentNode[i].lcjdmc+'</td>')
								.attr("cursor","pointer")
								.bind({
									click: function(event,message) {
									//查找 id 以 daohang2_ 开头的table 
									$("table[id^='daohang2_']").css('display','none');
									var fjdid =  $(this).attr("nodeid");
									//如果已经加载过了，则直接显示
									if($.trim($('#daohang2_'+fjdid).text())!=''){
										$('#daohang2_'+fjdid).css('display','block');
										return ;
									}
								   //循环判断是否有子节点,如果有，则显示
								   var daohang = $('<table border="0" cellspacing="0" cellpadding="0" id ="daohang2_'+fjdid+'" style="" />');
								   
								   var firstRow = $('<tr id="firstRow_'+fjdid+'" />');
								   //二级导航样式
								   daohang.append(firstRow);
								  
								   var secondRow = $('<tr id="secondRow_'+fjdid+'"/>');
								   daohang.append(secondRow);
								   lcjdxx.append(daohang);
								    for(var j=0;j<childNode.length;j++){
								    	
								    	if(fjdid==childNode[j].flcjdid){
								    		//如果是第一次加载
								    		var flag =$('#node_'+childNode[j].lcjdid).text();
								    		if(flag==''){
								    			
								    			var jdtd = 	$('<td id="node_'+childNode[j].lcjdid+'" childnodeid="'+childNode[j].lcjdid+'" style="height:45px;font-size:14px;text-align:center;vertical-align:middle;font-family:微软雅黑" ><span style="font-size:18px" id="img_'+childNode[j].lcjdid+'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>'+childNode[j].lcjdmc+'</td>');
								    			secondRow.append(jdtd);
								    			
								    			var childnode_right=$('<td id="childnode_right_'+childNode[j].lcjdid+'" style="height:45px;width:10px;font-size:14px;font-family:微软雅黑">&nbsp;&nbsp;</td>');	
												secondRow.append(childnode_right);
												daohang.append(secondRow);
												lcjdxx.append(daohang);
								    			lcjdxx.appendTo(container);
								    			
								    			var jdid = $('#node_'+childNode[j].lcjdid).attr('childnodeid');
								    			$('#img_'+childNode[j].lcjdid).html($.ui.util.loadingTip(''));
								    			//加载状态
								    			$.ajax({
													async : true,
													type : 'POST',
													url : ssfwConfig.contextPath + '/progerssIndicator/getNodeStatus.widgets',
													data : {flowID : that.options.flowID, nodeID : jdid,userID : userid},
													dataType : 'json',
													success : (function(nodeid){ //这里将jdid封装到闭包传递到变量nodeid
												        return function(data){ 
												    	if(data.jsonStr){
															
															var nodeStatus = eval( "(" + data.jsonStr + ")" );
															
															$('#img_'+nodeid).attr('statusCode',nodeStatus.statusCode);
															$('#node_'+nodeid).attr('mouseOverMsg',nodeStatus.mouseOverMsg);
															//300:已通过
															$('#img_'+nodeid).html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
															if(nodeStatus.statusCode=='300'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-182px -86px'});
															
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-1px -265px'});
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -265px'});
																    
															}else if(nodeStatus.statusCode=='201'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-26px -37px'});
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-2px -312px'});
																 
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -312px'});
											    					
															}else if(nodeStatus.statusCode=='202'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-26px -37px'});
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-2px -312px'});
																 
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -312px'});
											    					
															}else if(nodeStatus.statusCode=='203'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-136px -37px'});
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-2px -312px'});
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -312px'});
															}else if(nodeStatus.statusCode=='100'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-53px -37px'});
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-2px -360px'});
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -360px'});
															}else if(nodeStatus.statusCode=='101'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-108px -34px'});
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-2px -312px'});
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -312px'});
															}else if(nodeStatus.statusCode=='500'){
																$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-80px -34px'});
																$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
																    ,'background-repeat':'no-repeat','background-position':'-2px -312px'});
																$('#childnode_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    					,'background-repeat':'no-repeat','background-position':'-245px -312px'});
															}
															//alert(nodeid);
															//alert("nodeStatus-------"+nodeStatus.statusCode);
														}
														
														}
														
													})(jdid)
													
												 });
												
												//alert(daohang.html());
								    			var childnodeid = childNode[j].lcjdid;
								    			//子节点点击事件调用
												$('#node_'+childNode[j].lcjdid).live('click', function() {
													
												   $.ajax({
												   	async : false,
													type : 'POST',
													url : ssfwConfig.contextPath + '/progerssIndicator/doOnClickNode.widgets',
													data : {flowID : that.options.flowID, nodeID : this.childnodeid,userID : userid},
													dataType : 'json',
													cache : false,
													success : function (data){
														if(data.jsonStr){
														//
														var nodeStatus = eval( "(" + data.jsonStr + ")" );
														//alert("alertMsg-------"+nodeStatus.alertMsg);
														if(nodeStatus.mouseClickAlertMsg){
															alert(nodeStatus.mouseClickAlertMsg);
														} 
														if(nodeStatus.mouseClickConfirmMsg){
															if(confirm(nodeStatus.mouseClickConfirmMsg)){
																if(nodeStatus.link){
																	var url = ssfwConfig.contextPath + nodeStatus.link;
																	window.location.href = url;
																  }
															}
														}
														
													}
													}});});
												$('#node_'+childNode[j].lcjdid).live('mouseover',function(){
													this.style.cursor='pointer';
													var mouseOverMsg = $('#'+this.id).attr('mouseOverMsg');
								  					$('#'+this.id).attr('title',mouseOverMsg);
													
												});
								    		}
								    	}//if结束
								    
								    }//for循环结束
								    //二级导航关闭
								    if($('#closeButton_'+fjdid).text()==''){
								    	var close=$('<td ><span id="closeButton_'+fjdid+'" style="display:none;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>');
								    	secondRow.append(close);
								    	$('#closeButton_'+fjdid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												    ,'background-repeat':'no-repeat','background-position':'-101px -6px','display':'inline-block','font-size':'16px'});
								    	$('#closeButton_'+fjdid).live('click', function() {
											    $('#daohang2_'+fjdid).css('display','none');
										});
								    }else{
								    	$('#daohang2_'+fjdid).css('display','block');
								    }
								    $('#closeButton_'+fjdid).live('mouseover',function(){
													this.style.cursor='pointer';
									});
								    //secondRow.append($('</tr>'));
								   	daohang.append(secondRow);
								   	lcjdxx.append(daohang);
								    lcjdxx.appendTo(container);
								    
									var width_daohang = secondRow.width();
									var jiedian = $('#node_'+fjdid).offset().left + ($('#node_'+fjdid).width()/2);
									
									var left_dh_offset = jiedian - (width_daohang)/2;
									var right_dh_offset = left_dh_offset + width_daohang;
									var startNode = $('#start_node').offset().left;
									var endNode = $('#end_node').offset().left;
									var endNodeWidth = $('#end_node').width();
									//三种情况判断。计算二级导航的总长度是否超过当前节点到结尾的长度，如果超过则，向左延伸
									//判断是否加载背景
								    if($.trim($('#daohang2_'+fjdid).text())!=''){
								    	firstRow.html('<td  rowSpan="2" ><span id="arr-border-left-'+fjdid+'">&nbsp;</span></td><td colSpan="20"><span id="arr-border-'+fjdid+'" style="height:8px;font-size:5px;">&nbsp;</span></td><td rowSpan="2" ><span id="arr-border-right-'+fjdid+'">&nbsp;&nbsp;&nbsp;</span></td>');
								     	//$('#daohang2_'+fjdid).css({'display':'block'});
								     	if(left_dh_offset<startNode){
										    var offset = jiedian -startNode-12;
											
										}else if(left_dh_offset >= startNode && right_dh_offset <= endNode){
											var offset = jiedian- left_dh_offset -12;
										   $('#daohang2_'+fjdid).offset({ 'left': left_dh_offset });
										   
								   		}else if(right_dh_offset > endNode){
								   			var right = endNode - width_daohang - endNodeWidth/2;
								   			
								   			$('#daohang2_'+fjdid).offset({ 'left': right });
								   			var offset = jiedian- right ;
								   		
								   		}
								   		
								   		
								   		var  pos = -576 +offset;
								   		$('#arr-border-left-'+fjdid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/arr-border-left.png)'
												,'background-repeat':'no-repeat','display':'inline-block','width':'12px','font-size':'22px'});
										  
									    $('#arr-border-'+fjdid).parent().css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/arr-border.png)'
									    			,'background-repeat':'no-repeat','background-position':pos+'px 0','width':width_daohang});
							   			$('#arr-border-right-'+fjdid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/arr-border-right.png)'
									    			,'background-repeat':'no-repeat','display':'inline-block','width':'12px','font-size':'22px'});
							   		
								    }
								    
								   
								    var url = ssfwConfig.contextPath + '/progerssIndicator/doOnClickNode.widgets';
								    if($.trim($('#daohang2_'+fjdid).text())=='' && message!="show"){
								    		//$('#daohang2_'+fjdid).css('display','none');
								    		//加载后台方法  
										    $.ajax({
											type : 'POST',
											url : url,
											data : {flowID : that.options.flowID, nodeID : fjdid,userID : userid},
											dataType : 'json',
											success : function (data){
												if(data.jsonStr){
												var nodeStatus =  eval( "(" + data.jsonStr + ")" );
												if(nodeStatus.mouseClickAlertMsg){
													alert(nodeStatus.mouseClickAlertMsg);
												} 
												if(nodeStatus.mouseClickConfirmMsg){
													if(confirm(nodeStatus.mouseClickConfirmMsg)){
														if(nodeStatus.link){
															var url = ssfwConfig.contextPath + nodeStatus.link;
															window.location.href = url;
														  }
													}
												}
												
												}
											}
											});
							    		}
															    
								  },
								  mouseenter: function() {
								  
								  var mouseOverMsg = $('#'+this.id).attr('mouseOverMsg');
								  $('#'+this.id).attr('title',mouseOverMsg);
								   this.style.cursor='pointer';
								  }
								})
								);
							$('#img_'+parentNode[i].lcjdid).html($.ui.util.loadingTip('').css('height','20px'));
							var node_right=$('<td id="node_right_'+parentNode[i].lcjdid+'" style="width:11px;height:45px;font-size:14px;text-align:center;vertical-align:middle;font-family:微软雅黑">&nbsp;&nbsp;&nbsp;</td>');	
							
							lczxx_tr.append(node_right);
							lczxx.append(lczxx_tr);
							nodeContainer.append(lczxx);
							nodeContainer.appendTo(container);
							//加载节点状态
							var fjdid = parentNode[i].lcjdid;
							
							add++;
							$.ajax({
								async : true,
								type : 'POST',
								url : ssfwConfig.contextPath + '/progerssIndicator/getNodeStatus.widgets',
								data : {flowID : that.options.flowID, nodeID : fjdid,userID : userid},
								dataType : 'json',
								success : (function(nodeid,count,array,add){ //这里将fjdid封装到闭包传递到变量nodeid
										return function(data){ 
			
									if(data.jsonStr){
										var nodeStatus = eval( "(" + data.jsonStr + ")" );
										
										$('#img_'+nodeid).attr('statusCode',nodeStatus.statusCode);
										//alert('#img_'+nodeid);
										$('#node_'+nodeid).attr('mouseOverMsg',nodeStatus.mouseOverMsg);
										$('#node_'+nodeid).attr('statusCode',nodeStatus.statusCode);
										
										$('#img_'+nodeid).html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;');
										
										//200:已达到
										if(nodeStatus.statusCode=='300'){
											$('#arr_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-120px -1px'});
											$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-134px -81px'});
											$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'0 -118px'});
											$('#node_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-244px -118px'});
										}else if(nodeStatus.statusCode=='100'){
											$('#arr_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-144px 0'});
											$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-68px 0'});
											$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'repeat','background-position':'0 -214px'});
											$('#node_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-244px -214px'});
											
										}else if(nodeStatus.statusCode=='203'){
											$('#arr_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-120px -1px'});
											$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-134px -37px'});
											$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'repeat','background-position':'0 -166px'});
											$('#node_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-244px -166px'});
										}
										else if(nodeStatus.statusCode=='202'){
											$('#arr_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-120px -1px'});
											$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-36px -1px'});
											$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'repeat','background-position':'0 -166px'});
											$('#node_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-244px -166px'});
										}else if(nodeStatus.statusCode=='201'){
											$('#arr_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-120px -1px'});
											$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-31px 0px'});
											$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'repeat','background-position':'0 -166px'});
											$('#node_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-244px -166px'});
											    
										} else if(nodeStatus.statusCode=='101'){
											$('#arr_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-120px -1px'});
											$('#img_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												 ,'background-repeat':'no-repeat','background-position':'-108px -34px'});
											$('#node_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'repeat','background-position':'0 -166px'});
											$('#node_right_'+nodeid).css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
											    ,'background-repeat':'no-repeat','background-position':'-244px -166px'});
										}
										//判断是否是最后展现的节点
										array.push(add);
										if(count == array.length){
											//触发正在进行中的节点的click事件，如果有二级节点则展示
											$("td[statusCode^='201']").trigger("click","show");	
											$("td[statusCode^='202']").trigger("click","show");
										}
									}
										
								}
						
							})(fjdid,count,array,add)
						});
							
										
						}
						 
						var endarr = $('<td style="font-size:18px;text-align:center;vertical-align:middle;"><span style="height:40px;" id="endarr">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></td>');
						lczxx_tr.append(endarr);
						$('#endarr').css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
												,'background-repeat':'no-repeat','background-position':'-144px 0'});
						//结束节点
						lczxx_tr.append($('<td style="width:65px;height:53px;font-size:14px;font-family:微软雅黑;text-align:center;vertical-align:middle;" id="end_node">&nbsp;&nbsp;&nbsp;&nbsp;结束&nbsp;&nbsp;&nbsp;</td>')
						.attr("title","结束")
						.css({'background-image':'url('+ssfwConfig.contextPath+'/resources/js/ssfw/widgets/jddh/images/icon.png)'
						,'background-repeat':'no-repeat','background-position':'-61px -65px'})
						);
						lczxx.append(lczxx_tr);
						nodeContainer.append(lczxx);
						nodeContainer.appendTo(container);
						loadTip.remove();
						//闪烁效果
						setInterval(function(){ 
						 	var show = $("span[statusCode^='202']").css('visibility');
						 	var show1 = $("span[statusCode^='203']").css('visibility');
						 	var show2 = $("span[statusCode^='101']").css('visibility');
						 	var show3 = $("span[statusCode^='201']").css('visibility');
						 	if(show == 'visible'||show1 == 'visible'||show2 == 'visible'||show3=='visible'){
						 		$("span[statusCode^='202']").css('visibility','hidden');
						 		$("span[statusCode^='203']").css('visibility','hidden');
						 		$("span[statusCode^='101']").css('visibility','hidden');
						 		$("span[statusCode^='201']").css('visibility','hidden');
						 	}else{
						 		$("span[statusCode^='202']").css('visibility','visible');
						 		$("span[statusCode^='203']").css('visibility','visible');
						 		$("span[statusCode^='101']").css('visibility','visible');
						 		$("span[statusCode^='201']").css('visibility','visible');
						 	}
						 	},300); 
					}
					
				},
				error : function(data){
					loadTip.remove();
					alert(data.errorThrown);
				}
				});
			
			//滚动条美化
			//$('body').css({'scrollbar-face-color':'#d9d9d9','scrollbar-highlight-color': '#ffffff','scrollbar-shadow-color': '#ffffff','scrollbar-3dlight-color': '#d9d9d9','scrollbar-track-color':'#ffffff','scrollbar-arrow-color':'#ffffff','scrollbar-darkshadow-color': '#d9d9d9'});
		}
		
	});
})(jQuery);