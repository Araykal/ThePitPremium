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
 * @Created_In: 2021/1/3 17:59
 */
public class DefensiveStatusButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        List<String> lores = new ArrayList<>();
        lores.add("&7死亡: &a" + profile.getDeaths());
        lores.add(" ");
        lores.add("&7受到伤害: &a" + profile.getHurtDamage());
        lores.add("&7受到近战伤害: &a" + profile.getMeleeHurtDamage());
        lores.add("&7受到弓箭伤害: &a" + profile.getBowHurtDamage());
        return new ItemBuilder(Material.IRON_CHESTPLATE).name("&9个人受击数据").lore(lores).buildWithUnbreakable();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}
