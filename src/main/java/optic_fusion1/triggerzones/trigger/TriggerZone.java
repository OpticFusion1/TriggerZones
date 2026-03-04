package optic_fusion1.triggerzones.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.jishuna.regionsystem.Region;
import optic_fusion1.triggerzones.trigger.component.action.TriggerAction;
import org.bukkit.Location;
import optic_fusion1.triggerzones.trigger.component.requirement.TriggerRequirement;

public class TriggerZone extends Region {

    private String id;

    private List<TriggerRequirement> onEnterConditions = new ArrayList<>();
    private List<TriggerRequirement> onLeaveConditions = new ArrayList<>();
    private List<TriggerAction> onEnterActions = new ArrayList<>();
    private List<TriggerAction> onLeaveActions = new ArrayList<>();

    public TriggerZone(String id, Location minCorner, Location maxCorner) {
        super(minCorner, maxCorner);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    // Conditions
    public void addRequirement(TriggerEvent type, TriggerRequirement condition) {
        Objects.requireNonNull(condition, "Requirement cannot be null");
        getRequirementList(type).add(condition);
    }

    public void removeRequirement(TriggerEvent type, TriggerRequirement condition) {
        Objects.requireNonNull(condition, "Requirement cannot be null");
        getRequirementList(type).remove(condition);
    }

    public void clearRequirements(TriggerEvent type) {
        Objects.requireNonNull(type, "Event type cannot be null");
        getRequirementList(type).clear();
    }

    // Actions
    public void addAction(TriggerEvent type, TriggerAction condition) {
        Objects.requireNonNull(condition, "Requirement cannot be null");
        getActionList(type).add(condition);
    }

    public void removeAction(TriggerEvent type, TriggerAction condition) {
        Objects.requireNonNull(condition, "Requirement cannot be null");
        getActionList(type).remove(condition);
    }

    public void clearActions(TriggerEvent type) {
        Objects.requireNonNull(type, "Event type cannot be null");
        getActionList(type).clear();
    }

    public boolean allRequirementsMet(TriggerEvent type, TriggerContext context) {
        List<TriggerRequirement> conditions = getRequirementList(type);
        return conditions.isEmpty() || conditions.stream().allMatch(condition -> condition.execute(context));
    }

    public void onEnter(TriggerContext context) {
        onEnterActions.forEach(action -> action.execute(context));
    }

    public void onExit(TriggerContext context) {
        onLeaveActions.forEach(action -> action.execute(context));
    }

    private List<TriggerRequirement> getRequirementList(TriggerEvent type) {
        return type == TriggerEvent.ENTER ? onEnterConditions : onLeaveConditions;
    }

    private List<TriggerAction> getActionList(TriggerEvent type) {
        return type == TriggerEvent.ENTER ? onEnterActions : onLeaveActions;
    }

    public enum TriggerEvent {
        ENTER, LEAVE;
    }

}
