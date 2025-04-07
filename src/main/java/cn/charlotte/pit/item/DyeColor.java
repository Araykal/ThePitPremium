package cn.charlotte.pit.item;


import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum DyeColor {

    WHITE(ChatColor.WHITE, Color.WHITE), //白色
    FUCHSIA(ChatColor.LIGHT_PURPLE, Color.FUCHSIA), //粉色
    LIME(ChatColor.GREEN, Color.LIME), //亮绿色
    MAROON(ChatColor.DARK_RED, Color.MAROON), //深红色
    NAVY(ChatColor.DARK_BLUE, Color.NAVY),//深蓝色
    TEAL(ChatColor.DARK_AQUA, Color.TEAL), //sewer
    OLIVE(ChatColor.GREEN, Color.OLIVE), //草绿色
    SILVER(ChatColor.WHITE, Color.SILVER), //银色
    GRAY(ChatColor.GRAY, Color.GRAY), //灰色
    PURPLE(ChatColor.WHITE, Color.PURPLE), //紫色
    AQUA(ChatColor.AQUA, Color.AQUA), //天蓝色
    BLUE(ChatColor.BLUE, Color.BLUE), //蓝色
    GREEN(ChatColor.DARK_GREEN, Color.GREEN), //绿色
    YELLOW(ChatColor.YELLOW, Color.YELLOW), //黄色
    ORANGE(ChatColor.GOLD, Color.ORANGE), //橙色
    RED(ChatColor.RED, Color.RED); //红色

    private final ChatColor chatColor;
    private final Color color;

    DyeColor(ChatColor chatColor, Color color) {

        this.chatColor = chatColor;
        this.color = color;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public Color getColor() {
        return this.color;
    }
}
