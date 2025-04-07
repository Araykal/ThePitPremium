package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.shop.ShopMenu;
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
 * @Created_In: 2021/1/1 11:16
 */
public class ShopNPC extends SkinNPC {
    @Override
    public String getNpcInternalName() {
        return "shop";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> lines = new ArrayList<>();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        lines.add("&b&l商店");
        if (profile.getLevel() >= 10) {
            lines.add("&e&l右键查看");
        } else {
            lines.add("&c在 " + LevelUtil.getLevelTag(profile.getPrestige(), 10) + " &c时解锁");
        }
        return lines;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getShopNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNzAxNjc3NzIxNCwKICAicHJvZmlsZUlkIiA6ICIzZGE2ZDgxOTI5MTY0MTNlODhlNzg2MjQ3NzA4YjkzZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJGZXJTdGlsZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mM2VhZDVmZGNiNjJhZjZmZTI5MzU0NTg3ZTNjOGJiZGE3NjQ1MTA0ZWE5OWYzYTVhNzgzOGIyNGNkNzQxZDNmIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
                null);
    }

    @Override
    public void handlePlayerInteract(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getLevel() < 10) {
            player.sendMessage(CC.translate("&c&l等级不足! &7商店在 " + LevelUtil.getLevelTag(profile.getPrestige(), 10) + " &7时解锁."));
        } else {
            new ShopMenu().openMenu(player);
        }
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return null;
    }
}
