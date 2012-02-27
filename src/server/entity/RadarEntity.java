package server.entity;

import java.util.HashSet;

import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;

import base.game.entity.Entity;
import base.game.player.Player;

public class RadarEntity extends ServerEntity implements QueryCallback {

	public HashSet<Entity> visibleEntities = new HashSet<>();
	public HashSet<Entity> newEntities = new HashSet<>();
	private HashSet<Entity> persistentEntities = new HashSet<>();
	public HashSet<Entity> toRemoveEntities = new HashSet<>();
	private final float radius;
	
	public RadarEntity(int id, Player player, float radius) {
		super(id, player);
		this.radius = radius;
	}

	@Override
	public boolean reportFixture(Fixture arg0) {
		Entity toAdd = (Entity) arg0.getBody().getUserData();
		toRemoveEntities.remove(toAdd);
		
		if(visibleEntities.add(toAdd))
			newEntities.add(toAdd);
		else
			persistentEntities.add(toAdd);
		return true;
	}

	public AABB getAABB() {
		Vec2 lV = infoBody.body.getWorldCenter().sub(new Vec2(radius,radius));
		Vec2 uV = infoBody.body.getWorldCenter().add(new Vec2(radius,radius));
		return new AABB(lV, uV);
	}

	public void preQuery() {
		visibleEntities.removeAll(toRemoveEntities);
		toRemoveEntities.clear();
		toRemoveEntities.addAll(visibleEntities);
		newEntities.clear();
		persistentEntities.clear();
	}

}
