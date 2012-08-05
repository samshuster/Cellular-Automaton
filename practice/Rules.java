package practice;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;


public class Rules {
	private final int[][] DEFAULT_TEMPLATE = {{0,1,0},{1,-1,1}};
	
	private Color[] colorSet;
	private int numOfCenters;
	private int numActive;
	//Which squares are considerable, -1 is a center square
	private int[][] template;
	//answers are what happens to the center cells
	private HashMap<Integer,int[]> answerSet;
	//Holds the x and y coordinates of all centers in order they are found from
	//Top left to bottom right. Also gives x and y coordinates in relation to
	//the top left square
	private ArrayList<Integer> xAnswerSet;
	private ArrayList<Integer> yAnswerSet;
	//Holds only the rules that are interesting
	private TreeSet<Integer> ruleSet;
	//Holds the rule wizard rules
	private ArrayList<SmartRules> smartRuleSet;
	private long numOptions;
	
	/**
	 * Default Constructor
	 */
	public Rules(){
		makeDefaultColorSet();
		makeDefaultTemplate();
	}
	/**
	 * The Operator enum
	 * @author Sam
	 *
	 */
	private enum Operator{
		GREATEREQUAL(">="),
		GREATER(">"),
		LESSEREQUAL("<="),
		LESSER("<"),
		EQUAL("="),
		NOTEQUAL("!=");
		private final String rep;
		Operator(String rep){
			this.rep = rep;
		}
	};
	
	public int getLargestSide(){
		return Math.max(template.length, template[0].length);
	}
	
	/**
	 * Making rules based on old rule
	 * @param rule the old rule
	 */
	public Rules(Rules rule){
		this.colorSet = rule.getColorSet();
		this.numOfCenters = rule.getNumOfCenters();
		this.template = rule.getTemplate();
		this.answerSet = rule.getAnswerSet();
		this.ruleSet = rule.getRuleSet();
		this.numActive = rule.getNumActive();
		this.xAnswerSet = rule.getXAnswerSet();
		this.yAnswerSet = rule.getYAnswerSet();
		this.smartRuleSet = rule.getSmartRuleSet();
	}

	/**
	 * Makes a default color set
	 */
	private void makeDefaultColorSet(){
		colorSet = new Color[]{Color.WHITE, Color.BLACK};
	}
	/**
	 * Makes a default template
	 */
	private void makeDefaultTemplate(){
		setTemplate(DEFAULT_TEMPLATE);	
	}
	
	/**
	 * Creates rule set, default answer set and linear and normal templates
	 * @param t the user defined template frame
	 */
	public void setTemplate(int[][] t){
		int height = t.length;
		int width = t[0].length;
		this.smartRuleSet = new ArrayList<SmartRules>();
		this.template = new int[height][width];
		this.numOfCenters = 0;
		this.numActive = 0;
		xAnswerSet = new ArrayList<Integer>();
		yAnswerSet = new ArrayList<Integer>();
		for(int i = 0; i<height; i++){
			for(int j = 0; j<width; j++){
				int cur = t[i][j];
				this.template[i][j] = cur;
				if(cur!=0){
					this.numActive += 1;
				}
				if(cur==-1){
					this.numOfCenters += 1;
					xAnswerSet.add(j);
					yAnswerSet.add(i);
				}
			}
		}
		this.clearRules();
	}
	/**
	 * Clears the current rule and answer objects
	 */
	private void clearRules(){
		recalculateNumOptions();
		answerSet = new HashMap<Integer,int[]>();
		ruleSet = new TreeSet<Integer>();
	}
	/**
	 * Recalculate the number of available options
	 */
	private void recalculateNumOptions(){
		numOptions = (long)Math.pow(colorSet.length, this.numActive);
	}
	
	/**
	 * Makes a rule set with random rules
	 * Eventually need to add functionality to allow for random range only
	 * @param numActive
	 */
	private void makeRandomRules(){
		Random rand = new Random();
		for(int curRule = 0; curRule<numOptions; curRule++){
			int[] correctDig = this.findCenterDigits(curRule);
			boolean interesting = false;
			int[] newDig = new int[numOfCenters];
			for(int i = 0; i<numOfCenters; i++){
				newDig[i] = rand.nextInt(colorSet.length);
				if(newDig[i] != correctDig[i]){
					interesting = true;
				}
			}
			if(interesting){
				addRule(curRule, newDig);
			}
		}
	}
	
	/**
	 * Finds the center digits based on ruleNum
	 * @param ruleNum
	 * @return the value of the center digits
	 */
	public int[] findCenterDigits(int ruleNum){
		int[][] active = this.convertToActiveMatrix(ruleNum);
		int[] ret = findCenterDigits(active);
		return ret;
	}
	/**
	 * Finds the center digits based on activation matrix
	 * @param activation matrix
	 * @return the value of the center digits
	 */
	public int[] findCenterDigits(int[][] activation){
		int[] ret = new int[numOfCenters];
		for(int i = 0; i<numOfCenters; i++){
			int xOffSet = this.xAnswerSet.get(i);
			int yOffSet = this.yAnswerSet.get(i);
			ret[i] = activation[yOffSet][xOffSet];
		}
		return ret;
	}
	
	/**
	 * Takes an activation matrix and returns the rule num
	 * @param the activation matrix
	 * @return the rule num
	 */
	public int convertToRuleNum(int[][] activation){
		int dig = 0;
		int curMult = 1;
		for(int i = 0; i<activation.length; i++){
			for(int j = 0; j<activation[i].length; j++){
				if(template[i][j] != 0){
					dig += activation[i][j]*curMult;
					curMult = curMult * colorSet.length;
				}
			}
		}
		return dig;
	}
	
	/**
	 * Takes a rule num and converts to activation matrix based on template
	 * @param ruleNum
	 * @return the activation matrix
	 */
	public int[][] convertToActiveMatrix(int ruleNum){
		int[][] ret = new int[template.length][template[0].length];
		int chunkState = ruleNum;
		for(int i = 0; i<template.length; i++){
			for(int j = 0; j<template[i].length; j++){
				if(this.template[i][j] != 0){
					int dig = chunkState % colorSet.length;
					ret[i][j] = dig;
					chunkState = chunkState - dig;
					chunkState = chunkState / colorSet.length;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Converts a ruleNum to an answer matrix
	 * @param ruleNum
	 * @return the answer matrix
	 */
	public int[][] convertToAnswerMatrix(int ruleNum){
		int[][] ret = convertToActiveMatrix(ruleNum);
		for(int i = 0; i<numOfCenters; i++){
			int xOffSet = this.xAnswerSet.get(i);
			int yOffSet = this.yAnswerSet.get(i);
			ret[yOffSet][xOffSet] = this.answerSet.get(ruleNum)[i];
		}
		return ret;
	}
	
	/**
	 * Converts a rule num to a linear matrix
	 * @param ruleNum
	 * @return the linear matrix
	 */
	public int[] convertToLinearMatrix(int ruleNum){
		int[] ret = new int[numActive];
		int chunkState = ruleNum;
		int k = 0;
		for(int i = 0; i<template.length; i++){
			for(int j = 0; j<template[i].length; j++){
				if(this.template[i][j] != 0){
					int dig = chunkState % colorSet.length;
					ret[k] = dig;
					k++;
					chunkState = chunkState - dig;
					chunkState = chunkState / colorSet.length;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Deletes all rules
	 * @param c the panel containing the rules
	 */
	public void deleteAll(){
		this.clearRules();
	}
	
	/**
	 * Randomly makes new rules on top of existing rules
	 */
	public void randomizeAll(){
		this.clearRules();
		this.makeRandomRules();
	}
	
	/**
	 * Adds a rule
	 * @param before activation matrix
	 * @param after activation matrix
	 */
	public void addRule(int[][] be, int[][] af){
		int ruleNum = convertToRuleNum(be);
		int ruleNumAfter = convertToRuleNum(af);
		if(ruleNum == ruleNumAfter) return;
		answerSet.put(ruleNum, findCenterDigits(af));
		ruleSet.add(ruleNum);
	}
	/**
	 * Adds a rule
	 * @param ruleNum to be added
	 * @param centerDig answer set to be added
	 */
	public void addRule(int ruleNum, int[] centerDig){
		int[] centerDigOrig = this.findCenterDigits(ruleNum);
		boolean valid = false;
		for(int i = 0; i<centerDig.length; i++){
			if(centerDigOrig[i] != centerDig[i]){
				valid = true;
			}
		}
		if(valid){
			answerSet.put(ruleNum, centerDig);
			ruleSet.add(ruleNum);
		} else {
			answerSet.put(ruleNum, centerDig);
		}
	}
	
	/**
	 * Deletes a rule
	 * @param the rule num to delete
	 */
	public void deleteRule(int ruleNum){
		if(ruleSet.contains(ruleNum)){
			ruleSet.remove(ruleNum);
		}
		answerSet.remove(ruleNum);
	}
	
	/**
	 * Add a color to the colorSet
	 * @param c the color to be added
	 */
	public void addColor(Color c){
		Color[] newC = new Color[colorSet.length+1];
		for(int i = 0; i<colorSet.length; i++){
			newC[i] = colorSet[i];
		}
		newC[colorSet.length] = c;
		colorSet = newC;
	}
	
	/**
	 * Calculates the answer set for a given rule
	 * Also memoizes to improve performance
	 * @param ruleNum
	 * @return the answer set for the particular rule
	 */
	public int[] calculateRule(int ruleNum){
		if(this.answerSet.containsKey(ruleNum)){
			return answerSet.get(ruleNum);
		}
		int[] lin = this.convertToLinearMatrix(ruleNum);
		int[] confirmed = new int[]{-1};
		for(int i = 0; i<this.smartRuleSet.size(); i++){
			if(smartRuleSet.get(i).processRuleNum(lin)){
				if(confirmed[0] == -1){
					confirmed = smartRuleSet.get(i).getAnswerSet();
				} else {
					int[] temp = smartRuleSet.get(i).getAnswerSet();
					for(int k = 0; k<temp.length; k++){
						if(temp[k] != confirmed[k]){
							throw new IllegalArgumentException();
						}
					}
				}
			}
		}
		if(confirmed[0] != -1){
			this.addRule(ruleNum, confirmed);
			return confirmed;
		}
		int[] norm = findCenterDigits(ruleNum);
		this.addRule(ruleNum, norm);
		return norm;
	}
	
	/**
	 * Processes a smart rule and adds it to the smart rule repository
	 * NOTE: PROBS BETTER TO ADD ALL SMART RULES THAN AFTER MAKING EACH ONE IN RECURSIVE
	 * FUNCTION, DECIDE IF IT MEETS ALL CRITERIA
	 * @param op
	 * @param num
	 * @param colors
	 */
	public void smartRuleProcessing(SmartRules[] sr){
		/*int[] newC = this.findCenterDigits(cColor.getActive());
		int[][] b = bounds.getActive();
		ArrayList<Integer> ruleNums = enumerate(b, num, num2, colors, op, op2);
		smartRuleSet.put((new SmartRule(op,num,op2,num2,colors,cColor,bounds)), ruleNums);
		for(int i = 0; i<ruleNums.size(); i++){
			this.deleteRule(ruleNums.get(i));
			this.addRule(ruleNums.get(i), newC);
		}*/
	}
	
	/**
	 * Will enumerate through n choose k
	 * @param bounds
	 * @param choose
	 * @param op
	 * @param colors
	 * @return the array of rule nums to be added
	 */
	private ArrayList<Integer> enumerate(int[][] bounds, int k, int k2, int[] colors, int op, int op2){
		int n = 0;
		for(int i = 0; i<bounds.length; i++){
			for(int j = 0; j<bounds[i].length; j++){
				if(this.template[i][j] != 0 && bounds[i][j] == 1){
					n++;
				}
			}
		}
		ArrayList<Integer> cont = new ArrayList<Integer>();
		for(int i = 0; i<=n; i++){
			if(opTest(op,i,k) && opTest(op2,i,k2)){
				enumerateHelper(cont, 0, bounds, i, n, 0, 0, 1, colors);
			}
		}
		return cont;
	}
	/**
	 * Recursive method
	 * @param container - the holder of the ruleNums
	 * @param active - the current working ruleNum
	 * @param k - the current amount of squares that have been activated recursively
	 * @param n - the number of viable squares remaining
	 * @param x - the current x position
	 * @param y - the current y position
	 * @param curMult - the current multiplication factor
	 * @param colors - the colors to be considered as viable
	 */
	private void enumerateHelper(ArrayList<Integer> container, int active, int[][] bounds, int k, int n, int x, int y, int curMult, int[] colors){
		//Reset the bounds if its off
		if(x>bounds[0].length-1){
			x = 0;
			y++;
		}
		//BASE CASE: If we are through the template than add to container and return
		if(y>bounds.length-1){
			container.add(active);
			return;
		}
		while(template[y][x] == 0){
			x++;
			if(x>bounds[0].length-1){
				x = 0;
				y++;
			}
			if(y>bounds.length-1){
				container.add(active);
				return;
			}
		}
		int newX = x+1;
		int newCurMult = curMult*this.colorSet.length;
		//Must declare the rest of the variables inside loop so they reset on subsequent loops
		if(bounds[y][x] == 1 && k==n && k!=0){
			for(int i = 0; i<colors.length; i++){
				int newK = k-1;
				int newN = n-1;
				int newActive = active+(curMult*colors[i]);
				enumerateHelper(container, newActive, bounds, newK, newN, newX, y, newCurMult, colors);
			}
		} else {
			for(int i = 0; i<this.colorSet.length; i++){
				boolean valid = true;
				int newN = n;
				int newK = k;
				int newActive = active+(curMult*i);
				if(bounds[y][x] == 1){
					newN -= 1;
					for(int j = 0; j<colors.length; j++){
						if(i == colors[j]){
							if(k>0){
								newK -= 1;
							} else {
								valid = false;
							}
							break;
						}
					}
				}
				//Only call recursive call if we haven't surpassed k
				if(valid){
					enumerateHelper(container, newActive, bounds, newK, newN, newX, y, newCurMult, colors);
				}
			}
		}
	}
	/**
	 * Used to return the boolean result of a boolean expression
	 */
	private boolean opTest(int op, int arg1, int arg2){
		switch(op){
		case 0:
			return arg1 >= arg2;
		case 1:
			return arg1 > arg2;
		case 2:
			return arg1 <= arg2;
		case 3:
			return arg1 < arg2;
		case 4:
			return arg1 == arg2;
		case 5:
			return arg1 != arg2;
		default:
			return arg1 > arg2;
		}		
	}
	/**
	 * Getter method for Color Set
	 * @return The Current Color Set
	 */
	public Color[] getColorSet(){
		return colorSet;
	}

	/**
	 * @return the answerSet
	 */
	public HashMap<Integer,int[]> getAnswerSet() {
		return answerSet;
	}
	
	/**
	 * @return the template
	 */
	public int[][] getTemplate() {
		return template;
	}
	
	/**
	 * @return the numOfCenters
	 */
	public int getNumOfCenters() {
		return numOfCenters;
	}
	
	/**
	 * 
	 * @return the interesting rule set
	 */
	public TreeSet<Integer> getRuleSet() {
		return this.ruleSet;
	}
	
	/**
	 * 
	 * @return num active
	 */
	public int getNumActive() {
		return this.numActive;
	}

	/**
	 * @return the yAnswerSet
	 */
	public ArrayList<Integer> getYAnswerSet() {
		return yAnswerSet;
	}

	/**
	 * @return the xAnswerSet
	 */
	public ArrayList<Integer> getXAnswerSet() {
		return xAnswerSet;
	}

	/**
	 * @return the smartRuleSet
	 */
	public ArrayList<SmartRules> getSmartRuleSet() {
		return smartRuleSet;
	}
	
	/**
	 * gets the symbols from the enum
	 * @return the string set of symbols
	 */
	public String[] getOps(){
		String[] ret = new String[6];
		Operator[] op = Operator.values();
		for(int i = 0; i<op.length; i++){
			ret[i] = op[i].rep;
		}
		return ret;
	}
	
	public void setSmartRuleSet(ArrayList<SmartRules> sr){
		this.smartRuleSet = sr;
	}
}
