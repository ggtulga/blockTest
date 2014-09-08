
public class ErrorMessage {

    String msg;
    DrawableBlock v;

    public ErrorMessage() {
	msg = "";
	v = null;
    }

    public void setBlock(DrawableBlock block) { v = block; }
    public DrawableBlock getBlock() { return v; }

    public void setMsg(String m) { msg = m; }
    public String getMsg() { return msg; }
};
