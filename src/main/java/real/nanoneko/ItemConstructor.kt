package real.nanoneko

import real.nanoneko.register.IMagicLicense

/**
 * @author Araykal
 * @since 2025/1/31
 */
object ItemConstructor {
    private val items: MutableList<Class<*>> = mutableListOf()

    fun getItems(): List<Class<*>> {
        return items
    }

    fun addItems(enchantment: Class<*>) {
        if (IMagicLicense::class.java.isAssignableFrom(enchantment)) {
            items.add(enchantment)
        } else {
            throw IllegalArgumentException("Only classes implementing IMagicLicense can be added as items.")
        }
    }

    fun removeItems(enchantment: Class<*>) {
        items.remove(enchantment)
    }
}