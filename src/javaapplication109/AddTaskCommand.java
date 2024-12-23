package javaapplication109;
// The AddTaskCommand class implements the Command interface
public class AddTaskCommand implements Command {

    // Instance variables for TaskManager and Task
    private TaskManager taskManager;  // Manages tasks
    private Task task;  // The task to be added

    // Constructor for AddTaskCommand that takes TaskManager and Task as parameters
    public AddTaskCommand(TaskManager taskManager, Task task) {
        this.taskManager = taskManager;  // Initializes the taskManager instance variable
        this.task = task;  // Initializes the task instance variable
    }

    // The execute method implements the execute command from the Command interface
    @Override
    public void execute() {
        taskManager.addTask(task);  // Calls the addTask method on the taskManager to add the task
    }
}
