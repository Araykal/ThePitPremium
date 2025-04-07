package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.IActionDisplayEnchant;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.chat.MessageType;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/30 13:46
 */
@ArmorOnly
public class HealShieldEnchant extends AbstractEnchantment implements IPlayerDamaged, ITickTask, IActionDisplayEnchant {

    private final HashMap<UUID, Integer> shield = new HashMap<>();
    private final HashMap<UUID, Cooldown> cooldown = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "沃土予身";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "heal_shield";
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
        return "&7每 &e" + (30 - enchantLevel * 5) + " &7秒获得一层护盾 (可以抵消1次玩家伤害)"
                + "/s&7(最高3层) , 每层护盾破裂恢复自身 &c20% &7最大生命值";
    }

    @Override
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        shield.putIfAbsent(myself.getUniqueId(), 0);
        if (shield.get(myself.getUniqueId()) > 0) {
            shield.put(myself.getUniqueId(), shield.get(myself.getUniqueId()) - 1);
            cancel.set(true);
            myself.sendMessage(CC.translate("&c你的一层护盾因受到攻击而破裂! 剩余层数: " + shield.get(myself.getUniqueId())));
            attacker.sendMessage(CC.translate("&c对方的一层护盾抵消了你的攻击而破裂!"));
            ((Player) attacker).playSound(attacker.getLocation(), Sound.ANVIL_LAND, 1, 1);
            myself.playSound(attacker.getLocation(), Sound.ANVIL_LAND, 1, 1);
            PlayerUtil.heal(myself, 0.2 * myself.getMaxHealth());
        }
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
        shield.putIfAbsent(player.getUniqueId(), 0);
        cooldown.putIfAbsent(player.getUniqueId(), new Cooldown(30 - enchantLevel * 5L, TimeUnit.SECONDS));
        if (shield.get(player.getUniqueId()) < 3) {
            if (cooldown.get(player.getUniqueId()).hasExpired()) {
                cooldown.put(player.getUniqueId(), new Cooldown(30 - enchantLevel * 5L, TimeUnit.SECONDS));
                shield.put(player.getUniqueId(), shield.get(player.getUniqueId()) + 1);
                CC.send(MessageType.MISC, player, "&a你恢复了一层护盾,当前护盾层数: " + shield.get(player.getUniqueId()) + "/3");
            }
        } else {
            cooldown.put(player.getUniqueId(), new Cooldown(0));
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }

    @Override
    public String getText(int level, Player player) {
        shield.putIfAbsent(player.getUniqueId(), 0);
        cooldown.putIfAbsent(player.getUniqueId(), new Cooldown(15, TimeUnit.SECONDS));
        String duration = cooldown.getOrDefault(player.getUniqueId(), new Cooldown(0)).hasExpired() ? "" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.get(player.getUniqueId()).getRemaining()).replace(" ", "");
        return (shield.get(player.getUniqueId()) < 3 ? "&e&l" : "&a&l") + shield.get(player.getUniqueId()) + "/3 " + duration;
    }
}
