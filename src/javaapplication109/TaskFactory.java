/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

import java.time.LocalDateTime;
import java.util.List;

public class TaskFactory {
    public static Task createTask(String taskType, String name, String description, LocalDateTime addDate, LocalDateTime deadline, List<User> responsibleUsers) {
        switch (taskType.toLowerCase()) {
            case "bug":
                return new BugTask(name, description, addDate, deadline, responsibleUsers);
            case "feature":
                return new FeatureTask(name, description, addDate, deadline, responsibleUsers);
            case "improvement":
                return new ImprovementTask(name, description, addDate, deadline, responsibleUsers);
            default:
                throw new IllegalArgumentException("Unknown task type: " + taskType);
        }
    }
}
