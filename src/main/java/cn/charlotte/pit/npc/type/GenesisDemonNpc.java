package cn.charlotte.pit.npc.type;

import cn.charlotte.pit.ThePit;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yurinan
 * @since 2022/3/6 7:45
 */

public class GenesisDemonNpc extends SkinNPC {

    @Override
    public String getNpcInternalName() {
        return "GenesisDemon";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&e限时活动: 光暗派系");
        lines.add("&c&l恶魔");
        lines.add("&e&l右键查看");
        return lines;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getGenesisDemonNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNzU3NTA1NDA5NSwKICAicHJvZmlsZUlkIiA6ICJhYWMxYjA2OWNkMjE0NWE2ODNlNzQxNzE4MDcxMGU4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJqdXNhbXUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjg3ZmYwNGQxNWU3MGZhZDhiZGQ5ODMyYTY1MzU4MWQwNGE4NDhkMzViN2VmZDUyOTliMmUwN2U1MTJlYjYxNCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                "X1PUef605bNN+jGzyrypGESH2J5J+sJw/G/uHSQ3OtLYpsIOCXOKStKgSCSbd92SXna+xMyTqLfWEJebyb253rcTavM8IAb+gOWETEzEt2qOyKygKnPVf/K91kD6GFO5pfjovrpdhV1Zyg6Yvii3J4g9WkDwBxARcNC1DiznV1Usn+QFn4MODX/bO6VGLmzewYcZRb3/ApA3Ul9UaRgJ5lmAmU4bgpROWscsd1HtVm8wIDqbplvZB2ciLBFkmJ0ogriuJPWotkRxQuWhm6dB5SWQUSLxTXF5HdW/68BQtwFdXXX8GgB+hnbUHWGlXm3M1LZh3YTPAHbVufmPeASt0RQSU4SBzfkhgTmNHhpSMmB2WZyAlbZ4xm3i7iI7268v/UBttTMAZ2nGWvTaS0s33Lng3NNuGdFHMYqMBGgZ/2ltw3M/QMCHEWaNfF0x4Y+rhZ+A8925H6VBaysTgS+oqfMsxrRbm/Yeh3piHBDKhDSUDeTcI/K47FE960lX4qDZuH83T3wuq/IR5jChUNrJZiGEscRPD6h3uVAgxQK629hxKeVs+jru0kXAJO+Z+SWKVJ6lwBGFw2h5nkwdh1griiLjLTC52g2zZIgS3JwxuPRu1G4Qu0qJ+7X7n4Z7ivJhRVN8twy4696aXIRELj4P+ThiAsWtsIQJvTSM88cb+HU="
        );
    }

    @Override
    public void handlePlayerInteract(Player player) {
        ThePit.getApi().openDemonMenu(player);
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return null;
    }

}
