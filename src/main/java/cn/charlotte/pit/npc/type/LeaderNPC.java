package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.menu.leaderboard.LeaderBoardMenu;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LeaderNPC extends SkinNPC{
    @Override
    public String getNpcInternalName() {
        return "leaderboard";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&e&l排行榜助手");
        lines.add("&e&l右键打开");
        return lines;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getLeaderBoardNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin("ewogICJ0aW1lc3RhbXAiIDogMTczNTA1Njk4MDYwNiwKICAicHJvZmlsZUlkIiA6ICJiODU0NWMxMDlhZjE0ZGRjYmY4ZjhmZjg4ZTU2NzI4OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJQdGFrb3B5c2tDWiIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hNGY1OTM4ZWVhNzJmN2E2YWFlMmRhYWE1NjVkMjY4Y2MwY2U0MjhhMGJiMjI5MDFhZDZjODE3MDEzZTM5MGNhIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=","lftzbKQXTBx/onJKbUxg8YZR+kfaspzAwC1CoFHRsmer1V95DAN8eLLzqb/yB6QaPvCkYmA2x7Ipn3/dTK1KM1kTYuxRfDCMIlm8PZtagk7g2qY5zYzANYujyUGlqfv6yZtBewwus2bxQod9EP/T64nHStzxn8hzPlKziEhih12VCtfdw+Uz/TtOt6itJFmi/CoPXVRmN7LJhIxeCkDcoQE4VUqErJakcyY9ikPfvqHLaxbIl2Hdtsf6NYPHOloD1VZhn2D1suA+eCJszI3vmZ/nsU11j3lEMUJyS+LyROjXkMfd8zKiDcjGQypxfs+s0GvKP3HQdfwozwzc2/gkOQw6Bfz3MMOefilikfm1m5ooWjDI0vcPRGpWXKtjHIqK42soQ6O5wDFI2Tmgp8RGvmkvT4Mj4eDuZKfdMQOja6GLdTn7jzsKgMnn587WYFx+MAggPgr5pOr+nDf98Q7FMSZFxvY4oduB3IG7e/1S9wK0iqc1SB51TRF751hmX5P6i8JUXwVOT5lct//EuXFsN1P2kxMsoK4dWbwRAmYLHBZGoOI+nB9hjB5YxukQnfXTIjp71tAv79g6yIuzNzYGGtJojkVxI9zl5X6Nn7kZtkV8i4vWA6xDzWfZ3sgW9hC/Rlhl8dJGEyoPDnpEVvH80blVlLL0nz6hWQtZsjKPV+E=");
    }

    @Override
    public void handlePlayerInteract(Player player) {
        new LeaderBoardMenu().openMenu(player);
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return null;
    }
}
