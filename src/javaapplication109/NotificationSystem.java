package javaapplication109;

// The NotificationSystem class implements the Singleton pattern to ensure only one instance is created
import java.util.ArrayList;
import java.util.List;

public class NotificationSystem {
    private static NotificationSystem instance;  // Holds the single instance of NotificationSystem
    private List<Observer> observers;  // List to hold all registered observers

    // Private constructor to prevent instantiation from outside the class
    private NotificationSystem() {
        observers = new ArrayList<>();  // Initializes the observers list
    }

    // Public method to get the single instance of NotificationSystem (Singleton pattern)
    public static NotificationSystem getInstance() {
        if (instance == null) {
            instance = new NotificationSystem();  // Creates the instance if it doesn't exist
        }
        return instance;  // Returns the existing instance
    }

    // Method to add an observer to the list
    public void addObserver(Observer observer) {
        observers.add(observer);  // Adds the observer to the list
    }

    // Method to notify all observers when a task is added
    public void notifyTaskAdded(Task task) {
        for (Observer observer : observers) {
            observer.update("Task Added: " + task.getName());  // Notifies each observer with the task's name
        }
    }

    // Method to notify all observers when a task is removed
    public void notifyTaskRemoved(Task task) {
        for (Observer observer : observers) {
            observer.update("Task Removed: " + task.getName());  // Notifies each observer with the task's name
        }
    }
}
