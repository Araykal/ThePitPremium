package cn.charlotte.pit.enchantment.type.normal;

import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.IAttackEntity;
import cn.charlotte.pit.parm.listener.IPlayerShootEntity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.item.ItemUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

@ArmorOnly
public class KingKillersEnchant extends AbstractEnchantment implements IAttackEntity, IPlayerShootEntity {
    @Override
    public String getEnchantName() {
        return "国王杀手";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "kingkiller_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NORMAL;
    }

    @Nullable
    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return (new StringBuilder()).insert(0, "&7对穿戴 &6国王的王冠 &7的玩家额外造成 &c").append(enchantLevel * 0.5D).append("❤ &7的伤害.").toString();
    }
    @PlayerOnly
    @Override
    public void handleAttackEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player player = (Player)target;
        if (player.getInventory().getHelmet() != null && "kings_helmet".equals(ItemUtil.getInternalName(player.getInventory().getHelmet())))
            boostDamage.getAndAdd(enchantLevel * 0.1D);
    }
    @PlayerOnly
    @Override
    public void handleShootEntity(int enchantLevel, Player attacker, Entity target, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        Player player = (Player)target;
        if (player.getInventory().getHelmet() != null && "kings_helmet".equals(ItemUtil.getInternalName(player.getInventory().getHelmet())))
            boostDamage.getAndAdd(enchantLevel * 0.1D);
    }
}
