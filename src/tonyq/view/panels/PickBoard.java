package tonyq.view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tonyq.game.UserBoard;

public class PickBoard extends JPanel implements ActionListener {

//	private int checkNum[] = new int[26];
//	private int step = 0;

	private static final long serialVersionUID = 4124482357945592143L;

	// TODO implements this
	private JButton randomButton = null;
	private JButton cancelButton = null;
	private JButton submitButton = null;
	private JTextField line;

	private JButton[] numberButton = new JButton[25];

	// already done
	UserBoard comboard; // [腹絏] = 竚
	UserBoard userboard; // [number] = location;

	private static String EVENT_USER_RANDOM = "user_random";
	private static String EVENT_USER_CANCEL = "user_cancel";
	private static String EVENT_USER_OK = "user_ok";

	public PickBoard() {
		comboard = new UserBoard();
		comboard.random();
		userboard = new UserBoard();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();

		if (EVENT_USER_RANDOM.equals(command)) {
			userboard.random();

		} else if (EVENT_USER_CANCEL.equals(command)) {
			userboard.cancel();
		} else if (EVENT_USER_OK.equals(command)) {

			// TODO go next step
		} else if (command.matches("[0-9]+")) {
			int clickedNumber = Integer.parseInt(command);
			if (clickedNumber >= 0 && clickedNumber <= 25
					&& !userboard.isLocationSelected(clickedNumber)) {
				userboard.doSelect(clickedNumber);
			}

		}
		updateUI();

	}

	public void updateUI() {
		// if selected end

		if (userboard.isFull()) {
			line.setText("﹚Ч┮Τ计  叫 絋﹚ 膥尿  ┪  ㄓ");
		}

		if (userboard.isCancel()) {
			line.setText("砛ぇ玡﹚计 タ璶﹚计:1 \t 临Τ25计﹚ ");
		}

		cancelButton.setEnabled(userboard.isCancelable());
		randomButton.setEnabled(userboard.isRandomable());
		submitButton.setEnabled(userboard.isSummitable());

		for (int i = 0; i < 25; i++) {
			if (userboard.isLocationSelected(i)) {
				numberButton[i].setText("" + userboard.getNumber(i));
			} else {
				numberButton[i].setText("");
			}
			numberButton[i].setEnabled(userboard.isLocationSelected(i));

		}
	}

}
