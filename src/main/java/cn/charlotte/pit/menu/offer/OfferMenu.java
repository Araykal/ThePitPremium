package cn.charlotte.pit.menu.offer;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.menu.offer.button.OfferButton;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @Creator Misoryan
 * @Date 2021/5/29 11:36
 */
@RequiredArgsConstructor
public class OfferMenu extends Menu {
    private final Player target;
    private final String PATTEN_DEFAULT_YMD = "yyyy-MM-dd";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(PATTEN_DEFAULT_YMD);

    @Override
    public String getTitle(Player player) {
        return "来自 " + target.getName() + " 的交易报价";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerProfile targetProfile = PlayerProfile.getOrLoadPlayerProfileByUuid(target.getUniqueId());
        if (targetProfile.getOfferData().getBuyer() == null || !targetProfile.getOfferData().getBuyer().equals(player.getUniqueId())) {
            player.closeInventory();
        } else {
            if (targetProfile.getOfferData().hasUnclaimedOffer()) {
                player.closeInventory();
                player.sendMessage(CC.translate("&c此交易报价已经过期!"));
            }
            buttons.put(11, new DisplayButton(targetProfile.getOfferData().getItemStack(), true));
            buttons.put(15, new OfferButton(target));
        }
        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}
