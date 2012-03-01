package base.game.entity.physics.common;

public abstract class PhysicalBody {

	/**
	 * abstract class that represents a movable object in the physics world
	 * 
	 * the 2 most basic subclasses are effective collidable bodies, and radars
	 */

	private float[] transform; // this represents the position and rotation of
								// the object

	public float[] getTransform() {
		return transform;
	}

}
