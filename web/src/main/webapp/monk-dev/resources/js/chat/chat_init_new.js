var ChatUtil = function(){

    return{
        //淘汽车信的url地址
        init: function (header_url) {
            if(header_url == undefined || header_url == ""){
                console.log("header_url not be empty");
                return false;
            }
            //change title
            document.title='淘汽车信';
            var body_obj = $("body");

            //执行方法
            ConstantsMap.headerUrl = header_url;

            //渲染页面
            var html = HtmlUtil.section_html + HtmlUtil.modal_html+HtmlUtil.template_html;
            var veg = /ConstantsMap.headerUrl/g;
            html = html.replace(veg,ConstantsMap.headerUrl);
            body_obj.prepend(html);


            MMDChat.blockUI();
            //后台背景色
            backgroundReady();
            //页面样式初始化
            initCss();
            //校验访问这个页面的用户是否是允许的
            initCheck();
            //按钮初始化
            initClick();
            //window对象初始化
            windowChange();
        },
        //用户切换事件
        beforeChangePeople:function(do_fuc){
            if($.isFunction(do_fuc)){
                beforeChangePeople(do_fuc);
            }
        },
        //关闭整个聊天框
        closeChatWindow: function () {
            windowClose();
        }
    }
}();
