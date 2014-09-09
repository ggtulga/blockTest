import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class test extends JFrame {

	private static final long serialVersionUID = 1L;
	private File currentFile=null;
	private final static String TITEL="Flow chart runner";
	private static char FileEdited=' ';
	private JFileChooser fileChooser;
	testPanel mainPanel;
	JList<String> inputList;
	Map<String,String> inputs;
	JPanel inputPanel;
	DefaultListModel<String> inputModel;
	JTextArea taResult;
	String currentVariable="";
	JPanel jpMenuBar;
	ToolBar t;
	private JPanel temp=new JPanel(new GridLayout());
	@SuppressWarnings("deprecation")
	public test() {
		setTitle(TITEL);
		setLayout(new BorderLayout());
		JPanel holderPanel = new JPanel(new BorderLayout());
		holderPanel.add(temp, BorderLayout.NORTH);
		setLayout(new BorderLayout());
	      //Size(1000,1000);
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), " ");
		title.setTitlePosition(TitledBorder.CENTER);
		setJMenuBar(createMenuBar());
		
		
		
		
		temp.setBorder(title);
		
		
		mainPanel = new testPanel();
		mainPanel.newPanel();
		mainPanel.setPreferredSize(new Dimension(1000, 1000));
		temp.add(mainPanel);		
		t = new ToolBar();
		setLayout(new BorderLayout());			
		add(t, BorderLayout.LINE_START);
		
		getContentPane().add(new JScrollPane(holderPanel), BorderLayout.CENTER);
	
		//add(temp, BorderLayout.CENTER);	
		
		
		fileChooser=new JFileChooser();
		 FileNameExtensionFilter blockfilter = new FileNameExtensionFilter("block files (*.block)", "block");
		 fileChooser.setFileFilter(blockfilter);
		jpMenuBar=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnNew=new JButton("New");
		btnNew.setFocusable(false);
		btnNew.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {	

				newp();
				
			}			
		});
		jpMenuBar.add(btnNew);
		JButton btnSave=new JButton("Save");
		btnSave.setFocusable(false);
		btnSave.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {	

				save();
			}			
		});
		jpMenuBar.add(btnSave);
		JButton btnOpen=new JButton("Open");
		btnOpen.setFocusable(false);
		btnOpen.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				open();
			}			
		});
		jpMenuBar.add(btnOpen);
		
		//////////////////debug test////////////////////////
		JButton btnrun=new JButton("run");
		btnrun.setFocusable(false);
		btnrun.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				temp.add(new JLabel("dfff"));
				System.out.println("dfns,fnsd,");
				revalidate();
				repaint();
				
				ErrorCheck checker = new ErrorCheck();
				if (checker.checkForErrors(mainPanel.getStartBlock()) == false) {
					CodeGenerator g = new CodeGenerator();
					if (g.generateCode(mainPanel.getStartBlock())) {
						
						// outArea.setText(g.getOutput());
						
					} else {
						// outArea.setText(g.getError());
					}
				} else {
					// errArea.setText(checker.getErrors());
				}
			}			
		});
		jpMenuBar.add(btnrun);
		
		inputs=new HashMap<String, String>();
		inputPanel=new JPanel(new FlowLayout());
		
		
		this.add(jpMenuBar,BorderLayout.NORTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.show();
		this.repaint();		
	}
	public static void main(String[] args) {
		new test();
	}
	public class ToolBar extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L;
		List<JComponent> tools;
		
		public ToolBar() {
			super();
			setLayout(new GridLayout());
			tools = new ArrayList<JComponent>();
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
			temp.add(new JLabel("                                     "));
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
		for (Component c : t.getTools()) {
			DrawableBlock b=(DrawableBlock)c;
			b.setEnabled(edit);
			if(edit)
			{
				b.addMouseListener(t);
			}
			else
				b.removeMouseListener(t);
		}
		for (Component c : jpMenuBar.getComponents()) {
			
			c.setEnabled(edit);
		
		}
	}
	  public JMenuBar createMenuBar() {
	      JMenuBar top_menu_bar = new JMenuBar();
	      JMenu main_menu = new JMenu("Menu");
	      main_menu.setMnemonic(KeyEvent.VK_M);
	      top_menu_bar.add(main_menu);
	      JMenuItem menu_item;

	      menu_item = new JMenuItem("New file");
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
	      menu_item = new JMenuItem("Open file");
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
	      menu_item = new JMenuItem("Save");
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
	      menu_item = new JMenuItem("Save as");
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

	      return top_menu_bar;
	   }
	  private void saveAs(){
		  int returnVal = fileChooser.showSaveDialog(mainPanel);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
				mainPanel.savePanel(currentFile);
				repaint();
				setTitle(TITEL+" - "+currentFile.getAbsolutePath());
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
			  setTitle(TITEL+" - "+currentFile.getAbsolutePath());
          }
		 }
	  }
	  private void newp(){
		  if(checkFileSaved()==1||FileEdited==' ')
			 {
			  mainPanel.newPanel();
			  currentFile=null;
			  setTitle(TITEL);
			 }
	  }
	 
	public static void setEdited() {
		// TODO Auto-generated method stub
	//	super.setEnabled(arg0);
		  FileEdited='*';
		  
	}
	
	private int checkFileSaved(){
		 if(FileEdited!=' '){
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
}
