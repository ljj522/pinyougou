<html>
<head>
    <title>demo</title>
    <meta charset="UTF-8">
</head>
<body>
<#include "head.ftl">
<#--我是一个注释，不会输出-->
<!--html注释-->

${name},你好。${message}<br>
<#assign linkman='周孝正'>
${linkman}<br>

<#if success=true>
    你已通过实名认证
<#else >
你未通过实名认证
</#if><br>

----商品列表----<br>
<#list goodsList as goods>
    ${goods_index+1}.    商品名称：${goods.name} 商品价格${goods.price}<br>
</#list>
一共${goodsList?size}条记录<br>

<#assign text="{'bank':'工商银行','account':'12344557'}"/>
<#assign data=text?eval/>
开户行：${data.bank}  账户：${data.account}<br>

当前日期：${today?date}<br>
当前时间：${today?time}<br>
当前日期+时间：${today?datetime}<br>
日期格式化：${today?string('yyyy年MM月')}<br>

当前积分：${point?c}<br>

<#if aaa??>
    aaa变量存在${aaa}<br>
<#else >
aaa变量不存在
</#if><br>

${bbb!'bbb没有被赋值'}

</body>
</html>