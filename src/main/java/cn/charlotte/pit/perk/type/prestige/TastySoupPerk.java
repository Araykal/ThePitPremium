package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerAssist;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/9 11:59
 */
public class TastySoupPerk extends AbstractPerk implements IPlayerKilledEntity, IPlayerAssist, IAttackEntity, IPlayerShootEntity {

    private final Map<UUID, Boolean> strength = new HashMap<>();

    @Override
    public String getInternalPerkName() {
        return "tasty_soup_perk";
    }

    @Override
    public String getDisplayName() {
        return "可口浓汤";
    }

    @Override
    public Material getIcon() {
        return Material.MUSHROOM_SOUP;
    }

    @Override
    public double requireCoins() {
        return 8000;
    }

    @Override
    public double requireRenown(int level) {
        return 30;
    }

    @Override
    public int requirePrestige() {
        return 7;
    }

    @Override
    public int requireLevel() {
        return 90;
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
        lines.add("&7将击杀获得的回复道具变成 &a可口浓汤 &7,");
        lines.add("&7助攻时可以获得 &a助攻浓汤 &7,");
        lines.add("&7浓汤类回复道具共享最大回复道具持有数(默认2).");
        lines.add(" ");
        lines.add("&7食用任意类型浓汤后获得以下效果:");
        lines.add("  &f▶ &6获得 1❤ 伤害吸收效果 &7(上限&64❤&7)");
        lines.add("  &f▶ &b速度 I &f(00:07)");
        lines.add("  &f▶ &c下次攻击造成的伤害 +15% (不可叠加)");
        lines.add(" ");
        lines.add("&7食用 &a可口浓汤 &7后额外获得以下效果:");
        lines.add("  &f▶ &a立刻回复 1❤ 生命值");
        lines.add(" ");
        lines.add("&7食用 &a助攻浓汤 &7后额外获得以下效果:");
        lines.add("  &f▶ &c生命恢复 II &f(00:05)");
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
            myself.getInventory().addItem(new ItemBuilder(Material.MUSHROOM_SOUP)
                    .name("&a可口浓汤")
                    .deathDrop(true)
                    .removeOnJoin(true)
                    .canSaveToEnderChest(false)
                    .canDrop(false)
                    .canTrade(false)
                    .internalName("perk_tasty_soup_kill")
                    .isHealingItem(true)
                    .build());
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerAssist(int enchantLevel, Player myself, Entity target, double damage, double finalDamage, AtomicDouble coins, AtomicDouble experience) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }

        if (PlayerUtil.getAmountOfActiveHealingPerk(myself) != 1) {
            return;
        }
        if (PlayerUtil.getPlayerHealItemAmount(myself) < PlayerUtil.getPlayerHealItemLimit(myself)) {
            myself.getInventory().addItem(new ItemBuilder(Material.MUSHROOM_SOUP)
                    .name("&a助攻浓汤")
                    .deathDrop(true)
                    .removeOnJoin(true)
                    .canSaveToEnderChest(false)
                    .canDrop(false)
                    .canTrade(false)
                    .internalName("perk_tasty_soup_assist")
                    .isHealingItem(true)
                    .build());
        }
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        strength.putIfAbsent(attacker.getUniqueId(), false);
        if (strength.get(attacker.getUniqueId())) {
            boostDamage.getAndAdd(0.15);
            strength.put(attacker.getUniqueId(), false);
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        strength.putIfAbsent(attacker.getUniqueId(), false);
        if (strength.get(attacker.getUniqueId())) {
            boostDamage.getAndAdd(0.15);
            strength.put(attacker.getUniqueId(), false);
        }
    }
}
