/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.graphics.actions;

/**
 * 
 * @author mauro
 */
public abstract class GraphicAction {

	public static enum ActionType {

		CREATE, REMOVE, FOLLOW_OBJECT
	}

	public ActionType actionType;

	public GraphicAction(ActionType actionType) {
		this.actionType = actionType;
	}
}
