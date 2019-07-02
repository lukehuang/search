<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<form action="/product/list" method="post">
    <table border="1">
        <input type="submit" value="查询">
        <tr>
            <td>字段</td>
            <td>
                <select name="field">
                    <option value="id">id</option>
                    <option value="name">name</option>
                    <option value="category">category</option>
                    <option value="price">price</option>
                    <option value="area">area</option>
                    <option value="code">code</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>关键词</td>
            <td><input type="text" name="keyword"></td>
        </tr>
    </table>
</form>

<table border="1">
    <tr>
        <td><a href="/product/page">新增</a></td>
        <td><a href="/product/deleteAll">全部删除</a></td>
        <td><a href="/product/batchInsert">批量插入</a></td>
    </tr>
    <tr>
        <td>id</td>
        <td>name</td>
        <td>category</td>
        <td>price</td>
        <td>area</td>
        <td>code</td>
        <td>修改</td>
        <td>删除</td>
    </tr>
    <#list pageBean.list as product>
        <tr>
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>${product.category}</td>
            <td>${product.price}</td>
            <td>${product.area}</td>
            <td>${product.code}</td>
            <td><a href="/product/page?id=${product.id}">修改</a></td>
            <td><a href="/product/delete?id=${product.id}">删除</a></td>
        </tr>
    </#list>
</table>

<div class="page">
    <span>共${pageBean.totalRecord?c}条记录 ${pageBean.currentPage?c}/${pageBean.totalPage?c}</span>
    <a href="/product/list?currentPage=1">第一页</a>
    <#list pageBean.beginPage..pageBean.endPage as i>
        <a href="/product/list?currentPage=${i}"><span>${i}</span></a>
    </#list>
    <a href="/product/list?currentPage=${pageBean.currentPage+1}">下一页</a>
    <a href="/product/list?currentPage=${pageBean.totalPage?c}">最后一页</a>
</div>

</body>
</html>