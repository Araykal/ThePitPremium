package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/17 13:14
 */
public class MythicismPerk extends AbstractPerk {
    @Override
    public String getInternalPerkName() {
        return "Mythicism";
    }

    @Override
    public String getDisplayName() {
        return "神话附魔师";
    }

    @Override
    public Material getIcon() {
        return Material.ENCHANTMENT_TABLE;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        if (level == 1) {
            return 10;
        }
        if (level < 4) {
            return 5;
        }
        if (level < 8) {
            return 10;
        }
        if (level == 8) {
            return 15;
        }
        return 20;
    }

    @Override
    public int requirePrestige() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        int nextLevel = PlayerUtil.getPlayerUnlockedPerkLevel(player, this.getInternalPerkName()) + 1;
        HashMap<Integer, List<String>> effects = new HashMap<>();
        for (int i = 1; i < 12; i++) {
            effects.put(i, new ArrayList<>());
            if (i > 1) {
                effects.get(i).add("  &7可以通过击杀玩家概率掉落 &e神话之剑 &7, &e神话之弓 &7.");
                effects.get(i).add("  &7可以进行 &dI , II &7阶附魔.");
            }
            if (i > 4) {
                effects.get(i).add("  &7可以进行 &dIII &7阶附魔.");
                effects.get(i).add("  &7可以通过击杀玩家概率掉落 &e神话之甲 &7.");
            }
            if (i > 1) {
                effects.get(i).add("  &7击杀玩家掉落神话物品的几率提升至基础概率的 &d" + (100 + (i - 1) * 20) + "% &7.");
            }
        }
        if (nextLevel > 1) {
            lines.add("&7当前:");
            lines.addAll(effects.get(nextLevel - 1));
            lines.add("&7下一升级:");
        } else {
            lines.add("&7解锁后:");
        }
        lines.addAll(nextLevel > 10 ? Collections.singletonList("&d没有下一等级了,恭喜!") : effects.get(nextLevel));
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }
}
