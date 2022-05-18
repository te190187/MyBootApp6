<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>Index</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
    <h1>Index Page</h1>
    <p>this is sample page for Spring Boot!</p>
    <p>${msg}</p>
    <form method="post" action="/post">
        <table>
            <tr><td>ID:</td><td><input type="text" name="id" /></td></tr>
            <tr><td>タイトル:</td><td><input type="text" name="title" /></td></tr>
            <tr><td>著者:</td><td><input type="text" name="writter" /></td></tr>
            <tr><td>出版社:</td><td><input type="text" name="publisher" /></td></tr>
            <tr><td>価格:</td><td><input type="text" name="price" /></td></tr>
        </table>
        <input type="submit" />
    </form>
</body>
</html>