package practice;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Template extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Color NA = Color.white;
	private final Color ENVIRONMENT = Color.black;
	private final Color SPECIMEN = Color.red;
	private Square[][] squares;
	
	//TODO
	//WORK ON THIS SECTION
	/**
	 * Template constructor
	 * @param width
	 * @param height
	 * @param activation
	 */
	public Template(int size, int[][] activation, int[][] template, Color[] colorSet, 
			boolean fresh){
		this.setLayout(new FullyJustifiedGridLayout(size,size));
		this.setPreferredSize(new Dimension(size*10,size*10));
		int width,height;
		if(fresh){
			template = activation;
			width = size;
			height = size;
		} else {
			height = activation.length;
			width = activation[0].length;
		}
		squares = new Square[height][width];
		Color[] cSet;
		if (colorSet == null){
			cSet = new Color[]{NA, ENVIRONMENT, SPECIMEN};
		} else {
			cSet = colorSet;
		}
		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				Square s = new Square(cSet);
				int type;
				//If the square is in activation then if its not actually
				//A feasible square (but activation is 0) then just don't
				//worry about it. Unless its a fresh template
				if(i<activation.length && j<activation[0].length){
					type = activation[i][j];
					if(template[i][j]!=0 || fresh){
						if(template[i][j] == -1 && !fresh){
							s.setBorder(BorderFactory.createLineBorder(SPECIMEN));
						}
						if(fresh){
							if(type == -1){
								type = 2;
							}
						}
						this.add(s);
						s.setType(type);
						squares[i][j] = s;
					} else {
						this.add(new JPanel());
					}
				} else if(fresh) {
					type = 0;
					s.setType(type);
					squares[i][j] = s;
					this.add(s);
				} else {
					this.add(new JPanel());
				}
			}
		}
	}
	
	public void addListeners(){
		for(Square[] sl : squares){
			for(Square s : sl){
				if(s!=null)
				s.addMouseListener(s);
			}
		}
	}
	
	/**
	 * Add listeners so that two templates are attached (used for addition)
	 * @param link
	 * @param template
	 */
	public void addListenersSpecial(Template link, int[][] template){
		for(int i = 0; i<squares.length; i++){
			for(int j = 0; j<squares[i].length; j++){
				if(squares[i][j]!=null){
					squares[i][j].addMouseListener(squares[i][j]);
					if(template[i][j] != -1){
						squares[i][j].addMouseListener(link.squares[i][j]);
					} else {
						link.squares[i][j].addMouseListener(link.squares[i][j]);
					}
				}
			}
		}
	}
	
	public void removeListeners(){
		for(Square[] sl : squares){
			for(Square s : sl){
				if(s!=null)
				s.removeMouseListener(s);
			}
		}
	}
	
	//TODO
	/**
	 * Still very buggy and should take advantage of rules methods
	 * @return
	 */
	public int[][] fetchTemplate(){
		int[][] template = getActive();
		int[] boundaries = crop(template);
		int[][] ret = new int[boundaries[3]-boundaries[2]][boundaries[1]-boundaries[0]];
		for(int i = boundaries[2]; i<boundaries[3]; i++){
			for(int j = boundaries[0]; j<boundaries[1]; j++){
				int type = template[i][j];
				if(type == 2){
					ret[i-boundaries[2]][j-boundaries[0]] = -1;
				} else {
					ret[i-boundaries[2]][j-boundaries[0]] = type;
				}
			}
		}
		return ret;
	}
	
	/** 
	 * removes all side zero sections (CURRENTLY ALSO REMOVES INTERIOR ZEROS
	 * @return the cropped 2d array
	 */
	private int[] crop(int[][] template){
		int xMax = 0;
		int xMin = template.length;
		int yMax = 0;
		int yMin = template[0].length;
		for(int i = 0; i<template.length; i++){
			for(int j = 0; j<template[i].length; j++){
				if(template[i][j]!=0){
					xMin = (xMin > j) ? j : xMin;
					xMax = (xMax < j) ? j : xMax;
					yMin = (yMin > i) ? i : yMin;
					yMax = (yMax < i) ? i : yMax;
				}
			}
		}
		return new int[]{xMin,xMax+1,yMin,yMax+1};
	}

	/**
	 * @return the squares
	 */
	public Square[][] getSquares() {
		return squares;
	}
	
	/**
	 * 
	 * @return the active matrix
	 */
	public int[][] getActive(){
		int[][] active = new int[squares.length][squares[0].length];
		for(int i = 0; i<squares.length; i++){
			for(int j = 0; j<squares[0].length; j++){
				if(squares[i][j]!=null){
					active[i][j] = squares[i][j].getType();
				}
			}
		}
		return active;
	}
}
