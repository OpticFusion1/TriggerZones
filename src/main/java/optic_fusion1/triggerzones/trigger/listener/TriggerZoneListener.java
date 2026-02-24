package optic_fusion1.triggerzones.trigger.listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import me.jishuna.regionsystem.Region;
import me.jishuna.regionsystem.RegionManager;
import optic_fusion1.triggerzones.trigger.TriggerContext;
import optic_fusion1.triggerzones.trigger.TriggerZone;
import optic_fusion1.triggerzones.trigger.TriggerZone.TriggerEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TriggerZoneListener implements Listener {

    private Map<UUID, Set<TriggerZone>> playerCurrentZones = new HashMap<>();
    private RegionManager regionManager;

    public TriggerZoneListener(RegionManager regionManager) {
        this.regionManager = regionManager;
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
            return;
        }
        UUID playerId = player.getUniqueId();
        Set<TriggerZone> previousZones = playerCurrentZones.getOrDefault(playerId, new HashSet<>());
        Set<TriggerZone> currentZones = new HashSet<>();
        for (Region region : regionManager.getRegionsForLocation(to)) {
            if (!(region instanceof TriggerZone zone)) {
                continue;
            }
            TriggerContext ctx = new TriggerContext(zone, null, player, from, to);
            if (zone.allConditionsMet(TriggerEvent.ENTER, ctx)) {
                currentZones.add(zone);
            }
        }
        // TODO: Could probably be merged with the above loop
        for (TriggerZone zone : currentZones) {
            if (!previousZones.contains(zone)) {
                TriggerContext ctx = new TriggerContext(zone, TriggerZone.TriggerEvent.ENTER, player, from, to);
                zone.onEnter(ctx);
            }
        }
        for (TriggerZone zone : previousZones) {
            if (currentZones.contains(zone)) {
                continue;
            }
            TriggerContext ctx = new TriggerContext(zone, TriggerZone.TriggerEvent.LEAVE, player, from, to);
            if (zone.allConditionsMet(TriggerZone.TriggerEvent.LEAVE, ctx)) {
                zone.onExit(ctx);
            }
        }
        playerCurrentZones.put(playerId, currentZones);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerCurrentZones.remove(event.getPlayer().getUniqueId());
    }

}
