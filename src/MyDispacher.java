import java.awt.Color;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;



public  class MyDispacher implements KeyEventDispatcher {
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
		if (DrawableBlock.CurrentNote != null && e.getID() == KeyEvent.KEY_FIRST) {
			System.out.println("++++++");
			String tempText = DrawableBlock.CurrentNote.getText();
			switch ((int) e.getKeyChar()) {
			case KeyEvent.VK_ENTER:
				DrawableBlock.CurrentNote.setFont(new Font(Font.MONOSPACED, Font.ITALIC
						| Font.BOLD, 14));
				DrawableBlock.CurrentNote.setColor(Color.black);
				DrawableBlock.CurrentNote = null;
				break;
			case KeyEvent.VK_BACK_SPACE:
				if (tempText.length() > 0)
					tempText = tempText.substring(0, tempText.length() - 1);
				DrawableBlock.CurrentNote.setText(tempText);
				break;
			default:
				tempText += e.getKeyChar();
				DrawableBlock.CurrentNote.setText(tempText);
				break;
			}
		}
        return false;
    }
}