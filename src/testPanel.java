import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
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

/*
  testPanel class contains 
*/

public class testPanel extends JPanel implements MouseMotionListener,
						 MouseListener {
    // points to the starting block
    private DrawableBlock startBlock;
    private DrawableBlock newBlock = null;
    private Point cursorPoint;
    private Point dragStartPoint = null;
    private List<DrawableBlock> selectedBlocks;
    private Rectangle selectedRec = null;
    
    public testPanel() {
	setLayout(null);
	this.addMouseMotionListener(this);
	this.addMouseListener(this);
	setBackground(Color.white);
	selectedBlocks = new ArrayList<>();
	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new MyDispacher());
	this.newPanel();
    }
	
    public void newPanel(){
	this.removeAll();
	startBlock = new StartBlock(1);
	add(startBlock);
	repaint();
    }
	
    public void savePanel(File file){
		
	List<DrawableBlock> temp = new ArrayList<>();
	for (Component c : getComponents()) {
	    DrawableBlock block = (DrawableBlock) c;
	    if (!temp.contains(block)) {
		block.addToList(temp);
	    }
	}
	
	FileOutputStream fout;
	try {			

            String fname = file.getAbsolutePath();

            if (!fname.endsWith(".block")) {
                file = new File(fname + ".block");
            }

	    fout = new FileOutputStream(file);
	    ObjectOutputStream oos = new ObjectOutputStream(fout);
	    try{
		oos.writeObject(temp);
	    }
	    finally {
		fout.close();
	    }
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
		
		
    }
	
    public void openFile(File file){
	//filename="d:\\address.txt";
	removeAll();
	try{
	    //use buffering
	    ObjectInput input = new ObjectInputStream (new BufferedInputStream(new FileInputStream(file)));
	    try{
		//deserialize the List
		List<DrawableBlock> recoveredQuarks = (List<DrawableBlock>) input.readObject();
		//display its data
		for (DrawableBlock drawableBlock : recoveredQuarks) {
		    drawableBlock.initListners();
		    drawableBlock.initMenu();
		    add(drawableBlock);
		    System.out.println(drawableBlock);
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
	if (DrawableBlock.selectedBlock != null) {
	    g.setColor(Color.blue);
	    OutputLines line = new OutputLines(DrawableBlock.selectedBlock);
	    line.drawLine(DrawableBlock.selectedBlock.getOutputPoint(),
			  cursorPoint, g);
	}
	if (selectedRec != null) {
	    g.setColor(Color.yellow);
	    g.drawRect(selectedRec.x, selectedRec.y, selectedRec.width,
		       selectedRec.height);
	}
    }

    @Override
    public void mouseDragged(MouseEvent e) {
	cursorPoint = e.getPoint();
	if (dragStartPoint == null) {
	    dragStartPoint = cursorPoint;
	}
	if (selectedBlocks.size() > 0) {			
	    System.out.println(selectedRec);
	    System.out.println(getBounds());
	    if (getBounds().contains(selectedRec)&&selectedRec.contains(cursorPoint)) 
		{	
		    Point p = new Point();
		    p.x = cursorPoint.x-dragStartPoint.x;
		    p.y = cursorPoint.y-dragStartPoint.y;
		    dragStartPoint = cursorPoint;
		    selectedRec.x = selectedRec.x + p.x;
		    selectedRec.y = selectedRec.y + p.y;
		    if (getBounds().contains(selectedRec)) {
			for (DrawableBlock block : selectedBlocks) {
			    block.dragBlock(e.getLocationOnScreen());
			}
		    } else {
			selectedRec.x = selectedRec.x - p.x;
			selectedRec.y = selectedRec.y - p.y;
		    }							
		}
	    else if (!selectedRec.contains(cursorPoint)){
		deSelectBlocks();
	    }
	}
    }

    private void selectBlocks() {
	selectedBlocks.clear();
	// Rectangle rect=new
	// Rectangle(start.x,start.y,end.x-start.x,end.y-start.y);
	// System.out.println(rect);
	for (Component b : getComponents()) {
	    DrawableBlock block = (DrawableBlock) b;
	    // System.out.println(block.getLocation());
	    if (selectedRec.contains(block.getLocation())) {
		selectedBlocks.add(block);
		block.setColor(Color.blue);
	    } else
		block.setColor(Color.black);
	}
	if (selectedBlocks.size() == 0){
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
	System.out.println("*******");
	dragStartPoint = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	cursorPoint = e.getPoint();

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

    @Override
    public void mouseClicked(MouseEvent e) {
	if(DrawableBlock.currentBlock!=null){
	    DrawableBlock.currentBlock.setFont(new Font(Font.MONOSPACED, Font.ITALIC
						       | Font.BOLD, 14));
	    DrawableBlock.currentBlock = null;
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
	    else
		b = new IfBlock();
	    addBlock(b);
	    b.setLocation(e.getPoint());
	    newBlock.setColor(Color.black);
	    newBlock = null;
	} else if (DrawableBlock.selectedBlock != null) {
	    DrawableBlock.selectedBlock.setColor(Color.black);
	    DrawableBlock.selectedBlock = null;
	}

	if (selectedBlocks.size() > 0) {
	    deSelectBlocks();
	}
    }
	
    @Override
    public void mousePressed(MouseEvent e) {
	if(selectedRec!=null){
	    dragStartPoint = cursorPoint;
	    for (DrawableBlock block : selectedBlocks) {
		block.setTempLocation(e.getLocationOnScreen());
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

}
