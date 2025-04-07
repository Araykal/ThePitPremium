package cn.charlotte.pit.perk.type.shop;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.medal.impl.challenge.EndlessQuiverMedal;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.parm.type.BowOnly;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.item.ItemBuilder;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/1 22:18
 */
public class ArrowRecoveryPerk extends AbstractPerk implements IPlayerShootEntity {
    @Override
    public String getInternalPerkName() {
        return "ArrowRecovery";
    }

    @Override
    public String getDisplayName() {
        return "无尽箭袋";
    }

    @Override
    public Material getIcon() {
        return Material.ARROW;
    }

    @Override
    public double requireCoins() {
        return 2000;
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
        return 20;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7使用弓箭击中玩家时,你可以获得 &f3支箭 &7.");
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
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    @BowOnly
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        ItemBuilder arrowBuilder = new ItemBuilder(Material.ARROW).internalName("default_arrow").defaultItem().canDrop(false).canSaveToEnderChest(false);
        attacker.getInventory().addItem(arrowBuilder.amount(3).build());
        new EndlessQuiverMedal().addProgress(PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()), 3);
    }
}
