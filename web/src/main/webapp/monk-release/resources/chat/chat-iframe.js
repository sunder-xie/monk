var beforeChangePeople,TimIframeUtil=function(){return{alertFunc:function(n,e,t){parent.alertForChild(n,e,t)},alertHide:function(){parent.alertHideForChild()},alertImg:function(n){parent.alertImgForChlid(n)},alertImgHide:function(){parent.alertImgHideForChlid()},sendMessageToChatArea:function(n){if(n instanceof Array||console.log("非数组参数"),0!=n.length){var e=$(window.parent.document).find("#new_message_area"),t=e.val();e.val(t+n.join("\n"))}return!1},sendMessageToChatAreaAndSend:function(n){if(n instanceof Array||console.log("非数组参数"),0!=n.length){var e=$(window.parent.document).find("#new_message_area"),t=$(window.parent.document).find("#send_btn"),i=e.val();e.val(n.join("\n")),t.trigger("click"),e.val(i)}return!1},closeTopIframe:function(){parent.expand_top_click()},closeChatWindow:function(){parent.windowClose()}}}(),MonkWishListFunc,TimIframeUtilForLOP=function(){return{todayWishList:function(n){MonkWishListFunc=n}}}();"function"==typeof define&&define("TqmallImIframe",[],function(){return{IframeUtil:TimIframeUtil,TimIframeUtilForLOP:TimIframeUtilForLOP}});