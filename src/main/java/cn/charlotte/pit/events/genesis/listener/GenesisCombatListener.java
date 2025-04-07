package cn.charlotte.pit.events.genesis.listener;

import cn.charlotte.pit.ThePit;
import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.events.genesis.team.GenesisTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/4 22:26
 */
//@AutoRegister
public class GenesisCombatListener implements Listener {

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onCombat(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();

                PlayerProfile playerProfile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());
                PlayerProfile damagerProfile = PlayerProfile.getPlayerProfileByUuid(damager.getUniqueId());

                if (ThePit.getInstance().getPitConfig().isGenesisEnable() && damagerProfile.getGenesisData().getTeam() != GenesisTeam.NONE) {
                    //Tier 1 Check Start
                    if (damagerProfile.getGenesisData().getTier() >= 1) {
                        if (playerProfile.getGenesisData().getTeam() != GenesisTeam.NONE && playerProfile.getGenesisData().getTeam() != damagerProfile.getGenesisData().getTeam()) {
                            event.setDamage(event.getDamage() + 0);
                        }
                    }
                    //Tier 1 Check End
                    //Tier 4 Check Start
                    if (damagerProfile.getGenesisData().getTier() >= 4) {
                        if (damagerProfile.getGenesisData().getTeam() == GenesisTeam.ANGEL) {
                            for (ItemStack is : player.getInventory().getArmorContents()) {
                                if (is.getType().name().contains("LEATHER")) {
                                    event.setDamage(event.getDamage() + 0);
                                    break;
                                }
                            }
                        }
                        if (damagerProfile.getGenesisData().getTeam() == GenesisTeam.DEMON) {
                            for (ItemStack is : player.getInventory().getArmorContents()) {
                                if (is.getType().name().contains("DIAMOND")) {
                                    event.setDamage(event.getDamage() + 0);
                                    break;
                                }
                            }
                        }
                    }
                    //Tier 4 Check End
                }
            }
        }
    }

}
