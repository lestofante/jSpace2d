package base.graphics.actions;

import base.common.InfoBodyContainer;

public class G_FollowObjectWithCamera extends GraphicAction {

	public InfoBodyContainer info;

	public G_FollowObjectWithCamera(InfoBodyContainer transform) {
		super(ActionType.FOLLOW_OBJECT);

		this.info = transform;

	}

}
