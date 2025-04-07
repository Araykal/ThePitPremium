package cn.charlotte.pit.enchantment.type.rare;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.item.type.ChunkOfVileItem;
import cn.charlotte.pit.parm.listener.IPlayerKilledEntity;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.random.RandomUtil;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
@WeaponOnly
@ArmorOnly
public class MinersEnchant extends AbstractEnchantment implements IPlayerKilledEntity {
    @Override
    public String getEnchantName() {
        return "矿工";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 3;
    }

    @Override
    public String getNbtName() {
        return "miners_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.RARE;
    }

    @Nullable
    @Override
    public Cooldown getCooldown() {
        return null;
    }


    @Override
    public String getUsefulnessLore(int i) {
        return "&7每达到 &e1000 &7连杀有 &b" + ((i + 2) * 10) + "% &7的概率获得 &f1x &5暗聚块.";
    }

    @Override
    @PlayerOnly
    public void handlePlayerKilled(int enchantLevel, Player myself, Entity target, AtomicDouble coins, AtomicDouble experience) {
        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(myself.getUniqueId());
        if (profile.getStreakKills() % 1000 == 0) {
            double chance = 0.01 * (enchantLevel + 2) * 10;
            if (RandomUtil.hasSuccessfullyByChance(chance)) {
                myself.getInventory().addItem(ChunkOfVileItem.toItemStack());
                myself.sendMessage(CC.translate("&5&l矿工! &7你获得了 &f1x &5暗聚块."));
            } else {
                myself.sendMessage(CC.translate("&5&l矿工! &c看起来你的运气不太好! :("));
            }
        }
    }
}
