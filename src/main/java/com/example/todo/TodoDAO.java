package com.example.todo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class TodoDAO {
	public List<Todo> getAllTodos(){
		List<Todo>todos = new ArrayList<>();
		String sql = "SELECT id, title, update_at, due_date FROM todo_items ORDER BY id ASC";
		
		try(Connection conn = MySQLConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery()){
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				Timestamp updateAt = rs.getTimestamp("update_at");
				Date dueDate = rs.getDate("due_date");
				todos.add(new Todo(id, title, updateAt, dueDate));
			}
			
			//デバッグ用：取得件数を出力
			System.out.println("デバッグ：取得したTodo件数 = " + todos.size());
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return todos;
	}
	
	public int updateTodo(int id, String title, Date dueDate, Timestamp oldUpdateAt) {
		String sql = "UPDATE todo_items SET title = ?, update_at = NOW(), due_date = ? WHERE id = ? AND update_at = ?";
		
		try(Connection conn = MySQLConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, title);
			pstmt.setDate(2, dueDate); 
			pstmt.setInt(3, id);
			pstmt.setTimestamp(4, oldUpdateAt); //更新前の'update_at'を条件にする
			
			int affectedRows = pstmt.executeUpdate(); //更新成功時は１，失敗時は０を返す
			return affectedRows;
			
		}catch(SQLException e) {
			e.printStackTrace();
			return 0; //エラー時は更新失敗（排他制御エラーを識別するため）
		}
	}
	
	public void addTodo(String title, Date dueDate) {
		String sql = "INSERT INTO todo_items(title, update_at, due_date)VALUES(?, NOW(), ?)";
		
		try(Connection conn = MySQLConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setString(1, title);
			pstmt.setDate(2, dueDate);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTodo(int id) {
		String sql = "DELETE FROM todo_items WHERE id = ?";
		
		try(Connection conn = MySQLConnection.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)){
			
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}