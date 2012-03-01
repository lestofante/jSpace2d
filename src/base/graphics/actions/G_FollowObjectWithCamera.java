package base.graphics.actions;

public class G_FollowObjectWithCamera extends GraphicAction {

	public float[] transform;

	public G_FollowObjectWithCamera(float[] transform) {
		super(ActionType.FOLLOW_OBJECT);

		this.transform = transform;

	}

}
