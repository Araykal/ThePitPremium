package cn.charlotte.pit.menu.perk.normal.choose;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.menu.perk.normal.buy.button.BoostButton;
import cn.charlotte.pit.menu.perk.normal.choose.button.KillStreakMainButton;
import cn.charlotte.pit.menu.perk.normal.choose.button.PerkButton;
import cn.charlotte.pit.perk.PerkFactory;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 22:46
 */
public class PerkChooseMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "天赋商店";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        Map<Integer, Button> button = new HashMap<>();
        PerkData data = profile.getUnlockedPerkMap().get("ExtractPerkSlot");
        if (data != null) {
            for (int i = 0; i < 4; i++) {
                button.put(11 + i, new PerkButton(i + 1));
            }
        }
        for (int i = 0; i < 3; i++) {
            button.putIfAbsent(12 + i, new PerkButton(i + 1));
        }
        button.put(15, new KillStreakMainButton());

        final PerkFactory perkFactory = ThePit.getInstance().getPerkFactory();
        button.put(28, new BoostButton(perkFactory.getPerkMap().get("XPBoost"), -1));
        button.put(29, new BoostButton(perkFactory.getPerkMap().get("CoinBoost"), -2));
        button.put(30, new BoostButton(perkFactory.getPerkMap().get("MeleeBoost"), -3));
        button.put(31, new BoostButton(perkFactory.getPerkMap().get("ArrowBoost"), -4));
        button.put(32, new BoostButton(perkFactory.getPerkMap().get("DmgReduceBoost"), -5));
        button.put(33, new BoostButton(perkFactory.getPerkMap().get("BuilderBattleBoost"), -6));
        button.put(34, new BoostButton(perkFactory.getPerkMap().get("ElGatoBoost"), -7));

        return button;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
