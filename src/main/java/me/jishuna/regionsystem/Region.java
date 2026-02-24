package me.jishuna.regionsystem;

import org.bukkit.util.BoundingBox;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;

public class Region {

    private final BoundingBox bounds;
    private final UUID worldId;

    public Region(Location minCorner, Location maxCorner) {
        bounds = new BoundingBox(
                minCorner.getX(), minCorner.getY(),
                minCorner.getZ(), maxCorner.getX(),
                maxCorner.getY(), maxCorner.getZ()
        );

        worldId = minCorner.getWorld().getUID();
    }

    public Region(World world, BoundingBox bounds) {
        this.worldId = world.getUID();
        this.bounds = bounds;
    }

    public boolean isInSameWorld(Location location) {
        return location.getWorld() != null && worldId.equals(location.getWorld().getUID());
    }

    public UUID getWorldId() {
        return worldId;
    }

    public BoundingBox getBounds() {
        return bounds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region region)) {
            return false;
        }
        return Objects.equals(bounds, region.bounds) && Objects.equals(worldId, region.worldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bounds, worldId);
    }
}
