package cn.charlotte.pit.menu.cdk.view.button;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.CDKData;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.data.mail.Mail;
import cn.charlotte.pit.util.chat.CC;
import cn.charlotte.pit.util.inventory.InventoryUtil;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.menu.Button;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;

/**
 * @Author: EmptyIrony
 * @Date: 2021/3/27 19:37
 */
public class CDKButton extends Button {
    private static final Gson gson = new Gson();
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private final CDKData data;

    public CDKButton(CDKData data) {
        this.data = data;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        final long now = System.currentTimeMillis();

        return new ItemBuilder(Material.NAME_TAG)
                .name("&a" + data.getCdk())
                .lore(
                        "&b到期时间: &a" + (data.getExpireTime() < now ? "&c" : "&a") + format.format(data.getExpireTime()),
                        "&b限制等级: &a" + data.getLimitLevel(),
                        "&a限制权限: " + data.getLimitPermission(),
                        "&a限制领取: " + data.getLimitClaimed(),
                        "&a领取: " + data.getClaimedPlayers().size(),
                        "&a经验: " + data.getExp(),
                        "&a硬币: " + data.getCoins(),
                        "&a声望: " + data.getRenown(),
                        "&a物品: " + InventoryUtil.getInventoryFilledSlots(data.getItem().getContents()),
                        "",
                        "&e&l左键 查看",
                        "&c&l右键 删除"
                )
                .build();
    }

    @Override
    @SneakyThrows
    public void clicked(Player player, int slot, ClickType clickType, int hotbarButton, ItemStack currentItem) {
        if (clickType == ClickType.RIGHT) {
            CDKData.getCachedCDK().remove(data.getCdk());
            ThePit.getInstance()
                    .getMongoDB()
                    .getCdkCollection()
                    .deleteOne(Filters.eq("cdk", data.getCdk()));
            player.closeInventory();
            player.sendMessage(CC.translate("&c删除完成."));
            return;
        }
        if (clickType == ClickType.LEFT) {
            final StringBuilder builder = new StringBuilder("Expire: " + format.format(data.getExpireTime()) + "\n" +
                    "LimitLevel: " + data.getLimitLevel() + "\n" +
                    "LimitPerm: " + data.getLimitPermission() + "\n" +
                    "Exp: " + data.getExp() + "\n" +
                    "Coins: " + data.getCoins() + "\n" +
                    "Renown: " + data.getRenown() + "\n" +
                    "Items: " + InventoryUtil.getInventoryFilledSlots(data.getItem().getContents()) + "\n" +
                    "LimitClaimed: " + data.getLimitClaimed() + "\n" +
                    "Claimed: " + "\n");

            for (String claimedPlayer : data.getClaimedPlayers()) {
                builder.append(claimedPlayer)
                        .append("\n");
            }
            player.sendMessage(builder.toString());
        }

   /*     if (clickType == ClickType.SHIFT_LEFT) {
            final Mail mail = new Mail();
            mail.setExpireTime(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);
            mail.setCoins(data.getCoins());
            mail.setExp(data.getExp());
            mail.setRenown(data.getRenown());
            mail.setItem(data.getItem());
            mail.setSendTime(System.currentTimeMillis());
            mail.setTitle("&e【奖励】兑换码兑换奖励");
            mail.setContent("&f亲爱的玩家: 请查收通过兑换码获得的奖励");

            final PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
            profile.getMailData().sendMail(mail);
        }*/
    }
}
