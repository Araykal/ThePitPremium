package cn.charlotte.pit.menu.viewer.button;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.chat.RomanUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Misoryan
 * @Created_In: 2021/1/12 16:34
 */
public class PitPassiveStatusButton extends Button {
    private final PlayerProfile profile;

    public PitPassiveStatusButton(PlayerProfile profile) {
        this.profile = profile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lores = new ArrayList<>();
        lores.add("&7经验获取提升: &e" + (profile.getChosePerk().get(-1) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-1).getLevel())));
        lores.add("&7硬币获取提升: &e" + (profile.getChosePerk().get(-2) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-2).getLevel())));
        lores.add("&7近战伤害提升: &e" + (profile.getChosePerk().get(-3) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-3).getLevel())));
        lores.add("&7远程伤害提升: &e" + (profile.getChosePerk().get(-4) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-4).getLevel())));
        lores.add("&7伤害减免: &e" + (profile.getChosePerk().get(-5) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-5).getLevel())));
        lores.add("&7方块存在时间提升: &e" + (profile.getChosePerk().get(-6) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-6).getLevel())));
        lores.add("&7先发制人: &e" + (profile.getChosePerk().get(-7) == null ? "&c未解锁" : RomanUtil.convert(profile.getChosePerk().get(-7).getLevel())));
        if (!profile.getPlayerOption().isProfileVisibility() && !PlayerUtil.isStaff(player)) {
            lores.clear();
            lores.add("&c此玩家隐藏了他的档案信息.");
        }
        return new ItemBuilder(Material.CAKE).name("&a被动天赋").lore(lores).build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {

    }
}
