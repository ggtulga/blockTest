import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public abstract class DrawableBlock extends JComponent implements ActionListener {
	/**
	 * 
	 */
	public static DrawableBlock currentBlock = null, selectedBlock = null;
	public static KeyListener keyListener;
	private String text;
	private Font textFont;
	private JPopupMenu popupMenu = null;
	private Point tempLocation;
	private int state = 0;
	public BLOCKTYPE TYPE;
	private Color color, preColor;
	private DrawableBlock next = null;
	private List<DrawableBlock> preBlocks = new ArrayList<DrawableBlock>();

	public static void setCurrentBlock(DrawableBlock block) {
		if(currentBlock != null) {
			DrawableBlock.currentBlock.setFont(new Font(Font.MONOSPACED, Font.ITALIC
					| Font.BOLD, 14));
		}
		currentBlock = block;
	}

	public DrawableBlock() {
		super();
		text = "";
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(0, 0);
		setSize(200, 200);
		setColor(Color.black);
		initListners();
		initMenu() ;
	}

	public DrawableBlock(DrawableBlock block) {
		super();
		text = block.getText();
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(block.getLocation());
		setSize(block.getSize());
		setColor(Color.black);
		initListners();
		initMenu();
	}

	/**
	 * */
	public DrawableBlock(int i){
		super();
		text = "";
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(0, 0);
		setSize(200, 200);
		setColor(Color.black);
		switch (i) {
		case 0: initMenu();
		case 1: initListners();
		}
	}

	public void initListners(){
		this.addMouseMotionListener(new dragDrop());
		this.addMouseListener(new dragDrop());
	}

	void initMenu() {
		// Create some menu items for the popup
		JMenuItem menuItemEdit = new JMenuItem("Засварлах");
		JMenuItem menuItemDelete = new JMenuItem("Устгах");
		// Create a popup menu
		popupMenu = new JPopupMenu("Menu");
		popupMenu.add(menuItemEdit);
		popupMenu.add(menuItemDelete);
		this.add(popupMenu);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		menuItemEdit.addActionListener(this);
		menuItemDelete.addActionListener(this);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		textFont = font;
	}
	@Override
	public Font getFont() {

		return textFont;
	}

	@Override
	public void paintAll(Graphics g) {
		super.paintAll(g);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Component cont = this.getParent();
		cont.repaint();
		int charWidth = getFont().getSize();
		if(getText().length() < 6){
			this.setSize(80, 50);
		}else
			this.setSize((int)(charWidth / 1.5) * getText().length() + 20, 50);

		g.setFont(getFont());
		draw(g);
	}

	public abstract void draw(Graphics g);

	public String getText() {
		return text;
	}

	public void setText(String newText) {
		text = newText;
		repaint();
	}

	public Point getTempLocation() {
		return tempLocation;
	}

	public void setTempLocation(Point p) {
		this.tempLocation = p;
	}

	public void processMouseEvent(MouseEvent event) {
		super.processMouseEvent(event);
		if (event.isPopupTrigger()) {
			if(popupMenu != null)
				popupMenu.show(event.getComponent(), event.getX(), event.getY());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Засварлах":
			setCurrentBlock(this);
			setFocusable(true);
			textFont = new Font(Font.MONOSPACED, Font.ITALIC, 14);
			selectedBlock = null;
			break;

		case "Устгах":
			if(this.TYPE != BLOCKTYPE.BEGIN) {
				Container c = this.getParent();
				for (DrawableBlock b : preBlocks){
					if (b.TYPE != BLOCKTYPE.IF)
						b.setNext(null);
					else {
						IfBlock block = (IfBlock) b;
						if(block.getNextTrue().equals(this)) 
							block.setNextTrue(null);
						else
							block.setNextFalse(null);
					}
				}
				if (this.TYPE == BLOCKTYPE.IF) {
					IfBlock block = (IfBlock) this;
					if (block.getNextTrue() != null)
						block.getNextTrue().removeFromPre(this);
					if (block.getNextFalse() != null)
						block.getNextFalse().removeFromPre(this);
					block.setNextTrue(null);
					block.setNextFalse(null);
				} else if (this.getNext() != null)
					this.getNext().removeFromPre(this);
				this.setNext(null);
				c.repaint();
				c.remove(this);
				selectedBlock = null;
			}
		default:
			break;
		}
	}

	public Point getInputPoint() {
		Point p = new Point(getWidth() / 2 + getLocation().x, getLocation().y);
		return p;
	}

	public Point getOutputPoint() {
		Point p = new Point(getWidth() / 2 + getLocation().x, getHeight()
				+ getLocation().y);
		return p;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		setPreColor(this.color);
		this.color = color;
	}

	public DrawableBlock getNext() {
		return next;
	}

	public void setNext(DrawableBlock next) {
		if (this.next != null && next != null) {
			this.next.removeFromPre(this);
		}
		this.next = next;
		if (next != null)
			this.next.addToPre(this);

	}

	public void addToPre(DrawableBlock b) {
		preBlocks.add(b);
	}

	public void removeFromPre(DrawableBlock b) {
		preBlocks.remove(b);
	}

	public boolean isHasPre(){
		if (preBlocks.size()>0)
			return true;
		else
			return false;
	}

	public Color getPreColor() {
		return preColor;
	}

	public void setPreColor(Color color) {
		preColor = color;
	}

	public boolean dragBlock(Point p){
		Point temp = new Point(p);
		if(getTempLocation() == null)	setTempLocation(temp);
		else {
			p.x = p.x - getTempLocation().x;
			p.y = p.y - getTempLocation().y;
			setTempLocation(temp);
			p.x = getLocation().x + p.x;
			p.y = getLocation().y + p.y;
			Container c = getParent();
			if(!(p.x < 0 || p.y < 0 ||
					(p.x + getWidth()) > c.getWidth() || (p.y + getHeight()) > c.getHeight())) {
				setLocation(p);
				//repaint();		
			}
			return true;
		}
		return false;
	}
	/*public String toXMLTags(){
      String result="<Block type='"+TYPE.getValue()+"', text='"+getText()+"', x="+getLocation().x+", y="+getLocation().y+">";
      if(getNext()!=null)
      result+=getNext().toXMLTags();
      result+="</Block>";		
      return result;
      }*/

	public void addToList(List<DrawableBlock> list){
		list.add(this);
		//System.out.println("in");
		if(getNext() != null && !list.contains(getNext())){
			//System.out.println("inn");
			getNext().addToList(list);
		}
	}
}
