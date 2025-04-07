@file:Suppress("removal", "DEPRECATION")

package cn.charlotte.pit.js

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.data.PlayerProfile
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity
import cn.charlotte.pit.enchantment.type.JSEnchantment
import cn.charlotte.pit.enchantmentInt
import cn.charlotte.pit.util.AddonUtil
import cn.charlotte.pit.util.chat.CC
import jdk.dynalink.beans.StaticClass
import jdk.nashorn.api.scripting.ScriptObjectMirror
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import javax.script.*
import kotlin.math.pow

object JSHandler {

    private val scriptEngineFactory by lazy {
        try {
            Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory").getDeclaredConstructor()
                .newInstance() as ScriptEngineFactory
        } catch (ex: ClassNotFoundException) {
            jdk.nashorn.api.scripting.NashornScriptEngineFactory()
        }
    }

    private val arraysClass: StaticClass = StaticClass.forClass(Arrays::class.java)
    private val bukkitClass: StaticClass = StaticClass.forClass(Bukkit::class.java)
    private val playerClass: StaticClass = StaticClass.forClass(Player::class.java)
    private val runnableClass: StaticClass = StaticClass.forClass(Runnable::class.java)

    private val loadedJsEnchant = HashMap<String, JSEnchantment>()

    private var version = 0

    fun load() {

//        if (AddonUtil.check("js")) {
//            version = 5
//            println("Loading addon js...")
//        }
//        if (version != 2.0.pow(2).toInt() + 1) {
//            return
//        }


        val jsDir = File(ThePit.getInstance().dataFolder, "js")
        if (!jsDir.exists()) {
            jsDir.mkdir()
        }

        if (jsDir.listFiles().let { it == null || it.isEmpty() }) {
            File(ThePit.getInstance().dataFolder, "js/example.js").apply {
                createNewFile()
                writeBytes(
                    ThePit.getInstance().getResource("js/example.js").readBytes()
                )
            }
        }

        jsDir.walk().forEach {
            if (it.extension == "js") {
                val script = it.readText()
                load(script, it)
                println("已加载JS附魔 ${it.name}")
            }
        }
    }

    private fun load(script: String, file: File) {
        val existJsEnchant = loadedJsEnchant.remove(file.name)
        if (existJsEnchant != null) {
            ThePit.getInstance().enchantmentFactor.apply {
                enchantments -= existJsEnchant
                enchantmentMap.remove(existJsEnchant.nbtName)
                playerDamageds -= existJsEnchant
                attackEntities -= existJsEnchant
                playerBeKilledByEntities -= existJsEnchant
                playerShootEntities -= existJsEnchant
                playerAssists -= existJsEnchant
            }
            enchantmentInt -= 1
        }
        try {
            val engine = createScriptEngineEnv(script)

            val invocable = engine as Invocable

            val attackFunc = (engine.get("attack") as? ScriptObjectMirror)?.isFunction == true
            val killedFunc = (engine.get("killed") as? ScriptObjectMirror)?.isFunction == true
            val shootFunc = (engine.get("shoot") as? ScriptObjectMirror)?.isFunction == true
            val assistFunc = (engine.get("assist") as? ScriptObjectMirror)?.isFunction == true
            val beKilledFunc = (engine.get("be_killed") as? ScriptObjectMirror)?.isFunction == true
            val beDamagedFunc = (engine.get("be_damaged") as? ScriptObjectMirror)?.isFunction == true

            Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), Runnable {

            }, 1L)

            val jsEnchantment = JSEnchantment(
                engine, engine.get("ENCHANT_NAME")!!.toString(),
                engine.get("INTERNAL_NAME")!!.toString(),
                EnchantmentRarity.valueOf(engine.get("RARITY")!!.toString().uppercase()),
                (engine.get("DESCRIPTIONS") as ScriptObjectMirror).map { it.value as String }.toList(),
                (engine.get("TYPE") as ScriptObjectMirror).map { it.value as String }.toSet(),
                { enchantLevel, attacker, victim, damage, finalDamage, boostDamage, cancel ->
                    if (attackFunc) {
                        invocable.invokeFunction(
                            "attack",
                            enchantLevel,
                            attacker,
                            victim,
                            damage,
                            finalDamage,
                            boostDamage,
                            cancel
                        )
                    }
                },
                { enchantLevel, myself, target, coins, experience ->
                    if (killedFunc) {
                        invocable.invokeFunction("killed", enchantLevel, myself, target, coins, experience)
                    }
                },
                { enchantLevel, attacker, target, damage, finalDamage, boostDamage, cancel ->
                    if (shootFunc) {
                        invocable.invokeFunction(
                            "shoot",
                            enchantLevel,
                            attacker,
                            target,
                            damage,
                            finalDamage,
                            boostDamage,
                            cancel
                        )
                    }
                },
                { enchantLevel, myself, target, damage, finalDamage, coins, experience ->
                    if (assistFunc) {
                        invocable.invokeFunction(
                            "assist",
                            enchantLevel,
                            myself,
                            target,
                            damage,
                            finalDamage,
                            coins,
                            experience
                        )
                    }
                },
                { enchantLevel, myself, target, coins, experience ->
                    if (beKilledFunc) {
                        invocable.invokeFunction("be_killed", enchantLevel, myself, target, coins, experience)
                    }
                },
                { enchantLevel, myself, attacker, damage, finalDamage, boostDamage, cancel ->
                    if (beDamagedFunc) {
                        invocable.invokeFunction(
                            "be_damaged",
                            enchantLevel,
                            myself,
                            attacker,
                            damage,
                            finalDamage,
                            boostDamage,
                            cancel
                        )
                    }
                },
            )

            loadedJsEnchant[file.name] = jsEnchantment
            ThePit.getInstance().enchantmentFactor.apply {
                enchantments += jsEnchantment
                enchantmentMap[jsEnchantment.internalName0] = jsEnchantment
                if (beDamagedFunc) {
                    playerDamageds += jsEnchantment
                }
                if (attackFunc) {
                    attackEntities += jsEnchantment
                }
                if (beKilledFunc) {
                    playerBeKilledByEntities += jsEnchantment
                }
                if (shootFunc) {
                    playerShootEntities += jsEnchantment
                }
                if (assistFunc) {
                    playerAssists += jsEnchantment
                }

                enchantmentInt += 1
            }
        } catch (e: Exception) {
            println("加载 ${file.name} 时出现错误")
            e.printStackTrace()
        }
    }

    private fun createScriptEngineEnv(script: String): ScriptEngine {
        return scriptEngineFactory.scriptEngine.apply {
            eval(script)

            // 定义全局绑定
            val bindings = SimpleBindings().apply {
                // 将 Lambda 包装为 Nashorn 可识别的函数
                put("print", Consumer { args: Any? ->
                    println(args)
                })

                // 定义 pitProfile 为 Function<Player, PlayerProfile>
                put("pit_profile", Function<Player, PlayerProfile> { player ->
                    PlayerProfile.getPlayerProfileByUuid(player.uniqueId)
                })

                put("translate", Function<String, String> { input ->
                    CC.translate(input)
                })

                // 其他绑定
                put("Pit", ThePit.getInstance())
                put("Bukkit", bukkitClass)
                put("Arrays", arraysClass)
                put("Player", playerClass)
                put("Runnable", runnableClass)
            }

            // 设置全局作用域
            context.setBindings(bindings, ScriptContext.GLOBAL_SCOPE)
        }
    }

}