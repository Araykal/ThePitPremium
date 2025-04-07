package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/25 16:29
 */
public class OlympusPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "Olympus";
    }

    @Override
    public String getDisplayName() {
        return "奥林匹斯";
    }

    @Override
    public Material getIcon() {
        return Material.POTION;
    }

    @Override
    public double requireCoins() {
        return 6000;
    }

    @Override
    public double requireRenown(int level) {
        return 20;
    }

    @Override
    public int requirePrestige() {
        return 4;
    }

    @Override
    public int requireLevel() {
        return 70;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7将击杀获得的回复道具变成 &b奥林匹斯药水 &7,");
        lines.add("&7但回复道具的持有量降低至 &c1 &7.");
        lines.add(" ");
        lines.add("&7饮用 &b奥林匹斯药水 &7后获得以下效果:");
        lines.add("  &f▶ &c生命恢复 III &f(00:10)");
        lines.add("  &f▶ &3抗性提升 II &f(00:04)");
        lines.add("  &f▶ &b速度 I &f(00:24)");
        lines.add("  &f▶ &b+27 经验值!");
        return lines;
    }

    @PlayerOnly
    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }

        if (PlayerUtil.getAmountOfActiveHealingPerk(myself) != 1) {
            return;
        }

        if (PlayerUtil.getPlayerHealItemAmount(myself) < PlayerUtil.getPlayerHealItemLimit(myself)) {
            myself.getInventory().addItem(new ItemBuilder(Material.POTION)
                    .name("&b奥林匹斯药水")
                    .deathDrop(true)
                    .removeOnJoin(true)
                    .canSaveToEnderChest(false)
                    .canDrop(false)
                    .canTrade(false)
                    .isHealingItem(true)
                    .internalName("perk_olympus")
                    .build());
        }
    }

}
