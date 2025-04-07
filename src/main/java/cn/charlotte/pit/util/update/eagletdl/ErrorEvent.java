package cn.charlotte.pit.util.update.eagletdl;

public class ErrorEvent {

    private final Throwable e;
    private final EagletTask task;

    public ErrorEvent(Throwable e, EagletTask task) {
        this.e = e;
        this.task = task;
    }

    public EagletTask getTask() {
        return task;
    }

    public Throwable getException() {
        return e;
    }
}
