
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import org.python.core.PyList;
import org.python.core.PyDictionary;

/**
 * class to generate python script from the connected components
 * of DrawableBlock
 * @author ggt
 *
 */

public class CodeGenerator {
	
	/** 
	 *  class to represent nodes in a graph
	 *  
	 * @author ggt
	 *
	 */
	private class Node {
		public int loop, endIf;
		public boolean endLoop, isEndLoop;
		public DrawableBlock b;
		/**
		 * Used for finding whether "if" block breaks a loop
		 */
		public boolean isIfEnded; 
		/**
		 * start and end time of visit in dfs
		 */
		public int sTime, eTime;
		/**
		 * when generating python script, this denotes whether we have
		 * visited this node already.
		 */
		public boolean visited;
		/**
		 * parent node
		 */
		public Node parent;
		/**
		 * line number to add codes in the else statement when 
		 * this node is "if" block. 
		 */
		public int lineElse;
		
		Node(DrawableBlock b, Node p) {
			loop = endIf = 0;
			isIfEnded = isEndLoop = false;
			parent = p;
			this.b = b;
			sTime = eTime = -1;
			visited = false;
		}
	}
	
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
		public int tabs;
		
		public LineCode() { code = ""; tabs = 0; }
		public LineCode(LineCode l) { this.code = l.code; this.tabs = l.tabs; }
	}
	
	/**
	 * used for dfs
	 */
	int time;
	ArrayList <LineCode> script;
	JythonFactory jf;
	Node src;
	/**
	 * line number to add one line of code to the script list
	 */
	int lineNumber;
	
	private void findLoopBreak(Node p, Node pre) {

		while (p != null) {
			if (p.b.TYPE == BLOCKTYPE.IF && p.isEndLoop == false) {
				break;
			}
			pre = p;
			p = p.parent;
		}
		
		if (p == null) {
			// this shouldn't happen
			System.err.println("Wrong end loop detection\n");
		} else {
			// p is a "if" block to break out of the loop
			p.isEndLoop = true;
			IfBlock b = (IfBlock) p.b;
			if (b.getNextTrue() == pre.b)
				p.endLoop = false;
			else
				p.endLoop = true;
		}
	}
	
	/**
	 * Visits every node to detect loops, end loops, and end if blocks.
	 * @param g stores visited node
	 * @param v node to visit
	 * @param parent a parent node
	 */
	private void visit(Map <DrawableBlock, Node> g, DrawableBlock v, Node parent, Node innerIf) {
		if (v == null)
			return;
		
		time++;
		Node node = g.get(v);
		if (node == null) {
			// we haven't visited this block so far
			// let's insert it so that we know that we have visited this node.
			node = new Node(v, parent);
			node.sTime = time;
			g.put(v, node);
			
			// "if" block is always handled differently
			if (v.TYPE == BLOCKTYPE.IF) {
				IfBlock b = (IfBlock) v;
				
				visit(g, b.getNextTrue(), node, node);
				visit(g, b.getNextFalse(), node, node);
				
				if (node.isIfEnded && node.isEndLoop) {
					/*
					 * This "if" block is closed within the loop.
					 * Therefore, it won't break the loop. Let's find next loop breaker
					 */
					node.isEndLoop = false;
					findLoopBreak(node.parent, node);
				}
			} else
				visit(g, v.getNext(), node, innerIf);
			
			time++;
			node.eTime = time;
		} else {
			
			if (node.eTime > 0) {
				// "if" block ends here
				node.endIf++;
				innerIf.isIfEnded = true;
			} else {
				// Yes, it's a loop
				node.loop++;
				// find the loop breaker.
				findLoopBreak(parent, node);
			}
			return;
		}
	}
	
	private void generatePythonScript(Map <DrawableBlock, Node> g, DrawableBlock v, int tabs, Node innerIf) {
		if (v == null)
			return;
		
		Node node = g.get(v);
		LineCode line = new LineCode();
		line.tabs = tabs;
		int newTabs = tabs, loop = node.loop;
		
		if (node.visited)
			return;
		
		node.visited = true;
		
		// check whether this node is the starting of the loop
		while (loop > 0) {
			// make it loop
			line.tabs = newTabs;
			if (node.b.TYPE == BLOCKTYPE.IF && node.isEndLoop) 
				line.code = "while (" + node.b.getText() + "):";
			else 
				line.code = "while (True):";
			
			script.add(lineNumber, new LineCode(line));
			lineNumber++;
			newTabs++;
			loop--;
		}
		
		// check whether this node is the end of "if" block
		if (node.endIf > 0) {
			// save the current lineNumber for else statement
			innerIf.lineElse = lineNumber;
			newTabs--;
		}
		
		// tabs might have changed
		line.tabs = newTabs;
	
		if (v.TYPE == BLOCKTYPE.INIT) {
			// Let's do a nice thing by separating declarations by commas
			int s = 0, f = 0;
			String c = v.getText(), tmp = "";
			while (true) {
				f = c.indexOf(',', f);
				if (f == -1)
					f = c.length();
				
				tmp = c.substring(s, f);
				// if it's not the first declaration
				if (s != 0)
					tmp = "," + tmp;
				
				if (tmp.indexOf('=') == -1) 
					line.code += tmp + "=None";
				else
					line.code += tmp;
				
				
				if (f == c.length())
					break; // this is the end of this list of declarations;
				
				f++;
				s = f; // new start
			}
		} else 	if (v.TYPE == BLOCKTYPE.OUTPUT) {
			line.code = "print(" + v.getText() + ")";
		} else if (v.TYPE == BLOCKTYPE.IF) {
			IfBlock b = (IfBlock) node.b;
			
			if (node.isEndLoop == true) { // loop must break here.
				// check whether the loop will break when the condition is true or false
				if (node.endLoop == true) {
					
					if (node.loop == 0) {
						// It shouldn't be a start of the loop too
						// If it's a start of the loop then while is used.
						line.code = "if (" + node.b.getText() + "): break";
						script.add(lineNumber, new LineCode(line));
						lineNumber++;
					}
					
					generatePythonScript(g, b.getNextFalse(), newTabs, innerIf);
					
					// getting out of the loop
					newTabs--;
					// All the codes inside the loop are generated so we must start from
					// the end.
					lineNumber = script.size();
					generatePythonScript(g, b.getNextTrue(), newTabs, innerIf);
				} else {
					if (node.loop == 0) {
						// It shouldn't be a start of the loop too
						// If it's a start of the loop then while is used.
						line.code = "if (not (" + node.b.getText() + ")): break";
						script.add(lineNumber, new LineCode(line));
						lineNumber++;
					}
					
					generatePythonScript(g, b.getNextTrue(), newTabs, innerIf);
					
					// getting out of the loop
					newTabs--;
					lineNumber = script.size();
					// All the codes inside the loop are generated so we must start from
					// the end.
					generatePythonScript(g, b.getNextFalse(), newTabs, innerIf);
				}
			} else if (node.loop == 0) { // not a loop	but it's a "if" block	
				line.code = "if (" + v.getText() + "):";
				script.add(lineNumber, new LineCode(line));
				lineNumber++;
				
				newTabs++;
								
				// If there is no blocks when the condition is true.
				if (g.get(b.getNextTrue()).endIf > 0) {
					line.code = "pass";
					line.tabs++;
					script.add(lineNumber, new LineCode(line));
					lineNumber++;
					// this line number becomes the location to place
					// codes in the else statement
					node.lineElse = lineNumber;
					line.tabs--;
				} else 
					generatePythonScript(g, b.getNextTrue(), newTabs, node);
				
				
				// there should be no need to restore lineNumber
				// Because, in any way whether the recursion is returning or
				// we're going forward, lineNumber is correct.
				lineNumber = node.lineElse;
				line.code = "else:";
				script.add(lineNumber, new LineCode(line));
				lineNumber++;
					
				if (g.get(b.getNextFalse()).endIf > 0) {
					// If there is no blocks when the condition is false.
					line.tabs++;
					line.code = "pass";
					script.add(lineNumber, new LineCode(line));
					lineNumber++;
				} else 					
					generatePythonScript(g, b.getNextFalse(), newTabs, innerIf);
				
				if (innerIf != null) {
					innerIf.lineElse = lineNumber; 
				}
			}
			
     		// BLOCKTYPE.IF is complicated 
			// so it's self contained.
			// and it returns here.
			return;

		} else if (v.TYPE == BLOCKTYPE.END) {
			line.code = "sys.exit(0)";
			script.add(lineNumber, new LineCode(line));
			lineNumber++;
			return; // there's no need to go further
		} else if (v.TYPE == BLOCKTYPE.VALUE)
			line.code = v.getText();
		else if (v.TYPE == BLOCKTYPE.INPUT) {
			int s = 0, f;
			String tmp = v.getText(), substr;
			while (true) {
				f = tmp.indexOf(',', s);
				if (f == -1)
					f = tmp.length();
				
				substr = tmp.substring(s, f).trim();
				line.code = substr + " = JOptionPane.showInputDialog('" + substr + "')";
				script.add(lineNumber, new LineCode(line));
				lineNumber++;
				
				System.out.println(substr);
				if (f == tmp.length())
					break;
				
				s = f + 1;
			}
			
			generatePythonScript(g, v.getNext(), newTabs, innerIf);
			return;
			
		} else if (v.TYPE == BLOCKTYPE.BEGIN)
			line.code = "pass";
		
		script.add(lineNumber, new LineCode(line));
		lineNumber++;
		generatePythonScript(g, v.getNext(), newTabs, innerIf);
	}
	
	/**
	 * initializes locals to generate new script
	 */
	private void init() {
		time = 0;
		lineNumber = 0;
		script.clear();
	}
	
	private void runPythonScript(String s) {
		LoggerType logger = (LoggerType) jf.getJythonObject(
				"LoggerType", "logger.py");
		PyList trace = logger.run_script(s); 
		System.out.println(trace);
		for (Iterator<PyList> i = trace.iterator(); i.hasNext(); ) {
			System.out.println(((PyDictionary) i.next().get(1)).get("__user_stdout__"));
		}
	}
	
	public CodeGenerator() {
		script = new ArrayList<LineCode>();
	}
	
	/**
	 * 
	 * @param start DrawableBlock which represents the start node
	 */
	public void generateCode(DrawableBlock start) {
		init();
		
		Map <DrawableBlock, Node> g = new HashMap <DrawableBlock, Node>();
		
		time = 0;
		visit(g, start, null, null);
		
		// Now ready to generate python script
		generatePythonScript(g, start, 0, null);
		
		String s = "import sys\nfrom javax.swing import JOptionPane\n";
		for (LineCode line:script) {
			for (int i = 0; i < line.tabs; i++)
				s += "\t";
			
			s += line.code + "\n"; 
		}
		
		System.out.println(s);
		runPythonScript(s);
	}
}
