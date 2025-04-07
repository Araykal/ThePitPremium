package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerAssist;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/2 17:47
 */
public class GoldMinerPerk extends AbstractPerk implements IPlayerKilledEntity, IPlayerShootEntity, IPlayerAssist {
    @Override
    public String getInternalPerkName() {
        return "GoldMiner";
    }

    @Override
    public String getDisplayName() {
        return "灵活战术";
    }

    @Override
    public Material getIcon() {
        return Material.STRING;
    }

    @Override
    public double requireCoins() {
        return 4000;
    }

    @Override
    public double requireRenown(int level) {
        return 0;
    }

    @Override
    public int requirePrestige() {
        return 0;
    }

    @Override
    public int requireLevel() {
        return 45;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7助攻获得的硬币 &6+2 &7.");
        lines.add("&7箭矢命中其他玩家后,该玩家死亡时");
        lines.add("&7你参与击杀/助攻获得的硬币 &6+50% &7.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        if (myself.hasMetadata("mixed_combat_" + myself.getUniqueId())) {
            coins.getAndAdd(0.5 * coins.get());
            myself.removeMetadata("mixed_combat_" + myself.getUniqueId(), ThePit.getInstance());
        }
    }

    @Override
    @PlayerOnly
    public void handlePlayerAssist(int enchantLevel, Player myself, Entity target, double damage, double finalDamage, AtomicDouble coins, AtomicDouble experience) {
        if (myself.hasMetadata("mixed_combat_" + myself.getUniqueId())) {
            coins.getAndAdd(0.5 * coins.get());
            myself.removeMetadata("mixed_combat_" + myself.getUniqueId(), ThePit.getInstance());
        }
        coins.getAndAdd(2);
    }

    @Override
    @PlayerOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        target.setMetadata("mixed_combat_" + attacker.getUniqueId(), new FixedMetadataValue(ThePit.getInstance(), true));
    }
}
