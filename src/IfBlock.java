import java.awt.Color;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.Point;
import java.util.List;

public class IfBlock   extends DrawableBlock{
	private DrawableBlock nextTrue,nextFalse;
	
	public IfBlock  (){
		super();
		setText("");
		TYPE=BLOCKTYPE.IF;	
	}
	public IfBlock(int i){
		super(i);
		setText("Шалгах");
		TYPE=BLOCKTYPE.IF;
	}
	@Override
	public void draw(Graphics g) {
		if(getText().length()<5){
			this.setSize(90, 50);
		}else
			this.setSize(10 *getText().length()+45, 50);
		this.setSize(getWidth(), 60);
			g.setColor(Color.white);
		int x[]={10,getWidth()/2,getWidth()-10,getWidth()/2};
		int y[]={getHeight()/2,10,getHeight()/2,getHeight()-10};
		g.fillPolygon(x, y, 4);
		g.setColor(getColor());
		g.drawPolygon(x, y, 4);
		g.drawString(getText(), 30, getHeight()/2+5);	
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
	public String toXMLTags(){
		String result="<Block type='"+TYPE.getValue()+"', text='"+getText()+"', x="+getLocation().x+", y="+getLocation().y+">";
		if(getNextTrue()!=null)
			result+="<NextTrueBlock>"+getNextTrue().toXMLTags()+"</NextTrueBlock>";
		if(getNextFalse()!=null)
			result+="<NextFalseBlock>"+getNextFalse().toXMLTags()+"</NextFalseBlock>";
		result+="</Block>";		
		return result;
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
