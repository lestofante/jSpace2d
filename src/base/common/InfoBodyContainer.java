/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.common;

import org.jbox2d.dynamics.Body;


/**
 * 
 * @author mauro
 */
public class InfoBodyContainer {
	public Body body;
	public float[] transform;

	public InfoBodyContainer(Body body) {
		this.body = body;
		this.transform = new float[3];
	}

	public void updateSharedPosition() {
		transform[0] = body.getPosition().x;
		transform[1] = body.getPosition().y;
		transform[2] = body.getAngle();
	}

}
