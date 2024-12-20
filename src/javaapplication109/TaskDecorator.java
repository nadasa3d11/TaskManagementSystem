
package javaapplication109;

public abstract class TaskDecorator extends Task {
    protected Task task;

    public TaskDecorator(Task task) {
        super(task.getName(), task.getDescription(), task.getAddDate(), task.getDeadline(), task.getResponsibleUsers());
        this.task = task;
    }

    @Override
    public String getDescription() {
        return task.getDescription();
    }
}