package com.pomodoro.data;

import java.util.Date;
import java.util.List;

public class Task {
	
	enum Priority {HiGH, LOW, MEDIAN};
	
	private String name;
	private Date creatTime;
	private Date due;
	private int unfinished = 0;
	private int finished = 0;
	private Note note;
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
