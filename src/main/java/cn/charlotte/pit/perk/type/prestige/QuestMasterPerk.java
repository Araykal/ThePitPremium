package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.QuestData;
import cn.charlotte.pit.event.PitQuestInactiveEvent;
import cn.charlotte.pit.item.type.ChunkOfVileItem;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@AutoRegister
public class QuestMasterPerk extends AbstractPerk implements Listener {
    @Override
    public String getInternalPerkName() {
        return "quest_master_perk";
    }

    @Override
    public String getDisplayName() {
        return "任务大师";
    }

    @Override
    public Material getIcon() {
        return Material.BOOK;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 5; //5
    }

    @Override
    public int requirePrestige() {
        return 8;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7允许你在完成夜幕任务时额外获得 &f1 &7个 &5暗聚块");
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }


    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @EventHandler
    public void onQuestInactive(PitQuestInactiveEvent event) {
        Player player = event.getPlayer();
        if (event.getQuestData().getCurrent() >= event.getQuestData().getTotal() && isNightQuest(PlayerProfile.getPlayerProfileByUuid(event.getPlayer().getUniqueId()), event.getQuestData())) {
            player.getInventory().addItem(ChunkOfVileItem.toItemStack());
        }
    }

    private static boolean isNightQuest(PlayerProfile profile, QuestData quest) {
        return profile.isNightQuestEnable() && TimeUtil.getMinecraftTick(quest.getStartTime()) > 12000;
    }
}