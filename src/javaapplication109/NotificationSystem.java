/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

import java.util.ArrayList;
import java.util.List;

public class NotificationSystem {
    private static NotificationSystem instance;
    private List<Observer> observers;

    private NotificationSystem() {
        observers = new ArrayList<>();
    }

    public static NotificationSystem getInstance() {
        if (instance == null) {
            instance = new NotificationSystem();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyTaskAdded(Task task) {
        for (Observer observer : observers) {
            observer.update("Task Added: " + task.getName());
        }
    }

    public void notifyTaskRemoved(Task task) {
        for (Observer observer : observers) {
            observer.update("Task Removed: " + task.getName());
        }
    }
}
