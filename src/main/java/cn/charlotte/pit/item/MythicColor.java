package cn.charlotte.pit.item;

import org.bukkit.ChatColor;
import org.bukkit.Color;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/10 21:28
 */
public enum MythicColor {

    ORANGE("橙", "orange", ChatColor.GOLD, Color.ORANGE, (byte) 1),
    RED("红", "red", ChatColor.RED, Color.RED, (byte) 14),
    BLUE("蓝", "blue", ChatColor.BLUE, Color.BLUE, (byte) 11),
    YELLOW("黄", "yellow", ChatColor.YELLOW, Color.YELLOW, (byte) 4),
    GREEN("绿", "green", ChatColor.DARK_GREEN, Color.GREEN, (byte) 13),
    DARK("黑", "dark", ChatColor.DARK_PURPLE, Color.BLACK, (byte) 15),
    RAGE("红", "rage", ChatColor.DARK_RED, Color.fromBGR(0, 0, 107), (byte) 14),
    AQUA("天蓝", "aqua", ChatColor.AQUA, Color.AQUA, (byte) 3),
    DARK_GREEN("深绿", "dark_green", ChatColor.DARK_GREEN, Color.fromBGR(125,195,131), (byte) 13),
    NONE("无", "none", ChatColor.AQUA, Color.AQUA, (byte) 0),
    DEMON_DARK("", "demon_dark", ChatColor.RED, Color.RED, (byte) 15);


    private final String displayName;
    private final String internalName;
    private final ChatColor chatColor;
    private final Color leatherColor;
    private final byte colorByte;


    MythicColor(String displayName, String internalName, ChatColor color, Color leatherColor, byte colorByte) {
        this.displayName = displayName;
        this.internalName = internalName;
        this.chatColor = color;
        this.leatherColor = leatherColor;
        this.colorByte = colorByte;
    }

    public static MythicColor valueOfInternalName(String internalName) {
        for (MythicColor mythicColor : MythicColor.values()) {
            if (mythicColor.getInternalName().equalsIgnoreCase(internalName)) {
                return mythicColor;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getInternalName() {
        return this.internalName;
    }

    public ChatColor getChatColor() {
        return this.chatColor;
    }

    public Color getLeatherColor() {
        return this.leatherColor;
    }

    public byte getColorByte() {
        return this.colorByte;
    }
}
