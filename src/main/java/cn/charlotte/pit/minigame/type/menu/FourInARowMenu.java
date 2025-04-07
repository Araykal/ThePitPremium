package cn.charlotte.pit.minigame.type.menu;

import cn.charlotte.pit.minigame.type.FourInARow;
import cn.charlotte.pit.minigame.type.menu.button.ChessButton;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import cn.charlotte.pit.util.menu.buttons.DisplayButton;
import cn.charlotte.pit.util.rank.RankUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/24 22:15
 */
public class FourInARowMenu extends Menu {

    private final FourInARow game;

    public FourInARowMenu(FourInARow game) {
        this.game = game;
    }

    @Override
    public String getTitle(Player player) {
        Player opponent;
        final boolean black = game.getBlackPlayer().equals(player);
        if (black) {
            opponent = game.getWhitePlayer();
        } else {
            opponent = game.getBlackPlayer();
        }


        return "&f四子棋 - " + opponent.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        boolean white = game.getWhitePlayer().equals(player);
        Player other = white ? game.getBlackPlayer() : game.getWhitePlayer();

        final Map<Integer, Button> buttonMap = new HashMap<>(28);
        buttonMap.put(2, new DisplayButton(new ItemBuilder(Material.SKULL_ITEM).durability(3).name(RankUtil.getPlayerRankColor(player.getUniqueId())).setSkullOwner(player.getName()).build(), true));
        buttonMap.put(6, new DisplayButton(new ItemBuilder(Material.SKULL_ITEM).durability(3).name(RankUtil.getPlayerRankColor(other.getUniqueId())).setSkullOwner(other.getName()).build(), true));

        final int seconds = (int) (game.getTimer().getRemaining() / 1000);
        buttonMap.put(4, new DisplayButton(new ItemBuilder(Material.COMPASS).name("&a剩余时间: " + seconds).amount(seconds).build(), true));

        boolean canPlay = white ? game.getCurrentState() == FourInARow.GameState.WHITE : game.getCurrentState() == FourInARow.GameState.BLACK;
        for (int i = 9; i < 54; i++) {
            final int y = (i - 9) / 9;
            final int x = (i - 9) - y * 9;
            FourInARow.PosInfo targetInfo = new FourInARow.PosInfo(x, y);
            FourInARow.PosInfo info = game.getBoard().get(targetInfo);
            if (info == null) {
                info = targetInfo;
            }

            buttonMap.put(i, new ChessButton(game, info, canPlay));
        }

        if (game.getCurrentState() == FourInARow.GameState.END) {
            for (FourInARow.PosInfo info : game.getWinsInfo()) {
                final int slot = this.getSlot(info.getX(), info.getY() + 1);
                buttonMap.put(slot, new DisplayButton(new ItemBuilder(Material.SKULL_ITEM).name(" ").durability(3).setSkullProperty("eyJ0aW1lc3RhbXAiOjE1MzE5NjU0NjA5MzAsInByb2ZpbGVJZCI6ImUzYjQ0NWM4NDdmNTQ4ZmI4YzhmYTNmMWY3ZWZiYThlIiwicHJvZmlsZU5hbWUiOiJNaW5pRGlnZ2VyVGVzdCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTQ5NzMxYTg1M2ZhZjIwOWE4ZWI0MTM0YzkwYzllYzA5ZTBiN2I4ZTJhYjMwNWIxODg3MzZjMzJlOTJhYTYifX19").build(), true));
            }
        }


        return buttonMap;
    }

    @Override
    public void onClose(Player player) {
        game.end();
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }
}
