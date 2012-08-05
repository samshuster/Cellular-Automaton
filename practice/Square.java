package practice;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class Square extends JPanel implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Color BORDER_COLOR = Color.black;
	private final int BORDER_THICK = 1;
	private int livingType;
	private Color[] colorSet;
	
	public Square(Color[] colors) {
		this.setBorder(BorderFactory.createMatteBorder(BORDER_THICK, 0, 0, 
				BORDER_THICK, BORDER_COLOR));
		colorSet = colors;
		this.setBackground(colorSet[0]);
		livingType = 0;
	}

	public void setType(int newType){
		livingType = newType;
		this.setBackground(colorSet[newType]);
	}

	public int getType(){
		return livingType;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int i;
		for(i = 0; i<colorSet.length; i++){
			if(colorSet[i] == this.getBackground()){
				break;
			}
		}
		setType((i+1)%colorSet.length);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
