package cn.charlotte.pit.config;

import cn.charlotte.pit.util.configuration.Configuration;
import cn.charlotte.pit.util.configuration.annotations.ConfigData;
import cn.charlotte.pit.util.configuration.annotations.ConfigSerializer;
import cn.charlotte.pit.util.configuration.serializer.LocationSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/30 22:02
 */
@Getter
@Setter
public class PitConfig extends Configuration {

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ConfigData(
            path = "service.mongodb.ip"
    )
    private String mongoDBAddress;
    @ConfigData(
            path = "service.mongodb.port"
    )
    private int mongoDBPort;

    @ConfigData(
            path = "service.mongodb.database"
    )
    private String databaseName;

    @ConfigData(
            path = "service.mongodb.user"
    )
    private String mongoUser;

    @ConfigData(
            path = "service.mongodb.password"
    )
    private String mongoPassword;

    @ConfigData(
            path = "service.redis.enable"
    )
    private boolean redisEnable;

    @ConfigData(
            path = "service.redis.ip"
    )
    private String redisAddress;
    @ConfigData(
            path = "service.redis.port"
    )
    private int redisPort;

    @ConfigData(
            path = "service.redis.password"
    )
    private String redisPassword;

    @ConfigData(
            path = "enchants.disable"
    )
    public List<String> disableEnchants = new ArrayList<>();

    @ConfigData(
            path = "arenaHighestY"
    )
    private int arenaHighestY;
    @ConfigData(
            path = "loc.spawn"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private List<Location> spawnLocations = new ArrayList<>();
    @ConfigData(
            path = "loc.npc.shop"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location shopNpcLocation;
    @ConfigData(
            path = "loc.npc.quest"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location questNpcLocation;
    @ConfigData(
            path = "loc.npc.perk"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location perkNpcLocation;

    @ConfigData(path = "server-name")
    private String serverName = "&e天坑乱斗";

    @ConfigData(
            path = "loc.npc.prestige"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location prestigeNpcLocation;
    @ConfigData(
            path = "loc.npc.status"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location statusNpcLocation;
    @ConfigData(
            path = "loc.npc.keeper"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location keeperNpcLocation;
    @ConfigData(
            path = "loc.npc.leaderBoard"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location leaderBoardNpcLocation;

    @ConfigData(
            path = "loc.npc.mail"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location mailNpcLocation;
    @ConfigData(
            path = "loc.npc.genesis_demon"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location genesisDemonNpcLocation;
    @ConfigData(
            path = "loc.hologram.spawn"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location hologramLocation;

    @ConfigData(
            path = "loc.npc.genesis_angel"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location genesisAngelNpcLocation;
    @ConfigData(
            path = "loc.hologram.leaderBoard"
    )

    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location leaderBoardHologram;
    @ConfigData(
            path = "loc.hologram.mythic"
    )

    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location mythicHologram;
    @ConfigData(
            path = "loc.hologram.chest"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location chestHologram;
    @ConfigData(
            path = "loc.hologram.helper"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location helperHologramLocation;
    @ConfigData(
            path = "loc.region.pitA"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location pitLocA;
    @ConfigData(
            path = "loc.region.pitB"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location pitLocB;
    @ConfigData(
            path = "loc.region.enchant"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location enchantLocation;
    @ConfigData(
            path = "loc.events.hamburger.shop"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location hamburgerShopLoc;
    @ConfigData(
            path = "loc.events.hamburger.villager.a"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private List<Location> hamburgerNpcLocA = new ArrayList<>();
    @ConfigData(
            path = "loc.events.spire.spireLoc"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location spireLoc;
    //每层塔地面中心坐标 (1~9)
    @ConfigData(
            path = "loc.events.spire.spireFloorLocations"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private List<Location> spireFloorLoc = new ArrayList<>();
    @ConfigData(
            path = "loc.events.spire.spireFloorY"
    )
    private List<Integer> floorY = new ArrayList<>();
    @ConfigData(
            path = "loc.events.hamburger.villager.a-offer" //the villager who offer the ham
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location hamburgerOfferNpcLocA;
    @ConfigData(
            path = "loc.events.rage.middle" //the middle point of rage pit
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location ragePitMiddle;
    @ConfigData(
            path = "loc.events.rage.radius" //the radius of rage pit
    )
    private int ragePitRadius;
    @ConfigData(
            path = "loc.events.rage.height" //the height of rage pit
    )
    private int ragePitHeight;
    @ConfigData(
            path = "loc.portal.posA" //Middle portal posA
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location portalPosA;
    @ConfigData(
            path = "loc.portal.posB" //Middle portal posA
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location portalPosB;

    @ConfigData(path = "loc.events.egg.loc")
    @ConfigSerializer(serializer = LocationSerializer.class)
    public Location eggLoc;
    @ConfigData(path = "loc.events.koth.loc")
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location kothLoc;

    @ConfigData(
            path = "loc.events.cake.a.posA" //cake posA
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneAPosA;
    @ConfigData(
            path = "loc.events.cake.a.posB" //cake posB
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneAPosB;
    @ConfigData(
            path = "loc.events.cake.b.posA" //cake posA
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneBPosA;
    @ConfigData(
            path = "loc.events.cake.b.posB" //cake posB
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneBPosB;
    @ConfigData(
            path = "loc.events.cake.c.posA" //cake posA
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneCPosA;
    @ConfigData(
            path = "loc.events.cake.c.posB" //cake posB
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneCPosB;
    @ConfigData(
            path = "loc.events.cake.d.posA" //cake posA
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneDPosA;
    @ConfigData(
            path = "loc.events.cake.d.posB" //cake posB
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private Location cakeZoneDPosB;

    @ConfigData(
            path = "loc.Genesis.angel"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private List<Location> angelSpawns = new ArrayList<>();

    @ConfigData(
            path = "loc.Genesis.demon"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private List<Location> demonSpawns = new ArrayList<>();

    @ConfigData(
            path = "loc.packages"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    private List<Location> packageLocations = new ArrayList<>();

    @ConfigData(
            path = "debug.debugServer"
    )
    private boolean debugServer;
    @ConfigData(
            path = "debug.public"
    )
    private boolean debugServerPublic;
    @ConfigData(
            path = "debug.infinityNpcLoc"
    )
    private Location infinityNpcLocation;
    @ConfigData(
            path = "debug.ienchantNpcLoc"
    )
    private Location enchantNpcLocation;
    @ConfigData(
            path = "booster"
    )
    private double booster = 1.0;

    @ConfigData(path = "beta-version")
    public boolean betaVersion = false;

    @ConfigData(
            path = "auth-code"
    )
    public String code = "xxxx";
    @ConfigData(
            path = "firewell-code"
    )
    private String firewellcode = "null";

    @ConfigData(
            path = "curfew.enable"
    )
    private boolean curfewEnable;
    @ConfigData(
            path = "curfew.start"
    )
    private int curfewStart;
    @ConfigData(
            path = "curfew.end"
    )
    private int curfewEnd;

    @ConfigData(
            path = "loc.Sewers.chests"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    public List<Location> sewersChestsLocations = new ArrayList<>();

    @ConfigData(
            path = "loc.squads"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    public List<Location> squadsLocations = new ArrayList<>();

    @ConfigData(
            path = "loc.blockHead"
    )
    @ConfigSerializer(serializer = LocationSerializer.class)
    public List<Location> blockHeadLocations = new ArrayList<>();

    @ConfigData(
            path = "genesis-start-date"
    )
    private long genesisStartDate = 1675339795842L;

    public PitConfig(JavaPlugin plugin) {
        super(plugin);
    }

    public boolean isGenesisEnable() {
        try {
            return System.currentTimeMillis() >= getGenesisStartTime() && System.currentTimeMillis() < getGenesisEndTime();
        } catch (Exception ignored) {
            return false;
        }
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");

    public long getGenesisStartTime() {
        return genesisStartDate;
    }

    public long getGenesisOriginalEndTime() {
        return getGenesisStartTime() + 16 * 24 * 60 * 60 * 1000;
    }

    public long getGenesisEndTime() {
        long endTime = getGenesisOriginalEndTime();
        while (endTime < System.currentTimeMillis()) {
            endTime += 56 * 24 * 60 * 60 * 1000L;
        }
        return endTime;
    }

    //Season X: From Season X-1 End To Season X End
    public int getGenesisSeason() {
        int season = 1;
        long endTime = getGenesisOriginalEndTime();
        while (endTime < System.currentTimeMillis()) {
            endTime += 56L * 24 * 60 * 60 * 1000;
            season++;
        }
        return season;
    }
}
