package base.simpleTest;

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import base.common.ActionBus;
import base.graphics.actions.G_FollowObjectWithCamera;
import base.graphics.engine.GraphicsManager;
import base.physics.actions.P_CreateBodyAction;
import base.physics.engine.PhysicsManager;

public class Launcher implements WindowListener {

	private static final long physicsStep = 12500000;
	private static final int numberOfCubes = 200;
	private static ModeChooser frame;
	private static DisplayMode mode;

	private JComboBox<DisplayMode> comboBox;
	private Boolean fullScreen;
	private JCheckBox fullScreenCheckBox;
	private CountDownLatch loginSignal;
	private Boolean vSync;
	private JCheckBox vSyncCheckBox;
	
	public Launcher() {
		mode = null;
		DisplayMode[] dModes = null;

		try {
			dModes = Display.getAvailableDisplayModes();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		simpleModeSelectorUI(dModes);

		// TODO improve handling ActionManager

		ActionBus aManager = new ActionBus();

		GraphicsManager test = new GraphicsManager(mode, fullScreen, vSync, aManager);
		Thread graphics = new Thread(test);
		graphics.start();

		PhysicsManager pManager = new PhysicsManager(aManager, physicsStep, false);
		pManager.start();
		
		createRandomActions(aManager);
		

		boolean temp = true;
		while (true) {

			pManager.update();

			if(pManager.sortedOggetto2D.get(new Integer(0))!=null&&temp){
				aManager.addGraphicsAction(new G_FollowObjectWithCamera(pManager.sortedOggetto2D.get(new Integer(0)).atomicFloat));
				temp = false;
			}
		}

	}

	private void createRandomActions(ActionBus aManager) {

		for (int i = 0; i < numberOfCubes; i++) {
			aManager.addPhysicAction(new P_CreateBodyAction(0));
		}
	}
	

	private void simpleModeSelectorUI(DisplayMode[] dModes) {
		loginSignal = new CountDownLatch(1);
		frame = new ModeChooser("Choose mode");
		frame.setLayout(new FlowLayout());
		comboBox = new JComboBox<>(dModes);
		frame.add(comboBox);
		DisplayMode current = Display.getDesktopDisplayMode();
		comboBox.setSelectedItem(current);
		JButton button = new JButton("OK");
		frame.add(button);
		button.addActionListener(frame);
		fullScreenCheckBox = new JCheckBox("Fullscreen");
		vSyncCheckBox = new JCheckBox("vSync");
		frame.add(fullScreenCheckBox);
		frame.add(vSyncCheckBox);
		frame.addWindowListener(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		try {
			loginSignal.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("Window closed.");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Window closing.");
		mode = (DisplayMode) comboBox.getSelectedItem();
		fullScreen = fullScreenCheckBox.isSelected();
		vSync = vSyncCheckBox.isSelected();
		loginSignal.countDown();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
