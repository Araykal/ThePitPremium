package cn.charlotte.pit.menu.trade;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.TradeData;
import cn.charlotte.pit.data.sub.PlayerInv;
import cn.charlotte.pit.medal.impl.challenge.FirstTradeMedal;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.ChatComponentBuilder;
import cn.charlotte.pit.util.chat.StringUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.rank.RankUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 20:57
 */
public class TradeManager {
    public static final Map<UUID, TradeManager> trades = new HashMap<>();

    private final Player playerA;
    private final Player playerB;
    private final List<ItemStack> aItems;
    private final List<ItemStack> bItems;
    private double aCoins;
    private double bCoins;

    private boolean aConfirm;
    private boolean bConfirm;

    private boolean aEditingCoins;
    private boolean bEditingCoins;

    private Cooldown aPutCooldown;
    private Cooldown bPutCooldown;

    private boolean end;

    public TradeManager(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.aItems = new ArrayList<>();
        this.bItems = new ArrayList<>();
        this.aPutCooldown = new Cooldown(5, TimeUnit.SECONDS);
        this.bPutCooldown = new Cooldown(5, TimeUnit.SECONDS);

        trades.put(playerA.getUniqueId(), this);
        trades.put(playerB.getUniqueId(), this);
    }

    public void openMenu(Player player) {
        if (end) {
            return;
        }
        new TradeMenu(this).openMenu(player);
    }


    public void onPlayerCancel(Player player) {
        if (end) {
            return;
        }
        end = true;
        if (player.equals(playerA)) {
            playerB.closeInventory();
        } else {
            playerA.closeInventory();
        }
        playerA.sendMessage(CC.translate("&c交易被取消."));
        playerB.sendMessage(CC.translate("&c交易被取消."));
        this.cancelTrade();
    }

    private void doTrade() {
        if (end) {
            return;
        }

        TradeData tradeData = new TradeData(this.playerA.getUniqueId().toString(), this.playerB.getUniqueId().toString(), this.playerA.getName(), this.getPlayerB().getName());
        tradeData.setAPaidCoin((long) this.aCoins);
        tradeData.setBPaidCoin((long) this.bCoins);

        PlayerInv aInv = new PlayerInv();
        aInv.setContents(aItems.toArray(new ItemStack[36]));
        tradeData.setAPaidItem(aInv);

        PlayerInv bInv = new PlayerInv();
        bInv.setContents(bItems.toArray(new ItemStack[36]));
        tradeData.setBPaidItem(bInv);

        tradeData.setCompleteTime(System.currentTimeMillis());

        tradeData.save();

        end = true;

        for (ItemStack item : aItems) {
            playerB.getInventory().addItem(item);
        }
        for (ItemStack item : bItems) {
            playerA.getInventory().addItem(item);
        }
        PlayerProfile profileA = PlayerProfile.getPlayerProfileByUuid(playerA.getUniqueId());
        profileA.setCoins(profileA.getCoins() + bCoins);

        PlayerProfile profileB = PlayerProfile.getPlayerProfileByUuid(playerB.getUniqueId());
        profileB.setCoins(profileB.getCoins() + aCoins);

        profileA.getTradeLimit().setAmount(profileA.getTradeLimit().getAmount() + aCoins + bCoins);
        profileB.getTradeLimit().setAmount(profileB.getTradeLimit().getAmount() + aCoins + bCoins);
        profileA.getTradeLimit().setTimes(profileA.getTradeLimit().getTimes() + 1);
        profileB.getTradeLimit().setTimes(profileB.getTradeLimit().getTimes() + 1);

        sendTradeCompleteMsg(profileB, playerB, playerA, bItems, bCoins, aItems, aCoins);
        sendTradeCompleteMsg(profileA, playerA, playerB, aItems, aCoins, bItems, bCoins);

        FirstTradeMedal medal = new FirstTradeMedal();
        medal.addProgress(profileA, 1);
        medal.addProgress(profileB, 1);

        aItems.clear();
        bItems.clear();
        aCoins = 0;
        bCoins = 0;


        ThePit.getInstance().getSoundFactory()
                .playSound("successfully", playerA);
        ThePit.getInstance().getSoundFactory()
                .playSound("successfully", playerB);

        trades.remove(playerA.getUniqueId());
        trades.remove(playerB.getUniqueId());

        Bukkit.getScheduler().runTaskLaterAsynchronously(ThePit.getInstance(), () -> {
            playerA.closeInventory();
            playerB.closeInventory();
        }, 1L);
    }

    private void sendTradeCompleteMsg(PlayerProfile profileB, Player playerB, Player playerA, List<ItemStack> bItems, double bCoins, List<ItemStack> aItems, double aCoins) {
        String nameB = RankUtil.getPlayerColoredName(playerB.getUniqueId());
        playerA.sendMessage(CC.translate("&6与 " + LevelUtil.getLevelTag(profileB.getPrestige(), profileB.getLevel()) + " " + nameB + " &6交易成功!"));


        if (bItems.size() > 0) {
            for (ItemStack item : bItems) {
                String message = "&a&l+ &7" + item.getAmount() + "x &f" + (item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null ? item.getItemMeta().getDisplayName() : item.getType().toString().toUpperCase());
                net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = new NBTTagCompound();
                nms.save(tag);
                BaseComponent[] hoverEventComponents = new BaseComponent[]{
                        new TextComponent(tag.toString())
                };
                playerA.spigot().sendMessage(new ChatComponentBuilder(CC.translate(message))
                        .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents)).create());
            }
        }
        if (bCoins > 0) {
            playerA.sendMessage(CC.translate("&a&l+ &6" + StringUtil.getFormatLong((long) bCoins) + "硬币"));
        }

        if (aItems.size() > 0) {
            for (ItemStack item : aItems) {
                String message = "&c&l- &7" + item.getAmount() + "x &f" + (item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null ? item.getItemMeta().getDisplayName() : item.getType().toString().toUpperCase());
                net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
                NBTTagCompound tag = new NBTTagCompound();
                nms.save(tag);
                BaseComponent[] hoverEventComponents = new BaseComponent[]{
                        new TextComponent(tag.toString())
                };
                playerA.spigot().sendMessage(new ChatComponentBuilder(CC.translate(message))
                        .setCurrentHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents)).create());
            }
        }
        if (aCoins > 0) {
            playerA.sendMessage(CC.translate("&c&l- &6" + StringUtil.getFormatLong((long) aCoins) + "硬币"));
        }
    }

    private void cancelTrade() {
        //fixme 无法关闭背包或者取消交易
        for (ItemStack item : aItems) {
            playerA.getInventory().addItem(item);
        }
        for (ItemStack item : bItems) {
            playerB.getInventory().addItem(item);
        }
        PlayerProfile profileA = PlayerProfile.getPlayerProfileByUuid(playerA.getUniqueId());
        profileA.setCoins(profileA.getCoins() + aCoins);

        PlayerProfile profileB = PlayerProfile.getPlayerProfileByUuid(playerB.getUniqueId());
        profileB.setCoins(profileB.getCoins() + bCoins);

        trades.remove(playerA.getUniqueId());
        trades.remove(playerB.getUniqueId());

    }

    public Player getPlayerA() {
        return this.playerA;
    }

    public Player getPlayerB() {
        return this.playerB;
    }

    public List<ItemStack> getAItems() {
        return this.aItems;
    }

    public List<ItemStack> getBItems() {
        return this.bItems;
    }

    public double getACoins() {
        return this.aCoins;
    }

    public void setACoins(double aCoins) {
        this.aCoins = aCoins;
    }

    public double getBCoins() {
        return this.bCoins;
    }

    public void setBCoins(double bCoins) {
        this.bCoins = bCoins;
    }

    public boolean isAConfirm() {
        return this.aConfirm;
    }

    public void setAConfirm(boolean aConfirm) {
        this.aConfirm = aConfirm;
        if (aConfirm && bConfirm) {
            doTrade();
        }
    }

    public boolean isBConfirm() {
        return this.bConfirm;
    }

    public void setBConfirm(boolean bConfirm) {
        this.bConfirm = bConfirm;
        if (aConfirm && bConfirm) {
            doTrade();
        }
    }

    public boolean isAEditingCoins() {
        return this.aEditingCoins;
    }

    public void setAEditingCoins(boolean aEditingCoins) {
        this.aEditingCoins = aEditingCoins;
    }

    public boolean isBEditingCoins() {
        return this.bEditingCoins;
    }

    public void setBEditingCoins(boolean bEditingCoins) {
        this.bEditingCoins = bEditingCoins;
    }

    public Cooldown getAPutCooldown() {
        return this.aPutCooldown;
    }

    public void setAPutCooldown(Cooldown aPutCooldown) {
        this.aPutCooldown = aPutCooldown;
    }

    public Cooldown getBPutCooldown() {
        return this.bPutCooldown;
    }

    public void setBPutCooldown(Cooldown bPutCooldown) {
        this.bPutCooldown = bPutCooldown;
    }

    public boolean isEnd() {
        return this.end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TradeManager)) return false;
        final TradeManager other = (TradeManager) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$playerA = this.getPlayerA();
        final Object other$playerA = other.getPlayerA();
        if (this$playerA == null ? other$playerA != null : !this$playerA.equals(other$playerA)) return false;
        final Object this$playerB = this.getPlayerB();
        final Object other$playerB = other.getPlayerB();
        if (this$playerB == null ? other$playerB != null : !this$playerB.equals(other$playerB)) return false;
        final Object this$aItems = this.getAItems();
        final Object other$aItems = other.getAItems();
        if (this$aItems == null ? other$aItems != null : !this$aItems.equals(other$aItems)) return false;
        final Object this$bItems = this.getBItems();
        final Object other$bItems = other.getBItems();
        if (this$bItems == null ? other$bItems != null : !this$bItems.equals(other$bItems)) return false;
        if (Double.compare(this.getACoins(), other.getACoins()) != 0) return false;
        if (Double.compare(this.getBCoins(), other.getBCoins()) != 0) return false;
        if (this.isAConfirm() != other.isAConfirm()) return false;
        if (this.isBConfirm() != other.isBConfirm()) return false;
        if (this.isAEditingCoins() != other.isAEditingCoins()) return false;
        if (this.isBEditingCoins() != other.isBEditingCoins()) return false;
        final Object this$aPutCooldown = this.getAPutCooldown();
        final Object other$aPutCooldown = other.getAPutCooldown();
        if (this$aPutCooldown == null ? other$aPutCooldown != null : !this$aPutCooldown.equals(other$aPutCooldown))
            return false;
        final Object this$bPutCooldown = this.getBPutCooldown();
        final Object other$bPutCooldown = other.getBPutCooldown();
        if (this$bPutCooldown == null ? other$bPutCooldown != null : !this$bPutCooldown.equals(other$bPutCooldown))
            return false;
        if (this.isEnd() != other.isEnd()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TradeManager;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $playerA = this.getPlayerA();
        result = result * PRIME + ($playerA == null ? 43 : $playerA.hashCode());
        final Object $playerB = this.getPlayerB();
        result = result * PRIME + ($playerB == null ? 43 : $playerB.hashCode());
        final Object $aItems = this.getAItems();
        result = result * PRIME + ($aItems == null ? 43 : $aItems.hashCode());
        final Object $bItems = this.getBItems();
        result = result * PRIME + ($bItems == null ? 43 : $bItems.hashCode());
        final long $aCoins = Double.doubleToLongBits(this.getACoins());
        result = result * PRIME + (int) ($aCoins >>> 32 ^ $aCoins);
        final long $bCoins = Double.doubleToLongBits(this.getBCoins());
        result = result * PRIME + (int) ($bCoins >>> 32 ^ $bCoins);
        result = result * PRIME + (this.isAConfirm() ? 79 : 97);
        result = result * PRIME + (this.isBConfirm() ? 79 : 97);
        result = result * PRIME + (this.isAEditingCoins() ? 79 : 97);
        result = result * PRIME + (this.isBEditingCoins() ? 79 : 97);
        final Object $aPutCooldown = this.getAPutCooldown();
        result = result * PRIME + ($aPutCooldown == null ? 43 : $aPutCooldown.hashCode());
        final Object $bPutCooldown = this.getBPutCooldown();
        result = result * PRIME + ($bPutCooldown == null ? 43 : $bPutCooldown.hashCode());
        result = result * PRIME + (this.isEnd() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "TradeManager(playerA=" + this.getPlayerA() + ", playerB=" + this.getPlayerB() + ", aItems=" + this.getAItems() + ", bItems=" + this.getBItems() + ", aCoins=" + this.getACoins() + ", bCoins=" + this.getBCoins() + ", aConfirm=" + this.isAConfirm() + ", bConfirm=" + this.isBConfirm() + ", aEditingCoins=" + this.isAEditingCoins() + ", bEditingCoins=" + this.isBEditingCoins() + ", aPutCooldown=" + this.getAPutCooldown() + ", bPutCooldown=" + this.getBPutCooldown() + ", end=" + this.isEnd() + ")";
    }
}
