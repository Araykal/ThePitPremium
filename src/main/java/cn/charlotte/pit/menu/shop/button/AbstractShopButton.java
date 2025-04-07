package cn.charlotte.pit.menu.shop.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 16:58
 */
public abstract class AbstractShopButton extends Button {

    public abstract String getInternalName();

    public ItemStack getButtonItem(Player player) {
        if (!PlayerUtil.isPlayerUnlockedPerk(player, "auto_buy_perk")) return getDisplayButtonItem(player);

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        ItemStack itemStack = getDisplayButtonItem(player);
        try {
            List<String> lines = itemStack.getItemMeta().getLore();
            lines.add(" ");
            lines.add("&7自动购买 " + (profile.getAutoBuyButtons().contains(getInternalName()) ? "&a开启" : "&c关闭"));
            lines.add("&b使用Shift点击以" + (profile.getAutoBuyButtons().contains(getInternalName()) ? "关闭" : "开启") + "自动购买!");
            return new ItemBuilder(itemStack).lore(lines).build();
        } catch (Exception e) {
            return getDisplayButtonItem(player);
        }
    }


    public static int getDiscountPrice(Player player, int price) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        PerkData data = profile.getUnlockedPerkMap().get("ScamArtist");
        if (data != null) {
            return (int) ((1 - 0.05 * data.getLevel()) * price);
        }
        return price;
    }

    public abstract ItemStack getDisplayButtonItem(Player player);

    public abstract ItemStack[] getResultItem(Player player);

    public abstract int getPrice(Player player);

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (getButtonItem(player).getType().equals(Material.AIR)) {
            return;
        }

        if (clickType.isShiftClick()) {
            if (PlayerUtil.isPlayerUnlockedPerk(player, "auto_buy_perk")) {
                if (profile.getAutoBuyButtons().contains(getInternalName())) {
                    profile.getAutoBuyButtons().remove(getInternalName());
                } else {
                    profile.getAutoBuyButtons().add(getInternalName());
                }
                return;
            }
        }

        if (profile.getCoins() >= getDiscountPrice(player, getPrice(player))) {
            if (InventoryUtil.isInvFull(player)) {
                player.sendMessage(CC.translate("&c&l背包已满! &7你的背包已满,暂时无法购买物品."));
                return;
            }
            profile.setCoins(profile.getCoins() - getDiscountPrice(player, getPrice(player)));
            player.getInventory().addItem(this.getResultItem(player));
            player.sendMessage(CC.translate("&a购买成功!"));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
        } else {
            player.sendMessage(CC.translate("&c你的硬币不足!"));
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
        }
    }
}
