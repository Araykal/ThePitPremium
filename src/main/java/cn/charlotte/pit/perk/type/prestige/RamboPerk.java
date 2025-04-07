package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/1 16:38
 */
public class RamboPerk extends AbstractPerk implements IPlayerKilledEntity {
    @Override
    public String getInternalPerkName() {
        return "rambo";
    }

    @Override
    public String getDisplayName() {
        return "猛汉";
    }

    @Override
    public Material getIcon() {
        return Material.STICK;
    }

    @Override
    public double requireCoins() {
        return 6000;
    }

    @Override
    public double requireRenown(int level) {
        return 15;
    }

    @Override
    public int requirePrestige() {
        return 3;
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
    public List<String> getDescription(Player player) {
        return Arrays.asList("&7携带时击杀&c无法获得治疗道具&7但恢复全部生命,", "&7且携带期间最大生命值 &c-2❤ &7.", "&c与天赋吸血鬼携带时,吸血鬼天赋失效.");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public void onPerkActive(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getExtraMaxHealth().put(getInternalPerkName(), -4.0);
        player.setMaxHealth(profile.getMaxHealth());
    }

    @Override
    public void onPerkInactive(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.getExtraMaxHealth().remove(getInternalPerkName());
        player.setMaxHealth(profile.getMaxHealth());

    }

    @Override
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        if ("rage_pit".equals(ThePit.getInstance().getEventFactory().getActiveEpicEventName())) {
            return;
        }
        myself.setHealth(myself.getMaxHealth());
    }
}
