
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.io.OutputStream;
import java.io.DataOutputStream;

import org.python.core.PyList;
import org.python.core.PyDictionary;
import org.python.core.PyObject;
import org.python.core.PyTuple;

/**
 * class to generate python script from the connected components
 * of DrawableBlock
 * @author ggt
 *
 */

public class CodeGenerator {
	/**
	 *  class to represent one line of code
	 * @author ggt
	 *
	 */
	private class LineCode {
		public String code;
		/**
		 * tabs before code which is very important for python
		 */
		public int tab;
		public DrawableBlock v;

		public LineCode() { code = ""; tab = 0; v = null; }
		public LineCode(LineCode l) {
			this.v = l.v;
			this.code = l.code;
			this.tab = l.tab;
		}
	}
	
	ArrayList <LineCode> script;
	JythonFactory jf;
	/**
	 * stores errors encountered to process blocks.
	 */
	ArrayList<ErrorMessage> errs;

	// Each element of which describes a trace;
	ArrayList<BlockTrace> traces;
	// keep the number
	int bNumber;
	// function to generate block names
	private String generateBlockName() {
		bNumber++;
		return "__block_" + bNumber + "__";
	}

	HashMap<DrawableBlock, String> block2name;

	String currentName;
	int startLine;

	/*
	  This function skips over PointBlock. Because it's not used in the code
	  generation
	 */
	private DrawableBlock nextBlock(DrawableBlock v) {
		while (v != null && v.TYPE == BLOCKTYPE.POINT)
			v = v.getNext();

		return v;
	}
	
	private void processBlock(DrawableBlock v, String globals) {

		if (v == null)
			return;
		
		block2name.put(v, currentName);
		
		int i;
		DrawableBlock next;
		
		// declare function name
		LineCode line = new LineCode();
		line.tab = 0;
		line.code = "def " + currentName + "():";
		line.v = v;
		script.add(new LineCode(line));

		line.tab = 1;

		// declare globals to be used in the function
		if (globals.equals("global") == false) {
			line.code = new String(globals);
			script.add(new LineCode(line));
		}

		String cont = v.getText().trim();
		
		switch (v.TYPE) {
		case BEGIN:
			break;
		case IF:
			IfBlock b = (IfBlock) v;

			line.code = "if " + cont + ":";
			script.add(new LineCode(line));

			boolean callTrue = true;
			boolean callFalse = true;
			String trueName = "", falseName = "";
			// if the statement is true
			next = nextBlock(b.getNextTrue());
			line.tab = 2;
			if (block2name.containsKey(next)) {// check if we have visited there before
				line.code = block2name.get(next) + "()";
				script.add(new LineCode(line));
				callTrue = false;
			} else {
				trueName = generateBlockName();
				line.code = trueName + "()";
				script.add(new LineCode(line));
			}
			// else
			line.tab = 1;
			line.code = "else:";
			script.add(new LineCode(line));
			
			// if the statement is false
			next = nextBlock(b.getNextFalse());
			line.tab = 2;
			if (block2name.containsKey(next)) {// check if we have visited there before
				line.code = block2name.get(next) + "()";
				script.add(new LineCode(line));
				callFalse = false;
			} else {
				falseName = generateBlockName();
				line.code = falseName + "()";
				script.add(new LineCode(line));
			}
			line.tab = 1;

			if (callTrue) {
				currentName = trueName;
				processBlock(nextBlock(b.getNextTrue()), globals);
			}

			if (callFalse) {
				currentName = falseName;
				processBlock(nextBlock(b.getNextFalse()), globals);
			}
			
			break;
		case INIT:
			// Let's do a nice thing by separating declarations by commas
			int s = 0, f = 0, x, y;
			String tmp = "";
			HashMap<String, String> vars = new HashMap<String, String>();
			while (true) {
				f = cont.indexOf(',', f);
				if (f == -1)
					f = cont.length();

				tmp = cont.substring(s, f).trim();
				
				x = tmp.indexOf('[');
				if (x != -1) {
					// it's an array
					ArrayList<String> sizes = new ArrayList<String>();
					String arrayName = tmp.substring(0, x), val;
					// Array can be multiple dimension, therefore needs to be written as follows
					// eg. [[None for i in range(3)] for i in range(4)] for 3x4 size array
					while (x != -1) {
						y = tmp.indexOf(']', x + 1);
						sizes.add(tmp.substring(x + 1, y));
						// find the size of the next dimension
						x = tmp.indexOf('[', x + 1);
					}

					val = "None";
					for (i = sizes.size() - 1; i >= 0; i--)
						val = '[' + val + " for __init_array__ in range(" + sizes.get(i) + ")]";
						
					vars.put(arrayName, val);
				} else {
					vars.put(tmp, "None");
				}

				if (f == cont.length())
					break; // this is the end of this list of declarations;

				f++;
				s = f; // new start
			}

			String rvalue = "", lvalue = "";
			Iterator it = vars.entrySet().iterator();
			Map.Entry<String, String> pair;
			while (true) {
				pair = (Map.Entry<String, String>) it.next();
				lvalue += pair.getKey();
				rvalue += pair.getValue();
				if (it.hasNext()) {
					lvalue += ",";
					rvalue += ",";
				} else
					break;
			}

			// declare variables as globals
			line.code = "global " + lvalue;
			script.add(new LineCode(line));
			
			line.code = lvalue + "=" + rvalue;
			script.add(new LineCode(line));

			if (globals.equals("global"))
				globals += " " + lvalue;
			else
				globals += "," + lvalue;

			break;
		case VALUE:
			line.code = cont;
			script.add(new LineCode(line));
			
			break;
		case INPUT:
		case OUTPUT:
			String[] var = cont.split(",");
			String substr;

			for (i = 0; i < var.length; i++) {

				if (v.TYPE == BLOCKTYPE.INPUT)
					line.code = var[i] + "=int(JOptionPane.showInputDialog('" + var[i] + "'))";
				else
					line.code = "print " + var[i] + ",";

				script.add(new LineCode(line));
			}

			break;
		case END:
			line.code = "sys.exit(0)";
			script.add(new LineCode(line));
			break;
		}

		next = nextBlock(v.getNext());
		
		if (v.TYPE != BLOCKTYPE.IF && v.TYPE != BLOCKTYPE.END) {
			if (block2name.containsKey(next)) {
				line.code = block2name.get(next) + "()";
				script.add(new LineCode(line));
			} else {
				currentName = generateBlockName();
				line.code = currentName + "()";
				script.add(new LineCode(line));
				processBlock(next, globals);
			}
		}

	}
		
	/**
	 * initializes locals to generate new script
	 */
	private void init() {
		bNumber = 0;
		script.clear();
		errs.clear();
		traces.clear();
		block2name.clear();
	}

	private boolean runPythonScript(String s) {
		String jarPath = JythonFactory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		LoggerType logger = (LoggerType) jf.getJythonObject(
			"LoggerType", "logger.py");

		PyList trace = new PyList();

		trace = logger.run_script(s);

		Log.log(trace);
		int lineNumber;
		boolean isEnded, isStarted;
		isEnded = isStarted = false;
		DrawableBlock v, pre = null;

		for (Iterator<PyList> i = trace.iterator(); i.hasNext(); ) {
			PyList list = (PyList) i.next();
			lineNumber = Integer.parseInt(list.get(0).toString());
			lineNumber--;
			// Need to substract 2:	
			// - array starts from 0
			// - two for the imports
			if (lineNumber >= script.size() || lineNumber < 0) {
				Log.log("This should not happen. Out of index\n");
				continue;
			}

			if (lineNumber == startLine)
				isStarted = true;

			if (isStarted == false || isEnded == true)
				continue;

			v = script.get(lineNumber).v;
			
			if (v != null) {
				BlockTrace t = new BlockTrace();
				t.setBlock(v);
			
				PyDictionary dict = (PyDictionary) list.get(1);
				PyTuple var;

				for (PyObject item : dict.iteritems().asIterable()) {
					var = (PyTuple) item;
					Log.log(var.get(0));
					if (var.get(1) == null)
						t.addVariable(var.get(0).toString(), "None");
					else if (var.get(0).equals("__error_max_line__")) {
						t.addVariable("Алдаа: ", "Хамгийн их хийж болох үйлдлийн хязгаар хүрлээ. Боломжит төгсгөлгүй давталтаас сэргийлэхийн тулд энд зогсож байна.");
						isEnded = true;
					} else if (var.get(0).equals("__error__")) {
						isEnded = true;
						t.addVariable("Алдаа: ", var.get(1).toString());
					} else
						t.addVariable(var.get(0).toString(), var.get(1).toString());
				}

				if (pre != v) {
					if (v.TYPE == BLOCKTYPE.END)
						isEnded = true;
					pre = v;
					traces.add(t);
				} else if (isEnded)
					traces.add(t);
			}
		}

		if (Log.ENABLELOGGING) {
			for (int i = 0; i < traces.size(); i++) {
				Log.log(i + ":");
				switch (traces.get(i).getBlock().TYPE) {
				case BEGIN:
					Log.log("begin");
					break;
				case IF:
					Log.log("if");
					break;
				case INIT:
					Log.log("init");
					break;
				case VALUE:
					Log.log("value");
					break;
				case INPUT:
					Log.log("input");
					break;
				case OUTPUT:
					Log.log("OUTPUT");
					break;
				case END:
					Log.log("end");
					break;
				}
				for (Object k : traces.get(i).getVariables().keySet())
					Log.log(k.toString() + ": " + traces.get(i).getVariables().get(k).toString());
			}
		}


		return true;
	}



	public CodeGenerator() {
		script = new ArrayList<LineCode>();
		errs = new ArrayList<ErrorMessage>();
		traces = new ArrayList<BlockTrace>();
		block2name = new HashMap<DrawableBlock, String>();
	}

	/**
	 * 
	 * @param start DrawableBlock which represents the start node
	 */
	public boolean generateCode(DrawableBlock start) {
		init();
		
		LineCode line = new LineCode();
		String startFunction = currentName = generateBlockName();

		String s = "";
		
		line.code = "import sys";
		script.add(new LineCode(line));
		line.code = "from javax.swing import JOptionPane";
		script.add(new LineCode(line));
		
		processBlock(start, "global");

		startLine = script.size();
		line.code = startFunction + "()";
		script.add(new LineCode(line));

		for (LineCode codeLine : script) {
			for (int i = 0; i < codeLine.tab; i++)
				s += "\t";

			s += codeLine.code + "\n"; 
		}

		Log.log(s);
		return runPythonScript(s);
	}

	public ArrayList<BlockTrace> getOutput() {
		return traces;
	}

	public ArrayList<ErrorMessage> getErrors() {
		return errs;
	}
}
