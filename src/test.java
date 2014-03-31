import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class test extends JFrame {

	private static final long serialVersionUID = 1L;
	testPanel mainPanel;
	@SuppressWarnings("deprecation")
	public test() {
		setLayout(new BorderLayout());
		JPanel temp=new JPanel(new GridLayout());
		temp.setSize(1000,1000);
		//JScrollPane temps=new JScrollPane(temp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//new JScrollPane(testField);
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), " ");
		title.setTitlePosition(TitledBorder.CENTER);
		temp.setBorder(title);
		mainPanel = new testPanel();
		mainPanel.newPanel();
		mainPanel.setSize(1500,500);
		temp.add(mainPanel);		
		ToolBar t = new ToolBar();
		setLayout(new BorderLayout());			
		add(t, BorderLayout.LINE_START);
		add(temp, BorderLayout.CENTER);			

		JButton btnSave=new JButton("Save");
		btnSave.setFocusable(false);
		btnSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {				
				save();
			}
			
		});
		
		this.add(btnSave,BorderLayout.NORTH);
		
		JButton btnCheck=new JButton("Check");
		btnCheck.setFocusable(false);
		btnCheck.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Construct python code
				CodeGenerator g = new CodeGenerator();
				g.generateCode(mainPanel.getStartBlock());
			}				
		});
		
		
		this.add(btnCheck, BorderLayout.SOUTH);
	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.repaint();		
	}

	public static void main(String[] args) {
		new test();
	}
	public class ToolBar extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L;
		public ToolBar() {
			super();
			setLayout(new GridLayout());
			List<JComponent> tools = new ArrayList<JComponent>();
			JComponent cc = new InitBlock(3);
			cc.setLocation(18, 0);
			tools.add(cc);
			cc = new InputBlock(3);
			cc.setLocation(14, 0);
			tools.add(cc);
			cc = new ValueBlock(3);
			// cc.setLocation(10,0);
			tools.add(cc);
			cc = new OutputBlock(3);
			cc.setLocation(18, 0);
			tools.add(cc);
			cc = new IfBlock(3);
			cc.setLocation(3, 0);
			tools.add(cc);
			cc = new EndBlock(3);
			cc.setLocation(15, 0);
			tools.add(cc);
			JPanel temp = new JPanel(new GridLayout(7, 1));
			for (JComponent c : tools) {
				c.addMouseListener(this);
				c.addMouseMotionListener(null);
				JPanel t = new JPanel(null);
				// c.setLocation(10,10);
				t.add(c);

				temp.add(t);
			}
			System.out.println(temp.getHeight());
			temp.setSize(100, 1000);
			TitledBorder title = BorderFactory.createTitledBorder(
					BorderFactory.createLoweredBevelBorder(), "Блокууд");
			title.setTitlePosition(TitledBorder.CENTER);
			temp.setBorder(title);
			temp.add(new JLabel("                                      "));
			temp.setSize(100, 100);
			this.add(temp);
		}
		@Override
		public void mouseClicked(MouseEvent e) {

			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			DrawableBlock block = (DrawableBlock) e.getSource();
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
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			DrawableBlock block = (DrawableBlock) e.getSource();
			if (!block.getColor().equals(Color.red))
				block.setColor(Color.blue);
		}
		@Override
		public void mouseExited(MouseEvent e) {
			DrawableBlock block = (DrawableBlock) e.getSource();
			if (!block.getColor().equals(Color.red))
				block.setColor(Color.black);
		}
	}
	public void save(){
		mainPanel.savePanel("");
	}
}
