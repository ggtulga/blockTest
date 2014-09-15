import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.activation.MailcapCommandMap;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class pnlError extends JPanel{
	private testPanel blockPanel;
	private List<ErrorMessage> errors;
	private JList<String> lError;
	private DefaultListModel<String> mdlError;
	public pnlError(testPanel panel,List<ErrorMessage>  error) {
		// TODO Auto-generated constructor stub
		blockPanel=panel;
		errors=error;
		mdlError=new DefaultListModel<>();
		for (ErrorMessage errorCheck : error) {
			mdlError.addElement(errorCheck.getMsg());
		}
		lError=new JList<>(mdlError);
		lError.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				JList<String> source=(JList<String>)arg0.getSource();
				int selectedIndex=source.getSelectedIndex();
				blockPanel.setErrorTypeToBlock(errors.get(selectedIndex).getBlock());							
			}
		});
		
		JPanel temp=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnClose=new JButton("Гарах");
		temp.add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				blockPanel.getparent().setEditable(true);
			}
		});
		this.setLayout(new BorderLayout());
		this.add(temp,BorderLayout.LINE_START);
		this.add(new JScrollPane(lError),BorderLayout.CENTER);
	}
}
