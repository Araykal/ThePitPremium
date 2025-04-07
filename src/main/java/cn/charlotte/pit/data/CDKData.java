package cn.charlotte.pit.data;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.sub.PlayerInv;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/25 13:26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CDKData {
    private static volatile Map<String, CDKData> cachedCDK = new HashMap<>();
    private static boolean loading;

    private String cdk;
    private long timeout;
    private double limitLevel;
    private String limitPermission;

    private double exp;
    private double coins;
    private int renown;
    private PlayerInv item = new PlayerInv();
    private long sendTime;
    private long expireTime;
    private int limitPrestige = -1;
    private int limitClaimed;
    private List<String> claimedPlayers = new ArrayList<>();

    public CDKData() {
    }

    public static void loadAllCDKFromData() {
        loading = true;
        cachedCDK.clear();
        for (CDKData data : ThePit.getInstance()
                .getMongoDB()
                .getCdkCollection()
                .find()) {
            cachedCDK.put(data.cdk, data);
        }
        loading = false;
    }

    public static Map<String, CDKData> getCachedCDK() {
        return CDKData.cachedCDK;
    }

    public static boolean isLoading() {
        return CDKData.loading;
    }

    public void active() {
        ThePit.getInstance()
                .getMongoDB()
                .getCdkCollection()
                .replaceOne(Filters.eq("cdk", cdk), this, new ReplaceOptions().upsert(true));
        cachedCDK.put(cdk, this);
    }
    public int getLimitPrestige() {
        return limitPrestige;
    }
    public void setLimitPrestige(int limitPrestige) {
        this.limitPrestige = limitPrestige;
    }
    public String getCdk() {
        return this.cdk;
    }

    public void setCdk(String cdk) {
        this.cdk = cdk;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public double getLimitLevel() {
        return this.limitLevel;
    }

    public void setLimitLevel(double limitLevel) {
        this.limitLevel = limitLevel;
    }

    public String getLimitPermission() {
        return this.limitPermission;
    }

    public void setLimitPermission(String limitPermission) {
        this.limitPermission = limitPermission;
    }

    public double getExp() {
        return this.exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getCoins() {
        return this.coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public int getRenown() {
        return this.renown;
    }

    public void setRenown(int renown) {
        this.renown = renown;
    }

    public PlayerInv getItem() {
        return this.item;
    }

    public void setItem(PlayerInv item) {
        this.item = item;
    }

    public long getSendTime() {
        return this.sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public int getLimitClaimed() {
        return this.limitClaimed;
    }

    public void setLimitClaimed(int limitClaimed) {
        this.limitClaimed = limitClaimed;
    }

    public List<String> getClaimedPlayers() {
        return this.claimedPlayers;
    }

    public void setClaimedPlayers(List<String> claimedPlayers) {
        this.claimedPlayers = claimedPlayers;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CDKData)) return false;
        final CDKData other = (CDKData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$cdk = this.getCdk();
        final Object other$cdk = other.getCdk();
        if (this$cdk == null ? other$cdk != null : !this$cdk.equals(other$cdk)) return false;
        if (this.getTimeout() != other.getTimeout()) return false;
        if (Double.compare(this.getLimitLevel(), other.getLimitLevel()) != 0) return false;
        final Object this$limitPermission = this.getLimitPermission();
        final Object other$limitPermission = other.getLimitPermission();
        if (this$limitPermission == null ? other$limitPermission != null : !this$limitPermission.equals(other$limitPermission))
            return false;
        if (Double.compare(this.getExp(), other.getExp()) != 0) return false;
        if (Double.compare(this.getCoins(), other.getCoins()) != 0) return false;
        if (this.getRenown() != other.getRenown()) return false;
        final Object this$item = this.getItem();
        final Object other$item = other.getItem();
        if (this$item == null ? other$item != null : !this$item.equals(other$item)) return false;
        if (this.getSendTime() != other.getSendTime()) return false;
        if (this.getExpireTime() != other.getExpireTime()) return false;
        if (this.getLimitClaimed() != other.getLimitClaimed()) return false;
        final Object this$claimedPlayers = this.getClaimedPlayers();
        final Object other$claimedPlayers = other.getClaimedPlayers();
        if (this$claimedPlayers == null ? other$claimedPlayers != null : !this$claimedPlayers.equals(other$claimedPlayers))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CDKData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $cdk = this.getCdk();
        result = result * PRIME + ($cdk == null ? 43 : $cdk.hashCode());
        final long $timeout = this.getTimeout();
        result = result * PRIME + (int) ($timeout >>> 32 ^ $timeout);
        final long $limitLevel = Double.doubleToLongBits(this.getLimitLevel());
        result = result * PRIME + (int) ($limitLevel >>> 32 ^ $limitLevel);
        final Object $limitPermission = this.getLimitPermission();
        result = result * PRIME + ($limitPermission == null ? 43 : $limitPermission.hashCode());
        final long $exp = Double.doubleToLongBits(this.getExp());
        result = result * PRIME + (int) ($exp >>> 32 ^ $exp);
        final long $coins = Double.doubleToLongBits(this.getCoins());
        result = result * PRIME + (int) ($coins >>> 32 ^ $coins);
        result = result * PRIME + this.getRenown();
        final Object $item = this.getItem();
        result = result * PRIME + ($item == null ? 43 : $item.hashCode());
        final long $sendTime = this.getSendTime();
        result = result * PRIME + (int) ($sendTime >>> 32 ^ $sendTime);
        final long $expireTime = this.getExpireTime();
        result = result * PRIME + (int) ($expireTime >>> 32 ^ $expireTime);
        result = result * PRIME + this.getLimitClaimed();
        final Object $claimedPlayers = this.getClaimedPlayers();
        result = result * PRIME + ($claimedPlayers == null ? 43 : $claimedPlayers.hashCode());
        return result;
    }

    public String toString() {
        return "CDKData(cdk=" + this.getCdk() + ", timeout=" + this.getTimeout() + ", limitLevel=" + this.getLimitLevel() + ", limitPermission=" + this.getLimitPermission() + ", exp=" + this.getExp() + ", coins=" + this.getCoins() + ", renown=" + this.getRenown() + ", item=" + this.getItem() + ", sendTime=" + this.getSendTime() + ", expireTime=" + this.getExpireTime() + ", limitClaimed=" + this.getLimitClaimed() + ", claimedPlayers=" + this.getClaimedPlayers() + ")";
    }
}
