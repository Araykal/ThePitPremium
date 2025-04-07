package cn.charlotte.pit.menu.admin.item;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.item.MythicColor;
import cn.charlotte.pit.item.type.*;
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem;
import cn.charlotte.pit.map.kingsquests.item.Cherry;
import cn.charlotte.pit.map.kingsquests.item.MiniCake;
import cn.charlotte.pit.menu.admin.item.button.MythicItemButton;
import cn.charlotte.pit.menu.admin.item.button.PitItemButton;
import cn.charlotte.pit.menu.admin.item.button.RedPacketButton;
import cn.charlotte.pit.menu.admin.item.button.ShopItemButton;
import cn.charlotte.pit.menu.shop.button.type.CombatSpadeShopButton;
import cn.charlotte.pit.menu.shop.button.type.GoldPickaxeShopButton;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.menu.Button;
import cn.charlotte.pit.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/17 22:41
 */
public class AdminItemMenu extends Menu {
    private final boolean admin;

    public AdminItemMenu() {
        this.admin = true;
    }

    public AdminItemMenu(boolean isAdmin) {
        this.admin = isAdmin;
    }

    @Override
    public String getTitle(Player player) {
        return "Pit物品";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttonMap = new HashMap<>();
        try {
            buttonMap.put(0, new MythicItemButton(0));
            buttonMap.put(1, new MythicItemButton(1));
            buttonMap.put(2, new MythicItemButton(2));
            buttonMap.put(3, new MythicItemButton(3));
            buttonMap.put(4, new ShopItemButton(Material.DIAMOND_HELMET, "shopItem"));
            buttonMap.put(5, new ShopItemButton(Material.DIAMOND_CHESTPLATE, "shopItem"));
            buttonMap.put(6, new ShopItemButton(Material.DIAMOND_LEGGINGS, "shopItem"));
            buttonMap.put(7, new ShopItemButton(Material.DIAMOND_BOOTS, "shopItem"));
            buttonMap.put(8, new ShopItemButton(Material.DIAMOND_SWORD, "shopItem"));
            buttonMap.put(9, new ShopItemButton(Material.OBSIDIAN, "shopItem", 64));

            buttonMap.put(10, new MythicItemButton(8));
            buttonMap.put(11, new GoldPickaxeShopButton());
            buttonMap.put(12, new CombatSpadeShopButton());
            buttonMap.put(13, new MythicItemButton(4));

            if (ThePit.getInstance().getPitConfig().getCode().equals("4847a648-bd9f-463e-ab18-3006b0fabd3b")) {
                buttonMap.put(14, new MythicItemButton(9));
            }

            if (admin) {
                buttonMap.put(9, new ShopItemButton(Material.TNT, "tnt", 64));
                buttonMap.put(16, new PitItemButton(JumpBoostPotion.toItemStack()));
                buttonMap.put(17, new PitItemButton(ThePit.getApi().getMythicItemItemStack("bounty_solvent_potion")));
                buttonMap.put(18, new PitItemButton(FourInARowGadget.toItemStack()));
                buttonMap.put(19, new PitItemButton(ChunkOfVileItem.toItemStack()));
                buttonMap.put(20, new RedPacketButton());
                buttonMap.put(21, new PitItemButton(FunkyFeather.toItemStack()));
                buttonMap.put(22, new PitItemButton(PitCactus.toItemStack()));
                buttonMap.put(23, new PitItemButton(MythicRepairKit.toItemStack0()));
                buttonMap.put(24, new PitItemButton(new JewelSword().toItemStack()));
                buttonMap.put(25, new PitItemButton(new ArmageddonBoots().toItemStack()));
                buttonMap.put(27, new PitItemButton(new TotallyLegitGem().toItemStack()));
                buttonMap.put(28, new MythicItemButton(5));
                buttonMap.put(29, new MythicItemButton(6));
                buttonMap.put(30, new PitItemButton(new GoldenHelmet().toItemStack()));
                final MythicLeggingsItem item = new MythicLeggingsItem();
                item.setColor(MythicColor.RAGE);
                buttonMap.put(31, new PitItemButton(item.toItemStack()));

                buttonMap.put(33, new PitItemButton(new UberDrop().toItemStack()));

                final MythicLeggingsItem aquaLeggings = new MythicLeggingsItem();
                aquaLeggings.setColor(MythicColor.AQUA);
                buttonMap.put(34, new PitItemButton(aquaLeggings.toItemStack()));

                buttonMap.put(35, new PitItemButton(Cherry.INSTANCE.toItemStack()));
                final MythicLeggingsItem darkGreenLeggings = new MythicLeggingsItem();
                buttonMap.put(36, new PitItemButton(MiniCake.INSTANCE.toItemStack()));


                darkGreenLeggings.setColor(MythicColor.DARK_GREEN);
                buttonMap.put(37, new PitItemButton(darkGreenLeggings.toItemStack()));

                MusicalRune musicalRune = new MusicalRune();
                buttonMap.put(39, new PitItemButton(musicalRune.toItemStack()));
                buttonMap.put(40, new PitItemButton(new DimensionalCactus().toItemStack()));
                buttonMap.put(41, new PitItemButton(new SacrosanctCactus().toItemStack()));

                if (ThePit.getInstance().getPitConfig().isBetaVersion()) {
                    buttonMap.put(43, new PitItemButton(new GlobalAttentionGem().toItemStack()));
                }
            }

        } catch (Exception e) {
            CC.printError(player, e);
        }


        return buttonMap;
    }

    @Override
    public void onClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() instanceof PlayerInventory) {
            final PlayerInventory inv = (PlayerInventory) event.getClickedInventory();
            inv.setItem(event.getSlot(), new ItemStack(Material.AIR));
        }
    }
}
