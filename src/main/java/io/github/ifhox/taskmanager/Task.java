package io.github.ifhox.taskmanager;

import java.time.LocalDate;

public class Task  {

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETE,
        OVERDUE
    }

    private int id;
    private String title;
    private LocalDate dueDate;
    private Status status;
    private Priority priority;

    public Task (int id, String title, LocalDate dueDate, Priority priority, Status status){
        this.id = id;
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String toString() {
            return this.id + ". " + this.priority + ": " + this.title + ". due on: " + this.dueDate + ". status: "
                    + getStatus();
    }
}
