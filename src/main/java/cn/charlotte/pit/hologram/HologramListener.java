package cn.charlotte.pit.hologram;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.event.PitProfileLoadedEvent;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.hologram.Hologram;
import cn.charlotte.pit.util.hologram.HologramAPI;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/4 13:09
 */
@AutoRegister
public class HologramListener implements Listener {
    protected static final Map<UUID, PlayerHologram> hologramMap = new HashMap<>();

    @EventHandler
    @SneakyThrows
    public void onLoad(PitProfileLoadedEvent event) {
        Bukkit.getScheduler()
                .runTaskAsynchronously(ThePit.getInstance(), () -> {
                    Player player = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
                    if (player != null && player.isOnline()) {
                        PlayerHologram playerHologram = new PlayerHologram(new ArrayList<>());
                        hologramMap.put(player.getUniqueId(), playerHologram);
                        for (AbstractHologram hologram : (ThePit.getInstance().getHologramFactory()).loopHologram) {
                            handleHologramCreate(player, playerHologram, hologram);
                        }
                        for (AbstractHologram hologram : (ThePit.getInstance().getHologramFactory()).normalHologram) {
                            handleHologramCreate(player, playerHologram, hologram);
                        }
                    }
                });
    }

    private void handleHologramCreate(Player player, PlayerHologram playerHologram, AbstractHologram hologram) {
        List<Hologram> holograms = new ArrayList<>();
        for (int i = 0; i < hologram.getText(player).size(); i++) {
            String text = hologram.getText(player).get(i);
            Hologram holo = HologramAPI.createHologram(hologram.getLocation().clone().add(0.0D, -i * hologram.getHologramHighInterval(), 0.0D), CC.translate(text));
            holo.spawn(Collections.singletonList(player));
            holograms.add(holo);
        }
        playerHologram.hologramData.put(hologram.getInternalName(), new HologramData(holograms, hologram.getInternalName()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler()
                .runTaskAsynchronously(ThePit.getInstance(), () -> {
                    PlayerHologram playerHologram = hologramMap.get(event.getPlayer().getUniqueId());
                    if (playerHologram == null) {
                        return;
                    }
                    for (HologramData datum : playerHologram.hologramData.values()) {
                        for (Hologram hologram : datum.holograms) {
                            if (hologram.isSpawned()) {
                                hologram.deSpawn();
                            }
                        }
                    }
                    hologramMap.remove(event.getPlayer().getUniqueId());
                });
    }

    @Getter
    public static class PlayerHologram {
        private final Map<String, HologramListener.HologramData> hologramData = new HashMap<>();

        public PlayerHologram(List<HologramListener.HologramData> hologramData) {
            for (HologramListener.HologramData data : hologramData) {
                this.hologramData.put(data.internalName, data);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof PlayerHologram)) {
                return false;
            }
            PlayerHologram other = (PlayerHologram)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$hologramData = getHologramData();
            Object other$hologramData = other.getHologramData();
            return Objects.equals(this$hologramData, other$hologramData);
        }

        protected boolean canEqual(Object other) {
            return other instanceof PlayerHologram;
        }

        @Override
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Object $hologramData = getHologramData();
            result = result * 59 + (($hologramData == null) ? 43 : $hologramData.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "HologramListener.PlayerHologram(hologramData=" + getHologramData() + ")";
        }
    }

    @Setter
    @Getter
    public static class HologramData {
        private List<Hologram> holograms;

        private String internalName;

        public HologramData(List<Hologram> holograms, String internalName) {
            this.holograms = holograms;
            this.internalName = internalName;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof HologramData)) {
                return false;
            }
            HologramData other = (HologramData)o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$holograms = getHolograms();
            Object other$holograms = other.getHolograms();
            if (!Objects.equals(this$holograms, other$holograms)) {
                return false;
            }
            Object this$internalName = getInternalName();
            Object other$internalName = other.getInternalName();
            return Objects.equals(this$internalName, other$internalName);
        }

        protected boolean canEqual(Object other) {
            return other instanceof HologramData;
        }

        @Override
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            Object $holograms = getHolograms();
            result = result * 59 + (($holograms == null) ? 43 : $holograms.hashCode());
            Object $internalName = getInternalName();
            result = result * 59 + (($internalName == null) ? 43 : $internalName.hashCode());
            return result;
        }

        @Override
        public String toString() {
            return "HologramListener.HologramData(holograms=" + getHolograms() + ", internalName=" + getInternalName() + ")";
        }
    }
}

