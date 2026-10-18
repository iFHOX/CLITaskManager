package io.github.ifhox.taskmanager;

import java.time.LocalDate;
import java.util.Objects;

public class Task  {

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETE
    }

    private final int id;
    private String title;
    private LocalDate dueDate;
    private Status status;
    private Priority priority;

    public Task (int id, String title, LocalDate dueDate, Priority priority, Status status){
        this.id = id;
        this.title = requireValidTitle(title);
        this.dueDate = dueDate;
        this.status = Objects.requireNonNull(status, "status");
        this.priority = Objects.requireNonNull(priority, "priority");
    }

    private static String requireValidTitle(String title){
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("The task title cannot be empty");
        }
        return title.trim();
    }

    public boolean isOverdue() {
        LocalDate today = LocalDate.now();
        if(dueDate == null || !dueDate.isBefore(today)){ return false;
        } else return status != Status.COMPLETE;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = requireValidTitle(title);
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
        this.status = Objects.requireNonNull(status, "status");
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = Objects.requireNonNull(priority, "priority");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        if(!(dueDate == null)){
            return this.id + ". " + this.priority + ": " + this.title + ". due on: " + this.dueDate + ". status: "
                    + getStatus();
        } else {
            return this.id + ". " + this.priority + ": " + this.title + ". No due date. status: "
                    + getStatus();
        }
    }
}
