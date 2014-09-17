
import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Point;


public class StartBlock extends DrawableBlock{

	public StartBlock(){
		super(BLOCKTYPE.BEGIN);
		setText("");
	}
	public StartBlock(int i){
		super(BLOCKTYPE.BEGIN,i);
		setText("Start");
	}
	@Override
	public void draw(Graphics g) {
		this.setSize(80, 40);
		g.setColor(Color.white);
		g.fillArc(0, 0, 30, 30, 90, 180);
		g.fillArc(this.getWidth() - 30, 0, 25, 30, -90, 180);
		g.fillRect(15, 0, getWidth() - 30, 30);
		g.setColor(getColor());
		g.drawArc(0, 0, 30, 30, 90, 180);
		g.drawArc(this.getWidth() - 30, 0, 25, 30, -90, 180);
		g.drawLine(15, 0, getWidth() - 15, 0);
		g.drawLine(15, 30, getWidth() - 15, 30);
		g.drawString("Эхлэл", 15, 20);
		g.drawLine(getWidth() / 2, getHeight() - 10, getWidth() / 2,
			   getHeight());		
	}
	@Override
	public Point getInputPoint() {
		return null;
	}
	

}
