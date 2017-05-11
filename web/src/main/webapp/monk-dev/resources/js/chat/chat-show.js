/*
 * 淘汽 chat 页面的渲染js
 * by zxg 2016.04.12
 *
 * 愚人节快乐，我是后台开发，偶尔客串写前端，前端代码 不漂亮的地方，欢迎专业前端大神指出哈
 * */

var sdk = new WSDK();

//是否是测试
var is_the_test = true;
var online_url = "im.tqmall.com";
//登出
var logout_func = undefined;
//打开的当前时间戳
var opened_time;
//是否打开标识
var pageKey;
//传入的参数数据
var param;
//当前对象的aliUid;
var aliUid;
//定时扫是否登录状态
var ali_task;
//是否是第一次初始化登录云旺 undefined，还是 第n次重试登录 1
var is_first_yunwang;

//每次加载的最大消息数 --40
var max_history_num = 20;
//get history nextkey map
var next_key_map = {};

//默认换行标识
var new_line = "monk_<br>_on";

//未读消息总数
var all_un_read_sum = 0;

//当前聊天对象消去的未读数，用户取出最后几条未读消息的数量
var this_un_read_num = 0;

//存对象的uid 对应的 top——url
var iframe_top_map = {};

var nowUserDO = null;

/*====初始化时一些方法指定==*/
var beforeChangePeople_func;
function beforeChangePeople(do_fuc) {
    if ($.isFunction(do_fuc)) {
        beforeChangePeople_func = do_fuc;
    }
}

function initClick() {
    $("#clear_search_people_input").click(function () {
        $('#search_people').val("");
        $(this).addClass("hidden");
        $(".people_li").removeClass("hidden");
        $('#search_people').focus();
    });
    $('#search_people').keyup(function () {
        var old_value = $(this).val();
        var new_value = old_value.trim();
        var new_value_length = new_value.length;
        if (old_value.length != new_value_length) {
            $(this).val(new_value);
        }

        //判断是否隐藏图标
        var close_obj = $("#clear_search_people_input");
        if (new_value_length == 0) {
            close_obj.addClass("hidden");
            $(".people_li").removeClass("hidden");
        } else {
            close_obj.removeClass("hidden");
            var search_value = new_value.toUpperCase();
            $(".people_li").addClass("hidden");
            $.each($(".people_li"), function (i, li_obj) {
                var the_name = $(li_obj).data("name").toUpperCase();
                if (the_name.indexOf(search_value) > -1) {
                    $(li_obj).removeClass("hidden");
                }
            });
        }
    });

    //底部拓展按钮点击
    $('.expand_btn').unbind("click").click(function () {
        var expand_bottom_iframe = $("#expand_bottom_iframe");
        var expand_main = $("#expand_main");
        $(".chat_content").addClass("expand");

        $("#expand_bottom").addClass("hidden");
        $("#expand_bottom_close").removeClass("hidden");
        //展开
        expand_main.show("slow");
        to_chat_bottom();
        //聚焦在聊天框
        $("#new_message_area").focus();
    });
    //收缩按钮点击
    $('.expand_btn_close').unbind("click").click(function () {
        $("#expand_bottom").removeClass("hidden");
        $("#expand_bottom_close").addClass("hidden");

        $(".chat_content").removeClass("expand");
        //收缩
        $("#expand_main").hide("slow");

        //聚焦在聊天框
        $("#new_message_area").focus();
    });

    //弹框红叉叉关闭弹窗
    $("#monk_alert_model .model-close").unbind("click").click(function () {
        MMDChat.alertHide();
    });
    $("#monk_pic_model .model-close").unbind("click").click(function () {
        MMDChat.alertImgHide();
    });

    //当滚到顶部，出现加载gift
    $(".chat_main").scroll(function () {
        //加载数据
        if ($(".chat_main").scrollTop() <= 0) {

            var chat_msg_wrap_length = $("#chat_main_div .chat_msg_wrap").length;
            if ($("#loading").html() == '' && chat_msg_wrap_length > 0) {
                var touid = $(".people_li.active").data("uid");
                get_history(touid, next_key_map[touid])
            }
        }
    });
    //图片弹窗收缩
    $("#monk_pic_model").click(function () {
        MMDChat.alertImgHide();
        $('#new_message_area').focus();
    });
    $("#model_img").click(function () {
        return false;
    });

}
//window 变化
function windowChange() {
    //关闭窗口时
    $(window).unload(function () {
        onbeforeunload();
    });
    //当浏览器大小变化时
    $(window).resize(function () {
        initCss();
    });
    //浏览器加载时
    $(window).load(function () {
        // 检查客户端（一般是浏览器）对Notifications的支持
        NotifyUtil.sendNotify("淘汽车信", "欢迎您的到来~~~")
    });

}
//关闭窗口或刷新
function onbeforeunload() {
    if (param ) {
        ConstantsMap.storage.setItem(StorageKey.initParamKey, JSON.stringify(param));
    }
    ConstantsMap.storage.removeItem("__WSDK__");
    ConstantsMap.storage.removeItem(pageKey);
    ConstantsMap.storage.removeItem(pageKey + "refuse");
    ConstantsMap.storage.removeItem(pageKey + StorageKey.isClosed);
    ConstantsMap.storage.removeItem(pageKey + StorageKey.isClosed);
}

//关闭窗口
function windowClose() {
    onbeforeunload();
    ConstantsMap.storage.clear();
    window.close();
}

//初始化css样式
function initCss() {
    //body 跟浏览器一样高
    changeBodyHeight();
    //用户列表页面
    var people_ul_obj = $(".people_ul");
    var chat_height = $('.chat_people').height();
    var other_height = 0;
    $.each(people_ul_obj.siblings(), function (i, sib_obj) {
        other_height += $(sib_obj).outerHeight();
    });
    var people_ul_height = (chat_height - other_height) / chat_height * 100;
    people_ul_obj.height(people_ul_height + "%");
    fontCss();
}

function fontCss() {
    //根据不同的操作系统，如果是mac，span字体为微软雅黑
    if (MMDChat.isMac()) {
        $('span').css('font-family', '"Helvetica","Microsoft YaHei"');
        $('pre').css('font-family', '"Helvetica","Microsoft YaHei"');
    }
}

function changeBodyHeight() {
    var height = document.documentElement.clientHeight;
    var body = $('body');
    var min_height = body.css("min-height");
    body.css("height", height + "px");
    if (Number(height) < Number(min_height.replace("px", ""))) {
        body.css("overflow-y", "auto");
    } else {
        body.css("overflow-y", "hidden");
    }

    $('#monk_pic_model').css("max-height", height + "px");
    if ($('#monk_pic_model').height() + 10 > height) {
        $('#monk_pic_model').css("top", "0%");
    } else {
        $('#monk_pic_model').css("top", "50%");
    }
}
/*=======数据审核是否通过=============*/
function initCheck() {
    param = JSON.parse(ConstantsMap.storage.getItem(StorageKey.initParamKey));
    if (param == undefined) {
        MMDChat.alertFunc("参数错误或登录超时，5秒后将关闭窗口，请重新打开聊天框");
        //弹出错误的提示 关闭页面
        setTimeout(function () {
            windowClose();
        }, 5000);
    }
    if (ConstantsMap.headerUrl.indexOf(online_url) > -1) {
        is_the_test = false;
    }
    pageKey = StorageKey.imPageOpened + param['sys_id'] + param['sys_name'];

    error_func();
    ConstantsMap.storage.setItem(pageKey + "refuse", "2");
    ConstantsMap.storage.setItem(pageKey, "opened");
    ImConstants.checkIsTrue(param, function (result) {

        nowUserDO = result.data;
        //初始化socket
        aliUid = nowUserDO['aliUid'];
        $("#aliuid").val(aliUid);
        //top_url
        var top_url = param['top_url'];
        if (top_url ) {
            $("#top_url").val(top_url);
        }
        //ConstantsMap.storage.setItem(pageKey,"opened");
        StoryListener();
        //初始化云旺数据
        initAliYunWang();
    }, function (result) {
        console_func("wrong fail to check");
        MMDChat.alertFunc("出错啦~" + result.message + " 5秒后关闭窗口");
        //弹出错误的提示 关闭页面
        setTimeout(function () {
            windowClose();
        }, 5000);

    });

}

var first_init_yunwang = true;
var log_out_index = 0;
var log_out_max_num = 5;
function initAliYunWang(successdo) {
    //登录
    var uid = nowUserDO['aliUid'];
    var password = nowUserDO['aliPassword'];
    var appKey = nowUserDO['appKey'];

    //
    logout_func = sdk.Base.logout;
    if ($.isFunction(logout_func) && !first_init_yunwang) {

        sdk.Base.logout({
            success: function (data) {
                console_func("log out success");
                console_func(data);
                log_out_index = 0;
                yunwangLogin(uid, password, appKey, successdo);
            },
            error: function (data) {
                console_func("log out error");
                console_func(data);
                if (log_out_index < log_out_max_num) {
                    log_out_index++;
                } else {
                    log_out_index = 0;
                    first_init_yunwang = true;
                }
                initAliYunWang(successdo);
            }
        });
    } else {
        first_init_yunwang = false;
        yunwangLogin(uid, password, appKey, successdo);
    }


}

var login_500 = true;
function yunwangLogin(uid, password, appKey, successdo) {
    setTimeout(function () {
        if (login_500) {
            MMDChat.unblockUI();
            MMDChat.alertFunc("网络出现异常，请刷新或退出重新打开该页面");
        }
    }, 15000);

    sdk.Base.login({
        uid: uid,
        appkey: appKey,
        credential: password,
        timeout: 2 * 60 * 1000,
        success: function (data) {
            console_func('login success', data);
            login_500 = false;
            if (successdo ) {
                successdo();
            } else {
                base_ali_func();
            }
        },
        error: function (error) {
            console_func('login fail', error);
            login_500 = false;
            var code = Number(error['code']);
            error_func(code);
            if (code == 1008) {
                finalTry(successdo)
            } else if (code == 1002) {
                MMDChat.alertFunc("登录超时，请刷新后重试");
            } else {
                initAliYunWang(successdo);
            }
        }
    });
}

//登录成功执行的操作
function base_ali_func() {
    //任务
    timeTask();

    //第一次登录云旺
    if (is_first_yunwang == undefined) {
        //设置当前时间戳
        opened_time = new Date().getTime();
        //初始化联系人列表,初始化时未读消息数在其中处理
        recentContact();
    } else {
        //获得所有未读消息数
        getUnReadMessageNum();
        //获得当前联系人的聊天历史，对比后，补充
        getNowChatContentAppend();

    }


}

//定时任务,每20s扫一次，防止用户掉线，重新登录
function timeTask() {
    if (ali_task ) {
        window.clearInterval(ali_task);
    }
    //监听事件
    allMessageListenser();
    ali_task = window.setInterval(function () {
        errorRetry();
        //获得当前联系人的聊天历史，对比后，补充
        getNowChatContentAppend();

        //监听事件
        allMessageListenser();
    }, 20000);
}

/*===========ali 函数封装=======*/
function error_func(code, net_check) {
    //清除缓存，重新塞，防止一直登录上
    ConstantsMap.storage.removeItem("debug");
    ConstantsMap.storage.removeItem("__WSDK__");
    if (net_check ) {
        if (code == 1005) {
            MMDChat.alertFunc("网络出现问题，请检查网络后重试");
        }
    }
}
function finalTry(successdo) {
    sdk.Base.getRecentContact({
        count: 1,
        success: function (data) {
            console_func('final success', data);
            if (successdo ) {
                successdo();
            }
        },
        error: function (error) {
            console_func('final fail', error);
        }
    });
}
function errorRetry() {
    sdk.Base.getUnreadMsgCount({
        count: 1,
        success: function (data) {
            console_func('errorRetry success', data);
        },
        error: function (error) {
            console_func('errorRetry fail', error);
            var code = Number(error['code']);
            error_func(code);
            if (code == 1001) {
                initAliYunWang();
            }
        }
    });
}

//最近联系人，第一次初始化时，会附加未读消息
function recentContact() {
    sdk.Base.getRecentContact({
        count: 100,
        success: function (data) {
            console_func('get recent contact success', data);
            //第一次登录云旺/初始化联系人列表
            if (is_first_yunwang == undefined) {
                //处理数据渲染到页面上
                recentPeople(data);
                //第一次 在联系人渲染后，进行未读消息数的处理
                getUnReadMessageNum();
            }
            fontCss();
        },
        error: function (error) {
            console_func('get recent contact fail', error);
            var code = Number(error['code']);
            error_func(code);
            if (code == 1001 || code == 1005) {
                initAliYunWang(function () {
                    recentContact();
                });
            }
        }
    });
}

//重试数
var retry_history_num = 0;
//如果存在special_func 则做自定义处理
function get_history(touid, nextkey, special_func) {
    if (nextkey == undefined) {
        nextkey = "";
    }
    if (special_func == undefined) {
        MMDChat.blockUI("#chat_main_div");
    }
    //漫游历史消息
    sdk.Chat.getHistory({
        touid: touid,
        nextkey: nextkey,
        count: max_history_num,
        success: function (data) {
            console_func('get history msg success', data);
            MMDChat.unblockUI("#chat_main_div");

            retry_history_num = 0;
            if (touid != $(".people_li.active").data("uid")) {
                //联系人被快速切换了
                return false;
            }

            if (special_func == undefined) {
                next_key_map[touid] = data.data['nextKey'];
                //渲染到页面上
                appendChatContent(data);
            } else {
                special_func(data, data.data['nextKey']);
            }

            fontCss();
        },
        error: function (error) {
            console_func('get history msg fail', error);
            MMDChat.unblockUI("#chat_main_div");

            retry_history_num++;

            var code = Number(error['code']);
            error_func(code);

            to_chat_more();
            if (code == 1001) {
                if (retry_history_num < 5) {
                    if (special_func == undefined) {
                        $("#loading").html("加载数据出现错误，请重新加载数据");
                    }
                    //重新发送数据
                    initAliYunWang(function () {
                        get_history(touid, nextkey);
                    });

                } else {
                    retry_history_num = 0;
                    if (special_func == undefined) {
                        $("#loading").html("加载数据出现错误，请重新加载数据");
                    }
                }
            }
        }
    });
}


//发消息
//发消息失败重试次数
var retry_send_message_num = 0;
function send_message(touid, message, msgType, this_obj) {
    var reg = /\n/g;
    message = message.replace(reg, new_line);

    if (this_obj == undefined) {
        //发送消息的处理
        var now_time = TimeUtil.get13Time(new Date().getTime());
        var last_time = TimeUtil.get13Time($(".chat_msg_wrap:last").data("time"));
        if (last_time >= now_time) {
            now_time = Number(last_time) + 1;
        }

        var time_str = TimeUtil.getTimeForChat(now_time);
        var msg_array = message.split(new_line);
        var map = {time_str: time_str, type: msgType, msg_array: msg_array, time_ns: now_time};
        var chat_html = template('chat_msg_template', {list: [map]});
        appendOneContent(chat_html, message, msgType, "not");
        //图片框消失
        MMDChat.alertHide();
        //聚焦到聊天框上
        $('#new_message_area').focus();

        this_obj = $(".chat_msg_wrap:last");
        this_obj.addClass("loading");

        $(".msg_error").unbind("click").click(function () {
            var now_obj = $(this).parent().parent().parent();
            var foot_html = '<button class="chat-default-btn chat-expand-blue" id="send_msg_again"> <span>重新发送</span> </button>';
            foot_html += "&nbsp;&nbsp;";
            foot_html += '<button class="chat-default-btn chat-expand-blue back-grey" id="send_msg_cancel"> <span>取消</span> </button>';
            MMDChat.alertFunc("该消息发送失败,是否重新发送该条消息?", undefined, foot_html);
            $("#send_msg_cancel").unbind("click").click(function () {
                MMDChat.alertHide();
            });
            $("#send_msg_again").unbind("click").click(function () {
                var the_type = Number(now_obj.data("type"));
                var the_uid = $(".people_li.active").data("uid");
                var msg = "";
                if (the_type == MessageType.text) {
                    msg = now_obj.find("pre").html().trim().replace(/<br>/, "\n");
                } else if (the_type == MessageType.img) {
                    msg = now_obj.find("pre img")[0].src;
                }
                now_obj.remove();
                MMDChat.alertHide();
                send_message(the_uid, msg, the_type);
            });

        });
    }

    sdk.Chat.sendMsg({
        touid: touid,
        msg: message,
        msgType: msgType,
        success: function (data) {
            console_func('send success', data);
            console_func('touid:' + touid + " message:" + message);

            retry_send_message_num = 0;
            $('#new_message_area').val('');
            //移除loading
            this_obj.removeClass("loading");
            this_obj.removeClass("error");

            if (msgType == MessageType.text) {
                //发送成功后，收集数据到缓存中
                saveLOPWishNumber(message);
            }

        },
        error: function (error) {
            console_func('send fail', error);
            retry_send_message_num++;
            var code = Number(error['code']);
            error_func(code);
            if (code == 1001) {
                //重新发送数据
                if (retry_send_message_num < 5) {
                    initAliYunWang(function () {
                        send_message(touid, message, msgType, this_obj);
                    });
                } else {
                    retry_send_message_num = 0;
                    this_obj.removeClass("loading");
                    this_obj.addClass("error");
                }
            }
            //30秒如果失败，则设置error
            setTimeout(function () {
                if (this_obj.hasClass("loading")) {
                    this_obj.removeClass("loading");
                    this_obj.addClass("error");
                }
            }, 10000);

        }
    });
}


var retry_un_read_num = 0;
function getUnReadMessageNum() {
    sdk.Base.getUnreadMsgCount({
        count: 100,
        success: function (data) {
            console_func('get unread msg count success', data);
            retry_un_read_num = 0;
            UnReadMessageDo(data);
            if (is_first_yunwang == undefined) {
                //指定跟某人聊天
                chat_with_uid_func();

                //赋值，结束第一次
                is_first_yunwang = 1;
            } else {
                MMDChat.unblockUI();
            }
        },
        error: function (error) {
            console_func('get unread msg count fail', error);
            retry_un_read_num++;
            var code = Number(error['code']);
            error_func(code);
            //if (Number(error['code']) == 1001) {
            //    if (retry_un_read_num < 5) {
            //        //重新发送数据
            //        initAliYunWang(getUnReadMessageNum);
            //    }else{
            //        retry_un_read_num = 0;
            //    }
            //}
        }
    });
}

//监听所有聊天内容,未读消息
function allMessageListenser() {
    sdk.Event.off('START_RECEIVE_ALL_MSG');
    sdk.Base.stopListenAllMsg();
    //监听所有的聊天内容
    sdk.Event.on('START_RECEIVE_ALL_MSG', function (data) {
        console_func("当前聊天对象的所有内容：");
        console_func(data);

        var code = data['code'];
        if (code == 1005) {
            console_func("allMessageListenser网络波动(网络切换或者断网后又自动连上)");
            //网络波动(网络切换或者断网后又自动连上)
            setTimeout(function () {
                sdk.Base.startListenAllMsg();
            }, 1000);
        } else if (code == 1000) {
            message_listenser_do(data);
        }

    });
    sdk.Base.startListenAllMsg();
}

//设置消息已读
var retry_have_read_num = 0;
function setHaveRead(touid, success_func) {
    error_func();
    sdk.Chat.setReadState({
        touid: touid,
        timestamp: Math.ceil((new Date()) / 1000),
        success: function (data) {
            console_func('set read state success', data);
            retry_have_read_num = 0;
            nowUnReadMsg();

            if (success_func ) {
                success_func();
            }
        },
        error: function (error) {
            console_func('set read state fail', error);
            retry_have_read_num++;
            var code = Number(error['code']);
            error_func(code);

        }
    });
}

//遍历现在所有的未读消息数
function nowUnReadMsg() {
    var sum = 0;
    $.each($(".people_li"), function (i, people_obj) {
        var msg_num = $(people_obj).find(".badge").text();
        if (msg_num == "") {
            msg_num = 0;
        }
        sum += Number(msg_num);
    });
    //未读消息总数 变更
    if (sum != all_un_read_sum) {
        //发送数据
        all_un_read_sum = sum;
        sendUnReadMessageToOut();

    }
}

/*=========云旺的数据处理到页面=========*/
//指定跟某uid聊天
function chat_with_uid_func(aliUid) {
    var to_sys_id = param['to_sys_id'];
    var to_sys_name = param['to_sys_name'];

    if (aliUid ) {
        //socket 传递过来 跟谁聊天
        getUsersByuuid(aliUid, chat_with_uid_func_do, function () {
            MMDChat.unblockUI();
        });
        return false;
    } else if (to_sys_id  && to_sys_id != "" && to_sys_name  && to_sys_name != "") {
        ImConstants.getUsersData(to_sys_id, to_sys_name, chat_with_uid_func_do, function () {
            MMDChat.unblockUI();
        });
        return false;
    } else
    //如果没有对象，则弹出
    if ($(".people_li").length == 0) {
        noPeopleDo();
    }
    MMDChat.unblockUI();

}

function chat_with_uid_func_do(result) {
    var userDO = result.data;
    var touid = userDO['aliUid'];
    var sysName = userDO['sysName'];
    if (isTheSameSysName(sysName)) {
        //改用户不与它聊
        //如果没有对象，则弹出
        if ($(".people_li").length == 0) {
            noPeopleDo();
        }
        MMDChat.unblockUI();
        return false;
    }

    var exist = false;
    //遍历列表，是否存在该联系人
    $.each($(".people_li"), function (i, pli_obj) {
        var li_uid = $(pli_obj).data("uid");
        if (li_uid.toLowerCase() == touid.toLowerCase()) {
            exist = true;
            //class active，模拟点击
            $(pli_obj).addClass("active");
            $(pli_obj).trigger('click');
            //break
            return false;
        }
    });
    //若不存在联系人，新增联系人
    if (!exist) {
        if (isMySelf(touid)) {
            return false;
        }
        var people_map = {
            uid: touid,
            time_ns: new Date().getTime(),
            time_str: TimeUtil.getTimeStringForPeople(),
            sys_id: userDO['sysId'],
            name: userDO['userName'],
            nickName: userDO['userNickName']
        };
        var people_html = template('people_li_template', {list: [people_map]});
        $(".people_ul").prepend(people_html);
        people_li_click();
        var pli_obj = $(".people_li:first");
        $(pli_obj).addClass("active");
        $(pli_obj).trigger('click');
    }

    fontCss();
    MMDChat.unblockUI();
}

//获得当前联系人的聊天历史，对比后，补充
function getNowChatContentAppend() {
    var uid = $(".people_li.active").data("uid");
    if (uid == undefined) {
        return;
    }
    uid = uid.toLowerCase();
    setHaveRead(uid, function () {
        //根据未读消息数，获得未读数量,默认移除超过数量的数据,若本身无数据，则添加数据
        var exist_fuc = undefined;
        if ($(".chat_msg_wrap").length > 0) {
            exist_fuc = function (dataObject, next_key) {

                var msgs = dataObject.data.msgs;
                if (msgs.length == 0) {
                    return false;
                }
                //是否进行数量变更
                var is_change_msg = false;

                $.each(msgs, function (i, msgVO) {
                    var msg = msgVO['msg'];
                    var from = sdk.Base.getNick(msgVO['from']).toLowerCase();
                    var type = msgVO['type'];
                    var time_ns = parseInt(msgVO['time']);
                    var time_str = TimeUtil.getTimeForChat(time_ns);
                    if (from != uid) {
                        //自身发的，过滤掉，continue
                        return true;
                    }
                    //对比现有的所有记录
                    var chat_msg_wrap_list = $(".chat_msg_wrap");
                    var reverse_list = [];
                    $.each(chat_msg_wrap_list, function (i, one_obj) {
                        reverse_list.unshift(one_obj);
                    });
                    $.each(reverse_list, function (k, one_obj) {
                        if (k > max_history_num) {
                            //超过最大数，则不比较，作为丢失处理
                            return false;
                        }
                        var this_obj = $(one_obj);
                        var final_time = this_obj.data("time");
                        var the_msg = this_obj.find("pre").html();
                        if (the_msg ) {
                            var reg = /<br>/g;
                            the_msg = the_msg.replace("\n", "").replace("\r", "").trim().replace(/ /g, "").replace(reg, new_line);
                        }

                        //文字全量匹配、图片地址包含、语音暂时不处理
                        if (this_obj.hasClass("chat_l") && ((type == MessageType.text && msg.replace("\n", "").replace("\r", "").replace(/ /g, "").trim() == the_msg) || (type == MessageType.img && isSameImg(msg, the_msg)) || type == MessageType.voice)) {
                            // break; 匹配上了
                            return false;
                        }


                        if (TimeUtil.get13Time(final_time) < TimeUtil.get13Time(time_ns)) {
                            console_func("新登录，动态增加的聊天内容");
                            //
                            //if (this_obj.hasClass("chat_r")) {
                            //    //是自身的，过滤掉
                            //    console_func("因为是自身，过滤掉");
                            //    return true;
                            //}
                            console_func(this_obj);
                            // 此时后面的数据无对比的需要，则表示没有找到该文案，填加内容在该obj后面
                            is_change_msg = true;
                            //https 替换
                            if(type == MessageType.img){
                                msg = msg.replace("http://","https://");
                            }
                            var msg_array = msg.split(new_line);
                            var msg_map = {
                                msg_array: msg_array,
                                time_ns: time_ns,
                                time_str: time_str,
                                type: type,
                                you: true
                            };//聊天对象
                            var msg_html = template('chat_msg_template', {list: [msg_map]});
                            console_func(msg_map);

                            var is_need_to_bottom = false;
                            if (final_time == $(".chat_msg_wrap:last").data("time") && this_obj.find("pre").html() == $(".chat_msg_wrap:last").find("pre").html() && is_footer_bottom()) {
                                is_need_to_bottom = true;
                            }

                            this_obj.after(msg_html);

                            if (type == MessageType.text) {
                                //接收成功后，收集数据到缓存中
                                saveLOPWishNumber(msg);
                                fontCss();
                                if (is_need_to_bottom) {
                                    to_chat_bottom();
                                }
                            }
                            if (type == MessageType.img) {
                                //图片加点击触发事件
                                img_show_func();
                                if (is_need_to_bottom) {
                                    loadHistoryImg(to_chat_bottom, ".chat_msg_wrap:last img");
                                }
                            }

                            console_func("end 新登录，动态增加的聊天内容");
                            //break;
                            return false;
                        }


                    });
                });
                //设置当前聊天对象时间和active
                if (is_change_msg) {
                    setFirstChatPeople_fuc($(".chat_msg_wrap:last").data("time"));
                    next_key_map[uid] = next_key;
                    //移除超过数量的消息
                    var msg_obj = $(".chat_msg_wrap");
                    var msg_obj_size = msg_obj.length;
                    for (var i = 0; i < msg_obj_size - max_history_num; i++) {
                        $(".chat_msg_wrap:first").remove();
                    }
                    fontCss();
                }
            };
        }

        get_history(uid, '', exist_fuc);
    });
}

//是否是同样的云旺img，通过fileId区别
function isSameImg(first_img, second_img) {
    var fileId_name = "fileId";
    var first_img_array = first_img.split(fileId_name);
    var second_img_array = second_img.split(fileId_name);
    if (first_img_array.length < 2 || second_img_array.length < 2) {
        return false;
    }
    var first_id = first_img_array[1].split("&")[0].replace("=", "");
    var second_id = second_img_array[1].split("&")[0].replace("=", "");
    if (first_id == second_id) {
        return true;
    }
    return false;
};

//最近联系人
function recentPeople(dataObject) {
    var cnts = dataObject.data.cnts;

    var people_list = new Array();
    $.each(cnts, function (i, peopleObject) {
        var uid = sdk.Base.getNick(peopleObject['uid']);
        if (isMySelf(uid)) {
            //是自身
            return true;
        }
        var time_ns = parseInt(peopleObject['time']);
        var time_str = TimeUtil.getTimeStringForPeople(time_ns);

        //根据uid获得名称,同步
        getUsersByuuid(uid, function (result) {
            var userDO = result.data;
            var sysId = userDO['sysId'];
            var sysName = userDO['sysName'];
            var userName = userDO['userName'];
            var userNickName = userDO['userNickName'];

            if (userName) {
                //非同一类型
                if (!isTheSameSysName(sysName)) {

                    var people_map = {
                        uid: uid,
                        time_ns: time_ns,
                        time_str: time_str,
                        sys_id: sysId,
                        name: userName,
                        nickName: userNickName
                    };
                    people_list.push(people_map);
                }
            }

        });

    });
    //定义了sort的比较函数,倒序
    people_list.sort(function (a, b) {
        return b['time_ns'] - a['time_ns'];
    });
    //渲染到页面中
    var people_html = template('people_li_template', {list: people_list});
    $(".people_ul").html(people_html);

    people_li_click();
}
//当前联系人聊天内容
function appendChatContent(dataObject) {
    $('#loading').html('');

    var touid = $(".people_li.active").data("uid");
    var msgs = dataObject.data.msgs;

    var is_first_append = false;
    if ($(".chat_msg_wrap").length == 0) {
        is_first_append = true;
    }
    var msg_list = new Array();

    var msg_wish_list = [];
    $.each(msgs, function (i, msgVO) {
        var msg = msgVO['msg']
        var from = sdk.Base.getNick(msgVO['from']);
        var type = msgVO['type'];
        var time_ns = parseInt(msgVO['time']);
        var time_str = TimeUtil.getTimeForChat(time_ns);
        //https 替换
        if(type == MessageType.img){
            msg = msg.replace("http://","https://");
        }
        var msg_array = msg.split(new_line);

        var msg_map = {msg_array: msg_array, time_ns: time_ns, time_str: time_str, type: type};
        if (from.toLowerCase() == touid.toLowerCase()) {
            //聊天对象
            msg_map['you'] = true;
        }
        msg_list.push(msg_map);

        //存今日聊到,第一次的20条都存进去
        if (type == MessageType.text && is_first_append) {
            var wish_map = {time_ns: time_ns, msg: msg};
            msg_wish_list.push(wish_map);
        }
    });
    //将msg_list 给到需求单中处理
    historySaveForWish(msg_wish_list);

    //设置当前的未读消息均已处理了
    this_un_read_num = 0;
    //顺序
    msg_list.sort(function (a, b) {
        return a['time_ns'] - b['time_ns'];
    });

    var msg_html = template('chat_msg_template', {list: msg_list});
    //保持当前位置使用
    var now_first_obj = $('.chat_msg_wrap:first');

    $("#chat_main_div").prepend(msg_html);


    //图片加点击触发事件
    img_show_func();

    if (msg_html == '' || next_key_map[touid] == '') {
        $("#loading").html("<span>-没有更多数据了-</span>");
    }

    //如果 此时是 第一次点击打开的,滚动
    if (is_first_append) {
        setFirstChatPeople_fuc($('.chat_msg_wrap:last').data("time"));
        loadHistoryImg(to_chat_bottom);
    } else {
        loadHistoryImg(function () {
            to_chat_more(now_first_obj);
        })
    }
}

//未读消息数据处理
function UnReadMessageDo(dataObject) {
    var sum = 0;
    var data_list = dataObject.data;
    $.each(data_list, function (i, unReadVO) {
        var from_uid = sdk.Base.getNick(unReadVO['contact']);
        var msg_num = unReadVO['msgCount'];
        var time_ns = unReadVO['lastmsgTime'];
        //是否当前聊天对象
        var is_active_user = false;
        var li_active_obj = $(".people_li.active");
        if (li_active_obj ) {
            var touid = li_active_obj.data("uid");
            if (touid  && touid.toLowerCase() == from_uid.toLowerCase()) {
                is_active_user = true;
            }
        }
        //若是当前聊天的，则在func中移动位置，若非，也移动位置
        changePeopleLiFunc(from_uid, msg_num, time_ns, 1);
        if (!is_active_user) {
            //除开当前聊天对象，未读数叠加
            sum += Number(msg_num);
        }

    });

    //未读消息总数 变更
    if (sum != all_un_read_sum) {
        //发送数据
        all_un_read_sum = sum;
        sendUnReadMessageToOut();
    }

}

//监听到所有消息
function message_listenser_do(dataObject) {
    var sum = all_un_read_sum;
    var msgs = dataObject.data.msgs;

    //{num:0 time:}
    var from_un_read_map = {};
    $.each(msgs, function (i, msgVO) {
        var from = sdk.Base.getNick(msgVO['from']).toLowerCase();
        var message = msgVO['msg'];
        var msgType = msgVO['type'];
        var time = msgVO['time'];
        var time_str = TimeUtil.getTimeForChat(time);
        if (TimeUtil.get13Time(opened_time) > TimeUtil.get13Time(time)) {
            console_func("获得未读消息，时间小于打开时间");
            //小于登录时间的消息过滤掉
            return true;
        }


        //是否当前聊天对象
        var is_active_user = false;
        var li_active_obj = $(".people_li.active");
        if (li_active_obj ) {
            var touid = li_active_obj.data("uid");
            if (touid  && touid.toLowerCase() == from) {
                is_active_user = true;
            }
        }

        if (is_active_user) {
            //https 替换
            if(msgType == MessageType.img){
                message = message.replace("http://","https://");
            }
            var msg_array = message.split(new_line);
            var map = {time_str: time_str, type: msgType, msg_array: msg_array, time_ns: time, you: true};
            var chat_html = template('chat_msg_template', {list: [map]});
            setHaveRead(from, function () {
                appendOneContent(chat_html, message, msgType);
            });

        } else {
            var un_sum_map = from_un_read_map[from];
            if (un_sum_map == undefined) {
                un_sum_map = {'num': 0, 'time': time, 'msg_obg': {'message': message, 'type': msgType}};
            }
            un_sum_map['num'] += 1;
            if (TimeUtil.get13Time(time) > TimeUtil.get13Time(un_sum_map['time'])) {
                un_sum_map['time'] = time
            }

            from_un_read_map[from] = un_sum_map;
            //未读总数+1
            sum += 1;
        }


    });

    //单个通知
    $.each(from_un_read_map, function (uid, un_sum_map) {
        // 遍历 people_li 判断存在不存在
        changePeopleLiFunc(uid, un_sum_map['num'], un_sum_map['time'], 0, un_sum_map['msg_obg']);
    });

    //未读消息总数 变更
    if (sum != all_un_read_sum) {
        //发送数据
        all_un_read_sum = sum;
        sendUnReadMessageToOut();
    }
}

//获得新消息或者获得未读消息，用户列表位置进行更改的操作:0-监听到新消息 1-未读消息数
//'msg_obg':{'message':message,'type':msgType}
function changePeopleLiFunc(uid, msg_num, time_ns, type, msg_obg) {
    var time_str = TimeUtil.getTimeStringForPeople(time_ns);
    //跟联系人列表 比较
    var find_true_uid = false;
    //已遍历过的对象
    var has_pli_list = [];
    $.each($(".people_li"), function (i, pli_obj) {
        var pe_time_sn = $(pli_obj).data("time");
        var pe_msg_num = $(pli_obj).find('.badge').text();
        if (pe_msg_num == '') {
            pe_msg_num = '0';
        }
        var pe_uid = $(pli_obj).data("uid");
        console_func("changePeopleLiFunc: type:" + type + " uid:" + uid + " pe_uid:" + pe_uid);
        if (uid.toLowerCase() == pe_uid.toLowerCase()) {
            find_true_uid = true;

            var is_active_people = $(pli_obj).hasClass("active");
            if (type == 0) {
                msg_num = Number(pe_msg_num) + msg_num
            }
            if (Number(msg_num) != 0 && !is_active_people) {
                $(pli_obj).find('.badge').text(msg_num);
            }
            //比较最后时间，挪位置
            if (TimeUtil.get13Time(time_ns) > TimeUtil.get13Time(pe_time_sn)) {
                $(pli_obj).data("time", time_ns);
                //document.querySelector(pli_obj).dataset.time = time_ns;
                $(pli_obj).find(".final_name").text(time_str);
                var new_obj = $(pli_obj).clone();
                $.each(has_pli_list, function (i, has_obj) {
                    if (TimeUtil.get13Time(time_ns) > TimeUtil.get13Time($(has_obj).data("time"))) {
                        $(has_obj).before(new_obj);
                        $(pli_obj).remove();
                        //跳出循环,break
                        return false;
                    }
                })
            }
            //非初始化，若消息数量变更，通知
            if (is_first_yunwang ) {
                if (Number(pe_msg_num) != Number(msg_num) && !is_active_people) {
                    var pli_name = $(pli_obj).data("name");
                    if (pli_name == undefined || pli_name == "") {
                        //无名称
                        return false;
                    }
                    if (type == 1) {
                        NotifyUtil.sendNotify(pli_name, "给你发了" + msg_num + "条未读消息");
                    } else {
                        var msg_message = msg_obg['message'];
                        var msg_type = msg_obg['type'];
                        var notify_html = "给你发了一段语言";
                        if (msg_type == MessageType.text) {
                            notify_html = msg_message.replace(eval("/" + new_line + "/g"), "\n");
                        } else if (msg_type == MessageType.img) {
                            notify_html = "给你发了一张图片";
                        }
                        NotifyUtil.sendNotify(pli_name, notify_html);
                    }
                }
            }
            //跳出循环,break
            return false;
        }
        has_pli_list.push(pli_obj);
    });

    //若当前联系人列表中无此联系人
    if (!find_true_uid) {
        console_func("changePeopleLiFunc add new people: type:" + type + " uid:" + uid);
        if (isMySelf(uid)) {
            //是自身
            return false;
        }
        //根据uid获得名称,同步
        var people_list = [];
        getUsersByuuid(uid, function (result) {
            var userDO = result.data;
            var name = userDO['userName'];
            //if(name == undefined || name == ""){
            //    //无名称
            //    return false;
            //}
            var people_map = {
                uid: uid,
                time_ns: time_ns,
                time_str: TimeUtil.getTimeStringForPeople(time_ns),
                num: msg_num,
                sys_id: userDO['sysId'],
                name: name,
                nickName: userDO['userNickName']
            };
            people_list.push(people_map);
            var people_html = template('people_li_template', {list: people_list});
            var people_li_obj = $(".people_li");
            if (people_li_obj.length == 0) {
                $(".people_ul").prepend(people_html);
            } else {
                $.each(people_li_obj, function (i, pli_obj) {
                    if (TimeUtil.get13Time(time_ns) > TimeUtil.get13Time($(pli_obj).data("time"))) {
                        //新增联系人
                        $(pli_obj).before(people_html);
                        //跳出循环,break
                        return false;
                    }
                });
            }

            //非初始化，若消息数量变更，通知
            if (is_first_yunwang ) {
                var pli_name = people_map["name"];
                if (pli_name == undefined || pli_name == "") {
                    //无名称
                    return false;
                }
                if (type == 1) {
                    NotifyUtil.sendNotify(pli_name, "给你发了" + msg_num + "条未读消息");
                } else {
                    var msg_message = msg_obg['message'];
                    var msg_type = msg_obg['type'];
                    var notify_html = "给你发了一段语言";
                    if (msg_type == MessageType.text) {
                        notify_html = msg_message.replace(eval("/" + new_line + "/g"), "\n");
                    } else if (msg_type == MessageType.img) {
                        notify_html = "给你发了一张图片";
                    }
                    NotifyUtil.sendNotify(pli_name, notify_html);
                }
            }

        });
    }

    people_li_click();
    fontCss();

}


//设置当前聊天对象时间和active
function setFirstChatPeople_fuc(time_ns) {
    var now_people_li = $(".people_li.active");
    var first_people_li = $(".people_li:first");

    if (time_ns == undefined) {
        time_ns = new Date().getTime();
    }
    var time_str = TimeUtil.getTimeStringForPeople(time_ns);
    now_people_li.data("time", time_ns);
    document.querySelector('.people_li.active').dataset.time = time_ns;
    now_people_li.find(".final_name").text(time_str);

    var now_uid = now_people_li.data("uid");
    var first_uid = first_people_li.data("uid");

    var time_ns_str = TimeUtil.get13Time(time_ns);
    // 是否有比
    var haveBiggerThanOther = false;
    $.each($(".people_li"), function (i, pli_obj) {
        if (now_uid == first_uid && i == 0) {
            // 不跟自己比较，自己是第一位
            return true;
        }
        var this_pli_time = $(pli_obj).data("time");
        var this_pli_time_str = $(pli_obj).find(".final_name").text().trim();
        var this_time_str = TimeUtil.getTimeStringForPeople(this_pli_time);
        if (this_pli_time_str != this_time_str) {
            $(pli_obj).find(".final_name").text(this_time_str);
        }

        var this_pli_str = TimeUtil.get13Time(this_pli_time);
        if (time_ns_str == this_pli_str){
            haveBiggerThanOther = true;
            return false;
        }
        if (time_ns_str > this_pli_str) {
            haveBiggerThanOther = true;
            if (now_uid == first_uid && i == 1) {
                // 比第二大，无须进行后续
                return false;
            }

            var new_obj = now_people_li.clone();
            //变更联系人
            $(pli_obj).before(new_obj);
            now_people_li.remove();
            people_li_click();
            //跳出循环,break
            return false;
        }
    });

    if (!haveBiggerThanOther) {
        //它最小，放最后
        var new_obj = now_people_li.clone();
        //变更联系人
        $(".people_ul").append(new_obj);
        now_people_li.remove();
        people_li_click();
        return false;
    }

}


//底部增加内容，判断是否要下拉到底部
function appendOneContent(html, message, msgType, send_not_ok) {
    var is_footer = is_footer_bottom();

    $("#chat_main_div").append(html);
    //设置当前聊天对象时间和active
    setFirstChatPeople_fuc();
    if (msgType == MessageType.text) {
        fontCss();

        if (is_footer) {
            to_chat_bottom();
        }

        if (send_not_ok == undefined) {
            //接收成功后，收集数据到缓存中
            saveLOPWishNumber(message);
        }

    }
    if (msgType == MessageType.img) {

        if (is_footer) {
            loadHistoryImg(to_chat_bottom, ".chat_msg_wrap:last img");
        }
        //图片加点击触发事件
        img_show_func();

    }


}
/*============数据加载后的点击事件================*/
function people_li_click() {
    $(".people_li").unbind("click").click(function () {
        is_first_wish_click = true;
        //用户切换点击事件-给子页面
        if ($.isFunction(beforeChangePeople_func)) {
            if (!beforeChangePeople_func()) {
                return false;
            }
        }

        error_func();

        var old_uid = $(".people_li.active").data("uid");
        $(".people_li").removeClass("active");
        $(this).addClass("active");

        //如果欢迎页面存在，则去掉
        if ($(".chat_content").hasClass("hidden")) {
            $(".welcome_content").addClass("hidden");
            $(".chat_content").removeClass("hidden");
        }
        ;
        //若用户变更，底部拓展框自动关闭
        if (old_uid != $(this).data("uid")) {
            $('.expand_btn_close').trigger('click');
        }
        //页面数据渲染和事件
        chat_content_do();


    })
}

function chat_content_do() {
    //去掉当前未读消息数，并通知
    var people_li_active = $(".people_li.active");
    var the_sum = people_li_active.find(".badge").text();
    this_un_read_num = 0;
    if (the_sum != "") {
        this_un_read_num = Number(the_sum);
        //发送数据
        all_un_read_sum = all_un_read_sum - Number(the_sum);
        sendUnReadMessageToOut();
    }

    people_li_active.find(".badge").text('');

    //标题更改为 您正在和【xxxx】对话中
    document.title = '您正在和【' + people_li_active.data("name") + '】对话中';
    //清空聊天框里面的文字、聊天框 loading页面
    $("#new_message_area").val("");
    $("#chat_main_div").html("");
    $("#loading").html("");

    setHaveRead(people_li_active.data("uid"));
    //是否存在嵌入，若存在 则进行处理
    iframe_func();
    //填入聊天页面
    chat_html_func();
    //工具栏按钮
    util_func();
    //发送文字内容
    send_message_func();
}

//是否存在嵌入，若存在 则进行处理
function iframe_func() {
    var top_url = $("#top_url").val();
    var people_active = $(".people_li.active");
    var chat_aliu_id = people_active.data("uid");
    var chat_sys_id = people_active.data("sys_id");
    var to_sys_id = param["to_sys_id"];

    var bottom_url_btn_name = param['bottom_url_btn_name'];
    var bottom_url = param['bottom_url'];

    //处理top url
    if (top_url.trim() != "") {
        if (to_sys_id  && chat_sys_id == to_sys_id) {
            //存入 该对象对应的
            iframe_top_map[chat_aliu_id] = top_url;
            $("#top_url").val("");
        }
    }
    var this_top_url = iframe_top_map[chat_aliu_id];
    if (this_top_url && this_top_url.trim() != "") {
        //当前联系人的
        var top_html = template('expand_top_template', {top_url: this_top_url});
        $("#chat_main_div").append(top_html);
        $("#expand_top").removeClass("hidden");
        $('#expand_top_close').unbind("click").click(function () {
            expand_top_click();
        });
    }

    //底部,是否隐藏
    if ((bottom_url_btn_name == undefined || bottom_url_btn_name.trim() == "") && (bottom_url == undefined || bottom_url.trim() == "")) {
        $(".chat_content").addClass("no-expand");
    } else {
        $(".chat_content").removeClass("no-expand");
        if (bottom_url_btn_name  && bottom_url_btn_name.trim() != "") {
            $(".expand_bottom_name").text(bottom_url_btn_name.trim())
        }
        if (bottom_url && bottom_url.trim() != "") {
            var sys_id = param['sys_id'];
            bottom_url = bottom_url.replace("chat_sys_id", sys_id).replace("chat_to_sys_id", chat_sys_id);
            $("#expand_bottom_iframe").attr('src', bottom_url);
        }
    }


}

function chat_html_func() {
    var choose_li = $(".people_li.active");
    var name = choose_li.data("name");
    var touid = choose_li.data("uid");
    //标题
    $("#chat_content_header").text(name);
    //内容
    get_history(touid);

}

function util_func() {

    //剪切按钮
    $("#cut_util").unbind("click").click(function () {
        var title = "亲，截图功能暂未开放，请使用截图工具进行复制黏贴，谢谢";
        var content = '<div class="cut_show">';
        content += '<div class="title"><i class="fa  fa-quote-left"></i>截图流程<i class="fa  fa-quote-right"></i></div>';
        content += '<div><span>使用QQ等截图工具截取需要区域(可快捷键)</span><i class="fa fa-arrow-down"></i></div>';
        content += '<div><img  src="' + ConstantsMap.headerUrl + '/img/cut/choose_cut.jpg"></div>';
        content += '<div><span>确定截图页面</span><i class="fa fa-arrow-down"></i></div>';
        content += '<div><img src="' + ConstantsMap.headerUrl + '/img/cut/cut.jpg"></div>';
        content += '<div><span>鼠标点击锁定车信聊天页面</span><i class="fa fa-arrow-down"></i></div>';
        content += '<div><img src="' + ConstantsMap.headerUrl + '/img/cut/focus_chat.jpg"></div>';
        content += '<div><span>Ctrl+V(window)/Command+V(Mac)黏贴图片</span><i class="fa fa-check"></i></div>';
        content += '</div>';
        MMDChat.alertFunc(title, content);

    });

    //图片上传
    $('#pic_util').unbind("change").change(function () {
        //文件处理
        putImgFile();
    });
}


/*======图片处理===========*/
function putImgFile() {
    var file = document.getElementById("pic_util").files[0];

    $("#pic_util").val("");
    solveImgFile(file);
}

function solveImgFile(the_file) {
    //这里我们判断下类型如果不是图片就返回
    var file_name = the_file.name;
    var ext = file_name.substring(file_name.lastIndexOf("."), file_name.length).toUpperCase();
    if (ext != ".PNG" && ext != ".JPG" && ext != ".JPEG" && ext != ".BMP") {
        MMDChat.alertFunc("当前上传的文件不是支持的图片类型(png/jpg/jpeg/bmp)，请选择图片重新上传");
        return false;
    }
    var file_size = the_file.size;
    if (file_size > 3 * 1024 * 1024) {
        MMDChat.alertFunc("图片文件大于3MB，无法发送，请压缩后重新发送");
        return false;
    }

    var reader = new FileReader();
    reader.readAsDataURL(the_file);
    reader.onload = function (e) {
        var base_data = e.target.result;
        if (file_size > 300 * 1024) {    //图片大于300k需要base64压缩上传
            doPic("1", base_data);

        } else {
            doPic(undefined, base_data);
        }
    }
}

var img_data;
function doPic(is_need_ya, base_data) {
    var result_data = base_data.split(",")[1];


    var loading_html = template('loading_template', {});
    MMDChat.alertFunc("发送图片", loading_html, '<button class="chat-default-btn">发送</button>');
    if (is_need_ya) {
        //需要压缩
        result_data = reduceImg(base_data)
    }
    img_data = result_data;
    send_img()
}

var img_fail_index = 0;
function send_img() {
    sdk.Plugin.Image.upload({
        base64Img: img_data,
        ext: 'jpeg',
        timeout: 20000,
        success: function (data) {
            console_func('upload base64ed success', data.data.url);
            img_fail_index = 0;
            var pic_url = data.data.url.replace("http://","https://");

            MMDChat.alertHide();
            var content_html = '<div class="img"><img src="' + pic_url + '"></div>';
            var btn_html = '<button class="chat-default-btn chat-expand-blue" id="send_pic_btn" >发送</button>';
            MMDChat.alertFunc("发送图片", content_html, btn_html);
            $("#send_pic_btn").unbind("click").click(function () {
                var touid = $(".people_li.active").data("uid");
                send_message(touid, pic_url, MessageType.img);
            })
        },
        error: function (error) {
            console_func('upload base64d fail', error);
            //重试10次
            if (img_fail_index < 10) {
                setTimeout(function () {
                        send_img();
                        img_fail_index++;
                    }, 1000
                )
            } else {
                img_fail_index = 0;
                MMDChat.alertHide();
                MMDChat.alertFunc("图片加载失败，请重试")
            }
        }
    });
}

//h5 canvas压缩图片
function reduceImg(data) {
    var _canvas = document.createElement('canvas');
    var img = new Image();
    var _context = _canvas.getContext('2d');
    img.src = data;
    var scale = (1000 * 1024 / (img.naturalWidth * img.naturalHeight)).toString();//求压缩比例
    _canvas.width = img.naturalWidth * scale;
    _canvas.height = img.naturalHeight * scale;
    _context.drawImage(img, 0, 0, _canvas.width, _canvas.height);
    var src = _canvas.toDataURL('image/jpeg');
    var base64Img = src.split(",")[1];
    return base64Img;
}

/*====拖拽处理图片====*/

//拖进
//var chat_drag = $('body');
document.addEventListener('dragenter', function (e) {
    e.preventDefault();
}, false);

// //拖离
document.addEventListener('dragleave', function (e) {
}, false);

//拖来拖去
document.addEventListener('dragover', function (e) {
    e.preventDefault();
}, false);

//扔下
document.addEventListener('drop', function (e) {
    e.stopPropagation();
    e.preventDefault();
    var files = e.target.files || e.dataTransfer.files;
    dropHandler(files)
}, false);

var dropHandler = function (files) {
    //若无选择指定聊天对象,则ctrl+v不生效
    var people_li_html = $(".people_li.active").html();
    if (people_li_html == undefined || people_li_html == "") {
        MMDChat.alertFunc("请选择一个联系人后，再进行图片拖动");
        return;
    }
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        solveImgFile(file);
    }
};

/*====end 拖拽处理图片====*/

// demo 程序将粘贴事件绑定到 document 上
document.addEventListener("paste", function (e) {
    //若无选择指定聊天对象,则ctrl+v不生效
    var people_li_html = $(".people_li.active").html();
    if (people_li_html == undefined || people_li_html == "") {
        MMDChat.alertFunc("请选择一个联系人后，再进行图片黏贴");
        return;
    }
    var cbd = e.clipboardData;

    // 如果是不支持 直接 return
    if (!(e.clipboardData && e.clipboardData.items)) {
        return;
    }

    for (var i = 0; i < cbd.items.length; i++) {
        var item = cbd.items[i];
        if (item.kind == "file") {
            var blob = item.getAsFile();
            if (blob.size === 0) {
                return;
            }
            var reader = new FileReader();
            reader.readAsDataURL(blob);
            reader.onload = function (e) {
                var dataUrl = e.target.result;
                //图片压缩上传
                doPic(undefined, dataUrl);
            };
        }
    }
}, false);
/*===end 黏贴图片====*/

/*==== 等待聊天内容的图片加载完成,执行操作*/
function loadHistoryImg(do_func, img_obj) {
    var imgdefereds = [];
    if (img_obj == undefined) {
        img_obj = ('.chat-msg-item img');
    }
    //多张图片
    $(img_obj).each(function () {
        var dfd = $.Deferred();
        $(this).bind('load', function () {
            dfd.resolve();
        })
        if (this.complete) {
            dfd.resolve();
        }
        imgdefereds.push(dfd);
    });
    $.when.apply(null, imgdefereds).done(function () {
        do_func();
    });

}

/*==== end 等待聊天内容的图片加载完成*/


function img_show_func() {
    //图片加点击触发事件
    $(".chat-msg-item img").unbind("click").click(function () {
        MMDChat.alertImg($(this)[0].src)
    });
}
function send_message_func() {
    var touid = $(".people_li.active").data("uid");
    var message_obj = $('#new_message_area');
    var send_btn_obj = $('#send_btn');
    //自动聚焦
    message_obj.focus();
    //搜索框回车键
    message_obj.unbind('keypress').bind('keypress', function (event) {
        if ((Number(event.keyCode) == 13 || Number(event.keyCode) == 10) && !event.ctrlKey) {
            send_btn_obj.trigger('click');
            return false;
        }
        if ((Number(event.keyCode) == 13 || Number(event.keyCode) == 10) && event.ctrlKey) {
            //换行
            message_obj.val(message_obj.val() + "\n");
            return false;
        }
    });

    send_btn_obj.unbind('click').click(function () {
        var message = message_obj.val().trim();
        if (message == "") {
            console_func("not send message");
            message_obj.val('');
            return false;
        }
        if (message.length > 500) {
            MMDChat.alertFunc("抱歉，单次发送字数不能超过500字，请分批发送");
            return false;
        }
        message_obj.val('');
        //发送文字数据
        send_message(touid, message, MessageType.text);


    });
}

/*======socket 相关操作=======*/
function sendUnReadMessageToOut() {
    ConstantsMap.storage.setItem(pageKey + StorageKey.unReadMsgSum, all_un_read_sum);
}

function StoryListener() {
    var str_func = function (e) {
        console_func("storage changed:");
        console_func(e);
        var storage_key = e.key;
        var new_value = e.newValue;

        if (storage_key == undefined) {
            return false;
        }
        if (pageKey == undefined) {
            return false;
        }
        if (storage_key.indexOf(pageKey) == -1) {
            return false;
        }

        if (new_value && new_value !="undefined") {

            if (storage_key == pageKey + StorageKey.isFocus) {
                //聚焦
                alert("现在，您可以和我欢度聊天时光了~~");
                ConstantsMap.storage.removeItem(pageKey + StorageKey.isFocus);
                return false;
            }

            if (storage_key == pageKey + StorageKey.topUrl) {
                $("#top_url").val(new_value);
                ConstantsMap.storage.removeItem(pageKey + StorageKey.topUrl);
                return false;
            }
            if (storage_key == pageKey + StorageKey.chatWithUid) {
                //指定跟某人聊天
                var uid_sys_array = new_value.split(",");
                param["to_sys_id"] = uid_sys_array[1];
                chat_with_uid_func(uid_sys_array[0]);
                ConstantsMap.storage.removeItem(pageKey + StorageKey.chatWithUid);
                return false;
            }

            if (storage_key == pageKey + StorageKey.isClosed) {
                //关闭窗口
                windowClose();
                return false;
            }

        }

    };
    window.removeEventListener("storage", str_func);
    window.addEventListener("storage", str_func);
}


function expand_top_click() {
    //顶部拓展叉叉
    $('#expand_top').addClass("hidden");
    param['top_url'] = undefined;
    iframe_top_map[$(".people_li.active").data("uid")] = undefined;
    $("#top_url").val("");
}

/*========今日聊到－判断是否进行逻辑处理＝＝＝*/
function check_is_not_wish(success_do) {
    if (is_first_wish_click) {
        var first_uid = $(".people_li.active").data("uid");
        setTimeout(function () {
            var final_uid = $(".people_li.active").data("uid");
            if (final_uid != first_uid) {
                console_func("send wish list fail,the people changed");
                return false;
            }
            is_first_wish_click = false;
            var the_func = expand_bottom_iframe.window.MonkWishListFunc;
            if ($.isFunction(the_func)) {
                success_do();
            }
        }, 2000);
    } else {
        var the_func = expand_bottom_iframe.window.MonkWishListFunc;
        if ($.isFunction(the_func)) {
            success_do();
        }
    }


}
/*＝＝＝＝＝＝＝＝＝今日聊到--需求单号＝＝＝＝＝＝＝＝＝＝＝*/
var is_first_wish_click = false;
//{time_ns:time_ns,msg:msg};
//历史消息，是今日聊到的处理到处理类中
function historySaveForWish(msg_wish_list) {
    check_is_not_wish(function () {
        if (msg_wish_list == undefined || msg_wish_list.length == 0) {
            return false;
        }

        var final_list = [];
        $.each(msg_wish_list, function (i, wish_map) {
            var time_ns = wish_map['time_ns'];
            var msg = wish_map['msg'];
            if (TimeUtil.isToday(time_ns)) {
                var wish_list = getWishListFromMsg(msg);
                final_list = final_list.concat(wish_list);
            }
        });

        final_list.sort();
        final_list = $.unique(final_list);
        send_wish_final(final_list);
    });

}


function saveLOPWishNumber(message) {
    check_is_not_wish(function () {
        var result_list = getWishListFromMsg(message);
        send_wish_final(result_list);
    });
}
//识别message中的wish，返回list
function getWishListFromMsg(message) {
    //判断今日聊天有无
    var me_length = message.length;
    if (me_length < 15) {
        //需求单 号 15位
        return [];
    }

    var reg = /X[\d]{14}/g;
    var result_list = message.match(reg);
    if (result_list == undefined || result_list.length == 0) {
        return [];
    }
    return result_list;
}

//结果的result 发送给子模块
function send_wish_final(result_list) {
    if (send_wish_final == undefined || send_wish_final.length == 0) {
        return false;
    }
    var to_sys_id = $(".people_li.active").data("sys_id");
    var key = new Date().toLocaleDateString() + "-" + to_sys_id;
    var history_list = [];
    var history_list_str = window.sessionStorage.getItem(key);
    if (history_list_str  && history_list_str != "") {
        history_list = JSON.parse(history_list_str);
    }

    if (history_list == undefined) {
        history_list = [];
    }
    //存今日聊到需求单
    var wish_list = [];
    var is_change = false;
    $.each(result_list, function (i, result_str) {
        if (history_list.indexOf(result_str) > -1) {
            //缓存中存在，则不推了
            return true;
        }
        if (wish_list.indexOf(result_str) == -1) {
            wish_list.push(result_str);
            history_list.push(result_str);
            is_change = true;
        }
    });
    if (is_change) {

        console_func("推送需求单数据：");
        console_func(wish_list);

        send_wish_to_iframe(key, to_sys_id, wish_list, history_list);
    }
}

function send_wish_to_iframe(key, to_sys_id, wish_list, history_list) {
    //调用子模块的处理方式，推送过去
    var the_func = expand_bottom_iframe.window.MonkWishListFunc;
    console_func("推送需求单的func1：");
    console_func(the_func);
    if ($.isFunction(the_func)) {
        console_func("into func");
        the_func({to_sys_id: to_sys_id, wish_list: wish_list});

        window.sessionStorage.setItem(key, JSON.stringify(history_list));
        console_func("out func");
    }
}

/*＝＝＝＝＝＝＝＝＝end 今日聊到--需求单号＝＝＝＝＝＝＝＝＝＝＝*/


/*＝＝＝＝＝＝＝＝＝无用户时，引导界面＝＝＝＝＝＝＝＝＝＝＝*/
function noPeopleDo() {
    var guide_content = param['guide_content'];
    var guide_url = param['guide_url'];
    if (guide_url == undefined || guide_content == undefined) {
        return false;
    }
    var body_html = guide_content;
    var foot_html = '<button class="chat-default-btn chat-expand-blue" id="no_people_do_btn"> <span>立刻前往</span> </button>';
    foot_html += "&nbsp;&nbsp;";
    foot_html += '<button class="chat-default-btn chat-expand-blue back-grey" id="no_people_cancle"> <span>取消</span> </button>';


    MMDChat.alertFunc(body_html, undefined, foot_html);

    $("#no_people_cancle").unbind("click").click(function () {
        MMDChat.alertHide();
    });
    $("#no_people_do_btn").unbind("click").click(function () {
        MMDChat.alertHide();
        //新打开页面
        var parent = window.opener;
        if (parent == undefined) {
            var new_html = '<a id="chat_monk_parent_url_a" href="' + guide_url + '" target="_blank"></a>';
            $('body').append(new_html);
            windowClose();
            document.getElementById("chat_monk_parent_url_a").click();
        } else {
            window.opener.location.href = guide_url;
            windowClose();
        }
    });
    return false;
}
/*＝＝＝＝＝＝＝＝＝end 无用户时，引导界面＝＝＝＝＝＝＝＝＝＝＝*/
function console_func(data, data_to) {
    if (is_the_test) {
        if (data_to ) {
            console.log(data, data_to);
        } else {
            console.log(data);
        }
    }
}

//是否是同一来源，目前通过sysname区别，之后通过type区别---暂时去除
function isTheSameSysName(sysName) {
    //if (aliUid.toLowerCase().indexOf(sysName.toLowerCase()) == -1) {
    //    return false;
    //}
    //return true;
    return false;
}

//是否是自身的uid，true即是的
function isMySelf(uid) {
    //规避自己跟自己聊天
    if (uid.toLowerCase() == aliUid.toLowerCase()) {
        //clear 云旺缓存
        ConstantsMap.storage.removeItem("__WSDK__");
        //重新登录
        //initAliYunWang();
        return true;
    }

    return false;
}

//同步 根据 用户uid 获得用户信息
function getUsersByuuid(uid, success_func, failFunc) {
    $.ajax({
        url: ConstantsMap.headerUrl + "/monk/chat/getUserNameByAliUid",
        data: {uid: uid},
        async: false,
        success: function (result) {
            if (result.success) {
                success_func(result);
            } else {
                if (failFunc ) {
                    failFunc(result);
                }
            }
        }
    });
}

//背景展示
function backgroundReady() {
    $.backstretch([
        ConstantsMap.headerUrl + "/img/bg/1.jpg",
        ConstantsMap.headerUrl + "/img/bg/2.jpg",
        ConstantsMap.headerUrl + "/img/bg/3.jpg",
        ConstantsMap.headerUrl + "/img/bg/4.jpg"
    ], {
        fade: 1000,
        duration: 20000
    });

}

//滚到聊天框底部
function to_chat_bottom() {
    //聊天框的顶部
    var container = $('#chat_main_div'),
        scrollTo = $('.chat_msg_wrap:last');
    if (scrollTo.offset() == undefined) {
        return;
    }
    $(".chat_main").animate({
        scrollTop: scrollTo.offset().top - container.offset().top + container.scrollTop() + 300 //让body的scrollTop等于pos的top，就实现了滚动
    }, 1);
}
//是否是聊天框底部
function is_footer_bottom() {
    //todo
    var footer_top = $('.chat_msg_wrap:last').offset();
    var the_chat_height = $(".chat_main").height() - 4;

    //if(footer_top  && Number(footer_top.top) == Number(the_chat_height)){
    //    return true;
    //}
    //return false;
    return true;
}

function to_chat_more(obj) {
    var scroll_height = 1;
    if (obj ) {
        scroll_height = $(obj).offset().top - 300;
    }
    //滚动到底部
    //$(".chat_main").scrollTop(scroll_height);
    $(".chat_main").animate({
        scrollTop: scroll_height
    }, 1);
}


/*===给子模块使用====*/
function alertForChild(heard_title, content_html, foot_html) {
    MMDChat.alertFunc(heard_title, content_html, foot_html);
}
function alertHideForChild() {
    MMDChat.alertHide();
}
function alertImgForChlid(src) {
    MMDChat.alertImg(src)
}
function alertImgHideForChlid(src) {
    MMDChat.alertImgHide();
}
