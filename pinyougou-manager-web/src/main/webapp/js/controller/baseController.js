app.controller('baseController',function ($scope) {
    //分页控件配置:currenPage:当前页 totalItems:总记录数 itemsPerPage：页面大小  perPageOptions：分页选项
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }

    };
    //刷新列表
    $scope.reloadList=function(){
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };
    //用户勾选
    $scope.selectIds=[];//用户id集合
    $scope.updateSeleytion=function ($event,id) {
        if ($event.target.checked){
            $scope.selectIds.push(id);//push往集合添加id
        }else {
            var index = $scope.selectIds.indexOf(id);//查询值得为之
            $scope.selectIds.splice(index,1);//为之和个数
        }
    }

    $scope.jsonToString=function(jsonString,key){
        var jsons=JSON.parse(jsonString);//将json字符串转换为json对象
        var value="";
        for(var i=0;i<jsons.length;i++){
            if(i>0){
                value+=","
            }
            value+=jsons[i][key];
        }
        return value;
    }

});