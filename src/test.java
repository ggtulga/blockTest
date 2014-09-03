import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class test extends JFrame {

	private JFileChooser fileChooser;
	// Drawing area
	testPanel mainTestPanel;

	// save, open, check buttons
	JPanel jpMenuBar;
	// Show outputs for the code generation
	//JScrollPane output;
	JTextArea errArea;
	JTextArea outArea;

	public test() {
		setLayout(new BorderLayout());

		//JPanel mainPanel = new JPanel(new GridLayout());
		
		TitledBorder title = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
				"шинэ талбар");
		//title.setTitlePosition(TitledBorder.CENTER);
		//mainPanel.setBorder(title);
		
		mainTestPanel = new testPanel();
		mainTestPanel.setBorder(title);
		//mainPanel.add(mainTestPanel);
		JScrollPane mainScroll = new JScrollPane(mainTestPanel,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		//init menu
		fileChooser = new JFileChooser();
		FileNameExtensionFilter blockFilter = new FileNameExtensionFilter("block files (*.block)", "block");
		fileChooser.setFileFilter(blockFilter);

		// Buttons are added to this menubar
		jpMenuBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JButton btnSave = new JButton("Хадгалах");
		btnSave.setFocusable(false);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				int returnVal = fileChooser.showSaveDialog(mainTestPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					mainTestPanel.savePanel(file);
					repaint();
				} 
			}			
		});
		jpMenuBar.add(btnSave);

		JButton btnOpen = new JButton("Нээх");
		btnOpen.setFocusable(false);
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(mainTestPanel);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					mainTestPanel.openFile(file);
					repaint();
				}
			}			
		});
		jpMenuBar.add(btnOpen);

		JButton btnCheck = new JButton("Шалгах");
		btnCheck.setFocusable(false);
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Construct python code
				//DrawableBlock.setCurrentBlock(null);
				ErrorCheck checker = new ErrorCheck();
				if (checker.checkForErrors(mainTestPanel.getStartBlock()) == false) {
					CodeGenerator g = new CodeGenerator();
					if (g.generateCode(mainTestPanel.getStartBlock())) {
						outArea.setText(g.getOutput());
					} else {
						outArea.setText(g.getError());
					}
				} else
					errArea.setText(checker.getErrors());
				
			}				
		});
		jpMenuBar.add(btnCheck);

		this.add(jpMenuBar, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		// textfield to show outputs
		outArea = new JTextArea();
		outArea.setFocusable(false);
		outArea.setEditable(false);
		JScrollPane outputScrollPane = new JScrollPane(outArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		errArea = new JTextArea();
		errArea.setFocusable(false);
		errArea.setEditable(false);
		errArea.setForeground(Color.red);
		
		JScrollPane errorScrollPane = new JScrollPane(errArea, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		tabbedPane.addTab("Алдаа", errorScrollPane);
		tabbedPane.addTab("Гаралт", outputScrollPane);

		JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		verticalSplit.add(horizontalSplit);

		horizontalSplit.add(new ToolBar());
		horizontalSplit.add(mainScroll);
		verticalSplit.setDividerLocation(500);

		this.add(verticalSplit, BorderLayout.CENTER);
		
		verticalSplit.add(tabbedPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setVisible(true);;
		this.repaint();		
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
				mainTestPanel.setNewBlock(null);
			} else {
				block.setColor(Color.red);
				if(mainTestPanel.getNewBlock()!=null)
					mainTestPanel.getNewBlock().setColor(Color.black);
				mainTestPanel.setNewBlock(block);
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
