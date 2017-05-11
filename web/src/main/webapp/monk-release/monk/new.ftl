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

    <link rel="stylesheet" href="/monk-release/resources/common/chat_init_new.css">
    <link rel="shortcut icon" href="/img/logo/im.ico">

</head>
<body class="center">
<#--==================js===============================    -->
<!-- IE8及以下支持JSON -->
<!-- WSDK-->
<#--<script src="https://g.alicdn.com/aliww/h5.imsdk/2.1.4/scripts/yw/wsdk.js" charset="utf-8"></script>-->

<#--jquery-->
<script src="/monk-release/resources/jquery.min.js" type="text/javascript"></script>

<script src="/monk-release/resources/chat/chat_init_new.js"></script>
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