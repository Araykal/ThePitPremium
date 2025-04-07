package cn.charlotte.pit.util.update.eagletdl;

public class StartEvent {

    private final EagletTask task;

    StartEvent(EagletTask task) {
        this.task = task;
    }

    public EagletTask getTask() {
        return task;
    }
}
