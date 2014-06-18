package com.pomodoro.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "Tasks")
public class Task extends Model implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        public Task createFromParcel(Parcel in) {
            return Task.getTaskByCreateTimeString(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    private static Task getTaskByCreateTimeString(Parcel parcel) {
       long time2long = parcel.readLong();
       List<Task> tasks = new Select().from(Task.class).where("CreateTime=?", time2long).execute();
       return tasks.get(0);

    }

    public Date getCreateTime() {
        return new Date(createTime);
    }

    enum Priority {HiGH, LOW, MEDIAN};

    @Column(name = "Name")
	private String name;

    @Column(name = "CreateTime",unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long createTime;

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
        createTime = new Date().getTime();
    }
	
	public Task(String name, TaskType type) {
		super();
	}
	
	public String getName() {
		return name;
	}

    public int getFinishedPomo() {return finished;}

    public static List<Task> getAll() {
        return new Select().from(Task.class)
                .orderBy("CreateTime DESC")
                .execute();

    }
}
