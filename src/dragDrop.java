
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class dragDrop implements MouseMotionListener,MouseListener,Serializable {

	@Override
	public void mouseDragged(MouseEvent e) {
			
		DrawableBlock tempNote=(DrawableBlock) e.getSource();
		Point p =e.getLocationOnScreen();
		tempNote.dragBlock(p);	
		test.setEdited();
	}
	@Override
	public void mouseMoved(MouseEvent e) {			
	}

	@Override
	public void mouseClicked(MouseEvent e) {		
		DrawableBlock temp=(DrawableBlock)e.getSource();
		DrawableBlock.setCurrentBlock(null, null);
		DrawableBlock.setSelectedBlock(temp, Color.orange);
		if(DrawableBlock.firstBLock==null){
			if(temp.TYPE!=BLOCKTYPE.END){
				DrawableBlock.firstBLock=temp;
				DrawableBlock.firstBLock.setColor(Color.red);
			}
		}else if(temp.TYPE==BLOCKTYPE.BEGIN||DrawableBlock.firstBLock.equals(temp)){
			if(DrawableBlock.firstBLock.equals(temp)&&!temp.TYPE.equals(BLOCKTYPE.BEGIN)&&!temp.TYPE.equals(BLOCKTYPE.END))
				//DrawableBlock.CurrentNote=temp;
				DrawableBlock.setSelectedBlock(null, null);
			DrawableBlock.setCurrentBlock(temp, Color.green);
			System.out.println("greeen");
			DrawableBlock.firstBLock.setColor(DrawableBlock.firstBLock.getBeforeColor());
			DrawableBlock.firstBLock=null;
		}else{
			if(DrawableBlock.firstBLock.TYPE!=BLOCKTYPE.IF){
				if(DrawableBlock.firstBLock.getNext()!=null){
					if(JOptionPane.showConfirmDialog(null,"Өмнөх холбоосыг устгах уу?", "Анхаар", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
						DrawableBlock.firstBLock.setNext(temp);
				}else
					DrawableBlock.firstBLock.setNext(temp);
				temp.repaint();
				DrawableBlock.firstBLock.setColor(Color.black);
				DrawableBlock.firstBLock=null;
			}else{
				IfBlock block=(IfBlock)DrawableBlock.firstBLock;
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
				DrawableBlock.firstBLock.setColor(Color.black);
				DrawableBlock.firstBLock=null;
				test.setEdited();
			}
		}			
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		DrawableBlock block=(DrawableBlock)e.getSource();
		if(!(DrawableBlock.firstBLock!=null&&DrawableBlock.firstBLock.equals(block)))
			if(!(DrawableBlock.CurrentNote!=null&&DrawableBlock.CurrentNote.equals(block)))
				if(!(DrawableBlock.SelectedBlock!=null&&DrawableBlock.SelectedBlock.equals(block)))
					block.setColor(Color.blue);
	}

	@Override
	public void mouseExited(MouseEvent e) {

		DrawableBlock block=(DrawableBlock)e.getSource();
		if(!(DrawableBlock.firstBLock!=null&&DrawableBlock.firstBLock.equals(block)))
			if(!(DrawableBlock.CurrentNote!=null&&DrawableBlock.CurrentNote.equals(block)))
				if(!(DrawableBlock.SelectedBlock!=null&&DrawableBlock.SelectedBlock.equals(block)))
					block.setColor(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		DrawableBlock tempNote=(DrawableBlock) e.getSource();
		tempNote.setTemp(null);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		DrawableBlock tempNote=(DrawableBlock) e.getSource();
		tempNote.setTemp(null);
	}		
}
