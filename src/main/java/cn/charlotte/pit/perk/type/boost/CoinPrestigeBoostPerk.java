package cn.charlotte.pit.perk.type.boost;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.Passive;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/12 21:36
 */
@Passive
public class CoinPrestigeBoostPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "CoinPrestigeBoost";
    }

    @Override
    public String getDisplayName() {
        return "硬币获取提升";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_NUGGET;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        if (level == 1) {
            return 5;
        }
        return 10;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
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
        List<String> lines = new ArrayList<>();
        lines.add("&7每次升级:");
        lines.add("  &7击杀获得 &6+2.5% 硬币 &7.");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 5;
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
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        PerkData data = profile.getUnlockedPerkMap().get(getInternalPerkName());
        if (data != null) {
            coins.set((1 + 0.025 * data.getLevel()) * coins.get());
        }
    }
}
