package cn.charlotte.pit.perk.type.streak.beastmode;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToughSkinKillStreak extends AbstractPerk implements IPlayerDamaged {

    @Override
    public String getInternalPerkName() {
        return "tough_skin";
    }

    @Override
    public String getDisplayName() {
        return "坚韧肌肤";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public double requireCoins() {
        return 3000;
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
        return 30;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> list = new ArrayList<>();
        list.add("&7此天赋每 &c5 连杀 &7触发一次.");
        list.add(" ");
        list.add("&7触发时:");
        list.add("  &a▶ &7获得效果: 受到的伤害 &9-3% &7(可叠加,最高8层)");
        return list;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.KILL_STREAK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        boostDamage.getAndAdd(Math.min(8, Math.floor(profile.getStreakKills() / 5)) * -0.03);
    }
}
