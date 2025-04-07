package cn.charlotte.pit.data;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.sub.PlayerInv;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/27 13:03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeData {
    private static ExecutorService executor = new ScheduledThreadPoolExecutor(3);

    private String tradeUuid;
    private String playerA;
    private String playerB;
    private String playerAName;
    private String playerBName;

    private long completeTime;

    private PlayerInv aPaidItem;
    private PlayerInv bPaidItem;

    private long aPaidCoin;
    private long bPaidCoin;

    public TradeData(String playerA, String playerB, String playerAName, String playerBName) {
        this.tradeUuid = UUID.randomUUID().toString();

        this.playerA = playerA;
        this.playerB = playerB;
        this.playerAName = playerAName;
        this.playerBName = playerBName;
    }

    public TradeData() {

    }

    public static ExecutorService getExecutor() {
        return TradeData.executor;
    }

    public void save() {
        executor.execute(() -> {
            ThePit.getInstance()
                    .getMongoDB()
                    .getTradeCollection()
                    .replaceOne(Filters.eq("tradeUuid", tradeUuid), this, new ReplaceOptions().upsert(true));
        });
    }

    public String getTradeUuid() {
        return this.tradeUuid;
    }

    public void setTradeUuid(String tradeUuid) {
        this.tradeUuid = tradeUuid;
    }

    public String getPlayerA() {
        return this.playerA;
    }

    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    public String getPlayerB() {
        return this.playerB;
    }

    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    public String getPlayerAName() {
        return this.playerAName;
    }

    public void setPlayerAName(String playerAName) {
        this.playerAName = playerAName;
    }

    public String getPlayerBName() {
        return this.playerBName;
    }

    public void setPlayerBName(String playerBName) {
        this.playerBName = playerBName;
    }

    public long getCompleteTime() {
        return this.completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public PlayerInv getAPaidItem() {
        return this.aPaidItem;
    }

    public void setAPaidItem(PlayerInv aPaidItem) {
        this.aPaidItem = aPaidItem;
    }

    public PlayerInv getBPaidItem() {
        return this.bPaidItem;
    }

    public void setBPaidItem(PlayerInv bPaidItem) {
        this.bPaidItem = bPaidItem;
    }

    public long getAPaidCoin() {
        return this.aPaidCoin;
    }

    public void setAPaidCoin(long aPaidCoin) {
        this.aPaidCoin = aPaidCoin;
    }

    public long getBPaidCoin() {
        return this.bPaidCoin;
    }

    public void setBPaidCoin(long bPaidCoin) {
        this.bPaidCoin = bPaidCoin;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof TradeData)) return false;
        final TradeData other = (TradeData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$tradeUuid = this.getTradeUuid();
        final Object other$tradeUuid = other.getTradeUuid();
        if (this$tradeUuid == null ? other$tradeUuid != null : !this$tradeUuid.equals(other$tradeUuid)) return false;
        final Object this$playerA = this.getPlayerA();
        final Object other$playerA = other.getPlayerA();
        if (this$playerA == null ? other$playerA != null : !this$playerA.equals(other$playerA)) return false;
        final Object this$playerB = this.getPlayerB();
        final Object other$playerB = other.getPlayerB();
        if (this$playerB == null ? other$playerB != null : !this$playerB.equals(other$playerB)) return false;
        final Object this$playerAName = this.getPlayerAName();
        final Object other$playerAName = other.getPlayerAName();
        if (this$playerAName == null ? other$playerAName != null : !this$playerAName.equals(other$playerAName))
            return false;
        final Object this$playerBName = this.getPlayerBName();
        final Object other$playerBName = other.getPlayerBName();
        if (this$playerBName == null ? other$playerBName != null : !this$playerBName.equals(other$playerBName))
            return false;
        if (this.getCompleteTime() != other.getCompleteTime()) return false;
        final Object this$aPaidItem = this.getAPaidItem();
        final Object other$aPaidItem = other.getAPaidItem();
        if (this$aPaidItem == null ? other$aPaidItem != null : !this$aPaidItem.equals(other$aPaidItem)) return false;
        final Object this$bPaidItem = this.getBPaidItem();
        final Object other$bPaidItem = other.getBPaidItem();
        if (this$bPaidItem == null ? other$bPaidItem != null : !this$bPaidItem.equals(other$bPaidItem)) return false;
        if (this.getAPaidCoin() != other.getAPaidCoin()) return false;
        if (this.getBPaidCoin() != other.getBPaidCoin()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof TradeData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $tradeUuid = this.getTradeUuid();
        result = result * PRIME + ($tradeUuid == null ? 43 : $tradeUuid.hashCode());
        final Object $playerA = this.getPlayerA();
        result = result * PRIME + ($playerA == null ? 43 : $playerA.hashCode());
        final Object $playerB = this.getPlayerB();
        result = result * PRIME + ($playerB == null ? 43 : $playerB.hashCode());
        final Object $playerAName = this.getPlayerAName();
        result = result * PRIME + ($playerAName == null ? 43 : $playerAName.hashCode());
        final Object $playerBName = this.getPlayerBName();
        result = result * PRIME + ($playerBName == null ? 43 : $playerBName.hashCode());
        final long $completeTime = this.getCompleteTime();
        result = result * PRIME + (int) ($completeTime >>> 32 ^ $completeTime);
        final Object $aPaidItem = this.getAPaidItem();
        result = result * PRIME + ($aPaidItem == null ? 43 : $aPaidItem.hashCode());
        final Object $bPaidItem = this.getBPaidItem();
        result = result * PRIME + ($bPaidItem == null ? 43 : $bPaidItem.hashCode());
        final long $aPaidCoin = this.getAPaidCoin();
        result = result * PRIME + (int) ($aPaidCoin >>> 32 ^ $aPaidCoin);
        final long $bPaidCoin = this.getBPaidCoin();
        result = result * PRIME + (int) ($bPaidCoin >>> 32 ^ $bPaidCoin);
        return result;
    }

    public String toString() {
        return "TradeData(tradeUuid=" + this.getTradeUuid() + ", playerA=" + this.getPlayerA() + ", playerB=" + this.getPlayerB() + ", playerAName=" + this.getPlayerAName() + ", playerBName=" + this.getPlayerBName() + ", completeTime=" + this.getCompleteTime() + ", aPaidItem=" + this.getAPaidItem() + ", bPaidItem=" + this.getBPaidItem() + ", aPaidCoin=" + this.getAPaidCoin() + ", bPaidCoin=" + this.getBPaidCoin() + ")";
    }
}
