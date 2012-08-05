package practice;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;

public class AutomataMain {
	
	private JFrame mainFrame;
	private Rules rules;
	private Rules tempRules;
	
	private JFrame ruleFrame;
	private JPanel mainRulePanel;
	private JScrollPane ruleScrollPane;
	private JPanel ruleAddPanel;
	private JFrame smartRulePane;
	private JPanel smartRulePanel;
	
	private final int DEFAULT_NUMDISPLAY = 4;
	/**
	 * Constructor of the Automata Construction panel
	 */
	private AutomataMain(){
		mainFrame = new JFrame("Cellular Automata");
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JButton startSimulation = new JButton("Start Simulation");
		final JButton loadSimulation = new JButton("Load Simulation");
		final JButton setRules = new JButton("Set Rules");
		setRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				setRules();
			}
		});
		final JTextField numTiles = new JTextField("25",5);
		final JTextField nameSim = new JTextField("SampleSim",20);
		final JLabel nameLabel = new JLabel("Name of Sim:");
		final JLabel numLabel = new JLabel("Number of Tiles:");
		startSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSim(numTiles.getText(), nameSim.getText());
			}
		});
		//Display Settings
		JPanel mainPanel = new JPanel();
		JPanel secondPanel = new JPanel();
		JPanel thirdPanel = new JPanel();
		thirdPanel.setSize(100,50);
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainFrame.dispose();
			}
		});
		mainPanel.add(loadSimulation);
		mainPanel.add(startSimulation);
		mainPanel.add(setRules);
		secondPanel.add(numLabel);
		secondPanel.add(numTiles);
		secondPanel.add(nameLabel);
		secondPanel.add(nameSim);
		thirdPanel.add(exit);
		mainFrame.add(mainPanel,BorderLayout.NORTH);
		mainFrame.add(secondPanel, BorderLayout.CENTER);
		mainFrame.add(thirdPanel, BorderLayout.SOUTH);
		mainFrame.pack();
		mainFrame.setVisible(true);	
		//Make Default Rules
		rules = new Rules();
	}
	
	/**
	 * Creates a new frame and panel
	 * @param num the number of tiles to create a new Sim of
	 */
	private void startSim(String num, String name){
		int numTiles = 0;
		if(num == null){
			throw new IllegalArgumentException();
		} else {
			numTiles = Integer.parseInt(num);
		}
		AutomataDisplay sz = new AutomataDisplay(numTiles, name, rules);
		sz.setVisible(true);
	}
	
	/**
	 * Manage the rule selection
	 */
	private void setRules(){
		//Init
		tempRules = new Rules(rules);
		ruleFrame = new JFrame("Set Rules");
		JPanel masterRulePanel = new JPanel(new GridBagLayout());
		ruleFrame.add(masterRulePanel);
		ruleFrame.setResizable(false);
		ruleFrame.setSize(700,600);
		ruleFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		ruleFrame.setVisible(true);
		
		mainRulePanel = new JPanel(new GridBagLayout());
		ruleScrollPane = new JScrollPane(mainRulePanel);
		ruleScrollPane.setPreferredSize(new Dimension(600,500));
		ruleAddPanel = new JPanel();
		ruleAddPanel.setPreferredSize(new Dimension(600,75));
		JPanel panelOne = new JPanel();
		JPanel panelTwo = new JPanel();
		JPanel panelThree = new JPanel();
		JPanel panelFour = new JPanel();
		JButton saveExit = new JButton("Save and Exit");
		saveExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rules = tempRules;
				ruleFrame.dispose();
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ruleFrame.dispose();
			}
		});
		JButton deleteAll = new JButton("Delete All");
		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempRules.deleteAll();
				displayRules();
			}
		});
		JButton randomRules = new JButton("Randomize");
		randomRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempRules.randomizeAll();
				displayRules();
			}
		});
		final JTextField templateSize = new JTextField(""+tempRules.getLargestSide(),3);
		JButton setTemplate = new JButton("Template");
		setTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayTemplatePane(templateSize);
			}
		});
		JLabel widthLabel = new JLabel("Width:");
		JLabel heightLabel = new JLabel("Height:");
		JLabel rgbValue = new JLabel("Enter RGB value:");
		final JTextField redAmount = new JTextField("",3);
		final JTextField greenAmount = new JTextField("",3);
		final JTextField blueAmount = new JTextField("",3);
		JButton addColor = new JButton("Add Color");
		addColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int red = Integer.parseInt(redAmount.getText());
				int green = Integer.parseInt(greenAmount.getText());
				int blue = Integer.parseInt(blueAmount.getText());
				tempRules.addColor(new Color(red,green,blue));
			}
		});
		displayRules();
		displayAdditionTemplate();
		//Set up the positions
		panelOne.add(setTemplate);
		panelOne.add(widthLabel);
		panelOne.add(templateSize);
		panelOne.add(heightLabel);
		panelTwo.add(saveExit);
		panelTwo.add(cancel);
		panelTwo.add(deleteAll);
		panelTwo.add(randomRules);
		panelThree.add(new JLabel("Current Interesting Rules"));
		panelFour.add(rgbValue);
		panelFour.add(redAmount);
		panelFour.add(greenAmount);
		panelFour.add(blueAmount);
		panelFour.add(addColor);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		masterRulePanel.add(panelOne, c);
		masterRulePanel.add(panelThree, c);
		masterRulePanel.add(ruleAddPanel,c);
		masterRulePanel.add(ruleScrollPane, c);
		masterRulePanel.add(panelTwo, c);
		masterRulePanel.add(panelFour,c);
		ruleFrame.pack();
	}
	 
	/**
	 * Responsible for displaying the template screen
	 * @param width
	 * @param height
	 */
	private void displayTemplatePane(final JTextField jt){
		final JFrame templateFrame = new JFrame("Template Wizard");
		templateFrame.setResizable(false);
		templateFrame.setSize(300,300);
		templateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		templateFrame.setVisible(true);
		this.ruleFrame.setEnabled(false);
		this.mainFrame.setEnabled(false);
		int size = Integer.parseInt(jt.getText());
		final Template t = new Template(size,tempRules.getTemplate(), null, null, true);
		t.setPreferredSize(new Dimension(280,280));
		t.addListeners();
		templateFrame.add(t, BorderLayout.NORTH,JLayeredPane.DEFAULT_LAYER);
		JPanel panelTwo = new JPanel();
		JButton saveExit = new JButton("Save and Exit");
		saveExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tempRules.setTemplate(t.fetchTemplate());
				jt.setText(""+tempRules.getLargestSide());
				displayRules();
				displayAdditionTemplate();
				templateFrame.dispose();
				ruleFrame.setEnabled(true);
				mainFrame.setEnabled(true);
			}
		});
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				templateFrame.dispose();
				ruleFrame.setEnabled(true);
				mainFrame.setEnabled(true);
			}
		});
		panelTwo.add(saveExit, BorderLayout.SOUTH);
		panelTwo.add(cancel, BorderLayout.SOUTH);
		templateFrame.add(panelTwo);
		templateFrame.pack();
	}
	
	/**
	 * Will add a template that will allow the addition of new rules
	 * @param jsp
	 */
	private void displayAdditionTemplate(){
		ruleAddPanel.removeAll();
		ruleAddPanel.add(new JLabel("Add a new Rule:"));
		int[][] defaultT = new int[tempRules.getTemplate().length][tempRules.getTemplate()[0].length];
		final Template be = new Template(tempRules.getLargestSide(), defaultT, tempRules.getTemplate(), tempRules.getColorSet(), false);
		final Template af = new Template(tempRules.getLargestSide(), defaultT, tempRules.getTemplate(), tempRules.getColorSet(), false);
		be.addListenersSpecial(af, tempRules.getTemplate());
		JButton butt = new JButton("Add Rule");
		butt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				tempRules.addRule(be.getActive(),af.getActive());
				displayRules();
			}
		});
		JButton smartRules = new JButton("Open Smart Rule Wizard");
		smartRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				displaySmartRulePane();
			}
		});
		ruleAddPanel.add(ruleDisplaySetUp(be, af));
		ruleAddPanel.add(butt);
		ruleAddPanel.add(smartRules);
		ruleAddPanel.revalidate();
		ruleAddPanel.repaint();
	}
	
	/**
	 * Sets up the display pattern of the rules
	 * @param before
	 * @param after
	 * @return
	 */
	private JPanel ruleDisplaySetUp(Template before, Template after){
		JPanel holder = new JPanel(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = GridBagConstraints.RELATIVE;
		c1.gridy= 0;
		c1.insets = new Insets(2,2,2,2);
		c1.fill = GridBagConstraints.BOTH;
		holder.add(before, c1);
		holder.add(new JLabel(">>"), c1);
		holder.add(after, c1);
		c1.gridx = 0;
		c1.gridy = 1;
		c1.insets = new Insets(0,0,0,0);
		holder.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		return holder;
	}
	
	/**
	 * Will display list of current rules on the mainRulePanel
	 */
	private void displayRules(){
		mainRulePanel.removeAll();
		Iterator<Integer> it = tempRules.getRuleSet().iterator();
		int i = 0;
		while(it.hasNext()){
			int curRule = it.next();
			int[][] before = tempRules.convertToActiveMatrix(curRule);
			int[][] after = tempRules.convertToAnswerMatrix(curRule);
			//Display
			MouseListener m = new MouseListener(){
				public void mouseClicked(MouseEvent e){
					deleteRule(e.getComponent());
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			};
			Template be = new Template(tempRules.getLargestSide(), before, tempRules.getTemplate(), tempRules.getColorSet(), false);
			Template af= new Template(tempRules.getLargestSide(), after, tempRules.getTemplate(), tempRules.getColorSet(), false);
			JPanel holder = ruleDisplaySetUp(be, af);
			holder.setName(""+curRule);
			holder.addMouseListener(m);
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 1;
			holder.add(new JLabel(""+curRule),c);
			c.gridy = 1+(i/DEFAULT_NUMDISPLAY);
			c.gridx = i%DEFAULT_NUMDISPLAY;
			c.insets = new Insets(3,3,3,3);
			i++;
			mainRulePanel.add(holder,c);	
		}
		mainRulePanel.revalidate();
		mainRulePanel.repaint();
	}
	
	/**
	 * Deletes a rule
	 * @param c
	 */
	private void deleteRule(Component c){
		Integer ruleNum = Integer.parseInt(c.getName());
		tempRules.deleteRule(ruleNum);
		displayRules();
	}
	
	/**
	 * Displays the Smart Rule Wizard Pane
	 */
	private void displaySmartRulePane(){
		smartRulePane = new JFrame("Smart Rule Wizard");
		smartRulePane.setLayout(new GridBagLayout());
		smartRulePane.setSize(new Dimension(600,600));
		smartRulePane.setVisible(true);
		smartRulePane.setResizable(false);
		smartRulePane.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		smartRulePanel = new JPanel(new GridBagLayout());
		JScrollPane smartRuleScroll = new JScrollPane(smartRulePanel);
		smartRuleScroll.setPreferredSize(new Dimension(600,525));
		JPanel buttonDisplay = new JPanel();
		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ArrayList<SmartRules> sr = new ArrayList<SmartRules>();
				for(Component c : smartRulePanel.getComponents()){
					if(c instanceof SmartRules){
						sr.add((SmartRules)c);
					}
				}
				tempRules.setSmartRuleSet(sr);
				smartRulePane.dispose();
			}
		});
		JButton addSmartRule = new JButton("Add Smart Rule");
		addSmartRule.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SmartRules rules = new SmartRules(tempRules.getOps(),tempRules.getColorSet(),
						tempRules.getTemplate());
				GridBagConstraints c = new GridBagConstraints();
				c.gridx = 0;
				c.gridy = GridBagConstraints.RELATIVE;
				smartRulePanel.add(rules,c);
				smartRulePanel.revalidate();
			}
		});
		JButton generate = new JButton("Generate");
		generate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ArrayList<SmartRules> sr = new ArrayList<SmartRules>();
				for(Component c : smartRulePanel.getComponents()){
					if(c instanceof SmartRules){
						sr.add((SmartRules)c);
					}
				}
				SmartRules[] ret = new SmartRules[sr.size()];
				tempRules.smartRuleProcessing(sr.toArray(ret));
			}
		});
		buttonDisplay.add(addSmartRule);
		buttonDisplay.add(generate);
		buttonDisplay.add(exit);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 25;
		c.gridy = GridBagConstraints.RELATIVE;
		smartRulePane.add(buttonDisplay,c);
		smartRulePane.add(smartRuleScroll,c);
		c.gridx = 0;
		ArrayList<SmartRules> sr = tempRules.getSmartRuleSet();
		for(int i = 0; i<sr.size(); i++){
			smartRulePanel.add(sr.get(i),c);
		}
		smartRulePane.pack();
	}
	
	
	/**
	 * Uses the SwingUtilities method so it creates a new thread instead of just
	 * building own top frame
	 * @param Args
	 */
	public static void main(String Args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new AutomataMain();
			}
		});
	}
}
