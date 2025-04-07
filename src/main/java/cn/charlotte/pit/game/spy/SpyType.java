package cn.charlotte.pit.game.spy;

import cn.charlotte.pit.util.chat.MessageType;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

/**
 * @Creator Misoryan
 * @Date 2021/5/3 10:43
 */
@Getter
public enum SpyType {

    PRIVATE(ChatColor.LIGHT_PURPLE, "私聊", new String[]{"/m", "/tell", "/msg", "/r"}, Material.SKULL_ITEM, MessageType.PRIVATE),
    PARTY(ChatColor.BLUE, "组队", new String[]{"/p", "/pc", "/party", "/partychat", "/组队", "/组队聊天"}, Material.BOOK, MessageType.PARTY),
    GUILD(ChatColor.DARK_GREEN, "公会", new String[]{"/g", "/gc", "/oc", "/guildofficerchat", "/guildchat", "/guild", "/公会", "/公会聊天"}, Material.PAINTING, MessageType.GUILD);
    //COMMAND(ChatColor.GOLD,"指令",new String[]{"/"},Material.COMMAND,MessageType.COMMAND);

    private final ChatColor chatColor;
    private final String name;
    private final String[] commands;
    private final Material material;
    private final MessageType messageType;

    SpyType(ChatColor chatColor, String name, String[] commands, Material material, MessageType messageType) {
        this.chatColor = chatColor;
        this.name = name;
        this.commands = commands;
        this.material = material;
        this.messageType = messageType;
    }
}
