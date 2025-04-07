package cn.charlotte.pit

import cn.charlotte.pit.enchantment.AbstractEnchantment

var all: MutableList<AbstractEnchantment> = mutableListOf()

var armor: MutableList<AbstractEnchantment> = mutableListOf()
var bow: MutableList<AbstractEnchantment> = mutableListOf()
var weapon: MutableList<AbstractEnchantment> = mutableListOf()
val music: MutableList<AbstractEnchantment> = mutableListOf()
val musicIndex = listOf(11, 12, 13, 14, 15, 4, 22)
val enchantNames = mutableListOf<String>()
var enchantmentInt: Int = 0;
var perkInt: Int = 0;