package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Misoryan
 * @Created_In: 2020/12/30 13:56
 */
@ArmorOnly
public class BooBooEnchant extends AbstractEnchantment implements ITickTask, IActionDisplayEnchant {

    public static final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "Boo-boo";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "encore";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NORMAL;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        int i = 6 - enchantLevel;
        return "&7装备时每 &a" + i + " &7秒额外回复 &c1❤ &7生命值";
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        if (player.getHealth() == player.getMaxHealth()) {
            return;
        }
        if (cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() && !PlayerUtil.isVenom(player) && !PlayerUtil.isVenom(player)) {
            cooldown.put(player.getUniqueId(), new Cooldown(6 - enchantLevel, TimeUnit.SECONDS));
            PlayerUtil.heal(player, 2);
        }
    }


    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }

    @Override
    public String getText(int level, Player player) {
        return getCooldownActionText(cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)));
    }
}
