/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication109;

public class UpdateTaskCommand implements Command {
    private TaskManager taskManager;
    private Task task;

    public UpdateTaskCommand(TaskManager taskManager, Task task) {
        this.taskManager = taskManager;
        this.task = task;
    }

    @Override
    public void execute() {
        // Logic to update the task (for example, change its status)
        taskManager.addTask(task);
    }
}
