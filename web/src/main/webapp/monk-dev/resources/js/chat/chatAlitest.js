/*
* 淘汽 自有im接入js
* by zxg 2016.04.01
*
* 愚人节快乐，我是后台开发，偶尔客串写前端，前端代码 不漂亮的地方，欢迎专业前端大神指出哈
* */
//var Header_url = "http://localhost:9866/";
var headerUrl = 'http://'+window.location.host;
var sdk = new WSDK();


var nextkey='';
$(function () {

    $('#start_chart_btn').click(function () {
        console.log($('#uuid').val());
        init_login()
    });

    $("#stop_chart_btn").click(function () {
        //destroy_login()

    });

    $("#history").click(function () {
        //destroy_login()
        get_history($('#touuid').val());
    });



});

function init_login(){
    var uid = $('#uuid').val();

    //登录
    sdk.Base.login({
        uid:uid,
        appkey: '23332316',
        credential: '123',
        timeout: 1000000,
        success: function(data){
            // {code: 1000, resultText: 'SUCCESS'}
            console.log('login success', data);
            base_func();
        },
        error: function(error){
            // {code: 1002, resultText: 'TIMEOUT'}
            console.log('login fail', error);
        }
    });
}


//基础功能
function base_func(){
    var touid = $('#touuid').val();
    //未读消息
    sdk.Base.getUnreadMsgCount({
        count: 10,
        success: function(data){
            console.log('get unread msg count success' ,data);
        },
        error: function(error){
            console.log('get unread msg count fail' ,error);
        }
    });

    //最近联系人
    sdk.Base.getRecentContact({
        count: 10,
        success: function(data){
            console.log('get recent contact success' ,data);
        },
        error: function(error){
            console.log('get recent contact success' ,error);
        }
    });
    //监听所有的聊天内容
    sdk.Event.on('MSG_RECEIVED', function(data){
        console.log("当前聊天对象的内容：");
        console.log(data);
    });

    //特定当前用户
    //sdk.Chat.startListenMsg({touid: touid});
    //全部用户
    sdk.Base.startListenAllMsg();


    //漫游历史消息
    get_history(touid);

    $("#chat_content").unbind("keypress").bind('keypress', function (event) {
        if (event.keyCode == "13") {
            send_message();
        }
    });

}


//发消息
function send_message(){
    sdk.Chat.sendMsg({
        touid: $('#touuid').val(),
        msg: $('#chat_content').val(),
        success: function(data){
            console.log('send success', data);
            $('#chat_content').val('');
        },
        error: function(error){
            console.log('send fail', error);
        }
    });
}

function get_history(touid){
    //漫游历史消息
    sdk.Chat.getHistory({
        touid: touid,
        nextkey: nextkey,
        count: 2,
        success: function(data){
            console.log('get history msg success', data);
            nextkey = data.data['nextKey'];
            console.log('get history msg nextKEY', nextkey);
        },
        error: function(error){
            console.log('get history msg fail', error);
        }
    });
}