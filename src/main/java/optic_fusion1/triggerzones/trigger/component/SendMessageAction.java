package optic_fusion1.triggerzones.trigger.component;

import optic_fusion1.triggerzones.trigger.TriggerContext;

public class SendMessageAction implements TriggerComponent {

    private String message;

    public SendMessageAction(String message) {
        this.message = message;
    }

    @Override
    public boolean execute(TriggerContext ctx) {
        ctx.getPlayer().sendMessage(message);
        return true;
    }

}
