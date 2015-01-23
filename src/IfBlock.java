
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Point;
import java.util.List;

public class IfBlock   extends DrawableBlock{
	private DrawableBlock nextTrue,nextFalse;
	
	public IfBlock  (){
		super(BLOCKTYPE.IF);
		setTextValue("         ");
	}
	public IfBlock(int i, testPanel mainPanel){
		super(BLOCKTYPE.IF,i, mainPanel);
		setTextValue("Шалгах");
	}
	public IfBlock(IfBlock b){
		super(b);
		//nextTrue=b.getNextTrue();
		//nextFalse=b.getNextFalse();
	}
	@Override
	public void draw(Graphics g) {
		if(getText().length()<=9){
			this.setSize(150, 70);
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 25));
		}else{
			this.setSize(10 *getText().length()+45, 70+getText().length());
			this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, (75+getText().length())/3));
		}
		//this.setSize(getWidth(), 60);
			g.setColor(Color.white);
		int x[]={10,getWidth()/2,getWidth()-10,getWidth()/2};
		int y[]={getHeight()/2,10,getHeight()/2,getHeight()-10};
		g.fillPolygon(x, y, 4);
		g.setColor(getColor());
		g.drawPolygon(x, y, 4);
		//g.drawString(getText(), 30, getHeight()/2+5);	
		g.drawString("-", 5, getHeight()/2-2);
		g.drawLine(0, getHeight()/2, 10, getHeight()/2)	;
		g.drawString("+", getWidth()-8, getHeight()/2-2);
		g.drawLine(getWidth()-10, getHeight()/2, getWidth(), getHeight()/2)	;
		g.drawLine(getWidth()/2, 0, getWidth()/2, 10);
		int x1[]={getWidth()/2-5,getWidth()/2,getWidth()/2+5};
		int y1[]={3,10,3};
		g.fillPolygon(x1, y1, 3);
		//g.drawRect(0, 0, getWidth(), getHeight());
	}
	public Point getOutputTruePoint(){
		Point p=new Point(getWidth()+getLocation().x, getHeight()/2+getLocation().y);
		return p;
	}
	public Point getOutputFalsePoint(){
		Point p=new Point(getLocation().x, getHeight()/2+getLocation().y);
		return p;
	}
	public DrawableBlock getNextFalse() {
		return nextFalse;
	}
	public void setNextFalse(DrawableBlock nextFalse) {
		this.nextFalse = nextFalse;
		if(nextFalse!=null) 
			this.nextFalse.addBefore(this);
	}
	public DrawableBlock getNextTrue() {
		return nextTrue;
	}
	public void setNextTrue(DrawableBlock nextTrue) {
		this.nextTrue = nextTrue;
		if(nextTrue!=null) 
			this.nextTrue.addBefore(this);
	}
	
	@Override
	public void addToList(List<DrawableBlock> list){
		list.add(this);
		if(getNextTrue()!=null && !list.contains(getNextTrue())){
			list.add(getNextTrue());
		}
		if(getNextFalse()!=null&& !list.contains(getNextFalse())){
			list.add(getNextFalse());
		}
	}

}
