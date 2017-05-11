<#escape x as x?html>
<!DOCTYPE html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>淘汽车信</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>

    <link rel="stylesheet" href="/monk-release/resources/common/commonCss-b4fddba27c.css">

</head>
    <input type="hidden" class="init_str" id="sysid" value="${chatVO.sysid}">
    <input type="hidden" class="init_str" id="sysname" value="${chatVO.sysname}">
    <input type="hidden" class="init_str" id="time" value="${chatVO.time}">
    <input type="hidden" class="init_str" id="token" value="${chatVO.token}">
    <input type="hidden" class="init_str" id="tosysid" value="${chatVO.tosysid}">
    <input type="hidden" class="init_str" id="tosysname" value="${chatVO.tosysname}">
    <input type="hidden" class="init_str" id="top_url" value="${chatVO.top_url}">
    <input type="hidden" class="init_str" id="bottom_url_btn_name" value="${chatVO.bottom_url_btn_name}">
    <input type="hidden" class="init_str" id="bottom_url" value="${chatVO.bottom_url}">

    <#--<input type="hidden" id="param" value="12">-->
    <body class="center">
        <section class="main_chat_section">
            <#--联系人列表-->
           <div class="chat_people">
               <#--头-->
               <div class="header">
                   <i aria-hidden="true" class="icon-users"></i>
                   <span >联系人列表</span>
               </div>
                   <div class="search_div">
                       <i class="fa fa-search"></i>
                       <i class="right fa fa-times-circle-o hidden" id="clear_search_people_input"></i>
                       <input type="text" id="search_people" placeholder="请输入要查找的用户名">
                   </div>
               <#--实际联系人，过多，则可滚动-->
               <ul class="nav people_ul">

               </ul>
           </div>
            <#--欢迎界面-->
            <div class="welcome_content">
                <div class="main_wel">
                    <div>
                        <img src="/img/logo/im_512_512.png"/>
                        <span>欢迎使用 淘汽车信</span>
                    </div>


                </div>
                <div class="help">
                    <i class="fa fa-coffee"></i>
                    <span>工作时间，一杯咖啡能让你精神抖擞</span>
                </div>
            </div>
            <#--具体聊天框-->
            <div class="chat_content hidden">
                <#--跟谁聊天-->
                <div class="header">
                    <span id="chat_content_header"></span>
                </div>
                <#--聊天内容-->
                <div class="chat_main">

                    <div id="loading"></div>
                    <div id="chat_main_div">
                    <#--顶部拓展区--发送些必要信息-->

                    </div>
                </div>
                <#--聊天的输入框-->
                <div class="chat_input">
                    <div class="chat_input_div">
                        <#--工具-->
                        <div class="chat_input_util">
                            <span>
                                <i class="fa fa-cut" id="cut_util"></i>
                            </span>
                            <span class="file_input">
                                <i aria-hidden="true" class="icon-picture"></i>

                                <input type="file" name="pic_util" id="pic_util" multiple="">
                            </span>
                        </div>
                        <textarea id="new_message_area" placeholder="换行按Ctrl+Enter,发送消息按Enter,请输入..."></textarea>
                    </div>

                    <button class="btn btn-blue" id="send_btn">
                        <span>发送</span>
                    </button>
                </div>


                <#--底部扩展框--按钮-->
                <div id="expand_bottom" class="expand_bottom">
                    <button class="btn expand-blue expand_btn">
                        <span class="expand_bottom_name">拓展</span>
                        <i class="fa fa-angle-up"></i>
                    </button>
                </div>
                <div id="expand_bottom_close" class="expand_bottom expand_bottom_close hidden">
                    <button class="btn  expand_btn_close">
                        <i class="fa fa-angle-down"></i>
                        <span class="expand_bottom_name">拓展</span>
                    </button>
                </div>
                <#--底部扩展框--内容-->
                <div style="display: none;" id="expand_main">
                    <iframe width="100%" style="" frameborder="no" scrolling=auto border=0 src="" id="expand_bottom_iframe" name="expand_bottom_iframe"></iframe>
                </div>
            </div>
        </section>

        <#--<img src="sss" id="test_img">-->
    </body>



    <#--==================modal===============================    -->
    <#--确定取消的_Model-->
    <div id="alert_model" class="modal fade modal-scroll" tabindex="-1" data-keyboard="false" aria-hidden="true">
        <a href="#" class="model-close btn red" data-dismiss="modal" aria-hidden="true">
            <i class="fa fa-times"></i>
        </a>

        <div class="modal-section">
            <div class="modal-header" >
                <h4 class="modal-title">发送图片</h4>
            </div>
            <div class="modal-body">
                <img class="img-circle" src="">
            </div>
            <div class="modal-footer" style="overflow-x: hidden;">
                <button class="btn">发送</button>
            </div>
        </div>

    </div>

    <#--pic-->
    <div id="pic_model" class="modal fade modal-scroll modal_img" tabindex="-1" data-keyboard="false" aria-hidden="true">
        <a href="#" class="model-close btn red" data-dismiss="modal" aria-hidden="true">
            <i class="fa fa-times"></i>
        <#--<span aria-hidden="true" class="icon-close"></span>-->
        </a>
        <div class="modal-section">
            <div class="modal-body">
                <img id="model_img" src="">
            </div>
        </div>
        <div class="modal-footer" id="img_foot" style="overflow-x: hidden;">
            <button class="btn" >发送</button>
        </div>

    </div>

    <button class="hidden" id="alert_model_show_btn"> </button>
    <button class="hidden" id="pic_model_show_btn"> </button>
    <button class="hidden" id="alert_model_hide_btn"> </button>
    <button class="hidden" id="pic_model_hide_btn"> </button>
    <#--template-->

    <#include "/monk-release/monk/chat-template.ftl"/>
    <#--==================js===============================    -->
    <!-- IE8及以下支持JSON -->
    <!--[if lt IE 9]>
    <script src="https://g.alicdn.com/aliww/ww/json/json.js" charset="utf-8"></script>
    <![endif]-->
    <!-- WSDK-->
<script src="https://g.alicdn.com/aliww/h5.imsdk/2.1.1/scripts/yw/wsdk.js" charset="utf-8"></script>

    <!--你的js-->
    <#--jquery-->
    <script src="/monk-release/resources/jquery.min.js" type="text/javascript"></script>
    <#--模板-->
    <script src="/monk-release/resources/common/js/template.js" type="text/javascript"></script>
    <#--弹出框-->
    <script src="/monk-release/resources/common/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
    <script src="/monk-release/resources/common/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript" ></script>


    <script src="/monk-release/resources/common/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
    <script src="/monk-release/resources/common/block/jquery.blockui.min.js" type="text/javascript"></script>
    <script src="/monk-release/resources/common/chat.js" type="text/javascript"></script>
    <script src="/monk-release/resources/js/constantsJS/constants.js" type="text/javascript"></script>

    <#--socket-->
    <#--<script src="/monk-release/resources/js/socket/socket.io.js" type="text/javascript"></script>-->
    <#--<script src="/monk-release/resources/js/socket/socket.js" type="text/javascript"></script>-->
    <script src="/monk-release/resources/js/notify/notify.js" type="text/javascript"></script>
    <script src="/monk-release/resources/js/time/time.js" type="text/javascript"></script>

    <script src="/monk-release/resources/js/chat/chat-show.js" type="text/javascript"></script>

    <script>

        $(function () {
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
        });
    </script>

</html>
</#escape>