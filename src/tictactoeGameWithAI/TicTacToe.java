package tictactoeGameWithAI;

import java.awt.Color;
import java.awt.Container;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import java.awt.event.InputEvent;

public class TicTacToe {
	private static Font menuFont = new Font("Calibri", Font.PLAIN, 16);
	private static Container container;
	private static JPanel panel;
	private static JButton[] btns;
	private static ActionHandle listenAction = new ActionHandle();
	
	private static int[][] array = new int[3][3];
	private static String turn = "X";
	private static JLabel scoreX, scoreO;
	private static int numScoreX = 0;
	private static int numScoreO = 0;
	private static int gameMode = 1;
	private static int computersMove;

	// ##########################################################################################################
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		JFrame frame = new JFrame("TicTacToe Game");
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		container = frame.getContentPane();
		frame.getContentPane().setLayout(null);

		scoreX = new JLabel("X");
		scoreX.setForeground(Color.BLUE);
		scoreX.setFont(new Font("Tahoma", Font.PLAIN, 60));
		scoreX.setHorizontalAlignment(SwingConstants.CENTER);
		scoreX.setBounds(360, 20, 100, 66);
		frame.getContentPane().add(scoreX);

		JLabel line = new JLabel("|");
		line.setFont(new Font("Tahoma", Font.PLAIN, 54));
		line.setHorizontalAlignment(SwingConstants.CENTER);
		line.setBounds(460, 20, 75, 66);
		frame.getContentPane().add(line);

		scoreO = new JLabel("O");
		scoreO.setForeground(Color.RED);
		scoreO.setFont(new Font("Tahoma", Font.PLAIN, 60));
		scoreO.setHorizontalAlignment(SwingConstants.CENTER);
		scoreO.setBounds(535, 20, 100, 66);
		frame.getContentPane().add(scoreO);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.white);
		frame.setJMenuBar(menuBar);

		JMenu menuGameOptions = new JMenu("Options");
		menuGameOptions.setFont(menuFont);
		menuBar.add(menuGameOptions);

		JMenuItem mStartANewGame = new JMenuItem("New Game");
		mStartANewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_MASK));
		mStartANewGame.setFont(menuFont);
		mStartANewGame.addActionListener(e -> gameReset(true));
		menuGameOptions.add(mStartANewGame);

		JSeparator separator = new JSeparator();
		menuGameOptions.add(separator);

		JMenu menuGameSytle = new JMenu("Game style");
		menuGameSytle.setFont(menuFont);
		menuGameOptions.add(menuGameSytle);

		JMenuItem mExit = new JMenuItem("Leave the game");
		mExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
		mExit.setFont(menuFont);
		mExit.addActionListener(e -> System.exit(0));
		menuGameOptions.add(mExit);

		// ############################################################################################################
		// PLAYER vs. PLAYER

		ButtonGroup btnGroup = new ButtonGroup();

		JRadioButtonMenuItem mPvsP = new JRadioButtonMenuItem("Player vs. Player");
		mPvsP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
		mPvsP.setFont(menuFont);
		mPvsP.setSelected(true);
		mPvsP.addActionListener(e -> {
			if (gameMode != 1) {
				mPvsP.setSelected(true);
				gameMode = 1;
				gameReset(true);
			}
			System.out.println("Player vs Player");
		});
		btnGroup.add(mPvsP);
		menuGameSytle.add(mPvsP);

		// ############################################################################################################
		// PLAYER vs. AI

// Easy----------------------------
		JMenu mPvsC = new JMenu("Player vs. AI");
		mPvsC.setFont(menuFont);
		menuGameSytle.add(mPvsC);

		JRadioButtonMenuItem mPvsCEasy = new JRadioButtonMenuItem("Easy");
		mPvsCEasy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
		mPvsCEasy.addActionListener(e -> {
			if (gameMode != 21) {
				mPvsCEasy.setSelected(true);
				gameMode = 21;
				gameReset(true);
			}
			System.out.println("Player vs AI - easy");
		});
		mPvsCEasy.setFont(menuFont);
		mPvsC.add(mPvsCEasy);
		btnGroup.add(mPvsCEasy);

// Middle------------------------------
		JRadioButtonMenuItem mPvsCMiddle = new JRadioButtonMenuItem("Middle");
		mPvsCMiddle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));
		mPvsCMiddle.addActionListener(e -> {
			if (gameMode != 22) {
				mPvsCMiddle.setSelected(true);
				gameMode = 22;
				gameReset(true);
			}
			System.out.println("Player vs AI - middle");
		});
		mPvsCMiddle.setFont(menuFont);
		mPvsC.add(mPvsCMiddle);
		btnGroup.add(mPvsCMiddle);

// Impossible------------------------------
		JRadioButtonMenuItem mPvsCHard = new JRadioButtonMenuItem("Impossible");
		mPvsCHard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));
		mPvsCHard.addActionListener(e -> {
			if (gameMode != 23) {
				mPvsCHard.setSelected(true);
				gameMode = 23;
				gameReset(true);
			}
			System.out.println("Player vs AI - imposibble");
		});
		mPvsCHard.setFont(menuFont);
		mPvsC.add(mPvsCHard);
		btnGroup.add(mPvsCHard);

		blocks();

	}

	static class ActionHandle implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "b11":
				array2D(btns[0], 1, 1);
				break;
			case "b12":
				array2D(btns[1], 1, 2);
				break;
			case "b13":
				array2D(btns[2], 1, 3);
				break;
			case "b21":
				array2D(btns[3], 2, 1);
				break;
			case "b22":
				array2D(btns[4], 2, 2);
				break;
			case "b23":
				array2D(btns[5], 2, 3);
				break;
			case "b31":
				array2D(btns[6], 3, 1);
				break;
			case "b32":
				array2D(btns[7], 3, 2);
				break;
			case "b33":
				array2D(btns[8], 3, 3);
				break;
			}
		}

	}

	public static void blocks() {
		panel = new JPanel();
		panel.setBounds(200, 120, 600, 600);
		panel.setLayout(new GridLayout(3, 0));
		container.add(panel);
		panel.setVisible(true);

		JButton b11 = new JButton();
		b11.setText("");
		b11.setFocusable(false);
		b11.setBackground(Color.WHITE);
		b11.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b11);
		b11.repaint();
		b11.addActionListener(listenAction);
		b11.setActionCommand("b11");
		b11.setVisible(true);

		JButton b12 = new JButton();
		b12.setText("");
		b12.setFocusable(false);
		b12.setBackground(Color.WHITE);
		b12.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b12);
		b12.repaint();
		b12.addActionListener(listenAction);
		b12.setActionCommand("b12");
		b12.setVisible(true);

		JButton b13 = new JButton();
		b13.setText("");
		b13.setFocusable(false);
		b13.setBackground(Color.WHITE);
		b13.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b13);
		b13.repaint();
		b13.addActionListener(listenAction);
		b13.setActionCommand("b13");
		b13.setVisible(true);

		JButton b21 = new JButton();
		b21.setText("");
		b21.setFocusable(false);
		b21.setBackground(Color.WHITE);
		b21.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b21);
		b21.repaint();
		b21.addActionListener(listenAction);
		b21.setActionCommand("b21");
		b21.setVisible(true);

		JButton b22 = new JButton();
		b22.setText("");
		b22.setFocusable(false);
		b22.setBackground(Color.WHITE);
		b22.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b22);
		b22.repaint();
		b22.addActionListener(listenAction);
		b22.setActionCommand("b22");
		b22.setVisible(true);

		JButton b23 = new JButton();
		b23.setText("");
		b23.setFocusable(false);
		b23.setBackground(Color.WHITE);
		b23.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b23);
		b23.repaint();
		b23.addActionListener(listenAction);
		b23.setActionCommand("b23");
		b23.setVisible(true);

		JButton b31 = new JButton();
		b31.setText("");
		b31.setFocusable(false);
		b31.setBackground(Color.WHITE);
		b31.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b31);
		b31.repaint();
		b31.addActionListener(listenAction);
		b31.setActionCommand("b31");
		b31.setVisible(true);

		JButton b32 = new JButton();
		b32.setText("");
		b32.setFocusable(false);
		b32.setBackground(Color.WHITE);
		b32.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b32);
		b32.repaint();
		b32.addActionListener(listenAction);
		b32.setActionCommand("b32");
		b32.setVisible(true);

		JButton b33 = new JButton();
		b33.setText("");
		b33.setFocusable(false);
		b33.setBackground(Color.WHITE);
		b33.setFont(new Font("Tahoma", Font.PLAIN, 80));
		panel.add(b33);
		b33.repaint();
		b33.addActionListener(listenAction);
		b33.setActionCommand("b33");
		b33.setVisible(true);

		panel.repaint();
		panel.revalidate();
		btns = new JButton[] { b11, b12, b13, b21, b22, b23, b31, b32, b33 };
	}

	public static void array2D(JButton btn, int y, int x) {
		x--;
		y--;

		if (gameMode == 1) {
			if (array[y][x] == 0) {
				if (turn.equals("X")) {
					array[y][x] = 1;
					btn.setText("X");
					turn = "O";
				} else if (turn.equals("O")) {
					array[y][x] = -1;
					btn.setText("O");
					turn = "X";
				}
			}
			gameEnd();

		} else if (gameMode == 21) {

			if (array[y][x] == 0) {
				if (turn.equals("X")) {
					array[y][x] = 1;
					btn.setText("X");
					turn = "O";
					gameEnd();

					if (getEmptyPlaces().size() > 0) {
						Random random = new Random();
						int location = getEmptyPlaces().get(random.nextInt(getEmptyPlaces().size()));
						array[location / 10][location % 10] = -1;
						btns[((location / 10) * 3) + (location % 10)].setText(turn);
						turn = "X";
						gameEnd();
					}
				}
			}

		} else if (gameMode == 22) {

			if (array[y][x] == 0) {
				if (turn.equals("X")) {
					array[y][x] = 1;
					btn.setText("X");
					turn = "O";
					gameEnd();

					Random random = new Random();
					
					if (random.nextInt(10)+1 % 2 == 0) {
						if (getEmptyPlaces().size() > 0) {
							int location = getEmptyPlaces().get(random.nextInt(getEmptyPlaces().size()));
							array[location / 10][location % 10] = -1;
							btns[((location / 10) * 3) + (location % 10)].setText(turn);
							turn = "X";
							gameEnd();

						}
					} else {

						int max = Integer.MIN_VALUE;
						for (int point : getEmptyPlaces()) {
							array[point / 10][point % 10] = -1;
							int score = minimax(array, 0, false);
							array[point / 10][point % 10] = 0;
							if (score > max) {
								max = score;
								computersMove = point;
							}
						}
						array[computersMove / 10][computersMove % 10] = -1;
						btns[((computersMove / 10) * 3) + (computersMove % 10)].setText(turn);
						turn = "X";
						gameEnd();

					}
				}
			}

		} else if (gameMode == 23) {

			if (array[y][x] == 0) {
				if (turn.equals("X")) {
					array[y][x] = 1;
					btn.setText("X");
					turn = "O";
					gameEnd();

					int max = Integer.MIN_VALUE;
					for (int point : getEmptyPlaces()) {
						array[point / 10][point % 10] = -1;
						int score = minimax(array, 0, false);
						array[point / 10][point % 10] = 0;
						if (score > max) {
							max = score;
							computersMove = point;
						}
					}
					array[computersMove / 10][computersMove % 10] = -1;
					btns[((computersMove / 10) * 3) + (computersMove % 10)].setText(turn);
					turn = "X";
					gameEnd();

				}
			}

		}
	}

	public static void gameEnd() {
		if (Math.abs(array[0][0] + array[0][1] + array[0][2]) == 3
				|| Math.abs(array[1][0] + array[1][1] + array[1][2]) == 3
				|| Math.abs(array[2][0] + array[2][1] + array[2][2]) == 3
				|| Math.abs(array[0][0] + array[1][0] + array[2][0]) == 3
				|| Math.abs(array[0][1] + array[1][1] + array[2][1]) == 3
				|| Math.abs(array[0][2] + array[1][2] + array[2][2]) == 3
				|| Math.abs(array[0][0] + array[1][1] + array[2][2]) == 3
				|| Math.abs(array[0][2] + array[1][1] + array[2][0]) == 3) {

			if (turn.equals("X")) {
				colorBtns(Color.RED);
				JOptionPane.showMessageDialog(null, "O winns");
				numScoreO++;
				scoreO.setText(numScoreO + "");
				if (scoreX.getText().equals("X")) {
					scoreX.setText("0");
				}
			} else {
				colorBtns(Color.BLUE);
				JOptionPane.showMessageDialog(null, "X winns");
				numScoreX++;
				scoreX.setText(numScoreX + "");
				if (scoreO.getText().equals("O")) {
					scoreO.setText("0");
				}

			}

			gameReset(false);

			return;

		}

		if (getEmptyPlaces().size() == 0) {

			JOptionPane.showMessageDialog(null, "No one winns");
			if (scoreX.getText().equals("X")) {
				scoreX.setText("0");
			}
			if (scoreO.getText().equals("O")) {
				scoreO.setText("0");
			}

			gameReset(false);
		}
	}

	public static void colorBtns(Color color) {

		if (Math.abs(array[0][0] + array[0][1] + array[0][2]) == 3) {
			btns[0].setForeground(color);
			btns[1].setForeground(color);
			btns[2].setForeground(color);

		} else if (Math.abs(array[1][0] + array[1][1] + array[1][2]) == 3) {
			btns[3].setForeground(color);
			btns[4].setForeground(color);
			btns[5].setForeground(color);

		} else if (Math.abs(array[2][0] + array[2][1] + array[2][2]) == 3) {
			btns[6].setForeground(color);
			btns[7].setForeground(color);
			btns[8].setForeground(color);

		} else if (Math.abs(array[0][0] + array[1][0] + array[2][0]) == 3) {
			btns[0].setForeground(color);
			btns[3].setForeground(color);
			btns[6].setForeground(color);

		} else if (Math.abs(array[0][1] + array[1][1] + array[2][1]) == 3) {
			btns[1].setForeground(color);
			btns[4].setForeground(color);
			btns[7].setForeground(color);

		} else if (Math.abs(array[0][2] + array[1][2] + array[2][2]) == 3) {
			btns[2].setForeground(color);
			btns[5].setForeground(color);
			btns[8].setForeground(color);

		} else if (Math.abs(array[0][0] + array[1][1] + array[2][2]) == 3) {
			btns[0].setForeground(color);
			btns[4].setForeground(color);
			btns[8].setForeground(color);

		} else if (Math.abs(array[0][2] + array[1][1] + array[2][0]) == 3) {
			btns[2].setForeground(color);
			btns[4].setForeground(color);
			btns[6].setForeground(color);

		}

	}

	public static void gameReset(boolean pointsToo) {

		if (pointsToo) {
			turn = "X";
			numScoreO = 0;
			numScoreX = 0;
			scoreO.setText("O");
			scoreX.setText("X");
		}

		array = new int[3][3];

		
		for(int i = 0; i<9; i++) {
			btns[i].setText("");
			btns[i].setForeground(Color.BLACK);
			
		}
	}

	public static LinkedList<Integer> getEmptyPlaces() {
		LinkedList<Integer> output = new LinkedList<>();
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if (array[i][j] == 0) {
					output.add(Integer.parseInt(i + "" + j));
				}
			}
		}
		return output;

	}

	// ################################################################################################################
	// AI at work!!

	public static boolean findWinnerForAI(String player) {

		String winner = "";
		if (array[0][0] + array[0][1] + array[0][2] == 3 || array[1][0] + array[1][1] + array[1][2] == 3
				|| array[2][0] + array[2][1] + array[2][2] == 3 || array[0][0] + array[1][0] + array[2][0] == 3
				|| array[0][1] + array[1][1] + array[2][1] == 3 || array[0][2] + array[1][2] + array[2][2] == 3
				|| array[0][0] + array[1][1] + array[2][2] == 3 || array[0][2] + array[1][1] + array[2][0] == 3) {
			winner = "X";

		} else if (array[0][0] + array[0][1] + array[0][2] == -3 || array[1][0] + array[1][1] + array[1][2] == -3
				|| array[2][0] + array[2][1] + array[2][2] == -3 || array[0][0] + array[1][0] + array[2][0] == -3
				|| array[0][1] + array[1][1] + array[2][1] == -3 || array[0][2] + array[1][2] + array[2][2] == -3
				|| array[0][0] + array[1][1] + array[2][2] == -3 || array[0][2] + array[1][1] + array[2][0] == -3) {
			winner = "O";
		}

		return player.equals(winner);
	}

	public static int minimax(int[][] array, int depth, Boolean isMaximizing) {

		if (findWinnerForAI("X")) {
			return -1;
		}
		if (findWinnerForAI("O")) {
			return 1;
		}
		if (getEmptyPlaces().size() == 0) {
			return 0;
		}

		if (isMaximizing) {
			int bestscore = Integer.MIN_VALUE;
			for (int point : getEmptyPlaces()) {
				array[point / 10][point % 10] = -1;
				int score = minimax(array, depth + 1, false);
				array[point / 10][point % 10] = 0;
				bestscore = Math.max(bestscore, score);
			}
			return bestscore;
		} else {
			int bestscore = Integer.MAX_VALUE;
			for (int point : getEmptyPlaces()) {
				array[point / 10][point % 10] = 1;
				int score = minimax(array, depth + 1, true);
				array[point / 10][point % 10] = 0;
				bestscore = Math.min(score, bestscore);
			}
			return bestscore;
		}

	}
}

