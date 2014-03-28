
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class testPanel extends JPanel implements MouseMotionListener,MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DrawableBlock startBlock;
	public DrawableBlock getStartBlock() {
		return startBlock;
	}

	public void setStartBlock(DrawableBlock startBlock) {
		this.startBlock = startBlock;
	}

	private DrawableBlock newBlock = null;
	public KeyListener keyListner;
	private Point cursorPoint;
	private Point dragStartPoint=null;
	private List<DrawableBlock> selectedBlocks;
	private Rectangle selectedRec=null;
	public testPanel(){
		setLayout(null);
		startBlock=new StartBlock(1);
		add(startBlock);	
		this.addKeyListener(DrawableBlock.keyListener);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		keyListner=DrawableBlock.keyListener;
		setBackground(Color.white);		
		selectedBlocks=new ArrayList<>();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		//lines.add(new OutputLines(block3));
		for (Component b : getComponents()) {
			OutputLines l=new OutputLines((DrawableBlock)b);
			l.drawOutputLine(g);
		}
		if(DrawableBlock.firstBLock!=null){
			OutputLines line=new OutputLines(DrawableBlock.firstBLock);
			line.drawLine(DrawableBlock.firstBLock.getOutputPoint(),cursorPoint,g);
		}
		if(selectedRec!=null){
			g.setColor(Color.yellow);
			g.drawRect(selectedRec.x,selectedRec.y,selectedRec.width,selectedRec.height);
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		cursorPoint=e.getPoint();	
		if(dragStartPoint==null){
			dragStartPoint=cursorPoint;
		}else{
			selectedRec=new Rectangle(dragStartPoint.x,dragStartPoint.y,cursorPoint.x-dragStartPoint.x,cursorPoint.y-dragStartPoint.y);
		}
		if(selectedBlocks.size()>0){
			for (DrawableBlock block : selectedBlocks) {
				
				block.dragBlock(e.getLocationOnScreen());		
		}
		}
	}
	private void selectBlocks(Point start,Point end){
		selectedBlocks.clear();
		//Rectangle rect=new Rectangle(start.x,start.y,end.x-start.x,end.y-start.y);
		//System.out.println(rect);
		for (Component b : getComponents()) {
			DrawableBlock block=(DrawableBlock)b;
			//System.out.println(block.getLocation());
			if(selectedRec.contains(block.getLocation())){
				selectedBlocks.add(block);
				block.setColor(Color.blue);
			}else 
				block.setColor(Color.black);
			
		}
		repaint();
	}
	private void deSelectBlocks(){
		
		for (DrawableBlock block : selectedBlocks) {
			
				block.setColor(Color.black);			
		}
		selectedBlocks.clear();
		selectedRec=null;
		repaint();
		System.out.println("*******");
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		cursorPoint=e.getPoint();	
		
	}
	public void addBlock(DrawableBlock block){
		add(block);
		repaint();
	}

	public DrawableBlock getNewBlock() {
		return newBlock;
	}

	public void setNewBlock(DrawableBlock newBlock) {
		this.newBlock = newBlock;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(newBlock!=null){
			DrawableBlock b;
			if(newBlock.TYPE==BLOCKTYPE.END)
				b=new EndBlock();
			else if(newBlock.TYPE==BLOCKTYPE.INIT)
				b=new InitBlock();
			else if(newBlock.TYPE==BLOCKTYPE.INPUT)
				b=new InputBlock();
			else if(newBlock.TYPE==BLOCKTYPE.OUTPUT)
				b=new OutputBlock();
			else if(newBlock.TYPE==BLOCKTYPE.VALUE)
				b=new ValueBlock();
			else 
				b=new IfBlock();
			addBlock(b);
			b.setLocation(e.getPoint());
			newBlock.setColor(Color.black);
			newBlock=null;
		}else if(DrawableBlock.firstBLock!=null){
			DrawableBlock.firstBLock.setColor(Color.black);
			DrawableBlock.firstBLock=null;
			}

		if(selectedBlocks.size()>0){
			deSelectBlocks();
		}		
	}	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(dragStartPoint!=null){
			selectBlocks(dragStartPoint,cursorPoint);
			dragStartPoint=null;
		} 
		
	}
	

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
