package cn.charlotte.pit.npc;

import cn.charlotte.pit.util.chat.CC;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 22:35
 */
public abstract class AbstractPitNPC {
    private NPC npc;

    //NPC 内部名称
    public abstract String getNpcInternalName();

    //NPC 显示名称 可以自行加空格
    public abstract List<String> getNpcDisplayName(Player player);

    public List<String> getNpcTextLine(Player player) {
        List<String> displayName = this.getNpcDisplayName(player);
        List<String> text = new ArrayList<>();
        for (String s : displayName) {
            text.add(CC.translate(s));
        }

        return text;
    }

    //NPC 生成位置
    public abstract Location getNpcSpawnLocation();

    //Player name
    public abstract Skin getNpcSkin();

    //玩家交互处理
    public abstract void handlePlayerInteract(Player player);

    //npc 手持物品
    public abstract ItemStack getNpcHeldItem();

    public NPC getNpc() {
        return this.npc;
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public abstract void initSkin(NPC npc);
}
