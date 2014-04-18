import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class test extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFileChooser fileChooser;
	testPanel mainPanel;
	JList<String> inputList;
	Map<String,String> inputs;
	JPanel inputPanel;
	DefaultListModel<String> inputModel;
	JTextArea taResult;
	String currentVariable="";
	JPanel jpMenuBar;
	JTextField output;
	
	public test() {
		setLayout(new BorderLayout());
		JPanel temp=new JPanel(new GridLayout());
		temp.setSize(1000,1000);
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), " ");
		title.setTitlePosition(TitledBorder.CENTER);
		
		mainPanel = new testPanel();
		mainPanel.newPanel();
		mainPanel.setSize(1500,500);
		temp.setBorder(title);
		temp.add(mainPanel);		
		
		setLayout(new BorderLayout());			
		
		//init menu
		fileChooser=new JFileChooser();
		 FileNameExtensionFilter blockfilter = new FileNameExtensionFilter("block files (*.block)", "block");
		 fileChooser.setFileFilter(blockfilter);
		jpMenuBar=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnSave=new JButton("Хадгалах");
		btnSave.setFocusable(false);
		btnSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {	

				int returnVal = fileChooser.showSaveDialog(mainPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
					mainPanel.savePanel(file);
					repaint();
	            } 
			}			
		});
		
		jpMenuBar.add(btnSave);
		JButton btnOpen=new JButton("Нээх");
		btnOpen.setFocusable(false);
		btnOpen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(mainPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fileChooser.getSelectedFile();
	                mainPanel.openFile(file);
					repaint();
	            }
			}			
		});
		jpMenuBar.add(btnOpen);
		
		JButton btnCheck=new JButton("Шалгах");
		btnCheck.setFocusable(false);
		btnCheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// Construct python code
				DrawableBlock.setCurrentNote(null);
				CodeGenerator g = new CodeGenerator();
				if (g.generateCode(mainPanel.getStartBlock())) {
					output.setText(g.getOutput());
				} else {
					output.setText(g.getError());
				}
				
			}				
		});
		jpMenuBar.add(btnCheck);

		inputs=new HashMap<String, String>();
		inputPanel=new JPanel(new FlowLayout());
				
		this.add(jpMenuBar,BorderLayout.NORTH);
		
		// textfield to show outputs
		output = new JTextField();
		output.setFocusable(false);
		output.setEditable(false);
		
		
		JSplitPane testt=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane testp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		testt.add(testp);
		
		testp.add(new ToolBar());
		testp.add(temp);
		testt.setDividerLocation(500);
		//testp.setDividerLocation(80);
		testt.add(output);
		this.add(testt, BorderLayout.CENTER);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setVisible(true);;
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
}
