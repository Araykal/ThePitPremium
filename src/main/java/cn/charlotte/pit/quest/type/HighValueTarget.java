package cn.charlotte.pit.quest.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.quest.AbstractQuest;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/20 21:31
 */
public class HighValueTarget extends AbstractQuest implements IAttackEntity, IPlayerShootEntity {
    @Override
    public String getQuestInternalName() {
        return "High_Value_Target";
    }

    @Override
    public String getQuestDisplayName() {
        return "高价值目标";
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public List<String> getQuestDescription(int level, boolean isNightQuest) {
        return Arrays.asList("&f普通攻击造成的伤害 &c-" + (20 * level) + "%", "&f攻击造成的真实伤害 &c-" + (40 * level) + "%");
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
    }

    @Override
    public void onInactive(Player player, int level) {
        super.onInactive(player, level);
    }

    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase(this.getQuestInternalName())) {
            boostDamage.getAndAdd(-0.2 * profile.getCurrentQuest().getLevel());
            finalDamage.set(finalDamage.get() * (1 - 0.4 * profile.getCurrentQuest().getLevel()));
        }
    }

    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase(this.getQuestInternalName())) {
            boostDamage.getAndAdd(-0.2 * profile.getCurrentQuest().getLevel());
            finalDamage.set(finalDamage.get() * (1 - 0.4 * profile.getCurrentQuest().getLevel()));
        }
    }
}
