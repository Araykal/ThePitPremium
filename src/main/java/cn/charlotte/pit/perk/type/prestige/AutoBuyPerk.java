package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.shop.ShopMenu;
import cn.charlotte.pit.menu.shop.button.AbstractShopButton;
import cn.charlotte.pit.parm.listener.IPlayerRespawn;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.Passive;
import cn.charlotte.pit.perk.PerkType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Creator Misoryan
 * @Date 2021/6/4 22:06
 */
@Passive
public class AutoBuyPerk extends AbstractPerk implements IPlayerRespawn {
    @Override
    public String getInternalPerkName() {
        return "auto_buy_perk";
    }

    @Override
    public String getDisplayName() {
        return "自动购买";
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 5; //5
    }

    @Override
    public int requirePrestige() {
        return 1;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Arrays.asList("&7允许你为商店中的物品开启自动购买,", "&7开启自动购买的物品将在你复活时自动进行购买.");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    private List<AbstractShopButton> buttons = null;

    @Override
    public void handleRespawn(int enchantLevel, Player myself) {
        if (buttons == null) {
            buttons = new ShopMenu()
                    .getButtons(myself)
                    .values()
                    .stream()
                    .filter(button -> button instanceof AbstractShopButton)
                    .map(button -> ((AbstractShopButton) button))
                    .collect(Collectors.toList());
        }

        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
            profile.getAutoBuyButtons().forEach(internal -> {
                for (AbstractShopButton button : buttons) {
                    if (button.getInternalName().equals(internal)) {
                        button.clicked(myself, 0, ClickType.LEFT, 0, new ItemStack(Material.AIR));
                        break;
                    }
                }
            });
        }, 1L);
    }
}
