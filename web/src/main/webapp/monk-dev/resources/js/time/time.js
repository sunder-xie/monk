//处理时间的类

var TimeUtil = function(){
  return{
      //是否是今天
      getTimeStringForPeople:function(ns){
          var today = new Date();
          var company = today;
          if(ns != undefined){
              if((ns+"").length != 13){
                  ns = parseInt(ns) * 1000;
              }
              company = new Date(Number(ns));
          }
          var todayYear = today.getFullYear();

          var year = company.getFullYear();
          var month = company.getMonth();
          var day = company.getDate();
          var hour = company.getHours();
          var minute = company.getMinutes();
          //返回 11：53

          if((hour+"").length == 1){
              hour = "0"+hour;
          }
          if((minute+"").length == 1){
              minute = "0"+minute;
          }

          if(todayYear == year && today.getMonth() == month && today.getDate() == day){
              //返回 11：53
              return hour+":"+minute;
          }

          //返回日期 03-18
          var true_month = month+1;
          if (true_month < 10){
              //加个 0
              true_month = "0"+true_month;
          }

          if(todayYear != year){
              //返回 2014
              return year+"-"+true_month;
          }else{

              if (day < 10){
                  //加个 0
                  day = "0"+day;
              }
              return true_month+"-"+day;
          }
      },
      getTimeForChat:function(ns){
          var today = new Date();
          var todayYear = today.getFullYear();

          var company = today;
          if(ns != undefined){
              if((ns+"").length != 13){
                  ns = parseInt(ns) * 1000;
              }
              company = new Date(parseInt(ns));
          }

          var year = company.getFullYear();
          var month = company.getMonth();
          var day = company.getDate();
          var hour = company.getHours();
          var minute = company.getMinutes();

          //返回 11：53
          if((hour+"").length == 1){
              hour = "0"+hour;
          }
          if((minute+"").length == 1){
              minute = "0"+minute;
          }
          if(todayYear == year && today.getMonth() == month && today.getDate() == day){

              return hour+":"+minute;
          }

          month += 1;

          if (month < 10){
              //加个 0
              month = "0"+month;
          }
          if (day < 10){
              //加个 0
              day = "0"+day;
          }

          if(todayYear != year){
              //返回 2014
              return year+"-"+month+"-"+day+" "+ hour+":"+minute;
          }else{
              //返回日期 03-18
              return month+"-"+day+" "+ hour+":"+minute;
          }

      },
      isToday:function(ns){
          //是否是今天
          var today = new Date();
          var company = today;
          if(ns != undefined){
              if((ns+"").length != 13){
                  ns = parseInt(ns) * 1000;
              }
              company = new Date(parseInt(ns));
          }
          var year = company.getFullYear();
          var month = company.getMonth();
          var day = company.getDate();
          if(today.getFullYear() == year && today.getMonth() == month && today.getDate() == day){
              return true;
          }
          return false;

      },
      get13Time:function(ns){
          if((ns+"").length != 13){
              ns = parseInt(ns) * 1000;
          }
          return Number(ns);
      }
  }
}();