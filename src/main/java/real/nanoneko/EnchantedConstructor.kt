package real.nanoneko

import real.nanoneko.register.IMagicLicense

object EnchantedConstructor {
    private val enchantments: MutableList<Class<*>> = mutableListOf()

    fun getEnchantments(): List<Class<*>> {
        return enchantments
    }

    fun addEnchantment(enchantment: Class<*>) {
        if (IMagicLicense::class.java.isAssignableFrom(enchantment)) {
            enchantments.add(enchantment)
        } else {
            throw IllegalArgumentException("Only classes implementing IMagicLicense can be added as enchantments.")
        }
    }

    fun removeEnchantment(enchantment: Class<*>) {
        enchantments.remove(enchantment)
    }
}