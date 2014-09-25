import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
	
	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
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
			DrawableBlock block=(DrawableBlock) c;
			block.setEnabled(enabled);
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
		startBlock = new StartBlock(1);
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
		DrawableBlock.CurrentNote = null;
		DrawableBlock.firstBLock = null;
		DrawableBlock.SelectedBlock=null;
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
				//deserialize the List
				List<DrawableBlock> recoveredQuarks = (List<DrawableBlock>)input.readObject();
				//display its data
				for (DrawableBlock drawableBlock : recoveredQuarks) {
					//drawableBlock.initListners();
					//drawableBlock.initMenu();
					add(drawableBlock);
					if(drawableBlock.TYPE.equals(BLOCKTYPE.BEGIN))
						startBlock = drawableBlock;
					Log.log(drawableBlock);
					if((drawableBlock.getLocation().x+drawableBlock.getWidth())>getWidth())
						setPreferredSize(new Dimension((drawableBlock.getLocation().x+drawableBlock.getWidth()), getHeight()));
					if((drawableBlock.getLocation().y+drawableBlock.getHeight())>getHeight())
						setPreferredSize(new Dimension(getWidth(), (drawableBlock.getLocation().y+drawableBlock.getHeight())));
					
					
					Rectangle cont=new Rectangle(getSize());
					Rectangle obj=new Rectangle(drawableBlock.getLocation().x, drawableBlock.getLocation().y, drawableBlock.getWidth(), drawableBlock.getHeight());
					
					if(!cont.contains(obj))
					{
						int w=(int) (obj.getWidth()+obj.getX());
						int h=(int) (obj.getHeight()+obj.getY());
						if(w<getWidth())	w=getWidth();
						if(h<getHeight())	h=getHeight();
						setPreferredSize(new Dimension(w,h));
					}
					
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
		if (DrawableBlock.firstBLock != null) {
			g.setColor(Color.blue);
			/*OutputLines line = new OutputLines(DrawableBlock.firstBLock);
			try{
				line.drawLine(DrawableBlock.firstBLock.getOutputPoint(),
					      cursorPoint, g);
			}catch(Exception e){}*/
		}
		if (selectedRec != null) {
			g.setColor(Color.yellow);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(5));
			g2.drawRect(selectedRec.x, selectedRec.y, selectedRec.width,
				    selectedRec.height);
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		cursorPoint = e.getPoint();

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
		if (dragStartPoint != null && selectedRec==null) {
			selectedRec = new Rectangle(dragStartPoint.x, dragStartPoint.y,
						    cursorPoint.x - dragStartPoint.x, cursorPoint.y
						    - dragStartPoint.y);
			selectBlocks();
		}else {
			
		}

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
		if(DrawableBlock.CurrentNote!=null){
			DrawableBlock.CurrentNote.setFont(new Font(Font.MONOSPACED, Font.ITALIC
								   | Font.BOLD, 14));
			DrawableBlock.CurrentNote.setColor(Color.black);
			DrawableBlock.setCurrentBlock(null, null);//CurrentNote = null;
		}	
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
			test.setEdited();
		} else if (DrawableBlock.firstBLock != null) {
			DrawableBlock.firstBLock.setColor(Color.black);
			DrawableBlock.firstBLock = null;
		}

		
		deSelectBlocks();
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		cursorPoint = e.getPoint();
		if (dragStartPoint == null) {
			dragStartPoint = cursorPoint;
		} 
		if (selectedBlocks.size() > 0) {			
			Log.log(selectedRec);
			Log.log(getBounds());
			if (getBounds().contains(selectedRec)&&selectedRec.contains(cursorPoint)) 
			{	
				Point p=new Point();
				p.x=cursorPoint.x-dragStartPoint.x;
				p.y=cursorPoint.y-dragStartPoint.y;
				dragStartPoint = cursorPoint;
				selectedRec.x=selectedRec.x+p.x;
				selectedRec.y=selectedRec.y+p.y;
				if (getBounds().contains(selectedRec)){
					for (DrawableBlock block : selectedBlocks) {
						block.dragBlock(e.getLocationOnScreen());
					}
				}else{
					selectedRec.x=selectedRec.x-p.x;
					selectedRec.y=selectedRec.y-p.y;
				}							
			}
			else if(!selectedRec.contains(cursorPoint)){
				deSelectBlocks();
			}
		}
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
}
