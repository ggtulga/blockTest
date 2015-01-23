import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;



public  class MyDispacher implements KeyEventDispatcher {
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

    	if(e.getKeyCode()==KeyEvent.VK_DELETE&&DrawableBlock.getSelectedBlock()!=null){
    		testPanel c=(testPanel)DrawableBlock.getSelectedBlock().getParent();
    		DrawableBlock.setFirstBLock(null); 		    		
    		DrawableBlock temp=DrawableBlock.getSelectedBlock();
    		temp.removeThis();
    		
    	}
    	if(e.getKeyCode()==KeyEvent.VK_DELETE&&testPanel.getSelectedBlocks().size()>0){
    		for (DrawableBlock b : testPanel.getSelectedBlocks()) {
				b.removeThis();
			}    		    		
    	}
    	
        return false;
    }
}
