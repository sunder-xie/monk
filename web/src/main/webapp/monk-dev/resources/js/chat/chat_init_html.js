var HtmlUtil = {
    section_html: '    <input type="hidden" class="init_str" id="aliuid" value="">' +
    '    <input type="hidden" class="init_str" id="top_url" value="">' +
    '<section class="main_chat_section"> ' +
    '<div class="chat_people"> ' +
        '<div class="header"> ' +
            '<i aria-hidden="true" class="icon-users"> </i> ' +
            '<span > 联系人列表</span> ' +
        '</div> ' +
        '<div class="search_div">' +
            '<i class="fa fa-search"></i>' +
            '<i class="right fa fa-times-circle-o hidden" id="clear_search_people_input"></i>' +
            '<input type="text" id="search_people" placeholder="请输入要查找的用户名">' +
        '</div>' +
        '<ul class="nav people_ul"> ' +

        '</ul> ' +
    '</div> ' +
    '<div class="welcome_content"> ' +
    '<div class="main_wel"> ' +
    '<div> ' +
    '<img src=' + 'ConstantsMap.headerUrl' + '/img/logo/im_512_512.png> ' +
    '<span> 欢迎使用 淘汽车信</span> ' +
    '</div> ' +


    '</div> ' +
    '</div> ' +
    '<div class="chat_content hidden"> ' +
    '<div class="header"> ' +
    '<span id="chat_content_header"> ' + '</span> ' +
    '</div> ' +
    '<div class="chat_main"> ' +

    '<div id="loading"> ' + '</div> ' +
    '<div id="chat_main_div"> ' +
    '</div> ' +
    '</div> ' +
    '<div class="chat_input"> ' +
    '<div class="chat_input_div"> ' +
    '<div class="chat_input_util"> ' +
    '<span> ' +
    '<i class="fa fa-cut" id="cut_util"> ' + '</i> ' +
    '</span> ' +
    '<span class="file_input"> ' +
    '<i aria-hidden="true" class="icon-picture"> ' + '</i> ' +

    '<input type="file" name="pic_util" id="pic_util" multiple=""> ' +
    '</span> ' +
    '</div> ' +
    '<textarea id="new_message_area" placeholder="换行按Ctrl+Enter,发送消息按Enter,请输入..."> ' + '</textarea> ' +
    '</div> ' +

    '<button class="btn btn-blue" id="send_btn"> ' +
    '<span> 发送</span> ' +
    '</button> ' +
    '</div> ' +

    '<div id="expand_bottom" class="expand_bottom"> ' +
    '<button class="btn chat-expand-blue expand_btn"> ' +
    '<span class="expand_bottom_name"> 拓展</span> ' +
    '<i class="fa fa-angle-up"> ' + '</i> ' +
    '</button> ' +
    '</div> ' +
    '<div id="expand_bottom_close" class="expand_bottom expand_bottom_close hidden"> ' +
    '<button class="btn  expand_btn_close"> ' +
    '<i class="fa fa-angle-down"> ' + '</i> ' +
    '<span class="expand_bottom_name"> 拓展</span> ' +
    '</button> ' +
    '</div> ' +
    '<div style="display: none;" id="expand_main"> ' +
    '<iframe width="100%" style="" frameborder="no" scrolling=auto border=0 src="" id="expand_bottom_iframe" name="expand_bottom_iframe"> ' + '</iframe> ' +
    '</div> ' +
    '</div> ' +
    '</section> ' +
    '<p class="monk_chat_copyright">' +
    ' <span>© 2014 - 2016 Tqmall</span>' +
    '<span class="sep"></span>' +
    '<span>ModelData</span>' +
    '</p>',
    modal_html: '<div id="monk_alert_model" class="monk_chat_modal modal fade modal-scroll" tabindex="-1" data-keyboard="false" aria-hidden="true"> ' +
    '<a href="#" class="model-close btn red" data-dismiss="modal" aria-hidden="true"> ' +
    '<i class="fa fa-times"> ' + '</i> ' +
    '</a> ' +

    '<div class="modal-section"> ' +
    '<div class="modal-header" > ' +
    '<h4 class="modal-title"> </h4> ' +
    '</div> ' +
    '<div class="modal-body"> ' +
    '<div class="cut_show">' + '<div class="title"><i class="fa  fa-quote-left"></i>截图流程<i class="fa  fa-quote-right"></i></div>' +
    '<div><span>使用QQ等截图工具截取需要区域(可快捷键)</span><i class="fa fa-arrow-down"></i></div>'
    + '<div><img  src="ConstantsMap.headerUrl/img/cut/choose_cut.jpg"></div>'
    + '<div><span>确定截图页面</span><i class="fa fa-arrow-down"></i></div>'
    + '<div><img src="ConstantsMap.headerUrl/img/cut/cut.jpg"></div>'
    + '<div><span>鼠标点击锁定车信聊天页面</span><i class="fa fa-arrow-down"></i></div>'
    + '<div><img src="ConstantsMap.headerUrl/img/cut/focus_chat.jpg"></div>'
    + '<div><span>Ctrl+C(window)/Command+C(Mac)黏贴图片</span><i class="fa fa-check"></i></div>'
    + '</div>' +
    '</div> ' +
    '<div class="modal-footer" style="overflow-x: hidden;"> ' +
    '<button class="btn"> 发送</button> ' +
    '</div> ' +
    '</div> ' +

    '</div> ' +

    '<div id="monk_pic_model" class="monk_chat_modal modal fade modal-scroll modal_img" tabindex="-1" data-keyboard="false" aria-hidden="true"> ' +
    '<a href="#" class="model-close btn red" data-dismiss="modal" aria-hidden="true"> ' +
    '<i class="fa fa-times"> ' + '</i> ' +
    '</a> ' +
    '<div class="modal-section"> ' +
    '<div class="modal-body"> ' +
    '<img id="model_img" src=""> ' +
    '</div> ' +
    '</div> ' +
    '<div class="modal-footer" id="img_foot" style="overflow-x: hidden;"> ' +
    '<button class="btn"> 发送</button> ' +
    '</div> ' +

    '</div> ',
    template_html: '<script id="loading_template" type="text/html"> ' +
    '<div class="loading-message "> ' + '<div class="block-spinner-bar"> ' + '<div class="bounce1"> ' + '</div> ' + '<div class="bounce2"> ' + '</div> ' + '<div class="bounce3"> ' + '</div> ' + '</div> ' + '</div> ' +
    '</script> ' +

    '<script id="people_li_template" type="text/html"> ' +
    '{{each list as peopleVO i}} ' +
    '<li class="people_li" data-uid="{{peopleVO.uid}}" data-sys_id="{{peopleVO.sys_id}}" data-name="{{peopleVO.name}}" data-time="{{peopleVO.time_ns}}"> ' +
    '<span class="badge badge-blue">{{if peopleVO.num}}{{peopleVO.num}}{{/if}}</span> ' +
    '<span class="username"> ' +
    '{{peopleVO.nickName}} ' +
    '</span> ' +
    '<span class="final_name"> {{peopleVO.time_str}}</span> ' +
    '</li> ' +
    '{{/each}} ' +
    '</script> ' +


    '<script id="expand_top_template" type="text/html"> ' +
    ' <div id="expand_top" class="expand_top hidden"> ' +
    '<a href="#" class="" id="expand_top_close"> ' +
    ' <i class="fa fa-times"></i> ' +
    '</a> ' +
    '<iframe src="{{top_url}}" width="100%" style="" frameborder="no" scrolling=auto border=0 src="" id="expand_top_iframe" name="expand_top_iframe"></iframe> ' +
    '</div> ' +
    '</script> ' +

    '<script id="chat_msg_template" type="text/html"> ' +
    '{{each list as msgVO i}} ' +
    '<div data-time ={{msgVO.time_ns}} data-type={{msgVO.type}} class="chat_msg_wrap' +
    '{{if msgVO.you}} ' +
    ' chat_l "> ' + '<img class="img-circle" src="' + 'ConstantsMap.headerUrl' + '/img/users/you.png"> ' +
    '{{else}} ' +
    ' chat_r"> ' + '<img class="img-circle" src="' + 'ConstantsMap.headerUrl' + '/img/users/me.png"> ' +
    ' {{/if}} ' +
    '<div class="chat_msg"> ' +
    '<div class="chat-msg-time"> ' + '<span> {{msgVO.time_str}}</span> ' + '</div> ' +
    '<div class="chat-msg-inner"> ' +
    '<i class="chat-arr"> ' + '</i> ' +
    '<div class="chat-msg-item"> ' +
    '<pre> ' +
    '{{each msgVO.msg_array as value index}} ' +
    '{{if index != 0}} ' +
    '<br> ' +
    '{{/if}} ' +
    '{{if msgVO.type ==  0}} ' +
    '{{value}} ' +
    '{{else if msgVO.type ==  1}} ' +
    '<img src="{{value}}"> ' +
    '{{/if}} ' +
    '{{/each}} ' +
    '</pre> ' +
    '</div> ' +
    '<i class="fa fa-warning msg_help msg_error"></i>' +
    '<img  class="msg_help  msg_loading" src="ConstantsMap.headerUrl/img/other/input-spinner.gif"></img>' +
    '</div> ' +
    '</div> ' +
    '</div> ' +
    '{{/each}} ' +
    '</script> '


};

