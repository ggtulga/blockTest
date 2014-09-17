
import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Point;


public class EndBlock extends DrawableBlock {

	private DrawableBlock before;
	public EndBlock(){
		super(BLOCKTYPE.END);
		setText("End");
		//TYPE=BLOCKTYPE.END;
		Log.log("--");
		Log.log(TYPE);
	}
	public EndBlock(int i){
		super(BLOCKTYPE.END,i);
	}
	@Override
	public void draw(Graphics g) {
				g.setColor(Color.white);
				g.fillArc(0, 10, 30, 30,90, 180);
				g.fillArc(this.getWidth()-30, 10, 25, 30, -90, 180);				
				g.fillRect(15, 10, getWidth()-30, 30);
				g.setColor(getColor());
				g.drawArc(0, 10, 30, 30,90, 180);
				g.drawArc(this.getWidth()-30, 10, 25, 30, -90, 180);
				g.drawLine(15, 10, getWidth()-15, 10);
				g.drawLine(15, 40, getWidth()-15, 40);
				g.drawString("Төгсгөл", 10, 30);
				g.drawLine(getWidth()/2, 0, getWidth()/2, 10);
				int x1[]={getWidth()/2-5,getWidth()/2,getWidth()/2+5};
				int y1[]={3,10,3};
				g.fillPolygon(x1, y1, 3);					
	}
	@Override
	public Point getOutputPoint() {
		return null;
	}
}
