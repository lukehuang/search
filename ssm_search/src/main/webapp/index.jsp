<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h2>欢迎页</h2>
<h3><a href="/">返回欢迎页</a></h3>

<!--************************************************************分割线************************************************************-->

<!-- todo TestMapper -->
<table border="1">
    <tr>
        <td><a href="/search/property">搜索条件收集</a></td>
    </tr>
    <tr>
        <td><a href="/search/fuzzy">多重模糊搜索</a></td>
    </tr>
    <tr>
        <td><a href="/search/fuzzy2">多重模糊+条件搜索</a></td>
    </tr>
</table>

</body>
</html>
