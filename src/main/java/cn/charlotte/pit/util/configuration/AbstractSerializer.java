package cn.charlotte.pit.util.configuration;

public abstract class AbstractSerializer<T> {
    public AbstractSerializer() {
    }

    public abstract String toString(T var1);

    public abstract T fromString(String var1);
}
