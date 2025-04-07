package cn.charlotte.pit.menu.genesis;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.menu.genesis.button.GenesisPerkButton;
import cn.charlotte.pit.menu.genesis.button.GenesisSpawnButton;
import cn.charlotte.pit.menu.genesis.button.GenesisStatusButton;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/4 10:02
 */

public class GenesisMenu extends Menu {

    private final GenesisTeam genesisTeam;
    private boolean bypassed = false;

    public GenesisMenu(GenesisTeam genesisTeam) {
        this.genesisTeam = genesisTeam;
    }

    @Override
    public String getTitle(Player player) {
        if (genesisTeam == GenesisTeam.ANGEL) {
            return "天使";
        }
        if (genesisTeam == GenesisTeam.DEMON) {
            return "恶魔";
        }
        return "未知NPC";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        Map<Integer, Button> button = new HashMap<>();
        if (ThePit.getInstance().getPitConfig().isGenesisEnable() || bypassed) {
            for (int i = 0; i < 7; i++) {
                button.put(10 + i, new GenesisPerkButton(genesisTeam, profile, i + 1));
                //button.put(19 + i, new GenesisPerkButton(genesisTeam, profile, i + 8));
            }
            button.put(29, new GenesisStatusButton(genesisTeam, profile));
        /*
        if (profile.getGenesisData().getTier() >= 1 && genesisTeam == profile.getGenesisData().getTeam()) {
            button.put(31 + 9, new GenesisBuffButton(genesisTeam, profile));
        }

         */
            button.put(33, new GenesisSpawnButton(genesisTeam, profile));
        } else {
            button.put(22, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.BARRIER)
                            .name("&c活动当前未开放!")
                            .build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                    if (PlayerUtil.isStaff(player)) {
                        bypassed = true;
                    }
                }

                @Override
                public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                    return true;
                }
            });
        }
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

    @Override
    public boolean isUpdateAfterClick() {
        return true;
    }
}
