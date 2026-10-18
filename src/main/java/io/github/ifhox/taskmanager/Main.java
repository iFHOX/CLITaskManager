package io.github.ifhox.taskmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Optional;
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


        System.out.println("Hello, Daniel! Welcome to your CLI Task Manager!");
        System.out.println("Today's date is: " + formattedTime + "\n");

        while (true) {
            try {
                System.out.println("""
                        1. View Tasks
                        2. Add Tasks
                        3. Update Tasks
                        4. Mark Complete
                        5. Remove Tasks
                        6. Exit
                        """);
                System.out.print("What would you like to do? (1-6): ");
                String line = scanner.nextLine();
                int choice = Integer.parseInt(line.trim());

                if (choice == 1) {
                    if (taskManager.listOfTasks.isEmpty()) {
                        System.out.println("No tasks in the list! Returning to the menu");
                        continue;
                    }
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
                        if (description.isBlank()) {
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


                    Task newTask = new Task(id, description, dueDate, priority,status);
                    taskManager.addTask(newTask);
                    fileHandler.saveTasks(taskManager.getTask());
                } else if (choice == 3) {
                    String newDescription;
                    Task.Priority newPriority;
                    Task.Status newStatus;
                    LocalDate newDate;

                    if (taskManager.listOfTasks.isEmpty()) {
                        System.out.println("No tasks in the list! Returning to the menu");
                        continue;
                    }

                    for (Task task : taskManager.getTask()) {
                        System.out.println(task);
                    }
                    Task selected;
                    while (true) {
                        System.out.println("What task do you want to update?: ");
                        try {
                            int taskChosen = scanner.nextInt();
                            scanner.nextLine();
                            Optional<Task> found = taskManager.getTaskByID(taskChosen);
                            if (found.isPresent()) {
                                selected = found.get();
                                break;
                            }
                            System.out.println("No task found with that ID.");
                        } catch (InputMismatchException e) {
                            scanner.nextLine();
                            System.out.println("Please add a valid ID");
                        }
                    }
                    System.out.println("New Description: (Current: " + selected.getTitle() + "). Enter to Keep");
                    newDescription = scanner.nextLine();
                    if (newDescription.isBlank()) {
                        newDescription = selected.getTitle();
                    }

                    while (true) {

                        System.out.println("New Priority (LOW/MEDIUM/HIGH): ");
                        try {
                            String input = scanner.nextLine();
                            if (input.isBlank()) {
                                newPriority = selected.getPriority();
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
                        System.out.println("Enter Status (PENDING/IN_PROGRESS/COMPLETE): ");
                        try {
                            String input = scanner.nextLine();
                            if (input.isBlank()) {
                                newStatus = selected.getStatus();
                                break;
                            } else {
                                newStatus = Task.Status.valueOf(input.toUpperCase());
                            }
                            break;
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid status, please use this format: (PENDING/IN_PROGRESS/COMPLETE)");
                        }
                    }

                    while (true) {
                        System.out.println("Enter Due Date (yyyy-mm-dd)");
                        try {
                            String input = scanner.nextLine();
                            if (input.isBlank()) {
                                newDate = selected.getDueDate();
                                break;
                            } else {
                                newDate = LocalDate.parse(input);
                            }
                            break;
                        } catch (DateTimeParseException e) {
                            System.out.println("Please add a date in the accepted format: (yyyy-mm-dd)");
                        }
                    }

                    taskManager.updateTask(selected, newDescription, newPriority, newStatus, newDate);
                    System.out.println("Task Successfully updated!");
                    fileHandler.saveTasks(taskManager.getTask());
                } else if (choice == 4) {

                    if (taskManager.listOfTasks.isEmpty()) {
                        System.out.println("No tasks in the list! Returning to the menu");
                        continue;
                    }

                    for (Task task : taskManager.getTask()) {
                        System.out.println(task);
                    }

                    Task selected;
                    while (true) {
                        System.out.println("What task do you want to mark as complete?");
                        try {
                            int taskChosen = scanner.nextInt();
                            scanner.nextLine();
                            Optional<Task> found = taskManager.getTaskByID(taskChosen);
                            if (found.isPresent()) {
                                selected = found.get();
                                break;
                            }
                            System.out.println("No task found with that ID.");
                        } catch (InputMismatchException e) {
                            scanner.nextLine();
                            System.out.println("Please add a valid ID");
                        }
                    }

                    taskManager.markComplete(selected);
                    if(!selected.getStatus().equals(Task.Status.COMPLETE)){
                        System.out.println("Task is not completed");
                    } else {
                        System.out.println("Task marked as completed!");
                    }
                    fileHandler.saveTasks(taskManager.getTask());
                } else if (choice == 5) {

                    if (taskManager.listOfTasks.isEmpty()) {
                        System.out.println("No tasks in the list! Returning to the menu");
                        continue;
                    }

                    for (Task task : taskManager.getTask()) {
                        System.out.println(task);
                    }
                    Task selected;
                    while (true) {
                        System.out.println("What task do you want to remove?");

                        try {
                            int taskChosen = scanner.nextInt();
                            scanner.nextLine();
                            Optional<Task> found = taskManager.getTaskByID(taskChosen);
                            if (found.isPresent()) {
                                selected = found.get();
                                break;
                            }
                            System.out.println("No task found with that ID.");
                        } catch (InputMismatchException e) {
                            scanner.nextLine();
                            System.out.println("Please add a valid ID");
                        }
                    }

                    taskManager.removeTask(selected);
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