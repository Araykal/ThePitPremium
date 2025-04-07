package cn.charlotte.pit.enchantment.type.rare

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.IActionDisplayEnchant
import cn.charlotte.pit.enchantment.param.event.PlayerOnly
import cn.charlotte.pit.enchantment.param.item.WeaponOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.enchantment.type.ragerare.ThinkOfThePeopleEnchant
import cn.charlotte.pit.item.type.mythic.MythicBowItem
import cn.charlotte.pit.item.type.mythic.MythicSwordItem
import cn.charlotte.pit.parm.listener.IAttackEntity
import cn.charlotte.pit.parm.listener.IPlayerShootEntity
import cn.charlotte.pit.parm.type.BowOnly
import cn.charlotte.pit.util.PlayerUtil
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.chat.MessageType
import cn.charlotte.pit.util.cooldown.Cooldown
import com.google.common.util.concurrent.AtomicDouble
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/17 18:35
 */
@WeaponOnly
class ComboStrikeEnchant : AbstractEnchantment(), IAttackEntity, IPlayerShootEntity,
    IActionDisplayEnchant {
    private val numFormat = DecimalFormat("0.0")
    override fun getEnchantName(): String {
        return "强力击: 闪电"
    }

    override fun getMaxEnchantLevel(): Int {
        return 3
    }

    override fun getNbtName(): String {
        return "lightning_strike"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    override fun getCooldown(): Cooldown? {
        return Cooldown(3, TimeUnit.SECONDS)
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return ("&7每 &e" + (if (enchantLevel > 1) 4 else 5) + " &7次击中目标额外召唤一道闪电攻击你的敌人,/s&7造成 &c"
                + numFormat.format(getDamage(enchantLevel))
                + "❤ &b雷电&f(真实)&7伤害"
                + if (enchantLevel >= 3) "&7,且闪电攻击目标/s每穿着一件&b钻石装备&7,闪电额外造成 &c1❤ &b雷电&f(真实)&7伤害" else "")
    }

    @PlayerOnly
    override fun handleAttackEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        val victim = target as Player
        if (attacker.itemInHand.type != MythicSwordItem().itemDisplayMaterial) {
            return
        }
        for (nearbyPlayers in PlayerUtil.getNearbyPlayers(victim.location, 10.0)) {
            if (nearbyPlayers.inventory.leggings != null && thinkOfThePeople.isItemHasEnchant(nearbyPlayers.inventory.leggings)) {
                val level = thinkOfThePeople.getItemEnchantLevel(nearbyPlayers.inventory.leggings)
                if (level > 1) {
                    boostDamage.getAndAdd(level * -0.1 + 0.1)
                }
                return
            }
        }
        if (PlayerProfile.getPlayerProfileByUuid(attacker.uniqueId).meleeHit % (if (enchantLevel > 1) 4 else 5) == 0) {
            finalDamage.set(finalDamage.get() + 2 * getDamage(enchantLevel))
            PlayerUtil.playThunderEffect(target.getLocation())
            val victimProfile = PlayerProfile.getPlayerProfileByUuid(victim.uniqueId)
            CC.send(MessageType.MISC, attacker, "&b&l闪电! &7你的闪电击中了 " + victimProfile.formattedName + " &7!")
            if (enchantLevel >= 3) {
                var extra = 0.0
                for (armorContent in victim.inventory.armorContents) {
                    if (armorContent != null && armorContent.type.name.contains("DIAMOND")) {
                        extra++
                    }
                }
                finalDamage.set(finalDamage.get() + extra * 2)
            }
        }
    }

    @PlayerOnly
    @BowOnly
    override fun handleShootEntity(
        enchantLevel: Int,
        attacker: Player,
        target: Entity,
        damage: Double,
        finalDamage: AtomicDouble,
        boostDamage: AtomicDouble,
        cancel: AtomicBoolean
    ) {
        val victim = target as Player
        if (attacker.itemInHand.type != MythicBowItem().itemDisplayMaterial) {
            return
        }
        for (nearbyPlayers in PlayerUtil.getNearbyPlayers(victim.location, 10.0)) {
            if (nearbyPlayers.inventory.leggings != null && thinkOfThePeople.isItemHasEnchant(nearbyPlayers.inventory.leggings)) {
                val level = thinkOfThePeople.getItemEnchantLevel(nearbyPlayers.inventory.leggings)
                if (level > 1) {
                    boostDamage.getAndAdd(level * -0.1 + 0.1)
                }
                return
            }
        }
        if (PlayerProfile.getPlayerProfileByUuid(attacker.uniqueId).meleeHit % (if (enchantLevel > 1) 4 else 5) == 0) {
            finalDamage.set(finalDamage.get() + 2 * getDamage(enchantLevel))
            PlayerUtil.playThunderEffect(target.getLocation())
            val victimProfile = PlayerProfile.getPlayerProfileByUuid(victim.uniqueId)
            CC.send(MessageType.MISC, attacker, "&b&l闪电! &7你的闪电击中了 " + victimProfile.formattedName + " &7!")
            if (enchantLevel >= 3) {
                var extra = 0.0
                for (armorContent in victim.inventory.armorContents) {
                    if (armorContent != null && armorContent.type.name.contains("DIAMOND")) {
                        extra++
                    }
                }
                finalDamage.set(finalDamage.get() + extra * 2)
            }
        }
    }

    override fun getText(level: Int, player: Player): String {
        val hit =
            if (player.itemInHand != null && player.itemInHand.type == Material.BOW) PlayerProfile.getPlayerProfileByUuid(
                player.uniqueId
            ).bowHit else PlayerProfile.getPlayerProfileByUuid(player.uniqueId).meleeHit
        return if (hit % (if (level > 1) 4 else 5) == 0) "&a&l✔" else "&e&l" + ((if (level > 1) 4 else 5) - hit % if (level > 1) 4 else 5)
    }

    companion object {
        private val thinkOfThePeople = ThinkOfThePeopleEnchant()
        fun getDamage(enchantLevel: Int): Double {
            return when (enchantLevel) {
                1 -> 1.5
                2 -> 2.0
                3 -> 1.0
                else -> 0.0
            }
        }
    }
}