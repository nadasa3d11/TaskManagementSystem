/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

import java.time.LocalDateTime;
import java.util.List;

public class BugTask extends Task {
    public BugTask(String name, String description, LocalDateTime addDate, LocalDateTime deadline, List<User> responsibleUsers) {
        super(name, description, addDate, deadline, responsibleUsers);
    }

    @Override
    public String getType() {
        return "Bug";
    }
}