package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2021/2/2 22:45
 */
public class KillRecap {
    public static Map<UUID, KillRecap> recapMap = new HashMap<>();


    private long completeTime;
    //killer
    private UUID killer;
    private List<String> perk = new ArrayList<>();

    private double totalExp;

    private double baseExp;
    //连杀小于等于3的exp
    private double notStreakExp;
    //连杀为5的倍数的exp
    private double streakExp;
    //等级差距exp
    private double levelDisparityExp;
    private double otherExp;

    private double totalCoin;
    private double notStreakCoin;
    private double streakCoin;
    private double levelDisparityCoin;
    private double baseCoin;
    private double otherCoin;


    //assist
    private List<AssistData> assistData = new ArrayList<>();

    //damage log
    private List<DamageData> damageLogs = new ArrayList<>();

    public KillRecap() {
    }

    public long getCompleteTime() {
        return this.completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public UUID getKiller() {
        return this.killer;
    }

    public void setKiller(UUID killer) {
        this.killer = killer;
    }

    public List<String> getPerk() {
        return this.perk;
    }

    public void setPerk(List<String> perk) {
        this.perk = perk;
    }

    public double getTotalExp() {
        return this.totalExp;
    }

    public void setTotalExp(double totalExp) {
        this.totalExp = totalExp;
    }

    public double getBaseExp() {
        return this.baseExp;
    }

    public void setBaseExp(double baseExp) {
        this.baseExp = baseExp;
    }

    public double getNotStreakExp() {
        return this.notStreakExp;
    }

    public void setNotStreakExp(double notStreakExp) {
        this.notStreakExp = notStreakExp;
    }

    public double getStreakExp() {
        return this.streakExp;
    }

    public void setStreakExp(double streakExp) {
        this.streakExp = streakExp;
    }

    public double getLevelDisparityExp() {
        return this.levelDisparityExp;
    }

    public void setLevelDisparityExp(double levelDisparityExp) {
        this.levelDisparityExp = levelDisparityExp;
    }

    public double getOtherExp() {
        return this.otherExp;
    }

    public void setOtherExp(double otherExp) {
        this.otherExp = otherExp;
    }

    public double getTotalCoin() {
        return this.totalCoin;
    }

    public void setTotalCoin(double totalCoin) {
        this.totalCoin = totalCoin;
    }

    public double getNotStreakCoin() {
        return this.notStreakCoin;
    }

    public void setNotStreakCoin(double notStreakCoin) {
        this.notStreakCoin = notStreakCoin;
    }

    public double getStreakCoin() {
        return this.streakCoin;
    }

    public void setStreakCoin(double streakCoin) {
        this.streakCoin = streakCoin;
    }

    public double getLevelDisparityCoin() {
        return this.levelDisparityCoin;
    }

    public void setLevelDisparityCoin(double levelDisparityCoin) {
        this.levelDisparityCoin = levelDisparityCoin;
    }

    public double getBaseCoin() {
        return this.baseCoin;
    }

    public void setBaseCoin(double baseCoin) {
        this.baseCoin = baseCoin;
    }

    public double getOtherCoin() {
        return this.otherCoin;
    }

    public void setOtherCoin(double otherCoin) {
        this.otherCoin = otherCoin;
    }

    public List<AssistData> getAssistData() {
        return this.assistData;
    }

    public void setAssistData(List<AssistData> assistData) {
        this.assistData = assistData;
    }

    public List<DamageData> getDamageLogs() {
        return this.damageLogs;
    }

    public void setDamageLogs(List<DamageData> damageLogs) {
        this.damageLogs = damageLogs;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof KillRecap)) return false;
        final KillRecap other = (KillRecap) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getCompleteTime() != other.getCompleteTime()) return false;
        final Object this$killer = this.getKiller();
        final Object other$killer = other.getKiller();
        if (this$killer == null ? other$killer != null : !this$killer.equals(other$killer)) return false;
        final Object this$perk = this.getPerk();
        final Object other$perk = other.getPerk();
        if (this$perk == null ? other$perk != null : !this$perk.equals(other$perk)) return false;
        if (Double.compare(this.getTotalExp(), other.getTotalExp()) != 0) return false;
        if (Double.compare(this.getBaseExp(), other.getBaseExp()) != 0) return false;
        if (Double.compare(this.getNotStreakExp(), other.getNotStreakExp()) != 0) return false;
        if (Double.compare(this.getStreakExp(), other.getStreakExp()) != 0) return false;
        if (Double.compare(this.getLevelDisparityExp(), other.getLevelDisparityExp()) != 0) return false;
        if (Double.compare(this.getOtherExp(), other.getOtherExp()) != 0) return false;
        if (Double.compare(this.getTotalCoin(), other.getTotalCoin()) != 0) return false;
        if (Double.compare(this.getNotStreakCoin(), other.getNotStreakCoin()) != 0) return false;
        if (Double.compare(this.getStreakCoin(), other.getStreakCoin()) != 0) return false;
        if (Double.compare(this.getLevelDisparityCoin(), other.getLevelDisparityCoin()) != 0) return false;
        if (Double.compare(this.getBaseCoin(), other.getBaseCoin()) != 0) return false;
        if (Double.compare(this.getOtherCoin(), other.getOtherCoin()) != 0) return false;
        final Object this$assistData = this.getAssistData();
        final Object other$assistData = other.getAssistData();
        if (this$assistData == null ? other$assistData != null : !this$assistData.equals(other$assistData))
            return false;
        final Object this$damageLogs = this.getDamageLogs();
        final Object other$damageLogs = other.getDamageLogs();
        if (this$damageLogs == null ? other$damageLogs != null : !this$damageLogs.equals(other$damageLogs))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof KillRecap;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $completeTime = this.getCompleteTime();
        result = result * PRIME + (int) ($completeTime >>> 32 ^ $completeTime);
        final Object $killer = this.getKiller();
        result = result * PRIME + ($killer == null ? 43 : $killer.hashCode());
        final Object $perk = this.getPerk();
        result = result * PRIME + ($perk == null ? 43 : $perk.hashCode());
        final long $totalExp = Double.doubleToLongBits(this.getTotalExp());
        result = result * PRIME + (int) ($totalExp >>> 32 ^ $totalExp);
        final long $baseExp = Double.doubleToLongBits(this.getBaseExp());
        result = result * PRIME + (int) ($baseExp >>> 32 ^ $baseExp);
        final long $notStreakExp = Double.doubleToLongBits(this.getNotStreakExp());
        result = result * PRIME + (int) ($notStreakExp >>> 32 ^ $notStreakExp);
        final long $streakExp = Double.doubleToLongBits(this.getStreakExp());
        result = result * PRIME + (int) ($streakExp >>> 32 ^ $streakExp);
        final long $levelDisparityExp = Double.doubleToLongBits(this.getLevelDisparityExp());
        result = result * PRIME + (int) ($levelDisparityExp >>> 32 ^ $levelDisparityExp);
        final long $otherExp = Double.doubleToLongBits(this.getOtherExp());
        result = result * PRIME + (int) ($otherExp >>> 32 ^ $otherExp);
        final long $totalCoin = Double.doubleToLongBits(this.getTotalCoin());
        result = result * PRIME + (int) ($totalCoin >>> 32 ^ $totalCoin);
        final long $notStreakCoin = Double.doubleToLongBits(this.getNotStreakCoin());
        result = result * PRIME + (int) ($notStreakCoin >>> 32 ^ $notStreakCoin);
        final long $streakCoin = Double.doubleToLongBits(this.getStreakCoin());
        result = result * PRIME + (int) ($streakCoin >>> 32 ^ $streakCoin);
        final long $levelDisparityCoin = Double.doubleToLongBits(this.getLevelDisparityCoin());
        result = result * PRIME + (int) ($levelDisparityCoin >>> 32 ^ $levelDisparityCoin);
        final long $baseCoin = Double.doubleToLongBits(this.getBaseCoin());
        result = result * PRIME + (int) ($baseCoin >>> 32 ^ $baseCoin);
        final long $otherCoin = Double.doubleToLongBits(this.getOtherCoin());
        result = result * PRIME + (int) ($otherCoin >>> 32 ^ $otherCoin);
        final Object $assistData = this.getAssistData();
        result = result * PRIME + ($assistData == null ? 43 : $assistData.hashCode());
        final Object $damageLogs = this.getDamageLogs();
        result = result * PRIME + ($damageLogs == null ? 43 : $damageLogs.hashCode());
        return result;
    }

    public String toString() {
        return "KillRecap(completeTime=" + this.getCompleteTime() + ", killer=" + this.getKiller() + ", perk=" + this.getPerk() + ", totalExp=" + this.getTotalExp() + ", baseExp=" + this.getBaseExp() + ", notStreakExp=" + this.getNotStreakExp() + ", streakExp=" + this.getStreakExp() + ", levelDisparityExp=" + this.getLevelDisparityExp() + ", otherExp=" + this.getOtherExp() + ", totalCoin=" + this.getTotalCoin() + ", notStreakCoin=" + this.getNotStreakCoin() + ", streakCoin=" + this.getStreakCoin() + ", levelDisparityCoin=" + this.getLevelDisparityCoin() + ", baseCoin=" + this.getBaseCoin() + ", otherCoin=" + this.getOtherCoin() + ", assistData=" + this.getAssistData() + ", damageLogs=" + this.getDamageLogs() + ")";
    }

    public void completeLog(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        this.completeTime = System.currentTimeMillis();
        profile.setKillRecap(new KillRecap());

        recapMap.put(player.getUniqueId(), this);
    }

    public static class DamageData {
        private boolean attack;
        private boolean melee;
        private double damage;
        private double boostDamage;
        private String displayName;
        private double afterHealth;
        private ItemStack usedItem;

        private Cooldown timer;

        public DamageData() {
        }

        public boolean isAttack() {
            return this.attack;
        }

        public void setAttack(boolean attack) {
            this.attack = attack;
        }

        public boolean isMelee() {
            return this.melee;
        }

        public void setMelee(boolean melee) {
            this.melee = melee;
        }

        public double getDamage() {
            return this.damage;
        }

        public void setDamage(double damage) {
            this.damage = damage;
        }

        public double getBoostDamage() {
            return this.boostDamage;
        }

        public void setBoostDamage(double boostDamage) {
            this.boostDamage = boostDamage;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public double getAfterHealth() {
            return this.afterHealth;
        }

        public void setAfterHealth(double afterHealth) {
            this.afterHealth = afterHealth;
        }

        public ItemStack getUsedItem() {
            return this.usedItem;
        }

        public void setUsedItem(ItemStack usedItem) {
            this.usedItem = usedItem;
        }

        public Cooldown getTimer() {
            return this.timer;
        }

        public void setTimer(Cooldown timer) {
            this.timer = timer;
        }

        public boolean equals(final Object o) {
            if (o == this) return true;
            if (!(o instanceof DamageData)) return false;
            final DamageData other = (DamageData) o;
            if (!other.canEqual((Object) this)) return false;
            if (this.isAttack() != other.isAttack()) return false;
            if (this.isMelee() != other.isMelee()) return false;
            if (Double.compare(this.getDamage(), other.getDamage()) != 0) return false;
            if (Double.compare(this.getBoostDamage(), other.getBoostDamage()) != 0) return false;
            final Object this$displayName = this.getDisplayName();
            final Object other$displayName = other.getDisplayName();
            if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName))
                return false;
            if (Double.compare(this.getAfterHealth(), other.getAfterHealth()) != 0) return false;
            final Object this$usedItem = this.getUsedItem();
            final Object other$usedItem = other.getUsedItem();
            if (this$usedItem == null ? other$usedItem != null : !this$usedItem.equals(other$usedItem)) return false;
            final Object this$timer = this.getTimer();
            final Object other$timer = other.getTimer();
            if (this$timer == null ? other$timer != null : !this$timer.equals(other$timer)) return false;
            return true;
        }

        protected boolean canEqual(final Object other) {
            return other instanceof DamageData;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + (this.isAttack() ? 79 : 97);
            result = result * PRIME + (this.isMelee() ? 79 : 97);
            final long $damage = Double.doubleToLongBits(this.getDamage());
            result = result * PRIME + (int) ($damage >>> 32 ^ $damage);
            final long $boostDamage = Double.doubleToLongBits(this.getBoostDamage());
            result = result * PRIME + (int) ($boostDamage >>> 32 ^ $boostDamage);
            final Object $displayName = this.getDisplayName();
            result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
            final long $afterHealth = Double.doubleToLongBits(this.getAfterHealth());
            result = result * PRIME + (int) ($afterHealth >>> 32 ^ $afterHealth);
            final Object $usedItem = this.getUsedItem();
            result = result * PRIME + ($usedItem == null ? 43 : $usedItem.hashCode());
            final Object $timer = this.getTimer();
            result = result * PRIME + ($timer == null ? 43 : $timer.hashCode());
            return result;
        }

        public String toString() {
            return "KillRecap.DamageData(attack=" + this.isAttack() + ", melee=" + this.isMelee() + ", damage=" + this.getDamage() + ", boostDamage=" + this.getBoostDamage() + ", displayName=" + this.getDisplayName() + ", afterHealth=" + this.getAfterHealth() + ", usedItem=" + this.getUsedItem() + ", timer=" + this.getTimer() + ")";
        }
    }

    public static class AssistData {
        private String displayName;
        private double percentage;

        private double baseExp;
        //连杀为5的倍数的exp
        private double streakExp;
        //等级差距exp
        private double levelDisparityExp;

        private double streakCoin;
        private double levelDisparityCoin;
        private double baseCoin;

        private double totalCoin;
        private double totalExp;

        public double getBaseExp() {
            return baseExp * percentage;
        }

        public void setBaseExp(double baseExp) {
            this.baseExp = baseExp;
        }

        public double getStreakExp() {
            return streakExp * percentage;
        }

        public void setStreakExp(double streakExp) {
            this.streakExp = streakExp;
        }

        public double getLevelDisparityExp() {
            return levelDisparityExp * percentage;
        }

        public void setLevelDisparityExp(double levelDisparityExp) {
            this.levelDisparityExp = levelDisparityExp;
        }

        public double getStreakCoin() {
            return streakCoin * percentage;
        }

        public void setStreakCoin(double streakCoin) {
            this.streakCoin = streakCoin;
        }

        public double getLevelDisparityCoin() {
            return levelDisparityCoin * percentage;
        }

        public void setLevelDisparityCoin(double levelDisparityCoin) {
            this.levelDisparityCoin = levelDisparityCoin;
        }

        public double getBaseCoin() {
            return baseCoin * percentage;
        }

        public void setBaseCoin(double baseCoin) {
            this.baseCoin = baseCoin;
        }

        public double getTotalCoin() {
            return totalCoin * percentage;
        }

        public void setTotalCoin(double totalCoin) {
            this.totalCoin = totalCoin;
        }

        public double getTotalExp() {
            return totalExp * percentage;
        }

        public void setTotalExp(double totalExp) {
            this.totalExp = totalExp;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public double getPercentage() {
            return this.percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }
    }
}
