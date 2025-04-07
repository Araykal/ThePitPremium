package cn.charlotte.pit.quest.type;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.quest.AbstractQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/20 21:47
 */
@AutoRegister
public class DeepInfiltration extends AbstractQuest implements Listener {
    @Override
    public String getQuestInternalName() {
        return "DeepInfiltration";
    }

    @Override
    public String getQuestDisplayName() {
        return "深度渗透";
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
        return Arrays.asList("&f饱食度上限 &c-" + (Math.min(35 * level, 100) + "%"));
    }


    @Override
    public long getDuration(int level) {
        return 4 * 60 * 1000;
    }

    @Override
    public int getTotal(int level) {
        return 10;
    }

    @Override
    public void onActive(Player player, int level) {
        super.onActive(player, level);
        int foodLevel = (int) (20 * (1 - (Math.min(0.35 * level, 1))));
        player.setFoodLevel(foodLevel);
        PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                .setFoodLevel(foodLevel);
    }

    @Override
    public void onInactive(Player player, int level) {
        super.onInactive(player, level);
        player.setFoodLevel(20);
        PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                .setFoodLevel(20);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onFoodLevel(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            if (profile.getCurrentQuest() != null && profile.getCurrentQuest().getInternalName().equalsIgnoreCase(this.getQuestInternalName())) {
                event.setCancelled(false);
                double level = 20 * (1 - (Math.min(0.35 * profile.getCurrentQuest().getLevel(), 1)));
                event.setFoodLevel(new Double(level).intValue());
            }
        }
    }
}
