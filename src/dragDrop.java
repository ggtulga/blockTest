import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;


import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

public class dragDrop extends MouseInputAdapter {

	@Override
	public void mouseDragged(MouseEvent e) {

		DrawableBlock block = (DrawableBlock) e.getSource();
		Point p = e.getLocationOnScreen();
		block.dragBlock(p);
	}
	@Override
	public void mouseMoved(MouseEvent e) {			
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		DrawableBlock block = (DrawableBlock) e.getSource();
		if (DrawableBlock.selectedBlock == null){
			if (block.TYPE != BLOCKTYPE.END){
				DrawableBlock.selectedBlock = block;
				DrawableBlock.selectedBlock.setColor(Color.red);
			}
		}else if (DrawableBlock.selectedBlock.equals(block) ||
				block.TYPE == BLOCKTYPE.BEGIN) {
			DrawableBlock.selectedBlock.setColor(Color.black);
			DrawableBlock.selectedBlock = null;
		}else {
			if (DrawableBlock.selectedBlock.TYPE != BLOCKTYPE.IF) {
				if (DrawableBlock.selectedBlock.getNext() != null){
					if (JOptionPane.showConfirmDialog(null,
							"Өмнөх холбоосыг устгах уу?",
							"Анхаар",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						DrawableBlock.selectedBlock.setNext(block);
				} else
					DrawableBlock.selectedBlock.setNext(block);

				block.repaint();
				DrawableBlock.selectedBlock.setColor(Color.black);
				DrawableBlock.selectedBlock = null;
			} else {
				IfBlock ifBlock = (IfBlock) DrawableBlock.selectedBlock;
				int t = JOptionPane.showConfirmDialog(null,
						"Хэрвээ үнэн гаралттай холбох бол Yes дарна уу!!!",
						"Үнэн гаралт эсэх",
						JOptionPane.YES_NO_OPTION);
				if (t == JOptionPane.YES_OPTION) {
					if (ifBlock.getNextTrue() != null) {
						if (JOptionPane.showConfirmDialog(null,
								"Өмнөх холбоосыг устгах уу?",
								"Анхаар",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
							ifBlock.setNextTrue(block);
					} else
						ifBlock.setNextTrue(block);

				} else if (t == JOptionPane.NO_OPTION) {
					if (ifBlock.getNextFalse() != null) {
						if (JOptionPane.showConfirmDialog(null,
								"Өмнөх холбоосыг устгах уу?",
								"Анхаар",
								JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
							ifBlock.setNextFalse(block);
					} else
						ifBlock.setNextFalse(block);
				}

				block.setColor(Color.black);
				block.repaint();
				DrawableBlock.selectedBlock.setColor(Color.black);
				DrawableBlock.selectedBlock = null;
			}
		}			
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		DrawableBlock block = (DrawableBlock) e.getSource();
		if (!(DrawableBlock.selectedBlock != null &&
				DrawableBlock.selectedBlock.equals(block))) {
			block.setColor(Color.blue);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {

		DrawableBlock block = (DrawableBlock) e.getSource();
		if (!(DrawableBlock.selectedBlock !=null &&
				DrawableBlock.selectedBlock.equals(block)))
			block.setColor(block.getPreColor());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		DrawableBlock block = (DrawableBlock) e.getSource();
		block.setState(1);
		block.setTempLocation(e.getLocationOnScreen());			
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		DrawableBlock block = (DrawableBlock) e.getSource();
		block.setState(0);
	}		
}
