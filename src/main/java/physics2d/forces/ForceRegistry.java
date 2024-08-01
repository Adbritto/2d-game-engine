package physics2d.forces;

import physics2d.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

public class ForceRegistry {
    private List<ForceRegistration> registry;

    public ForceRegistry() {
        this.registry = new ArrayList<>();
    }

    public void add(Rigidbody2D rigidbody, ForceGenerator forceGenerator) {
        ForceRegistration forceRegistration = new ForceRegistration(forceGenerator, rigidbody);
        registry.add(forceRegistration);
    }

    public void ramove(Rigidbody2D rigidbody, ForceGenerator forceGenerator) {
        ForceRegistration forceRegistration = new ForceRegistration(forceGenerator, rigidbody);
        registry.remove(forceRegistration);
    }

    public void clear() {
        registry.clear();
    }

    public void updateForces(float dt) {
        for (ForceRegistration forceRegistration : registry) {
            forceRegistration.forceGenerator.updateForce(forceRegistration.rigidbody, dt);
        }
    }

    public void zeroForces() {
        for (ForceRegistration forceRegistration : registry) {
            //TODO: IMPLEMENT ME
            //forceRegistration.rigidbody.zeroForces();
        }
    }
}
