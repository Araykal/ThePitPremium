package cn.charlotte.pit.events;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/9 13:11
 */
public interface IEvent {

    String getEventInternalName();

    String getEventName();

    int requireOnline();

    void onActive();

    void onInactive();

}
