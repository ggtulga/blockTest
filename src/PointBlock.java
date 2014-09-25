import java.awt.Color;
import java.awt.Graphics;


public class PointBlock extends DrawableBlock {
	public PointBlock() {
		super(BLOCKTYPE.POINT);
		// TODO Auto-generated constructor stub
	}
	
	public PointBlock(int i) {
		super(BLOCKTYPE.POINT,i);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics g) {
		this.setSize(22, 22);
		g.setColor(Color.white);
		g.fillOval(1, 1, 20, 20);
		g.setColor(getColor());
		g.drawOval(1, 1, 20, 20);
		g.fillOval(8, 8, 4, 4);
		
	}
}
