

import java.awt.Color;
import java.awt.Graphics;


public class OutputBlock extends DrawableBlock {

	public OutputBlock(){
		super(BLOCKTYPE.OUTPUT);
		setTextValue("        ");
	}
	public OutputBlock(int i,testPanel mainPanel){
		super(BLOCKTYPE.OUTPUT,i, mainPanel);
		setTextValue("Хэвлэх");
	}
	public OutputBlock(DrawableBlock block) {
		super(block);
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		int x[]={getWidth()-15,15,0,15,getWidth()-15};
		int y[]={getHeight()-10,getHeight()-10,getHeight()/2,10,10};
		g.fillPolygon(x, y, 5);
		g.fillArc(this.getWidth()-30, 10, 25, 30, -90, 180);	
		g.setColor(getColor());
		g.drawPolyline(x, y, 5);
		g.drawArc(this.getWidth()-30, 10, 25, 30, -90, 180);
		//g.drawString(getText(), 10, 30);
		g.drawLine(getWidth()/2, getHeight()-10, getWidth()/2, getHeight());
		g.drawLine(getWidth()/2, 0, getWidth()/2, 10);
		int x1[]={getWidth()/2-5,getWidth()/2,getWidth()/2+5};
		int y1[]={3,10,3};
		g.fillPolygon(x1, y1, 3);
	}
}

