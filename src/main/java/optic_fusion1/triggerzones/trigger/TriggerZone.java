package optic_fusion1.triggerzones.trigger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.jishuna.regionsystem.Region;
import optic_fusion1.triggerzones.trigger.component.TriggerComponent;
import org.bukkit.Location;

public class TriggerZone extends Region {

    private String id;

    private List<TriggerComponent> onEnterConditions = new ArrayList<>();
    private List<TriggerComponent> onLeaveConditions = new ArrayList<>();
    private List<TriggerComponent> onEnterActions = new ArrayList<>();
    private List<TriggerComponent> onLeaveActions = new ArrayList<>();

    public TriggerZone(String id, Location minCorner, Location maxCorner) {
        super(minCorner, maxCorner);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addCondition(TriggerEvent type, TriggerComponent condition) {
        Objects.requireNonNull(condition, "Condition cannot be null");
        getConditionList(type).add(condition);
    }

    public void removeCondition(TriggerEvent type, TriggerComponent condition) {
        Objects.requireNonNull(condition, "Condition cannot be null");
        getConditionList(type).remove(condition);
    }

    public void clearConditions(TriggerEvent type) {
        Objects.requireNonNull(type, "Event type cannot be null");
        getConditionList(type).clear();
    }

    public void addAction(TriggerEvent type, TriggerComponent action) {
        Objects.requireNonNull(action, "Action cannot be null");
        getActionList(type).add(action);
    }

    public void removeAction(TriggerEvent type, TriggerComponent action) {
        Objects.requireNonNull(action, "Action cannot be null");
        getActionList(type).remove(action);
    }

    public void clearActions(TriggerEvent type) {
        Objects.requireNonNull(type, "Event type cannot be null");
        getActionList(type).clear();
    }

    public boolean allConditionsMet(TriggerEvent type, TriggerContext context) {
        List<TriggerComponent> conditions = getConditionList(type);
        return conditions.isEmpty() || conditions.stream().allMatch(condition -> condition.execute(context));
    }

    public void onEnter(TriggerContext context) {
        onEnterActions.forEach(action -> action.execute(context));
    }

    public void onExit(TriggerContext context) {
        onLeaveActions.forEach(action -> action.execute(context));
    }

    private List<TriggerComponent> getConditionList(TriggerEvent type) {
        return type == TriggerEvent.ENTER ? onEnterConditions : onLeaveConditions;
    }

    private List<TriggerComponent> getActionList(TriggerEvent type) {
        return type == TriggerEvent.ENTER ? onEnterActions : onLeaveActions;
    }

    public enum TriggerEvent {
        ENTER, LEAVE;
    }

}
