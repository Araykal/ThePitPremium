package cn.charlotte.pit.impl

import cn.charlotte.pit.*
import cn.charlotte.pit.api.PitInternalHook
import cn.charlotte.pit.config.NewConfiguration
import cn.charlotte.pit.data.PlayerInvBackup
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.data.TradeData
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.menu.MythicWellMenu
import cn.charlotte.pit.events.IEpicEvent
import cn.charlotte.pit.events.INormalEvent
import cn.charlotte.pit.events.genesis.team.GenesisTeam
import cn.charlotte.pit.events.impl.*
import cn.charlotte.pit.events.impl.major.*
import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.item.type.AngelChestplate
import cn.charlotte.pit.item.type.ArmageddonBoots
import cn.charlotte.pit.item.type.ChunkOfVileItem
import cn.charlotte.pit.item.type.mythic.MythicBowItem
import cn.charlotte.pit.item.type.mythic.MythicLeggingsItem
import cn.charlotte.pit.item.type.mythic.MythicSwordItem
import cn.charlotte.pit.menu.admin.backpack.BackupShowMenu
import cn.charlotte.pit.menu.admin.item.AdminEnchantMenu
import cn.charlotte.pit.menu.admin.item.AdminItemMenu
import cn.charlotte.pit.menu.admin.item.AdminRuneMenu
import cn.charlotte.pit.menu.admin.trade.TradeTrackerMenu
import cn.charlotte.pit.menu.genesis.GenesisMenu
import cn.charlotte.pit.menu.heresy.HeresyMenu
import cn.charlotte.pit.menu.main.AuctionMenu
import cn.charlotte.pit.menu.shop.ShopMenu
import cn.charlotte.pit.util.AddonUtil
import cn.charlotte.pit.util.DecentHologramImpl
import cn.charlotte.pit.util.Utils
import cn.charlotte.pit.util.chat.CC
import cn.charlotte.pit.util.hologram.DefaultHologram
import cn.charlotte.pit.util.hologram.Hologram
import cn.charlotte.pit.util.item.ItemUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

object PitInternalImpl : PitInternalHook {
    var loaded = false
    override fun openMythicWellMenu(player: Player?) {
        MythicWellMenu(player!!).openMenu(player)
    }

    override fun openAuctionMenu(player: Player?) {
        AuctionMenu().openMenu(player)
    }

    override fun openAngelMenu(player: Player) {
        GenesisMenu(GenesisTeam.ANGEL).openMenu(player)
    }

    override fun openDemonMenu(player: Player) {
        GenesisMenu(GenesisTeam.DEMON).openMenu(player)
    }

    override fun openTradeTrackMenu(player: Player?, profile: PlayerProfile?, data: MutableList<TradeData>?) {
        TradeTrackerMenu(profile, data).openMenu(player)
    }

    override fun openBackupShowMenu(
        player: Player?,
        profile: PlayerProfile?,
        backup: PlayerInvBackup?,
        enderChest: Boolean
    ) {
        BackupShowMenu(profile, backup, enderChest).openMenu(player)
    }

    override fun openMenu(player: Player, menuName: String) {
        when (menuName) {
            "shop" -> {
                ShopMenu().openMenu(player)
            }

            "admin_enchant" -> {
                AdminEnchantMenu().openMenu(player)
            }

            "admin_item" -> {
                AdminItemMenu().openMenu(player)
            }

            "rune_item" -> {
                AdminRuneMenu().openMenu(player)
            }

            "heresy" -> {
                HeresyMenu().openMenu(player)
            }
        }
    }

    override fun openEvent(player: Player, eventName: String?): Boolean {
        eventName ?: return false
        val event = when (eventName.lowercase()) {
            "rage" -> {
                RagePitEvent()
            }

            "rvb" -> {
                RedVSBlueEvent()
            }

            "qm" -> {
                QuickMathEvent()
            }

            "package" -> {
                CarePackageEvent()
            }

            "100g" -> {
                EveOneBountyEvent()
            }

            "dragon_egg" -> {
                DragonEggsEvent()
            }

            "patronage" ->{
                PatronageEvent()
            }
            "hunt" -> {
                DamagePlus()
            }

            "cake" -> {
                CakeEvent()
            }

            "hamburger" -> {
                HamburgerEvent()
            }

            "spire" -> {
                SpireEvent()
            }

            "squads" -> {
                SquadsEvent()
            }

            "auction" + "" -> {
                AuctionEvent()
            }

            "bh" -> {
                BlockHeadEvent()
            }

            "koth" -> {
                KingOfTheHillEvent()
            }

            "cancel" -> {
                val factory = ThePit.getInstance().eventFactory
                if (factory.activeEpicEvent != null) {
                    factory.inactiveEvent(factory.activeEpicEvent)
                }
                if (factory.activeNormalEvent != null) {
                    factory.inactiveEvent(factory.activeNormalEvent)
                }
                return true
            }

            else -> return false
        }

        val factory = ThePit.getInstance().eventFactory

        if (event is IEpicEvent) {
            val profile = PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
            if (profile.playerOption.isDebugDamageMessage) {
                factory.activeEvent(event)
            } else {
                factory.pushEvent(event, true)
            }
        } else {
            if (factory.activeNormalEvent != null) {
                factory.inactiveEvent(factory.activeNormalEvent)
            }
            if (event is INormalEvent) {
                factory.activeEvent(event)
            } else {
                return false
            }
        }

        return true
    }

    override fun createHologram(location: Location, text: String): Hologram {
        val plugin = Bukkit.getPluginManager().getPlugin("DecentHolograms")
        return if (plugin == null) {
            DefaultHologram(location, text)
        } else {
            DecentHologramImpl(location, text)
        }
    }

    override fun getRunningKingsQuestsUuid(): UUID {
        return NewConfiguration.kingsQuestsMarker
    }

    override fun getPitSupportPermission(): String {
        return NewConfiguration.pitSupportPermission
    }

    override fun getRemoveSupportWhenNoPermission(): Boolean {
        return NewConfiguration.removeSupportWhenNoPermission
    }

    override fun reformatPitItem(itemStack: ItemStack?): ItemStack? {
        val internalName = ItemUtil.getInternalName(itemStack)
        val item = if ("mythic_sword" == internalName) {
            MythicSwordItem()
        } else if ("mythic_bow" == internalName) {
            MythicBowItem()
        } else if ("mythic_leggings" == internalName) {
            MythicLeggingsItem()
        } else if ("angel_chestplate" == internalName) {
            AngelChestplate()
        } else if ("armageddon_boots" == internalName) {
            ArmageddonBoots()
        } else {
            return itemStack
        }

        item.loadFromItemStack(itemStack)
        if (item.isEnchanted && (item.maxLive <= 0 || item.live <= 0)) {
            return null
        }


        return item.toItemStack()
    }

    override fun generateItem(item: String): ItemStack? {
        return when (item) {
            "ChunkOfVileItem" -> ChunkOfVileItem.toItemStack()
            "Leggings" -> MythicLeggingsItem().toItemStack()
            else -> null
        }
    }

    override fun getItemEnchantLevel(item: ItemStack?, enchantName: String?): Int {
        return Utils.getEnchantLevel(item, enchantName)
    }

    override fun addItemInHandEnchant(player: Player?, enchantName: String?, enchantLevel: Int): Int {
        val item = player?.inventory?.itemInHand ?: return 1

        if (item.type == Material.AIR) {
            return 0
        }

        val enchant = ThePit.getInstance().enchantmentFactor.enchantmentMap[enchantName] ?: return 1

        val mythicItem = Utils.getMythicItem(item) ?: return 2
        mythicItem.enchantments[enchant] = enchantLevel

        player.inventory.itemInHand = mythicItem.toItemStack()
        return 3
    }


    override fun getMythicItemItemStack(itemName: String?): ItemStack {
        val itemClass: Class<out AbstractPitItem> =
            ThePit.getInstance().itemFactor.itemMap[itemName] ?: return ItemStack(Material.AIR)
        val item = itemClass.getDeclaredConstructor().newInstance()
        val itemStack = item.toItemStack()
        return itemStack
    }


    override fun allEnchantments(): MutableList<AbstractEnchantment> {
        return all
    }

    override fun armorEnchantments(): MutableList<AbstractEnchantment> {
        return armor
    }

    override fun bowEnchantments(): MutableList<AbstractEnchantment> {
        return bow
    }

    override fun weaponEnchantments(): MutableList<AbstractEnchantment> {
        return weapon
    }

    override fun musicEnchantments(): MutableList<AbstractEnchantment> {
        return music
    }

    override fun checkIsAddon(addon: String): Boolean {
        return AddonUtil.check(addon)
    }

    override fun getWaterMark(): String {
        return CC.translate(NewConfiguration.waterMark)
    }

    override fun getBotName(): String {
        return CC.translate(NewConfiguration.botName)
    }

    override fun isLoaded(): Boolean {
        return loaded
    }
}