package cn.charlotte.pit.data;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.mail.Mail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/16 22:51
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerMailData {
    private String uuid;
    private String name;
    private String nameLower;
    private List<Mail> mails;

    public PlayerMailData() {
        this.mails = new ArrayList<>();
    }

    public PlayerMailData(UUID uuid, String name) {
        this();

        this.uuid = uuid.toString();
        this.name = name;
        this.nameLower = name.toLowerCase();
    }

    public void cleanUp() {
        final List<Mail> list = this.mails
                .stream()
                .filter(mail -> mail.getUuid() == null || mail.getUuid().equals("null"))
                .collect(Collectors.toList());

        mails.removeAll(list);
    }

    public void sendMail(Mail mail) {
        if (mails.size() >= 99) {
            this.mails = this.mails.subList(0, 97);
        }
        this.mails.add(mail);
        save();
    }

    /**
     * Shouldn't call this method on primary thread
     * when player claimed mail
     * or sent mail to player
     * you should call this method.DONT FORGET!!!
     */
    public void save() {
        ThePit.getInstance()
                .getMongoDB()
                .getMailCollection()
                .replaceOne(Filters.eq("uuid", this.uuid), this, new ReplaceOptions().upsert(true));
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLower() {
        return this.nameLower;
    }

    public void setNameLower(String nameLower) {
        this.nameLower = nameLower;
    }

    public List<Mail> getMails() {
        return this.mails;
    }

    public void setMails(List<Mail> mails) {
        this.mails = mails;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerMailData)) return false;
        final PlayerMailData other = (PlayerMailData) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$uuid = this.getUuid();
        final Object other$uuid = other.getUuid();
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$nameLower = this.getNameLower();
        final Object other$nameLower = other.getNameLower();
        if (this$nameLower == null ? other$nameLower != null : !this$nameLower.equals(other$nameLower)) return false;
        final Object this$mails = this.getMails();
        final Object other$mails = other.getMails();
        if (this$mails == null ? other$mails != null : !this$mails.equals(other$mails)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerMailData;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.getUuid();
        result = result * PRIME + ($uuid == null ? 43 : $uuid.hashCode());
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $nameLower = this.getNameLower();
        result = result * PRIME + ($nameLower == null ? 43 : $nameLower.hashCode());
        final Object $mails = this.getMails();
        result = result * PRIME + ($mails == null ? 43 : $mails.hashCode());
        return result;
    }

    public String toString() {
        return "PlayerMailData(uuid=" + this.getUuid() + ", name=" + this.getName() + ", nameLower=" + this.getNameLower() + ", mails=" + this.getMails() + ")";
    }
}
