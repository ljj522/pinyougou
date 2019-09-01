var app=angular.module('pinyougou',[]);//自定义模块



//定义过滤器
app.filter('trustHtml',['$sce',function ($sce) {
    return function (data) {//传入参数时被过滤的内容
        return $sce.trustAsHtml(data);
    }
}]);