import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class test extends JFrame {

	private static final long serialVersionUID = 1L;
	private File currentFile=null;
	private final static String TITLE = "Flow chart runner";
	private static char FileEdited=' ';
	private JFileChooser fileChooser;
	private List<DrawableBlock> TempCopy;
	private int pastCount=0;
	testPanel mainPanel;
	JList<String> inputList;
	Map<String,String> inputs;
 	JPanel inputPanel;
	DefaultListModel<String> inputModel;
	JTextArea taResult;
	String currentVariable="";
	JPanel jpMenuBar;
	ToolBar toolBar;
	JPanel result;
	private JPanel temp=new JPanel(new GridLayout());
	@SuppressWarnings("deprecation")
	public test() {
		setTitle(TITLE);
		setLayout(new BorderLayout());
		JPanel holderPanel = new JPanel(new BorderLayout());
		holderPanel.add(temp, BorderLayout.NORTH);
		setLayout(new BorderLayout());
		//Size(1000,1000);
		TitledBorder title = BorderFactory.createTitledBorder(
			BorderFactory.createLoweredBevelBorder(), " ");
		title.setTitlePosition(TitledBorder.CENTER);
		setJMenuBar(createMenuBar());
		
		result=new JPanel(new GridLayout(1,1));
	
		temp.setBorder(title);

		mainPanel = new testPanel(this);
		mainPanel.newPanel();
		mainPanel.setPreferredSize(new Dimension(1000, 1000));
		temp.add(mainPanel);		
		toolBar = new ToolBar();
		setLayout(new BorderLayout());			
		add(toolBar, BorderLayout.LINE_START);
		
		JPanel tt=new JPanel(new BorderLayout());
		tt.add(new JScrollPane(holderPanel), BorderLayout.CENTER);
		tt.add(result,BorderLayout.SOUTH);
		getContentPane().add(tt, BorderLayout.CENTER);
		//add(temp, BorderLayout.CENTER);	
		
		
		fileChooser=new JFileChooser();
		FileNameExtensionFilter blockfilter = new FileNameExtensionFilter("block files (*.block)", "block");
		fileChooser.setFileFilter(blockfilter);
		jpMenuBar=new JPanel(new FlowLayout(FlowLayout.LEFT));
		//jpMenuBar.add(new JTextField("fgdfgdfgffffffffffffffffff"));
		JButton btnNew=new JButton("Шинэ файл");
		btnNew.setFocusable(false);
		btnNew.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {	
					newp();
				}			
			});
		jpMenuBar.add(btnNew);
		JButton btnSave=new JButton("Файл хадгалах");
		btnSave.setFocusable(false);
		btnSave.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {	

					save();
				}			
			});
		jpMenuBar.add(btnSave);
		JButton btnOpen=new JButton("Файл нээх");
		btnOpen.setFocusable(false);
		btnOpen.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					open();
				}			
			});
		jpMenuBar.add(btnOpen);
		
		//////////////////debug test////////////////////////
		JButton btnrun=new JButton("Шалгах");
		btnrun.setToolTipText("ctrl+F9");
		btnrun.setFocusable(false);
		btnrun.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					save();
					checkAndRun();
				}

						
			});
		jpMenuBar.add(btnrun);
		/*
		  JButton btnCopy=new JButton("Copy");
		  btnCopy.setToolTipText("ctrl+c");
		  btnCopy.setFocusable(false);
		  btnCopy.addActionListener(new ActionListener(){

		  @Override
		  public void actionPerformed(ActionEvent e) {
		  checkAndRun();
		  }

						
		  });
		  jpMenuBar.add(btnCopy);
		
		  JButton btnPaste=new JButton("Paste");
		  btnPaste.setToolTipText("ctrl+v");
		  btnPaste.setFocusable(false);
		  btnPaste.addActionListener(new ActionListener(){

		  @Override
		  public void actionPerformed(ActionEvent e) {
		  checkAndRun();
		  }

						
		  });
		  jpMenuBar.add(btnPaste);
		*/
		
		inputs=new HashMap<String, String>();
		inputPanel=new JPanel(new FlowLayout());
		
		this.setMinimumSize(new Dimension(1000, 500));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		// this.setSize(1000, 700);
		this.add(jpMenuBar,BorderLayout.NORTH);
		//this.add(result,BorderLayout.SOUTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.show();
		this.repaint();		
	}
	public static void main(String[] args) {
		new test();
	}
	private void showError(CodeGenerator g) {
		setEditable(false);
		Log.log("checker error");
		result.removeAll();
		pnlError pnl = new pnlError(mainPanel, g.getErrors());
		result.add(pnl, BorderLayout.SOUTH);
		revalidate();
		repaint();

		// JOptionPane.showMessageDialog (this, g.getError(), "Алдаатай алгоритм", JOptionPane.ERROR_MESSAGE);
		
	}
	public class ToolBar extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L;
		List<JComponent> tools;
		
		public ToolBar() {
			super();
			setLayout(new GridLayout());
			tools = new ArrayList<JComponent>();
			JComponent cc = new InitBlock(3,mainPanel);
			cc.setLocation(43, 0);
			tools.add(cc);
			cc = new InputBlock(3,mainPanel);
			cc.setLocation(43, 0);
			tools.add(cc);
			cc = new ValueBlock(3,mainPanel);
			cc.setLocation(43,0);
			tools.add(cc);
			cc = new OutputBlock(3,mainPanel);
			cc.setLocation(43, 0);
			tools.add(cc);
			cc = new IfBlock(3,mainPanel);
			cc.setLocation(13, 0);
			tools.add(cc);
			cc = new EndBlock(3,mainPanel);
			cc.setLocation(43, 0);
			tools.add(cc);
			cc = new PointBlock(3,mainPanel);
			cc.setLocation(78, 0);
			tools.add(cc);
			initTools();
			
			
			
		}
		public void initTools(){
			removeAll();
			JPanel temp = new JPanel(new GridLayout(8, 1));
			for (JComponent c : tools) {
				c.addMouseListener(this);
				c.addMouseMotionListener(null);
				JPanel t = new JPanel(null);
				// c.setLocation(10,10);
				t.add(c);

				temp.add(t);
			}
			
			Log.log(temp.getHeight());

			
			temp.setSize(100, 1000);
			TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "Блокууд");
			title.setTitlePosition(TitledBorder.CENTER);
			temp.setBorder(title);
			temp.add(new JLabel("                                                          "));
			temp.setSize(100, 100);
			this.add(temp);
		}
		public List<JComponent> getTools() {
			return tools;
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
				setEdited();
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
	public void setEditable(boolean edit){
		mainPanel.setEnabled(edit);
		
		if(edit){
			toolBar.initTools();
			result.removeAll();
		}
		else{
			for (Component c : toolBar.getTools()) {
					
				if(!edit)
				{	
					DrawableBlock b=(DrawableBlock)c;
					b.setEnabled(edit);
					b.removeMouseListener(toolBar);
				}				
			}
		}
		for (Component c : jpMenuBar.getComponents()) {			
			c.setEnabled(edit);
		
		}
		revalidate();
		repaint();
	}
	public JMenuBar createMenuBar() {
		JMenuBar top_menu_bar = new JMenuBar();
		JMenu main_menu = new JMenu("Цэс");
		main_menu.setMnemonic(KeyEvent.VK_M);
		top_menu_bar.add(main_menu);
		JMenuItem menu_item;

		menu_item = new JMenuItem("Шинэ файл үүсгэх");
		menu_item.setMnemonic(KeyEvent.VK_N);
		menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
								ActionEvent.CTRL_MASK));
		menu_item.setActionCommand("new");
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getActionCommand().equals("save")){
						newp();
					}
				}
			});
		main_menu.add(menu_item);
		menu_item = new JMenuItem("Файл нээх");
		menu_item.setMnemonic(KeyEvent.VK_O);
		menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
								ActionEvent.CTRL_MASK));
		menu_item.setActionCommand("open");
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getActionCommand().equals("open")){
						open();
					}
				}
			});
		main_menu.add(menu_item);
		menu_item = new JMenuItem("Хадгалах");
		menu_item.setMnemonic(KeyEvent.VK_S);
		menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
								ActionEvent.CTRL_MASK));
		menu_item.setActionCommand("save");
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getActionCommand().equals("save")){
						save();
					}
				}
			});
		main_menu.add(menu_item);
		menu_item = new JMenuItem("Шинэ нэрээр хадгалах");
		menu_item.setActionCommand("save");
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(arg0.getActionCommand().equals("save")){
						saveAs();
					}
				}
			});
		main_menu.add(menu_item);
	      
		menu_item = new JMenuItem("Шалгах");
		menu_item.setMnemonic(KeyEvent.VK_F9);
		menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,
								ActionEvent.CTRL_MASK));
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					checkAndRun();
				}
			});
		main_menu.add(menu_item);

		  
		menu_item = new JMenuItem("Copy");
		menu_item.setMnemonic(KeyEvent.VK_C);
		menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
								ActionEvent.CTRL_MASK));
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					copy();
				}
			});
		main_menu.add(menu_item);
		
		menu_item = new JMenuItem("Paste");
		menu_item.setMnemonic(KeyEvent.VK_V);
		menu_item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
								ActionEvent.CTRL_MASK));
		menu_item.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					paste();
				}
			});
		main_menu.add(menu_item);
		
		menu_item = new JMenuItem("Тухай");
		menu_item.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent arg0) {
					pnlAbout pnl = new pnlAbout();
					pnl.setVisible(true);
				}
			});
		main_menu.add(menu_item);
		
		return top_menu_bar;
	}
	private void saveAs(){
		int returnVal = fileChooser.showSaveDialog(mainPanel);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			currentFile = fileChooser.getSelectedFile();
			mainPanel.savePanel(currentFile);
			repaint();
			setTitle(TITLE + " - " + currentFile.getAbsolutePath());
			FileEdited=' ';
				
		} 
	}
	private void save(){
		if(currentFile==null)
			saveAs();
		else
			mainPanel.savePanel(currentFile);
		FileEdited=' ';
	}
	private void open(){
		if(checkFileSaved()==1||FileEdited==' ')
		{
			int returnVal = fileChooser.showOpenDialog(mainPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				currentFile = fileChooser.getSelectedFile();
				mainPanel.openFile(currentFile);
				repaint();
				FileEdited=' ';
				setTitle(TITLE + " - " + currentFile.getAbsolutePath());
			}
		}
	}
	private void newp(){
		if(checkFileSaved()==1 || FileEdited==' ')
		{
			mainPanel.newPanel();
			currentFile=null;
			setTitle(TITLE);
		}
	}
	 
	public static void setEdited() {
		FileEdited='*';
	}
	
	private int checkFileSaved(){
		if(FileEdited != ' '){
			String msg="Баримтыг хадгалах уу?";
			if(currentFile!=null)
				msg=currentFile.getName()+ "-д өөрчлөлт орсон байна. өөрчлөлтийг хадгалах уу?";
			int result=JOptionPane.showConfirmDialog(this, msg,"Анхааруулга", JOptionPane.YES_NO_OPTION);
			if(result==JOptionPane.YES_OPTION){
				save();
				return 1;
			}else if(result==JOptionPane.NO_OPTION)
				return 1;
		}
		return 0;
	}
	private void checkAndRun(){
		ErrorCheck checker = new ErrorCheck();
		
		if (checker.checkForErrors(mainPanel.getStartBlock()) == false) {
			CodeGenerator g = new CodeGenerator();
			if (g.generateCode(mainPanel.getStartBlock())) {
				
				setEditable(false);
				result.removeAll();
				pnlTrace pnl=new pnlTrace(mainPanel,g.getOutput());
				result.add(pnl,BorderLayout.SOUTH);
				revalidate();
				repaint();
			} else {
				showError(g);
			}
		} else {
			setEditable(false);
			Log.log("checker error");
			result.removeAll();
			pnlError pnl = new pnlError(mainPanel, checker.getErrors());
			result.add(pnl, BorderLayout.SOUTH);
			revalidate();
			repaint();
		}
	}
	private void copy(){
		TempCopy=new ArrayList<DrawableBlock>();
		List<DrawableBlock> blocks=testPanel.getSelectedBlocks();
		if(blocks.size()>0){
			DrawableBlock temp=null;
			for (DrawableBlock block : blocks) {
				
				switch (block.TYPE) {
				case END:
					temp=new EndBlock(block);
					TempCopy.add(temp);
					break;
				case IF:
					temp=new IfBlock((IfBlock)block);
					TempCopy.add(temp);
					break;
				case INIT:
					temp=new InitBlock(block);
					TempCopy.add(temp);
					break;
				case INPUT:
					temp=new InputBlock(block);
					TempCopy.add(temp);
					break;
				case VALUE:
					temp=new ValueBlock(block);
					TempCopy.add(temp);
					break;
				case OUTPUT:
					temp=new OutputBlock(block);
					TempCopy.add(temp);
					break;
				default:
					break;			
				}			
			}
			pastCount=0;
		}
		else
			JOptionPane.showMessageDialog(this, "Та ямар нэгэн блок сонгоогүй байна","Мэдээлэл",JOptionPane.OK_OPTION);
		
	}
	private void paste(){
		pastCount++;
		DrawableBlock temp=null;
		if(TempCopy!=null&&TempCopy.size()>0){
			for (DrawableBlock block : TempCopy) {
				Point p=block.getLocation();
				p.x=p.x+20*pastCount;
				p.y=p.y+40*pastCount;
				block.setLocation(p);
				mainPanel.addBlock(block);				
			}
			mainPanel.setSelectedBlocks(TempCopy);
			
		}
	}

}
