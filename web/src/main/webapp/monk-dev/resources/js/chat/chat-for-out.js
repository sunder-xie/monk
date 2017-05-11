/*
* 淘汽 自有im接入js
* by zxg 2016.04.12
*
* 我是后台开发，偶尔客串写前端，前端代码 不漂亮的地方，欢迎专业前端大神指出哈
* */
/*外部团队调用的方法*/
var TimChat = function(){

    //ali appkey
    var appKey;
    //当跟原始的未读消息不一致时，返回结果
    var old_message_num = 0;
    //新弹开的页面对象,已打开就去聚焦那个窗口
    var new_window =undefined;
    //获得未读消息去做的操作
    var get_un_read_do;
    //
    var get_result;
    //是否打开的唯一标识
    var pageKey = undefined;

    /*=====拦截页面 弹框====*/
    //拦截页面弹窗
    var hold_html ='<div id="all_chat_hold"><div class="chat_from_monk_modal-scrollable">'+
        '<div id="chat_from_monk_modal" class="monk_chat_modal modal fade modal-scroll" tabindex="-1" data-keyboard="false" aria-hidden="true" style="top:0px; margin-top: 0px; display: block;"> ' +
        ' <a href="#" class="model-close btn red" data-dismiss="modal" aria-hidden="true"> ' +
    ' <img src="MonkHeaderUrl/img/other/portlet-remove-icon-white.png"> ' +
    ' </a> ' +
    ' <div class="modal-section"> ' +
    ' <div class="modal-header" > ' +
    ' <h4 class="modal-title">聊天页面被拦截，需要手动调整，操作如下：</h4> ' +
    ' </div> ' +
    ' <div class="modal-body"> ' +
    ' <div class="cut_show" style="height:600px !important;"> ' +
    ' <div><span>第一步：找到网址输入框最右侧的拦截图标</span></div> ' +
    ' <div><img src="MonkHeaderUrl/img/hold/1-find.png"></div> ' +
    ' <div><span>第二步：点击拦截图标，选择始终允许</span></div> ' +
    ' <div><img src="MonkHeaderUrl/img/hold/3-choose.png"></div> ' +
    ' <div><span>第三步：点击聊天页面地址，打开聊天页面</span></div> ' +
    ' <div><img src="MonkHeaderUrl/img/hold/4-open.png"></div> ' +
    ' </div> ' +
    ' </div> ' +
    ' </div> ' + '</div> </div> <div class="modal-backdrop fade in"></div></div>';

    function alert_Hold_html(){
        if($("#chat_from_monk_modal").html() == undefined){
            $('body').append(hold_html.replace(/MonkHeaderUrl/g,ConstantsMap.headerUrl));
        }

        var chat_from_monk_modal = $("#chat_from_monk_modal");
        //判断top距离
        var document_height = document.documentElement.clientHeight;
        //if(chat_from_monk_modal.height()+10 > document_height){
        //    chat_from_monk_modal.css("top","50%");
        //}else{
        //    chat_from_monk_modal.css("top","0%");
        //}

        chat_from_monk_modal.addClass("in");


        var chat_forbid_obj = chat_from_monk_modal.parent().parent();

        chat_from_monk_modal.find(".modal-section").unbind("click").click(function(){
           return false;
        });
        chat_from_monk_modal.find(".model-close").unbind("click").click(function(){
            chat_forbid_obj.remove();
            chat_from_monk_modal.removeClass("in");
            return false;
        });
        chat_from_monk_modal.parent().unbind("click").click(function(){
            chat_forbid_obj.remove();
            chat_from_monk_modal.removeClass("in");
            return false;
        });
    }

    /*====end hold=====*/
    /*====start 云旺========*/
    var sdk = new WSDK();

    var the_uid,the_password;
    var the_successFunc;

    function initAliYunWang(uid,password,successFunc){
        if(uid != undefined){
            the_uid = uid
        }
        if(password != undefined){
            the_password = password
        }
        sdk.Base.login({
            uid:the_uid,
            appkey: appKey,
            credential: the_password,
            timeout: 24*60*60*1000,
            success: function(data){
                if(successFunc != undefined){
                    successFunc();
                }
            },
            error: function(error){
                //清除缓存，重新塞，防止一直登录上
                //var pageValue = undefined;
                //if(pageKey != undefined){
                //    pageValue = ConstantsMap.storage.getItem(pageKey);
                //}
                //if(pageValue != undefined){
                //    ConstantsMap.storage.setItem(pageKey,pageValue);
                //}
            }
        });
    }
    function getUnreadMsgCountInYunWang(successFunc){
        if(successFunc != undefined){
            the_successFunc = successFunc;
        }
        sdk.Base.getUnreadMsgCount({
            count: 10,
            success: function(data){
                if(successFunc != undefined){
                    successFunc(data);
                }
            },
            error: function(error){
                if (Number(error['code']) == 1001) {
                    //重新发送数据
                    initAliYunWang(undefined,undefined,getUnreadMsgCountInYunWang);
                }
            }
        });
    }
    /*====end 云旺========*/

    //检查必要参数是否有传
    function check_param(param){
        //判断系统是否正常
        var sys_id = param['sys_id'];
        var sys_name = param['sys_name'];
        var time = param['time'];
        var token = param['token'];
        if(sys_id == undefined || sys_name == undefined || time == undefined || token == undefined
           || sys_id == "" || sys_name == "" || time == "" || token == ""){
            console.log("缺少必要参数");
            alert("初始化 淘汽云聊失败，缺少必要参数");
            return false;
        }
        return true;
    }

    //未读消息数获得的总的任务
    function getUnReadTask(result,doFunc){
        if(result != undefined){
            get_result = result;
        }
        if(get_result != undefined) {
            var userDO = get_result.data;
            var uid = userDO['aliUid'];
            var password = userDO['aliPassword'];
            appKey = userDO['appKey'];

            get_un_read_do = doFunc;
            //获得未读消息总数
            if(pageKey == undefined){
                pageKey = StorageKey.imPageOpened+userDO['sysId']+userDO['sysName'];
            }
            var pageValue = ConstantsMap.storage.getItem(pageKey);
            if (pageValue == undefined) {
                getUnreadMsgCountFuncByOut(uid, password);
            }else{
                //已打开，则从浏览器缓存中获得未读消息数
                var un_read_sum = ConstantsMap.storage.getItem(pageKey+StorageKey.unReadMsgSum);
                getUnreadMsgCountFromStory(un_read_sum);
            }
            //每分钟进行一次
            var q = window.setInterval(function () {
                var pageValue = ConstantsMap.storage.getItem(pageKey);
                if (pageValue == undefined) {
                    //获得未读消息总数
                    getUnreadMsgCountFuncByOut(uid, password);
                } else {
                    window.clearInterval(q);
                }
            }, 20000);
        }
    }


    var is_first_ali;
    //聊天框没打开时，获得未读消息总数
    function getUnreadMsgCountFuncByOut(uid,password){
        if(is_first_ali == undefined){
            //第一次 进行 登录初始化
            is_first_ali = 1;
            initAliYunWang(uid,password,help_get_out_sum);
        }else{
            help_get_out_sum();
        }
    }

    function help_get_out_sum(){
        getUnreadMsgCountInYunWang(function(Object){
            var data = Object['data'];
            var sum = 0;
            $.each(data,function(i,the_ob){
                sum += the_ob['msgCount'];
            });
            if(sum != old_message_num){
                old_message_num = sum;
                if(get_un_read_do != undefined && Number(sum) != 0){
                    get_un_read_do(sum,false);
                }
            }
        });
    }
    //聊天框打开了，缓存中获得数据
    function getUnreadMsgCountFromStory(un_read_sum){

        if(un_read_sum != undefined){
            if (un_read_sum != old_message_num) {
                if (get_un_read_do != undefined) {
                    old_message_num = un_read_sum;
                    get_un_read_do(un_read_sum,true);
                }
            }
        }
    }

    //监听事件
    var is_closed = true;
    function StoryListener(){
        var sto_func = function(e){
            var storage_key = e.key;
            var new_value = e.newValue;

            if(pageKey == undefined){
                return false;
            }
            if(storage_key == pageKey){
                //若值为 undefined 则已关闭，进入原始轮询登录
                if(new_value == undefined){
                    is_closed = true;
                    setTimeout(function() {
                        //确实关闭了,重新进入轮询获得未读消息数
                        if(is_closed){
                            ConstantsMap.storage.clear();
                            getUnReadTask(get_result, get_un_read_do);
                        }
                    },3000)
                }else{
                    is_closed = false;
                }
            }

            //未读消息
            if(storage_key == pageKey+StorageKey.unReadMsgSum){
                getUnreadMsgCountFromStory(new_value);
            }

        };
        window.removeEventListener("storage",sto_func);
        window.addEventListener("storage",sto_func );

    }
    //发消息让弹出框居中
    function focusChatWindow(){
        if(new_window != undefined){
            new_window.focus();
        }else{
            if(pageKey != undefined){
                ConstantsMap.storage.setItem(pageKey+StorageKey.isFocus,"1");
            }
        }
        //SocketClient.sendMessage({source:SocketMap.SOURCE_OUT,isFocus:SocketMap.IS_YES});
    }
    //发消息让弹出框关闭
    function closeChatWindow(){
        ConstantsMap.storage.setItem(pageKey+StorageKey.isClosed,"1");
        //SocketClient.sendMessage({source:SocketMap.SOURCE_OUT,isClosed:SocketMap.IS_YES});
    }
    //发消息给弹出框--要跟uid对象聊天
    function chatWithUidOnChat(to_sys_id,aliUid,topUrl){
        if(topUrl != ""){
            ConstantsMap.storage.setItem(pageKey+StorageKey.topUrl,topUrl);
        }
        ConstantsMap.storage.setItem(pageKey+StorageKey.chatWithUid,aliUid+","+to_sys_id);
        //SocketClient.sendMessage({source:SocketMap.SOURCE_OUT,chatWithUid:aliUid,topUrl:topUrl});
    }

    return{
        //初始化弹窗,若已存在，则提示已打开，自动聚焦--可自定义existFunc
        initOpenChatPage:function(param,exist_func){
            var heardUrl = param['header_url'];
            if(heardUrl == undefined){
                console.log("必须传接入的地址前缀");
                return false;
            }
            ConstantsMap.headerUrl = heardUrl;
            if(check_param(param)) {
                var sys_id = param['sys_id'];
                var sys_name = param['sys_name'];
                pageKey = StorageKey.imPageOpened+sys_id+sys_name;
                var pageValue =  ConstantsMap.storage.getItem(pageKey);
                if(pageValue == undefined) {
                    //初始化socket
                    if(sys_id !=undefined && sys_name !=undefined) {
                        ImConstants.getUsersData(sys_id,sys_name,function(result){
                            var data = result.data;
                            appKey = data['appKey'];
                            //未打开，则打开聊天框
                            var url = param['opened_url'];
                            ConstantsMap.storage.setItem(StorageKey.initParamKey, JSON.stringify(param));

                            //弹框
                            new_window = window.open(url, "monk-chat", 'width='+(window.screen.availWidth-30)+',height='+(window.screen.availHeight-30)+ ',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');

                            //监听local story事件
                            StoryListener();
                            //判断是否被组织弹窗
                            var refuse_key = pageKey+"refuse";
                            ConstantsMap.storage.setItem(refuse_key,"0");
                            setTimeout(function(){
                                //若两秒后无弹出来，给提示
                                var refuse_result = ConstantsMap.storage.getItem(refuse_key);
                                if(refuse_result != undefined && Number(ConstantsMap.storage.getItem(refuse_key)) == 0 ){
                                    alert_Hold_html();
                                }
                            },3000);
                        });
                    }
                }else{
                    /*再次点击*/
                    focusChatWindow();
                    //如果传要聊天对象，发消息给聊天框要聊天了
                    var to_sys_id = param['to_sys_id'];
                    var to_sys_name = param['to_sys_name'];
                    if(to_sys_id !=undefined && to_sys_name !=undefined) {
                        ImConstants.getUsersData(to_sys_id,to_sys_name,function(result){
                            var data = result.data;
                            chatWithUidOnChat(to_sys_id,data['aliUid'],param['top_url']);
                        });
                    }
                    //已打开，则提示
                    if(exist_func == undefined){
                        exist_func = function(){console.log("当前用户的聊天页面已打开~~~请直接到聊天页面查看");}
                    }
                    exist_func();
                }
            }

        },
        //初始化获得未读消息到来源项目中，并进行获得数据后的后续处理----可自定义doFunc,出参数:未读消息总数,若无
        initGetNewMessage:function(param,exist_func){
            var heardUrl = param['header_url'];
            if(heardUrl == undefined){
                console.log("必须传接入的地址前缀");
                return false;
            }
            ConstantsMap.headerUrl = heardUrl;

            var base_func = function(sum){
                NotifyUtil.sendNotify("尊敬的客户，你好","您有"+sum+"条未读消息，请及时打开右侧淘汽车信入口进行查看");
            };
            if(exist_func == undefined){
                exist_func = base_func
            }

            if(check_param(param)) {
                //一次性验证是否是系统调用，通过后即开始执行定时器
                ImConstants.checkIsTrue(param,function(result){
                    getUnReadTask(result,exist_func);
                });

            }

        },
        //关闭聊天窗口
        closeChatWindow:function(sys_id,sys_name){
            var chat_object_html = $(window.parent.document).find(".main_chat_section").html();
            if(chat_object_html == undefined){
                if(sys_id != undefined && sys_name != undefined){
                    pageKey = StorageKey.imPageOpened+sys_id+sys_name;
                }
                closeChatWindow();
            }else{
                $(window.parent.document).close();
            }
        },
        //聚焦---慎重，会让聊天窗弹出一个alert框来聚焦
        focusChatWindow:function(){
            focusChatWindow();
        },
        //桌面消息通知
        notify: function (title, content) {
            NotifyUtil.sendNotify(title,content);
        }

    }
}();



"function"==typeof define&&define("TqmallIm",[],function(){return {Chat:TimChat}});


