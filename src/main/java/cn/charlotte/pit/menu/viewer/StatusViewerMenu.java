package cn.charlotte.pit.menu.viewer;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.sub.PerkData;
import cn.charlotte.pit.data.sub.PlayerInv;
import cn.charlotte.pit.menu.viewer.button.EnderChestViewerMenu;
import cn.charlotte.pit.menu.viewer.button.InventoryViewerMenu;
import cn.charlotte.pit.menu.viewer.button.PitPassiveStatusButton;
import cn.charlotte.pit.menu.viewer.button.PitStatusButton;
import cn.charlotte.pit.menu.viewer.button.admin.TradeDataViewerButton;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/12 15:53
 */
public class StatusViewerMenu extends Menu {
    private final PlayerProfile profile;

    private boolean adminVersion = false;

    public StatusViewerMenu(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public String getTitle(Player player) {
        return "玩家档案查看";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        if (!profile.isSupporter()) {
            profile.getPlayerOption().setInventoryVisibility(true);
            profile.getPlayerOption().setEnderChestVisibility(true);
            profile.getPlayerOption().setProfileVisibility(true);
        }
        PlayerInv inventory = profile.getInventory();
        Map<Integer, Button> button = new HashMap<>();

        //Armor contents
        button.put(0, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (profile.getPlayerOption().isInventoryVisibility()) {
                    return profile.getInventory().getHelmet() == null ? new ItemBuilder(Material.AIR).build() : profile.getInventory().getHelmet();
                }
                return new ItemBuilder(Material.AIR).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

            }
        });
        button.put(9, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (profile.getPlayerOption().isInventoryVisibility() || PlayerUtil.isStaff(player)) {
                    return profile.getInventory().getChestPiece() == null ? new ItemBuilder(Material.AIR).build() : profile.getInventory().getChestPiece();
                }
                return new ItemBuilder(Material.AIR).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

            }
        });
        button.put(18, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (profile.getPlayerOption().isInventoryVisibility() || PlayerUtil.isStaff(player)) {
                    return profile.getInventory().getLeggings() == null ? new ItemBuilder(Material.AIR).build() : profile.getInventory().getLeggings();
                }
                return new ItemBuilder(Material.AIR).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

            }
        });
        button.put(27, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (profile.getPlayerOption().isInventoryVisibility() || PlayerUtil.isStaff(player)) {
                    return profile.getInventory().getBoots() == null ? new ItemBuilder(Material.AIR).build() : profile.getInventory().getBoots();
                }
                return new ItemBuilder(Material.AIR).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

            }
        });

        //Contents
        for (int i = 0; i < 9; i++) {
            int finalI = i;
            button.put(36 + i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    if (profile.getPlayerOption().isInventoryVisibility() || PlayerUtil.isStaff(player)) {
                        return inventory.getContents()[finalI] == null ? new ItemBuilder(Material.AIR).build() : inventory.getContents()[finalI];
                    }
                    return new ItemBuilder(Material.AIR).build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

                }
            });
        }

        //Perk slot

        List<AbstractPerk> perks = ThePit.getInstance()
                .getPerkFactory()
                .getPerks();

        int perkSize = 3;
        PerkData data = profile.getUnlockedPerkMap().get("ExtractPerkSlot");
        if (data != null) {
            perkSize = 4;
        }
        if (PlayerUtil.isStaff(player)) {
            button.put(8, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return adminVersion ? new ItemBuilder(Material.WATCH)
                            .name("&a返回普通版")
                            .build() : new ItemBuilder(Material.COMPASS)
                            .name("&c切换管理员版")
                            .build();

                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                    adminVersion = !adminVersion;
                }

                @Override
                public boolean shouldUpdate(Player player, int slot, ClickType clickType) {
                    return true;
                }
            });
        }
        for (int i = 0; i < perkSize; i++) {
            int finalI = i;
            button.put((perkSize == 4 ? 13 : 14) + i, new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    if (!profile.getPlayerOption().isProfileVisibility() && !PlayerUtil.isStaff(player)) {
                        return new ItemBuilder(Material.BARRIER).name("&e天赋栏 #" + (finalI + 1)).lore("&c此玩家选择隐藏了档案信息.").amount(finalI + 1).build();
                    }
                    if (profile.getChosePerk().get(finalI + 1) == null) {
                        return new ItemBuilder(Material.DIAMOND_BLOCK).name("&e天赋栏 #" + (finalI + 1)).lore("&7此天赋栏没有选择天赋.").amount(finalI + 1).build();
                    }
                    for (AbstractPerk perk : perks) {
                        if (perk.getInternalPerkName().equalsIgnoreCase(profile.getChosePerk().get(finalI + 1).getPerkInternalName())) {
                            List<String> lores = new ArrayList<>();
                            lores.add("&7携带天赋: &a" + perk.getDisplayName());
                            lores.add(" ");
                            lores.addAll(perk.getDescription(player));
                            return perk.getIconWithNameAndLore("&e天赋栏 #" + (finalI + 1), lores, 0, finalI + 1);
                        }
                    }
                    return new ItemBuilder(Material.BARRIER).name("&a天赋栏 #" + (finalI + 1)).lore("&c无法加载此数据!").amount(finalI + 1).build();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

                }
            });
        }

        button.put(11, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (Bukkit.getPlayer(profile.getPlayerUuid()) != null && Bukkit.getPlayer(profile.getPlayerUuid()).isOnline())
                    if (profile.getLevel() >= 60 && PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getLevel() >= 60 && !profile.getPlayerUuid().equals(player.getUniqueId())) {
                        return new ItemBuilder(Material.GOLD_INGOT).name("&e与此玩家进行交易").lore("&7与这名玩家交易现金,物品等.").build();
                    }
                return new ItemBuilder(Material.AIR).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                Player target = Bukkit.getPlayer(profile.getPlayerUuid());
                if (target != null && target.isOnline()) {
                    player.chat("/trade " + target.getName());
                }
            }
        });

        button.put(20, new PitStatusButton(profile));
        button.put(22, adminVersion ? new TradeDataViewerButton(profile) : new PitPassiveStatusButton(profile));
        button.put(23, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (!profile.getPlayerOption().isProfileVisibility() && !PlayerUtil.isStaff(player)) {
                    return new ItemBuilder(Material.BLAZE_POWDER).name("&a连杀天赋").lore("&c此玩家选择隐藏了档案信息.").build();
                }
                List<String> lines = new ArrayList<>();
                if (profile.getChosePerk().get(5) != null) {
                    for (AbstractPerk perk : perks) {
                        if (profile.getChosePerk().get(5).getPerkInternalName().equals(perk.getInternalPerkName())) {
                            lines.add("&7超级连杀: &e" + perk.getDisplayName());
                        }
                    }
                }
                for (int i = 0; i < 2; i++) {
                    if (profile.getChosePerk().get(i + 5) != null) {
                        for (AbstractPerk perk : perks) {
                            if (profile.getChosePerk().get(i + 5).getPerkInternalName().equals(perk.getInternalPerkName())) {
                                lines.add("&7连杀: &e" + perk.getDisplayName());
                            }
                        }
                    }
                }
                return new ItemBuilder(Material.BLAZE_POWDER).name("&a连杀天赋").lore(lines).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

            }
        });
        button.put(24, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (!profile.getPlayerOption().isInventoryVisibility() && !PlayerUtil.isStaff(player)) {
                    return new ItemBuilder(Material.CHEST).name("&a背包").lore("&7查看这名玩家的背包.", " ", "&c此玩家选择隐藏了背包信息,你无法查看!").build();
                }
                return new ItemBuilder(Material.CHEST).name("&a背包").lore("&7查看这名玩家的背包.", " ", "&e点击查看!").build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                if (!profile.getPlayerOption().isInventoryVisibility() && !PlayerUtil.isStaff(player)) {
                    return;
                }
                new InventoryViewerMenu(profile).openMenu(player);
            }
        });
        button.put(25, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                if (!profile.getPlayerOption().isEnderChestVisibility() && !PlayerUtil.isStaff(player)) {
                    return new ItemBuilder(Material.ENDER_CHEST).name("&5末影箱").lore("&7查看这名玩家末影箱中的物品.", " ", "&c此玩家选择隐藏了末影箱信息,你无法查看!").build();
                }
                return new ItemBuilder(Material.ENDER_CHEST).name("&5末影箱").lore("&7查看这名玩家末影箱中的物品.", " ", "&e点击查看!").build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
                if (!profile.getPlayerOption().isEnderChestVisibility() && !PlayerUtil.isStaff(player)) {
                    return;
                }
                new EnderChestViewerMenu(profile).openMenu(player);
            }
        });
        return button;
    }
}
