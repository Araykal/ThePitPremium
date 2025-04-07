package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.status.StatusMenu;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.level.LevelUtil;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/2 21:31
 */
public class StatusNPC extends SkinNPC {
    @Override
    public String getNpcInternalName() {
        return "status";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&9&l统计信息");
        if (profile.getLevel() >= 50 || profile.getPrestige() > 0) {
            lines.add("&e&l右键查看");
        } else {
            lines.add("&c在 " + LevelUtil.getLevelTag(profile.getPrestige(), 50) + " &c时解锁");
        }
        return lines;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getStatusNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNzAxOTA2Mzg5MiwKICAicHJvZmlsZUlkIiA6ICJmNTBjOGRkN2FiN2Y0ZmUyYWI4ZGI1M2NjYzRiYWQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYWNoYWRvVF9UIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzcyOTFmYzgwMzkxMWEyMTg3NzYyYjUzZjEzNTViOWJjZWQzOTFiZTdiMmRjMmE0YmI5ODU0MjIwYTg4ZTkzNjYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                null
        );
    }

    @Override
    public void handlePlayerInteract(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getLevel() < 50 && profile.getPrestige() == 0) {
            player.sendMessage(CC.translate("&c&l等级不足! &7统计在 " + LevelUtil.getLevelTag(profile.getPrestige(), 50) + " &7时解锁."));
        } else {
            new StatusMenu().openMenu(player);
        }
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return null;
    }
}
