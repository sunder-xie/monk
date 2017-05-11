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
    <title>111</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>

    <!-- build:css ../resources/common/chat_init_new.css -->
    <#--弹出框-->
    <link href="/monk-dev/resources/common/bootstrap-modal/css/modal.css" rel="stylesheet" type="text/css"/>
    <link href="/monk-dev/resources/common/icon/simple-line-icons.min.css" rel="stylesheet" type="text/css">
    <link href="/monk-dev/resources/common/icon/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="/monk-dev/resources/common/chat-common.css" rel="stylesheet" type="text/css">
    <link href="/monk-dev/resources/common/chat.css" rel="stylesheet" type="text/css">
    <!-- endbuild -->
    <link rel="shortcut icon" href="/img/logo/im.ico">

</head>
<body class="center">
<#--==================js===============================    -->
<!-- IE8及以下支持JSON -->
<!-- WSDK-->
<#--<script src="https://g.alicdn.com/aliww/h5.imsdk/2.1.4/scripts/yw/wsdk.js" charset="utf-8"></script>-->

<#--jquery-->
<script src="/monk-dev/resources/jquery.min.js" type="text/javascript"></script>

<!-- build:js ../resources/chat/chat_init_new.js -->

<!--你的js-->
<#--模板-->
<script src="/monk-dev/resources/common/js/template.js" type="text/javascript"></script>
<#--弹出框-->
<script src="/monk-dev/resources/common/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript" ></script>
<script src="/monk-dev/resources/common/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript" ></script>


<script src="/monk-dev/resources/common/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
<script src="/monk-dev/resources/common/block/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/monk-dev/resources/common/chat.js" type="text/javascript"></script>
<script src="/monk-dev/resources/js/constantsJS/constants.js" type="text/javascript"></script>
<#--引入wsdk-->
<script src="/monk-dev/resources/js/wsdk/wsdk.js" type="text/javascript"></script>
<script src="/monk-dev/resources/js/notify/notify.js" type="text/javascript"></script>
<script src="/monk-dev/resources/js/time/time.js" type="text/javascript"></script>

<script src="/monk-dev/resources/js/chat/chat-show.js" type="text/javascript"></script>

<script src="/monk-dev/resources/js/chat/chat_init_html.js" type="text/javascript"></script>
<script src="/monk-dev/resources/js/chat/chat_init_new.js" type="text/javascript"></script>

<!-- endbuild -->
<script>
    var chat_url = 'http://' + window.location.host;
//    var chat_url = "http://120.26.105.99:19101/";
//    var chat_url = "http://im.epei360.com/";

ChatUtil.beforeChangePeople(function(){
    console.log("change people");
    return true;
});

    ChatUtil.init(chat_url);
</script>

</html>
</#escape>