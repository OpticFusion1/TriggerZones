package optic_fusion1.triggerzones;

import me.jishuna.regionsystem.RegionManager;
import optic_fusion1.triggerzones.trigger.listener.TriggerZoneListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TriggerZones extends JavaPlugin {

    private PluginManager pluginManager = Bukkit.getPluginManager();
    private RegionManager regionManager = new RegionManager();

    @Override
    public void onEnable() {
        registerListeners();
    }

    private void registerListeners() {
        pluginManager.registerEvents(new TriggerZoneListener(regionManager), this);
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

}
