package cn.charlotte.pit.util.update.eagletdl;

public class CompleteEvent {
    private final EagletTask task;
    private final boolean success;

    CompleteEvent(EagletTask task, boolean success) {
        this.task = task;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public EagletTask getTask() {
        return task;
    }
}
