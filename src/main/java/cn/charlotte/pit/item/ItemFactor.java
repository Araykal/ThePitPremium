package cn.charlotte.pit.item;

import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: EmptyIrony
 * @Date: 2020/12/31 12:57
 */
public class ItemFactor {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ItemFactor.class);
    private final Map<String, Class<? extends AbstractPitItem>> itemMap;

    public ItemFactor() {
        this.itemMap = new HashMap<>();
    }

    public void registerItem(AbstractPitItem item) {
        itemMap.put(item.getInternalName(), item.getClass());
    }

    public Map<String, Class<? extends AbstractPitItem>> getItemMap() {
        return this.itemMap;
    }
}
