package javaapplication109;
// The PriorityTaskDecorator class extends the TaskDecorator class to add additional functionality (priority) to the task
public class PriorityTaskDecorator extends TaskDecorator {
    private String priority;  // Stores the priority level of the task

    // Constructor that takes a Task and a priority level, and passes the Task to the parent class constructor
    public PriorityTaskDecorator(Task task, String priority) {
        super(task);  // Calls the parent class constructor to initialize the task
        this.priority = priority;  // Initializes the priority attribute
    }

    // Override the getDescription method to append the priority to the task description
    @Override
    public String getDescription() {
        return super.getDescription() + " (Priority: " + priority + ")";  // Adds the priority information to the description
    }

    // Override the getType method to return the type of the task from the decorated task
    @Override
    public String getType() {
        return task.getType();  // Returns the type of the task (delegated to the original task)
    }
}
