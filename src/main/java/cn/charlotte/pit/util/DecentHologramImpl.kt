package cn.charlotte.pit.util

import cn.charlotte.pit.util.hologram.Hologram
import cn.charlotte.pit.util.hologram.HologramAPI
import cn.charlotte.pit.util.hologram.touch.TouchHandler
import cn.charlotte.pit.util.hologram.view.ViewHandler
import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.nms.NMS
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.*

class DecentHologramImpl(var loc: Location, var inputText: String): Hologram {

    val uuid = UUID.randomUUID()

    val hologram = DHAPI.createHologram(uuid.toString(), loc, false, mutableListOf(inputText))

    var above: Hologram? = null

    var attachingEntity = -1

    var receivers = arrayListOf<Player>()

    override fun isSpawned(): Boolean {
        return true
    }

    override fun spawn(ticks: Long) {
        spawn()
    }

    override fun spawn(receivers: MutableCollection<out Player>?): Boolean {
        hologram.isDefaultVisibleState = false

        hologram.hideAll()
        receivers?.forEach {
            hologram.setShowPlayer(it)
        }

        this.receivers.clear()
        this.receivers += receivers ?: emptyList()

        return spawn()
    }

    override fun spawn(): Boolean {
        return true
    }

    override fun deSpawn(): Boolean {
        DHAPI.removeHologram(uuid.toString())
        return true
    }

    override fun getText(): String {
        return inputText
    }

    override fun setText(text: String?) {
        val after = text ?: ""
        if (after == inputText) return
        inputText = after
        for (page in hologram.pages) {
            page.setLine(0, inputText)
        }

        hologram.hideAll()
        receivers.forEach {
            hologram.setShowPlayer(it)
        }
    }

    override fun update() {

    }

    override fun update(interval: Long) {

    }

    override fun getLocation(): Location {
        return loc
    }

    override fun setLocation(loc: Location) {
        move(loc)
    }

    override fun move(loc: Location) {
        hologram.location = loc
        this.loc = loc
    }

    override fun isTouchable(): Boolean {
        return false
    }

    override fun setTouchable(flag: Boolean) {

    }

    override fun addTouchHandler(handler: TouchHandler) {

    }

    override fun removeTouchHandler(handler: TouchHandler) {

    }

    override fun getTouchHandlers(): MutableCollection<TouchHandler> {
        TODO("Not yet implemented")
    }

    override fun clearTouchHandlers() {

    }

    override fun addViewHandler(handler: ViewHandler) {

    }

    override fun removeViewHandler(handler: ViewHandler) {

    }

    override fun getViewHandlers(): MutableCollection<ViewHandler> {
        return mutableListOf()
    }

    override fun clearViewHandlers() {

    }

    override fun addLineBelow(text: String): Hologram {
        TODO("Not yet implemented")
    }

    override fun getLineBelow(): Hologram? {
        return null
    }

    override fun removeLineBelow(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getLinesBelow(): MutableCollection<Hologram> {
        TODO("Not yet implemented")
    }

    override fun addLineAbove(text: String): Hologram {
        return HologramAPI.createHologram(this.location.add(0.0, 0.25, 0.0), text).apply {
            above = this
        }
    }

    override fun getLineAbove(): Hologram? {
        return above
    }

    override fun removeLineAbove(): Boolean {
        above?.deSpawn()

        return true
    }

    override fun getLinesAbove(): MutableCollection<Hologram> {
        TODO("Not yet implemented")
    }

    override fun getLines(): MutableCollection<Hologram> {
        TODO("Not yet implemented")
    }

    override fun getAttachedTo(): Entity? {
        TODO("Not yet implemented")
    }

    override fun setAttachedTo(entity: Entity?) {
        entity ?: run {
            attachingEntity = -1
            return
        }

        attachingEntity = entity.entityId
        for (player in Bukkit.getOnlinePlayers()) {
            sendAttach(player)
        }
    }

    private fun sendAttach(player: Player) {
        for (page in hologram.pages) {
            for (line in page.lines) {
                val lineId = line.entityIds[0]
                NMS.getInstance().attachFakeEntity(player, lineId, attachingEntity)
            }
        }
    }
}