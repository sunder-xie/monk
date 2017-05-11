/**
 * Created by huangzhangting on 16/1/30.
 */
function testOne(){
    alert('one one');

}

$(function () {

    $('#test_btn').click(function() {
        var loadingEl = '#part_div';
        EPC.blockUI(loadingEl);

        window.setTimeout(function() {
            EPC.unblockUI(loadingEl);
        }, 2000);
    });
    $('#test1_btn').click(function() {
        EPC.blockUI();

        window.setTimeout(function() {
            EPC.unblockUI();
        }, 2000);
    });

    $('#test_alert').click(function() {
        EPC.alertFuc("1231","header","1000");
        return false;
    });
    $('#test_confirm').click(function() {
        EPC.confirmFuc("1231",okFunction);
    });




});


function okFunction(){
    EPC.alertFuc("alert ok");
}