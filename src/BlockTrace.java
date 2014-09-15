import java.util.HashMap;

public class BlockTrace {

	private DrawableBlock v;
	private HashMap<String, String> vars;

	public BlockTrace() {
		vars = new HashMap<String, String>();
	}

	public void setBlock(DrawableBlock block) {
		v = block;
	}

	public DrawableBlock getBlock() { return v; }

	public void addVariable(String k, String v) {
		vars.put(k, v);
	}

	public HashMap<String, String> getVariables() {
		return vars;
	}
};	
