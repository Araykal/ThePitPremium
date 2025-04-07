package cn.charlotte.pit.util.chat;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/14 15:33
 */
public enum MessageType {
    //不需要归类的消息请直接不带类型参数发送(如权限不足提示等)
    BOUNTY, //赏金
    STREAK, //连杀
    PRESTIGE, //精通消息
    EVENT, //活动消息 仅限小事件开始/倒计时提醒 其余请用ANNOUNCEMENT
    COMBAT, //战斗消息
    CHAT, //聊天
    MISC, //杂项消息 暂时留空
    QUEST,//任务
    PRIVATE, //玩家私聊
    PARTY, //玩家组队内消息
    GUILD, //玩家公会内消息
    COMMAND //玩家指令消息
}
