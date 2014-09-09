import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class OutputLines {
	private DrawableBlock block;
	public OutputLines(DrawableBlock block){
		setBLock(block);
	}
	public void drawOutputLine(Graphics g){
		g.setColor(Color.black);
		if(block.TYPE!=BLOCKTYPE.IF){
			if(block.TYPE!=BLOCKTYPE.END&&block.getNext()!=null){
				Point source=block.getOutputPoint();
				Point destination=block.getNext().getInputPoint();				
				drawLine(source, destination, g);
			}
		}else{
			IfBlock ifBlock=(IfBlock)block;
			if(ifBlock.getNextTrue()!=null){
				Point source=ifBlock.getOutputTruePoint();
				Point destination=ifBlock.getNextTrue().getInputPoint();
				drawLine(source, destination, g);
			}
			if(ifBlock.getNextFalse()!=null){
				Point source=ifBlock.getOutputFalsePoint();
				Point destination=ifBlock.getNextFalse().getInputPoint();
				drawLine(source, destination, g);
			}			
		}		
	}
	public void drawLine(Point source,Point destination,Graphics g){
		if(source.y==destination.y||source.x==destination.x){
			g.drawLine(destination.x, destination.y, source.x, source.y);
		}else{
			if(source.y<destination.y){	
				if(source.x!=destination.x){	
					g.drawLine(source.x, source.y, destination.x, source.y);
					g.drawLine(destination.x, destination.y, destination.x, source.y);
				}
			}else if(source.y>(destination.y+block.getHeight())){
				if(source.x>destination.x){
						g.drawLine(source.x, source.y, source.x+block.getWidth()+10, source.y);
						g.drawLine(source.x+block.getWidth()+10, destination.y, source.x+block.getWidth()+10, source.y);
						g.drawLine(source.x+block.getWidth()+10, destination.y, destination.x, destination.y);
					}else{
						g.drawLine(source.x, source.y, source.x-block.getWidth()-10, source.y);
						g.drawLine(source.x-block.getWidth()-10, destination.y, source.x-block.getWidth()-10, source.y);
						g.drawLine(source.x-block.getWidth()-10, destination.y, destination.x, destination.y);
					}
				}else{
					if(source.x<destination.x){
						g.drawLine(source.x, source.y, source.x+block.getWidth()+10, source.y);
						g.drawLine(source.x+block.getWidth()+10, destination.y, source.x+block.getWidth()+10, source.y);
						g.drawLine(source.x+block.getWidth()+10, destination.y, destination.x, destination.y);
					}else{
						g.drawLine(source.x, source.y, source.x-block.getWidth()-10, source.y);
						g.drawLine(source.x-block.getWidth()-10, destination.y, source.x-block.getWidth()-10, source.y);
						g.drawLine(source.x-block.getWidth()-10, destination.y, destination.x, destination.y);
					}
			}
		}
	}

	public DrawableBlock getBLock() {
		return block;
	}
	public void setBLock(DrawableBlock bLock) {
		block = bLock;
	}
}
