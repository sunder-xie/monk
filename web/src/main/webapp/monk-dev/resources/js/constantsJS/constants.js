var ConstantsMap = {
    headerUrl:"",
    //全局h5 session缓存
    storage: window.localStorage,
    wsdkUrl:"https://g.alicdn.com/aliww/h5.imsdk/2.1.4/scripts/yw/wsdk.js",
};

var MessageType = {
    text:0,
    img:1,
    voice:2
};

var StorageKey = {
    //是否已打开聊天框:imPage+sysid+sysname. opened:已打开，undefined:未打开
    imPageOpened:'imPage',
    //登录时传递的参数key，打开后可删除了
    initParamKey: 'initParamKey',
    //pageKey+未读消息数（即 imPage+sysid+sysname+unReadMsgSum）
    unReadMsgSum:'unReadMsgSum',
    //是否关闭，规则同上
    isClosed:'isClosed',
    //是否聚焦，规则同上
    isFocus:'isFocus',
    //跟哪个uid聊天，规则同上
    chatWithUid:'chatWithUid',
    //顶部内嵌框，规则同上
    topUrl:'topUrl'
};

/*im 对外对内都有用的接口*/
var ImConstants = function(){
    /*===START 动态include js＝＝＝＝＝＝*/
    function includeJS(sId,source_text)
    {
        if ( ( source_text != null ) && ( !document.getElementById( sId ) ) ){
            var oHead = document.getElementsByTagName('HEAD').item(0);
            var oScript = document.createElement( "script" );
            oScript.type = "text/javascript";
            oScript.id = sId;
            oScript.text = source_text;

            var scriptObj = oHead.getElementsByTagName('script');
            if(scriptObj.length > 0){
                oHead.insertBefore( oScript,scriptObj[0] );
            }else{
                oHead.appendChild(oScript);
            }
        }
    }

    function ajaxIncludeJsPage(sId, url){
        $.ajax({
            type:"GET",
            url:url,
            async: false,
            success:function(data){
                includeJS(sId,data);
            }
        });
    }
    /*===end 动态include js＝＝＝＝＝＝*/


    return{
        //判断是否是正确的接入im:param['']--sysId,sysName,time,token
        checkIsTrue:function(param,successFunc,failFunc){
            var sysId = param['sys_id'];
            var sysName = param['sys_name'];
            var time = param['time'];
            var token = param['token'];
            if(sysId == undefined || sysName == undefined || time == undefined || token == undefined
                || sysId == "" || sysName == "" || time == "" || token == ""){
                console.log("缺少必要参数");
                return false;
            }
            $.get(ConstantsMap.headerUrl+"/monk/chat/checkApp",{sysId:sysId,sysName:sysName,time:time,token:token},function(result){
                if(result.success){
                    if(successFunc != undefined){
                        successFunc(result);
                    }
                }else{
                    if(failFunc!= undefined){
                        failFunc(result);
                    }
                }
            });
        },
        //根据sysid和sysname获得用户信息
        getUsersData:function(sysId,sysName,successFunc,failFunc){
            $.get(ConstantsMap.headerUrl+"/monk/chat/getUser",{sysId:sysId,sysName:sysName},function(result){
                if(result.success){
                    if(successFunc != undefined){
                        successFunc(result);
                    }
                }else{
                    if(failFunc!= undefined){
                        failFunc(result);
                    }
                }
            });
        },
        //include js
        includeJs:function(sId, url){
            ajaxIncludeJsPage(sId,url);
        }
    }
}();


