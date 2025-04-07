package cn.charlotte.pit.events.impl;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.INormalEvent;
import cn.charlotte.pit.medal.impl.challenge.QuickMathsMedal;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.TitleUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/7 11:08
 */
public class QuickMathEvent implements IEvent, INormalEvent, Listener {
    public int RandomNumber;
    public int RandomNumber2;
    public int TheEquation;
    public String equationType;
    private final Set<UUID> alreadyAnswered = new HashSet<>();

    private long startTime = 0L;
    private int top = 0;

    private boolean ended = false;

    @Override
    public String getEventInternalName() {
        return "quick_math_event";
    }

    @Override
    public String getEventName() {
        return "&9&l速算";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    @Override
    public void onActive() {
        randomEquation();
        if (!"x".equals(equationType)) {
            randomNumber(10, 100);
        } else {
            randomNumber(10, 30);
        }
        setTop(0);
        setStartTime(System.currentTimeMillis());
        alreadyAnswered.clear();
        CC.boardCast("&5&l速算! &7前五名在聊天栏发出答案的玩家可以获得 &6+200硬币 &b+100经验值 &7!");
        CC.boardCast("&5&l速算! &7在聊天栏里写下你的答案: &e" + RandomNumber + equationType + RandomNumber2);
        for (Player player : Bukkit.getOnlinePlayers()) {
            TitleUtil.sendTitle(player, "&5&l速算!", ("&e" + RandomNumber + equationType + RandomNumber2), 20, 20 * 5, 10);
        }
        Bukkit.getPluginManager()
                .registerEvents(this, ThePit.getInstance());
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
            if (!ended) {
                ThePit.getInstance()
                        .getEventFactory()
                        .inactiveEvent(this);
            }
        }, 5 * 60 * 20L);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().contains(TheEquation + "")) {
            if (!alreadyAnswered.add(e.getPlayer().getUniqueId())) {
                e.getRecipients().clear();
                e.getRecipients().add(e.getPlayer());
            } else {
                e.setCancelled(true);
                top++;
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(e.getPlayer().getUniqueId());
                if (System.currentTimeMillis() - startTime <= 2.5 * 1000) {
                    new QuickMathsMedal().addProgress(profile, 1);
                }
                CC.boardCast("&5&l速算! &e#" + top + " " + profile.getFormattedName() + " &7在 &e" + TimeUtil.millisToRoundedTime(System.currentTimeMillis() - startTime) + " &7内回答正确!");
                profile.setCoins(profile.getCoins() + 200);
                profile.grindCoins(200);
                profile.setExperience(profile.getExperience() + 100);
                if (top >= 5) {
                    ThePit.getInstance()
                            .getEventFactory()
                            .inactiveEvent(this);
                }
            }
        }
    }

    @Override
    public void onInactive() {
        ended = true;
        HandlerList.unregisterAll(this);
        CC.boardCast("&5&l速算! &7活动结束! 正确答案: &e" + TheEquation);
    }

    public void randomNumber(int min, int max) {
        if (min > max) {
            RandomNumber = (int) (Math.random() * (min - max + 1) + max);
            RandomNumber2 = (int) (Math.random() * (min - max + 1) + max);
        } else if (min < max) {
            RandomNumber = (int) (Math.random() * (max - min + 1) + min);
            RandomNumber2 = (int) (Math.random() * (max - min + 1) + min);
        } else {
            RandomNumber = (int) (Math.random() * (100 - 1 + 1) + 1);
            RandomNumber2 = (int) (Math.random() * (100 - 1 + 1) + 1);
        }
        switch (equationType) {
            case "+":
                TheEquation = RandomNumber + RandomNumber2;
                break;
            case "-":
                TheEquation = RandomNumber - RandomNumber2;
                break;
            case "x":
                TheEquation = RandomNumber * RandomNumber2;
                break;
        }
    }

    public void randomEquation() {
        equationType = (String) RandomUtil.helpMeToChooseOne("+", "-", "x");
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
