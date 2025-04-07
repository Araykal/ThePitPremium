package cn.charlotte.pit.enchantment.type.aqua


import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.param.item.ArmorOnly
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.util.cooldown.Cooldown

@ArmorOnly
class ClubRodEnchant: AbstractEnchantment() {
    companion object {
        init {
//            Bukkit.getScheduler().runTaskTimer(ThePit.getInstance(), {
//                for (player in Bukkit.getOnlinePlayers()) {
//                    if (player.inventory.leggings == null || ItemUtil.getEnchantLevel(player.inventory.leggings, "club_rod") == -1) {
//                        InventoryUtil.removeItemWithInternalName(player,"fishing_rod_aqua")
//                    } else if (InventoryUtil.getAmountOfItem(player,"fishing_rod_aqua") == 0) {
//                        player.inventory.addItem(
//                            ItemBuilder(Material.FISHING_ROD)
//                                .name("&a鱼竿")
//                                .canDrop(false)
//                                .canTrade(false)
//                                .canDrop(false)
//                                .canSaveToEnderChest(false)
//                                .deathDrop(false)
//                                .internalName("fishing_rod_aqua")
//                                .buildWithUnbreakable()
//                        )
//                    }
//                }
//            }, 20L, 20L)
        }
    }

    override fun getEnchantName(): String {
        return "鱼竿会员"
    }

    override fun getMaxEnchantLevel(): Int {
        return 1
    }

    override fun getNbtName(): String {
        return "club_rod"
    }

    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.FISH_NORMAL
    }

    override fun getCooldown(): Cooldown? {
        return null
    }

    override fun getUsefulnessLore(enchantLevel: Int): String {
        return "&7装备此神话之甲时,自身额外获得一条钓鱼竿"
    }


}