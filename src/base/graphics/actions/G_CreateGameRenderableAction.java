package base.graphics.actions;

/**
 * 
 * @author mauro
 */
public class G_CreateGameRenderableAction extends GraphicAction {

	public Integer iD;
	public float[] transform;
	public String modelName;

	public G_CreateGameRenderableAction(Integer iD, String modelName, float[] transform) {
		super(ActionType.CREATE);
		this.iD = iD;
		this.modelName = modelName;
		this.transform = transform;
	}

}
