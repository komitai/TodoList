package com.example.todo;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


/**
 * Servlet implementation class TodoServlet
 */
@WebServlet("/todo")
public class TodoServlet extends HttpServlet{
	private TodoDAO todoDAO = new TodoDAO();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		//Todoリストを取得
		List<Todo> todoList = todoDAO.getAllTodos();
		
		//デバッグ：取得できた件数を表示
		System.out.println("デバッグ：取得したTodo件数 = " + (todoList != null ? todoList.size() : "null"));
		
		//リストをリクエストスコープに設定
		request.setAttribute("todos", todoList);
		
		//index.jspにフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession(); //セッション取得
		String action = request.getParameter("action");
		String title = request.getParameter("title");
		String updateIdStr = request.getParameter("update_id");
		String updateAtStr = request.getParameter("update_at");
		String deleteIdStr = request.getParameter("delete_id");
		
		//削除処理
		if(deleteIdStr != null && !deleteIdStr.isEmpty()) {
			int deleteId = Integer.parseInt(deleteIdStr);
			todoDAO.deleteTodo(deleteId);
			session.setAttribute("message", "削除が完了しました。"); //削除メッセージをセット
			response.sendRedirect(request.getContextPath() + "/todo");
			return;
		}
		
		//追加処理
		if ("add".equals(action) && title != null && !title.trim().isEmpty()) {
			String dueDateStr = request.getParameter("due_date");
		    java.sql.Date dueDate = null;

		    if (dueDateStr != null && !dueDateStr.isEmpty()) {
		        dueDate = java.sql.Date.valueOf(dueDateStr);
		    }
		    
			todoDAO.addTodo(title, dueDate);
			session.setAttribute("message", "追加が完了しました。"); //追加メッセージをセット
			response.sendRedirect(request.getContextPath() + "/todo");
			return;
		}
		
		//更新処理
		if ("update".equals(action) && updateIdStr != null && !updateIdStr.isEmpty()) {
			int updateId = Integer.parseInt(updateIdStr);
			Timestamp updateAt = Timestamp.valueOf(updateAtStr); // update_at を比較
			
			String dueDateStr = request.getParameter("due_date");
		    java.sql.Date dueDate = null;
		    if (dueDateStr != null && !dueDateStr.isEmpty()) {
		        dueDate = java.sql.Date.valueOf(dueDateStr);
		    }
			
			int result = todoDAO.updateTodo(updateId, title, dueDate, updateAt);
			
			if (result == 0) {
				//排他エラー → 成功メッセージをクリア
				session.setAttribute("errorMessage", "他のユーザーがデータを更新しました。最新の情報を取得してください。");
				session.removeAttribute("successMessage"); //成功メッセージを削除
			} else {
				//正常更新 → エラーメッセージをクリア
				session.setAttribute("message", "更新が完了しました。");
				session.removeAttribute("errorMessage"); //エラーメッセージを削除
			}
			
			response.sendRedirect(request.getContextPath() + "/todo");
			return;
		}

		//エラー処理
		session.setAttribute("errorMessage", "入力値が不正です。");
		session.removeAttribute("successMessage"); //成功メッセージを削除
		response.sendRedirect(request.getContextPath() + "/todo");
	}
	
	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}
}

