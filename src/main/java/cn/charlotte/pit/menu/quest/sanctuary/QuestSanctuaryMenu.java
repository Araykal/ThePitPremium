package cn.charlotte.pit.menu.quest.sanctuary;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.menu.quest.sanctuary.button.MusicalRuneSanctuaryButton;
import cn.charlotte.pit.menu.quest.sanctuary.button.PermanentBoostButton;
import cn.charlotte.pit.perk.PerkFactory;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/31 19:25
 */
public class QuestSanctuaryMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "兑换所";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        final PerkFactory perkFactory = ThePit.getInstance().getPerkFactory();

        buttons.put(10, new PermanentBoostButton(perkFactory.getPerkMap().get("CoinContractBoost"), 200));
        buttons.put(11, new PermanentBoostButton(perkFactory.getPerkMap().get("XPContractBoost"), 150));
/*        buttons.put(12, new MusicalRuneSanctuaryButton());*/
        //buttons.put(12, new CactusQuestSanctuaryButton());
        //buttons.put(13, new FunkyFeatherSanctuaryButton());
        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
