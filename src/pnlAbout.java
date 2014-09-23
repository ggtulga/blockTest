import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class pnlAbout extends JFrame {

	private void close() {
		super.setVisible(false);
		super.dispose();
	}

	public pnlAbout() {
		this.setLayout(new BorderLayout());
		JPanel temp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnClose = new JButton("За");
		JTextArea text = new JTextArea("Энэхүү програм нь GPLv3 лицензийн дагуу тараагдсан болно.\n" +
					       "Програмын зохиогчид: lhamrolom\n" +
					       "                     gantulga <ggtulga [at] gmail.com>\n" +
					       "Програмтай холбоотой алдаа, дутагдал, санал хүсэлтээ\n" +
					       "дээрх хаягаар ирүүлнэ үү.\n");
		text.setHighlighter(null);
		text.setEnabled(false);
		text.setFont(new Font("default", Font.BOLD, 14));
		temp.add(text);
		
		temp.add(btnClose);
		btnClose.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					close();
				}
			});

		this.add(temp, BorderLayout.LINE_START);

		this.setPreferredSize(new Dimension(600, 150));
		this.setSize(600, 150);
		this.setResizable(false);
	}
}
