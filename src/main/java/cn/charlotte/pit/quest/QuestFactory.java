package cn.charlotte.pit.quest;

import cn.charlotte.pit.parm.listener.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/19 16:43
 */
@Getter
@Slf4j
public class QuestFactory {
    private final List<AbstractQuest> quests;
    private final Map<String, AbstractQuest> questMap;
    private final List<IPlayerDamaged> playerDamageds;
    private final List<IAttackEntity> attackEntities;
    private final List<IItemDamage> iItemDamages;
    private final List<IPlayerBeKilledByEntity> playerBeKilledByEntities;
    private final List<IPlayerKilledEntity> playerKilledEntities;
    private final List<IPlayerRespawn> playerRespawns;
    private final List<IPlayerShootEntity> playerShootEntities;
    private final List<ITickTask> tickTasks;
    private final List<IPlayerAssist> playerAssists;

    public QuestFactory() {
        this.quests = new ArrayList<>();
        this.questMap = new HashMap<>();
        this.playerDamageds = new ArrayList<>();
        this.iItemDamages = new ArrayList<>();
        this.attackEntities = new ArrayList<>();
        this.playerBeKilledByEntities = new ArrayList<>();
        this.playerKilledEntities = new ArrayList<>();
        this.playerRespawns = new ArrayList<>();
        this.tickTasks = new ArrayList<>();
        this.playerShootEntities = new ArrayList<>();
        this.playerAssists = new ArrayList<>();
    }

    public void init(Collection<Class<?>> classes) {
        log.info("Loading quests...");
        for (Class<?> clazz : classes) {
            if (AbstractQuest.class.isAssignableFrom(clazz)) {
                try {
                    AbstractQuest quest = (AbstractQuest) clazz.newInstance();
                    this.quests.add(quest);
                    this.questMap.put(quest.getQuestInternalName(), quest);

                    if (IPlayerDamaged.class.isAssignableFrom(clazz)) {
                        playerDamageds.add((IPlayerDamaged) quest);
                    }
                    if (IAttackEntity.class.isAssignableFrom(clazz)) {
                        attackEntities.add((IAttackEntity) quest);
                    }
                    if (IItemDamage.class.isAssignableFrom(clazz)) {
                        iItemDamages.add((IItemDamage) quest);
                    }
                    if (IPlayerBeKilledByEntity.class.isAssignableFrom(clazz)) {
                        playerBeKilledByEntities.add((IPlayerBeKilledByEntity) quest);
                    }
                    if (IPlayerKilledEntity.class.isAssignableFrom(clazz)) {
                        playerKilledEntities.add((IPlayerKilledEntity) quest);
                    }
                    if (IPlayerRespawn.class.isAssignableFrom(clazz)) {
                        playerRespawns.add((IPlayerRespawn) quest);
                    }
                    if (IPlayerShootEntity.class.isAssignableFrom(clazz)) {
                        playerShootEntities.add((IPlayerShootEntity) quest);
                    }
                    if (ITickTask.class.isAssignableFrom(clazz)) {
                        tickTasks.add((ITickTask) quest);
                    }
                    if (IPlayerAssist.class.isAssignableFrom(clazz)) {
                        playerAssists.add((IPlayerAssist) quest);
                    }
                } catch (Exception e) {
                    log.error(e + " exception on install quests.");
                }
            }
        }
        log.info("" + quests.size() + "quests loaded!");
    }
}
