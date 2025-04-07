package cn.charlotte.pit.event

import org.bukkit.entity.Player

class PitDamagePlayerEvent(attack: Player, damage: Double, finalDamage: Double, val victim: Player) :
    PitDamageEvent(attack, damage, finalDamage)