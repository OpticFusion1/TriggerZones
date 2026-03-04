package me.jishuna.regionsystem;

import org.bukkit.util.BoundingBox;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;

public class Region {

    private BoundingBox bounds;
    private UUID worldId;

    public Region(Location minCorner, Location maxCorner) {
        bounds = normalizeBounds(minCorner, maxCorner);
        worldId = minCorner.getWorld().getUID();
    }

    public Region(World world, BoundingBox bounds) {
        this.worldId = world.getUID();
        this.bounds = bounds;
    }

    public void updateLocation(Location minCorner, Location maxCorner) {
        bounds = normalizeBounds(minCorner, maxCorner);
        worldId = minCorner.getWorld().getUID();
    }

    private BoundingBox normalizeBounds(Location firstCorner, Location secondCorner) {
        Objects.requireNonNull(firstCorner, "firstCorner cannot be null");
        Objects.requireNonNull(secondCorner, "secondCorner cannot be null");
        World firstWorld = firstCorner.getWorld();
        World secondWorld = secondCorner.getWorld();
        if (firstWorld == null || secondWorld == null || !firstWorld.getUID().equals(secondWorld.getUID())) {
            throw new IllegalArgumentException("Selection positions must be in the same world");
        }
        int minX = Math.min(firstCorner.getBlockX(), secondCorner.getBlockX());
        int minY = Math.min(firstCorner.getBlockY(), secondCorner.getBlockY());
        int minZ = Math.min(firstCorner.getBlockZ(), secondCorner.getBlockZ());
        int maxX = Math.max(firstCorner.getBlockX(), secondCorner.getBlockX()) + 1;
        int maxY = Math.max(firstCorner.getBlockY(), secondCorner.getBlockY()) + 1;
        int maxZ = Math.max(firstCorner.getBlockZ(), secondCorner.getBlockZ()) + 1;
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
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
