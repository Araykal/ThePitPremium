package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/6 23:30
 */
public class EmailData {
    private String title;
    private String content;
    private double exp;
    private double coins;
    private double renown;
    private List<ItemStack> items = new ArrayList<>();
    private List<AbstractPerk> perks = new ArrayList<>();
    private long sendTime;
    private long expireTime;

    public EmailData(String title) {
        this.title = title;
    }

    public static EmailData loadFromDocument(Document document) {
        EmailData emailData = new EmailData(document.getString("title"));
        emailData.setContent(document.getString("content"));
        emailData.setExp(document.getDouble("exp"));
        emailData.setCoins(document.getDouble("coins"));
        emailData.setRenown(document.getDouble("renown"));

        PlayerInv inv = InventoryUtil.playerInventoryFromString(document.getString("items"));
        List<ItemStack> items = new ArrayList<>(Arrays.asList(inv.getContents()));

        emailData.setItems(items);

        String[] perks = document.getString("perk").split(":");
        List<AbstractPerk> perkList = new ArrayList<>();
        if (perks.length > 0) {
            for (String perk : perks) {
                ThePit.getInstance()
                        .getPerkFactory()
                        .getPerks()
                        .stream()
                        .filter(abstractPerk -> abstractPerk.getInternalPerkName().equals(perk))
                        .findFirst()
                        .ifPresent(perkList::add);
            }
        }

        emailData.setPerks(perkList);

        return emailData;
    }

    public void sendToPlayer(String name) {

    }

    public Document toSave() {
        Document document = new Document();

        document.put("title", title);
        document.put("content", content);
        document.put("exp", exp);
        document.put("coins", coins);
        document.put("renown", renown);

        PlayerInv playerInv = new PlayerInv();
        playerInv.setContents(items.toArray(new ItemStack[0]));
        String s = InventoryUtil.playerInvToString(playerInv);

        document.put("items", s);

        StringBuilder builder = new StringBuilder();
        builder.append(":");
        for (AbstractPerk perk : perks) {
            builder.append(perk.getInternalPerkName())
                    .append(":");
        }

        document.put("perk", builder.toString());

        return document;
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

    public double getRenown() {
        return this.renown;
    }

    public void setRenown(double renown) {
        this.renown = renown;
    }

    public List<ItemStack> getItems() {
        return this.items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public List<AbstractPerk> getPerks() {
        return this.perks;
    }

    public void setPerks(List<AbstractPerk> perks) {
        this.perks = perks;
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

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof EmailData)) return false;
        final EmailData other = (EmailData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) return false;
        if (Double.compare(this.getExp(), other.getExp()) != 0) return false;
        if (Double.compare(this.getCoins(), other.getCoins()) != 0) return false;
        if (Double.compare(this.getRenown(), other.getRenown()) != 0) return false;
        final Object this$items = this.getItems();
        final Object other$items = other.getItems();
        if (this$items == null ? other$items != null : !this$items.equals(other$items)) return false;
        final Object this$perks = this.getPerks();
        final Object other$perks = other.getPerks();
        if (this$perks == null ? other$perks != null : !this$perks.equals(other$perks)) return false;
        if (this.getSendTime() != other.getSendTime()) return false;
        if (this.getExpireTime() != other.getExpireTime()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EmailData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.getTitle();
        result = result * PRIME + ($title == null ? 43 : $title.hashCode());
        final Object $content = this.getContent();
        result = result * PRIME + ($content == null ? 43 : $content.hashCode());
        final long $exp = Double.doubleToLongBits(this.getExp());
        result = result * PRIME + (int) ($exp >>> 32 ^ $exp);
        final long $coins = Double.doubleToLongBits(this.getCoins());
        result = result * PRIME + (int) ($coins >>> 32 ^ $coins);
        final long $renown = Double.doubleToLongBits(this.getRenown());
        result = result * PRIME + (int) ($renown >>> 32 ^ $renown);
        final Object $items = this.getItems();
        result = result * PRIME + ($items == null ? 43 : $items.hashCode());
        final Object $perks = this.getPerks();
        result = result * PRIME + ($perks == null ? 43 : $perks.hashCode());
        final long $sendTime = this.getSendTime();
        result = result * PRIME + (int) ($sendTime >>> 32 ^ $sendTime);
        final long $expireTime = this.getExpireTime();
        result = result * PRIME + (int) ($expireTime >>> 32 ^ $expireTime);
        return result;
    }

    public String toString() {
        return "EmailData(title=" + this.getTitle() + ", content=" + this.getContent() + ", exp=" + this.getExp() + ", coins=" + this.getCoins() + ", renown=" + this.getRenown() + ", items=" + this.getItems() + ", perks=" + this.getPerks() + ", sendTime=" + this.getSendTime() + ", expireTime=" + this.getExpireTime() + ")";
    }
}
