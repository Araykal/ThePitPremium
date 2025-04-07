package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.parm.listener.ITickTask;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.Passive;
import cn.charlotte.pit.perk.PerkType;
import cn.charlotte.pit.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/21 18:55
 */
@Passive
public class ImpatientPerk extends AbstractPerk implements ITickTask {
    @Override
    public String getInternalPerkName() {
        return "impatient";
    }

    @Override
    public String getDisplayName() {
        return "急不可待";
    }

    @Override
    public Material getIcon() {
        return Material.CARROT_ITEM;
    }

    @Override
    public double requireCoins() {
        return 0;
    }

    @Override
    public double requireRenown(int level) {
        return 10;
    }

    @Override
    public int requirePrestige() {
        return 3;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        return Collections.singletonList("&7在保护区时获得永久 &b速度 II &7效果.");
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public PerkType getPerkType() {
        return PerkType.PERK;
    }

    @Override
    public void onPerkActive(Player player) {

    }

    @Override
    public void onPerkInactive(Player player) {

    }

    @Override
    public void handle(int enchantLevel, Player player) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        if (profile.isInArena()) {
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                if (potionEffect.getType().getName().equalsIgnoreCase("speed")) {
                    //不在主城且拥有效果时
                    if (potionEffect.getDuration() > 8640 * 20 && potionEffect.getAmplifier() >= 1) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        return;
                    }
                }
            }
            //----------------------------------
        } else {
            try {
                for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                    if (potionEffect.getType().getName().equalsIgnoreCase("speed")) {
                        //拥有速度效果且时间小于8640秒时给予60min speed2
                        if (potionEffect.getDuration() < 8640 * 20) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 86400 * 20, 1), true);
                            return;
                        }
                    }
                }
                //没有speed且在主城时补充效果
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 86400 * 20, 1), true);
            } catch (Exception e) {
                CC.printError(player, e);
            }
        }
    }

    @Override
    public int loopTick(int enchantLevel) {
        return 20 * 3;
    }
}
