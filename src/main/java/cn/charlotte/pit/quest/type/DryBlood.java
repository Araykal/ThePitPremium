package cn.charlotte.pit.quest.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.QuestData;
import cn.charlotte.pit.event.PitRegainHealthEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.quest.AbstractQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import java.util.Collections;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/8 15:35
 */
@AutoRegister
public class DryBlood extends AbstractQuest implements Listener {
    @Override
    public String getQuestInternalName() {
        return "dried_blood";
    }

    @Override
    public String getQuestDisplayName() {
        return "断流";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public List<String> getQuestDescription(int level, boolean isNightQuest) {
        return Collections.singletonList("&f通过任何途径(除复活)恢复生命值时,恢复量 &c-" + (level * 0.5) + "❤");
    }

    @Override
    public long getDuration(int level) {
        return 5 * 60 * 1000;
    }

    @Override
    public int getTotal(int level) {
        return 10;
    }

    @EventHandler
    public void onHeal(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getEndTime() > System.currentTimeMillis()) {
            QuestData questData = profile.getCurrentQuest();
            if (!questData.getInternalName().equals(this.getQuestInternalName())) return;
            event.setAmount(Math.max(0, event.getAmount() - 2 * questData.getLevel()));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRegain(PitRegainHealthEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getEndTime() > System.currentTimeMillis()) {
            QuestData questData = profile.getCurrentQuest();
            if (!questData.getInternalName().equals(this.getQuestInternalName())) return;
            event.setAmount(Math.max(0, event.getAmount() - 2 * questData.getLevel()));
        }
    }

    @Override
    public void onActive(Player player, int level) {
        super.onActive(player, level);
    }

    @Override
    public void onInactive(Player player, int level) {
        super.onInactive(player, level);
    }
}
