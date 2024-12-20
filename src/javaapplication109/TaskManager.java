/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static TaskManager instance;
    private List<Task> tasks;
    private NotificationSystem notificationSystem;

    private TaskManager() {
        tasks = new ArrayList<>();
        notificationSystem = NotificationSystem.getInstance();
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public void addTask(Task task) {
        tasks.add(task);
        notificationSystem.notifyTaskAdded(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        notificationSystem.notifyTaskRemoved(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
