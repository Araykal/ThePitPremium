package cn.charlotte.pit.util

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.lang.reflect.Modifier
import java.text.DecimalFormat
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

val decimalFormat = DecimalFormat("###,###,###,###,###")

/**
 * 获取背包中符合特定规则的物品的数量
 *
 * @return amount
 */
fun Inventory.countItem(matcher: (itemStack: ItemStack) -> Boolean): Int {
    var amount = 0
    contents.forEach { itemStack ->
        if (itemStack != null && itemStack.type != Material.AIR && matcher(itemStack)) {
            amount += itemStack.amount
        }
    }
    return amount
}


/**
 * 取该类在当前项目中被加载的任何实例
 * 例如：@Awake 自唤醒类，或是 Kotlin Companion Object、Kotlin Object 对象
 *
 * @param newInstance 若无任何已加载的实例，是否实例化
 */
fun Class<*>.getInstance(): Any {
    return try {
        newInstance()
    } catch (e: Exception) {
        for (field in fields) {
            if (field.type == this && Modifier.isStatic(field.modifiers)) {
                field.isAccessible = true
                return field.get(null)
            }
        }
    }
}

/**
 * 移除背包中特定数量的符合特定规则的物品
 *
 * @param matcher   规则
 * @param amount    实例
 * @return boolean
 */
fun Inventory.takeItem(amount: Int = 1, matcher: (itemStack: ItemStack) -> Boolean): Boolean {
    var takeAmount = amount
    contents.forEachIndexed { index, itemStack ->
        if (itemStack.isNotAir() && matcher(itemStack)) {
            takeAmount -= itemStack.amount
            if (takeAmount < 0) {
                itemStack.amount = itemStack.amount - (takeAmount + itemStack.amount)
                return true
            } else {
                setItem(index, null)
                if (takeAmount == 0) {
                    return true
                }
            }
        }
    }
    return false
}

@OptIn(ExperimentalContracts::class)
fun Material?.isAir(): Boolean {
    contract { returns(false) implies (this@isAir != null) }
    return this == null || this == Material.AIR || name.endsWith("_AIR")
}

@OptIn(ExperimentalContracts::class)
fun Material?.isNotAir(): Boolean {
    contract { returns(true) implies (this@isNotAir != null) }
    return !isAir()
}

@OptIn(ExperimentalContracts::class)
fun ItemStack?.isAir(): Boolean {
    contract { returns(false) implies (this@isAir != null) }
    return this == null || type == Material.AIR || type.name.endsWith("_AIR")
}

@OptIn(ExperimentalContracts::class)
fun ItemStack?.isNotAir(): Boolean {
    contract { returns(true) implies (this@isNotAir != null) }
    return !isAir()
}

@JvmOverloads
fun submit(
    now: Boolean = false,
    async: Boolean = false,
    delay: Long = 0,
    period: Long = 0,
    executor: BukkitRunnable.() -> Unit,
): BukkitRunnable {
    val runnable = object :BukkitRunnable() {
        override fun run() {
            executor.invoke(this)
        }
    }

    if (now) {
        if (async) {
            runnable.runTaskAsynchronously(ThePit.getInstance())
        } else {
            runnable.runTask(ThePit.getInstance())
        }
    } else {
        if (async) {
            if (period == 0L) {
                runnable.runTaskLaterAsynchronously(ThePit.getInstance(), delay)
            } else {
                runnable.runTaskTimerAsynchronously(ThePit.getInstance(), delay, period)
            }
        } else {
            if (period == 0L) {
                runnable.runTaskLater(ThePit.getInstance(), delay)
            } else {
                runnable.runTaskTimer(ThePit.getInstance(), delay, period)
            }
        }
    }

    return runnable
}

fun org.bukkit.inventory.ItemStack?.toMythicItem(): cn.charlotte.pit.item.IMythicItem? {
    if (this == null) {
        return null
    }

    return when (ItemUtil.getInternalName(this)) {
        "mythic_sword" -> {
            cn.charlotte.pit.item.type.mythic.MythicSwordItem()
        }

        "mythic_bow" -> {
            cn.charlotte.pit.item.type.mythic.MythicBowItem()
        }

        "mythic_leggings" -> {
            cn.charlotte.pit.item.type.mythic.MythicLeggingsItem()
        }

        else -> {
            null
        }
    }?.also {
        it.loadFromItemStack(this)
    }
}