package com.example.todo;

import java.sql.Date;
import java.sql.Timestamp;

public class Todo{
	private int id;
	private String title;
	private Timestamp updateAt; //更新時刻を管理
	private Date dueDate;
	
	//コンストラクタ
	public Todo(int id, String title, Timestamp updateAt, Date dueDate) {
		this.id = id;
		this.title = title;
		this.updateAt = updateAt;
		this.dueDate = dueDate;
	}
		
	//ゲッター（データの取得用）
	public int getId() {
		return id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public Timestamp getUpdateAt() { //更新時刻を取得
		return updateAt;
	}
	public Date getDueDate() {
        return dueDate;
    }
	
	//セッター
	public void setUpdateAt(Timestamp updateAt) { //更新時刻をセット
		this.updateAt = updateAt;
	}
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
