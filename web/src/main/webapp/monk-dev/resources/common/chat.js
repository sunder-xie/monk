var MMDChat = function(){
    var headerUrl = 'http://'+window.location.host;

    function changeAlertHtml(heardTitle,contentHtml,footHtml){
        var header_obj = $('#monk_alert_model .modal-header');
        var body_obj = $('#monk_alert_model .modal-body');
        var foot_obj = $('#monk_alert_model .modal-footer');

        if(heardTitle == undefined) {
            header_obj.addClass("hidden");
        }else{
            header_obj.html(heardTitle);
            header_obj.removeClass("hidden");
        }

        if(contentHtml == undefined){
            body_obj.addClass("hidden");
        }else{
            body_obj.html(contentHtml);
            body_obj.removeClass("hidden");
        }

        if(footHtml == undefined){
            foot_obj.addClass("hidden");
        }else{
            foot_obj.html(footHtml);
            foot_obj.removeClass("hidden");
        }
    }
    return{
        //弹框
        alertFunc:function(heardTitle,contentHtml,footHtml){
            changeAlertHtml(heardTitle,contentHtml,footHtml);
            $('#monk_alert_model').modal("show");
        },
        alertHide: function () {
            var alert_model = $('#monk_alert_model');
            alert_model.find('.modal-body').html("");
            alert_model.removeAttr("style");
            alert_model.modal("hide");
        },
        alertImg: function (img_src,bodyHtml,is_foot) {
            var b_html = "";
            if(img_src != undefined){
                b_html = '<img id="model_img" src="'+img_src+'">'
            }else{
                b_html =bodyHtml;
            }

            $("#monk_pic_model .modal-body").html(b_html);
            if(is_foot == undefined){
                $("#img_foot").addClass("hidden");
            }else{
                $("#img_foot").removeClass("hidden");
            }
            var height = document.documentElement.clientHeight;
            if ($('#monk_pic_model').height() + 10 > height) {
                $('#monk_pic_model').css("top", "0%");
            } else {
                $('#monk_pic_model').css("top", "50%");
            }
            $('#monk_pic_model').modal("show");
        },
        alertImgHide:function(){
            var pic_model = $('#monk_pic_model');
            pic_model.find('.modal-body').html("");
            pic_model.removeAttr("style");
            pic_model.modal("hide");
        },
        //文字聊天内容记录
        saveText:function(id,toid,message,time){
            $.post(headerUrl+"/monk/chatHis/insert_chat",{id:id,toid:toid,message:message,time:time,type:"text"});
        },
         //图片聊天内容记录
        saveImage:function(id,toid,url,time){
            $.post(headerUrl+"/monk/chatHis/insert_chat",{id:id,toid:toid,message:url,time:time,type:"image"});
        },
        //是否是mac
        isMac:function(){
            var ua = navigator.userAgent.toLowerCase();
            isMac = (/macintosh|mac os x|adobeair/).test(ua);
            return isMac;
        },
        //在target层加入loading
        blockUI: function(target,text) {
            var options = {
                'target':target
            };
            options = $.extend(true, {}, options);

            var span = text==undefined?'':'<span>'+text+'</span>';

            var html = '<div class="loading-message">' + '<div class="block-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div>'+span+ '</div>';
            //alert(imgLoadingPath.height());
            if (options.target) { // element blocking
                var el = $(options.target);
                if (el.height() <= ($(window).height())) {
                    options.cenrerY = true;
                }
                el.block({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 10151,
                    centerY: options.cenrerY !== undefined ? options.cenrerY : false,
                    css: {
                        top: '10%',
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity:  0.1,
                        cursor: 'wait'
                    }
                });
            } else { // page blocking
                $.blockUI({
                    message: html,
                    baseZ: options.zIndex ? options.zIndex : 10151,
                    css: {
                        border: '0',
                        padding: '0',
                        backgroundColor: 'none'
                    },
                    overlayCSS: {
                        backgroundColor: options.overlayColor ? options.overlayColor : '#555',
                        opacity: 0.1,
                        cursor: 'wait'
                    }
                });
            }
        },

        // finish loading
        unblockUI: function(target) {
            if (target) {
                $(target).unblock({
                    onUnblock: function() {
                        $(target).css('position', '');
                        $(target).css('zoom', '');
                    }
                });
            } else {
                $.unblockUI();
            }
        }

    }
}();