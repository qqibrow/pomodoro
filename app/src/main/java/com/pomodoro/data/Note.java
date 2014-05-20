package com.pomodoro.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Notes")
public class Note extends Model{
    @Column(name = "Desp")
	private String description;

    public Note() {
        super();
    }
}
