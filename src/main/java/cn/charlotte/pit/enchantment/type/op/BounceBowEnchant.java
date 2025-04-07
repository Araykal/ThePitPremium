package cn.charlotte.pit.enchantment.type.op;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.enchantment.AbstractEnchantment;
import cn.charlotte.pit.enchantment.rarity.EnchantmentRarity;
import cn.charlotte.pit.parm.AutoRegister;
import cn.charlotte.pit.util.PlayerUtil;
import cn.charlotte.pit.util.cooldown.Cooldown;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

/**
 * @Creator Misoryan
 * @Date 2021/6/19 12:50
 */
@AutoRegister
public class BounceBowEnchant extends AbstractEnchantment implements Listener {
    @Override
    public String getEnchantName() {
        return "弹跳箭矢";
    }

    @Override
    public int getMaxEnchantLevel() {
        return 1;
    }

    @Override
    public String getNbtName() {
        return "bounce_bow_enchant";
    }

    @Override
    public EnchantmentRarity getRarity() {
        return EnchantmentRarity.NOSTALGIA_RARE;
    }

    @Override
    public Cooldown getCooldown() {
        return null;
    }

    @Override
    public String getUsefulnessLore(int enchantLevel) {
        return "&7箭矢碰撞到墙壁时会进行反弹(至多4次),"
                + "/s&7每次反弹使此箭矢造成的伤害 &c+25%";
    }

    @EventHandler
    public void onBowShot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
        final org.bukkit.inventory.ItemStack itemInHand = player.getItemInHand();
        if (itemInHand == null) return;
        final int level = this.getItemEnchantLevel(itemInHand);
        if (level == -1) {
            return;
        }
        if (!player.isSneaking()) return;
        if (itemInHand.getType() == Material.BOW) {
            event.getProjectile().setMetadata("bounce_bow", new FixedMetadataValue(ThePit.getInstance(), true));
            //event.setProjectile(projectile);
        }
    }

    @EventHandler
    public void onBowHit(ProjectileHitEvent event) {
        if (event.getEntity().hasMetadata("bounce_bow") && event.getEntity().getShooter() != null) {
            if (event.getEntity().getShooter() instanceof Player) {
                Player player = (Player) event.getEntity().getShooter();
                //if (PlayerUtil.isVenom(player) || PlayerUtil.isEquippingSomber(player)) return;
                event.getEntity().setBounce(true);
                if (event.getEntity().getLocation().getBlock() == null || event.getEntity().getLocation().getBlock().getType() == Material.AIR)
                    return;
                Block hitBlock = event.getEntity().getLocation().getBlock();
                Vector bounceVector = getBounceVector(event.getEntity(), hitBlock);
                Projectile projectile = (Projectile) player.getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.ARROW);
                event.getEntity().remove();
                projectile.setShooter(player);
                projectile.setVelocity(bounceVector);
            }
        }
    }

    public Vector getBounceVector(Entity entity, Block hitBlock) {
        double min = 0.5;
        double dec = 0.6;
        Vector vector = entity.getVelocity();
        double b1 = Math.sqrt(Math.pow(vector.getX(), 2)) + Math.pow(vector.getY(), 2) + Math.pow(vector.getZ(), 2);
        if (b1 < min) return null;
        Location location = entity.getLocation();
        BlockIterator blockIterator = new BlockIterator(location.getWorld(), location.toVector(), vector, 0.0, 3);
        Block blockPrevious = hitBlock;
        Block block = blockIterator.next();
        while (blockIterator.hasNext() && !block.getType().isSolid()) {
            blockPrevious = block;
            block = blockIterator.next();
        }
        BlockFace blockFace = block.getFace(blockPrevious);
        if (blockFace == null) return null;
        if (blockFace == BlockFace.SELF) {
            blockFace = BlockFace.UP;
        }
        Vector v1 = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
        Vector v2 = v1.multiply(vector.dot(v1)).multiply(2.0);
        return vector.subtract(v2).normalize().multiply(b1 * dec);
    }

}
