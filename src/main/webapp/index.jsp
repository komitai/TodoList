<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.todo.Todo" %>

<html>
<head>
	<title>Todoリスト</title>
	<style>
		.error{color: red; font-weight: bold;}
		.success{color: green; font-weight: bold;}
	</style>
</head>
<body>

<h1>Todoリスト</h1>

<%-- メッセージの表示処理 --%>
<%
	String errorMessage = (String) session.getAttribute("errorMessage");
	String successMessage = (String) session.getAttribute("message");
	if (errorMessage != null) { 
%>
	<p class="error"><%= errorMessage %></p>
<%
		session.removeAttribute("errorMessage"); //表示後に削除
	}
	
	if (successMessage != null && errorMessage == null) { //エラーメッセージがない場合のみ表示
%>
	<p class="success"><%= successMessage %></p>
<%
		session.removeAttribute("message"); //表示後に削除
	}
%>

<%-- Todoの追加フォーム --%>
<form action="todo" method="post">
	<input type="hidden" name="action" value="add">
	<input type="text" name="title" placeholder="タイトル" required>
    <input type="date" name="due_date">
	<button type="submit">追加</button>
</form>

<%-- Todoリストの表示 --%>
<ul>
<%
	List<Todo> todos = (List<Todo>) request.getAttribute("todos");
	if (todos != null && !todos.isEmpty()) {
		for (Todo todo : todos) {
%>
		<li>
			<form action="todo" method="post" style="display:inline;">
				<input type="hidden" name="action" value="update">
				<input type="hidden" name="update_id" value="<%= todo.getId() %>">
				<input type="text" name="title" value="<%= todo.getTitle() %>" required>
				<input type="date" name="due_date" value="<%= todo.getDueDate() %>">
				<input type="hidden" name="update_at" value="<%= todo.getUpdateAt() %>">
				<button type="submit">更新</button>
			</form>
			<form action="todo" method="post" style="display:inline;">
				<input type="hidden" name="delete_id" value="<%= todo.getId() %>">
				<button type="submit">削除</button>
			</form>
		</li>
<%
		}
	} else {
%>
		<p>現在、登録されているTodoはありません。</p>
<%
	}
%>
</ul>

</body>
</html>