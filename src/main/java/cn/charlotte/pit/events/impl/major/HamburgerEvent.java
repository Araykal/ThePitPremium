package cn.charlotte.pit.events.impl.major;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.config.NewConfiguration;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PlacedBlockData;
import cn.charlotte.pit.event.PitKillEvent;
import cn.charlotte.pit.event.PitProfileLoadedEvent;
import cn.charlotte.pit.events.IEpicEvent;
import cn.charlotte.pit.events.IEvent;
import cn.charlotte.pit.events.IScoreBoardInsert;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.medal.impl.challenge.PizzaEventMedal;
import cn.charlotte.pit.runnable.ClearRunnable;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.Utils;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.hologram.Hologram;
import cn.charlotte.pit.util.hologram.HologramAPI;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.charlotte.pit.util.random.RandomUtil;
import cn.charlotte.pit.util.time.TimeUtil;
import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftVillager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/13 11:48
 */
@Getter
public class HamburgerEvent implements IEvent, IEpicEvent, Listener, IScoreBoardInsert {
    private final static List<String> names;
    private final static String skinValue;

    static {
        skinValue = "eyJ0aW1lc3RhbXAiOjE0NzkxODY3Mjg0MTMsInByb2ZpbGVJZCI6ImYzYjU2YWJiNmM1YTQ2YTM4YTZlMTdiNmFjN2IxMGMzIiwicHJvZmlsZU5hbWUiOiJmb29kIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iMGUzOGMxNzZkYmY3ZGY5YjA2MzJjMjU2ZWViNmM1YWFjYTk5ZTFjOGMxYTUzMDY1NmVhZmYwNDE3YWVkMjIifX19";
        names = Arrays.asList(
                "亚伦",
                "亚伯",
                "亚伯拉罕",
                "亚当",
                "艾德里安",
                "艾登",
                "阿尔瓦",
                "亚历克斯",
                "亚历山大",
                "艾伦",
                "艾伯特",
                "阿尔弗雷德",
                "安德鲁",
                "安迪",
                "安格斯",
                "安东尼",
                "阿波罗",
                "阿诺德",
                "亚瑟",
                "奥古斯特",
                "奥斯汀",
                "本",
                "本杰明",
                "伯特",
                "本森",
                "比尔",
                "比利",
                "布莱克",
                "鲍伯",
                "鲍比",
                "布拉德",
                "布兰登",
                "布兰特",
                "布伦特",
                "布赖恩",
                "布朗",
                "布鲁斯",
                "迦勒",
                "卡梅伦",
                "卡尔",
                "卡洛斯",
                "凯里",
                "卡斯帕",
                "塞西",
                "查尔斯",
                "采尼",
                "克里斯",
                "克里斯蒂安",
                "克里斯多夫",
                "克拉克",
                "柯利弗",
                "科迪",
                "科尔",
                "科林",
                "科兹莫",
                "丹尼尔",
                "丹尼",
                "达尔文",
                "大卫",
                "丹尼斯",
                "德里克",
                "狄克",
                "唐纳德",
                "道格拉斯",
                "杜克",
                "迪伦",
                "埃迪",
                "埃德加",
                "爱迪生",
                "艾德蒙",
                "爱德华",
                "艾德文",
                "以利亚",
                "艾略特",
                "埃尔维斯",
                "埃里克",
                "伊桑",
                "柳真",
                "埃文",
                "企业英语培训",
                "福特",
                "弗兰克思",
                "弗兰克",
                "富兰克林",
                "弗瑞德",
                "加百利",
                "加比",
                "加菲尔德",
                "加里",
                "加文",
                "杰弗里",
                "乔治",
                "基诺",
                "格林",
                "格林顿",
                "汉克",
                "哈帝",
                "哈里森",
                "哈利",
                "海顿",
                "亨利",
                "希尔顿",
                "雨果",
                "汉克",
                "霍华德",
                "亨利",
                "伊恩",
                "伊格纳缇伍兹",
                "伊凡",
                "艾萨克",
                "以赛亚",
                "杰克",
                "杰克逊",
                "雅各布",
                "詹姆士",
                "詹森",
                "杰伊",
                "杰弗瑞",
                "杰罗姆",
                "杰瑞",
                "杰西",
                "吉姆",
                "吉米",
                "乔",
                "约翰",
                "约翰尼",
                "乔纳森",
                "乔丹",
                "约瑟夫",
                "约书亚",
                "贾斯汀",
                "凯斯",
                "肯",
                "肯尼迪",
                "肯尼斯",
                "肯尼",
                "凯文",
                "凯尔",
                "兰斯",
                "拉里",
                "劳伦特",
                "劳伦斯",
                "利安德尔",
                "李",
                "雷欧",
                "雷纳德",
                "利奥波特",
                "莱斯利",
                "劳伦",
                "劳瑞",
                "劳瑞恩",
                "路易斯",
                "路加",
                "马库斯",
                "马西",
                "马克",
                "马科斯",
                "马尔斯",
                "马歇尔",
                "马丁",
                "马文",
                "梅森",
                "马修",
                "马克斯",
                "迈克尔",
                "米奇",
                "麦克",
                "内森",
                "纳撒尼尔",
                "尼尔",
                "尼尔森",
                "尼古拉斯",
                "尼克",
                "诺亚",
                "诺曼",
                "奥利弗",
                "奥斯卡",
                "欧文",
                "帕特里克",
                "保罗",
                "彼得",
                "菲利普",
                "菲比",
                "昆廷",
                "兰德尔",
                "伦道夫",
                "兰迪",
                "雷",
                "雷蒙德",
                "列得",
                "雷克斯",
                "理查德",
                "里奇",
                "赖利/瑞利",
                "罗伯特",
                "罗宾",
                "鲁宾逊",
                "洛克",
                "罗杰",
                "罗纳",
                "罗文",
                "罗伊",
                "赖安",
                "萨姆",
                "萨米",
                "塞缪尔",
                "斯考特",
                "肖恩",
                "肖恩",
                "西德尼",
                "西蒙",
                "所罗门",
                "斯帕克",
                "斯宾塞",
                "斯派克",
                "斯坦利",
                "史蒂夫",
                "史蒂文",
                "斯图尔特",
                "斯图亚特",
                "特伦斯",
                "特里",
                "泰德",
                "托马斯",
                "提姆",
                "蒂莫西",
                "托德",
                "汤米",
                "汤姆",
                "托马斯",
                "托尼",
                "泰勒",
                "奥特曼",
                "尤利塞斯",
                "范",
                "弗恩",
                "弗农",
                "维克多",
                "文森特",
                "华纳",
                "沃伦",
                "韦恩",
                "卫斯理",
                "威廉",
                "维利",
                "扎克",
                "圣扎迦利",
                "阿比盖尔",
                "艾比",
                "艾达",
                "阿德莱德",
                "艾德琳",
                "亚历桑德拉",
                "艾丽莎",
                "艾米",
                "亚历克西斯",
                "爱丽丝",
                "艾丽西娅",
                "艾琳娜",
                "艾莉森",
                "艾莉莎",
                "阿曼达",
                "艾美",
                "安伯",
                "阿纳斯塔西娅",
                "安德莉亚",
                "安琪",
                "安吉拉",
                "安吉莉亚",
                "安吉莉娜",
                "安",
                "安娜",
                "安妮",
                "安妮",
                "安尼塔",
                "艾莉尔",
                "阿普里尔",
                "艾许莉",
                "欧蕊",
                "阿维娃",
                "笆笆拉",
                "芭比",
                "贝亚特",
                "比阿特丽斯",
                "贝基",
                "贝拉",
                "贝斯",
                "贝蒂",
                "贝蒂",
                "布兰奇",
                "邦妮",
                "布伦达",
                "布莱安娜",
                "布兰妮",
                "布列塔尼",
                "卡米尔",
                "莰蒂丝",
                "坎蒂",
                "卡瑞娜",
                "卡门",
                "凯罗尔",
                "卡罗琳",
                "凯丽",
                "凯莉",
                "卡桑德拉",
                "凯西",
                "凯瑟琳",
                "凯茜",
                "切尔西",
                "沙琳",
                "夏洛特",
                "切莉",
                "雪莉尔",
                "克洛伊",
                "克莉丝",
                "克里斯蒂娜",
                "克里斯汀",
                "克里斯蒂",
                "辛迪",
                "克莱尔",
                "克劳迪娅",
                "克莱门特",
                "克劳瑞丝",
                "康妮",
                "康斯坦斯",
                "科拉",
                "科瑞恩",
                "科瑞斯特尔",
                "戴茜",
                "达芙妮",
                "达茜",
                "戴夫",
                "黛比",
                "黛博拉",
                "黛布拉",
                "黛米",
                "黛安娜",
                "德洛丽丝",
                "堂娜",
                "多拉",
                "桃瑞丝",
                "伊迪丝",
                "伊迪萨",
                "伊莱恩",
                "埃莉诺",
                "伊丽莎白",
                "埃拉",
                "爱伦",
                "艾莉",
                "艾米瑞达",
                "艾米丽",
                "艾玛",
                "伊妮德",
                "埃尔莎",
                "埃莉卡",
                "爱斯特尔",
                "爱丝特",
                "尤杜拉",
                "伊娃",
                "伊芙",
                "伊夫林",
                "芬妮",
                "费怡",
                "菲奥纳",
                "福罗拉",
                "弗罗伦丝",
                "弗郎西丝",
                "弗雷德里卡",
                "弗里达",
                "上海英语外教",
                "吉娜",
                "吉莉安",
                "格拉蒂丝",
                "格罗瑞娅",
                "格瑞丝",
                "格瑞丝",
                "格瑞塔",
                "格温多琳",
                "汉娜",
                "海莉",
                "赫柏",
                "海伦娜",
                "海伦",
                "汉纳",
                "海蒂",
                "希拉里",
                "英格丽德",
                "伊莎贝拉",
                "爱沙拉",
                "艾琳",
                "艾丽丝",
                "艾维",
                "杰奎琳",
                "小玉",
                "詹米",
                "简",
                "珍妮特",
                "贾斯敏",
                "姬恩",
                "珍娜",
                "詹妮弗",
                "詹妮",
                "杰西卡",
                "杰西",
                "姬尔",
                "琼",
                "乔安娜",
                "乔斯林",
                "乔莉埃特",
                "约瑟芬",
                "乔茜",
                "乔伊",
                "乔伊斯",
                "朱迪丝",
                "朱蒂",
                "朱莉娅",
                "朱莉安娜",
                "朱莉",
                "朱恩",
                "凯琳",
                "卡瑞达",
                "凯瑟琳",
                "凯特",
                "凯西",
                "卡蒂",
                "卡特里娜",
                "凯",
                "凯拉",
                "凯莉",
                "凯尔西",
                "特里娜",
                "基蒂",
                "莱瑞拉",
                "蕾西",
                "劳拉",
                "罗兰",
                "莉娜",
                "莉迪娅",
                "莉莲",
                "莉莉",
                "琳达",
                "琳赛",
                "丽莎",
                "莉兹",
                "洛拉",
                "罗琳",
                "路易莎",
                "路易丝",
                "露西娅",
                "露茜",
                "露西妮",
                "露露",
                "莉迪娅",
                "林恩",
                "梅布尔",
                "马德琳",
                "玛姬",
                "玛米",
                "曼达",
                "曼迪",
                "玛格丽特",
                "玛丽亚",
                "玛丽莲",
                "玛莎",
                "梅维丝",
                "玛丽",
                "玛蒂尔达",
                "莫琳",
                "梅维丝",
                "玛克辛",
                "梅",
                "梅米",
                "梅甘",
                "梅琳达",
                "梅利莎",
                "美洛蒂",
                "默西迪丝",
                "梅瑞狄斯",
                "米娅",
                "米歇尔",
                "米莉",
                "米兰达",
                "米里亚姆",
                "米娅",
                "茉莉",
                "莫尼卡",
                "摩根",
                "南茜",
                "娜塔莉",
                "娜塔莎",
                "妮可",
                "尼基塔",
                "尼娜",
                "诺拉",
                "诺玛",
                "尼迪亚",
                "奥克塔维亚",
                "奥琳娜",
                "奥利维亚",
                "奥菲莉娅",
                "奥帕",
                "帕梅拉",
                "帕特丽夏",
                "芭迪",
                "保拉",
                "波琳",
                "珀尔",
                "帕姬",
                "菲洛米娜",
                "菲比",
                "菲丽丝",
                "波莉",
                "普里西拉",
                "昆蒂娜",
                "雷切尔",
                "丽贝卡",
                "瑞加娜",
                "丽塔",
                "罗丝",
                "洛克萨妮",
                "露丝",
                "萨布丽娜",
                "萨莉",
                "桑德拉",
                "萨曼莎",
                "萨米",
                "桑德拉",
                "桑迪",
                "莎拉",
                "萨瓦纳",
                "斯佳丽",
                "塞尔玛",
                "塞琳娜",
                "塞丽娜",
                "莎伦",
                "希拉",
                "雪莉",
                "雪丽",
                "雪莉",
                "斯莱瑞",
                "西尔维亚",
                "索尼亚",
                "索菲娅",
                "丝塔茜",
                "丝特拉",
                "斯蒂芬妮",
                "苏",
                "萨妮",
                "苏珊",
                "塔玛拉",
                "苔米",
                "谭雅坦尼娅",
                "塔莎",
                "特莉萨",
                "苔丝",
                "蒂凡妮",
                "蒂娜",
                "棠雅",
                "特蕾西",
                "厄休拉",
                "温妮莎",
                "维纳斯",
                "维拉",
                "维姬",
                "维多利亚",
                "维尔莉特",
                "维吉妮亚",
                "维达",
                "薇薇安");
    }

    private final Map<UUID, PizzaData> pizzaDataMap;
    private final ItemStack pizza;
    private final Map<UUID, VillagerData> villagerDataMap;
    private final List<Location> spawnedLocations;
    private Location location;
    private Villager villager;
    private Hologram hologram;
    private int done;
    private boolean end;
    private BukkitRunnable runnable;
    private Cooldown timer;
    private EditSession session;

    @SneakyThrows
    public HamburgerEvent() {
        this.pizzaDataMap = new HashMap<>();
        this.villagerDataMap = new HashMap<>();
        this.spawnedLocations = new ArrayList<>();
        this.pizza = new ItemBuilder(Material.SKULL_ITEM)
                .durability(3)
                .internalName("ham")
                .removeOnJoin(true)
                .name("&c汉堡!")
                .lore("&7请将这些汉堡配送到附近的村民手里...", "&7&m配送要求:", "  &7&m不可飞行,不可攀爬,不可冲刺,不可沾染元素气息", "")
                .setSkullProperty(skinValue)
                .build();
    }

    public static void redo(Player player, Location location) {
        player.sendBlockChange(location.clone().add(0, -1, 0), location.clone().add(0, -1, 0).getBlock().getType(), location.clone().add(0, -1, 0).getBlock().getData());
        player.sendBlockChange(location.clone().add(0, -2, 0), location.clone().add(0, -2, 0).getBlock().getType(), location.clone().add(0, -2, 0).getBlock().getData());
    }

    @Override
    public String getEventInternalName() {
        return "ham";
    }

    @Override
    public String getEventName() {
        return "天坑外卖";
    }

    @Override
    public int requireOnline() {
        return NewConfiguration.INSTANCE.getEventOnlineRequired().get(getEventInternalName());
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.SKULL) {
                final Skull skull = (Skull) event.getClickedBlock().getState();
                if (skull.getSkullType() == SkullType.PLAYER) {
                    if (location.distance(event.getClickedBlock().getLocation()) <= 3) {
                        final Player player = event.getPlayer();
                        InventoryUtil.removeItemWithInternalName(player, "ham");

                        for (int i = 0; i < 6; i++) {
                            player.getInventory().addItem(new ItemBuilder(pizza)
                                    .dontStack()
                                    .build());
                        }

                        final PizzaData playerData = pizzaDataMap.get(player.getUniqueId());
                        playerData.hamburger = 6;

                        player.sendMessage(CC.translate("&6&l汉堡! &7你已领取汉堡,请将其送至周围的村民手上以获取现金."));
                        player.sendMessage(CC.translate("&6&l汉堡! &7将获取的现金交给此处的村民以完成订单."));
                    }
                }
            }
        }
    }

    @Override
    public void onActive() {
        this.timer = new Cooldown(5, TimeUnit.MINUTES);

        this.runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (timer.hasExpired()) {
                    cancel();
                    if (HamburgerEvent.this.equals(ThePit.getInstance().getEventFactory().getActiveEpicEvent())) {
                        ThePit.getInstance()
                                .getEventFactory()
                                .inactiveEvent(HamburgerEvent.this);
                    }
                }

                Bukkit.getOnlinePlayers().forEach(player -> {
                    PlayerProfile.getPlayerProfileByUuid(player.getUniqueId())
                            .setMoveSpeed(0.2F * 1.6F);
                });
            }
        };
        this.runnable.runTaskTimer(ThePit.getInstance(), 20, 20);

        Bukkit.getPluginManager().registerEvents(this, ThePit.getInstance());

        Bukkit.getOnlinePlayers().forEach(player -> {
            this.pizzaDataMap.put(player.getUniqueId(), new PizzaData(player.getUniqueId(), player.getDisplayName()));
        });


        BukkitWorld world = new BukkitWorld(Bukkit.getWorlds().get(0));
        this.location = ThePit.getInstance().getPitConfig().getHamburgerOfferNpcLocA(); //villager location

        Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
            final List<Player> players = PlayerUtil.getNearbyPlayers(location, 5);
            final List<Location> locations = ThePit.getInstance().getPitConfig().getSpawnLocations();

            for (Player player : players) {
                player.teleport(locations.get(RandomUtil.random.nextInt(locations.size())));
                player.sendMessage(CC.translate("&c为了保证您的安全，我们已将您传送回出生点"));
            }

            final ArrayList<PlacedBlockData> data = new ArrayList<>(ClearRunnable.getClearRunnable().getPlacedBlock());
            for (PlacedBlockData blockData : data) {
                if (blockData.getLocation().distance(location) <= 5) {
                    ClearRunnable.getClearRunnable().getPlacedBlock().remove(blockData);
                    blockData.getLocation().getBlock().setType(Material.AIR);
                }
            }
        });

        FaweAPI.getTaskManager().async(() -> {
            try {
                final InputStream inputStream = ThePit.getInstance().getClass().getClassLoader().getResourceAsStream("hamburger.schematic");

                BlockVector vector = new BlockVector(location.getX(), location.getY(), location.getZ());

                session = FaweAPI.getEditSessionBuilder(world).build();

                final MCEditSchematicFormat mcedit = (MCEditSchematicFormat) SchematicFormat.MCEDIT;
                
                final CuboidClipboard clipboard = mcedit.load(inputStream);


                clipboard.paste(session, vector, false);

                session.flushQueue();

                Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                    try {
                        this.villager = (Villager) Bukkit.getWorlds().get(0).spawnEntity(location, EntityType.VILLAGER);
                        final EntityVillager entityVillager = ((CraftVillager) villager).getHandle();
                        NBTTagCompound tag = entityVillager.getNBTTag();
                        if (tag == null) {
                            tag = new NBTTagCompound();
                        }
                        entityVillager.c(tag);
                        tag.setInt("NoAI", 1);
                        entityVillager.f(tag);
                        villager.setAdult();

                        hologram = HologramAPI.createHologram(location.clone().add(0, 2.4, 0), CC.translate("&d请给我钱"));
                        hologram.spawn();
                        hologram.setAttachedTo(villager);
                    } catch (Exception e) {
                        Bukkit.getOnlinePlayers()
                                .forEach(player -> {
                                    CC.printError(player, e);
                                });
                    }
                });
            } catch (Exception e) {
                Bukkit.getOnlinePlayers()
                        .forEach(player -> {
                            CC.printError(player, e);
                        });
            }
        });


        for (int i = 0; i < 20; i++) {
            this.spawnVillager();
        }
    }

    @EventHandler
    public void onKill(PitKillEvent event) {
        final PizzaData data = this.pizzaDataMap.get(event.getKiller().getUniqueId());
        if (data == null) {
            return;
        }
        final PizzaData targetData = this.pizzaDataMap.get(event.getTarget().getUniqueId());
        if (targetData == null) {
            return;
        }

        data.money += targetData.money;
        targetData.money = 0;

        data.hamburger += targetData.hamburger;
        for (int i = 0; i < targetData.hamburger; i++) {
            event.getKiller().getInventory().addItem(new ItemBuilder(pizza)
                    .build());
        }

        if (targetData.hamburger > 0 || targetData.money > 0) {
            CC.send(MessageType.EVENT, event.getKiller(), "&6&l天坑外卖! &7你通过击杀一名玩家夺取了 &6" + targetData.money + "$ &7.");
        }

        targetData.hamburger = 0;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.SKULL) {
            event.setCancelled(true);
        }
        if (event.getBlockPlaced().getLocation().distance(location) < 8) {
            ClearRunnable.getClearRunnable().placeBlock(event.getBlock().getLocation(), new Cooldown(8, TimeUnit.SECONDS));
        }
    }

    @Override
    public void onInactive() {
        end = true;
        HandlerList.unregisterAll(this);
        this.runnable.cancel();

        hologram.deSpawn();
        villager.remove();

        FaweAPI.getTaskManager().async(() -> {
            session.undo(session);
            session.flushQueue();
        });

        for (Map.Entry<UUID, VillagerData> entry : this.villagerDataMap.entrySet()) {
            entry.getValue().remove(this);
        }

        final List<PizzaData> list = pizzaDataMap.values()
                .stream()
                .sorted(Comparator.comparingInt(value -> {
                    final PizzaData data = (PizzaData) value;
                    return data.paidMoney;
                }).reversed())
                .collect(Collectors.toList());

        Map<UUID, Integer> rankMap = new HashMap<>();
        int rankNumber = 0;
        for (PizzaData data : list) {
            rankMap.put(data.uuid, rankNumber);
            rankNumber++;
        }

        CC.boardCast(CC.CHAT_BAR);
        CC.boardCast("&6&l天坑事件结束: " + this.getEventName() + "&6&l!");

        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryUtil.removeItemWithInternalName(player, "ham");

            final int rank = rankMap.get(player.getUniqueId()) + 1;
            final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            final PizzaData data = pizzaDataMap.get(player.getUniqueId());

            profile.setMoveSpeed(0.2F);

            double rewardCoins = 0;
            int rewardRenown = 0;
            if (rank <= 3) {
                rewardCoins += 2000;
                rewardRenown += 2;
            } else if (rank <= 20) {
                rewardCoins += 500;
                rewardRenown += 1;
            } else {
                rewardCoins += 100;
            }
            if (done >= 600) {
                rewardCoins = 2 * rewardCoins;
            }
            if (ThePit.getInstance().getPitConfig().isGenesisEnable() && profile.getGenesisData().getTier() >= 5 && rewardRenown > 0) {
                rewardRenown++;
            }
            int enchantBoostLevel = Utils.getEnchantLevel(player.getInventory().getLeggings(), "Paparazzi");
            if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) {
                enchantBoostLevel = 0;
            }
            if (enchantBoostLevel > 0) {
                rewardCoins += 0.5 * enchantBoostLevel * rewardCoins;
                rewardRenown += Math.floor(0.5 * enchantBoostLevel * rewardRenown);
                MythicLeggingsItem mythicLeggings = new MythicLeggingsItem();
                mythicLeggings.loadFromItemStack(player.getInventory().getLeggings());
                if (mythicLeggings.isEnchanted()) {
                    if (mythicLeggings.getMaxLive() > 0 && mythicLeggings.getLive() <= 2) {
                        player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    } else {
                        mythicLeggings.setLive(mythicLeggings.getLive() - 2);
                        player.getInventory().setLeggings(mythicLeggings.toItemStack());
                    }
                }
            }
            if (PlayerUtil.isPlayerUnlockedPerk(player, "self_confidence")) {
                if (rank <= 5) {
                    rewardCoins += 5000;
                } else if (rank <= 10) {
                    rewardCoins += 2500;
                } else if (rank <= 15) {
                    rewardCoins += 1000;
                }
            }
            profile.grindCoins(rewardCoins);
            profile.setCoins(profile.getCoins() + rewardCoins);
            profile.setRenown(profile.getRenown() + rewardRenown);
            profile.kingsQuestsData.checkUpdate();
            if (profile.kingsQuestsData.getAccepted()) {
                if (!profile.kingsQuestsData.getCompleted()) {
                    profile.kingsQuestsData.setCollectedRenown(profile.kingsQuestsData.getCollectedRenown() + rewardRenown);
                }
            }

            if (data.getHamOrdered() >= 35) {
                new PizzaEventMedal().addProgress(profile, 1);
            }

            player.sendMessage(CC.translate("&6你的奖励: &6+" + rewardCoins + "硬币 &e+" + rewardRenown + "声望"));
            player.sendMessage(CC.translate("&6&l你: &7完成了总计 &6" + data.paidMoney + "$ &7的订单 (排名#" + rank + ")"));
            if (done >= 500) {
                player.sendMessage(CC.translate("&6&l全局奖励: &a&l成功! &7所有人获得的金币翻倍!"));
            } else {
                player.sendMessage(CC.translate("&6&l全局奖励: &c&l失败! &7所有人累计完成了 &c" + done + "&7/500 &7份订单."));
            }
            if (list.size() >= 3) {
                player.sendMessage(CC.translate("&6顶级玩家: "));
                for (int i = 0; i < 3; i++) {
                    Player top = Bukkit.getPlayer(list.get(i).getUuid());
                    if (top != null && top.isOnline()) {
                        PlayerProfile topProfile = PlayerProfile.getPlayerProfileByUuid(top.getUniqueId());

                        int d = list.get(i).paidMoney;
                        player.sendMessage(CC.translate(" &e&l#" + (i + 1) + " " + topProfile.getFormattedName() + " &e订单业绩 &6" + d + "&6$"));
                    }
                }
            }
        }
        CC.boardCast(CC.CHAT_BAR);


    }

    private void spawnVillager() {
        final List<Location> configLoc = ThePit.getInstance().getPitConfig().getHamburgerNpcLocA();
        final List<Location> locations = new ArrayList<>(configLoc);
        locations.removeAll(spawnedLocations);
        final Location location = locations.get(RandomUtil.random.nextInt(locations.size()));
        spawnedLocations.add(location);
        if (location.getBlock().getType() != Material.AIR) {
            location.getBlock().setType(Material.AIR);
        }
        final Block upBlock = location.clone().add(0, 1, 0).getBlock();
        if (upBlock.getType() != Material.AIR) {
            upBlock.setType(Material.AIR);
        }

//        for (Player player : Bukkit.getOnlinePlayers()) {
//            createBeacon(player, location);
//        }

        try {
            final World world = Bukkit.getWorlds().get(0);
            final Villager villager = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
            final EntityVillager entityVillager = ((CraftVillager) villager).getHandle();
            NBTTagCompound tag = entityVillager.getNBTTag();
            if (tag == null) {
                tag = new NBTTagCompound();
            }
            entityVillager.c(tag);
            tag.setInt("NoAI", 1);
            tag.setInt("Silent", 1);
            entityVillager.f(tag);
            villager.setAdult();


            this.villagerDataMap.put(villager.getUniqueId(), new VillagerData(villager.getUniqueId(), villager));
        } catch (Exception e) {
            Bukkit.getOnlinePlayers()
                    .forEach(player -> {
                        CC.printError(player, e);
                    });
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PitProfileLoadedEvent event) {
        final Player player = Bukkit.getPlayer(event.getPlayerProfile().getPlayerUuid());
        if (this.pizzaDataMap.containsKey(player.getUniqueId())) {
            this.pizzaDataMap.get(player.getUniqueId()).hamburger = 0;
            return;
        }
        this.pizzaDataMap.put(player.getUniqueId(), new PizzaData(player.getUniqueId(), player.getDisplayName()));
    }

    @Override
    public List<String> insert(Player player) {
        List<String> lines = new ArrayList<>();
        final PizzaData data = pizzaDataMap.get(player.getUniqueId());
        if (data == null) {
            return lines;
        }
        lines.add("&f活动结束: &e" + TimeUtil.millisToTimer(timer.getRemaining()));
        lines.add("&f现金: &6" + data.money + "$");
        lines.add("&f已交付: &a" + data.paidMoney + "$");

        return lines;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Villager) {
            final Villager clicked = (Villager) event.getRightClicked();
            final VillagerData villager = villagerDataMap.get(clicked.getUniqueId());
            if (villager != null) {
                event.setCancelled(true);

                final Player player = event.getPlayer();

                if (villager.getState() == VillagerState.DONE) {
                    CC.send(MessageType.EVENT, player, "&c这个村民已经拿到了他的外卖!");
                    return;
                }

                if ("ham".equals(ItemUtil.getInternalName(player.getItemInHand()))) {
                    PlayerUtil.takeOneItemInHand(player);
                    villager.changeState(this, player);

                    final PizzaData playerData = pizzaDataMap.get(player.getUniqueId());
                    playerData.money += villager.coins;
                    playerData.hamburger--;
                    playerData.hamOrdered++;

                    for (Player target : Bukkit.getOnlinePlayers()) {
                        redo(target, villager.getLocation());
                    }

                    done++;
                }
            } else if (clicked.getUniqueId().equals(clicked.getUniqueId())) {
                event.setCancelled(true);

                final Player player = event.getPlayer();
                final PizzaData data = pizzaDataMap.get(player.getUniqueId());

                if (data.money == 0) {
                    return;
                }

                data.paidMoney += data.money;

                CC.send(MessageType.EVENT, player, "&a&l订单交付! &7你已将手持价值 &6" + data.money + "$ &7的订单成功交付!");

                data.money = 0;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Villager) {
            if (villagerDataMap.containsKey(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
            if (villager.getUniqueId().equals(event.getEntity().getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    private enum VillagerState {
        WAITING,
        DONE
    }

    @Data
    @RequiredArgsConstructor
    public static class PizzaData {
        private final UUID uuid;
        private final String name;

        private int hamburger = 0;
        private int money = 0;
        private int paidMoney = 0;
        private int hamOrdered = 0;
    }

    @Data
    private static class VillagerData {
        private final UUID uuid;
        private final Villager villager;
        private final Hologram hologram;
        private VillagerState state;
        private int coins;
        private Location location;

        public VillagerData(UUID uuid, Villager villager) {
            this.uuid = uuid;
            this.villager = villager;
            this.location = villager.getLocation();

            final String name = names.get(RandomUtil.random.nextInt(names.size()));

            coins = (int) RandomUtil.helpMeToChooseOne(5, 10, 15, 20);

            this.hologram = HologramAPI.createHologram(villager.getLocation().clone().add(0, 2.4, 0), CC.translate("&c&l" + name));
            this.hologram.spawn();
            this.hologram.setAttachedTo(villager);
            this.hologram.addLineAbove(CC.translate("&7配送奖励: &6" + coins + "$"));

            this.state = VillagerState.WAITING;
        }

        public void changeState(HamburgerEvent event, Player player) {
            this.state = VillagerState.DONE;
            this.hologram.removeLineAbove();
            final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            this.hologram.setText(CC.translate("&a&l谢谢你! " + profile.getFormattedName()));
            Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), () -> {
                remove(event);
                if (event.end) {
                    return;
                }
                event.spawnVillager();
            }, 20 * 3L);
        }

        public void remove(HamburgerEvent event) {
            Bukkit.getScheduler().runTask(ThePit.getInstance(), () -> {
                try {
                    if (this.villager != null && !this.villager.isDead()) {
                        this.villager.remove();

                        for (Player target : Bukkit.getOnlinePlayers()) {
                            redo(target, villager.getLocation());
                        }
                    }

                    if (this.hologram != null && this.hologram.isSpawned()) {
                        final Hologram above = this.hologram.getLineAbove();
                        if (above != null && above.isSpawned()) {
                            above.deSpawn();
                        }
                        this.hologram.deSpawn();
                    }
                    event.villagerDataMap.remove(this.uuid);
                    event.spawnedLocations.remove(this.location);
                } catch (Exception e) {
                    Bukkit.getOnlinePlayers()
                            .forEach(player -> {
                                CC.printError(player, e);
                            });
                }
            });
        }
    }

}
