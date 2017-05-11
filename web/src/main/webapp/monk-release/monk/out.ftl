<!doctype html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>外部系统接入im测试页面</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <link rel="stylesheet" href="/monk-release/resources/chat/chat-min.css">

</head>
<body style="min-height:700px">

<input type="text" id="ucid" placeholder="id" value="37897">
<input type="text" id="sysname" placeholder="系统名" value="uc">

<input type="text" id="toucid" placeholder="to  id,选填" value="10029">
<input type="text" id="tosysname" placeholder="to系统名 选填" value="lop">

<input type="text" id="header_url" placeholder="header_url" value="http://127.0.0.1:9866/">

<input type="text" id="top_url" placeholder="top_url" value="http://www.tqmall.com/">
<input type="text" id="bottom_name" placeholder="bottom_name" value="需求单">
<input type="text" id="bottom_url" placeholder="bottom_url" value="http://www.tqmall.com?test=chat_sysid&test2=chat_tosysyid">
<button id="out_chat_btn">弹出聊天框</button>
<button id="notReadMessage">未读消息</button>
未读消息:
<span id="notReadMessage_span"></span>

<button id="close_by_socket">消息-关闭chat</button>
<button id="focus_by_socket">消息-聚焦chat</button>


<span id="test_data" data-test="1">asdsasdas</span>

<button id="modal_show">modal_show</button>


</body>




<script src="/monk-release/resources/jquery.min.js"></script>
<!-- IE8及以下支持JSON -->
<!--[if lt IE 9]>
<script src="https://g.alicdn.com/aliww/ww/json/json.js" charset="utf-8"></script>
<![endif]-->
<!-- WSDK-->
<#--<script src="https://g.alicdn.com/aliww/h5.imsdk/2.1.4/scripts/yw/wsdk.js" charset="utf-8"></script>-->

<!--公用的接口提供-->



<script src="/monk-release/resources/chat/chat-min.js"></script>
<script src="/monk-release/resources/chat/chat-iframe.js"></script>


<script>

    $("#test_data").click(function () {
        var num = Number($("#test_data").data("test"));
        num++;
        $("#test_data").data("test",num);
//        var test_data = document.querySelector('#test_data');
//        test_data.dataset.test = num;
    });
    $("#modal_show").click(function () {
        $("#chat_modal").modal('show')
    });

    $("#out_chat_btn").click(function () {
        //destroy_login()
        $.ajax({
            url: "/monk/test/getTime",
            async: false,
            data: {sysName:$('#sysname').val()},
            success: function(reslut) {
                var map = reslut.data;
                var testArray = {'sys_id':$("#ucid").val(),'sys_name':$('#sysname').val(),'to_sys_id':$("#toucid").val(),'to_sys_name':$('#tosysname').val(),'time':map['time'],'token':map['token']};
//                testArray['top_url']=$("#top_url").val().trim();
//                testArray['bottom_url_btn_name'] = $("#bottom_name").val().trim();
//                testArray['bottom_url']=$("#bottom_url").val().trim();

                testArray['header_url'] = $("#header_url").val();
                testArray['opened_url'] = "http://localhost:9866/monk/test/new_page";

//                testArray['guide_content'] = "666";
//                testArray['guide_url'] = "http://www.tqmall.com/";



                console.log(testArray);

                var exist_func = function () {
                    console.log("hhh");
                };
                TimChat.initOpenChatPage(testArray,exist_func);

            }
        });
//                $.get("/monk/test/getTime",{sysName:'uc'},function(reslut){
//                })

    });


    $("#notReadMessage").click(function(){
        //destroy_login()
        $.get("/monk/test/getTime",{sysName:$('#sysname').val()},function(reslut){
            var map = reslut.data;
            var testArray = {'sys_id':$("#ucid").val(),'sys_name':$('#sysname').val(),'to_sys_id':$("#toucid").val(),'to_sys_name':$('#tosysname').val(),'time':map['time'],'token':map['token']};

            testArray['header_url'] = $("#header_url").val();
            TimChat.initGetNewMessage(testArray,function(num){
                console.log("当前未读消息:"+num);
                $("#notReadMessage_span").text(num);
            });
        })
    });


    $("#close_by_socket").click(function(){
        TimChat.closeChatWindow();
    });

    $("#focus_by_socket").click(function(){
        TimChat.focusChatWindow();
    });


</script>



</html>