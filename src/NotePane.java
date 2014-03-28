
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class NotePane  extends JComponent implements ActionListener{
	public static NotePane  CurrentNote=null;
	//private Image note;
	private String Text;
	private Font textFont;
	private	JPopupMenu	popupMenu;
	private int state=0;
	private Point temp;
	public KeyListener keyListener;
	
	private int H,W;
	public NotePane(){
		super();
		
		
		initMenu();
		keyListener=new textEdit();
		Text=" sdgvsdnnnnnnnnnnnnnn";
		textFont=new Font(Font.MONOSPACED, Font.ITALIC|Font.BOLD, 14);
		
		setLocation(0, 0);
		setSize(200, 200);		
		this.addMouseMotionListener(new dragDrop());
		this.addMouseListener(new dragDrop());
					
	}
	private void initMenu(){
		// Create some menu items for the popup
				JMenuItem menuFileNew = new JMenuItem( "Edit" );
				// Create a popup menu
				popupMenu = new JPopupMenu( "Menu" );
				popupMenu.add( menuFileNew );
				
				this.add( popupMenu );

				enableEvents( AWTEvent.MOUSE_EVENT_MASK );
				menuFileNew.addActionListener( this );
				
		
	}
	@Override
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		super.setFont(font);
		textFont=font;
	}
@Override
public void paintAll(Graphics g) {
	// TODO Auto-generated method stub
	super.paintAll(g);
//	draw(g);
}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		draw(g);
	}
	public void draw(Graphics g) {
			int charWidth=textFont.getSize();
			this.setSize((int)(charWidth/1.5) *getText().length()+20, 30);
			
			g.setFont(textFont);

							
			g.setColor(Color.black);
			g.drawRect(5, 5, getWidth()-10, getHeight()-10);
			g.drawString(getText(), 10, 20);

		}
	
	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
		repaint();
	}
	public class textEdit implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			//System.out.println("pressed");
			
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void keyTyped(KeyEvent e) {
			//System.out.println("typed");
			//JOptionPane.showMessageDialog(null, Text);
			//NotePane note=(NotePane)e.getSource();
			if(CurrentNote!=null){
				String tempText=CurrentNote.getText();
				switch ((int)e.getKeyChar()) {
				case KeyEvent.VK_ENTER:
					
					CurrentNote.setFont(new Font(Font.MONOSPACED, Font.ITALIC|Font.BOLD, 14));
					CurrentNote=null;
					break;
				case KeyEvent.VK_BACK_SPACE:
					if(tempText.length()>0)
						tempText=tempText.substring(0,tempText.length()-1);				
					CurrentNote.setText(tempText);
					break;
		
				default:
					tempText+=e.getKeyChar();
					CurrentNote.setText(tempText);
					break;
				}
			}
			
		}
	}
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Point getTemp() {
		return temp;
	}

	public void setTemp(Point temp) {
		this.temp = temp;
	}
	public class dragDrop implements MouseMotionListener,MouseListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			
			NotePane tempNote=(NotePane) e.getSource();
			Point p =e.getLocationOnScreen();
			p.x=p.x-tempNote.getTemp().x;
			p.y=p.y-tempNote.getTemp().y;
			tempNote.setTemp(e.getLocationOnScreen());
			p.x=tempNote.getLocation().x+p.x;
			p.y=tempNote.getLocation().y+p.y;
			tempNote.setLocation(p);
			tempNote.repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {

			//NotePane note=(NotePane)e.getSource();
			//note.setBorder(BorderFactory.createLineBorder(Color.red));
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//NotePane note=(NotePane)e.getSource();
			//note.setBorder(null);
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			NotePane tempNote=(NotePane) e.getSource();
			tempNote.setState(1);
			tempNote.setTemp(e.getLocationOnScreen());
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			NotePane tempNote=(NotePane) e.getSource();
			tempNote.setState(0);
		}
		
	}
	
	public void processMouseEvent( MouseEvent event )
	{
		if( event.isPopupTrigger() )
		{
			popupMenu.show( event.getComponent(),event.getX(), event.getY() );
		}

		super.processMouseEvent( event );
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {	
		switch (e.getActionCommand()) {
		case "Edit":			
			CurrentNote=this;
			textFont=new Font(Font.MONOSPACED, Font.ITALIC, 14);
			break;

		default:
			break;
		}
	}
	public int getW() {
		return W;
	}
	public void setW(int w) {
		W = w;
	}
	public int getH() {
		return H;
	}
	public void setH(int h) {
		H = h;
	}
	
	
}
