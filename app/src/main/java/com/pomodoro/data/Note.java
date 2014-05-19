package com.pomodoro.data;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Notes")
public class Note {
    @Column(name = "Desp")
	private String description;
}
