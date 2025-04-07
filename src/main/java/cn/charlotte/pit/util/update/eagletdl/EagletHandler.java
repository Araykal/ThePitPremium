package cn.charlotte.pit.util.update.eagletdl;

@FunctionalInterface
public interface EagletHandler<T> {

    void handle(T event);

}
