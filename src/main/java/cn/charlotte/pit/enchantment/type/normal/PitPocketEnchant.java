package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/7 18:00
 */
@WeaponOnly
public class PitPocketEnchant extends AbstractEnchantment implements IAttackEntity {

    private final Map<UUID, Cooldown> COOLDOWN = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "天坑钱包";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "pit_pocket_enchant";
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
        return "&7攻击玩家时偷取对方 &6" + (10 + 5 * enchantLevel) + " 硬币 &7(" + (30 - 5 * enchantLevel) + "秒冷却)";
    }

    @Override
    @PlayerOnly
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        COOLDOWN.putIfAbsent(attacker.getUniqueId(), new Cooldown(0));
        Player targetPlayer = (Player) target;
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId());
        PlayerProfile targetProfile = PlayerProfile.getPlayerProfileByUuid(targetPlayer.getUniqueId());
        if (COOLDOWN.get(attacker.getUniqueId()).hasExpired()) {
            COOLDOWN.put(attacker.getUniqueId(), new Cooldown(30 - 5L * enchantLevel, TimeUnit.SECONDS));
            targetProfile.setCoins(targetProfile.getCoins() - (10 + 5 * enchantLevel));
            profile.setCoins(profile.getCoins() + (10 + 5 * enchantLevel));
            attacker.sendMessage(CC.translate("&6&l偷取硬币! &7你从 " + targetProfile.getFormattedName() + " &7处偷取了 &6" + (10 + 5 * enchantLevel) + " 硬币 &7!"));
        }
    }

}
