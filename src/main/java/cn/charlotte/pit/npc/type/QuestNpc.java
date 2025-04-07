package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.quest.main.QuestMenu;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.level.LevelUtil;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 18:38
 */
public class QuestNpc extends SkinNPC {
    @Override
    public String getNpcInternalName() {
        return "quest";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&3&l任务");
        if (profile.getLevel() >= 30 || profile.getPrestige() > 0) {
            lines.add("&e&l右键查看");
        } else {
            lines.add("&c在 " + LevelUtil.getLevelTag(profile.getPrestige(), 30) + " &c时解锁");
        }
        return lines;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getQuestNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin("ewogICJ0aW1lc3RhbXAiIDogMTczNzAxODA0MDM1MCwKICAicHJvZmlsZUlkIiA6ICI2ZjhlYWI1MTVmNTc0MmRhOWYxZDYzMzY1ODAxMDU4YyIsCiAgInByb2ZpbGVOYW1lIiA6ICJDaW5kZXJGb3hfMjAwNiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hZTRmZTNkNzUyNjcyZjE4Y2E5ODUwZDJjNzk5MDEwYTA0MmM4Yzg0OGRiYzk2ZWRkZWM3OWMxZmUxNTYxYzljIgogICAgfQogIH0KfQ==",
                null
        );
    }

    @Override
    public void handlePlayerInteract(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getLevel() < 30) {
            player.sendMessage(CC.translate("&c&l等级不足! &7任务在 " + LevelUtil.getLevelTag(profile.getPrestige(), 30) + " &7时解锁."));
            return;
        }
        new QuestMenu().openMenu(player);
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return new ItemStack(Material.BOOK);
    }
}
