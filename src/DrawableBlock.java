
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
	private static final long serialVersionUID = 1L;
	public static DrawableBlock CurrentNote = null, firstBLock = null, SelectedBlock=null;

	public static KeyListener keyListener;
	private String Text;
	private Font textFont;
	private JPopupMenu popupMenu=null;
	private Point temp;
	private int state = 0;
	public BLOCKTYPE TYPE;
	private Color color,beforeColor;
	private DrawableBlock next = null;
	private List<DrawableBlock> beforeBlocks = new ArrayList<DrawableBlock>();
	private dragDrop action;
	public DrawableBlock() {
		super();
		Text = "";
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(0, 0);
		setSize(200, 200);
		setColor(Color.black);


		action=new dragDrop();
		popupMenu = new JPopupMenu("Menu");
		initListners();
		initMenu() ;
		
	}
	@Override
	public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
		super.setEnabled(enabled);
		if(enabled){
			initListners();
			this.add(popupMenu);
			
		}else{
			this.removeMouseMotionListener(action);
			this.removeMouseListener(action);
			this.remove(popupMenu);
			
		}
	}
	public DrawableBlock(DrawableBlock block) {
		super();
		Text = block.getText();
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(block.getLocation());
		setSize(block.getSize());
		setColor(Color.black);
		popupMenu = new JPopupMenu("Menu");
		action=new dragDrop();
		initListners();
		initMenu();
	}
	
	/**
	 * */
	public DrawableBlock(int i){
		super();
		Text = "";
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(0, 0);
		setSize(200, 200);
		setColor(Color.black);
		popupMenu = new JPopupMenu("Menu");
		action=new dragDrop();
		switch (i){
		case 0:initMenu();
		case 1: initListners();
		
		}
		
	}
	public void initListners(){
		this.addMouseMotionListener(action);
		this.addMouseListener(action);
	}
	public void initMenu() {
		// Create some menu items for the popup
		JMenuItem menuFileNew = new JMenuItem("Засварлах");
		JMenuItem menuLink = new JMenuItem("Устгах");
		// Create a popup menu
		
		popupMenu.add(menuFileNew);
		popupMenu.add(menuLink);
		this.add(popupMenu);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		menuFileNew.addActionListener(this);
		menuLink.addActionListener(this);
		
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
		// draw(g);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Component cont = this.getParent();
		cont.repaint();
		int charWidth=getFont().getSize();
		if(getText().length()<6){
			this.setSize(80, 50);
		}else
			this.setSize((int)(charWidth/1.5) *getText().length()+20, 50);
		g.setFont(getFont());
		draw(g);
	}

	public abstract void draw(Graphics g);

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
		repaint();
	}

	public Point getTemp() {
		return temp;
	}

	public void setTemp(Point temp) {
		this.temp = temp;
	}

	public void processMouseEvent(MouseEvent event) {
		super.processMouseEvent(event);
		if (event.isPopupTrigger()) {
			if(popupMenu!=null)
			popupMenu.show(event.getComponent(), event.getX(), event.getY());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Засварлах":
			CurrentNote = this;
			setFocusable(true);
			textFont = new Font(Font.MONOSPACED, Font.ITALIC, 14);
			firstBLock = null;
			break;
		case "Устгах":
			if(this.TYPE!=BLOCKTYPE.BEGIN){
				Container c = this.getParent();
				for (DrawableBlock b : beforeBlocks){
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
						block.getNextTrue().removeBefore(this);
					if (block.getNextFalse() != null)
						block.getNextFalse().removeBefore(this);
					block.setNextTrue(null);
					block.setNextFalse(null);
				} else if (this.getNext() != null)
					this.getNext().removeBefore(this);
				this.setNext(null);
				c.repaint();
				c.remove(this);
				firstBLock = null;
			}
		default:
			break;
		}
		//e.getSource();
		//test.setedited();
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
		setBeforeColor(this.color);
		this.color = color;
	}

	public DrawableBlock getNext() {
		return next;
	}

	public void setNext(DrawableBlock next) {
		if (this.next != null && next != null) {
			this.next.removeBefore(this);
		}
		this.next = next;
		if (next != null)
			this.next.addBefore(this);

	}

	public void addBefore(DrawableBlock b) {
		beforeBlocks.add(b);
	}

	public void removeBefore(DrawableBlock b) {
		beforeBlocks.remove(b);
	}
	public boolean isHasBefore(){
	if (beforeBlocks.size()>0)
		return true;
	else
		return false;
	}
	public Color getBeforeColor() {
		return beforeColor;
	}
	public void setBeforeColor(Color beforeColor) {
		this.beforeColor = beforeColor;
	}
	public boolean dragBlock(Point p){		
		Point temp=new Point(p);
		if(getTemp()==null) setTemp(temp);
		else{
			p.x=p.x-getTemp().x;
			p.y=p.y-getTemp().y;
			setTemp(temp);
			p.x=getLocation().x+p.x;
			p.y=getLocation().y+p.y;
			Container c=getParent();
			if(!(p.x<0||p.y<0||(p.x+getWidth())>c.getWidth()||(p.y+getHeight())>c.getHeight())){
				setLocation(p);
				repaint();		
			}else if((p.x+getWidth())>c.getWidth()||(p.y+getHeight())>c.getHeight()){
				int h=c.getHeight();
				int w=c.getWidth();
				if((p.x+getWidth())>w)
					w=(p.x+getWidth());
				if((p.y+getHeight())>h)
					h=(p.y+getHeight());
				c.setPreferredSize(new Dimension(w, h));
				setLocation(p);
				c.getParent().revalidate();
				repaint();
				
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
		System.out.println("in");
		if(getNext()!=null && !list.contains(getNext())){
			System.out.println("inn");
			getNext().addToList(list);
		}
	}
	
	
	public static DrawableBlock getSelectedBlock() {
		return SelectedBlock;
	}
	public static void setSelectedBlock(DrawableBlock selectedBlock, Color color) {
		if(SelectedBlock!=null){
			SelectedBlock.setColor(SelectedBlock.getBeforeColor());
		}
			SelectedBlock = selectedBlock;
			if(SelectedBlock!=null){
				SelectedBlock.setColor(color);
			}					
	}
	
	
	
}
