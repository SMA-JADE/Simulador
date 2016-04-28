package behaviours;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

/**
 * Created by nancio on 22/04/16.
 */
public class PizzaState extends WakerBehaviour{

    Event event;
    int result;

    public PizzaState(Agent a, long timeout, Event e) {
        super(a, timeout);
        this.event = e;
    }

    @Override
    protected void onWake() {
        result = event.fire();
    }

    @Override
    public int onEnd() {
        return result;
    }

    public interface Event {
        int fire();
    }
}
