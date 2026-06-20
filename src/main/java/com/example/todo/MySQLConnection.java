package com.example.todo;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;


public class MySQLConnection {
	private static BasicDataSource dataSource = new BasicDataSource();
	
	static {
		//データベース接続情報を設定
		dataSource.setUrl("jdbc:mysql://localhost:3306/todo_db?serverTimezone=UTC");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		
		//コネクションプールの設定
		dataSource.setInitialSize(5);      //起動時に確保するコネクション数
		dataSource.setMaxTotal(50);        //プール内の最大コネクション数
		dataSource.setMaxIdle(2);          //アイドル状態で保持する最大コネクション数
		dataSource.setMinIdle(2);          //最低限保持するアイドルコネクション数（追加）
		dataSource.setMaxWaitMillis(5000); //コネクションが不足した場合の最大待機時間（追加）
	}
	
	//DataSourceを取得するメソッド
	public static DataSource getDataSource() {
		return dataSource;
	}
	
	//コネクションを取得するメソッド（追加）
	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		}catch(SQLException e) {
			//エラーメッセージをログに記録
			e.printStackTrace();
			//RuntimeExceprionに変換して、Servlet側でエラーメッセージを取得できるようにする
			throw new RuntimeException("データベース接続エラー：" + e.getMessage());
		}
	}
}
