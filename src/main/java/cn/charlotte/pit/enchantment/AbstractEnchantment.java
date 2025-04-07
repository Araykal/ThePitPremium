package cn.charlotte.pit.enchantment;

import cn.charlotte.pit.UtilKt;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.enchantment.param.item.ArmorOnly;
import cn.charlotte.pit.enchantment.param.item.BowOnly;
import cn.charlotte.pit.enchantment.param.item.RodOnly;
import cn.charlotte.pit.enchantment.param.item.WeaponOnly;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.util.cooldown.Cooldown;
import cn.charlotte.pit.util.time.TimeUtil;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/12/28 23:21
 * 4
 */

@Getter
public abstract class AbstractEnchantment {

    public abstract String getEnchantName();

    public abstract int getMaxEnchantLevel();

    public abstract String getNbtName();

    //Todo: 对重制后的附魔系统的所有附魔稀有度分级
    public abstract EnchantmentRarity getRarity();

    //Todo: 附魔触发后的冷却时间
    @Nullable
    public abstract Cooldown getCooldown();

    //填写此玩家触发附魔的所需冷却时间
    public String getCooldownActionText(Cooldown cooldown) {
        return (cooldown.hasExpired() ? "&a&l✔" : "&c&l" + TimeUtil.millisToRoundedTime(cooldown.getRemaining()).replace(" ", ""));
    }

    //填写每x次攻击触发
    public String getHitActionText(Player player, int activeHit) {
        int hit = (player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOW ? PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getBowHit() : PlayerProfile.getPlayerProfileByUuid(player.getUniqueId()).getMeleeHit());
        return (hit % activeHit == 0 ? "&a&l✔" : "&e&l" + (activeHit - hit % activeHit));
    }

    //Todo: 需要一个判断玩家身上附魔是否生效中(持续时间内)的方法 (虽然也许不应该写在这里)

    public int getItemEnchantLevel(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return -1;
        }
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        if (nmsItem == null) {
            return -1;
        }
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            return -1;
        }
        if (!tag.hasKey("extra") || !tag.getCompound("extra").hasKey("ench")) {
            return -1;
        }
        NBTTagList list = tag.getCompound("extra")
                .getList("ench", 8);

        if (list == null || list.size() == 0) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            String string = list.getString(i);
            String[] split = string.split(":");
            if (split.length != 2) {
                return -1;
            }
            if (split[0].equals(this.getNbtName())) {
                return Integer.parseInt(split[1]);
            }
        }
        return -1;
    }

    protected boolean weaponOnly = this.getClass().isAnnotationPresent(WeaponOnly.class);
    protected boolean bowOnly = this.getClass().isAnnotationPresent(BowOnly.class);
    protected boolean armorOnly = this.getClass().isAnnotationPresent(ArmorOnly.class);
    protected boolean rodOnly = this.getClass().isAnnotationPresent(RodOnly.class);


    public boolean canApply(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(item);
        if (nmsItem == null) return false;
        Item itemType = nmsItem.getItem();
        if (itemType instanceof ItemSword) {
            return weaponOnly;
        }
        if (itemType instanceof ItemBow) {
            return bowOnly;
        }
        if (itemType instanceof ItemArmor) {
            return armorOnly;
        }
        if (itemType instanceof ItemFishingRod) {
            return rodOnly;
        }
        return false;
    }

    public boolean isItemHasEnchant(ItemStack itemStack) {
        return this.getItemEnchantLevel(itemStack) != -1;
    }


    public abstract String getUsefulnessLore(int enchantLevel);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEnchantment that = (AbstractEnchantment) o;
        return that.getNbtName().equals(this.getNbtName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getNbtName());
    }
}
