package cn.charlotte.pit.menu.status.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/3 17:14
 */
public class OffensiveStatusButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> lores = new ArrayList<>();
        lores.add("&7击杀: &a" + profile.getKills());
        lores.add("&7助攻: &a" + profile.getAssists());
        lores.add("&7近战命中: &a" + profile.getMeleeHit());
        lores.add("&7弓箭命中: &a" + profile.getBowHit());
        lores.add(" ");
        lores.add("&7造成总伤害: &a" + profile.getTotalDamage());
        lores.add("&7造成近战伤害: &a" + profile.getMeleeTotalDamage());
        lores.add("&7造成弓箭伤害: &a" + profile.getArrowTotalDamage());
        lores.add(" ");
        lores.add("&7最高连杀: &a" + profile.getHighestStreaks());
        return new ItemBuilder(Material.IRON_SWORD).name("&c个人攻击数据").lore(lores).buildWithUnbreakable();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}
