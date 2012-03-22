package base.game.entity;

import base.game.entity.physics.common.BodyBlueprint;

public class GeneralBP {
	String g;
	BodyBlueprint b;

	public GeneralBP(String g, BodyBlueprint b) {
		this.g = g;
		this.b = b;
	}

	public String getGrapichsBluePrint(int blueprintID) {
		return g;
	}

	public BodyBlueprint getPhysicBluePrint() {
		return b;
	}

}
