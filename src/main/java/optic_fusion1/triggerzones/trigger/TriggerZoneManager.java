package optic_fusion1.triggerzones.trigger;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import me.jishuna.regionsystem.RegionManager;
import optic_fusion1.triggerzones.TriggerZones;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TriggerZoneManager {

    private TriggerZones plugin;
    private RegionManager regionManager;
    private Map<String, TriggerZone> zones = new HashMap<>();
    private File zonesFile;

    public TriggerZoneManager(TriggerZones plugin, RegionManager regionManager) {
        this.plugin = plugin;
        this.regionManager = regionManager;
        zonesFile = new File(plugin.getDataFolder(), "zones.yml");
    }

    public Collection<TriggerZone> getZones() {
        return Collections.unmodifiableCollection(zones.values());
    }

    public Optional<TriggerZone> getZone(String id) {
        return Optional.ofNullable(zones.get(id));
    }

    public void addZone(String zoneId, TriggerZone zone) {
        if (zones.putIfAbsent(zoneId, zone) == null) {
            regionManager.addRegion(zone);
        }
    }

    public void removeZone(String zoneId) {
        TriggerZone zone = zones.remove(zoneId.toLowerCase());
        if (zone == null) {
            return;
        }
        regionManager.removeRegion(zone);
    }

    public void loadZones() {
        zones.values().forEach(regionManager::removeRegion);
        zones.clear();
        if (!zonesFile.exists()) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(zonesFile);
        ConfigurationSection zonesSection = config.getConfigurationSection("zones");
        if (zonesSection == null) {
            return;
        }
        for (String zoneId : zonesSection.getKeys(false)) {
            ConfigurationSection section = zonesSection.getConfigurationSection(zoneId);
            if (section == null) {
                continue;
            }
            String worldIdValue = section.getString("world");
            if (worldIdValue == null) {
                continue;
            }
            World world;
            try {
                world = Bukkit.getWorld(UUID.fromString(worldIdValue));
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().log(Level.WARNING, "Invalid world UUID for zone ''{0}'': {1}", new Object[]{zoneId, worldIdValue});
                continue;
            }
            if (world == null) {
                plugin.getLogger().log(Level.WARNING, "Skipping zone ''{0}'' because world is not loaded", zoneId);
                continue;
            }
            Location min = new Location(world, section.getDouble("min.x"), section.getDouble("min.y"), section.getDouble("min.z"));
            Location max = new Location(world, section.getDouble("max.x"), section.getDouble("max.y"), section.getDouble("max.z"));
            TriggerZone zone = new TriggerZone(zoneId, min, max);
            zones.put(zoneId, zone);
            regionManager.addRegion(zone);
        }
        plugin.getLogger().log(Level.INFO, "Loaded {0} trigger zones from disk.", zones.size());
    }

    public void saveZones() {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
            plugin.getLogger().warning("Failed to create plugin data folder for zones.yml");
            return;
        }
        YamlConfiguration config = new YamlConfiguration();
        ConfigurationSection zonesSection = config.createSection("zones");
        for (TriggerZone zone : zones.values()) {
            ConfigurationSection section = zonesSection.createSection(zone.getId());
            section.set("world", zone.getWorldId().toString());
            section.set("min.x", zone.getBounds().getMinX());
            section.set("min.y", zone.getBounds().getMinY());
            section.set("min.z", zone.getBounds().getMinZ());
            section.set("max.x", zone.getBounds().getMaxX());
            section.set("max.y", zone.getBounds().getMaxY());
            section.set("max.z", zone.getBounds().getMaxZ());
        }
        try {
            config.save(zonesFile);
            plugin.getLogger().log(Level.INFO, "Saved {0} trigger zones to disk.", zones.size());
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save trigger zones", ex);
        }
    }

}
