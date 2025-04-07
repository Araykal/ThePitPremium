package cn.charlotte.pit.util.command.util;

import cn.charlotte.pit.util.command.CommandHandler;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;

public class ItemUtil {

    private static final Map<String, ItemData> NAME_MAP = new HashMap<>();

    public static ItemData[] repeat(final Material material, final int times) {
        return repeat(material, (byte) 0, times);
    }

    public static ItemData[] repeat(final Material material, final byte data, final int times) {
        final ItemData[] itemData = new ItemData[times];

        for (int i = 0; i < times; i++) {
            itemData[i] = new ItemData(material, data);
        }

        return itemData;

    }

    public static ItemData[] armorOf(final ArmorPart part) {
        final List<ItemData> data = new ArrayList<>();

        for (final ArmorType at : ArmorType.values()) {
            data.add(new ItemData(Material.valueOf(at.name() + "_" + part.name()), (short) 0));
        }

        return data.toArray(new ItemData[0]);
    }

    public static ItemData[] swords() {
        final List<ItemData> data = new ArrayList<>();

        for (final SwordType at : SwordType.values()) {
            data.add(new ItemData(Material.valueOf(at.name() + "_SWORD"), (short) 0));
        }

        return data.toArray(new ItemData[0]);
    }

    public static void load() {
        NAME_MAP.clear();

        final List<String> lines = readLines();

        for (final String line : Objects.requireNonNull(lines)) {
            final String[] parts = line.split(",");

            NAME_MAP.put(parts[0],
                    new ItemData(Material.getMaterial(Integer.parseInt(parts[1])), Short.parseShort(parts[2])));
        }
    }

    public static ItemStack get(final String input, final int amount) {
        final ItemStack item = get(input);

        if (item != null) {
            item.setAmount(amount);
        }

        return item;
    }

    public static ItemStack get(final String input) {
        if (NumberUtil.isInteger(input)) {
            return new ItemStack(Material.getMaterial(Integer.parseInt(input)));
        }

        if (input.contains(":")) {
            if (NumberUtil.isShort(input.split(":")[1])) {
                if (NumberUtil.isInteger(input.split(":")[0])) {
                    return new ItemStack(Material.getMaterial(Integer.parseInt(input.split(":")[0])), 1,
                            Short.parseShort(input.split(":")[1]));
                } else {
                    if (!NAME_MAP.containsKey(input.split(":")[0].toLowerCase())) {
                        return null;
                    }

                    final ItemData data = NAME_MAP.get(input.split(":")[0].toLowerCase());
                    return new ItemStack(data.getMaterial(), 1, Short.parseShort(input.split(":")[1]));
                }
            } else {
                return null;
            }
        }

        if (!NAME_MAP.containsKey(input)) {
            return null;
        }

        return NAME_MAP.get(input).toItemStack();
    }

    public static String getName(final ItemStack item) {
        if (item.getDurability() != 0) {
            String reflectedName = BukkitReflection.getItemStackName(item);

            if (reflectedName != null) {
                if (reflectedName.contains(".")) {
                    reflectedName = WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " "));
                }

                return reflectedName;
            }
        }

        final String string = item.getType().toString().replace("_", " ");
        final char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;

        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
                found = false;
            }
        }

        return String.valueOf(chars);
    }

    private static List<String> readLines() {
        try {
            return IOUtils.readLines(Objects.requireNonNull(CommandHandler.PLUGIN.getClass().getClassLoader().getResourceAsStream("items.csv")));
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public enum ArmorPart {
        HELMET, CHESTPLATE, LEGGINGS, BOOTS
    }

    public enum ArmorType {
        DIAMOND, IRON, GOLD, LEATHER
    }

    public enum SwordType {
        DIAMOND, IRON, GOLD, STONE
    }

    public static class ItemData {

        private final Material material;
        private final short data;

        public ItemData(Material material, short data) {
            this.material = material;
            this.data = data;
        }

        public String getName() {
            return ItemUtil.getName(toItemStack());
        }

        public boolean matches(final ItemStack item) {
            return item != null && item.getType() == material && item.getDurability() == data;
        }

        public ItemStack toItemStack() {
            return new ItemStack(material, 1, data);
        }

        public Material getMaterial() {
            return this.material;
        }

        public short getData() {
            return this.data;
        }
    }

}
