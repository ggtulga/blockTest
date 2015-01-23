
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


public abstract class DrawableBlock extends JComponent implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static DrawableBlock firstBLock = null, SelectedBlock=null;

	public static KeyListener keyListener;
	private Font textFont;
	private JPopupMenu popupMenu=null;
	private Point temp;
	public BLOCKTYPE TYPE;
	private Color color, beforeColor;
	JTextField input;
	public List<DrawableBlock> getBeforeBlocks() {
		return beforeBlocks;
	}
	public void setBeforeBlocks(List<DrawableBlock> beforeBlocks) {
		this.beforeBlocks = beforeBlocks;
	}
	private DrawableBlock next = null;
	private List<DrawableBlock> beforeBlocks = new ArrayList<DrawableBlock>();
	private dragDrop action;
	public DrawableBlock(DrawableBlock b){
		input=new JTextField(b.TYPE.getValue());
		input.addKeyListener(new InputText());
		this.TYPE=b.TYPE;
		//this.next=b.getNext();
		action=new dragDrop();
		popupMenu = new JPopupMenu("Menu");		
		textFont = b.getFont();
		setLocation(b.getLocation());
		setSize(b.getSize());
		setColor(Color.black);
		initListners();
		initMenu() ;
		setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
		//setLayout(new GridLayout(3, 1,5,10));
		
	}
	public DrawableBlock(BLOCKTYPE type) {
		super();
		input=new JTextField(type.getValue());
		input.addKeyListener(new InputText());
		TYPE=type;
		textFont = new Font(Font.DIALOG_INPUT, Font.ITALIC | Font.BOLD, 14);
		setLocation(0, 0);
		setSize(200, 200);
		setColor(Color.black);


		action=new dragDrop();
		popupMenu = new JPopupMenu("Menu");
		initListners();
		initMenu() ;
		setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
		
	}
	@Override
	public void setEnabled(boolean enabled) {
		
		setColor(Color.black);
		super.setEnabled(enabled);
		if(enabled){
			initListners();
			enableEvents(AWTEvent.MOUSE_EVENT_MASK);
			
		}else{
			this.removeMouseMotionListener(action);
			this.removeMouseListener(action);
			this.remove(this.popupMenu);
			disableEvents(AWTEvent.MOUSE_EVENT_MASK);
		}
	}
	
	
	/**
	 * */
	public DrawableBlock(BLOCKTYPE type,int i, final testPanel mainPanel){
		super();
		input=new JTextField();
		input.addKeyListener(new InputText());
		TYPE=type;
		textFont = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 14);
		setLocation(0, 0);
		setSize(200, 200);
		setColor(Color.black);
		popupMenu = new JPopupMenu("Menu");
		action=new dragDrop();
		switch (i){
		case 0: initMenu();
		case 1: initListners();
		}
		if(!(TYPE==BLOCKTYPE.BEGIN || TYPE==BLOCKTYPE.POINT)){
			setLayout(new FlowLayout(FlowLayout.CENTER,0,15));
			input.setFont(getFont());
			input.setHorizontalAlignment(JTextField.CENTER);
			//this.add(new JLabel());
			this.add(input);
			input.setEnabled(false);
			input.setBorder(null);
			input.addMouseListener(new MouseListener() {
			
					@Override
					public void mouseReleased(MouseEvent e) {
				
					}
			
					@Override
					public void mousePressed(MouseEvent e) {
						DrawableBlock block = (DrawableBlock) ((JTextField)e.getSource()).getParent();
						if (block.getColor().equals(Color.red)) {
							block.setColor(Color.blue);
							mainPanel.setNewBlock(null);
						} else {
							block.setColor(Color.red);
							if(mainPanel.getNewBlock()!=null)
								mainPanel.getNewBlock().setColor(Color.black);
							mainPanel.setNewBlock(block);
					
						}
				
					}
			
			
					@Override
					public void mouseExited(MouseEvent e) {
						DrawableBlock block = (DrawableBlock) ((JTextField)e.getSource()).getParent();
						block.setColor(color.black);
				
					}
			
					@Override
					public void mouseEntered(MouseEvent e) {
						DrawableBlock block = (DrawableBlock) ((JTextField)e.getSource()).getParent();
						block.setColor(color.blue);
					}
			
					@Override
					public void mouseClicked(MouseEvent e) {
		
				
					}
				});
		}
		
	}
	public void initListners(){
		this.addMouseMotionListener(action);
		this.addMouseListener(action);
		
	}
	public void initMenu() {

		if(!(TYPE==BLOCKTYPE.BEGIN || TYPE==BLOCKTYPE.POINT))
		{
			input.setFont(getFont());
			input.setHorizontalAlignment(JTextField.CENTER);
			this.add(input);
			input.setBorder(null);
			if(TYPE==BLOCKTYPE.END ) input.setEnabled(false);
		}
		
		JMenuItem menuDelete = new JMenuItem("Устгах");
				
		menuDelete.setMnemonic(KeyEvent.VK_DELETE);
		menuDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
								 ActionEvent.CTRL_MASK));
		
		menuDelete.addActionListener(this);
		if(TYPE!=BLOCKTYPE.BEGIN)
			popupMenu.add(menuDelete);
		this.add(popupMenu);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		
		
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
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		int charWidth=getFont().getSize();
		if(getText().length()<=9){
			this.setSize(90, 50);
		}else
			this.setSize((int)(charWidth/1.5) *getText().length(), 50);
		g.setFont(getFont());
		revalidate();
		draw(g);
		
		
	}

	public abstract void draw(Graphics g);

	public String getText() {
		return input.getText();
	}
	public void reset(){
		repaint();
	}
	public void setTextValue(String text) {
		// Text = text;
		input.setText(text);
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
		
		if(e.getActionCommand().equals("Устгах"))
		{
			removeThis();
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

	

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		setBeforeColor(this.color);
		this.color = color;
		repaint();
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
	
	public boolean isCanMoveToXY(Point p){
		if(getTemp()==null) setTemp(temp);
		else{
			p.x=p.x-getTemp().x;
			p.y=p.y-getTemp().y;
			p.x=getLocation().x+p.x;
			p.y=getLocation().y+p.y;
			if(p.x>=6&&p.y>=18)
				return true;
		}
		return false;
	}
	public boolean dragBlock(Point p){		
		Point temp=new Point(p);
		//if(isCanMoveToXY(p))
		if(getTemp()==null) setTemp(temp);
		else{
			p.x=p.x-getTemp().x;
			p.y=p.y-getTemp().y;
			setTemp(temp);
			p.x=getLocation().x+p.x;
			p.y=getLocation().y+p.y;
			Container c=getParent();
			Rectangle rect=new Rectangle(p.x, p.y, getWidth(), getHeight());
			if(c.getBounds().contains(rect)){
				setLocation(p);
				//repaint();	
				getParent().repaint();
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
				//repaint();
				getParent().repaint();
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
		Log.log("in");
		if (getNext()!=null && !list.contains(getNext())){
			Log.log("inn");
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

	
	public void removeThis(){
		
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
			try{
				c.remove(this);

				c.repaint();
			}catch(Exception e){};
			setFirstBLock(null);
		}
	}
	public void setType(int i){
		
	}
	
	public static DrawableBlock getFirstBLock() {
		return firstBLock;
	}
	public static void setFirstBLock(DrawableBlock firstBLock) {
		if(DrawableBlock.firstBLock!=null)
			DrawableBlock.firstBLock.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		DrawableBlock.firstBLock = firstBLock;

		if(DrawableBlock.firstBLock!=null)
			DrawableBlock.firstBLock.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
	}
	public class InputText  implements KeyListener
	{
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			JTextField source=(JTextField) arg0.getSource();
			if (getText().equals("")) {
				setTextValue("       ");
			} else if (getText().trim().equals("") == false)
				setTextValue(getText().trim());
			
			source.setColumns(source.getText().length()+1);
			revalidate();
			repaint();
			
			
		}
		
	}
	public void setEditable() {
		this.revalidate();
		this.repaint();
	}
}
