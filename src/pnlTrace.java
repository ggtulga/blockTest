import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.ScrollPane;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;


public class pnlTrace extends JPanel{
	private JSlider sldrSteps;
	private JList lMemoryDump;
	private ListModel<String> mdlTraces;
	private JTextArea taStdOutput;
	private testPanel blockPanel;
	
	public pnlTrace(testPanel block) {
		blockPanel=block;
		init();
	}
	private void init(){
		sldrSteps=new JSlider(JSlider.HORIZONTAL);
		lMemoryDump=new JList<>();
		mdlTraces=new DefaultListModel();
		taStdOutput=new JTextArea("dfgsdg");		
		setLayout(new BorderLayout());
		JPanel pnl1=new JPanel(new GridLayout());
		JPanel pnl2=new JPanel(new GridLayout());
		JPanel pnlCont=new JPanel(new GridLayout(1,2));
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "Хувьсагчийн утгууд:");
		title.setTitlePosition(TitledBorder.CENTER);
		pnl1.setBorder(title);
		title = BorderFactory.createTitledBorder(
				BorderFactory.createLoweredBevelBorder(), "Стандарт гаралт:");
		title.setTitlePosition(TitledBorder.CENTER);
		pnl2.setBorder(title);

		pnl1.add(new JScrollPane(lMemoryDump));
		pnl2.add(new JScrollPane(taStdOutput));
		pnlCont.add(pnl1);
		pnlCont.add(pnl2);
		pnl1.setBackground(Color.white);
		pnl2.setBackground(Color.white);
		this.add(pnlCont,BorderLayout.CENTER);
		
		
		
		
	}
	 private static void createAndShowGui() {
		   pnlTrace mainPanel = new pnlTrace(null);

	      JFrame frame = new JFrame("TestIt2");
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.getContentPane().add(mainPanel);
	      //frame.setJMenuBar(mainPanel.createMenuBar());
	      frame.pack();
	      frame.setLocationByPlatform(true);
	      frame.setVisible(true);
	   }

	   public static void main(String[] args) {
	      SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            createAndShowGui();
	         }
	      });
	   }
	   private void clearOutput(){
		   taStdOutput.setText("");
	   }
	   private void addOutput(String str){
		   taStdOutput.setText(taStdOutput.getText()+"\n"+str); 
	   }
}
