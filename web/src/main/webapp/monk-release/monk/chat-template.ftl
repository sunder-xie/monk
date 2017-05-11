<#--loading-->
<script id="loading_template" type="text/html">
    <div class="loading-message "><div class="block-spinner-bar"><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div></div>
</script>

<#--用户列表-->
<script id="people_li_template" type="text/html">
    {{each list as peopleVO i}}
        <li class="people_li" data-uid="{{peopleVO.uid}}" data-sysid="{{peopleVO.sysid}}" data-name="{{peopleVO.name}}" data-time="{{peopleVO.time_ns}}">
            <span class="badge badge-blue">{{if peopleVO.num}}{{peopleVO.num}}{{/if}}</span>
                <span class="username">
                    {{peopleVO.nickName}}
                </span>
            <span class="final_name">{{peopleVO.time_str}}</span>
        </li>
    {{/each}}
</script>

<#--顶部栏-->
<script id="expand_top_template" type="text/html">
    <div id="expand_top" class="expand_top hidden">
        <a href="#" class="" id="expand_top_close">
            <i class="fa fa-times"></i>
        </a>
        <iframe src="{{top_url}}" width="100%" style="" frameborder="no" scrolling=auto border=0 src="" id="expand_top_iframe" name="expand_top_iframe"></iframe>
    </div>
</script>

<#--对话内容-->
<script id="chat_msg_template" type="text/html">
    {{each list as msgVO i}}
        <div data-time ={{msgVO.time_ns}} class="chat_msg_wrap
        {{if msgVO.you}}
        chat_l "><img class="img-circle" src="/img/users/you.png">
        {{else}}
        chat_r"><img class="img-circle" src="/img/users/me.png">
        {{/if}}
            <div class="chat_msg">
                <div class="chat-msg-time"><span>{{msgVO.time_str}}</span></div>
                <div class="chat-msg-inner">
                    <i class="chat-arr"></i>
                    <div class="chat-msg-item">
                        <pre>
                            {{each msgVO.msg_array as value index}}
                                {{if index != 0}}
                                    <br>
                                {{/if}}
                                {{if msgVO.type ==  0}}
                                    {{value}}
                                {{else if msgVO.type ==  1}}
                                    <img src="{{value}}">
                                {{/if}}
                            {{/each}}
                        </pre>
                    </div>
                </div>
            </div>
        </div>
    {{/each}}
</script>
