
var NotifyUtil = function () {
    var notify_time = 3000;
  return{
        sendNotify:function(title,content) {
            if(!title && !content){
                title = "桌面提醒";
                content = "您看到此条信息桌面提醒设置成功";
            }

            var iconUrl = ConstantsMap.headerUrl+"/img/logo/im_80_80.png";


            var voice_html = '<audio id="im_audio_mp3" autoplay="autoplay"><source src="'+ConstantsMap.headerUrl+'/img/taoqi.mp3" /></audio>';
            $("body").append(voice_html);
            setTimeout(function(){
                $("#im_audio_mp3").remove();
            },1500);


            if (window.webkitNotifications) {
                //chrome老版本
                if (window.webkitNotifications.checkPermission() == 0) {
                    var notif = window.webkitNotifications.createNotification(iconUrl, title, content);
                    notif.display = function() {}
                    notif.onerror = function() {}
                    notif.onclose = function() {}
                    notif.onclick = function() {this.cancel();}
                    notif.replaceId = 'Meteoric';
                    notif.show();
                } else {
                    window.webkitNotifications.requestPermission(notify);
                }
            }
            else if("Notification" in window){
                // 判断是否有权限
                if (Notification.permission === "granted") {
                    var notification = new Notification(title, {
                        "icon": iconUrl,
                        "body": content,
                    });

                    notification.onshow = function(){
                        setTimeout(function(){
                            notification.close();
                        }, notify_time);
                    };

                }
                //如果没权限，则请求权限
                else if (Notification.permission !== 'denied') {
                    Notification.requestPermission(function(permission) {
                        // Whatever the user answers, we make sure we store the
                        // information
                        if (!('permission' in Notification)) {
                            Notification.permission = permission;
                        }
                        //如果接受请求
                        if (permission === "granted") {
                            var notification = new Notification(title, {
                                "icon": iconUrl,
                                "body": content
                            });
                            notification.onshow = function(){
                                setTimeout(function(){
                                    notification.close();
                                }, notify_time);
                            };
                        }
                    });
                }
            }
        }
    }
}()