package com.pomodoro.data;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;
import java.util.List;

@Table(name = "Tasks")
public class Task {
	
	enum Priority {HiGH, LOW, MEDIAN};

    @Column(name = "name")
	private String name;

    @Column(name = "createTime")
	private Date creatTime;

    @Column(name = "due")
	private Date due;

    @Column(name = "unfinished")
	private int unfinished = 0;

    @Column(name = "finished")
	private int finished = 0;

    @Column(name = "Note")
	private Note note;

    @Column(name = "TaskType")
	private TaskType type;

	private List<String> keywords;
	private int priority;
	
	public Task() {
		
	}
	
	public Task(String name) {
		this.name = name;
	}
	
	public Task(String name, TaskType type) {
		
	}
	
	public String getName() {
		return name;
	}
	
}
