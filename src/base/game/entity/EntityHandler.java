package base.game.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

import org.jbox2d.common.Vec2;

import base.common.AsyncActionBus;
import base.common.InfoBodyContainer;
import base.game.entity.graphics.actions.G_CreateGameRenderableAction;
import base.game.entity.graphics.actions.G_RemoveGameRenderable;
import base.game.entity.physics.PhysicsHandler;
import base.game.entity.physics.common.BodyBlueprint;
import base.game.player.Player;
import base.worker.Worker;

public class EntityHandler {

	private final LinkedList<Integer> unusedIDs = new LinkedList<>();
	private int currentID = 0;
	private final HashMap<Integer, Entity> entityMap = new HashMap<>();
	
	/*for physic*/
	protected final PhysicsHandler phisic;
	
	/*for graphics*/
	private final AsyncActionBus bus;
	
	public EntityHandler(AsyncActionBus graphicBus, AtomicInteger step){
		phisic = new PhysicsHandler(12000000, graphicBus.sharedLock, step);
		this.bus = graphicBus;
		phisic.start();
	}

	public void update(ArrayList<Worker> w){
		phisic.update(w);
	}

	private int getFreeID(){
		if(unusedIDs.size()>0){
			return unusedIDs.poll();
		}else{
			return currentID++; //return current ID and then increment it by 1
		}
	}

	private void removeID(int ID){
		if (ID < currentID){
			unusedIDs.add(currentID);
		}
	}

	public int createEntity(String graphicModelName, BodyBlueprint bodyBlueprint, Player player){
		int id = getFreeID();
		Entity e = new Entity(id, player);
		entityMap.put(id, e);

		InfoBodyContainer infoBody = createPhisic(bodyBlueprint); 
		
		infoBody.body.setUserData(e);
		
		if ( infoBody != null ){
			e.infoBody = infoBody;
			createGraphics(id, infoBody, graphicModelName);
			return id;
		}else
			return -3; //phisic error
	}
	
	public void moveEntity(float newX, float newY, int entity){
		entityMap.get(entity).infoBody.body.setTransform(new Vec2(newX, newY), entityMap.get(entity).infoBody.body.getAngle());
	}

	public void removeEntity(int id){
		removeID(id);
		Entity e = entityMap.remove(id);
		destroyBody( e.infoBody );
		destroyGraphic( e.entityID );
	}

	private void createGraphics(int ID, InfoBodyContainer infoBody, String graphicModelName) {
			G_CreateGameRenderableAction a = new G_CreateGameRenderableAction(ID, graphicModelName, infoBody.transform);
			bus.addGraphicsAction(a);
	}

	private void destroyBody(InfoBodyContainer infoBody) {
			phisic.removeBody(infoBody.body);
	}

	private void destroyGraphic(int ID) {
		bus.addGraphicsAction(new G_RemoveGameRenderable(ID));
	}

	private InfoBodyContainer createPhisic(BodyBlueprint bodyBlueprint){
		return phisic.addBody(bodyBlueprint);
	}


}
