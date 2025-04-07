package cn.charlotte.pit.util.update.eagletdl;

public class RetryFailedException extends RuntimeException {

    private final EagletTask task;

    RetryFailedException(EagletTask task) {
        this.task = task;
    }

    public EagletTask getTask() {
        return task;
    }
}
