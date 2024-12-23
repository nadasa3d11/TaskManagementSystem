package javaapplication109;
// The DeleteTaskCommand class implements the Command interface
public class DeleteTaskCommand implements Command {

    // Instance variables for TaskManager and Task
    private TaskManager taskManager;  // Manages tasks
    private Task task;  // The task to be deleted

    // Constructor for DeleteTaskCommand that takes TaskManager and Task as parameters
    public DeleteTaskCommand(TaskManager taskManager, Task task) {
        this.taskManager = taskManager;  // Initializes the taskManager instance variable
        this.task = task;  // Initializes the task instance variable
    }

    // The execute method implements the execute command from the Command interface
    @Override
    public void execute() {
        taskManager.removeTask(task);  // Calls the removeTask method on the taskManager to delete the task
    }
}
