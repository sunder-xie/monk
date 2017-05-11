/**
 * socket 连接公用js by zxg 2016.04.16
 * @type {undefined}
 */


//变量
var SocketMap = {
    SOURCE_OUT: '1',
    SOURCE_CHAT: '2',
    IS_YES: 'yes'
};


var SocketClient = function () {

    //socket 客户端
    var socket = undefined;
    //服务器上获得的数据
    var result_data = undefined;

    function init(){
        if (result_data == undefined) {
            //同步获得数据
            $.ajax({
                url: ConstantsMap.headerUrl + "/monk/socket/getSocketProperties",
                async: false,
                success: function(resultVO){
                    result_data = resultVO.data;
                }
            });
        }
        //建立连接
        var url = result_data['jsSocketUrl'];

        if(socket == undefined){
            socket = io.connect(url);
        }else{
            socket = io.connect(url,{'force new connection':true});
        }
    }

    function getMessage(doFunc){
        socket.on(result_data['EVENT_NAME_GET'], function (data) {
            doFunc(data);
        });
    }

    function sendMessage(jsonObject){
        socket.emit(result_data['EVENT_NAME_SEND'], jsonObject);
    }

    function disconnect(){
        if (socket != undefined) {
            socket.disconnect();
        }
    }

    return {
        //初始化
        init:function(){
            //初始化之前，默认断开连接
            //disconnect();
            //执行初始化
            init();
        },
        //接受数据
        getMessage: function (doFunc) {
            //连接socket服务器
            if (socket == undefined) {
                init();
            }
            getMessage(doFunc);


        },
        //发送数据:params---source, aliUid, unReadMsgSum, isClosed, toAliUid,isFocus,chatWithUid，chatWithUName,isInit,topUrl
        sendMessage: function (params) {
            var source = params['source'];
            if (source != undefined) {
                var jsonObject = {source: source};
                if (params['isInit'] != undefined) {
                    jsonObject["isInit"] = params['isInit'];
                }
                if (params['aliUid'] != undefined) {
                    jsonObject["aliUid"] = params['aliUid'];
                }
                if (params['unReadMsgSum'] != undefined) {
                    jsonObject["unReadMsgSum"] = params['unReadMsgSum'];
                }
                if (params['isClosed'] != undefined) {
                    jsonObject["isClosed"] = params['isClosed'];
                }
                if (params['toAliUid'] != undefined) {
                    jsonObject["toAliUid"] = params['toAliUid'];
                }
                if (params['isFocus'] != undefined) {
                    jsonObject["isFocus"] = params['isFocus'];
                }
                if (params['chatWithUid'] != undefined) {
                    jsonObject["chatWithUid"] = params['chatWithUid'];
                }
                if (params['chatWithUName'] != undefined) {
                    jsonObject["chatWithUName"] = params['chatWithUName'];
                }
                if (params['topUrl'] != undefined) {
                    jsonObject["topUrl"] = params['topUrl'];
                }
                //连接socket服务器
                if (socket == undefined) {
                    init();
                }
                sendMessage(jsonObject);

            }


        },
        //断开连接
        disConnect: function () {
           disconnect();
        }
    }
}();