package practice;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextFieldTemplate extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField[][] text;
	private ArrayList<JTextField> centers;
	private int[][] template;
	private final Color SPECIMEN = Color.red;
	
	public TextFieldTemplate(int size, int[][] template, boolean onlyCenter){
		if(onlyCenter){
			centers = new ArrayList<JTextField>();
		}
		this.setLayout(new FullyJustifiedGridLayout(size,size));
		this.setPreferredSize(new Dimension(size*10,size*10));
		int width,height;
		height = template.length;
		width = template[0].length;
		this.template = template;
		text = new JTextField[height][width];
		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				JTextField t;
				if(!onlyCenter){
					t = new JTextField("@*",8);
				} else {
					t = new JTextField("");
					t.setEditable(false);
				}
				//If the square is in activation then if its not actually
				//A feasible square (but activation is 0) then just don't
				//worry about it. Unless its a fresh template
				if(i<template.length && j<template[0].length){
					if(template[i][j]!=0){
						if(template[i][j] == -1){
							t.setBorder(BorderFactory.createLineBorder(SPECIMEN));
							t.setEditable(true);
							t.setText("0");
							t.setColumns(8);
							if(onlyCenter)
								centers.add(t);
						}
						this.add(t);
						text[i][j] = t;
					} else {
						this.add(new JPanel());
					}
				} else {
					this.add(new JPanel());
				}
			}
		}
	}
	
	/**
	 * Will return the activation matrix of the smart rule
	 * @return
	 */
	public String[] getActive(){
		String[] ret = new String[text.length*text[0].length];
		int k = 0;
		for(int i = 0; i<text.length; i++){
			for(int j = 0; j<text[i].length; j++){
				if(template[i][j]!=0){
					ret[k] = text[i][j].getText();
					k++;
				}
			}
		}
		return ret;
	}
	
	/**
	 * Will return the indices that are of consequence for a certain variable rule
	 * NOTE SHOULD DO THIS AUTOMATICALLY UPON EXIT FROM SMART RULES
	 * @param varNum
	 * @return
	 */
	public ArrayList<Integer> getVariableIndices(int varNum){
		String find = "#"+varNum;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int k = 0;
		for(int i = 0; i<text.length; i++){
			for(int j = 0; j<text[i].length; j++){
				if(template[i][j] != 0){
					if(text[i][j].getText().contains(find)){
						ret.add(k);
					}
					k++;
				}
			}
		}
		return ret;
	}
	
	/**
	 * If this is a center smart template then will return the center values
	 * @return
	 */
	public ArrayList<String> getCenters(){
		ArrayList<String> ret = new ArrayList<String>();
		for(int i = 0; i<centers.size(); i++){
			ret.add(centers.get(i).getText());
		}
		return ret;
	}
}
