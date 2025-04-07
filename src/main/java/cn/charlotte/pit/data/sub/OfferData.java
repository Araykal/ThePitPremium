package cn.charlotte.pit.data.sub;

import cn.charlotte.pit.util.inventory.InventoryUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @Creator Misoryan
 * @Date 2021/5/29 10:15
 */
@Data
public class OfferData {

    private String buyer;
    //use InventoryUtil to serialize
    private String itemStack;
    private double price;
    private long endTime;

    @JsonIgnore
    public UUID getBuyer() {
        if (buyer == null) return null;
        return UUID.fromString(buyer);
    }

    @JsonIgnore
    public void setBuyer(UUID uuid) {
        buyer = uuid.toString();
    }

    @JsonIgnore
    public ItemStack getItemStack() {
        return InventoryUtil.deserializeItemStack(itemStack);
    }

    @JsonIgnore
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = InventoryUtil.serializeItemStack(itemStack);
    }

    @JsonIgnore
    public boolean hasActiveOffer() {
        return buyer != null && System.currentTimeMillis() < endTime;
    }

    @JsonIgnore
    public boolean hasUnclaimedOffer() {
        return buyer != null && System.currentTimeMillis() >= endTime;
    }

    @JsonIgnore
    public void createOffer(UUID target, ItemStack itemStack, double price, long endTime) {
        if (hasActiveOffer() || hasUnclaimedOffer()) return;
        this.buyer = target.toString();
        setItemStack(itemStack);
        this.price = price;
        this.endTime = endTime;
    }

    @JsonIgnore
    public void createOffer(UUID target, ItemStack itemStack, double price) {
        createOffer(target, itemStack, price, System.currentTimeMillis() + 60 * 1000L);
    }
}
