package cn.charlotte.pit.menu.genesis.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.random.RandomUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/4 11:40
 */

public class GenesisSpawnButton extends Button {
    private final GenesisTeam displayTeam;
    private final PlayerProfile profile;

    public GenesisSpawnButton(GenesisTeam displayTeam, PlayerProfile profile) {
        this.displayTeam = displayTeam;
        this.profile = profile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lines = new ArrayList<>();
        String teamColor = "&f";
        Material material = Material.AIR;
        if (displayTeam == GenesisTeam.ANGEL) {
            teamColor = "&b";
            material = Material.BIRCH_DOOR_ITEM;
        }
        if (displayTeam == GenesisTeam.DEMON) {
            teamColor = "&c";
            material = Material.DARK_OAK_DOOR_ITEM;
        }
        lines.add("&7前往至阵营专属的出生点.");
        lines.add(" ");
        if (profile.getGenesisData().getTeam() == displayTeam && profile.getGenesisData().getTier() >= 2) {
            lines.add("&e点击传送至阵营出生点!");
        } else {
            lines.add("&c地区未开放!");
        }
        return new ItemBuilder(material).name(teamColor + "前往阵营出生点").lore(lines).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (profile.getGenesisData().getTeam() == displayTeam && profile.getGenesisData().getTier() >= 2) {
            final List<Location> locations = getLocations();
            if (locations.isEmpty()) {
                player.sendMessage(CC.translate("&c服务器管理员未配置阵营出生点, 请联系管理员"));
            } else {
                player.teleport(locations.get(RandomUtil.random.nextInt(locations.size())));
            }
        }
    }

    private List<Location> getLocations() {
        if (displayTeam == GenesisTeam.ANGEL) {
            return ThePit.getInstance()
                    .getPitConfig()
                    .getAngelSpawns();
        } else {
            return ThePit.getInstance()
                    .getPitConfig()
                    .getDemonSpawns();
        }
    }
}
