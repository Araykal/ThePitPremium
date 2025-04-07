package cn.charlotte.pit

import cn.charlotte.pit.actionbar.ActionBarManager
import cn.charlotte.pit.addon.AddonHandler
import cn.charlotte.pit.command.CleanupNoDupeItemCommand
import cn.charlotte.pit.command.PitAdminCommand
import cn.charlotte.pit.command.PitCommand
import cn.charlotte.pit.config.NewConfiguration
import cn.charlotte.pit.data.CDKData
import cn.charlotte.pit.enchantment.AbstractEnchantment
import cn.charlotte.pit.enchantment.type.aqua.ClubRodEnchant
import cn.charlotte.pit.enchantment.type.aqua.GrandmasterEnchant
import cn.charlotte.pit.enchantment.type.aqua.LuckOfPondEnchant
import cn.charlotte.pit.enchantment.type.aqua.RogueEnchant
import cn.charlotte.pit.enchantment.type.dark_normal.*
import cn.charlotte.pit.enchantment.type.dark_rare.ComboDazzlingGoldEnchant
import cn.charlotte.pit.enchantment.type.dark_rare.ComboUnpredictablyEnchant
import cn.charlotte.pit.enchantment.type.dark_rare.ComboVenomEnchant
import cn.charlotte.pit.enchantment.type.dark_rare.GoldenHandcuffsEnchant
import cn.charlotte.pit.enchantment.type.genesis.*
import cn.charlotte.pit.enchantment.type.normal.*
import cn.charlotte.pit.enchantment.type.op.*
import cn.charlotte.pit.enchantment.type.rage.*
import cn.charlotte.pit.enchantment.type.ragerare.Regularity
import cn.charlotte.pit.enchantment.type.ragerare.ThinkOfThePeopleEnchant
import cn.charlotte.pit.enchantment.type.rare.*
import cn.charlotte.pit.enchantment.type.sewer_normal.AegisEnchant
import cn.charlotte.pit.enchantment.type.special.SoulRipperEnchant
import cn.charlotte.pit.events.impl.*
import cn.charlotte.pit.events.impl.major.*
import cn.charlotte.pit.hook.PitPapiHook
import cn.charlotte.pit.impl.PlayerPointsAPIImpl
import cn.charlotte.pit.item.AbstractPitItem
import cn.charlotte.pit.item.type.*
import cn.charlotte.pit.js.JSHandler
import cn.charlotte.pit.listener.*
import cn.charlotte.pit.map.kingsquests.KingsQuests
import cn.charlotte.pit.menu.shop.button.type.BowBundleShopButton
import cn.charlotte.pit.menu.shop.button.type.CombatSpadeShopButton
import cn.charlotte.pit.menu.shop.button.type.PantsBundleShopButton
import cn.charlotte.pit.menu.shop.button.type.SwordBundleShopButton
import cn.charlotte.pit.nametag.NameTagImpl
import cn.charlotte.pit.npc.type.*
import cn.charlotte.pit.perk.AbstractPerk
import cn.charlotte.pit.perk.type.boost.*
import cn.charlotte.pit.perk.type.prestige.*
import cn.charlotte.pit.perk.type.shop.*
import cn.charlotte.pit.perk.type.streak.beastmode.*
import cn.charlotte.pit.perk.type.streak.grandfinale.ApostleForTheGesusKillStreak
import cn.charlotte.pit.perk.type.streak.grandfinale.AssuredStrikeKillStreak
import cn.charlotte.pit.perk.type.streak.grandfinale.GrandFinaleMegaStreak
import cn.charlotte.pit.perk.type.streak.grandfinale.LeechKillStreak
import cn.charlotte.pit.perk.type.streak.hermit.*
import cn.charlotte.pit.perk.type.streak.highlander.GoldNanoFactoryKillStreak
import cn.charlotte.pit.perk.type.streak.highlander.HighlanderMegaStreak
import cn.charlotte.pit.perk.type.streak.highlander.KhanateKillStreak
import cn.charlotte.pit.perk.type.streak.highlander.WitherCraftKillStreak
import cn.charlotte.pit.perk.type.streak.nonpurchased.*
import cn.charlotte.pit.perk.type.streak.tothemoon.SuperStreaker
import cn.charlotte.pit.perk.type.streak.tothemoon.ToTheMoonMegaStreak
import cn.charlotte.pit.perk.type.streak.uber.UberStreak
import cn.charlotte.pit.quest.type.*
import cn.charlotte.pit.runnable.*
import cn.charlotte.pit.scoreboard.Scoreboard
import cn.charlotte.pit.sound.impl.*
import cn.charlotte.pit.util.command.CommandHandler
import cn.charlotte.pit.util.nametag.NametagHandler
import cn.charlotte.pit.util.scoreboard.Assemble
import com.comphenix.protocol.ProtocolLibrary
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.spigotmc.AsyncCatcher
import real.nanoneko.EnchantedConstructor
import real.nanoneko.ItemConstructor
import real.nanoneko.PerkConstructor
import sinc.Native
import java.io.File
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Native
object PitHook {

    var isLoaded = false;

    /*    val gitVersion = "7a57acd"*/

    fun init() {
//        if (!WebSocketClient.connect) {
//            WebSocketClient().connect()
//        }
        Bukkit.getScheduler().runTaskLater(ThePit.getInstance(), {
            try {
                NewConfiguration.loadFile()
                NewConfiguration.load()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            registerActionBarManager()
            loadEnchants()
            loadPerks()
            loadItems()
            loadNameTag()
            loadScoreBoard()
            loadQuests()
            loadEvents()
            registerListeners()
            loadRunnable()
            registerSounds()

            loadCommands()
            loadNpcs()
            loadAPI()

            Bukkit.getPluginManager().getPlugin("PlaceholderAPI")?.let {
                PitPapiHook.register()
            }

            /*                val description = ThePit.getInstance().description

                            val field = PluginDescriptionFile::class.java.getDeclaredField("version")
                            field.isAccessible = true
                            field.set(description, gitVersion)*/

            AddonHandler.start()

            ActionBarDisplayRunnable.start()

            KingsQuests.enable()

            CDKData.loadAllCDKFromData()

            Bukkit.getPluginManager().registerEvents(SewersRunnable, ThePit.getInstance())
            SewersRunnable.runTaskTimer(ThePit.getInstance(), 20L, 20L)

            println("Loading complete.")
            isLoaded = true
        }, 20L)
    }


    private fun loadAPI() {
        ThePit.getInstance().pointsAPI = PlayerPointsAPIImpl;
        JSHandler.load()
    }

    private fun loadCommands() {
        CommandHandler.init(ThePit.getInstance())
        CommandHandler.registerClass(PitAdminCommand::class.java)
        CommandHandler.registerClass(PitCommand::class.java)
        CommandHandler.registerClass(CleanupNoDupeItemCommand::class.java)
    }

    private fun loadRunnable() {
        GameRunnable()
            .runTaskTimer(ThePit.getInstance(), 1L, 1L)

        GoldDropRunnable()
            .runTaskTimer(ThePit.getInstance(), 20, 20)

        ProtectRunnable()
            .runTaskTimer(ThePit.getInstance(), 20, 20)
        NightVisionRunnable().runTaskTimer(ThePit.getInstance(), 20, 20)
//        NiggerRunnable().runTaskTimerAsynchronously(ThePit.getInstance(), 20, 20)
        /*        AntiCollateralRunnable().runTaskTimerAsynchronously(ThePit.getInstance(), 1 * 20 * 60, 60L)*/
        ScheduledThreadPoolExecutor(1).scheduleWithFixedDelay(BountyRunnable(), 100, 100, TimeUnit.MILLISECONDS)
    }

    private fun loadItems() {
        val clazzList = mutableListOf(
            AngelChestplate::class.java,
            ArmageddonBoots::class.java,
            BountySolventPotion::class.java,
            ChunkOfVileItem::class.java,
            FunkyFeather::class.java,
            GoldenHelmet::class.java,
            JewelSword::class.java,
            JumpBoostPotion::class.java,
            MythicRepairKit::class.java,
            MusicalRune::class.java,
            PitCactus::class.java,
            SuperPackage::class.java,
            TotallyLegitGem::class.java,
            GlobalAttentionGem::class.java,
            UberDrop::class.java,
            LuckyGem::class.java,
            GrimReaper::class.java,
        )
        try {
            if (ItemConstructor.getItems().isNotEmpty()) {
                println("加载额外物品中...")
                clazzList.addAll(ItemConstructor.getItems())
            }
            if (ThePit.getInstance().pitConfig.code == "4847a648-bd9f-463e-ab18-3006b0fabd3b") {
                clazzList += LuckyChestplate::class.java
            }
            for (clazz in clazzList) {
                try {
                    val pitItem = clazz.getDeclaredConstructor().newInstance()
                    if (pitItem is AbstractPitItem) {
                        if (pitItem is Listener) {
                            Bukkit.getPluginManager().registerEvents(pitItem, ThePit.getInstance())
                        }
                        ThePit.getInstance().itemFactor.registerItem(pitItem)
                        /*                        ThePit.getInstance().itemFactor.itemMap[pitItem.internalName] = clazz as Class<AbstractPitItem>*/
                    }
                } catch (e: Exception) {
                    println("An error occurred: ${e.message}")
                }
            }
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
        }
    }

    private fun loadEnchants() {
        val enchantmentFactor = ThePit.getInstance().enchantmentFactor
        val classes = mutableListOf(
            JudgmentShot::class.java,
            LastStandEnchant::class.java,
            BreakTheMirror::class.java,
            ComboUnpredictablyEnchant::class.java,
            ComboDazzlingGoldEnchant::class.java,
            NightFallEnchant::class.java,
            MysticRealmEnchant::class.java,
            TheSwiftWindEnchant::class.java,
            SoulEarterEnchant::class.java,
            PhantomShieldEnchant::class.java,
            KingKillersEnchant::class.java,
            //end
            ClubRodEnchant::class.java,
            GrandmasterEnchant::class.java,
            LuckOfPondEnchant::class.java,
            RogueEnchant::class.java,
            Regularity::class.java,
            ThinkOfThePeopleEnchant::class.java,
            NewDealEnchant::class.java,
            ReallyToxicEnchant::class.java,
            SingularityEnchant::class.java,
            GrimReaperEnchant::class.java,
            HedgeFundEnchant::class.java,
            MindAssaultEnchant::class.java,
            MiseryEnchant::class.java,
            SanguisugeEnchant::class.java,
            SomberEnchant::class.java,
            SpiteEnchant::class.java,
            ComboVenomEnchant::class.java,
            GoldenHandcuffsEnchant::class.java,
            EvilWithinEnchant::class.java,
            GuardianEnchant::class.java,
            JerryEnchant::class.java,
            JerryEnchant2::class.java,
            JerryEnchant3::class.java,
            JerryEnchant4::class.java,
            JerryEnchant5::class.java,
            JerryEnchant6::class.java,
            JerryEnchant7::class.java,
            AntiAbsorptionEnchant::class.java,
            AntiBowSpammerEnchantP::class.java,
            AntiBowSpammerEnchantW::class.java,
            AntiMythicismEnchant::class.java,
            ArrowArmoryEnchant::class.java,
            BerserkerEnchant::class.java,
            BillyEnchant::class.java,
            BooBooEnchant::class.java,
            BountyHunterEnchant::class.java,
            BowComboEnchant::class.java,
            BruiserEnchant::class.java,
            BulletTimeEnchant::class.java,
            ComboDamageEnchant::class.java,
            ComboHealEnchant::class.java,
            ComboSwiftEnchant::class.java,
            CounterJanitorEnchant::class.java,
            CounterOffensiveEnchant::class.java,
            CreativeEnchant::class.java,
            CriticallyFunkyEnchant::class.java,
            CriticallyRichEnchant::class.java,
            CrushEnchant::class.java,
            DavidAndGoliathEnchant::class.java,
            DiamondAllergyEnchant::class.java,
            DiamondBreakerEnchant::class.java,
            ElectrolytesEnchant::class.java,
            EndlessQuiverEnchant::class.java,
            FractionalReserveEnchant::class.java,
            GoldExplorerEnchant::class.java,
            GutsEnchant::class.java,
            HermesEnchant::class.java,
            HuntTheHunterEnchant::class.java,
            LifeStealEnchant::class.java,
            LureEnchant::class.java,
            MirrorEnchant::class.java,
            MixedCombatEnchant::class.java,
            NotGladiatorEnchant::class.java,
            OverHealEnchant::class.java,
            PantsRadarEnchant::class.java,
            ParasiteEnchant::class.java,
            PebbleEnchant::class.java,
            PeroxideEnchant::class.java,
            PitMBAEnchant::class.java,
            PitPocketEnchant::class.java,
            PowerEnchant::class.java,
            ProtectionEnchant::class.java,
            PurpleGoldEnchant::class.java,
            ReaperEnchant::class.java,
            ResentmentEnchant::class.java,
            RespawnAbsorptionEnchant::class.java,
            RespawnResistanceEnchant::class.java,
            RustBowEnchant::class.java,
            SelfCheckoutEnchant::class.java,
            SharkEnchant::class.java,
            SharpnessEnchant::class.java,
            SierraEnchant::class.java,
            SniperEnchant::class.java,
            SpeedyKillEnchant::class.java,
            SprintDrainEnchant::class.java,
            StrikeGoldEnchant::class.java,
            ThornsEnchant::class.java,
            ThumpEnchant::class.java,
            TNTEnchant::class.java,
            TrueDamageArrowEnchant::class.java,
            UnBreakEnchant::class.java,
            WaspEnchant::class.java,
            WisdomEnchant::class.java,
            BlazingAngelEnchant::class.java,
            BounceBowEnchant::class.java,
            DJBundlePVZ::class.java,
            EchoOfSnowlandPEnchant::class.java,
            EchoOfSnowlandWEnchant::class.java,
            EmergencyColonyEnchant::class.java,
            KFCBoomerEnchant::class.java,
            LaserEnchant::class.java,
            MultiExchangeLocationEnchant::class.java,
            OPDamageEnchant::class.java,
            PowerAngelEnchant::class.java,
            StarJudgementEnchant::class.java,
            SuperLaserEnchant::class.java,
            SuperSlimeEnchant::class.java,
            TestEnchant::class.java,
            AbsorptionEnchant::class.java,
            ArchangelEnchant::class.java,
            AssassinEnchant::class.java,
            BillionaireEnchant::class.java,
            ComboStrikeEnchant::class.java,
            ComboStunEnchant::class.java,
            DivineMiracleEnchant::class.java,
            EnderBowEnchant::class.java,
            ExecutionerEnchant::class.java,
            FightOrDieEnchant::class.java,
            GambleEnchant::class.java,
            GomrawsHeartEnchant::class.java,
            HealerEnchant::class.java,
            HealShieldEnchant::class.java,
            HemorrhageEnchant::class.java,
            LuckyShotEnchant::class.java,
            MegaLongBowEnchant::class.java,
            PaparazziEnchant::class.java,
            PullBowEnchant::class.java,
            SlimeEnchant::class.java,
            SnowballsEnchant::class.java,
            SolitudeEnchant::class.java,
            SpeedyHitEnchant::class.java,
            ThePunchEnchant::class.java,
            VolleyEnchant::class.java,
            AegisEnchant::class.java,
            SoulRipperEnchant::class.java,
            AceOfSpades::class.java,
            Brakes::class.java,
            BreachingChargeEnchant::class.java
        )

        fun addSpecialEnchantments() {
            val pitConfigCode = ThePit.getInstance().pitConfig.code

            when (pitConfigCode) {
                "d8157845-8e44-4c83-ab53-60bb7abc3a08", "a41a5f6e-fcdc-443c-aaa2-8eb6567863d8" -> {
                    classes += HappyNewYearEnchant::class.java
                    classes += WitheredAndPiercingThroughTheHeart::class.java
                    classes += LastShadowLeapForward::class.java
                }

                "4847a648-bd9f-463e-ab18-3006b0fabd3b" -> {
                    classes += RealManEnchant::class.java
                }
            }
        }

        fun filterDisabledEnchantments() {
            val pitConfig = ThePit.getInstance().pitConfig
            val iterator = classes.iterator()
            while (iterator.hasNext()) {
                val enchantClass = iterator.next()
                val enchantNameMethod = enchantClass.getMethod("getEnchantName")
                val enchantName =
                    enchantNameMethod.invoke(enchantClass.getDeclaredConstructor().newInstance()) as String

                if (pitConfig.disableEnchants.isEmpty()) {
                    println("未禁用任何附魔")
                    return
                }
                if (pitConfig.disableEnchants.contains(enchantName)) {
                    println("移除附魔: $enchantName")
                    iterator.remove()
                } else {
                    enchantNames.add(enchantName)
                }
            }
        }

        fun addEnchantments() {
            val enchantmentClasses: List<Class<*>> = EnchantedConstructor.getEnchantments()
            val enchantmentCollection: List<Class<out AbstractEnchantment>> =
                enchantmentClasses.filterIsInstance<Class<out AbstractEnchantment>>()

            if (enchantmentCollection.isNotEmpty()) {
                println("加载额外附魔中...")
                classes.addAll(enchantmentCollection)
            }
        }

        addSpecialEnchantments()
        filterDisabledEnchantments()
        addEnchantments()

        enchantmentFactor.init(classes)
        enchantmentInt = enchantmentFactor.enchantmentMap.size

        val file = File(ThePit.getInstance().dataFolder, "Enable_Enchantment_List.txt")
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.writeText(enchantNames.joinToString("\n"))
        fun filterEnchantmentList(annotationName: String): MutableList<AbstractEnchantment> {
            return classes.mapNotNull {
                val instance = it.getDeclaredConstructor().newInstance()
                val hasAnnotation =
                    it.declaredAnnotations.any { annotation -> annotation.annotationClass.simpleName == annotationName }
                if (hasAnnotation && instance is AbstractEnchantment) {
                    instance
                } else {
                    null
                }
            }.toMutableList()
        }
        all = classes.map {
            it.getDeclaredConstructor().newInstance() as AbstractEnchantment
        }.toMutableList()
        music.apply {
            add(JerryEnchant())
            add(JerryEnchant2())
            add(JerryEnchant3())
            add(JerryEnchant4())
            add(JerryEnchant5())
            add(JerryEnchant6())
            add(JerryEnchant7())
        }
        weapon = filterEnchantmentList("WeaponOnly")
        bow = filterEnchantmentList("BowOnly")
        armor = filterEnchantmentList("ArmorOnly")
        println("开启附魔列表已保存至: ${file.absolutePath}")
    }


    private fun loadScoreBoard() {
        val assemble = Assemble(ThePit.getInstance(), Scoreboard())
        assemble.ticks = 4
    }

    private fun loadNameTag() {
        val nametagHandler = NametagHandler(ThePit.getInstance(), NameTagImpl())
        nametagHandler.ticks = 20
    }

    private fun loadPerks() {
        val perkFactory = ThePit.getInstance().perkFactory
        val classes = mutableListOf(
            BowBoostPerk::class.java,
            BuildBattlerBoostPerk::class.java,
            CoinBoostPerk::class.java,
            CoinContractBoostPerk::class.java,
            CoinPrestigeBoostPerk::class.java,
            DmgReduceBoostPerk::class.java,
            ElGatoBoostPerk::class.java,
            MeleeBoostPerk::class.java,
            XPBoostPerk::class.java,
            XPContractBoostPerk::class.java,
            XPPrestigeBoostPerk::class.java,
            ArrowArmoryPerk::class.java,
            AssistantToTheStreakerPerk::class.java,
            AutoBuyPerk::class.java,
            BarbarianPerk::class.java,
            BeastModeBundlePerk::class.java,
            BountySolventShopPerk::class.java,
            CelebrityPerk::class.java,
            CombatSpadePerk::class.java,
            ContractorPerk::class.java,
            CoolPerk::class.java,
            CoopCatPerk::class.java,
            DiamondLeggingsShopPerk::class.java,
            DirtyPerk::class.java,
            DivineInterventionPerk::class.java,
            ExtraEnderchestPerk::class.java,
            ExtraHeartPerk::class.java,
            ExtraKillStreakSlotPerk::class.java,
            ExtraPerkSlotPerk::class.java,
            FastPassPerk::class.java,
            FirstAidEggPerk::class.java,
            FirstStrikePerk::class.java,
            FishClubPerk::class.java,
            GoldPickaxePerk::class.java,
            GrandFinaleBundlePerk::class.java,
            HeresyPerk::class.java,
            HermitBundlePerk::class.java,
            HighlanderBundlePerk::class.java,
            ImpatientPerk::class.java,
            IronPackShopPerk::class.java,
            JumpBoostShopPerk::class.java,
//            KungFuKnowledgePerk::class.java,
            MarathonPerk::class.java,
            MonsterPerk::class.java,
            MythicismPerk::class.java,
            ObsidianStackShopPerk::class.java,
            OlympusPerk::class.java,
            PantsBundleShopPerk::class.java,
            SwrodBundleShopPerk::class.java,
            BowBundleShopPerk::class.java,
            PromotionPerk::class.java,
            PureRage::class.java,
            RamboPerk::class.java,
            RawNumbersPerk::class.java,
            ReconPerk::class.java,
            ScamArtistPerk::class.java,
            SelfConfidencePerk::class.java,
            TacticalInsertionsPerk::class.java,
            TastySoupPerk::class.java,
            TenacityPerk::class.java,
            TheWayPerk::class.java,
            ThickPerk::class.java,
            ToTheMoonBundle::class.java,
            YummyPerk::class.java,
            ArrowRecoveryPerk::class.java,
            BountyHunterPerk::class.java,
            FishingRodPerk::class.java,
            GladiatorPerk::class.java,
            GoldenHeadPerk::class.java,
            GoldMinerPerk::class.java,
            LuckyDiamondPerk::class.java,
            MinerPerk::class.java,
            OverHealPerk::class.java,
            SafetyFirstPerk::class.java,
            SafetySecondPerk::class.java,
            StrengthPerk::class.java,
            TrickleDownPerk::class.java,
            VampirePerk::class.java,
            BeastModeMegaStreak::class.java,
            MonsterKillStreak::class.java,
            RAndRKillStreak::class.java,
            TacticalRetreatKillStreak::class.java,
            ToughSkinKillStreak::class.java,
            ApostleForTheGesusKillStreak::class.java,
            AssuredStrikeKillStreak::class.java,
            GrandFinaleMegaStreak::class.java,
            LeechKillStreak::class.java,
            AuraOfProtectionKillStreak::class.java,
            GlassPickaxeKillStreak::class.java,
            HermitMegaStreak::class.java,
            IceCubeKillStreak::class.java,
            PungentKillStreak::class.java,
            GoldNanoFactoryKillStreak::class.java,
            HighlanderMegaStreak::class.java,
            KhanateKillStreak::class.java,
            WitherCraftKillStreak::class.java,
            ArquebusierKillStreak::class.java,
            ExpliciousKillStreak::class.java,
            FightOrFlightKillStreak::class.java,
            OverDriveMegaStreak::class.java,
            SecondGappleKillStreak::class.java,
            SpongeSteveKillStreak::class.java,
            UberStreak::class.java,
            ToTheMoonMegaStreak::class.java,
            SuperStreaker::class.java
        )
        val perkClasses: List<Class<*>> = PerkConstructor.getPerks()
        val perkCollection: List<Class<out AbstractPerk>> =
            perkClasses.filterIsInstance<Class<out AbstractPerk>>()

        if (perkCollection.isNotEmpty()) {
            println("加载额外增益中...")
            classes.addAll(perkCollection)
        }
        perkFactory.init(classes)
        perkInt = perkFactory.perkMap.size;
        AsyncCatcher.enabled = false
    }

    private fun registerSounds() {
        listOf(
            DoubleStreakSound,
            TripleStreakSound,
            QuadraStreakSound,
            StreakSound,
            SuccessfullySound
        ).forEach {
            ThePit.getInstance().soundFactory.registerSound(it)
        }
    }

    private fun loadNpcs() {
        val npc = ThePit.getInstance().npcFactory
        npc.init(
            listOf(
                GenesisAngelNpc::class.java,
                GenesisDemonNpc::class.java,
                KeeperNPC::class.java,
                MailNpc::class.java,
                PerkNPC::class.java,
                PrestigeNPC::class.java,
                QuestNpc::class.java,
                ShopNPC::class.java,
                StatusNPC::class.java,
                LeaderNPC::class.java
            )
        )
    }

    private fun loadQuests() {
        val questFactory = ThePit.getInstance().questFactory
        val classes = listOf<Class<*>>(
            DeepInfiltration::class.java,
            DestoryArmor::class.java,
            DryBlood::class.java,
            HighValueTarget::class.java,
            KeepSilence::class.java,
            LowEfficiency::class.java,
            LowHealth::class.java,
            SinkingMoonlight::class.java
        )
        questFactory.init(classes)
    }

    private fun loadEvents() {
        val eventFactory = ThePit.getInstance().eventFactory
        val classes = listOf<Class<*>>(
            PatronageEvent::class.java,
            HamburgerEvent::class.java,
            RagePitEvent::class.java,
            RedVSBlueEvent::class.java,
            BlockHeadEvent::class.java,
            SpireEvent::class.java,
            AuctionEvent::class.java,
            CakeEvent::class.java,
            CarePackageEvent::class.java,
            EveOneBountyEvent::class.java,
            DragonEggsEvent::class.java,
            DamagePlus::class.java,
            QuickMathEvent::class.java,
            KingOfTheHillEvent::class.java,
   /*         SquadsEvent::class.java*/
        )

        eventFactory.init(classes)
    }

    private fun registerListeners() {
//        val classes = mutableListOf<Class<*>>(
//            CombatListener::class.java, GameEffectListener::class.java,
//            DataListener::class.java, EnderChestListener::class.java,
//            ChatListener::class.java, PlayerListener::class.java,
//            ProtectListener::class.java, PantsBundleShopButton::class.java,
//            SwordBundleShopButton::class.java, BowBundleShopButton::class.java,
//            CombatSpadeShopButton::class.java
//        )
//        for (aClass in classes) {
//            try {
//                val o = aClass.getDeclaredConstructor().newInstance()
//                Bukkit.getPluginManager().registerEvents(o as Listener, ThePit.getInstance())
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }

        ProtocolLibrary.getProtocolManager().addPacketListener(PacketListener())

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            Bukkit.getPluginManager().registerEvents(MythicMobListener, ThePit.getInstance());
            println("加载MM支持")
        }
    }

    private fun registerActionBarManager() {
        val actionBarManager = ActionBarManager()
        ThePit.getInstance().actionBarManager = actionBarManager;
        Bukkit.getScheduler().runTaskTimerAsynchronously(ThePit.getInstance(), {
            actionBarManager.tick();
        }, 1, 1)
    }
}

