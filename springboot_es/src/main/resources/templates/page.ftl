<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<form action="/product/insert" method="post">
    <input type="submit" value="提交">
    <table border="1">
        <tr>
            <td>id</td>
            <td><input type="text" name="id">${product.id}</td>
        </tr>
        <tr>
            <td>name</td>
            <td><input type="text" name="name">${product.name}</td>
        </tr>
        <tr>
            <td>category</td>
            <td><input type="text" name="category">${product.category}</td>
        </tr>
        <tr>
            <td>price</td>
            <td><input type="text" name="price">${product.price}</td>
        </tr>
        <tr>
            <td>area</td>
            <td><input type="text" name="area">${product.area}</td>
        </tr>
        <tr>
            <td>code</td>
            <td><input type="text" name="code">${product.name}</td>
        </tr>
    </table>
</form>

</body>
</html>