/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.graphics.actions;

/**
 * 
 * @author mauro
 */
public class G_RemoveGameRenderable extends GraphicAction {

	public Integer iD;

	public G_RemoveGameRenderable(Integer iD) {
		super(ActionType.REMOVE);
		this.iD = iD;
	}
}
