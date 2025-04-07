package cn.charlotte.pit.data.sub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/13 21:53
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerOption {

    private boolean
            bountyNotify = true,
            streakNotify = true,
            prestigeNotify = true,
            eventNotify = true,
            combatNotify = true,
            chatMsg = true,
            otherMsg = true ,outfit = true,

    bountyHiddenWhenNear = false,


    tradeNotify = true;


    private BarPriority barPriority = BarPriority.DAMAGE_PRIORITY;
    //admin only
    private boolean debugDamageMessage = false;
    //vip only
    private boolean
            profileVisibility = true,
            inventoryVisibility = true,
            enderChestVisibility = true,
            supporterStarDisplay = true;

    public boolean isBountyNotify() {
        return bountyNotify;
    }

    public void setBountyNotify(boolean bountyNotify) {
        this.bountyNotify = bountyNotify;
    }

    public boolean isStreakNotify() {
        return streakNotify;
    }



    public void setStreakNotify(boolean streakNotify) {
        this.streakNotify = streakNotify;
    }

    public boolean isPrestigeNotify() {
        return prestigeNotify;
    }

    public void setPrestigeNotify(boolean prestigeNotify) {
        this.prestigeNotify = prestigeNotify;
    }

    public boolean isEventNotify() {
        return eventNotify;
    }

    public void setEventNotify(boolean eventNotify) {
        this.eventNotify = eventNotify;
    }

    public boolean isCombatNotify() {
        return combatNotify;
    }

    public void setCombatNotify(boolean combatNotify) {
        this.combatNotify = combatNotify;
    }

    public boolean isChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(boolean chatMsg) {
        this.chatMsg = chatMsg;
    }

    public boolean isOtherMsg() {
        return otherMsg;
    }

    public void setOtherMsg(boolean otherMsg) {
        this.otherMsg = otherMsg;
    }

    public boolean isBountyHiddenWhenNear() {
        return bountyHiddenWhenNear;
    }

    public void setBountyHiddenWhenNear(boolean bountyHiddenWhenNear) {
        this.bountyHiddenWhenNear = bountyHiddenWhenNear;
    }

    public boolean isTradeNotify() {
        return tradeNotify;
    }

    public void setTradeNotify(boolean tradeNotify) {
        this.tradeNotify = tradeNotify;
    }

    public BarPriority getBarPriority() {
        return barPriority;
    }

    public void setBarPriority(BarPriority barPriority) {
        this.barPriority = barPriority;
    }

    public boolean isDebugDamageMessage() {
        return debugDamageMessage;
    }

    public void setDebugDamageMessage(boolean debugDamageMessage) {
        this.debugDamageMessage = debugDamageMessage;
    }

    public boolean isProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(boolean profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public boolean isInventoryVisibility() {
        return inventoryVisibility;
    }

    public void setInventoryVisibility(boolean inventoryVisibility) {
        this.inventoryVisibility = inventoryVisibility;
    }

    public boolean isEnderChestVisibility() {
        return enderChestVisibility;
    }

    public void setEnderChestVisibility(boolean enderChestVisibility) {
        this.enderChestVisibility = enderChestVisibility;
    }

    public boolean isSupporterStarDisplay() {
        return supporterStarDisplay;
    }

    public void setSupporterStarDisplay(boolean supporterStarDisplay) {
        this.supporterStarDisplay = supporterStarDisplay;
    }

    public boolean isOutfit() {
        return outfit;
    }

    public void setOutfit(boolean outfit) {
        this.outfit = outfit;
    }

    public enum BarPriority {
        DAMAGE_ONLY("仅显示攻击目标状态"),
        ENCHANT_ONLY("仅显示附魔冷却状态"),
        DAMAGE_PRIORITY("优先显示攻击目标状态");

        private final String description;

        BarPriority(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }
}
