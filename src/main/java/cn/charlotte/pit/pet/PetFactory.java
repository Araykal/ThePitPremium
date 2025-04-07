package cn.charlotte.pit.pet;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.event.PitPlayerSpawnEvent;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.command.util.ClassUtil;
import cn.charlotte.pit.util.hologram.Hologram;
import cn.charlotte.pit.util.hologram.HologramAPI;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/22 10:07
 */
public class PetFactory implements Listener {
    private final Map<String, Class<? extends IPet>> petClazz = new HashMap<>();
    private final Map<UUID, PetData> petMap = new HashMap<>();
    private final Map<UUID, PetData> entityToPetData = new HashMap<>();

    public void init() {
        for (Class<?> clazz : ClassUtil.getClassesInPackage(ThePit.getInstance(), "cn.charlotte.pit.pet.impl")) {
            if (IPet.class.isAssignableFrom(clazz)) {
                try {
                    final Class<? extends IPet> petClazz = clazz.asSubclass(IPet.class);
                    final IPet instance = (IPet) clazz.newInstance();
                    this.petClazz.put(instance.getInternalName(), petClazz);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final LivingEntity entity = e.getPlayer();
        final PetData data = entityToPetData.get(entity.getUniqueId());
        if (data == null) {
            return;
        }

        data.removeThis();
    }

    @SneakyThrows
    public void spawnPet(String internal, Player owner) {
        try {
            final Class<? extends IPet> clazz = this.petClazz.get(internal);
            if (clazz == null) {
                return;
            }

            final IPet pet = clazz.newInstance();
            pet.setOwner(owner);

            final Location location = owner.getLocation();
            final World world = owner.getWorld();

            final Entity entity = world.spawnEntity(location, pet.getEntityType());
            pet.insertEntity(entity);

            final Hologram hologram = HologramAPI.createHologram(location, CC.translate(pet.getCustomName().get(0)));
            hologram.spawn();
            hologram.setAttachedTo(entity);
            if (pet.getCustomName().size() > 1) {
                for (int i = 1; i < pet.getCustomName().size(); i++) {
                    hologram.addLineAbove(CC.translate(pet.getCustomName().get(i)));
                }
            }

            final PetData data = new PetData();
            data.entity = entity;
            data.pet = pet;
            data.entityUuid = entity.getUniqueId();
            data.ownerUuid = owner.getUniqueId();
            data.hologram = hologram;

            this.petMap.put(owner.getUniqueId(), data);
            this.entityToPetData.put(entity.getUniqueId(), data);
        } catch (Exception e) {
            CC.printErrorWithCode(owner, e);
        }
    }

    @EventHandler
    public void onMissTarget(EntityUnleashEvent event) {
        final Entity entity = event.getEntity();
        final PetData data = entityToPetData.get(entity.getUniqueId());
        if (data == null) {
            return;
        }

        final IPet pet = data.pet;
        if (pet == null) {
            data.removeThis();
            return;
        }

        final Player owner = pet.getOwner();
        if (owner == null || !owner.isOnline()) {
            data.removeThis();
            return;
        }

        if (entity.getLocation().distanceSquared(owner.getLocation()) >= 32 * 32) {
            entity.teleport(owner);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();
        final PetData data = entityToPetData.get(entity.getUniqueId());
        if (data == null) {
            return;
        }

        data.removeThis();
    }

    @EventHandler
    public void onSlimeSplit(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && !(event.getDamager() instanceof Player)) {
            final PetData data = entityToPetData.get(event.getDamager().getUniqueId());
            if (data == null) return;
            event.setCancelled(data.ownerUuid.equals(event.getDamager().getUniqueId()));
        } else if (event.getDamager() instanceof Player && !(event.getEntity() instanceof Player)) {
            final PetData data = entityToPetData.get(event.getEntity().getUniqueId());
            if (data == null) return;
            event.setCancelled(data.ownerUuid.equals(event.getDamager().getUniqueId()));
        }

        final PetData data = entityToPetData.get(event.getDamager().getUniqueId());
        if (data != null) {
            final Player killer = Bukkit.getPlayer(data.ownerUuid);
            if (event.getEntity() instanceof LivingEntity) {
                final LivingEntity entity = (LivingEntity) event.getEntity();
                if (entity.getHealth() < event.getFinalDamage()) {
                    final EntityLiving player = ((CraftLivingEntity) entity).getHandle();
                    player.killer = ((CraftPlayer) killer).getHandle();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PitKillEvent event) {
        final PetData data = petMap.get(event.getTarget().getUniqueId());
        if (data != null) {
            data.removeThis();
        }
    }

    @EventHandler
    public void onSpawn(PitPlayerSpawnEvent event) {
        final PetData data = petMap.get(event.getPlayer().getUniqueId());
        if (data != null) {
            data.removeThis();
        }
    }

    @EventHandler
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        final Entity entity = event.getEntity();
        final PetData data = entityToPetData.get(entity.getUniqueId());
        if (data == null) {
            return;
        }

        final IPet pet = data.pet;
        if (pet == null) {
            data.removeThis();
            return;
        }

        final Player owner = pet.getOwner();
        if (owner == null || !owner.isOnline()) {
            data.removeThis();
            return;
        }

        final Optional<Player> first = PlayerUtil.getNearbyPlayers(entity.getLocation(), 10)
                .stream()
                .filter(player -> !player.getUniqueId().equals(pet.getOwner().getUniqueId()))
                .findFirst();

        if (first.isPresent()) {
            event.setTarget(first.get());
        } else {
            event.setTarget(null);
        }
    }

    public Map<String, Class<? extends IPet>> getPetClazz() {
        return this.petClazz;
    }

    public Map<UUID, PetData> getPetMap() {
        return this.petMap;
    }

    public Map<UUID, PetData> getEntityToPetData() {
        return this.entityToPetData;
    }


    public static class PetData {
        private UUID ownerUuid;
        private UUID entityUuid;
        private Hologram hologram;
        private Entity entity;
        private IPet pet;

        private void removeThis() {
            final PetFactory factory = ThePit.getInstance().getPetFactory();
            if (entity != null && !entity.isDead()) {
                entity.remove();
            }
            if (hologram.isSpawned()) {
                hologram.deSpawn();
            }

            factory.petMap.remove(this.ownerUuid);
            factory.entityToPetData.remove(entityUuid);
        }

        public UUID getOwnerUuid() {
            return this.ownerUuid;
        }

        public UUID getEntityUuid() {
            return this.entityUuid;
        }

        public Hologram getHologram() {
            return this.hologram;
        }

        public Entity getEntity() {
            return this.entity;
        }

        public IPet getPet() {
            return this.pet;
        }
    }

}
