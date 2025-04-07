ENCHANT_NAME = "千万富翁";
INTERNAL_NAME = "multimillionaire"
RARITY = "RARE"
TYPE = ["WEAPON", "BOW", "ARMOR"]
DESCRIPTIONS = ["&7比亿万富翁穷一点的富翁", "&7没那么有钱的那种"];

function attack(enchant_level, attacker, target, damage, final_damage, boost_damage, cancel) {
    if (target instanceof Player) {
        var attacker_profile = pit_profile(attacker);
        if (attacker_profile.getCoins() >= enchant_level * 10) {
            boost_damage.getAndAdd(enchant_level * 0.15);
            attacker_profile.setCoins(attacker_profile.getCoins() - enchant_level * 10);
            Bukkit.getScheduler().runTaskLater(Pit, new Runnable({
              run: function() {
                  attacker.sendMessage("延迟 20ticks 执行的代码");
              }
            }), 20);
        }
    }
}

function killed(enchant_level, myself, target, coins, experience) {
    // 击杀玩家
}

function shoot(enchant_level, attacker, target, damage, final_damage, boost_damage, cancel) {
    // 射箭
}

function assist(enchant_level, myself, target, damage, final_damage, coins, experience) {
    // 助攻
}

function be_killed(enchant_level, myself, target, coins, experience) {
    // 被击杀
}

function be_damaged(enchant_level, myself, attacker, damage, final_damage, boost_damage, cancel) {
    // 被攻击
}