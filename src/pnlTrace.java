import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class pnlTrace extends JPanel implements ChangeListener{
	private JSlider sldrSteps;
	private JList lMemoryDump;
	private DefaultListModel<String> mdlTraces;
	private JTextArea taStdOutput;
	private testPanel blockPanel;
	private List<BlockTrace> lTraces;
	private JButton btnFirst, btnPrev, btnNext, btnLast;
	private JLabel lblSteps;
	public pnlTrace(testPanel block, List<BlockTrace> traces) {
		blockPanel=block;
		lTraces=traces;
		if(lTraces!=null)
			init();
		else
			JOptionPane.showConfirmDialog(this, "алдаа");
	}
	private void init(){
		sldrSteps=new JSlider(JSlider.HORIZONTAL,1,lTraces.size(),1);
		lMemoryDump=new JList<>();
		mdlTraces=new DefaultListModel();
		lMemoryDump.setModel(mdlTraces);
		taStdOutput=new JTextArea("dfgsdg");	
		taStdOutput.setForeground(Color.blue);
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
		pnlCont.setPreferredSize(new Dimension(1500, 150));
		pnlCont.setMaximumSize(new Dimension(1500, 250));
		pnl1.setBackground(Color.white);
		pnl2.setBackground(Color.white);
				
		sldrSteps.setMajorTickSpacing(sldrSteps.getMaximum()/5);
		sldrSteps.setMinorTickSpacing(1);
		sldrSteps.setPaintTicks(true);
		sldrSteps.setPaintLabels(true);	
		sldrSteps.addChangeListener(this);
		Hashtable<Integer, JLabel> labelTable = new Hashtable();
		labelTable.put( new Integer( 1 ), new JLabel("First") );
		labelTable.put( new Integer( sldrSteps.getMaximum()), new JLabel("Last") );
		sldrSteps.setLabelTable( labelTable );
		sldrSteps.setValue(1);
		JPanel temp=new JPanel(new BorderLayout(100,100));
		JPanel sld=new JPanel(new BorderLayout());
		sld.add(sldrSteps,BorderLayout.NORTH);
		JPanel pButtons=new JPanel(new GridLayout(1, 5));
		btnFirst=new JButton("Эхлэл");
		btnPrev=new JButton("Өмнөх");
		btnNext=new JButton("Дараах");
		btnLast=new JButton("Төгсгөл");
		lblSteps=new JLabel("");
		JPanel tt=new JPanel(new FlowLayout());
		pButtons.add(btnFirst);
		pButtons.add(btnPrev);
		lblSteps.setFont(new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, 20));
		tt.add(lblSteps);
		pButtons.add(tt);
		pButtons.add(btnNext);
		pButtons.add(btnLast);
		
		tt=new JPanel(new FlowLayout());
		tt.add(pButtons);
		sld.add(tt,BorderLayout.SOUTH);
		sldrSteps.setValue(1);
		stateChanged(null);
		temp.add(sld,BorderLayout.CENTER);
		temp.add(new JLabel("Алхамууд: "),BorderLayout.LINE_START);
		JButton btnClose=new JButton("Гарах");
		temp.add(btnClose,BorderLayout.LINE_END);
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				blockPanel.getparent().setEditable(true);
				
			}
		});
		
		this.add(pnlCont,BorderLayout.SOUTH);
		this.add(temp,BorderLayout.CENTER);
		btnFirst.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sldrSteps.setValue(1);
				stateChanged(null);
				
			}
		});
		btnPrev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sldrSteps.setValue(sldrSteps.getValue()-1);
				stateChanged(null);
				
			}
		});
		btnNext.addActionListener(new ActionListener() {
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sldrSteps.setValue(sldrSteps.getValue()+1);
				stateChanged(null);
				
			}
		});
		btnLast.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sldrSteps.setValue(sldrSteps.getMaximum());
				stateChanged(null);
				
			}
		});

	}

	@Override
	public void stateChanged(ChangeEvent e) {

	        int fps = (int)sldrSteps.getValue();
	        BlockTrace trace=lTraces.get(fps-1);
	        blockPanel.setRunTypeToBlock(trace.getBlock());
	        setMemoryValue(trace.getVariables());
	       
	        if(fps==1){
	        	btnFirst.setEnabled(false);
	        	btnPrev.setEnabled(false);
	        	btnLast.setEnabled(true);
	        	btnNext.setEnabled(true);
	        }else if(fps==lTraces.size()){
	        	btnLast.setEnabled(false);
	        	btnNext.setEnabled(false);
	        	btnFirst.setEnabled(true);
	        	btnPrev.setEnabled(true);
	        }else{
	        	btnFirst.setEnabled(true);
	        	btnPrev.setEnabled(true);
	        	btnLast.setEnabled(true);
	        	btnNext.setEnabled(true);
	        }
	        lblSteps.setText(fps+"/"+lTraces.size());
	}
	
	private void setMemoryValue(HashMap<String, String> vars){
		mdlTraces.clear();
		taStdOutput.setText("");
		for (Object k : vars.keySet())
			if(k.toString().equals("__user_stdout__"))
				taStdOutput.setText(vars.get(k).toString());
			else
				mdlTraces.addElement(k.toString() + ": " + vars.get(k).toString());
	}

}
