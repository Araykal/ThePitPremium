package cn.charlotte.pit.minigame.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.minigame.AbstractMiniGame;
import cn.charlotte.pit.minigame.type.menu.FourInARowMenu;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/24 22:02
 */
public class FourInARow extends AbstractMiniGame {
    private final Map<PosInfo, PosInfo> board = new HashMap<>();
    private Player whitePlayer;
    private Player blackPlayer;
    private Menu whitePlayerMenu;
    private Menu blackPlayerMenu;
    private GameState currentState;
    private Cooldown timer;
    private List<PosInfo> winsInfo;
    private Player winner;

    public void init(Player p1, Player p2) {
        this.whitePlayer = p1;
        this.blackPlayer = p2;

        this.currentState = GameState.WHITE;
        this.timer = new Cooldown(15, TimeUnit.SECONDS);

        whitePlayerMenu = new FourInARowMenu(this);
        blackPlayerMenu = new FourInARowMenu(this);

        whitePlayerMenu.openMenu(whitePlayer);
        blackPlayerMenu.openMenu(blackPlayer);
    }

    public void tryPlay(Player player, PosInfo posInfo) {
        boolean white = player.equals(whitePlayer);
        PosInfo info = board.get(posInfo);
        if (info == null || info.markedInfo == Marked.NULL) {
            posInfo.markedInfo = white ? Marked.WHITE : Marked.BLACK;
            board.put(posInfo, posInfo);

            currentState = currentState == GameState.BLACK ? GameState.WHITE : GameState.BLACK;
            this.timer = new Cooldown(15, TimeUnit.SECONDS);

            winsInfo = handleDown(posInfo, white ? Marked.WHITE : Marked.BLACK);
            if (winsInfo != null) {
                winner = player;
                winner.playSound(winner.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
                end();
            }

            refreshMenu();
        }
    }

    private List<PosInfo> handleDown(PosInfo posInfo, Marked marked) {
        final int x = posInfo.x;
        final int y = posInfo.y;

        List<PosInfo> topNear = new ArrayList<>();
        List<PosInfo> downNear = new ArrayList<>();
        List<PosInfo> rightNear = new ArrayList<>();
        List<PosInfo> leftNear = new ArrayList<>();
        List<PosInfo> leftTopNear = new ArrayList<>();
        List<PosInfo> leftDownNear = new ArrayList<>();
        List<PosInfo> rightDownNear = new ArrayList<>();
        List<PosInfo> rightTopNear = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            PosInfo info = new PosInfo(x + i, y);
            PosInfo mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                topNear.add(mapInfo);
                if (topNear.size() >= 4) {
                    return topNear;
                }
            }

            info = new PosInfo(x - i, y);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                downNear.add(mapInfo);
                if (downNear.size() >= 4) {
                    return downNear;
                }
            }

            info = new PosInfo(x, y + i);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                rightNear.add(mapInfo);
                if (rightNear.size() >= 4) {
                    return rightNear;
                }
            }

            info = new PosInfo(x, y - i);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                leftNear.add(mapInfo);
                if (leftNear.size() >= 4) {
                    return leftNear;
                }
            }

            //左上
            info = new PosInfo(x - i, y - i);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                leftTopNear.add(mapInfo);
                if (leftTopNear.size() >= 4) {
                    return leftTopNear;
                }
            }

            //左下
            info = new PosInfo(x - i, y + i);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                leftDownNear.add(mapInfo);
                if (leftDownNear.size() >= 4) {
                    return leftDownNear;
                }
            }

            //右下
            info = new PosInfo(x + i, y + i);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                rightDownNear.add(mapInfo);
                if (rightDownNear.size() >= 4) {
                    return rightDownNear;
                }
            }

            //右上
            info = new PosInfo(x + i, y - i);
            mapInfo = board.get(info);
            if (checkIllegal(info.x, info.y) && mapInfo != null && mapInfo.markedInfo == marked) {
                rightTopNear.add(mapInfo);
                if (rightTopNear.size() >= 4) {
                    return rightTopNear;
                }
            }
        }

        return null;
    }

    private boolean checkIllegal(int x, int y) {
        return x >= 0 && x <= 8 && y >= 0 && y <= 5;
    }

    public void end() {
        this.currentState = GameState.END;
        ThePit.getInstance()
                .getMiniGameController()
                .getMiniGameInstances()
                .remove(this);
    }

    private void refreshMenu() {
        this.blackPlayerMenu.setClosedByMenu(true);
        this.blackPlayerMenu.openMenu(blackPlayer);
        this.whitePlayerMenu.setClosedByMenu(true);
        this.whitePlayerMenu.openMenu(whitePlayer);
    }


    @Override
    public int getLoopTick() {
        return 20;
    }

    @Override
    public void onTick() {
        if (this.currentState != GameState.END && this.timer.hasExpired()) {
            this.timer = new Cooldown(15, TimeUnit.SECONDS);
            switch (this.currentState) {
                case BLACK:
                    this.blackPlayer.sendMessage(CC.translate("&c您已超时"));
                    this.currentState = GameState.WHITE;
                    break;
                case WHITE:
                    this.whitePlayer.sendMessage(CC.translate("&c您已超时"));
                    this.currentState = GameState.BLACK;
                    break;
                default:
            }
        }
        this.refreshMenu();
    }

    public Map<PosInfo, PosInfo> getBoard() {
        return this.board;
    }

    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    public Player getBlackPlayer() {
        return this.blackPlayer;
    }

    public Menu getWhitePlayerMenu() {
        return this.whitePlayerMenu;
    }

    public Menu getBlackPlayerMenu() {
        return this.blackPlayerMenu;
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public Cooldown getTimer() {
        return this.timer;
    }

    public List<PosInfo> getWinsInfo() {
        return this.winsInfo;
    }

    public Player getWinner() {
        return this.winner;
    }

    public enum Marked {
        NULL,
        WHITE,
        BLACK
    }

    public enum GameState {
        WHITE,
        BLACK,
        END
    }

    public static class PosInfo {
        private int x;
        private int y;
        private Marked markedInfo;

        public PosInfo(int x, int y) {
            this.x = x;
            this.y = y;
            this.markedInfo = Marked.NULL;
        }

        public PosInfo(int x, int y, Marked markedInfo) {
            this.x = x;
            this.y = y;
            this.markedInfo = markedInfo;
        }

        public PosInfo() {
            this.markedInfo = Marked.NULL;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PosInfo posInfo = (PosInfo) o;
            return x == posInfo.x && y == posInfo.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public Marked getMarkedInfo() {
            return this.markedInfo;
        }
    }
}
