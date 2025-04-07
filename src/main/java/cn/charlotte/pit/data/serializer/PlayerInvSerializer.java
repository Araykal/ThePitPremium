package cn.charlotte.pit.data.serializer;

import cn.charlotte.pit.data.sub.PlayerInv;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/8 18:24
 */
public class PlayerInvSerializer extends JsonSerializer<PlayerInv> {
    @Override
    public void serialize(PlayerInv value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            gen.writeStartObject();
            gen.writeStringField("inv", InventoryUtil.playerInvToString(value));
            gen.writeEndObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
