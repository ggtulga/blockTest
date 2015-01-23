import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class testPanel extends JPanel implements MouseMotionListener,
				      MouseListener {

	private static final long serialVersionUID = 1L;
	private DrawableBlock startBlock;
	private DrawableBlock newBlock = null;
	private Point cursorPoint;
	private Point dragStartPoint = null;
	private static List<DrawableBlock> selectedBlocks;
	

	private Rectangle selectedRec = null;
	private test parent;
	private boolean isDrag=false;
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(enabled){
			this.addMouseMotionListener(this);
			this.addMouseListener(this);			
		}
		else{
			this.removeMouseListener(this);
			this.removeMouseMotionListener(this);
		}
		for(Component c:getComponents()){
			c.setEnabled(enabled);
		}
	}
	public testPanel(test parent) {
		setLayout(null);		
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
		setBackground(Color.white);
		selectedBlocks = new ArrayList<>();
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispacher());
		this.parent=parent;
	}
	
	public void newPanel(){
		this.removeAll();
		startBlock = new StartBlock(1,this);
		startBlock.setLocation(50, 50);
		add(startBlock);
		repaint();
	}
	
	public void savePanel(File file){
		
		List<DrawableBlock> temp=new ArrayList<>();
		for(Component c:getComponents()){
			DrawableBlock block=(DrawableBlock) c;
			if(!temp.contains(block)){
				block.addToList(temp);
			}
		}
		FileOutputStream fout;
		DrawableBlock.setFirstBLock(null);
		DrawableBlock.setSelectedBlock(null, null);
		try {			

			String fname = file.getAbsolutePath();

			if(!fname.endsWith(".block") ) {
				file = new File(fname + ".block");
			}

			fout = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			try{
				oos.writeObject(temp);
			}
			finally{
				fout.close();
			}
		} catch (FileNotFoundException e) {
			Log.log(e);
		} catch (IOException e) {
			Log.log(e);
		}
		
		
	}	
	
	public void openFile(File file){

		removeAll();
		 
		try{
			//use buffering
			InputStream filei = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(filei);
			ObjectInput input = new ObjectInputStream (buffer);
			try{
				List<DrawableBlock> recoveredQuarks = (List<DrawableBlock>)input.readObject();
				for (DrawableBlock drawableBlock : recoveredQuarks) {
					addBlock(drawableBlock);
					
				}
			}
			finally{
				input.close();
			}
		}
		catch(ClassNotFoundException ex){
		      
		}
		catch(IOException ex){
		}
		revalidate();
		repaint();
	}
	
	public DrawableBlock getStartBlock() {
		return startBlock;
	}

	public void setStartBlock(DrawableBlock startBlock) {
		this.startBlock = startBlock;
	}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		// lines.add(new OutputLines(block3));
		for (Component b : getComponents()) {
			OutputLines l = new OutputLines((DrawableBlock) b);
			l.drawOutputLine(g);
		}
		if (DrawableBlock.getFirstBLock() != null) {
			g.setColor(Color.blue);
			/*OutputLines line = new OutputLines(DrawableBlock.firstBLock);
			  try{
			  line.drawLine(DrawableBlock.firstBLock.getOutputPoint(),
			  cursorPoint, g);
			  }catch(Exception e){}*/
		}
		if (selectedRec != null) {
			g.setColor(Color.blue);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(selectedRec.x, selectedRec.y, selectedRec.width,
				    selectedRec.height);
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if (newBlock != null) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else{
			if(selectedRec!=null&&selectedRec.contains(cursorPoint))
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			else if(DrawableBlock.getFirstBLock()!=null){
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			}else
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		cursorPoint = e.getLocationOnScreen();
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(selectedRec!=null){
			dragStartPoint = cursorPoint;
			for (DrawableBlock block : selectedBlocks) {
				block.setTemp(e.getLocationOnScreen());
			}			
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (dragStartPoint != null ) {
			int x=dragStartPoint.x;
			int y=dragStartPoint.y;
			int w=Math.abs( cursorPoint.x - dragStartPoint.x);
			int h=Math.abs( cursorPoint.y - dragStartPoint.y);
			if(x>cursorPoint.x) x=cursorPoint.x;
			if(y>cursorPoint.y) y=cursorPoint.y;
			selectedRec = new Rectangle(x, y, w, h);
			selectBlocks();
		}else {
		}
		isDrag=false;

	}		
	
	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		if(DrawableBlock.getSelectedBlock()!=null)
			DrawableBlock.setSelectedBlock(null, null);
		if (newBlock != null) {
			DrawableBlock b;
			if (newBlock.TYPE == BLOCKTYPE.END)
				b = new EndBlock();
			else if (newBlock.TYPE == BLOCKTYPE.INIT)
				b = new InitBlock();
			else if (newBlock.TYPE == BLOCKTYPE.INPUT)
				b = new InputBlock();
			else if (newBlock.TYPE == BLOCKTYPE.OUTPUT)
				b = new OutputBlock();
			else if (newBlock.TYPE == BLOCKTYPE.VALUE)
				b = new ValueBlock();
			else if(newBlock.TYPE == BLOCKTYPE.IF)
				b = new IfBlock();
			else 
				b = new PointBlock();
			addBlock(b);
			b.setLocation(e.getPoint());
			newBlock.setColor(Color.black);
			newBlock = null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			test.setEdited();
		} else if (DrawableBlock.getFirstBLock() != null) {
			DrawableBlock.getFirstBLock().setColor(Color.black);
			DrawableBlock.setFirstBLock(null);
		}

		if(selectedRec!=null&&!selectedRec.contains(e.getPoint())){
			deSelectBlocks();
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		cursorPoint = e.getPoint();
		if (dragStartPoint == null) {
			dragStartPoint = cursorPoint;
		}else {
//			int x=dragStartPoint.x;
//			int y=dragStartPoint.y;
//			int w=Math.abs( cursorPoint.x - dragStartPoint.x);
//			int h=Math.abs( cursorPoint.y - dragStartPoint.y);
//			if(x>cursorPoint.x&&y>cursorPoint.y) {
//				x=cursorPoint.x;
//				y=cursorPoint.y;
//				setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));	
//			}else if(x>cursorPoint.x){
//				x=cursorPoint.x;
//				setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
//			}else if(y>cursorPoint.y){
//				y=cursorPoint.y;
//				setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
//			}else 
//				setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
//			selectedRec = new Rectangle(x, y, w, h);
			
		} 

		if (selectedBlocks.size() > 0) {
			setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			DrawableBlock.setSelectedBlock(null, null);			
			Log.log(selectedRec+"mmm");
			Log.log(getBounds());
			Log.log(cursorPoint);
			if (selectedRec.contains(cursorPoint))
				isDrag=true;
			if (isDrag) 
			{	
				Point p=new Point();
				cursorPoint = e.getLocationOnScreen();
				p.x=cursorPoint.x-dragStartPoint.x;
				p.y=cursorPoint.y-dragStartPoint.y;
				dragStartPoint = cursorPoint;
				
				if (true){
					boolean iscan=true;
					for (DrawableBlock block : selectedBlocks) {
						if(!block.isCanMoveToXY(e.getLocationOnScreen()))
						{iscan=false;  break;}
					}
					if(iscan){
						selectedRec.x=selectedRec.x+p.x;
						selectedRec.y=selectedRec.y+p.y;
						for (DrawableBlock block : selectedBlocks) {
						
							block.dragBlock(e.getLocationOnScreen());
						}
					}
				}							
			}
			
		}

		repaint();
	}
	
	private void selectBlocks() {
		selectedBlocks.clear();		
		for (Component b : getComponents()) {
			DrawableBlock block = (DrawableBlock) b;
			if (selectedRec.contains(block.getLocation())) {
				selectedBlocks.add(block);
				block.setColor(Color.blue);
			} else
				block.setColor(Color.black);

		}
		if(selectedBlocks.size()==0){
			selectedBlocks.clear();
			selectedRec = null;
			dragStartPoint = null;
		}
		repaint();
	}

	private void deSelectBlocks() {

		for (DrawableBlock block : selectedBlocks) {

			block.setColor(Color.black);
		}
		selectedBlocks.clear();
		selectedRec = null;
		repaint();
		dragStartPoint = null;
	}
	
	public void addBlock(DrawableBlock block) {
		add(block);
		if(block.TYPE.equals(BLOCKTYPE.BEGIN))
			startBlock = block;
		Log.log(block);
		if((block.getLocation().x+block.getWidth())>getWidth())
			setPreferredSize(new Dimension((block.getLocation().x+block.getWidth()), getHeight()));
		if((block.getLocation().y+block.getHeight())>getHeight())
			setPreferredSize(new Dimension(getWidth(), (block.getLocation().y+block.getHeight())));
		
		
		Rectangle cont=new Rectangle(getSize());
		Rectangle obj=new Rectangle(block.getLocation().x, block.getLocation().y, block.getWidth(), block.getHeight());
		
		if(!cont.contains(obj))
		{
			int w=(int) (obj.getWidth()+obj.getX());
			int h=(int) (obj.getHeight()+obj.getY());
			if(w<getWidth())	w=getWidth();
			if(h<getHeight())	h=getHeight();
			setPreferredSize(new Dimension(w,h));
		}
		repaint();
	}

	public DrawableBlock getNewBlock() {
		return newBlock;
	}

	public void setNewBlock(DrawableBlock newBlock) {
		this.newBlock = newBlock;
	}
	
	public void setErrorTypeToBlock(DrawableBlock block){
		DrawableBlock.setSelectedBlock(block, Color.RED);
	}
	public void setRunTypeToBlock(DrawableBlock block){
		DrawableBlock.setSelectedBlock(block, Color.GREEN);
	}
	
	public test getparent() {
		return parent;
	}
	public static List<DrawableBlock> getSelectedBlocks() {
		return selectedBlocks;
	}
	public void setSelectedBlocks(List<DrawableBlock> blocks){
		selectedBlocks.clear();
		if(blocks.size()>0){
			selectedBlocks.addAll(blocks);
			Point p=null;
			int w=50, h=50;
			Point temp;
			for (DrawableBlock b : blocks) {
				temp=b.getLocation();
				if(p!=null){
					if(p.x>temp.x)
						w=w+p.x-temp.x;
					else{
						if((temp.x-p.x)>w)
							w=temp.x-p.x+b.getWidth();
					}
					if(p.y>temp.y)
						h=h+p.y-temp.y;
					else{
						if((temp.y-p.y)>h)
							h=temp.y-p.y+b.getHeight();
					}
				}else{
					p=temp;
					w=b.getWidth();
					h=b.getHeight();
				}
			}
			selectedRec=new Rectangle(p.x, p.y, w, h);
		}
	}
}
