<!doctype html>
<html>
<head>
    <link href="/monk-dev/resources/common/icon/simple-line-icons.min.css" rel="stylesheet" type="text/css">
    <link href="/monk-dev/resources/common/chat-demo.css" rel="stylesheet" type="text/css">
</head>

<body>

<section id="tqmall-chat-section">

    <#--<div class="col col-left">-->
    <#--&lt;#&ndash;头像&ndash;&gt;-->
        <#--<div class="center">-->
            <#--<img class="img-circle" src="http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg">-->
        <#--</div>-->
        <#--<div class="center">-->
            <#--<span class="username headername" id="chat_main_user_name">-->
                <#--中西-->
            <#--</span>-->
        <#--</div>-->

    <#--&lt;#&ndash;今天聊天和明天聊天的tab&ndash;&gt;-->
        <#--<div class="center">-->
            <#--<ul class="nav chat_tab">-->
                <#--<li class="active">-->
                    <#--<span aria-hidden="true" class="icon-bubble"></span>-->
                <#--</li>-->
                <#--<li>-->
                    <#--<span aria-hidden="true" class="icon-users"></span>-->
                <#--</li>-->
            <#--</ul>-->
        <#--</div>-->
    <#--</div>-->

    <div class="col">
    <#--今天联系-->
        <div class="row">
            <div class="col col_chat_list back_grey">
                <ul class="nav chat_people">
                    <li class="active">
                        <span>
                            <img class="img-circle" src="http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=23&gp=0.jpg">
                            <span class="badge badge-danger"></span>
                        </span>
                        <span class="username">
                            老王
                        </span>
                        <span class="final_time">10:00</span>
                    </li>
                    <li>
                        <span>
                            <img class="img-circle" src="http://img3.imgtn.bdimg.com/it/u=3399543288,3713437897&fm=23&gp=0.jpg">
                            <span class="badge badge-danger">3 </span>
                        </span>
                        <span class="username" >
                            老李
                        </span>
                        <span class="final_time">17:20</span>
                    </li>
                </ul>
            </div>
            <div class="col col_chat_content back_white">
                <div class="chat_heard">
                    <img class="img-circle" src="http://img3.imgtn.bdimg.com/it/u=3399543288,3713437897&fm=23&gp=0.jpg">
                    <span class="username" >
                            老李
                    </span>

                    <#--<div class="chat_tools">-->
                        <#--<a class="chat_close"></a>-->
                    <#--</div>-->
                </div>
                <#--聊天的内容框-->
                <div class="chat_content">
                    <#--他人的聊天内容-->
                    <div class="chat_msg_wrap chat_l">
                        <img class="img-circle" src="http://img3.imgtn.bdimg.com/it/u=3399543288,3713437897&fm=23&gp=0.jpg">
                        <div class="chat_msg">
                            <div class="chat-msg-time">03-29 10:12:37</div>
                            <div class="chat-msg-inner">
                                <i class="chat-arr"></i>
                                <div class="chat-msg-item">我是来看看样式的</div>
                            </div>
                        </div>
                    </div>
                    <div class="chat_msg_wrap chat_r">
                        <img class="img-circle" src="http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=23&gp=0.jpg">
                        <div class="chat_msg">
                            <div class="chat-msg-time">03-29 10:12:37</div>
                            <div class="chat-msg-inner">
                                <i class="chat-arr"></i>
                                <div class="chat-msg-item">test，看到如何了？</div>
                            </div>
                        </div>
                    </div>

                </div>
                <#--聊天的输入框-->
                <div class="chat_input_div">
                    <#--工具-->
                    <div class="chat_input_util">
                        <span aria-hidden="true" class="icon-emoticon-smile"></span>
                        <span aria-hidden="true" class="icon-picture"></span>
                    </div>
                    <textarea id="txta" placeholder="请输入聊天内容，回车即可发送~~"></textarea>
                    <button id="test" >test</button>
                </div>
            </div>
            <div class="col col_ext back_white">123</div>
            <pre id="pre">hhhh</pre>
        </div>

    </div>

</section>
</body>
<script src="/monk-dev/resources/jquery.min.js" type="text/javascript"></script>

<script>
    $('#test').click(function(){
        var value = $('#txta').text();
        var value2 = $('#txta').html();
        var value3 = $('#txta').val();
        console.log(value)
        $("#pre").html(value3);
    })
</script>
</html>