package practice;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


public class AutomataDisplay extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4650371836502914232L;
	//final Constant Values
	final int GAMEWIDTH = 700;
	final int GAMEHEIGHT = 750;
	final int BOARDWIDTH = 700;
	final int BOARDHEIGHT = 600;
	private JPanel BoardDisplay;
	private JPanel Board;
	private JScrollPane BoardScroll;
	//The Engine
	private AutomataEngine engine;
	//A mimic board
	private Square[][] fBoard;
	//The rule container
	private Rules rules;
	//The number of tiles to be used
	private int numTiles;
	//Field that displays curGen
	private JTextField curGen;
	//Square length of display
	private int squareLength;
	
	/**
	 * Constructor
	 * @param numTiles
	 * @param name
	 * @param rule
	 */
	public AutomataDisplay(int numTiles, String name, Rules rule)
	{
		super(name);
		setSize(new Dimension(GAMEWIDTH, GAMEHEIGHT));
		setFocusable(true);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		//Sets class variables
		this.numTiles = numTiles;
		this.rules = rule;
		squareLength = this.BOARDHEIGHT;
		BuildBoard();
	}
	/**
	 * Will build a new Automata board
	 */
	private void BuildBoard()
	{
		//Builds the Cellular Automata Board
		Board = new JPanel();
		BoardScroll = new JScrollPane(Board);
		this.add(BoardScroll, BorderLayout.NORTH, JLayeredPane.DEFAULT_LAYER);
		Board.setLayout(new FullyJustifiedGridLayout(numTiles,numTiles));
		Board.setPreferredSize(new Dimension(squareLength, squareLength));
		BoardScroll.setPreferredSize(new Dimension(BOARDWIDTH, BOARDHEIGHT));
		fBoard = new Square[numTiles][numTiles];
		for (int i = 0; i < numTiles; i++) {
			for(int j = 0; j<numTiles; j++){
	            Square square = new Square(rules.getColorSet());
	            fBoard[i][j] = square;
	            square.addMouseListener(square);
	            Board.add(square);
			}
        }
		//Configures the Board Display which holds the buttons
		configureButtons();
		//Create the engine
		this.engine = new AutomataEngine(rules, fBoard, curGen);
	}
	/**
	 * Helper method that will configure the buttons
	 */
	private void configureButtons(){
		BoardDisplay = new JPanel(new GridBagLayout());
		BoardDisplay.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		BoardDisplay.setBackground(Color.white);
		this.add(BoardDisplay, null, JLayeredPane.DEFAULT_LAYER);
		final JButton startStop = new JButton("Start Simulation");
		startStop.setActionCommand("Start");
		ActionListener startSim = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("Start")){
					engine.start();
					startStop.setText("Stop Simulation");
					startStop.setActionCommand("Stop");
				} else {
					engine.stop();
					startStop.setText("Start Simulation");
					startStop.setActionCommand("Start");
				}
			}
		};
		startStop.addActionListener(startSim);
		JButton finish = new JButton("Finish");
		finish.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				engine.finish();
				finish();
			}
		});
		curGen = new JTextField("0",5);
		curGen.setEditable(false);
		//Labels
		JLabel generation = new JLabel("Current Generation:");
		JLabel speedText = new JLabel("Current Speed:");
		final JTextField speedField = new JTextField(AutomataEngine.DEFAULT_DELAY+"",6);
		JButton changeSpeed = new JButton("Change Speed");
		changeSpeed.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				engine.changeSpeed(Integer.parseInt(speedField.getText()));
			}
		});
		JButton resetScreen = new JButton("Reset Screen");
		resetScreen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				engine.resetScreen();
			}
		});
		JLabel displayText = new JLabel("Set size of Display");
		final JTextField displayField = new JTextField(""+this.squareLength,6);
		JButton changeDisplay = new JButton("Change Display");
		changeDisplay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				squareLength = Integer.parseInt(displayField.getText());
				Board.setPreferredSize(new Dimension(squareLength, squareLength));
				BoardScroll.revalidate();
				BoardScroll.repaint();
			}
		});
		//Add stuff to BoardDisplay at bottom of screen
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.gridx = GridBagConstraints.RELATIVE;
		BoardDisplay.add(startStop,c);
		BoardDisplay.add(speedText,c);
		BoardDisplay.add(speedField,c);
		BoardDisplay.add(changeSpeed,c);
		BoardDisplay.add(generation,c);
		BoardDisplay.add(curGen,c);
		BoardDisplay.add(finish,c);
		c.gridy = 1;
		BoardDisplay.add(displayText,c);
		BoardDisplay.add(displayField,c);
		BoardDisplay.add(changeDisplay,c);
		BoardDisplay.add(resetScreen,c);
	}
	
	private void finish(){
		this.dispose();
	}
}
