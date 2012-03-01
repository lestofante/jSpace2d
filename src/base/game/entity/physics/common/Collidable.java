package base.game.entity.physics.common;

import java.util.LinkedList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import base.game.entity.Entity;

public class Collidable extends PhysicalObject {

	private final Body body;

	private final LinkedList<CollidableAction> actions = new LinkedList<>();

	public Collidable(Body body) {
		this.body = body;
	}

	public void updateSharedPosition() {
		transform[0] = getBody().getPosition().x;
		transform[1] = getBody().getPosition().y;
		transform[2] = getBody().getAngle();
	}

	public Body getBody() {
		return body;
	}

	@Override
	public void setOwner(Entity e) {
		super.setOwner(e);
		body.setUserData(this);
	}

	@Override
	public void setTransform(Vec2 translation, float angle) {
		// TODO Auto-generated method stub
		try {
			throw new Exception("Collidables cannot be moved liked this!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applyAction(CollidableAction action) {
		actions.add(action);
	}

	public LinkedList<CollidableAction> getCollidableActions() {
		return actions;
	}

	public void clearCollidableActions() {
		actions.clear();
	}

}
