package cn.charlotte.pit.util.effect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * @Author: Misoryan
 * @Created_In: 2021/3/28 9:58
 */
public class EffectUtil {

    //todo: how to storage the custom pit effect?
    public static void addCustomPitEffect(Player player, CustomPitEffect effect) {

    }

    public static boolean hasCustomPitEffect(Player player, CustomPitEffectType type) {
        return false;
    }

    public static void removeCustomPitEffect(Player player, CustomPitEffectType type) {

    }


    public static String getEffectName(PotionEffectType effect) {
        return getEffectName(effect.getName());
    }

    public static String getEffectName(CustomPitEffectType effect) {
        return effect.name();
    }

    public static String getEffectName(String effect) {
        HashMap<String, String> effectMap = new HashMap<>();
        effectMap.put(PotionEffectType.SPEED.getName(), "&b速度");
        effectMap.put(PotionEffectType.REGENERATION.getName(), "&c生命恢复");
        effectMap.put(PotionEffectType.DAMAGE_RESISTANCE.getName(), "&3抗性提升");
        effectMap.put(PotionEffectType.SLOW_DIGGING.getName(), "&7挖掘疲劳");
        effectMap.put(PotionEffectType.SLOW.getName(), "&7缓慢");
        effectMap.put(PotionEffectType.JUMP.getName(), "&a跳跃提升");
        effectMap.put(PotionEffectType.WITHER.getName(), "&4凋零");
        effectMap.put(PotionEffectType.ABSORPTION.getName(), "&6生命吸收");
        effectMap.put(PotionEffectType.INCREASE_DAMAGE.getName(), "&c力量");
        effectMap.put(PotionEffectType.BLINDNESS.getName(), "&7失明");
        effectMap.put(PotionEffectType.WEAKNESS.getName(), "&4虚弱");

        for (CustomPitEffectType eff : CustomPitEffectType.values()) {
            effectMap.put(eff.name(), eff.getDisplayName());
        }

        return effectMap.getOrDefault(effect, "&c未知(" + effect.toUpperCase() + ")");
    }

}
