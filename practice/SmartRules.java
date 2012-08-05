package practice;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SmartRules extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] operators;
	Color[] colorSet;
	int[][] template;
	int size;
	JPanel rulePanel;
	ArrayList<Var> varRules;
	int curRule;
	TextFieldTemplate tbe;
	TextFieldTemplate taf;
		
	public SmartRules(String[] operators, Color[] colorSet, int[][] template){
		this.operators = operators;
		this.colorSet = colorSet;
		this.template = template;
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		size = Math.max(template.length, template[0].length);
		varRules = new ArrayList<Var>();
		curRule = 0;
		this.rulePanel = new JPanel(new GridBagLayout());
		tbe = new TextFieldTemplate(size,template,false);
		tbe.setPreferredSize(new Dimension(100,100));
		taf = new TextFieldTemplate(size,template,true);
		taf.setPreferredSize(new Dimension(100,100));
		JLabel w3 = new JLabel("Then change center(s) to following colors");
		final GridBagConstraints c = new GridBagConstraints();
		c.gridx = GridBagConstraints.RELATIVE;
		c.gridy = 0;
		JButton addVariableRule = new JButton("Add Variable");
		addVariableRule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				addVariableRule();
				rulePanel.revalidate();
				rulePanel.repaint();
			}
		});JButton deleteRule = new JButton("Delete Rule");
		deleteRule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Container f = rulePanel.getParent();
				Container c = f.getParent();
				c.remove(f);
				c.validate();
				c.repaint();
			}
		});
		this.add(rulePanel,c);
		rulePanel.add(tbe,c);
		rulePanel.add(w3,c);
		rulePanel.add(taf,c);
		c.gridy = 1;
		rulePanel.add(addVariableRule,c);
		rulePanel.add(deleteRule,c);
		
	}
	private class Var{
		int name;
		int op1;
		int op2;
		int num1;
		int num2;
		int[] colors;
		public Var(int na, int o1, int o2, int nu1, int nu2, int[] colors){
			this.name = na;
			this.op1 = o1;
			this.op2 = o2;
			this.num1 = nu1;
			this.num2 = nu2;
			this.colors = colors;
		}
	}
	/**
	 * Adds a variable counter rule to this smart Rule
	 * NOTE THAT VARIABLE RULES ARE CALCULATED AS AND
	 */
	private void addVariableRule(){
		final JPanel variableRule = new JPanel(new GridBagLayout());
		final Var v = new Var(curRule,0,0,0,0,new int[]{0});
		curRule++;
		varRules.add(v);
		JLabel title = new JLabel("RULE: #" + v.name);
		JLabel w1 = new JLabel("In bounds selected above, if there are:");
		final JTextField number = new JTextField("0", 3);
		number.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
			}
			public void focusLost(FocusEvent arg0) {
				v.num1 = Integer.parseInt(number.getText());
			}
		});
		final JTextField number2 = new JTextField("0", 3);
		number2.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
			}
			public void focusLost(FocusEvent arg0) {
				v.num2 = Integer.parseInt(number2.getText());
			}
		});
		JLabel and = new JLabel("AND");
		final JComboBox selectOperator = new JComboBox(operators);
		selectOperator.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				v.op1 = selectOperator.getSelectedIndex();
			}
		});
		final JComboBox selectOperator2 = new JComboBox(operators);
		selectOperator2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				v.op2 = selectOperator2.getSelectedIndex();
			}
		});
		JLabel colorLabel = new JLabel("squares with the following colors:");
		final JTextField colors = new JTextField(8);
		colors.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
			}
			public void focusLost(FocusEvent arg0) {
				v.colors = csvParser(colors.getText());
			}
		});
		//Layout
		GridBagConstraints c1 = new GridBagConstraints();
		JButton delete = new JButton("Delete Variable");
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				rulePanel.remove(variableRule);
				varRules.remove(varRules.indexOf(v));
				rulePanel.revalidate();
				rulePanel.repaint();
			}
		});
		c1.gridx = GridBagConstraints.RELATIVE;
		c1.gridy = 0;
		variableRule.add(title,c1);
		c1.gridy = 1;
		variableRule.add(w1,c1);
		variableRule.add(selectOperator,c1);
		variableRule.add(number,c1);
		c1.gridy = 2;
		variableRule.add(and, c1);
		variableRule.add(selectOperator2,c1);
		variableRule.add(number2,c1);
		c1.gridy = 3;
		variableRule.add(colorLabel,c1);
		variableRule.add(colors,c1);
		c1.gridy = 4;
		variableRule.add(delete,c1);
		c1.gridx = 1;
		c1.gridy = GridBagConstraints.RELATIVE;
		rulePanel.add(variableRule,c1);
	}
	
	private int[] csvParser(String str){
		if(str.equals("")){
			return new int[]{};
		}
		String[] results = str.split(",");
		int[] ret =  new int[results.length];
		for(int i = 0; i<results.length; i++){
			ret[i] = Integer.parseInt(results[i]);
		}
		return ret;
	}
	
	/**
	 * Takes in a bit string and returns whether the test has passed or not
	 * Values can be then received from the hashmap
	 * @param ruleNum
	 * @return whether it meets smart rule criteria
	 */
	public boolean processRuleNum(int[] ruleNum){
		boolean a = true;
		for(int i = 0; i<varRules.size(); i++){
			Var v = varRules.get(i);
			ArrayList<Integer> indices = tbe.getVariableIndices(v.name);
			int count = 0;
			for(int j = 0; j<indices.size(); j++){
				for(int k = 0; k<v.colors.length; k++){
					if(ruleNum[indices.get(j)]==v.colors[k]){
						count++;
						break;
					}
				}
			}
			a = a && opTest(v.op1,count,v.num1);
			a = a && opTest(v.op2,count,v.num2);
		}
		if(a){
			String[] bounds = tbe.getActive();
			for(int k = 0; k<bounds.length; k++){
				a = a&&isAllowed(ruleNum[k],bounds[k]);
			}
		}
		return a;
	}
	
	/**
	 * Returns the answer set of the smart rule
	 * @return the answer set
	 */
	public int[] getAnswerSet(){
		ArrayList<String> t = this.taf.getCenters();
		int[] ret = new int[t.size()];
		for(int i = 0; i<t.size(); i++){
			ret[i] = Integer.parseInt(t.get(i));
		}
		return ret;
	}
	
	/**
	 * Checks to see if a color is allowed in a certain square based off of
	 * reg exp rules
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean isAllowed(int a, String b){
		//NEED TO MAKE MORE USER FRIENDLY
		String[] ret = b.split("@");
		if(ret[1].charAt(0) == '*'){
			return true;
		}
		if(ret[1].charAt(0) == '!'){
			String[] n1 = ret[1].split("(");
			String[] n2 = n1[1].split(")");
			String[] csv = n2[0].split(",");
			for(int i = 0; i<csv.length; i++){
				if(a == Integer.parseInt(csv[i])){
					return false;
				}
			}
		}
		String[] csv = ret[1].split(",");
		for(int i = 0; i<csv.length; i++){
			if(a == Integer.parseInt(csv[i])){
				return true;
			}
		}
		return false;
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
	
}
