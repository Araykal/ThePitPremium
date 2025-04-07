package real.nanoneko

import real.nanoneko.register.IMagicLicense

/**
 * @author Araykal
 * @since 2025/1/31
 */
object PerkConstructor {
    private val perks: MutableList<Class<*>> = mutableListOf()

    fun getPerks(): List<Class<*>> {
        return perks
    }

    fun addPerk(enchantment: Class<*>) {
        if (IMagicLicense::class.java.isAssignableFrom(enchantment)) {
            perks.add(enchantment)
        } else {
            throw IllegalArgumentException("Only classes implementing IMagicLicense can be added as perk.")
        }
    }

    fun removePerk(enchantment: Class<*>) {
        perks.remove(enchantment)
    }
}