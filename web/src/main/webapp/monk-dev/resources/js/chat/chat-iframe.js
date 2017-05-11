/*
* 淘汽 自有im接入js
* by zxg 2016.04.12
*
* 我是后台开发，偶尔客串写前端，前端代码 不漂亮的地方，欢迎专业前端大神指出哈
* */

//用户切换点击事件
var beforeChangePeople;
//公用内嵌页面
var  TimIframeUtil = function(){
    return{
        //弹框
        alertFunc:function(heard_title,content_html,foot_html){
            parent.alertForChild(heard_title,content_html,foot_html);
        },
        alertHide: function () {
            parent.alertHideForChild();
        },
        alertImg: function (img_src) {
            parent.alertImgForChlid(img_src);
        },
        alertImgHide:function(){
            parent.alertImgHideForChlid();
        },

        //内嵌页面使用，发内容到聊天框--内，可二次加工
        sendMessageToChatArea:function(message_list){
            if(! (message_list instanceof Array)){
                console.log("非数组参数")
            }
            if(message_list.length != 0){
                var area_obj = $(window.parent.document).find("#new_message_area");
                var old_value = area_obj.val();
                area_obj.val(old_value+message_list.join("\n"));
            }
            return false;
        },
        //内嵌页面使用，发内容到对话框内
        sendMessageToChatAreaAndSend:function(message_list){
            if(! (message_list instanceof Array)){
                console.log("非数组参数")
            }
            if(message_list.length != 0){
                var area_obj = $(window.parent.document).find("#new_message_area");
                var send_btn_obj = $(window.parent.document).find("#send_btn");
                var old_value = area_obj.val();
                area_obj.val(message_list.join("\n"));
                send_btn_obj.trigger('click');
                area_obj.val(old_value);
            }
            return false;
        },
        //关闭顶部内嵌页面
        closeTopIframe:function(){
            parent.expand_top_click();
        },
        //关闭整个聊天框
        closeChatWindow: function () {
            parent.windowClose();
        }
    }
}();

//专门给lop使用的
//处理需求单数据
var MonkWishListFunc;
var TimIframeUtilForLOP = function(){
    return{
        //获得当前聊天对象的今日聊到,当个数据
        todayWishList:function(do_fuc){
            MonkWishListFunc = do_fuc;
        }
    }

}();

"function"==typeof define&&define("TqmallImIframe",[],function(){return {IframeUtil:TimIframeUtil,TimIframeUtilForLOP:TimIframeUtilForLOP}});


