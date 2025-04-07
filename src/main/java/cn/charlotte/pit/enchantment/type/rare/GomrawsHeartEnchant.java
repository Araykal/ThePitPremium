package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/23 13:53
 */
@ArmorOnly
public class GomrawsHeartEnchant extends AbstractEnchantment implements ITickTask {

    private static final HashMap<UUID, Boolean> inCombat = new HashMap<>();

    @Override
    public String getEnchantName() {
        return "快速愈合";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "gomraws_heart";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7脱离战斗状态期间恢复自身全部生命值" + (enchantLevel >= 2 ?
                "/s&7且进入战斗状态后获得 &c生命恢复 IV &f(00:0" + (enchantLevel - 1) + ")" : "");
    }

    @Override
    public void handle(int enchantLevel, Player player) {
        if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        inCombat.putIfAbsent(player.getUniqueId(), true);
        if (profile.getCombatTimer().hasExpired()) {
            player.setHealth(player.getMaxHealth());
            inCombat.put(player.getUniqueId(), false);
        } else {
            if (!inCombat.get(player.getUniqueId())) {
                inCombat.put(player.getUniqueId(), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, (enchantLevel - 1) * 20, 3), true);
            }
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20;
    }
}
