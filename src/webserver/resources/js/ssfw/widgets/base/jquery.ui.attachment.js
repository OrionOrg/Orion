/**
 * author : qsyan
 */
(function($) {
	$.widget('ui.attachment', {
		options : {
			allowFileType : [], //['jpg','bmp']
			size : 5,
			success : $.noop,
			error : $.noop,
			beforeRemove : $.noop,			 
			afterRemove : $.noop,
			handler : 'attachmentHandler',
			attachInfo : null,
			serviceId : null,
			buttonValue  : '文件上传',
			oneKeyDownload : true
		},
		destroy : function (){
			//TODO 销毁
		},
		getFileInfos : function (){
			return this.attchments;
		},
		getValue : function (){
			return this.element.val();
		},
		getServiceId : function (){
			var element = this.element;
			serviceId = element.attr('serviceId');
			return serviceId;
		},		
		_changeServiceId : function (attachment){		
			var that = this;	
		    that.element.attr('serviceId',attachment.serviceId);	
		    that.element.val(attachment.serviceId);			       	 
		},
		_upload : function (file){
			// 验证文件类型
			if(!file && !that.uploading) return ;
			var fileValue = file.value;
			if(!fileValue || fileValue.indexOf('.') == -1) return;
			var index = fileValue.indexOf('.'),
			    extName = fileValue.substring(index + 1).toUpperCase(),
			    that = this;
			for(var i = 0,length = this.options.allowFileType.length; i < length; i++){
				var type = this.options.allowFileType[i];
				if(type.toUpperCase() === extName){
					break;
				}
			}
			if(this.options.allowFileType.length && i === length){
				alert('不能上传文件类型' + extName + '。');
				return;
			}
			var frame = $('#tempFileUploadFrame');
			if(!this.frame && !frame.length){
				this.frame = $('<iframe id=\'tempFileUploadFrame\' name=\'tempFileUploadFrame\' src="about:blank"/>')
				.css({left : '-700px', position : 'absolute'}).appendTo('body');
			}else{
				this.frame = frame;
			}
			// 上传文件
			var tempFileUploadForm = $('<form/>',{
                    enctype : 'multipart/form-data',
                    id : 'tempFileUploadForm',
                    method : 'post',
                    action : ssfwConfig.contextPath + '/attach/uploadAttachment.widgets',
                    target : 'tempFileUploadFrame'
               }).css({ position : 'absolute', left : -700});
            $(file).appendTo(tempFileUploadForm);
            $('<input/>',{type : 'hidden',name : 'handler',value : that.options.handler}).appendTo(tempFileUploadForm);
            $('<input/>',{type : 'hidden',name : 'serviceId',value : that.options.serviceId}).appendTo(tempFileUploadForm);
            if(that.options.attachInfo){
               $('<input/>',{type : 'hidden',name : 'attachinfo',value : that.options.attachInfo}).appendTo(tempFileUploadForm);
            }
   			var loadTip = $.ui.util.loadingTip('上传中...');
			$('.ui-attchment-button-container',that.container).before(loadTip);
			that.frame.bind('load',function (e){
				    try{
				    	eval('var resultData = ' + this.contentWindow.document.body.innerHTML);				     
				    	if(resultData.success == true){
		                	if(that.options.size === 1){
		                	   that.element.val(resultData.attachmentId);
		                	   $('.ui-attchment',that.container).remove();
		                	}		                	
		                	
		                	that._createAttachmentByJsonObject(resultData);
		                	
		                	var val = that.element.val();
		                	if(val){
		                	    val += ',';
		                	}
		                	that.element.val(val + resultData.attachmentId);
		                	that._changeServiceId(resultData); //serviceId重新赋值
		                	that._trigger('success',e,resultData);
		            	 }else{
		            	    that._trigger('error',e);
		            	 }
				    }catch(ex){
				    	that._trigger('error',e);
				    }
            	    loadTip.remove();
            	    $(this).unbind('load').attr('src','about:blank');
            	    that.uploading = false;
             });
             tempFileUploadForm.appendTo('body');
             that.uploading = true;
             tempFileUploadForm.submit();
 	         this._createInputFile();
		},
		_createInputFile : function (width){
			var that = this;
			that.marginwidth = 	width;	
			if(!width){
				that.marginwidth=-30;
			}		
			$('#attchment-button-file').remove();	
				 
			$('<input>',{id:'attchment-button-file',type : 'file',size : 28,name : 'attachment'})
		    .addClass('ui-attchment-button-file')
		    .css({opacity : 0,left : that.marginwidth,top : 0})		   
		    .appendTo(this.buttonContainer)
		    .change(function (){
				var alreadyUploaded = $('.ui-attchment',that.container).length;
				if(alreadyUploaded < that.options.size){
					that._upload(this);
				}else{
					alert('最多只能上传 ' + that.options.size + ' 个文件。');
					that._createInputFile();
				}
			 });
		},
		_createAttachmentByJsonObject : function (attachment){
			var attchmentDom = $('<div/>').addClass('ui-attchment'),
			    donwloadLink = $('<a/>',{ target : '_blank' ,href : ssfwConfig.contextPath + '/attach/downloadAttachment.widgets?attachmentId=' + attachment.attachmentId,title : '下载'}).text(attachment.originName).css({cursor : 'pointer'}),
			    name = $('<div/>').css({float : 'left'}).append(donwloadLink),
			    remove = $('<div/>',{title : '删除'}).html('<a>删除</a>').addClass('ui-attchment-button-remove').data('attachmentId',attachment.attachmentId).css('display',this.editModel == true ? 'block' : 'none'),
			    clear = $('<div/>').addClass('ui-clear'),
			    buttonContainer = $('.ui-attchment-button-container',this.container);
				attchmentDom.append(name).append(remove).append(clear),
				that = this;
			if(buttonContainer.length){
				buttonContainer.before(attchmentDom);
			}else{
				this.container.append(attchmentDom);
			}
			(function (attachmentInst){
				remove.click(function (e){
				var attachmentId = $(this).data('attachmentId'),remove = this;
				attachmentInst._trigger('beforeRemove',e,attachmentId);
				if(attachmentInst._trigger('beforeRemove',e,attachmentId) === false){
					return false;
				}
				var loadTip = $.ui.util.loadingTip('删除中...');
				$('.ui-attchment:first',attachmentInst.container).before(loadTip);
				$.ajax({
					url : ssfwConfig.contextPath + '/attach/deleteAttachment.widgets?attachmentId=' + attachmentId,
					dataType : 'json',
					success : function (data){
					    attachmentInst._trigger('afterRemove',e,data,attachmentId);
						loadTip.remove();
						if(data.success == true){
							$(remove).closest('.ui-attchment').remove();
						}else{
							alert('删除失败');
						}
					}
				});
			    return false;
			 });
			})(that);
		
		},
		_create : function() {
			var element = this.element,
			    container = this.container = $('<div/>').addClass('ui-attchmentContainer'),
				attchment = $('<div/>').addClass('ui-attchment'),
				buttonContainer = this.buttonContainer = $('<div/>').addClass('ui-attchment-button-container'),
				that = this,
				customHandler = element.attr('handler'),
				customAttachInfo = element.attr('attachInfo'),
				serviceId = element.attr('serviceId'),
				elementVal = element.val(),
				buttonValue  = element.attr('buttonValue');
			if(customHandler){
				this.options.handler = customHandler;
			}
			if(customAttachInfo){
				this.options.attachInfo = customAttachInfo;
			}
			if(serviceId){
				this.options.serviceId = serviceId;
			}else if(elementVal){
				this.options.serviceId = elementVal;
			}
			if(buttonValue){
				this.options.buttonValue = buttonValue;
			}
			
			var loadTip = $.ui.util.loadingTip('加载中...').appendTo(container);
			element.hide().parent().append(container);
			// 获取附件列表信息
			$.ajax({
				url : ssfwConfig.contextPath + '/attach/listAttachment.widgets',
				data : {handler : this.options.handler, serviceId : this.options.serviceId},
				dataType : 'json',
				cache : false,
				success : function (data){
					if (data.attachments) {
						for ( var i = 0; i < data.attachments.length; i++) {
							var attchmentData = data.attachments[i];
							that._createAttachmentByJsonObject(attchmentData);
						}
					}
					loadTip.remove();
				}
			});
				
			//$('<input/>',{type : 'button',value : this.options.buttonValue})
				//.addClass('ui-attchment-button')
				//.addClass('submitLong')
				//.appendTo(buttonContainer);
				//.appendTo(buttonContainer);
		     var attach_button = this.attach_button = $('<a/>').addClass('attach_button')
		     .bind("mouseover",function(e){
		     		var attachLeft = that.attach_button.offset().left; 
		     		var mouseLeft  = e.pageX;		     	 
		     		var left = (mouseLeft-attachLeft) -50;		     		 
		     		that._createInputFile(left);
		     	}		     
		     );
			 var buttonspan =this.buttonspan = $('<span/>',{html : this.options.buttonValue});
			 buttonspan.appendTo(attach_button);
			 attach_button.appendTo(buttonContainer);
				
			//this._createInputFile();
			buttonContainer.appendTo(container);
			if(this.options.oneKeyDownload){
				var download_button = this.download_button =$('<a/>').addClass('attach_button').css('left',100)
				.click(function (){
					window.open(ssfwConfig.contextPath + '/attach/oneKeyDownload.widgets?serviceId='+ that.options.serviceId);
				}),
				download_buttonspan = $('<span/>',{html : '下载'});
				download_buttonspan.appendTo(download_button);
				download_button.appendTo(buttonContainer);
			}
		},
		showEdit : function() {
			if(!this.element.prop('readonly')){
				$('.ui-attchment-button-container',this.container).show();
		        $('.ui-attchment-button-remove',this.container).show();
		        this.editModel = true;
			}
		},
		showView : function() {
		   $('.ui-attchment-button-container',this.container).hide();
		   $('.ui-attchment-button-remove',this.container).hide();
		   this.editModel = false;
		}
	});
})(jQuery);
