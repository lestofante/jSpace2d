package base.game.entity.graphics.actions;

public class G_FollowObjectWithCamera extends GraphicAction {
	
	public Integer ID;
	
	public G_FollowObjectWithCamera(Integer objectID) {
		super(ActionType.FOLLOW_OBJECT);
	
		this.ID = objectID;
		
	}

}
