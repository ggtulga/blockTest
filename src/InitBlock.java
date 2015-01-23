
import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;


public class InitBlock extends DrawableBlock {

	private DrawableBlock before,next;
	public InitBlock(){
		super(BLOCKTYPE.INIT);
		setTextValue("     ");
	}
	public InitBlock(int i,testPanel mainPanel){
		super(BLOCKTYPE.INIT,i, mainPanel);
		setTextValue("Зарлах");
	}
	public InitBlock(DrawableBlock block) {
		super(block);
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 10, getWidth()-1, getHeight()-20);
		g.setColor(getColor());
		//g.drawString(getText(), 10, 30);
		g.drawRect(0, 10, getWidth()-1, getHeight()-20);
		g.drawRect(5, 10, getWidth()-11, getHeight()-20);

		g.drawLine(getWidth()/2, getHeight()-10, getWidth()/2, getHeight());
		g.drawLine(getWidth()/2, 0, getWidth()/2, 10);
		int x1[]={getWidth()/2-5,getWidth()/2,getWidth()/2+5};
		int y1[]={3,10,3};
		g.fillPolygon(x1, y1, 3);	
	}
}
