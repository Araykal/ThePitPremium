package cn.charlotte.pit.data;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.sub.PlayerEnderChest;
import cn.charlotte.pit.data.sub.PlayerInv;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.SneakyThrows;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/17 21:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerInvBackup {
    private String uuid;
    private long timeStamp;
    private String backupUuid;

    private PlayerInv inv;
    private PlayerEnderChest chest;

    public PlayerInvBackup() {
    }

    @SneakyThrows
    public void save() {
        ThePit.getInstance()
                .getMongoDB()
                .getInvCollection()
                .replaceOne(Filters.eq("backupUuid", backupUuid), this, new ReplaceOptions().upsert(true));
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getBackupUuid() {
        return this.backupUuid;
    }

    public void setBackupUuid(String backupUuid) {
        this.backupUuid = backupUuid;
    }

    public PlayerInv getInv() {
        return this.inv;
    }

    public void setInv(PlayerInv inv) {
        this.inv = inv;
    }

    public PlayerEnderChest getChest() {
        return this.chest;
    }

    public void setChest(PlayerEnderChest chest) {
        this.chest = chest;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerInvBackup)) return false;
        final PlayerInvBackup other = (PlayerInvBackup) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        if (this.getTimeStamp() != other.getTimeStamp()) return false;
        final Object this$backupUuid = this.getBackupUuid();
        final Object other$backupUuid = other.getBackupUuid();
        if (this$backupUuid == null ? other$backupUuid != null : !this$backupUuid.equals(other$backupUuid))
            return false;
        final Object this$inv = this.getInv();
        final Object other$inv = other.getInv();
        if (this$inv == null ? other$inv != null : !this$inv.equals(other$inv)) return false;
        final Object this$chest = this.getChest();
        final Object other$chest = other.getChest();
        if (this$chest == null ? other$chest != null : !this$chest.equals(other$chest)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerInvBackup;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final long $timeStamp = this.getTimeStamp();
        result = result * PRIME + (int) ($timeStamp >>> 32 ^ $timeStamp);
        final Object $backupUuid = this.getBackupUuid();
        result = result * PRIME + ($backupUuid == null ? 43 : $backupUuid.hashCode());
        final Object $inv = this.getInv();
        result = result * PRIME + ($inv == null ? 43 : $inv.hashCode());
        final Object $chest = this.getChest();
        result = result * PRIME + ($chest == null ? 43 : $chest.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerInvBackup(uuid=" + this.getUuid() + ", timeStamp=" + this.getTimeStamp() + ", backupUuid=" + this.getBackupUuid() + ", inv=" + this.getInv() + ", chest=" + this.getChest() + ")";
    }
}
