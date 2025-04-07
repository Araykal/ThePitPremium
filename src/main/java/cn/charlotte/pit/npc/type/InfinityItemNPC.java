package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.menu.admin.item.AdminItemMenu;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: EmptyIrony
 * @Date: 2021/4/21 21:58
 */
public class InfinityItemNPC extends SkinNPC {
    @Override
    public String getNpcInternalName() {
        return "infinity_item";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        return Arrays.asList(
                "&b大物品制造者",
                "&e右键查看"
        );
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getInfinityNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin("ewogICJ0aW1lc3RhbXAiIDogMTcyNzAzMTkwNTg3NywKICAicHJvZmlsZUlkIiA6ICJjN2Y2Nzk3ZmE4ZGM0NTdiYTkxNTU0NWIwMGU3M2UzMSIsCiAgInByb2ZpbGVOYW1lIiA6ICJlZGd5c3BlbmNlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2E5ZjI4ZGRlZTUzZmExYWU4ZmJmNGU3NGYzY2FkZWY3NTFhNmQxNzY5ZTMzMDFiNTAyNDU3N2E2ODYxOWJkZjEiCiAgICB9CiAgfQp9","GyZd7K/tzBVQgMKWBIHrl1thI5JBMSjKznc5sTfam9pu0zLY5DjnJzvpFJyk4q0iJFMqWjJ+/mL0tjoLIw30oFDOJ39ZnbCJBVdNFWr4Eg4v452fGWxZySnyh/jnc/bE58g1zOOjzkpgo4BxNRZoQR4vyb9rUvPAZca+D1cs9AHrJBjttuq5gtChuKnZh9x9iTnijNUdl9Tahw5hfJZMPAYd5RSknIE8ZjatUQVZnKPONGe3BuM5kpz3tX8e1d+4+/dFZDqjODnOmtWUIr6NHoSZlcT2sCThdvSfapPKbAqyhMpJVsbIUoiCWC3mgu8J7nMt9zwqPEZYCLLm3BVl093Bh0wSWu64q8AKRVpBXgvURDdP7EAHjF56WcmipaEO8yMMf+fAH60w93dktjxemwMznut/z6Q015gq/jbpGQzQYLmSvNBhJK/vK8q3HY4llUS7x4sTN8LOWRbNP+JuVWQN+LIV2yXrl8thDgKt1Hq+gtt9mUz/fLSWtQFHOepA9ra+3zUIfGc5HwwaLE40g9FymS/h4ElbT3cafE6X9xvE3oZVxVEo4ZOU7pznLW1X6ZcjqxdLUdH3CURC0qi7+VmdP3J75lvZJerCnBl9PDfMQ3SwwtmBLPNwr17DvNVdS8MNbXVB5t5q78w1vT2e3EYpCQz3T/qel4LQawDx7Rg=");
    }

    @Override
    public void handlePlayerInteract(Player player) {
        if (ThePit.isDEBUG_SERVER()) {
            new AdminItemMenu(false).openMenu(player);
        }
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return new ItemStack(Material.DIAMOND_BLOCK);
    }
}
