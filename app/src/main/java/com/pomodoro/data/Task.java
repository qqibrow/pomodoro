package com.pomodoro.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "Tasks")
public class Task extends Model{
	
	enum Priority {HiGH, LOW, MEDIAN};

    @Column(name = "Name")
	private String name;

    @Column(name = "CreateTime")
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
		super();
	}
	
	public Task(String name) {
		super();
        this.name = name;
        creatTime = new Date();
	}
	
	public Task(String name, TaskType type) {
		super();
	}
	
	public String getName() {
		return name;
	}

    public static List<Task> getAll() {
        return new Select().from(Task.class)
                .orderBy("CreateTime DESC")
                .execute();

    }
}
