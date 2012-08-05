package practice;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JTextField;
import javax.swing.Timer;

public class AutomataEngine {
	//Default time delay
	public final static int DEFAULT_DELAY = 500;
	//Rules object containing all necessary rule information
	private Rules rules;
	//Fake board to hold squares
	private Square[][] fBoard;
	//Timer object
	private Timer time;
	private JTextField curGen;
	private int[][] template;
	private HashMap<Integer,int[]> answerSet;
	
	/**
	 * Constructor
	 * @param rule - the rule set to be used
	 * @param fBoard - the mimic board to be used for data
	 * @param curGen - the curGen container
	 */
	public AutomataEngine(Rules rule, Square[][] fBoard, JTextField curGen){
		rules = rule;
		this.fBoard = fBoard;
		this.curGen = curGen;
		this.time = new Timer(DEFAULT_DELAY, new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		        tick();
		    }
		});
		template = rules.getTemplate();
		answerSet = rules.getAnswerSet();
	}
	/**
	 * Start the Simulation
	 */
	public void start(){
		if(time.isRunning()) return;
		for(Square[] sl:fBoard){
			for(Square s:sl){
				s.removeMouseListener(s);
			}
		}
		time.start();
	}
	
	/**
	 * Stops the simulation
	 */
	public void stop(){
		if(!time.isRunning()) return;
		time.stop();
		for(Square[] sl:fBoard){
			for(Square s:sl){
				s.addMouseListener(s);
			}
		}
	}
	
	/**
	 * One tick of the simulation
	 */
	private void tick(){
		int[][] tempBoard = new int[fBoard.length][fBoard.length];
		int colorLength = rules.getColorSet().length;
		//Iterate through every square where the square will serve as top
		//left position for template
		for(int i = 0; i<fBoard.length; i++){
			for(int j = 0; j<fBoard[i].length; j++){
				//chunkState will determine which rule will be used
				//curMultiplier helps performance
				int chunkState = 0;
				int curMultiplier = 1;
				//This is iterating through template at position i,j
				for(int k = i; k<i+template.length; k++){
					for(int p = j; p<j+template[0].length; p++){
						//If the template actually contains something lets see if real thing does
						if(template[k-i][p-j] != 0){
							chunkState += fBoard[k%fBoard.length][p%fBoard.length].getType()*curMultiplier;
							curMultiplier = curMultiplier*colorLength;
						}
					}
				}
				int[] newDig = rules.calculateRule(chunkState);
				for(int k = 0; k<newDig.length; k++){
					//need to generalize to cover multiple centers
					tempBoard[(i+rules.getYAnswerSet().get(k))%tempBoard.length]
						     [(j+rules.getXAnswerSet().get(k))%tempBoard.length] = newDig[k];
				}
			}
		}
		//Transfer changes to the actual board
		for(int i = 0; i<tempBoard.length; i++){
			for(int j = 0; j<tempBoard.length; j++){
				fBoard[i][j].setType(tempBoard[i][j]);
			}
		}
		Integer cGen = Integer.parseInt(curGen.getText());
		cGen++;
		curGen.setText(cGen.toString());
	}
	
	/**
	 * Finishes the current simulation while giving option to save initial settings
	 */
	public void finish(){
		// TODO need to add saving function
	}
	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return time.isRunning();
	}
	
	public void changeSpeed(int speed){
		time.setDelay(speed);
	}
	
	public void resetScreen(){
		this.stop();
		this.curGen.setText("0");
		for(int i = 0; i<fBoard.length; i++){
			for(int j = 0; j<fBoard[i].length; j++){
				fBoard[i][j].setType(0);
			}
		}
		
	}
}
