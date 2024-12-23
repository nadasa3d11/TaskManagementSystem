package javaapplication109;

// The TaskDecorator class is an abstract class that acts as a base class for decorators to add additional functionality to tasks
public abstract class TaskDecorator extends Task {
    protected Task task;  // The task being decorated

    // Constructor that initializes the TaskDecorator with the task to be decorated
    public TaskDecorator(Task task) {
        super(task.getName(), task.getDescription(), task.getAddDate(), task.getDeadline(), task.getResponsibleUsers());
        this.task = task;  // Assigns the task to the decorator
    }

    // Override the getDescription method to return the description of the decorated task
    @Override
    public String getDescription() {
        return task.getDescription();  // Returns the description of the task being decorated
    }
}