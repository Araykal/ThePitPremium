package cn.charlotte.pit.perk.type.prestige;

import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.buff.impl.BountySolventBuff;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.event.PlayerOnly;
import cn.charlotte.pit.parm.listener.IPlayerDamaged;
import cn.charlotte.pit.perk.AbstractPerk;
import cn.charlotte.pit.perk.Passive;
import cn.charlotte.pit.perk.PerkType;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Creator Misoryan
 * @Date 2021/5/14 16:26
 */
@Passive
public class BountySolventShopPerk extends AbstractPerk implements IPlayerDamaged {

    private static final BountySolventBuff buff = new BountySolventBuff();

    @Override
    public String getInternalPerkName() {
        return "bounty_solvent_shop_perk";
    }

    @Override
    public String getDisplayName() {
        return "商店升级: 赏金溶剂";
    }

    @Override
    public Material getIcon() {
        return Material.POTION;
    }

    @Override
    public double requireCoins() {
        return 0;//575
    }

    @Override
    public double requireRenown(int level) {
        return 10;
    }

    @Override
    public int requirePrestige() {
        return 4;
    }

    @Override
    public int requireLevel() {
        return 0;
    }

    @Override
    public List<String> getDescription(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7允许你在商店中购买赏金溶剂.");
        lines.add("");
        lines.add("&6赏金溶剂 (01:00)");
        lines.addAll(Arrays.asList("&7受到来自被悬赏 &6&l1000g &7以上的玩家攻击",
                "&7受到的伤害 &9-30%",
                "",
                "&7获得赏金时赏金的获取量 &6+50%"));
        return lines;
    }

    @Override
    public int getMaxLevel() {
        return 0;
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
    @PlayerOnly
    public void handlePlayerDamaged(int enchantLevel, Player myself, Entity attacker, double damage, AtomicDouble finalDamage, AtomicDouble boostDamage, AtomicBoolean cancel) {
        if (UtilKt.hasRealMan(myself)) return;
        if (PlayerProfile.getPlayerProfileByUuid(attacker.getUniqueId()).getBounty() > 1000) {
            if (buff.getPlayerBuffData(myself).getTier() > 0) {
                boostDamage.getAndAdd(-0.3);
            }
        }
    }
}
