package cn.charlotte.pit.data.mail;

import cn.charlotte.pit.data.sub.PlayerInv;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/16 22:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mail {
    private String uuid;
    private String title;
    private String content;
    private double exp;
    private double coins;
    private int renown;
    private PlayerInv item;
    private long sendTime;
    private long expireTime;
    private boolean claimed;

    public Mail() {
        this.item = new PlayerInv();
        this.build();
    }

    public void build() {
        this.uuid = UUID.randomUUID().toString();
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isClaimed() {
        return this.claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Mail)) return false;
        final Mail other = (Mail) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        if (Double.compare(this.getExp(), other.getExp()) != 0) return false;
        if (Double.compare(this.getCoins(), other.getCoins()) != 0) return false;
        if (this.getRenown() != other.getRenown()) return false;
        final Object this$item = this.getItem();
        final Object other$item = other.getItem();
        if (this$item == null ? other$item != null : !this$item.equals(other$item)) return false;
        if (this.getSendTime() != other.getSendTime()) return false;
        if (this.getExpireTime() != other.getExpireTime()) return false;
        if (this.isClaimed() != other.isClaimed()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Mail;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
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
        result = result * PRIME + (this.isClaimed() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "Mail(uuid=" + this.getUuid() + ", title=" + this.getTitle() + ", content=" + this.getContent() + ", exp=" + this.getExp() + ", coins=" + this.getCoins() + ", renown=" + this.getRenown() + ", item=" + this.getItem() + ", sendTime=" + this.getSendTime() + ", expireTime=" + this.getExpireTime() + ", claimed=" + this.isClaimed() + ")";
    }
}
