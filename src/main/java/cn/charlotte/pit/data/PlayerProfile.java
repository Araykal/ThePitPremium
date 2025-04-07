package cn.charlotte.pit.data;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.api.PitInternalHook;
import cn.charlotte.pit.buff.BuffData;
import cn.charlotte.pit.data.sub.*;
import cn.charlotte.pit.event.PitGainCoinsEvent;
import cn.charlotte.pit.event.PitGainRenownEvent;
import cn.charlotte.pit.event.PitStreakKillChangeEvent;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import cn.charlotte.pit.medal.impl.challenge.HundredLevelMedal;
import cn.charlotte.pit.quest.AbstractQuest;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import cn.charlotte.pit.util.chat.TitleUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.level.LevelUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import cn.charlotte.pit.util.rank.RankUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/29 23:04
 */

@JsonIgnoreProperties(ignoreUnknown = true, value = {
        "inArena",
        "streakKills",
        "strikeAssist",
        "combatTimer",
        "editingMode",
        "damageReduced",
        "damageMap",
        "strengthNum",
        "strengthTimer",
        "bountyCooldown",
        "bountyStreak",
        "lastKilledPlayer",
        "killRecap",
        "screenShare",
        "screenShareQQ",
        "mailData",
        "tempInvUsing",
        "nicked",
        "nickPrestige",
        "nickLevel",
        "invBackups",
        "noDamageAnimations",
        "liteStreakKill",
        "lastActionTimestamp",
        "buffData",
        "streakCooldown",
        "streakCount",
        "bot",
        "lastDamageAt"
})
public class PlayerProfile {

    public final static PlayerProfile NONE_PROFILE = new PlayerProfile(UUID.randomUUID(), "NotLoadPlayer");

    private final static Map<UUID, PlayerProfile> cacheProfile = new HashMap<>();
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PlayerProfile.class);
    public int prestige;
    public List<String> claimedMail;
    public PlayerMailData mailData;
    @JsonIgnore
    private boolean loaded;
    private String playerName;
    private String uuid;
    private String lowerName;
    private long registerTime;
    private long lastLoginTime;
    private long lastLogoutTime;
    private long totalPlayedTime;
    private long yearPlayedTime;
    private long monthPlayedTime;
    private long weekPlayedTime;
    private long todayPlayedTime;
    private int kills;
    private int assists;
    private int deaths;
    private int highestStreaks;
    private long totalDamage;
    private long meleeTotalDamage;
    private long arrowTotalDamage;
    private long hurtDamage;
    private long meleeHurtDamage;
    private long bowHurtDamage;
    private int meleeAttack;
    private int shootAttack;
    private int meleeHit;
    private int bowHit;
    private int rodUsed;
    private int rodHit;
    private int goldPicked;
    private int fishingNumber;
    private int goldenHeadEaten;
    private double experience;
    private double coins;
    private int renown;
    private int bounty;
    private int actionBounty;
    private double respawnTime;
    private PlayerInv inventory;
    private PlayerEnderChest enderChest;
    private int enderChestRow;

    //每次都遍历查询，效率低下
    //所以专用Map降低大O复杂度
    @Deprecated
    private List<PerkData> unlockedPerk;
    private Map<String, PerkData> unlockedPerkMap = new HashMap<>();
    @Deprecated
    private List<PerkData> boughtPerk;
    private Map<String, PerkData> boughtPerkMap = new HashMap<>();

    private Set<String> usedCdk;
    private Map<Integer, PerkData> chosePerk;
    private double totalExp;
    private List<String> autoBuyButtons;
    private TradeLimit tradeLimit;
    private MedalData medalData;
    private QuestLimit questLimit;
    private OfferData offerData;
    //累计获得硬币
    private double grindedCoins;
    private PlayerOption playerOption;
    private PlayerBanData playerBanData;
    private boolean supporter;
    private boolean supporterGivenByAdmin;
    //补偿信息
    private int remedyLevel;
    private double remedyExp;
    private String remedyDate;
    private int totalFishTimes;
    private int totalFishTreasureTimes;
    private int totalFishTrashTimes;
    //当前的任务
    private QuestData currentQuest;
    //上一次的任务
    private QuestData lastQuest;
    //是否开启了夜晚任务
    private boolean nightQuestEnable;
    private QuestCenter questCenter;
    private GenesisData genesisData;
    private List<String> currentQuestList;
    private double maxHealth;
    private int foodLevel;
    private float moveSpeed;
    private String enchantingItem;
    private String enchantingScience;
    //玩家是否在退出其他pit服务器，用于数据保存用，防止产生脏数据

    private String enchantingBook;
    private boolean login;
    private WipedData wipedData;
    //shouldn't save fields
    private BuffData buffData;
    private boolean inArena;
    private double streakKills;
    private Cooldown combatTimer;
    private boolean editingMode;
    private double damageReduced;
    private Map<UUID, DamageData> damageMap;
    private int strengthNum;
    private Cooldown strengthTimer;
    private UUID lastKilledPlayer;
    private Cooldown bountyCooldown;
    private int bountyStreak;
    private KillRecap killRecap;
    private boolean screenShare;
    private String screenShareQQ;
    private boolean nicked;
    private int nickPrestige;
    private int nickLevel;
    private boolean tempInvUsing;
    private boolean noDamageAnimations;
    private double liteStreakKill;
    private long lastActionTimestamp;

    private double goldStackAddon = 0.0;
    private double goldStackMax = 0.5;

    private double xpStackAddon = 0.0;
    private double xpStackMax = 1.0;

    private List<PlayerInvBackup> invBackups;

    private int todayCompletedUber;
    private long todayCompletedUberLastRefreshed;

    private int profileFormatVersion = 0;

    private Cooldown streakCooldown;
    private int streakCount;

    private boolean bot;

    private long lastDamageAt = -1L;

    private HashMap<String, Double> extraMaxHealth = new HashMap<>();

    public KingsQuestsData kingsQuestsData = new KingsQuestsData();

    public PlayerProfile(UUID uuid, String playerName) {
        //调用默认构造函数，初始化赋值
        this();

        this.uuid = uuid.toString();
        this.playerName = playerName;
        this.lowerName = playerName.toLowerCase();
        this.mailData = new PlayerMailData(uuid, playerName);
    }

    public PlayerProfile() {
        this.inventory = new PlayerInv();
        this.enderChest = new PlayerEnderChest();
        this.killRecap = new KillRecap();
        this.buffData = new BuffData();
        this.combatTimer = new Cooldown(0);
        this.damageMap = new HashMap<>();
        this.unlockedPerk = new ArrayList<>();
        this.boughtPerk = new ArrayList<>();
        this.strengthTimer = new Cooldown(0);
        this.usedCdk = new HashSet<>();
        this.enderChestRow = 3;
        this.respawnTime = 0.1;

        if (ThePit.isDEBUG_SERVER()) {
            this.coins = 10000000;
            this.renown = 100000;
            this.prestige = 20;
            this.experience = LevelUtil.getLevelTotalExperience(20, 120);
        } else {
            this.coins = 5000;
        }

        this.screenShare = false;
        this.screenShareQQ = "none";

        this.chosePerk = new HashMap<>();

        this.autoBuyButtons = new ArrayList<>();

        this.medalData = new MedalData();

        this.tradeLimit = new TradeLimit();
        this.questLimit = new QuestLimit();
        this.offerData = new OfferData();

        this.playerOption = new PlayerOption();
        this.playerBanData = new PlayerBanData();
        this.bountyCooldown = new Cooldown(0);
        this.currentQuestList = new ArrayList<>();
        this.genesisData = new GenesisData();
        this.invBackups = new ArrayList<>();
        this.claimedMail = new ArrayList<>();

        this.nightQuestEnable = false;

        this.supporter = false;
        this.supporterGivenByAdmin = false;

        this.totalFishTimes = 0;
        this.totalFishTreasureTimes = 0;
        this.totalFishTrashTimes = 0;

        this.foodLevel = 20;
        this.maxHealth = 20.0d;
        this.moveSpeed = 0.2F;

        //Level 0 : NoRemedy
        //Level 1 : Remedy + 1 Perk
        //Level 2 : Remedy + 2 Perks
        this.remedyLevel = 0;
        this.remedyExp = 0;
        this.remedyDate = "none";

        this.mailData = new PlayerMailData();

        this.loaded = false;
    }

    public static Map<UUID, PlayerProfile> getCacheProfile() {
        return PlayerProfile.cacheProfile;
    }

    @JsonIgnore
    public static PlayerProfile getPlayerProfileByUuid(UUID uuid) {

        PlayerProfile profile = cacheProfile.get(uuid);
        if (profile == null) {
//            Player player = Bukkit.getPlayer(uuid);
//            if (player != null && player.isOnline()) {
//                player.kickPlayer("你的档案因未知异常没有被加载,请尝试稍等一会儿重新进入游戏!");
//            }
//            throw new RuntimeException(uuid.toString() + "'s Player profile is not loaded");
            return NONE_PROFILE;
        }
        return profile;
    }

    /**
     * 该方法用于查找玩家，如果玩家可能离线时请使用本方法
     * 注意！请异步调用本方法，如果在主线程上调用会抛异常
     *
     * @param uuid 寻找的玩家UUID
     * @return 目标玩家玩家档案，如果该玩家未注册，则返回null
     */
    @JsonIgnore
    public static PlayerProfile getOrLoadPlayerProfileByUuid(UUID uuid) {
        PlayerProfile profile = cacheProfile.get(uuid);
        if (profile != null) {
            return profile;
        }
        return loadPlayerProfileByUuid(uuid);
    }

    public static PlayerProfile loadPlayerProfileByUuid(UUID uuid) {
        if (Bukkit.getServer().isPrimaryThread()) {
            throw new RuntimeException("Shouldn't load profile on primary thread!");
        }

        PlayerProfile playerProfile = ThePit.getInstance()
                .getMongoDB()
                .getProfileCollection()
                .find(Filters.eq("uuid", uuid.toString()))
                .first();

        if (playerProfile != null) {
            //load mail
            loadMail(playerProfile, uuid);

            //load inv backup
            try {
                final FindIterable<PlayerInvBackup> invBackups = ThePit.getInstance()
                        .getMongoDB()
                        .getInvCollection()
                        .find(Filters.eq("uuid", uuid.toString()));

                long lastTime = 0;
                for (PlayerInvBackup backup : invBackups) {
                    if (Math.abs(backup.getTimeStamp() - lastTime) < 10 * 60 * 1000) {
                        lastTime = backup.getTimeStamp();
                        ThePit.getInstance()
                                .getMongoDB()
                                .getInvCollection()
                                .deleteOne(Filters.eq("backupUuid", backup.getBackupUuid()));
                        continue;
                    }
                    lastTime = backup.getTimeStamp();
                    playerProfile.getInvBackups().add(backup);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return playerProfile;
    }

    /**
     * 该方法用于查找玩家，如果玩家可能离线时请使用本方法
     * 注意！请异步调用本方法，如果在主线程上调用会抛异常
     *
     * @param name 目标玩家名字
     * @return 目标玩家玩家档案，如果该玩家未注册，则返回null
     */
    @JsonIgnore
    public static PlayerProfile getOrLoadPlayerProfileByName(String name) {
        for (Map.Entry<UUID, PlayerProfile> entry : cacheProfile.entrySet()) {
            if (entry.getValue().getPlayerName().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        if (Bukkit.getServer().isPrimaryThread()) {
            throw new RuntimeException("Shouldn't load profile on primary thread!");
        }

        PlayerProfile playerProfile = ThePit.getInstance()
                .getMongoDB()
                .getProfileCollection()
                .find(Filters.eq("lowerName", name.toLowerCase()))
                .first();

        return playerProfile;
    }

    @JsonIgnore
    public static PlayerProfile getPlayerProfileByName(String name) {
        for (Map.Entry<UUID, PlayerProfile> entry : cacheProfile.entrySet()) {
            if (entry.getValue().getPlayerName().equalsIgnoreCase(name)) {
                return entry.getValue();
            }
        }
        Player player = Bukkit.getPlayer(name);
        if (player != null && player.isOnline()) {
            player.sendMessage(CC.translate("&c在加载您的天坑乱斗数据时出现了一个问题,您可以尝试再次进入游戏以重试."));
            player.kickPlayer("加载您的天坑乱斗数据时出现了一个问题,您可以尝试再次进入游戏以重试.");
        }
        throw new RuntimeException(name + "'s Player profile not load");
    }

    public static void saveAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                if (profile.isLoaded()) {
                    profile.setInventory(InventoryUtil.playerInventoryFromPlayer(player));
                    profile.save(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                CC.printError(player, e);
            }
        }
    }

    public static void loadMail(PlayerProfile playerProfile, UUID uuid) {
        PlayerMailData mailData = ThePit.getInstance()
                .getMongoDB()
                .getMailCollection()
                .find(Filters.eq("uuid", uuid.toString()))
                .first();

        if (mailData == null) {
            mailData = new PlayerMailData();
            mailData.setName(playerProfile.playerName);
            mailData.setNameLower(playerProfile.lowerName);
            mailData.setUuid(playerProfile.uuid);
        }

        mailData.cleanUp();

        playerProfile.setMailData(mailData);
    }

    public void init() {
        cacheProfile.put(this.getPlayerUuid(), this);
    }

    public PlayerProfile save(Player player) {
        this.totalExp = experience;
        for (int i = 0; i < prestige; i++) {
            this.totalExp = totalExp + LevelUtil.getLevelTotalExperience(i, 120);
        }

        if (!this.loaded) {
            return this;
        }


        saveData(player);
        return this;
    }

    public void saveData(Player player) {
        final long now = System.currentTimeMillis();
        if(player != null){
            this.setInventory(InventoryUtil.playerInventoryFromPlayer(player));
        }
        //if (invBackups.isEmpty() || invBackups.stream().noneMatch(backup -> now - backup.getTimeStamp() < 10 * 60 * 1000)) {
        final PlayerInvBackup backup = new PlayerInvBackup();
        backup.setUuid(this.uuid);
        backup.setTimeStamp(now);
        backup.setBackupUuid(UUID.randomUUID().toString());
        backup.setInv(this.inventory);
        backup.setChest(this.enderChest);
        backup.setTimeStamp(System.currentTimeMillis());

        backup.save();
//        gcBackups(gcBackupIterators(), this, true);
//           gcBackups(invBackups,this,false);
// }

        ThePit.getInstance()
                .getMongoDB()
                .getProfileCollection()
                .replaceOne(Filters.eq("uuid", this.uuid), this, new ReplaceOptions().upsert(true));
    }
    public PlayerProfile load() {

        PlayerProfile profile = PlayerProfile.loadPlayerProfileByUuid(this.getPlayerUuid());
        if (profile == null) {
            this.registerTime = System.currentTimeMillis();
            //refresh quests - start
            this.refreshQuest();
            //refresh quests - end


            this.loaded = true;
            return this;
        }


        //refresh quests - start
        profile.refreshQuest();
        profile.refreshGenesisData();
        //refresh quests - end

        profile.loaded = true;

        Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
            try {
                final Player player = Bukkit.getPlayer(profile.getPlayerUuid());
                if (player != null) {
                    if (!player.getName().equals(player.getDisplayName())) {
                        profile.nicked = true;
                        if (profile.prestige <= 0) {
                            profile.nickPrestige = 0;
                        } else {
                            profile.nickPrestige = RandomUtil.random.nextInt(profile.getPrestige()) + 1;
                        }

                        profile.nickLevel = RandomUtil.random.nextInt(profile.getLevel() + 1);
                    }
                }
            } catch (Exception e) {
                Bukkit.getOnlinePlayers()
                        .forEach(player -> CC.printError(player, e));
            }
        });

        return profile;
    }

    private void refreshQuest() {
        if (this.getCurrentQuestList().size() == 0) {
            List<AbstractQuest> quests = ThePit.getInstance()
                    .getQuestFactory()
                    .getQuests();

            List<AbstractQuest> list = new ArrayList<>(quests);
            Collections.shuffle(list);

            for (int i = 0; i < 3; i++) {

                AbstractQuest quest = list.get(i);
                int level = RandomUtil.random.nextInt(quest.getMaxLevel()) + 1;
                this.getCurrentQuestList().add(quest.getQuestInternalName() + ":" + level);

            }
        }
    }

    public boolean isBanned() {
        return this.playerBanData.getEnd() > System.currentTimeMillis();
    }

    public void checkUpdate(double newExperience) {
        final Player player = Bukkit.getPlayer(this.getPlayerUuid());
        if (player != null) {
            applyExperienceToPlayer(player);
            final int newLevel = LevelUtil.getLevelByExp(prestige, newExperience);
            final int oldLevel = this.getLevel();

            if (newLevel > oldLevel) {

                if (newLevel >= 100) {
                    new HundredLevelMedal().setProgress(this, 1);
                }

                final String newLevelTag = LevelUtil.getLevelTag(this.prestige, newExperience);
                final String oldLevelTag = LevelUtil.getLevelTag(this.prestige, this.experience);

                CC.send(MessageType.MISC, player, "&b&l天坑升级! " + oldLevelTag + " &7➠ " + newLevelTag);
                TitleUtil.sendTitle(player, CC.translate("&6&l升 级!"), CC.translate(oldLevelTag + " &7➠ " + newLevelTag), 10, 20, 10);
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            }
        }
    }

    @JsonIgnore
    public void applyExperienceToPlayer(Player player) {
        player.setLevel(getLevel());
        if (getLevel() >= 120) {
            this.experience = LevelUtil.getLevelTotalExperience(this.prestige, 120);
            player.setExp(1);
            return;
        }
        player.setExp(LevelUtil.getLevelProgress(this.prestige, this.experience));
    }

    @JsonIgnore
    public int getLevel() {
        return LevelUtil.getLevelByExp(this.prestige, this.experience);
    }

    @JsonIgnore
    public String getPrestigeColor() {
        return LevelUtil.getPrestigeColor(prestige);
    }

    @JsonIgnore
    public String geLevelColor() {
        return LevelUtil.getLevelColor(this.getLevel());
    }

    @JsonIgnore
    public UUID getPlayerUuid() {
        return UUID.fromString(this.uuid);
    }

    @JsonIgnore
    public void grindCoins(double coins) {
        final Player player = Bukkit.getPlayer(this.getPlayerUuid());
        if (player != null) {
            new PitGainCoinsEvent(player, coins).callEvent();
        }
        this.setGrindedCoins(this.getGrindedCoins() + coins);
    }

    @JsonIgnore
    public void refreshGenesisData() {
        if (ThePit.getInstance().getPitConfig().getGenesisSeason() != this.getGenesisData().getSeason()) {
            int boostTier = this.getGenesisData().getBoostTier();
            GenesisTeam team = this.getGenesisData().getTeam();
            this.setGenesisData(new GenesisData());
            this.getGenesisData().setBoostTier(boostTier);
            this.getGenesisData().setTeam(team);
            this.getGenesisData().setSeason(ThePit.getInstance().getPitConfig().getGenesisSeason());
        }
    }

    public boolean wipe(String reason) {
        PlayerProfile profile = new PlayerProfile(this.getPlayerUuid(), this.playerName);

        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player == null || !player.isOnline()) {
            if (this.isLogin()) {
                return false;
            }
            WipedData wipedData = new WipedData();
            wipedData.setWipedProfile(this);
            wipedData.setReason(reason);
            wipedData.setWipedTimestamp(System.currentTimeMillis());

            profile.setWipedData(wipedData);

            ThePit.getInstance()
                    .getMongoDB()
                    .getProfileCollection()
                    .replaceOne(Filters.eq("uuid", this.uuid), profile, new ReplaceOptions().upsert(true));
        } else {
            if (!this.loaded) {
                return false;
            }
            WipedData wipedData = new WipedData();
            wipedData.setWipedProfile(this);
            wipedData.setWipedTimestamp(System.currentTimeMillis());
            wipedData.setReason(reason);
            profile.setRegisterTime(System.currentTimeMillis());

            profile.setWipedData(wipedData);
            profile.setLoaded(true);

            ThePit.getInstance()
                    .getMongoDB()
                    .getProfileCollection()
                    .replaceOne(Filters.eq("uuid", this.uuid), profile, new ReplaceOptions().upsert(true));

            cacheProfile.put(this.getPlayerUuid(), profile);

            Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                player.kickPlayer("working..");
            });
        }
        return true;
    }

    public boolean unWipe() {
        WipedData wipedData = this.wipedData;
        if (wipedData == null) {
            return false;
        }

        Player player = Bukkit.getPlayer(this.getPlayerUuid());
        if (player != null && player.isOnline()) {
            Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                player.kickPlayer("working..");
            });
        }

        ThePit.getInstance()
                .getMongoDB()
                .getProfileCollection()
                .replaceOne(Filters.eq("uuid", this.uuid), wipedData.getWipedProfile(), new ReplaceOptions().upsert(true));

        return true;
    }

    public String getFormattedName() {
        return getFormattedLevelTag() + " " + RankUtil.getPlayerColoredName(this.getPlayerUuid());
    }

    public String getFormattedNameWithRoman() {
        return getFormattedLevelTagWithRoman() + " " + RankUtil.getPlayerColoredName(this.getPlayerUuid());
    }

    public String getFormattedLevelTag() {
        if (nicked) {
            return LevelUtil.getLevelTag(this.nickPrestige, this.nickLevel);
        }
        return LevelUtil.getLevelTag(this.getPrestige(), this.getLevel());
    }

    public String getFormattedLevelTagWithRoman() {
        if (nicked) {
            return LevelUtil.getLevelTagWithRoman(this.nickPrestige, this.nickLevel);
        }
        return LevelUtil.getLevelTagWithRoman(this.getPrestige(), this.getLevel());
    }

    public void addLiteStreakKill(double value) {
        this.liteStreakKill += value;
        if (liteStreakKill > 1) {
            final int streakKill = (int) liteStreakKill;
            liteStreakKill = liteStreakKill - streakKill;

            this.streakKills += liteStreakKill;
        }
    }

    public int addAndGetStreakNumberShort() {
        if (streakCooldown == null || streakCooldown.hasExpired()) {
            streakCount = 0;
            streakCount++;
            streakCooldown = new Cooldown(10, TimeUnit.SECONDS);
            return streakCount;
        }

        streakCooldown = new Cooldown(10, TimeUnit.SECONDS);
        streakCount++;

        return streakCount;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    @JsonIgnore
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getLowerName() {
        return this.lowerName;
    }

    public void setLowerName(String lowerName) {
        this.lowerName = lowerName;
    }

    public long getRegisterTime() {
        return this.registerTime;
    }

    public void setRegisterTime(long registerTime) {
        this.registerTime = registerTime;
    }

    public long getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public long getLastLogoutTime() {
        return this.lastLogoutTime;
    }

    public void setLastLogoutTime(long lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public long getTotalPlayedTime() {
        return this.totalPlayedTime;
    }

    public void setTotalPlayedTime(long totalPlayedTime) {
        this.totalPlayedTime = totalPlayedTime;
    }

    public long getYearPlayedTime() {
        return this.yearPlayedTime;
    }

    public void setYearPlayedTime(long yearPlayedTime) {
        this.yearPlayedTime = yearPlayedTime;
    }

    public long getMonthPlayedTime() {
        return this.monthPlayedTime;
    }

    public void setMonthPlayedTime(long monthPlayedTime) {
        this.monthPlayedTime = monthPlayedTime;
    }

    public long getWeekPlayedTime() {
        return this.weekPlayedTime;
    }

    public void setWeekPlayedTime(long weekPlayedTime) {
        this.weekPlayedTime = weekPlayedTime;
    }

    public long getTodayPlayedTime() {
        return this.todayPlayedTime;
    }

    public void setTodayPlayedTime(long todayPlayedTime) {
        this.todayPlayedTime = todayPlayedTime;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getAssists() {
        return this.assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getHighestStreaks() {
        return this.highestStreaks;
    }

    public void setHighestStreaks(int highestStreaks) {
        this.highestStreaks = highestStreaks;
    }

    public long getTotalDamage() {
        return this.totalDamage;
    }

    public void setTotalDamage(long totalDamage) {
        this.totalDamage = totalDamage;
    }

    public long getMeleeTotalDamage() {
        return this.meleeTotalDamage;
    }

    public void setMeleeTotalDamage(long meleeTotalDamage) {
        this.meleeTotalDamage = meleeTotalDamage;
    }

    public long getArrowTotalDamage() {
        return this.arrowTotalDamage;
    }

    public void setArrowTotalDamage(long arrowTotalDamage) {
        this.arrowTotalDamage = arrowTotalDamage;
    }

    public long getHurtDamage() {
        return this.hurtDamage;
    }

    public void setHurtDamage(long hurtDamage) {
        this.hurtDamage = hurtDamage;
    }

    public long getMeleeHurtDamage() {
        return this.meleeHurtDamage;
    }

    public void setMeleeHurtDamage(long meleeHurtDamage) {
        this.meleeHurtDamage = meleeHurtDamage;
    }

    public long getBowHurtDamage() {
        return this.bowHurtDamage;
    }

    public void setBowHurtDamage(long bowHurtDamage) {
        this.bowHurtDamage = bowHurtDamage;
    }

    public int getMeleeAttack() {
        return this.meleeAttack;
    }

    public void setMeleeAttack(int meleeAttack) {
        this.meleeAttack = meleeAttack;
    }

    public int getShootAttack() {
        return this.shootAttack;
    }

    public void setShootAttack(int shootAttack) {
        this.shootAttack = shootAttack;
    }

    public int getMeleeHit() {
        return this.meleeHit;
    }

    public void setMeleeHit(int meleeHit) {
        this.meleeHit = meleeHit;
    }

    public int getBowHit() {
        return this.bowHit;
    }

    public void setBowHit(int bowHit) {
        this.bowHit = bowHit;
    }

    public int getRodUsed() {
        return this.rodUsed;
    }

    public void setRodUsed(int rodUsed) {
        this.rodUsed = rodUsed;
    }

    public int getRodHit() {
        return this.rodHit;
    }

    public void setRodHit(int rodHit) {
        this.rodHit = rodHit;
    }

    public int getGoldPicked() {
        return this.goldPicked;
    }

    public void setGoldPicked(int goldPicked) {
        this.goldPicked = goldPicked;
    }

    public int getFishingNumber() {
        return this.fishingNumber;
    }

    public void setFishingNumber(int fishingNumber) {
        this.fishingNumber = fishingNumber;
    }

    public int getGoldenHeadEaten() {
        return this.goldenHeadEaten;
    }

    public void setGoldenHeadEaten(int goldenHeadEaten) {
        this.goldenHeadEaten = goldenHeadEaten;
    }

    public double getExperience() {
        return this.experience;
    }

    public void setExperience(double experience) {
        if (loaded) {
            checkUpdate(experience);
        }
        this.experience = experience;
    }

    public int getPrestige() {
        return this.prestige;
    }

    public void setPrestige(int prestige) {
        if (ThePit.isDEBUG_SERVER()) {
            prestige = 20;
        } else {
            this.prestige = prestige;
        }
    }

    public double getCoins() {
        return this.coins;
    }

    public void setCoins(double coins) {
        this.coins = coins;
    }

    public int getRenown() {
        return this.renown;
    }

    public void setRenown(int renown) {
        final Player player = Bukkit.getPlayer(this.getPlayerUuid());
        if (player != null && renown > this.renown) {
            new PitGainRenownEvent(player, renown - this.renown).callEvent();
        }
        this.renown = renown;
    }

    public int getBounty() {
        return this.bounty;
    }

    public void setBounty(int bounty) {
        if (bounty >= 5000) {
//            final Player player = Bukkit.getPlayer(this.getPlayerUuid());
//            if (player != null) {
//                new MaxBountyMedal().setProgress(PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()), 1);
//            }
        }
        this.bounty = bounty;
    }

    public int getActionBounty() {
        return this.actionBounty;
    }

    public void setActionBounty(int actionBounty) {
        this.actionBounty = actionBounty;
    }

    public double getRespawnTime() {
        return this.respawnTime;
    }

    public void setRespawnTime(double respawnTime) {
        this.respawnTime = respawnTime;
    }

    public PlayerInv getInventory() {
        return this.inventory;
    }

    public void setInventory(PlayerInv inv) {
        if (this.tempInvUsing) {
            return;
        }

        this.inventory = inv;
    }

    public PlayerEnderChest getEnderChest() {
        return this.enderChest;
    }

    public void setEnderChest(PlayerEnderChest enderChest) {
        this.enderChest = enderChest;
    }

    public int getEnderChestRow() {
        return this.enderChestRow;
    }

    public void setEnderChestRow(int enderChestRow) {
        this.enderChestRow = enderChestRow;
    }

    @Deprecated
    public List<PerkData> getUnlockedPerk() {
        return this.unlockedPerk;
    }

    public void setUnlockedPerk(List<PerkData> unlockedPerk) {
        this.unlockedPerk = unlockedPerk;
    }

    @Deprecated
    public List<PerkData> getBoughtPerk() {
        return this.boughtPerk;
    }

    public void setBoughtPerk(List<PerkData> boughtPerk) {
        this.boughtPerk = boughtPerk;
    }

    public Set<String> getUsedCdk() {
        return this.usedCdk;
    }

    public void setUsedCdk(Set<String> usedCdk) {
        this.usedCdk = usedCdk;
    }

    public Map<Integer, PerkData> getChosePerk() {
        return this.chosePerk;
    }

    public void setChosePerk(Map<Integer, PerkData> chosePerk) {
        this.chosePerk = chosePerk;
    }

    public double getTotalExp() {
        return this.totalExp;
    }

    public void setTotalExp(double totalExp) {
        this.totalExp = totalExp;
    }

    public List<String> getAutoBuyButtons() {
        return this.autoBuyButtons;
    }

    public void setAutoBuyButtons(List<String> autoBuyButtons) {
        this.autoBuyButtons = autoBuyButtons;
    }

    public TradeLimit getTradeLimit() {
        return this.tradeLimit;
    }

    public void setTradeLimit(TradeLimit tradeLimit) {
        this.tradeLimit = tradeLimit;
    }

    public MedalData getMedalData() {
        return this.medalData;
    }

    public void setMedalData(MedalData medalData) {
        this.medalData = medalData;
    }

    public QuestLimit getQuestLimit() {
        return this.questLimit;
    }

    public void setQuestLimit(QuestLimit questLimit) {
        this.questLimit = questLimit;
    }

    public OfferData getOfferData() {
        return this.offerData;
    }

    public void setOfferData(OfferData offerData) {
        this.offerData = offerData;
    }

    public double getGrindedCoins() {
        return this.grindedCoins;
    }

    public void setGrindedCoins(double grindedCoins) {
        this.grindedCoins = grindedCoins;
    }

    public PlayerOption getPlayerOption() {
        return this.playerOption;
    }

    public void setPlayerOption(PlayerOption playerOption) {
        this.playerOption = playerOption;
    }

    public PlayerBanData getPlayerBanData() {
        return this.playerBanData;
    }

    public void setPlayerBanData(PlayerBanData playerBanData) {
        this.playerBanData = playerBanData;
    }

    public boolean isSupporter() {
        PitInternalHook api = ThePit.getApi();
        if (api == null) return false;

        if (supporter && !api.getRemoveSupportWhenNoPermission()) return true;

        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return false;

        return player.hasPermission(api.getPitSupportPermission());
    }

    public void setSupporter(boolean supporter) {
        this.supporter = supporter;
    }

    public boolean isSupporterGivenByAdmin() {
        return this.supporterGivenByAdmin;
    }

    public void setSupporterGivenByAdmin(boolean supporterGivenByAdmin) {
        this.supporterGivenByAdmin = supporterGivenByAdmin;
    }

    public int getRemedyLevel() {
        return this.remedyLevel;
    }

    public void setRemedyLevel(int remedyLevel) {
        this.remedyLevel = remedyLevel;
    }

    public double getRemedyExp() {
        return this.remedyExp;
    }

    public void setRemedyExp(double remedyExp) {
        this.remedyExp = remedyExp;
    }

    public String getRemedyDate() {
        return this.remedyDate;
    }

    public void setRemedyDate(String remedyDate) {
        this.remedyDate = remedyDate;
    }

    public int getTotalFishTimes() {
        return this.totalFishTimes;
    }

    public void setTotalFishTimes(int totalFishTimes) {
        this.totalFishTimes = totalFishTimes;
    }

    public int getTotalFishTreasureTimes() {
        return this.totalFishTreasureTimes;
    }

    public void setTotalFishTreasureTimes(int totalFishTreasureTimes) {
        this.totalFishTreasureTimes = totalFishTreasureTimes;
    }

    public int getTotalFishTrashTimes() {
        return this.totalFishTrashTimes;
    }

    public void setTotalFishTrashTimes(int totalFishTrashTimes) {
        this.totalFishTrashTimes = totalFishTrashTimes;
    }

    public QuestData getCurrentQuest() {
        return this.currentQuest;
    }

    public void setCurrentQuest(QuestData currentQuest) {
        this.currentQuest = currentQuest;
    }

    public QuestData getLastQuest() {
        return this.lastQuest;
    }

    public void setLastQuest(QuestData lastQuest) {
        this.lastQuest = lastQuest;
    }

    public boolean isNightQuestEnable() {
        return this.nightQuestEnable;
    }

    public void setNightQuestEnable(boolean nightQuestEnable) {
        this.nightQuestEnable = nightQuestEnable;
    }

    public QuestCenter getQuestCenter() {
        return this.questCenter;
    }

    public void setQuestCenter(QuestCenter questCenter) {
        this.questCenter = questCenter;
    }

    public GenesisData getGenesisData() {
        return this.genesisData;
    }

    public void setGenesisData(GenesisData genesisData) {
        this.genesisData = genesisData;
    }

    public List<String> getCurrentQuestList() {
        return this.currentQuestList;
    }

    public void setCurrentQuestList(List<String> currentQuestList) {
        this.currentQuestList = currentQuestList;
    }

    public double getMaxHealth() {
        return 20.0 + getExtraMaxHealthValue();
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public float getMoveSpeed() {
        return this.moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public String getEnchantingItem() {
        return this.enchantingItem;
    }

    public void setEnchantingItem(String enchantingItem) {
        this.enchantingItem = enchantingItem;
    }

    public String getEnchantingScience() {
        return this.enchantingScience;
    }

    public void setEnchantingScience(String enchantingScience) {
        this.enchantingScience = enchantingScience;
    }

    public boolean isLogin() {
        return this.login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public WipedData getWipedData() {
        return this.wipedData;
    }

    public void setWipedData(WipedData wipedData) {
        this.wipedData = wipedData;
    }

    public List<String> getClaimedMail() {
        return this.claimedMail;
    }

    public void setClaimedMail(List<String> claimedMail) {
        this.claimedMail = claimedMail;
    }

    public BuffData getBuffData() {
        return this.buffData;
    }

    public void setBuffData(BuffData buffData) {
        this.buffData = buffData;
    }

    public boolean isInArena() {
        return this.inArena;
    }

    public void setInArena(boolean inArena) {
        if (inArena && !this.inArena) {
            final Player player = Bukkit.getPlayer(getPlayerUuid());
            if (player != null) {
                UtilKt.releaseItem(player);
            }
        }

        this.inArena = inArena;
    }

    public double getStreakKills() {
        return this.streakKills;
    }

    public void setStreakKills(double kills) {
        final PitStreakKillChangeEvent event = new PitStreakKillChangeEvent(this, this.streakKills, kills);
        if (kills > 0) {
            event.callEvent();
        }
        if (event.isCancelled()) {
            return;
        }

        this.streakKills = kills;
    }

    public Cooldown getCombatTimer() {
        return this.combatTimer;
    }

    public void setCombatTimer(Cooldown combatTimer) {
        this.combatTimer = combatTimer;
    }

    public boolean isEditingMode() {
        return this.editingMode;
    }

    public void setEditingMode(boolean editingMode) {
        this.editingMode = editingMode;
    }

    public double getDamageReduced() {
        return this.damageReduced;
    }

    public void setDamageReduced(double damageReduced) {
        this.damageReduced = damageReduced;
    }

    public Map<UUID, DamageData> getDamageMap() {
        return this.damageMap;
    }

    public void setDamageMap(Map<UUID, DamageData> damageMap) {
        this.damageMap = damageMap;
    }

    public int getStrengthNum() {
        return this.strengthNum;
    }

    public void setStrengthNum(int strengthNum) {
        this.strengthNum = strengthNum;
    }

    public Cooldown getStrengthTimer() {
        return this.strengthTimer;
    }

    public void setStrengthTimer(Cooldown strengthTimer) {
        this.strengthTimer = strengthTimer;
    }

    public UUID getLastKilledPlayer() {
        return this.lastKilledPlayer;
    }

    public void setLastKilledPlayer(UUID lastKilledPlayer) {
        this.lastKilledPlayer = lastKilledPlayer;
    }

    public Cooldown getBountyCooldown() {
        return this.bountyCooldown;
    }

    public void setBountyCooldown(Cooldown bountyCooldown) {
        this.bountyCooldown = bountyCooldown;
    }

    public int getBountyStreak() {
        return this.bountyStreak;
    }

    public void setBountyStreak(int bountyStreak) {
        this.bountyStreak = bountyStreak;
    }

    public KillRecap getKillRecap() {
        return this.killRecap;
    }

    public void setKillRecap(KillRecap killRecap) {
        this.killRecap = killRecap;
    }

    public boolean isScreenShare() {
        return this.screenShare;
    }

    public void setScreenShare(boolean screenShare) {
        this.screenShare = screenShare;
    }

    public String getScreenShareQQ() {
        return this.screenShareQQ;
    }

    public void setScreenShareQQ(String screenShareQQ) {
        this.screenShareQQ = screenShareQQ;
    }

    public PlayerMailData getMailData() {
        return this.mailData;
    }

    public void setMailData(PlayerMailData mailData) {
        this.mailData = mailData;
    }

    public boolean isNicked() {
        return this.nicked;
    }

    public void setNicked(boolean nicked) {
        this.nicked = nicked;
    }

    public int getNickPrestige() {
        return this.nickPrestige;
    }

    public void setNickPrestige(int nickPrestige) {
        this.nickPrestige = nickPrestige;
    }

    public int getNickLevel() {
        return this.nickLevel;
    }

    public void setNickLevel(int nickLevel) {
        this.nickLevel = nickLevel;
    }

    public boolean isTempInvUsing() {
        return this.tempInvUsing;
    }

    public void setTempInvUsing(boolean tempInvUsing) {
        this.tempInvUsing = tempInvUsing;
    }

    public boolean isNoDamageAnimations() {
        return this.noDamageAnimations;
    }

    public void setNoDamageAnimations(boolean noDamageAnimations) {
        this.noDamageAnimations = noDamageAnimations;
    }

    public double getLiteStreakKill() {
        return this.liteStreakKill;
    }

    public void setLiteStreakKill(double liteStreakKill) {
        this.liteStreakKill = liteStreakKill;
    }

    public long getLastActionTimestamp() {
        return this.lastActionTimestamp;
    }

    public void setLastActionTimestamp(long lastActionTimestamp) {
        this.lastActionTimestamp = lastActionTimestamp;
    }

    public List<PlayerInvBackup> getInvBackups() {
        return this.invBackups;
    }

    public void setInvBackups(List<PlayerInvBackup> invBackups) {
        this.invBackups = invBackups;
    }

    public double getGoldStackAddon() {
        return goldStackAddon;
    }

    public void setGoldStackAddon(double goldStackAddon) {
        this.goldStackAddon = goldStackAddon;
    }

    public double getGoldStackMax() {
        return goldStackMax;
    }

    public void setGoldStackMax(double goldStackMax) {
        this.goldStackMax = goldStackMax;
    }

    public double getXpStackAddon() {
        return xpStackAddon;
    }

    public void setXpStackAddon(double xpStackAddon) {
        this.xpStackAddon = xpStackAddon;
    }

    public double getXpStackMax() {
        return xpStackMax;
    }

    public void setXpStackMax(double xpStackMax) {
        this.xpStackMax = xpStackMax;
    }

    public int getTodayCompletedUber() {
        return todayCompletedUber;
    }

    public void setTodayCompletedUber(int todayCompletedUber) {
        this.todayCompletedUber = todayCompletedUber;
    }

    public long getTodayCompletedUberLastRefreshed() {
        return todayCompletedUberLastRefreshed;
    }

    public void setTodayCompletedUberLastRefreshed(long todayCompletedUberLastRefreshed) {
        this.todayCompletedUberLastRefreshed = todayCompletedUberLastRefreshed;
    }

    public Cooldown getStreakCooldown() {
        return streakCooldown;
    }

    public void setStreakCooldown(Cooldown streakCooldown) {
        this.streakCooldown = streakCooldown;
    }

    public int getStreakCount() {
        return streakCount;
    }

    public void setStreakCount(int streakCount) {
        this.streakCount = streakCount;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public long getLastDamageAt() {
        return lastDamageAt;
    }

    public void setLastDamageAt(long lastDamageAt) {
        this.lastDamageAt = lastDamageAt;
    }

    public HashMap<String, Double> getExtraMaxHealth() {
        return extraMaxHealth;
    }

    public void setExtraMaxHealth(HashMap<String, Double> extraMaxHealth) {
        this.extraMaxHealth = extraMaxHealth;
    }

    public double getExtraMaxHealthValue() {
        return extraMaxHealth
                .values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public String getEnchantingBook() {
        return enchantingBook;
    }

    public void setEnchantingBook(String enchantingBook) {
        this.enchantingBook = enchantingBook;
    }

    public Map<String, PerkData> getUnlockedPerkMap() {
        return unlockedPerkMap;
    }

    public Map<String, PerkData> getBoughtPerkMap() {
        return boughtPerkMap;
    }

    public void setUnlockedPerkMap(Map<String, PerkData> unlockedPerkMap) {
        this.unlockedPerkMap = unlockedPerkMap;
    }

    public void setBoughtPerkMap(Map<String, PerkData> boughtPerkMap) {
        this.boughtPerkMap = boughtPerkMap;
    }

    public int getProfileFormatVersion() {
        return profileFormatVersion;
    }

    public void setProfileFormatVersion(int profileFormatVersion) {
        this.profileFormatVersion = profileFormatVersion;
    }
}
