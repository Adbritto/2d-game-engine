package physics2d.forces;

import physics2d.rigidbody.Rigidbody2D;

public class ForceRegistration {
    public ForceGenerator forceGenerator;
    public Rigidbody2D rigidbody;

    public ForceRegistration(ForceGenerator forceGenerator, Rigidbody2D rigidbody) {
        this.forceGenerator = forceGenerator;
        this.rigidbody = rigidbody;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o.getClass() != ForceRegistration.class) return false;

        ForceRegistration forceRegistration = (ForceRegistration) o;
        return forceRegistration.rigidbody == this.rigidbody &&
               forceRegistration.forceGenerator == this.forceGenerator;
    }
}
