package tonyq.game;

/**

 *  如有程式上的疏失 煩請通知改進 Msn:q9804.tommy@msa.hinet.net
 *  power by TonyQ
 *
 *	程式目的:讓使用者與電腦玩賓果遊戲，
  * 電腦的一切動作跟選號均由隨機產生，先連成五條線者勝。
 *
 *
 *	編輯紀錄:
 *	2005/01/03 AM 03:35 開始撰寫
 *	2005/01/03 AM 06;10 完成step 1、2
 *	2005/01/03 AM 06:40 加入 懶人專用 自動選號 功能r功能 程式細部規劃
 *	2005/01/03 AM 07:51 猜拳機制完成
 *	2005/01/07~09	賓果遊戲數字部分撰寫
 *	2005/01/14  賓果遊戲勝利者判斷
 *	2005/01/14  加入觀看電腦以及使用者的結束狀況 以及重新開始機制。
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
	private int guessNum[] = { -1, 0 };
	private int guess = -1;

	private String guessStr[] = { "剪刀", "石頭", "布" };
	private StringBuffer BingoRecord = new StringBuffer("號碼紀錄區\n");
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

		// 第一個動作的狀況
		if (step == 0) {
			step++;
			setNumber();

			// 第二階段 :選取號碼
		} else if (step == 1) {



			// 第三階段 猜拳
		} else if (step == 2) {

			for (int i = 0; i < 3; i++) {
				if (clickedButton == guessButton[i]) {
					line.setText("你選擇了 " + guessButton[i].getText() + "， 確定嗎?");
					guessNum[0] = i;
				}
			}

			if (clickedButton == controlButton[1] && guessNum[0] == -1) {
				line.setText("你還沒選擇要出的拳喔!!! ");
			} else if (clickedButton == controlButton[1] && guessNum[0] != -1) {
				guessNum[1] = (int) (Math.random() * 3);

				if (guessNum[1] == guessNum[0]) {
					line.setText("平手 再來一次!");
					guessNum[0] = 0;
					guessNum[0] = 1;
				} else if (guessWin(guessNum[0], guessNum[1])) { // 玩家贏 player
					line.setText("玩家出 " + guessStr[guessNum[0]] + " 電腦出 "
							+ guessStr[guessNum[1]] + " 玩家贏了!!");
					step++;
					BingoRecord.append("玩家\t電腦\n");
					guess = 0;
					BingoStart();

				} else if (!guessWin(guessNum[0], guessNum[1])) { // 玩家輸 com
					line.setText("玩家出 " + guessStr[guessNum[0]] + " 電腦出 "
							+ guessStr[guessNum[1]] + " 電腦贏了!!");
					step++;
					BingoRecord.append("電腦\t玩家\n");
					guess = 1;
					BingoStart();

					int temp = (int) (Math.random() * 25 + 1);
					ClickNumber(temp);
					BingoNumber.setText(BingoRecord.toString());
				}
			}

			// 第四階段 賓果主線
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
				line.setText("你目前連線的數量: " + ConterLine(checkNum, UserNum)
						+ " 注意!! 電腦" + ConterLine(checkNum, ComNum)
						+ "連線了喔!!小心別輸掉了!!");
			} else if (tempcheck == -1) {
				line.setText("你目前連線的數量: " + ConterLine(checkNum, UserNum));
			}

			// 第五階段 : 勝利 或 失敗
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
				line.setText(temp + "\t電腦連線數:" + ConterLine(checkNum, ComNum));

			} else if (clickedButton == controlButton[6]) { // Student

				showConclusion(UserNum);
				controlButton[6].setEnabled(false);
				controlButton[5].setEnabled(true);
				line.setText(temp + "\t玩家連線數:" + ConterLine(checkNum, UserNum));
			}

		}

	}

	private void start() {

		step = 0;
		setNumber = 1;
		guessNum = new int[] { -1, 0 };
		guess = -1;
		ComNum = new int[25]; // [號碼] = 位置
		UserNum = new int[25]; // [number] = location;
		checkNum = new int[26];
		guessStr = new String[] { "剪刀", "石頭", "布" };
		BingoRecord = new StringBuffer("號碼紀錄區\n");

		// Set start button
		controlButton[0] = new JButton("Start");
		controlButton[0].setBounds(180, 300, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		contentPane.add(controlButton[0]);

		// set welcome message ( JTextField)

		textArea = new JTextArea();
		textArea.setBounds(50, 50, 300, 100);
		textArea.setEditable(false);
		textArea
				.setText("\n\n\n 歡迎你來玩Bingo遊戲， 本遊戲由元智資管 骨頭 所撰寫 \n 如程式執行有問題 請回報  Msn:q9804.tommy@msa.hinet.net \n\n\n 請按 Start 開啟!");
		contentPane.add(textArea);

		// set ActionListener
		controlButton[0].addActionListener(this);
	}

	private void setNumber() {

		contentPane.remove(controlButton[0]);
		textArea.setBounds(50, 50, 400, 100);
		textArea
				.setText("遊戲開始囉！現在請配置你的號碼 \n(按相對應的位置就會自動配置了  如不滿意可按取消 重新配置) \n自動挑選功能可隨機配置25個數字 配置完不滿意 仍可按 取消 再重新配置");

		controlButton[1] = new JButton("確定");
		controlButton[1].setBounds(140, 400, BUTTON_WIDTH, BUTTON_HEIGHT);
		controlButton[1].setEnabled(false);
		controlButton[1].addActionListener(this);
		contentPane.add(controlButton[1]);

		controlButton[2] = new JButton("取消");
		controlButton[2].setBounds(250, 400, BUTTON_WIDTH, BUTTON_HEIGHT);
		controlButton[2].addActionListener(this);
		contentPane.add(controlButton[2]);

		controlButton[3] = new JButton("自動挑選");
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
		line.setText(" 你正要指定的數字:1 \t 還有25個數字待指定 ");
		contentPane.add(line);

		contentPane.repaint();

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
		textArea.setText("\n現在與電腦猜拳決定先後順序!\n請選擇 剪刀 石頭 或布 ，選擇完後按確定");
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
		textArea.setText("現在進行賓果遊戲，請選取欲選取的號碼 \n先連成五條線的人就贏囉!!!");
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

		controlButton[4] = new JButton("重新開始");
		controlButton[4].setBounds(70, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[4].addActionListener(this);
		contentPane.add(controlButton[4]);

		controlButton[5] = new JButton("顯示電腦狀況");
		controlButton[5].setBounds(200, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[5].addActionListener(this);
		contentPane.add(controlButton[5]);

		controlButton[6] = new JButton("顯示玩家狀況");
		controlButton[6].setBounds(330, 400, BUTTON_WIDTH * 2, BUTTON_HEIGHT);
		controlButton[6].setEnabled(false);
		controlButton[6].addActionListener(this);
		contentPane.add(controlButton[6]);

		if (winner.equals("User")) {
			temp = "恭喜!!! 你贏了!!!!^___^";
			line.setText(temp);
		} else if (winner.equals("Com")) {
			temp = "哈哈哈 電腦贏了!!!!^_____________^!!";
			line.setText(temp);

		} else if (winner.equals("deuce")) {
			temp = "兩邊同時都達到或超過五條線 平手^________^llb";
			line.setText(temp);
		}

		textArea
				.setText("遊戲結束了，\n現在你可以選擇觀看玩家結束時狀態或電腦結束時狀態，\n或者按[重新開始]進行一個新遊戲 ");

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