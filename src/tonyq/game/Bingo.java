package tonyq.game;

/**

 *  �p���{���W������ �нгq����i Msn:q9804.tommy@msa.hinet.net
 *  power by TonyQ
 *
 *	�{���ت�:���ϥΪ̻P�q�������G�C���A
  * �q�����@���ʧ@��︹�����H�����͡A���s�������u�̳ӡC
 *
 *
 *	�s�����:
 *	2005/01/03 AM 03:35 �}�l���g
 *	2005/01/03 AM 06;10 ����step 1�B2
 *	2005/01/03 AM 06:40 �[�J �i�H�M�� �۰ʿ︹ �\��r�\�� �{���ӳ��W��
 *	2005/01/03 AM 07:51 �q�������
 *	2005/01/07~09	���G�C���Ʀr�������g
 *	2005/01/14  ���G�C���ӧQ�̧P�_
 *	2005/01/14  �[�J�[�ݹq���H�ΨϥΪ̪��������p �H�έ��s�}�l����C
 *  2010/08/31 refactory
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Bingo extends JFrame implements ActionListener {

	public static void main(String args[]) {

		Bingo frame = new Bingo();
		frame.setVisible(true);

	}

	// set Data Members
	private JButton[] numberButton = new JButton[25];
	private JButton[] controlButton = new JButton[7];
	private JButton[] guessButton = new JButton[3];

	private static final int BUTTON_WIDTH = 60;
	private static final int BUTTON_HEIGHT = 40;
	private JTextField line;
	private JTextArea textArea;
	private JTextArea BingoNumber;
	private int x[] = { 70, 130, 190, 250, 310 };
	private int y[] = { 150, 190, 230, 270, 310 };
	private int step = 0;
	private int setNumber = 1;
	private int guessNum[] = { -1, 0 };
	private int guess = -1;
	private int ComNum[] = new int[25]; // [���X] = ��m
	private int UserNum[] = new int[25]; // [number] = location;
	private int checkNum[] = new int[26];
	private String guessStr[] = { "�ŤM", "���Y", "��" };
	private StringBuffer BingoRecord = new StringBuffer("���X������\n");
	private String temp = "";

	Container contentPane;

	public Bingo() {

		// set Frame
		contentPane = getContentPane();
		setSize(600, 500);
		setResizable(false);
		setTitle("Bingo(Single)");
		setLocation(150, 150);

		// set the content pane properties
		contentPane.setLayout(null);
		contentPane.setBackground(Color.white);

		// create and place buttons on the frame's content pane

		start();

	}

	public void actionPerformed(ActionEvent event) {

		JButton clickedButton = (JButton) event.getSource();

		// �Ĥ@�Ӱʧ@�����p
		if (step == 0) {
			step++;
			setNumber();

			// �ĤG���q :������X
		} else if (step == 1) {

			ComNum = setRandomNumArray();

			if (clickedButton == controlButton[3]) {
				UserNum = setRandomNumArray();
				for (int i = 0; i < 25; i++) {

					pickNumber(numberButton[UserNum[i] - 1]);

				}
			} else {
				controlButton[3].setEnabled(false);
			}

			if (clickedButton == controlButton[2])
				CancelSetNumber();
			else if (clickedButton == controlButton[1]) {
				step++;
				setNumber = 1;
				guess();
			}

			pickNumber(clickedButton);

			if (setNumber == 26) {
				line.setText("�A�w���w���Ҧ��Ʀr  �Ы� �T�w �~��  �Ϋ� ���� ����");
				controlButton[1].setEnabled(true);
			}

			// �ĤT���q �q��
		} else if (step == 2) {

			for (int i = 0; i < 3; i++) {
				if (clickedButton == guessButton[i]) {
					line.setText("�A��ܤF " + guessButton[i].getText() + "�A �T�w��?");
					guessNum[0] = i;
				}
			}

			if (clickedButton == controlButton[1] && guessNum[0] == -1) {
				line.setText("�A�٨S��ܭn�X������!!! ");
			} else if (clickedButton == controlButton[1] && guessNum[0] != -1) {
				guessNum[1] = (int) (Math.random() * 3);

				if (guessNum[1] == guessNum[0]) {
					line.setText("���� �A�Ӥ@��!");
					guessNum[0] = 0;
					guessNum[0] = 1;
				} else if (guessWin(guessNum[0], guessNum[1])) { // ���aĹ player
					line.setText("���a�X " + guessStr[guessNum[0]] + " �q���X "
							+ guessStr[guessNum[1]] + " ���aĹ�F!!");
					step++;
					BingoRecord.append("���a\t�q��\n");
					guess = 0;
					BingoStart();

				} else if (!guessWin(guessNum[0], guessNum[1])) { // ���a�� com
					line.setText("���a�X " + guessStr[guessNum[0]] + " �q���X "
							+ guessStr[guessNum[1]] + " �q��Ĺ�F!!");
					step++;
					BingoRecord.append("�q��\t���a\n");
					guess = 1;
					BingoStart();

					int temp = (int) (Math.random() * 25 + 1);
					ClickNumber(temp);
					BingoNumber.setText(BingoRecord.toString());
				}
			}

			// �ĥ|���q ���G�D�u
		} else if (step == 3) {
			// player click
			ClickNumber(Integer.parseInt(clickedButton.getText()));
			if (guess == 1)
				BingoRecord.append("\n");

			int tempcheck = -1;

			if (IsBingoWin()) {
				step++;
				tempcheck = 1;

				Bingowin(winner());

			} else {

				int temp;

				if (!CheckOver(checkNum)) {

					do {
						temp = (int) (Math.random() * 25 + 1);
					} while (checkNum[temp] == 1);
					ClickNumber(temp);
				}

				if (guess == 0)
					BingoRecord.append("\n");
				BingoNumber.setText(BingoRecord.toString());

			}

			if (IsBingoWin() && tempcheck == -1) {
				step++;
				Bingowin(winner());

			} else if (ConterLine(checkNum, ComNum) >= 3 && tempcheck == -1) {
				line.setText("�A�ثe�s�u���ƶq: " + ConterLine(checkNum, UserNum)
						+ " �`�N!! �q��" + ConterLine(checkNum, ComNum)
						+ "�s�u�F��!!�p�ߧO�鱼�F!!");
			} else if (tempcheck == -1) {
				line.setText("�A�ثe�s�u���ƶq: " + ConterLine(checkNum, UserNum));
			}

			// �Ĥ����q : �ӧQ �� ����
		} else if (step == 4) {

			if (clickedButton == controlButton[4]) { // restart

				contentPane.remove(controlButton[4]);
				contentPane.remove(controlButton[5]);
				contentPane.remove(controlButton[6]);
				contentPane.remove(line);
				contentPane.remove(BingoNumber);
				contentPane.remove(textArea);

				NumberButtionAction(0);
				start();

				contentPane.repaint();

			} else if (clickedButton == controlButton[5]) { // Com

				showConclusion(ComNum);
				controlButton[5].setEnabled(false);
				controlButton[6].setEnabled(true);
				line.setText(temp + "\t�q���s�u��:" + ConterLine(checkNum, ComNum));

			} else if (clickedButton == controlButton[6]) { // Student

				showConclusion(UserNum);
				controlButton[6].setEnabled(false);
				controlButton[5].setEnabled(true);
				line.setText(temp + "\t���a�s�u��:" + ConterLine(checkNum, UserNum));
			}

		}

	}

	private void start() {

		step = 0;
		setNumber = 1;
		guessNum = new int[] { -1, 0 };
		guess = -1;
		ComNum = new int[25]; // [���X] = ��m
		UserNum = new int[25]; // [number] = location;
		checkNum = new int[26];
		guessStr = new String[] { "�ŤM", "���Y", "��" };
		BingoRecord = new StringBuffer("���X������\n");

		// Set start button
		controlButton[0] = new JButton("Start");
		controlButton[0].setBounds(180, 300, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		contentPane.add(controlButton[0]);

		// set welcome message ( JTextField)

		textArea = new JTextArea();
		textArea.setBounds(50, 50, 300, 100);
		textArea.setEditable(false);
		textArea
				.setText("\n\n\n �w��A�Ӫ�Bingo�C���A ���C���Ѥ������ ���Y �Ҽ��g \n �p�{�����榳���D �Ц^��  Msn:q9804.tommy@msa.hinet.net \n\n\n �Ы� Start �}��!");
		contentPane.add(textArea);

		// set ActionListener
		controlButton[0].addActionListener(this);
	}

	private void setNumber() {

		contentPane.remove(controlButton[0]);
		textArea.setBounds(50, 50, 400, 100);
		textArea
				.setText("�C���}�l�o�I�{�b�аt�m�A�����X \n(���۹�������m�N�|�۰ʰt�m�F  �p�����N�i������ ���s�t�m) \n�۰ʬD��\��i�H���t�m25�ӼƦr �t�m�������N ���i�� ���� �A���s�t�m");

		controlButton[1] = new JButton("�T�w");
		controlButton[1].setBounds(140, 400, BUTTON_WIDTH, BUTTON_HEIGHT);
		controlButton[1].setEnabled(false);
		controlButton[1].addActionListener(this);
		contentPane.add(controlButton[1]);

		controlButton[2] = new JButton("����");
		controlButton[2].setBounds(250, 400, BUTTON_WIDTH, BUTTON_HEIGHT);
		controlButton[2].addActionListener(this);
		contentPane.add(controlButton[2]);

		controlButton[3] = new JButton("�۰ʬD��");
		controlButton[3].setBounds(400, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[3].addActionListener(this);
		contentPane.add(controlButton[3]);

		for (int i = 0; i < 25; i++) {
			numberButton[i] = new JButton("");
			numberButton[i].setBounds(x[i % 5], y[i / 5], BUTTON_WIDTH,
					BUTTON_HEIGHT);
			contentPane.add(numberButton[i]);
			numberButton[i].addActionListener(this);
		}

		line = new JTextField();
		line.setBounds(70, 350, 300, 30);
		line.setEditable(false);
		line.setText(" �A���n���w���Ʀr:1 \t �٦�25�ӼƦr�ݫ��w ");
		contentPane.add(line);

		contentPane.repaint();

	}

	private int[] setRandomNumArray() {

		int[] check = new int[25];
		int[] com = new int[25];

		for (int i = 0; i < 25; i++) {

			int number = (int) (Math.random() * 25) + 1;

			while (check[number - 1] != 0) {
				number = (int) (Math.random() * 25) + 1;
			}

			com[i] = number;
			check[number - 1] = 1;

		}

		return com;

	}

	private void CancelSetNumber() {

		for (int i = 0; i < 25; i++) {
			numberButton[i].setText("");
			numberButton[i].setEnabled(true);
			UserNum[i] = 0;
		}
		controlButton[1].setEnabled(false);
		controlButton[3].setEnabled(true);

		line.setText("�A�\���F���e���w���Ʀr �A���n���w���Ʀr:1 \t �٦�25�ӼƦr�ݫ��w ");

		setNumber = 1;

	}

	private void pickNumber(JButton clickedButton) {

		for (int i = 0; i < 25; i++) {
			if (clickedButton == numberButton[i]) {
				numberButton[i].setText("" + setNumber);
				numberButton[i].setEnabled(false);
				UserNum[setNumber - 1] = i + 1;
				setNumber++;
				line.setText(" �A���n���w���Ʀr: " + setNumber + " \t �٦� "
						+ (26 - setNumber) + " �ӼƦr�ݫ��w ");

			}
		}

	}

	private void NumberButtionAction(int k) { // 0 - remove 1 - add

		for (int i = 0; i < 25; i++) {
			if (k == 0)
				contentPane.remove(numberButton[i]);
			else if (k == 1)
				contentPane.add(numberButton[i]);
			numberButton[i].setEnabled(true);
		}
	}

	private void guess() {

		NumberButtionAction(0);
		contentPane.remove(controlButton[2]);
		contentPane.remove(controlButton[3]);
		textArea.setText("\n�{�b�P�q���q���M�w���ᶶ��!\n�п�� �ŤM ���Y �Υ� �A��ܧ�����T�w");
		line.setText("");

		for (int i = 0; i < 3; i++) {
			guessButton[i] = new JButton(guessStr[i]);
			guessButton[i].setBounds(70 + 80 * i, 200, BUTTON_WIDTH,
					BUTTON_HEIGHT);
			guessButton[i].addActionListener(this);
			contentPane.add(guessButton[i]);
		}

		contentPane.repaint();

	}

	private boolean guessWin(int guess1, int guess2) {
		boolean isWin = true;

		if (guess1 == 0 && guess2 == 1)
			isWin = false;
		if (guess1 == 1 && guess2 == 2)
			isWin = false;
		if (guess1 == 2 && guess2 == 0)
			isWin = false;

		return isWin;
	}

	private void BingoStart() {

		BingoNumber = new JTextArea();
		BingoNumber.setBounds(400, 100, 200, 300);
		BingoNumber.setEditable(false);
		BingoNumber.setText(BingoRecord.toString());
		contentPane.add(BingoNumber);
		textArea.setBounds(50, 50, 300, 100);
		contentPane.remove(controlButton[1]);

		for (int i = 0; i < 3; i++)
			contentPane.remove(guessButton[i]);
		NumberButtionAction(1);

		controlButton[1].setEnabled(false);
		textArea.setText("�{�b�i�滫�G�C���A�п������������X \n���s�������u���H�NĹ�o!!!");
		contentPane.repaint();
	}

	private void ClickNumber(int number) {

		if (numberButton[UserNum[number - 1] - 1].isEnabled()) {

			numberButton[UserNum[number - 1] - 1].setEnabled(false);
			checkNum[number] = 1;
			BingoRecord.append(number + "\t");
		}

	}

	private int ConterLine(int[] checknumber, int[] input) {

		int line = 0;
		int[] checklocation = new int[26];

		for (int i = 1; i < 26; i++) {

			if (checknumber[i] == 1) {
				checklocation[input[i - 1]] = 1;
			}

		}

		for (int i = 0; i < 5; i++) {

			if (checklocation[1 + 5 * i] != 0 && checklocation[2 + 5 * i] != 0
					&& checklocation[3 + 5 * i] != 0
					&& checklocation[4 + 5 * i] != 0
					&& checklocation[5 + 5 * i] != 0)
				line++;
			if (checklocation[1 + i] != 0 && checklocation[6 + i] != 0
					&& checklocation[11 + i] != 0 && checklocation[16 + i] != 0
					&& checklocation[21 + i] != 0)
				line++;
		}

		if (checklocation[5] != 0 && checklocation[9] != 0
				&& checklocation[13] != 0 && checklocation[17] != 0
				&& checklocation[21] != 0)
			line++;
		if (checklocation[1] != 0 && checklocation[7] != 0
				&& checklocation[13] != 0 && checklocation[18] != 0
				&& checklocation[25] != 0)
			line++;

		return line;
	}

	private boolean CheckOver(int[] check) {

		boolean over = true;

		for (int i = 1; i < 26; i++) {
			if (check[i] == 0)
				over = false;
		}

		return over;

	}

	private boolean IsBingoWin() {

		boolean IsWin = false;
		if (ConterLine(checkNum, ComNum) > 4
				|| ConterLine(checkNum, UserNum) > 4)
			IsWin = true;

		return IsWin;
	}

	private String winner() {

		String output = "";

		if (ConterLine(checkNum, ComNum) > 4
				&& ConterLine(checkNum, UserNum) < 5)
			output = "Com";

		else if (ConterLine(checkNum, ComNum) < 5
				&& ConterLine(checkNum, UserNum) > 4)
			output = "User";

		else if (ConterLine(checkNum, ComNum) > 4
				&& ConterLine(checkNum, UserNum) > 4)
			output = "deuce";

		return output;

	}

	private void Bingowin(String winner) {

		controlButton[4] = new JButton("���s�}�l");
		controlButton[4].setBounds(70, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[4].addActionListener(this);
		contentPane.add(controlButton[4]);

		controlButton[5] = new JButton("��ܹq�����p");
		controlButton[5].setBounds(200, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[5].addActionListener(this);
		contentPane.add(controlButton[5]);

		controlButton[6] = new JButton("��ܪ��a���p");
		controlButton[6].setBounds(330, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[6].setEnabled(false);
		controlButton[6].addActionListener(this);
		contentPane.add(controlButton[6]);

		if (winner.equals("User")) {
			temp = "����!!! �AĹ�F!!!!^___^";
			line.setText(temp);
		} else if (winner.equals("Com")) {
			temp = "������ �q��Ĺ�F!!!!^_____________^!!";
			line.setText(temp);

		} else if (winner.equals("deuce")) {
			temp = "����P�ɳ��F��ζW�L�����u ����^________^llb";
			line.setText(temp);
		}

		textArea
				.setText("�C�������F�A\n�{�b�A�i�H����[�ݪ��a�����ɪ��A�ιq�������ɪ��A�A\n�Ϊ̫�[���s�}�l]�i��@�ӷs�C�� ");

		contentPane.repaint();

	}

	private void showConclusion(int[] num) {

		for (int i = 1; i < checkNum.length; i++) {
			if (checkNum[i] != 0) {
				numberButton[num[i - 1] - 1].setText("" + i);
				numberButton[num[i - 1] - 1].setEnabled(false);
			} else {
				numberButton[num[i - 1] - 1].setText("" + i);
				numberButton[num[i - 1] - 1].setEnabled(true);
			}
		}

	}

}