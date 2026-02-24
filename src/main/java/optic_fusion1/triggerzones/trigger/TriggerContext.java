package optic_fusion1.triggerzones.trigger;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TriggerContext {

    private TriggerZone zone;
    private TriggerZone.TriggerEvent event;
    private final Player player;
    private final Location from;
    private final Location to;

    public TriggerContext(TriggerZone zone, TriggerZone.TriggerEvent event, Player player, Location from, Location to) {
        this.zone = zone;
        this.event = event;
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public TriggerZone getZone() {
        return zone;
    }

    public TriggerZone.TriggerEvent getEvent() {
        return event;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

}
