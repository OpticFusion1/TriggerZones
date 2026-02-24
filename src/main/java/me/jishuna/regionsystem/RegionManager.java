package me.jishuna.regionsystem;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class RegionManager {

    private static final Set<Region> EMPTY_SET = Collections.emptySet();
    private final Long2ObjectMap<Set<Region>> regionMap = new Long2ObjectOpenHashMap<>();

    public void addRegion(Region region) {
        int minX = region.getBounds().getMin().getBlockX() >> 4;
        int maxX = region.getBounds().getMax().getBlockX() >> 4;
        int minZ = region.getBounds().getMin().getBlockZ() >> 4;
        int maxZ = region.getBounds().getMax().getBlockZ() >> 4;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                long key = Utils.getChunkKey(x, z);
                regionMap.computeIfAbsent(key, k -> new HashSet<>()).add(region);
            }
        }
    }

    public void removeRegion(Region region) {
        int minX = region.getBounds().getMin().getBlockX() >> 4;
        int maxX = region.getBounds().getMax().getBlockX() >> 4;
        int minZ = region.getBounds().getMin().getBlockZ() >> 4;
        int maxZ = region.getBounds().getMax().getBlockZ() >> 4;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                long key = Utils.getChunkKey(x, z);
                Set<Region> regionSet = regionMap.get(key);
                if (regionSet != null) {
                    regionSet.remove(region);
                }
            }
        }
    }

    public Set<Region> getRegions(Chunk chunk) {
        return regionMap.getOrDefault(Utils.getChunkKey(chunk), EMPTY_SET);
    }

    public Set<Region> getRegions(int x, int z) {
        return regionMap.getOrDefault(Utils.getChunkKey(x, z), EMPTY_SET);
    }

    public Set<Region> getRegionsForLocation(Location location) {
        Vector position = location.toVector();
        Set<Region> regions = new HashSet<>();
        for (Region region : getRegions(location.getChunk())) {
            if (region.isInSameWorld(location) && region.getBounds().contains(position)) {
                regions.add(region);
            }
        }
        return regions;
    }

    public Set<Region> getRegionsForLocation(double x, double y, double z) {
        int chunkX = NumberConversions.floor(x) >> 4;
        int chunkZ = NumberConversions.floor(z) >> 4;
        Set<Region> regions = new HashSet<>();
        for (Region region : getRegions(chunkX, chunkZ)) {
            if (region.getBounds().contains(x, y, z)) {
                regions.add(region);
            }
        }
        return regions;
    }
}
