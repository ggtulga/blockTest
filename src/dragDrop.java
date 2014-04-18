import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

public class dragDrop implements MouseMotionListener,MouseListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			
			DrawableBlock tempNote=(DrawableBlock) e.getSource();
			Point p =e.getLocationOnScreen();
			tempNote.dragBlock(p);
					
		}
		@Override
		public void mouseMoved(MouseEvent e) {			
		}

		@Override
		public void mouseClicked(MouseEvent e) {		
			DrawableBlock temp=(DrawableBlock)e.getSource();
			if(DrawableBlock.firstBLock==null){
				if(temp.TYPE!=BLOCKTYPE.END){
					DrawableBlock.firstBLock=temp;
					DrawableBlock.firstBLock.setColor(Color.red);
				}
			}else if(DrawableBlock.firstBLock.equals(temp)||temp.TYPE==BLOCKTYPE.BEGIN){
				DrawableBlock.firstBLock.setColor(Color.black);
				DrawableBlock.firstBLock=null;
			}else {
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
				}
			}			
		}

		@Override
		public void mouseEntered(MouseEvent e) {

			DrawableBlock block=(DrawableBlock)e.getSource();
			if(DrawableBlock.firstBLock!=null&&DrawableBlock.firstBLock.equals(block));
			else{
				block.setColor(Color.blue);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {

			DrawableBlock block=(DrawableBlock)e.getSource();
			if(DrawableBlock.firstBLock!=null&&DrawableBlock.firstBLock.equals(block));
			else
				block.setColor(block.getBeforeColor());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			DrawableBlock tempNote=(DrawableBlock) e.getSource();
			tempNote.setState(1);
			tempNote.setTemp(e.getLocationOnScreen());			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			DrawableBlock tempNote=(DrawableBlock) e.getSource();
			tempNote.setState(0);
		}		
	}