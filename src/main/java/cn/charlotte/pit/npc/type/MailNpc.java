package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerMailData;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.mail.Mail;
import cn.charlotte.pit.menu.mail.MailMenu;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/25 19:03
 */
public class MailNpc extends SkinNPC {
    @Override
    public String getNpcInternalName() {
        return "mail";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> hologram = new ArrayList<>();
        final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
        final PlayerMailData mailData = profile.getMailData();

        int unread = 0;
        for (Mail mail : mailData.getMails()) {
            if (!mail.isClaimed()) {
                unread++;
            }
        }
        hologram.add("&e&l邮件");
        if (unread > 0) {
            hologram.add((System.currentTimeMillis() % 2 == 0 ? "&a" : "&2") + "您有 " + unread + " 封未读邮件");
        }

        return hologram;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getMailNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin(
                "ewogICJ0aW1lc3RhbXAiIDogMTcyNDQ5NTM1NjIzMCwKICAicHJvZmlsZUlkIiA6ICIxM2YxYjU3ZDRmN2U0MDRjYTQ0Y2I0MTE2YTIyNjIxMCIsCiAgInByb2ZpbGVOYW1lIiA6ICJUaGVDdXBDdXAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY0ZDYyM2QzMGMyNzhlMmVjNDRjYmM2ZGNlNjc4MDI2OGQ1MDUyNmQ2YTQ5NmQyNjZiMjFlOTBlZjEwN2RjYSIKICAgIH0KICB9Cn0=",
                null
        );
    }

    @Override
    public void handlePlayerInteract(Player player) {
        new MailMenu().openMenu(player);
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return new ItemStack(Material.CHEST);
    }
}
