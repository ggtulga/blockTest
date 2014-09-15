

import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;


public class InputBlock  extends DrawableBlock{
	private DrawableBlock before,next;
	public InputBlock (){
		super(BLOCKTYPE.INPUT);
		setText("");
	}
	public InputBlock(int i){
		super(BLOCKTYPE.INPUT,i);
		setText("Оруулах");
	}
	@Override
	public void draw(Graphics g) {
			
			g.setColor(Color.white);
		int x[]={10,getWidth()-1,getWidth()-10,0};
		int y[]={10,10,getHeight()-10,getHeight()-10};
		g.fillPolygon(x, y, 4);
		g.setColor(getColor());
		g.drawPolygon(x, y, 4);
		g.drawString(getText(), 10, 30);
		g.drawLine(getWidth()/2, getHeight()-10, getWidth()/2, getHeight());
		g.drawLine(getWidth()/2, 0, getWidth()/2, 10);
		int x1[]={getWidth()/2-5,getWidth()/2,getWidth()/2+5};
		int y1[]={3,10,3};
		g.fillPolygon(x1, y1, 3);	
				
	}
	

}
