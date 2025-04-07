package cn.charlotte.pit.quest.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.quest.AbstractQuest;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Arrays;
import java.util.List;

/**
 * @Creator Misoryan
 * @Date 2021/5/4 12:23
 */
public class SinkingMoonlight extends AbstractQuest {
    @Override
    public String getQuestInternalName() {
        return "sinking_moonlight";
    }

    @Override
    public String getQuestDisplayName() {
        return "沉沦";
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinLevel() {
        return 2;
    }

    @Override
    public List<String> getQuestDescription(int level, boolean isNightQuest) {
        return Arrays.asList(
                "&f获得效果 &c沉默",
                "&f效果 &c沉默 &f: 装备的所有神话物品失效"
        );
    }

    @Override
    public long getDuration(int level) {
        return 5 * 60 * 1000;
    }

    @Override
    public int getTotal(int level) {
        return 15;
    }

    @Override
    public void onActive(Player player, int level) {
        super.onActive(player, level);
        player.setMetadata("sinking_moonlight", new FixedMetadataValue(ThePit.getInstance(), System.currentTimeMillis() + 12 * 60 * 1000L));
    }

    @Override
    public void onInactive(Player player, int level) {
        super.onInactive(player, level);
        player.removeMetadata("sinking_moonlight", ThePit.getInstance());
    }
}
