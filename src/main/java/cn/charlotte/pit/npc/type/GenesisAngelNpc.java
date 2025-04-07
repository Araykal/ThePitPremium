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
 * @since 2022/3/5 16:53
 */

public class GenesisAngelNpc extends SkinNPC {

    @Override
    public String getNpcInternalName() {
        return "GenesisAngel";
    }

    @Override
    public List<String> getNpcDisplayName(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&e限时活动: 光暗派系");
        lines.add("&f&l天使");
        lines.add("&e&l右键查看");
        return lines;
    }

    @Override
    public Location getNpcSpawnLocation() {
        return ThePit.getInstance().getPitConfig().getGenesisAngelNpcLocation();
    }

    @Override
    public Skin getNpcSkin() {
        return new Skin(
                "ewogICJ0aW1lc3RhbXAiIDogMTczNzU3NDk4MTQ3NiwKICAicHJvZmlsZUlkIiA6ICJlZmI1ZWQ2YjVjOTU0ODBlYWFmMjAyZDIxOWVmNjBjNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaWtlSHdhazAwMSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83MzRhOWZhZmI4NmZhNjQ2YThkNjFiODYzYTcxYTYzNjNlYmRiODc3ZjRjNTk4MzdhNjFmNzk3Y2Y4MTBkYmJlIgogICAgfQogIH0KfQ==",
                "Pfe1Ja37pUtj9ZSkoLr2E1pHktsS30ortAV6AGnqtQ4bPCEwuj2XZf7oCHSr57qnGOytsiWjvnKi3NTsVBm+2Wr5b0pSEAFN8MAC+w23om0IKr1UE451xw3wuunG+UccwfA1zOCaaINbcDguqNgAqPQ/4lgPfV3DtXzug7ZxpYTMfaMlkN5o4FCVxmaJmCaoz4gIkKv3AEQsmHS0Ol9DiF1Nr9cl2+DCuHgx1a5Cg2jx1jEEJWCIi5lAfzuR7MH+dgOg3d/S9YOEKm8IoBGFf24xaNgg/L9CLjz1Koyqp/opfMwLeLZBM+MCyqBwmd8VKuM5p9FsOtYiKfE/6cujvDcDyCoy+npc6XUbCQqbAPRBBfPvKFLcbNBiIO/+yyT/DgpEVKUDrfWmTuToSAIJi3F+JpVSFZhAw6w/XzTjHFu/+f8nlULKtTzNv3+J1xRzqKQLs9HkAHQpvrqxVy+4bYAjiEYSd605VajkBzB0RUtrzOtJ9JtbgiediV9WIQGuT7byElhunSePmmVWQt/mnexQSPgdKUP36CivrB5r6TcQC7tvLZDIJQiJ07wbjcxBheZ0fztv2ZSpSBgNE6xMP0bMPOFvU0L7A82Xu6cgsIJ0mohaksH6IAirMFdH/4QNhz3e4AfVKRyoVcyuptgQR31Y6n3k5uetrS5lNYwRZf8="
        );
    }

    @Override
    public void handlePlayerInteract(Player player) {
        ThePit.getApi().openAngelMenu(player);
    }

    @Override
    public ItemStack getNpcHeldItem() {
        return null;
    }

}
