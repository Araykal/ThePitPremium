package cn.charlotte.pit.menu.perk.normal.buy;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.menu.perk.UnKnowButton;
import cn.charlotte.pit.menu.perk.normal.buy.button.BackToPerkChooseButton;
import cn.charlotte.pit.menu.perk.normal.buy.button.PerkBuyButton;
import cn.charlotte.pit.menu.perk.normal.buy.button.ResetButton;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/1 22:43
 */
public class PerkBuyMenu extends Menu {
    private final int page;
    private final int[] perkSlot = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    private final PerkType perkType;

    public PerkBuyMenu(int page, PerkType perkType) {
        this.page = page;
        this.perkType = perkType;
    }

    @Override
    public String getTitle(Player player) {
        return "天赋";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> button = new HashMap<>();

        List<AbstractPerk> perks = ThePit.getInstance()
                .getPerkFactory()
                .getPerks()
                .stream()
                .sorted(Comparator.comparing(perk -> perk.requirePrestige() * 1000 + perk.requireLevel()))
                .collect(Collectors.toList());

        if (perkType == PerkType.PERK) {
            //如果是精通被动天赋 则移除
            perks.removeIf(perk -> perk.requirePrestige() > 0 && perk.requireLevel() == 0);
            //如果是boost天赋，则移除
            perks.removeIf(perk -> perk.requirePrestige() == 0 && perk.requireLevel() == 0);
        }
        //仅选择类型为perkType的天赋
        perks.removeIf(perk -> perk.getPerkType() == null || perk.getPerkType() != perkType);

        for (int i = 0; i < perks.size(); i++) {
            button.put(perkSlot[i], new PerkBuyButton(perks.get(i), page, perkType));
        }

        for (int i : perkSlot) {
            if (!button.containsKey(i)) {
                button.put(i, new UnKnowButton());
            }
        }

        button.put(49, new BackToPerkChooseButton());
        if (perkType == PerkType.PERK) {
            button.put(50, new ResetButton(page));
        }

        return button;
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }
}
