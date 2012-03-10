package client.swingLauncher;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import client.ClientTest;

public class ClientLauncher {

	private JFrame frmJspacdd;
	private JPanel panel_5;
	private JPanel panel_6;
	private JButton btnNewButton;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_2;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ClientLauncher window = new ClientLauncher();
					window.frmJspacdd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientLauncher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmJspacdd = new JFrame();
		frmJspacdd.setMinimumSize(new Dimension(300, 0));
		frmJspacdd.setTitle("jSpace2d");
		frmJspacdd.setBounds(100, 100, 305, 206);
		frmJspacdd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 290, 0 };
		gridBagLayout.rowHeights = new int[] { 77, 31, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		frmJspacdd.getContentPane().setLayout(gridBagLayout);

		panel_5 = new JPanel();
		panel_5.setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.anchor = GridBagConstraints.NORTH;
		gbc_panel_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_5.insets = new Insets(0, 0, 5, 0);
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 0;
		frmJspacdd.getContentPane().add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[] { 101, 99, 0 };
		gbl_panel_5.rowHeights = new int[] { 19, 19, 19, 0 };
		gbl_panel_5.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel_5.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel_5.setLayout(gbl_panel_5);

		lblNewLabel = new JLabel("Server IP");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_5.add(lblNewLabel, gbc_lblNewLabel);
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.TRAILING);
		textField.setText("127.0.0.1");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel_5.add(textField, gbc_textField);
		textField.setColumns(10);

		lblNewLabel_1 = new JLabel("Server port");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel_5.add(lblNewLabel_1, gbc_lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setHorizontalAlignment(SwingConstants.TRAILING);
		textField_1.setText("9999");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		panel_5.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);

		lblNewLabel_2 = new JLabel("Player name");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		panel_5.add(lblNewLabel_2, gbc_lblNewLabel_2);

		textField_2 = new JTextField();
		textField_2.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.BOTH;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		panel_5.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);

		panel_6 = new JPanel();
		panel_6.setBorder(new EmptyBorder(3, 3, 3, 3));
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[] { 92, 0 };
		gbl_panel_6.rowHeights = new int[] { 25, 0 };
		gbl_panel_6.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel_6.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel_6.setLayout(gbl_panel_6);
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 1;
		frmJspacdd.getContentPane().add(panel_6, gbc_panel_6);

		btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String playerName = getPlayerTextField().getText();
				String serverIP = getIPTextField().getText();
				int serverPort = Integer.parseInt(getPortTextField().getText());
				Thread gameThread = new Thread(new ClientTest(playerName, serverIP, serverPort));
				gameThread.start();
				frmJspacdd.dispose();
			}
		});

		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		panel_6.add(btnNewButton, gbc_btnNewButton);
		frmJspacdd.pack();
		frmJspacdd.setLocationRelativeTo(null);
		getPlayerTextField().requestFocusInWindow();

	}

	public JTextField getPlayerTextField() {
		return textField_2;
	}

	public JTextField getIPTextField() {
		return textField;
	}

	public JTextField getPortTextField() {
		return textField_1;
	}
}
