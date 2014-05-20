package com.pomodoro.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "TaskTypes")
public class TaskType extends Model{
    @Column(name = "name")
    private String name;

    public TaskType() {
        super();
    }
}
