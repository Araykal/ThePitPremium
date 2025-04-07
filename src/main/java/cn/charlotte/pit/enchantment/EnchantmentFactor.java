package cn.charlotte.pit.enchantment;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.parm.listener.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 21:49
 */
@Getter
@Slf4j
public class EnchantmentFactor {


    private final List<AbstractEnchantment> enchantments;
    private final Map<String, AbstractEnchantment> enchantmentMap;
    private final List<IPlayerDamaged> playerDamageds;
    private final List<IAttackEntity> attackEntities;
    private final List<IItemDamage> iItemDamages;
    private final List<IPlayerBeKilledByEntity> playerBeKilledByEntities;
    private final List<IPlayerKilledEntity> playerKilledEntities;
    private final List<IPlayerRespawn> playerRespawns;
    private final List<IPlayerShootEntity> playerShootEntities;
    private final Map<String, ITickTask> tickTasks;
    private final List<IPlayerAssist> playerAssists;
    private final Map<String, IActionDisplayEnchant> actionDisplayEnchants;

    public EnchantmentFactor() {
        this.enchantments = new ArrayList<>();
        this.enchantmentMap = new HashMap<>();
        this.playerDamageds = new ArrayList<>();
        this.iItemDamages = new ArrayList<>();
        this.attackEntities = new ArrayList<>();
        this.playerBeKilledByEntities = new ArrayList<>();
        this.playerKilledEntities = new ArrayList<>();
        this.playerRespawns = new ArrayList<>();
        this.tickTasks = new HashMap<>();
        this.playerShootEntities = new ArrayList<>();
        this.playerAssists = new ArrayList<>();
        this.actionDisplayEnchants = new HashMap<>();
    }

    public void init(Collection<Class<? extends AbstractEnchantment>> classes) {
        log.info("Loading enchantments...");
        for (Class<?> clazz : classes) {
            if (AbstractEnchantment.class.isAssignableFrom(clazz)) {
                try {
                    AbstractEnchantment enchantment = (AbstractEnchantment) clazz.newInstance();
                    this.enchantments.add(enchantment);
                    this.enchantmentMap.put(enchantment.getNbtName(), enchantment);

                    if (enchantment instanceof Listener && enchantment.getClass().isAnnotationPresent(AutoRegister.class)) {
                        Bukkit.getPluginManager().registerEvents((Listener) enchantment, ThePit.getInstance());
                    }

                    if (IPlayerDamaged.class.isAssignableFrom(clazz)) {
                        playerDamageds.add((IPlayerDamaged) enchantment);
                    }
                    if (IAttackEntity.class.isAssignableFrom(clazz)) {
                        attackEntities.add((IAttackEntity) enchantment);
                    }
                    if (IItemDamage.class.isAssignableFrom(clazz)) {
                        iItemDamages.add((IItemDamage) enchantment);
                    }
                    if (IPlayerBeKilledByEntity.class.isAssignableFrom(clazz)) {
                        playerBeKilledByEntities.add((IPlayerBeKilledByEntity) enchantment);
                    }
                    if (IPlayerKilledEntity.class.isAssignableFrom(clazz)) {
                        playerKilledEntities.add((IPlayerKilledEntity) enchantment);
                    }
                    if (IPlayerRespawn.class.isAssignableFrom(clazz)) {
                        playerRespawns.add((IPlayerRespawn) enchantment);
                    }
                    if (IPlayerShootEntity.class.isAssignableFrom(clazz)) {
                        playerShootEntities.add((IPlayerShootEntity) enchantment);
                    }
                    if (ITickTask.class.isAssignableFrom(clazz)) {
                        tickTasks.put(enchantment.getNbtName(), (ITickTask) enchantment);
                    }
                    if (IPlayerAssist.class.isAssignableFrom(clazz)) {
                        playerAssists.add((IPlayerAssist) enchantment);
                    }
                    if (IActionDisplayEnchant.class.isAssignableFrom(clazz)) {
                        actionDisplayEnchants.put(enchantment.getNbtName(), (IActionDisplayEnchant) enchantment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e + " exception on install enchantments.");
                }
            }
        }
        log.info("" + enchantmentMap.size() + " enchantments loaded!");
    }

    public List<AbstractEnchantment> getEnchantments() {
        return enchantments;
    }

    public Map<String, AbstractEnchantment> getEnchantmentMap() {
        return enchantmentMap;
    }

    public List<IPlayerDamaged> getPlayerDamageds() {
        return playerDamageds;
    }

    public List<IAttackEntity> getAttackEntities() {
        return attackEntities;
    }

    public List<IItemDamage> getiItemDamages() {
        return iItemDamages;
    }

    public List<IPlayerBeKilledByEntity> getPlayerBeKilledByEntities() {
        return playerBeKilledByEntities;
    }

    public List<IPlayerKilledEntity> getPlayerKilledEntities() {
        return playerKilledEntities;
    }

    public List<IPlayerRespawn> getPlayerRespawns() {
        return playerRespawns;
    }

    public List<IPlayerShootEntity> getPlayerShootEntities() {
        return playerShootEntities;
    }

    public Map<String, ITickTask> getTickTasks() {
        return tickTasks;
    }

    public List<IPlayerAssist> getPlayerAssists() {
        return playerAssists;
    }

    public Map<String, IActionDisplayEnchant> getActionDisplayEnchants() {
        return actionDisplayEnchants;
    }
}
