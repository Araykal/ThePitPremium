package cn.charlotte.pit.item.type;

import cn.charlotte.pit.buff.impl.BountySolventBuff;
import cn.charlotte.pit.item.AbstractPitItem;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/28 18:39
 */
@AutoRegister
public class BountySolventPotion extends AbstractPitItem implements Listener {

    private static final BountySolventBuff buff = new BountySolventBuff();

    @Override
    public String getInternalName() {
        return "bounty_solvent_potion";
    }

    @Override
    public String getItemDisplayName() {
        return "&6赏金溶剂 (01:00)";
    }

    @Override
    public Material getItemDisplayMaterial() {
        return Material.POTION;
    }

    @Override
    public ItemStack toItemStack() {
        return new ItemBuilder(getItemDisplayMaterial())
                .name(getItemDisplayName())
                .lore(
                        "&7受到来自被悬赏 &6&l1000g &7以上的玩家攻击",
                        "&7受到的伤害 &9-30%",
                        "",
                        "&7获得赏金时赏金的获取量 &6+50%"
                )
                .internalName(getInternalName())
                .canSaveToEnderChest(true)
                .deathDrop(true)
                .shiny()
                .build();
    }

    @Override
    public void loadFromItemStack(ItemStack item) {

    }

    @EventHandler
    public void onPotionDrank(PlayerItemConsumeEvent event) {
        if (ItemUtil.getInternalName(event.getItem()) != null && ItemUtil.getInternalName(event.getItem()).equalsIgnoreCase("bounty_solvent_potion")) {
            event.setCancelled(true);
            PlayerUtil.takeOneItemInHand(event.getPlayer());
            buff.stackBuff(event.getPlayer(), 60 * 1000L);
        }
    }
}
