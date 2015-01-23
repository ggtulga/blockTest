
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class dragDrop implements MouseMotionListener,MouseListener,Serializable {

	@Override
	public void mouseDragged(MouseEvent e) {
		if(testPanel.getSelectedBlocks().size()==0){	
			DrawableBlock tempNote=(DrawableBlock) e.getSource();
			Point p =e.getLocationOnScreen();
			tempNote.dragBlock(p);	
			test.setEdited();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {			
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		DrawableBlock temp=(DrawableBlock)e.getSource();
		
		
		if(DrawableBlock.getFirstBLock()==null){
			if(temp.TYPE!=BLOCKTYPE.END){
				DrawableBlock.setFirstBLock(temp);
				DrawableBlock.getFirstBLock().setColor(Color.red);
			}
		}else if(temp.TYPE==BLOCKTYPE.BEGIN||DrawableBlock.getFirstBLock().equals(temp)){
				DrawableBlock.setSelectedBlock(null, null);
				
			Log.log("green");
			DrawableBlock.getFirstBLock().setColor(DrawableBlock.getFirstBLock().getBeforeColor());
			DrawableBlock.setFirstBLock(null);
		}else{
			if(DrawableBlock.getFirstBLock().TYPE!=BLOCKTYPE.IF){
				if(DrawableBlock.getFirstBLock().getNext()!=null){
					if(JOptionPane.showConfirmDialog(null,"Өмнөх холбоосыг устгах уу?", "Анхаар", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
						DrawableBlock.getFirstBLock().setNext(temp);
				}else
					DrawableBlock.getFirstBLock().setNext(temp);
				temp.repaint();
				DrawableBlock.getFirstBLock().setColor(Color.black);
				DrawableBlock.setFirstBLock(null);
			}else{
				IfBlock block=(IfBlock)DrawableBlock.getFirstBLock();
				int t= JOptionPane.showConfirmDialog(null,"Хэрвээ үнэн гаралттай холбох бол Yes дарна уу!!!", "Үнэн гаралт эсэх", JOptionPane.YES_NO_OPTION);
				if(t==JOptionPane.YES_OPTION)						
					if(block.getNextTrue()!=null){
						if(JOptionPane.showConfirmDialog(null,"Өмнөх холбоосыг устгах уу?", "Анхаар", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
							block.setNextTrue(temp);
					}else
						block.setNextTrue(temp);
				else if(t==JOptionPane.NO_OPTION)
					if(block.getNextFalse()!=null){
						if(JOptionPane.showConfirmDialog(null,"Өмнөх холбоосыг устгах уу?", "Анхаар", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
							block.setNextFalse(temp);
					}else
						block.setNextFalse(temp);
				temp.setColor(Color.black);
				temp.repaint();
				DrawableBlock.getFirstBLock().setColor(Color.black);
				DrawableBlock.setFirstBLock(null);
				test.setEdited();
			}
		}			
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		DrawableBlock block=(DrawableBlock)e.getSource();
		if(!(DrawableBlock.getFirstBLock()!=null&&DrawableBlock.getFirstBLock().equals(block)))
			if(!(DrawableBlock.getSelectedBlock()!=null&&DrawableBlock.getSelectedBlock().equals(block)))
				block.setColor(Color.blue);
		if(DrawableBlock.getFirstBLock()!=null)
		{
			block.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {

		DrawableBlock block=(DrawableBlock)e.getSource();
		if(!(DrawableBlock.getFirstBLock()!=null&&DrawableBlock.getFirstBLock().equals(block)))
			if(!(DrawableBlock.getSelectedBlock()!=null&&DrawableBlock.getSelectedBlock().equals(block)))
				block.setColor(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		DrawableBlock tempNote=(DrawableBlock) e.getSource();
		tempNote.setTemp(null);
		tempNote.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		DrawableBlock tempNote=(DrawableBlock) e.getSource();
		tempNote.setTemp(null);
		tempNote.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}		
}
