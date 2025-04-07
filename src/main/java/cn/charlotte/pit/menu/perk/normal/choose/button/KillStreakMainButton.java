package cn.charlotte.pit.menu.perk.normal.choose.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.killstreak.StreakChooseMenu;
import cn.charlotte.pit.perk.AbstractPerk;
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
 * @Created_In: 2021/1/11 22:15
 */
public class KillStreakMainButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("&7在此选择你的连杀天赋,");
        lore.add("&7连杀天赋将在你每");
        lore.add("&7达到若干击杀时触发.");
        lore.add(" ");
        lore.add("&e点击查看!");
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        Material material = Material.GOLD_BLOCK;
        if (profile.getChosePerk().get(5) != null) {
            for (AbstractPerk perk : ThePit.getInstance().getPerkFactory().getPerks()) {
                if (profile.getChosePerk().get(5).getPerkInternalName().equalsIgnoreCase(perk.getInternalPerkName())) {
                    material = perk.getIcon();
                }
            }
        }
        return new ItemBuilder(material).name("&a连杀天赋").lore(lore).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        new StreakChooseMenu().openMenu(player);
    }

}
