package io.github.ifhox.taskmanager;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskManager{

    ArrayList<Task> listOfTasks = new ArrayList<Task>();

    public void addTask(Task task){
        listOfTasks.add(task);
    }

    public void setListOfTasks(ArrayList<Task> listOfTasks){
        this.listOfTasks = listOfTasks;
    }

    public void removeTask(Task task){
        listOfTasks.remove(task);
    }

    public void updateTask(Task task, String newTitle, Task.Priority newPriority, Task.Status newStatus, LocalDate newDate){
        task.setTitle(newTitle);
        task.setPriority(newPriority);
        task.setStatus(newStatus);
        task.setDueDate(newDate);
    }

    public boolean idExists(int id) {
        for (Task task : listOfTasks) {
            if (task.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Task> getTask(){
        return new ArrayList<Task>(listOfTasks);
    }

    public Task getTaskByID(int id){
        for(Task task : listOfTasks){
            if(task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public ArrayList<Task> filterByStatus(Task.Status status){
        ArrayList<Task> resultS = new ArrayList<Task>();
        for (Task listOfTask : listOfTasks) {
            if (listOfTask.getStatus().equals(status)) {
                resultS.add(listOfTask);
            }
        }
        return resultS;
    }

    public ArrayList<Task> filterByPriority(Task.Priority priority){
        ArrayList<Task> resultP = new ArrayList<Task>();
        for (Task listOfTask : listOfTasks) {
            if (listOfTask.getPriority().equals(priority)) {
                resultP.add(listOfTask);
            }
        }
        return resultP;
    }

    public void markComplete(Task task){
        task.setStatus(Task.Status.COMPLETE);
    }

}
