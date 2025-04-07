package cn.charlotte.pit.data.deserializer;

import cn.charlotte.pit.data.sub.PlayerEnderChest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * @Author: EmptyIrony
 * @Date: 2021/1/8 18:35
 */
public class EnderChestDeserializer extends JsonDeserializer<PlayerEnderChest> {
    @Override
    public PlayerEnderChest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            JsonNode node = p.getCodec().readTree(p);
            String content = node.get("content").asText();
            return PlayerEnderChest.deserialization(content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
