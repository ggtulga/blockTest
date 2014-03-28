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
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class test extends JFrame {
	testPanel mainPanel;
	JythonFactory jf = JythonFactory.getInstance();
	
	public test() {
		setLayout(new BorderLayout());
		JPanel temp=new JPanel(new GridLayout());
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), " ");
		title.setTitlePosition(TitledBorder.CENTER);
		temp.setBorder(title);
		mainPanel = new testPanel();
		temp.add(mainPanel);
		ToolBar t = new ToolBar();
		setLayout(new BorderLayout());
		JButton b= new JButton("check");
		b.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					DrawableBlock node = mainPanel.getStartBlock();
					LoggerType logger = (LoggerType) jf.getJythonObject(
						"LoggerType", "/home/ggt/src/blockTest/src/logger.py");
					System.out.println(logger.run_script("hello"));
				}
			});
		
		add(b, BorderLayout.NORTH);
		// JTextArea output = new JTextArea("ldfskgldfjgv");
		// output.setEnabled(false);
		// add(output, BorderLayout.SOUTH);
		add(t, BorderLayout.LINE_START);
		add(temp, BorderLayout.CENTER);
		this.addKeyListener(mainPanel.keyListner);
		this.setVisible(true);
		this.repaint();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new test();
	}

	public class ToolBar extends JPanel implements MouseListener {
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
}
