package io.github.ifhox.taskmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy. hh:mm a");
        String formattedTime = date.format(formatter);
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = new TaskManager();
        FileHandler fileHandler = new FileHandler();
        taskManager.setListOfTasks(fileHandler.loadTasks());
        int taskChosen = 0;

        System.out.println("Hello, Daniel! Welcome to your CLI Task Manager!");
        System.out.println("Today's date is: " + formattedTime + "\n");

        while (true) {
            try {
            System.out.println("1. View Tasks\n2. Add Tasks\n3. Update Tasks\n4. Mark Complete\n" +
                    "5. Remove Tasks\n6. Exit\n");
            System.out.print("What would you like to do? (1-6): ");
            String line = scanner.nextLine();
            int choice = Integer.parseInt(line.trim());

            if (choice == 1) {
                for (Task task : taskManager.getTask()) {
                    System.out.println(task);
                }
                System.out.println();
            } else if (choice == 2) {
                int id;
                String description;
                LocalDate dueDate;
                Task.Priority priority;
                Task.Status status;

                while (true) {
                    System.out.println("Enter ID: ");
                    try {
                        id = scanner.nextInt();
                        if (id <= 0) {
                            System.out.println("Must be at least 1.");
                        } else if (taskManager.idExists(id)) {
                            System.out.println("That ID already exists. Choose a different one.");
                        } else {
                            scanner.nextLine();
                            break;
                        }
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("Please add a valid ID");
                    }
                }
                while (true) {
                    System.out.println("Enter Description: ");
                    description = scanner.nextLine();
                    if (description.isEmpty()) {
                        System.out.println("Please add a description.");
                    } else {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter Due Date (yyyy-mm-dd)");
                    try {
                        dueDate = LocalDate.parse(scanner.nextLine());
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Please add a date in the accepted format: (yyyy-mm-dd)");
                    }
                }
                while (true) {
                    System.out.println("Enter Priority (LOW/MEDIUM/HIGH): ");
                    try {
                        priority = Task.Priority.valueOf(scanner.nextLine().toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid priority, please use this format: (LOW/MEDIUM/HIGH)");
                    }
                }

                while (true) {
                    System.out.println("Enter Status (PENDING/IN_PROGRESS/COMPLETE/OVERDUE): ");
                    try {
                        status = Task.Status.valueOf(scanner.nextLine().toUpperCase());
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status, please use this format: (PENDING/IN_PROGRESS/COMPLETE/OVERDUE)");
                    }
                }

                Task newTask = new Task(id, description, dueDate, priority, status);
                taskManager.addTask(newTask);
                fileHandler.saveTasks(taskManager.getTask());
            } else if (choice == 3) {
                String newDescription;
                Task.Priority newPriority;
                Task.Status newStatus;
                LocalDate newDate;

                if(taskManager.listOfTasks.isEmpty()){
                    continue;
                }

                for (Task task : taskManager.getTask()) {
                    System.out.println(task);
                }
                while (!taskManager.listOfTasks.isEmpty()) {
                    System.out.println("What task do you want to update?: ");

                    try {
                        taskChosen = scanner.nextInt();
                        if (taskChosen <= 0) {
                            System.out.println("Must be at least 1.");
                        } else if (!taskManager.idExists(taskChosen)) {
                            System.out.println("No task found with that ID.");
                        } else {
                            scanner.nextLine();
                            break;
                        }
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("Please add a valid ID");
                    }
                }

                while (true) {
                    System.out.println("New Description: ");
                    newDescription = scanner.nextLine();
                    if (newDescription.isEmpty()) {
                        newDescription = taskManager.getTaskByID(taskChosen).getTitle();
                        break;
                    } else {
                        break;
                    }
                }

                while (true) {
                    System.out.println("New Priority (LOW/MEDIUM/HIGH): ");
                    try {
                        String input = scanner.nextLine();
                        if (input.isEmpty()) {
                            newPriority = taskManager.getTaskByID(taskChosen).getPriority();
                            break;
                        } else {
                            newPriority = Task.Priority.valueOf(input.toUpperCase());
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid priority, please use this format: (LOW/MEDIUM/HIGH)");
                    }
                }

                while (true) {
                    System.out.println("Enter Status (PENDING/IN_PROGRESS/COMPLETE/OVERDUE): ");
                    try {
                        String input = scanner.nextLine();
                        if (input.isEmpty()) {
                            newStatus = taskManager.getTaskByID(taskChosen).getStatus();
                            break;
                        } else {
                            newStatus = Task.Status.valueOf(input.toUpperCase());
                        }
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status, please use this format: (PENDING/IN_PROGRESS/COMPLETE/OVERDUE)");
                    }
                }

                while (true) {
                    System.out.println("Enter Due Date (yyyy-mm-dd)");
                    try {
                        String input = scanner.nextLine();
                        if (input.isEmpty()) {
                            newDate = taskManager.getTaskByID(taskChosen).getDueDate();
                            break;
                        } else {
                            newDate = LocalDate.parse(input);
                        }
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println("Please add a date in the accepted format: (yyyy-mm-dd)");
                    }
                }

                Task task = taskManager.getTaskByID(taskChosen);
                taskManager.updateTask(task, newDescription, newPriority, newStatus, newDate);
                System.out.println("Task Successfully updated!");
                fileHandler.saveTasks(taskManager.getTask());
            } else if (choice == 4) {

                if(taskManager.listOfTasks.isEmpty()){
                    continue;
                }

                for (Task task : taskManager.getTask()) {
                    System.out.println(task);
                }
                if (taskManager.listOfTasks.isEmpty()) {
                    System.out.println("No tasks yet.");
                    continue;
                }
                while (true) {
                    System.out.println("What task do you want to mark as complete?");
                    try {
                        taskChosen = scanner.nextInt();
                        if (taskChosen <= 0) {
                            System.out.println("Must be at least 1.");
                        } else if (!taskManager.idExists(taskChosen)) {
                            System.out.println("No task found with that ID.");
                        } else {
                            scanner.nextLine();
                            break;
                        }
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("Please add a valid ID");
                    }
                }

                Task task = taskManager.getTaskByID(taskChosen);
                    taskManager.markComplete(task);
                    System.out.println("Task completed!");
                    fileHandler.saveTasks(taskManager.getTask());
            } else if (choice == 5) {

                if(taskManager.listOfTasks.isEmpty()){
                    continue;
                }

                for (Task task : taskManager.getTask()) {
                    System.out.println(task);
                }

                while (!taskManager.listOfTasks.isEmpty()) {

                    System.out.println("What task do you want to remove?");

                    try {
                        taskChosen = scanner.nextInt();
                        if (taskChosen <= 0) {
                            System.out.println("Must be at least 1.");
                        } else if (!taskManager.idExists(taskChosen)) {
                            System.out.println("No task found with that ID.");
                        } else {
                            scanner.nextLine();
                            break;
                        }
                    } catch (InputMismatchException e) {
                        scanner.nextLine();
                        System.out.println("Please add a valid ID");
                    }
                }

                Task task = taskManager.getTaskByID(taskChosen);
                taskManager.removeTask(task);
                System.out.println("Task removed!");
                fileHandler.saveTasks(taskManager.getTask());
            } else if (choice == 6) {
                System.out.println("Have a good day!");
                break;
            } else {
                System.out.println("Invalid number!");
            }

            } catch (NumberFormatException e) {
                System.out.println("Only numbers are permitted");
            } catch (NoSuchElementException e) {
                System.out.println("\nInput closed. Goodbye!");
                break;
            }
        }
    }
}