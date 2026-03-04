package optic_fusion1.triggerzones.trigger.component.action;

import optic_fusion1.triggerzones.trigger.TriggerContext;

public class SendMessageAction implements TriggerAction {

    private String message;

    public SendMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void execute(TriggerContext ctx) {
        ctx.getPlayer().sendMessage(message);
    }

}
