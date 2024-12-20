/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Task {
    private String name;
    private String description;
    private LocalDateTime addDate;
    private LocalDateTime deadline;
    private LocalDateTime completedDate;
    private String status;
    private List<User> responsibleUsers;

    public Task(String name, String description, LocalDateTime addDate, LocalDateTime deadline, List<User> responsibleUsers) {
        this.name = name;
        this.description = description;
        this.addDate = addDate;
        this.deadline = deadline;
        this.responsibleUsers = responsibleUsers;
        this.status = "Pending";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getAddDate() {
        return addDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public String getStatus() {
        return status;
    }

    public List<User> getResponsibleUsers() {
        return responsibleUsers;
    }

    public void setStatus(String status) {
        this.status = status;
        if ("Complete".equals(status)) {
            this.completedDate = LocalDateTime.now();
        }
    }

    public abstract String getType();
}

