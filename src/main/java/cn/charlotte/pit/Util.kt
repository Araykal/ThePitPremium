package cn.charlotte.pit

import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.util.PlayerUtil
import net.minecraft.server.v1_8_R3.ItemStack
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers
import org.bukkit.entity.Player


fun Player.getPitProfile(): PlayerProfile {
    return PlayerProfile.getPlayerProfileByUuid(this.uniqueId)
}

fun Player.releaseItem() {
    val craftPlayer = this as CraftPlayer
    craftPlayer.handle.bU()
}

fun Player.dead() {
    PlayerUtil.damage(player, PlayerUtil.DamageType.TRUE, player.maxHealth * 100, false)
}

fun Player.hasRealMan(): Boolean {
    return (player.getMetadata("real_man").firstOrNull()?.asLong() ?: Long.MIN_VALUE) >= System.currentTimeMillis()
}

private val nmsItemField = CraftItemStack::class.java.getDeclaredField("handle")
    .also {
        it.isAccessible = true
    }

fun org.bukkit.inventory.ItemStack?.reflectGetNmsItem(): ItemStack? {
    this ?: return null

    return if (this is CraftItemStack) {
        nmsItemField.get(this) as ItemStack
    } else {
        createNmsItemInstance(this)
    }
}

private fun createNmsItemInstance(bukkitItem: org.bukkit.inventory.ItemStack): ItemStack? {
    val item = CraftMagicNumbers.getItem(bukkitItem.type) ?: return null
    return run {
        val stack = ItemStack(item, bukkitItem.amount, bukkitItem.durability.toInt())
        if (bukkitItem.hasItemMeta()) {
            CraftItemStack.setItemMeta(stack, bukkitItem.itemMeta)
        }
        stack
    }
}