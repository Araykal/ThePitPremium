package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.ItemBow;
import net.minecraft.server.v1_8_R3.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/7 23:42
 */
@AutoRegister
@BowOnly
public class MegaLongBowEnchant extends AbstractEnchantment implements Listener, IActionDisplayEnchant {

    private static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "巨型长弓";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "mega_long_bow_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "射箭时无需蓄力即可让箭矢以最大蓄力状态射出,"
                + "/s&7同时为自身施加 &a跳跃提升 " + RomanUtil.convert(enchantLevel + 1) + " &f(00:02)"
                + "/s&7此附魔每秒只能触发一次.";
    }


    @EventHandler
    public void onInteract(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getForce() >= 1) return;
        final Player player = (Player) event.getEntity();
        if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
        final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        final int level = this.getItemEnchantLevel(itemInHand);
        if (level == -1) {
            return;
        }
        if (player.isSneaking()) {
            return;
        }
        if (itemInHand.getType() == Material.BOW) {
            if (cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired()) {
                cooldown.put(player.getUniqueId(), new Cooldown(1, TimeUnit.SECONDS));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2 * 20, level), false);
                event.setCancelled(true);
                final EntityPlayer ePlayer = ((CraftPlayer) player).getHandle();
                final ItemStack itemStack = CraftItemStack.asNMSCopy(itemInHand);
                final ItemBow bow = (ItemBow) itemStack.getItem();
                bow.a(itemStack, ePlayer.world, ePlayer, 0);
            }
        }
    }

    @Override
    public String getText(int level, Player player) {
        return cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() ? "&a&l✔" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.get(player.getUniqueId()).getRemaining()).replace(" ", "");
    }
}
