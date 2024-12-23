package javaapplication109;
// The UpdateTaskCommand class implements the Command interface to encapsulate the update task action
public class UpdateTaskCommand implements Command {
    private TaskManager taskManager;  // Instance of TaskManager to manage tasks
    private Task task;  // The task to be updated

    // Constructor to initialize the TaskManager and the task to be updated
    public UpdateTaskCommand(TaskManager taskManager, Task task) {
        this.taskManager = taskManager;  // Assigns the TaskManager instance
        this.task = task;  // Assigns the task to be updated
    }

    // Executes the command to update the task by adding it to the TaskManager
    @Override
    public void execute() {
        taskManager.addTask(task);  // Adds the task to the TaskManager (in this case, it's treated as an update)
    }
}