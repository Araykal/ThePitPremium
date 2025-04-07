package cn.charlotte.pit.quest.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.quest.AbstractQuest;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 16:32
 */
public class LowEfficiency extends AbstractQuest implements IAttackEntity, IPlayerShootEntity, IPlayerDamaged {
    @Override
    public String getQuestInternalName() {
        return "Low_Efficiency";
    }

    @Override
    public String getQuestDisplayName() {
        return "效率低下";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public List<String> getQuestDescription(int level, boolean isNightQuest) {
        return Arrays.asList("&f死亡需要等待 &c" + (level + 1) * 1.5 + " 秒 &f才能复活",
                "&f攻击造成的伤害 &c-" + (10 * level) + "%",
                "&f受到的玩家伤害 &c+" + (10 * level) + "%");
    }

    @Override
    public long getDuration(int level) {
        return 4 * 60 * 1000;
    }

    @Override
    public int getTotal(int level) {
        return 10;
    }

    @Override
    public void onActive(Player player, int level) {
        super.onActive(player, level);
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.setRespawnTime(1.5 * (1 + level));
    }

    @Override
    public void onInactive(Player player, int level) {
        super.onInactive(player, level);
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        profile.setRespawnTime(0.1d);
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase(this.getQuestInternalName())) {
            boostDamage.getAndAdd(-0.1 * profile.getCurrentQuest().getLevel());
        }
    }

    @Override

    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase(this.getQuestInternalName())) {
            boostDamage.getAndAdd(-0.1 * profile.getCurrentQuest().getLevel());
        }
    }

    @Override
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase(this.getQuestInternalName())) {
            boostDamage.getAndAdd(0.1 * profile.getCurrentQuest().getLevel());
        }
    }
}
