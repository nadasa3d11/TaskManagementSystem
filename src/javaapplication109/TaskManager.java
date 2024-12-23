package javaapplication109;
// The TaskManager class is responsible for managing tasks and notifying the system of task changes
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static TaskManager instance;  // Singleton instance of TaskManager
    private List<Task> tasks;  // List to store tasks
    private NotificationSystem notificationSystem;  // Instance of the NotificationSystem to notify about task changes

    // Private constructor to ensure only one instance of TaskManager is created (Singleton pattern)
    private TaskManager() {
        tasks = new ArrayList<>();  // Initializes the tasks list
        notificationSystem = NotificationSystem.getInstance();  // Gets the instance of the NotificationSystem
    }

    // Static method to get the instance of TaskManager (Singleton pattern)
    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();  // Creates a new instance if none exists
        }
        return instance;  // Returns the single instance of TaskManager
    }

    // Method to add a task to the TaskManager
    public void addTask(Task task) {
        tasks.add(task);  // Adds the task to the list
        notificationSystem.notifyTaskAdded(task);  // Notifies the system about the task addition
    }

    // Method to remove a task from the TaskManager
    public void removeTask(Task task) {
        tasks.remove(task);  // Removes the task from the list
        notificationSystem.notifyTaskRemoved(task);  // Notifies the system about the task removal
    }

    // Method to get the list of all tasks
    public List<Task> getTasks() {
        return tasks;  // Returns the list of tasks
    }
}