import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;


public class ValueBlock extends DrawableBlock{

    public ValueBlock(){
	super();
	setText("");
	TYPE = BLOCKTYPE.VALUE;
    }
    public ValueBlock(int i){
	super(i);
	setText("Утга оноох");
	TYPE = BLOCKTYPE.VALUE;
    }
    @Override
    public void draw(Graphics g) {
	g.setColor(Color.white);
	g.fillRect(0, 10, getWidth() - 1, getHeight() - 20);
	g.setColor(getColor());
	g.drawRect(0, 10, getWidth() - 1, getHeight() - 20);
	g.drawString(getText(), 10, 30);
	g.drawLine(getWidth() / 2, getHeight() - 10, getWidth() / 2, getHeight());
	g.drawLine(getWidth() / 2, 0, getWidth() / 2, 10);
	int x1[] = {getWidth() / 2 - 5, getWidth() / 2, getWidth() / 2 + 5};
	int y1[] = {3, 10, 3};
	g.fillPolygon(x1, y1, 3);			
    }
}
