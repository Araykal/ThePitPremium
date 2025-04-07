package cn.charlotte.pit.data.serializer;

import cn.charlotte.pit.data.sub.PlayerEnderChest;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/8 18:32
 */
public class EnderChestSerializer extends JsonSerializer<PlayerEnderChest> {
    @Override
    public void serialize(PlayerEnderChest value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            Inventory inventory = value.getInventory();

            int limit = 6 * 9;

            ItemStack[] itemStacks = new ItemStack[limit];
            for (int i = 0; i < limit; i++) {
                ItemStack item = inventory.getItem(i);
                if ("not_unlock_slot".equals(ItemUtil.getInternalName(item))) {
                    continue;
                }
                itemStacks[i] = item;
            }

            String content = InventoryUtil.itemsToString(itemStacks);

            gen.writeStartObject();
            gen.writeStringField("content", content);
            gen.writeEndObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
