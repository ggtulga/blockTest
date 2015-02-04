import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashMap;

public class ErrorCheck {

	String err;
	boolean foundEnd;
	HashSet<String> variables;
	HashSet<DrawableBlock> visited;
	ArrayList<ErrorMessage> errs;
	DrawableBlock current;
	final String[] keywords = {"and", "or"};

	private boolean matchKeyword(String s) {
		int i;
		for (i = 0; i < keywords.length; i++)
			if (s.equals(keywords[i]))
				return true;
		return false;
	}
	
	private boolean checkVarname(String varname, boolean isDeclared, boolean keywordAllowed) {
		boolean ret = true;
		
		varname = varname.trim(); 

		if (varname.equals(""))
			return true;

		if (isDeclared == true) {
			// if it's a number or string, consider it as declared 
			if (varname.matches("^[0-9]+") == true)
				return true;
			if (varname.matches("^(\\\"|\\')\\p{Print}+(\\\"|\\')$") == true)
				return true;
		}

		// check if there's a whitespace
		if (varname.matches("[^\\s]*") == false) {
			errs.add(new ErrorMessage(0, current));
			ret = false;
		}

		// validate variable name.
		if (varname.matches("^[A-Za-z_][A-Za-z0-9_\\[\\]]*") == false) {
			errs.add(new ErrorMessage(1, current));
			ret = false;
		}

		// if varname is okay, then check for declarations
		if (ret == true) {

			if (varname.indexOf('[') != -1)
				varname = varname.substring(0, varname.indexOf('['));

			if (matchKeyword(varname) == true) {
				if (keywordAllowed == false) {
					errs.add(new ErrorMessage(varname, 15, current));
					ret = false;
				}
			} else if (variables.contains(varname) == false) {
				if (isDeclared == true) {
					errs.add(new ErrorMessage(varname, 2, current));
					ret = false;
				}
			} else if (isDeclared == false) {
				errs.add(new ErrorMessage(varname, 3, current));
				ret = false;
			}
		}

		return ret;
	}
	// removes any strings by the string with their quotes.
	private String removeStrings(String cont) {
		int i, x, y;
		boolean isValid = true;
		x = y = 0;
		for (i = 0; i < cont.length(); i++) {
			if (cont.charAt(i) == '\'') {
				x = i;
				while (true) {
					i = cont.indexOf('\'', i + 1);
					if (i == -1) {
						errs.add(new ErrorMessage(4, current));
						isValid = false;
						break;
					} else if (cont.charAt(i - 1) != '\\') { // if it's escaped search again.
						y = i;
						break;
					}
				}
				if (isValid) {
					cont = cont.substring(0, x) + cont.substring(y + 1);
					i = 0;
				} else // if it's not valid, stop the search
					break;
				
			} else if (cont.charAt(i) == '"') {
				x = i;
				while (true) {
					i = cont.indexOf('"', i + 1);
					if (i == -1) {
						errs.add(new ErrorMessage(5, current));
						isValid = false;
						break;
					} else if (cont.charAt(i - 1) != '\\') { // if it's escaped search again.
						y = i;
						break;
					}
				}

				if (isValid) {
					cont = cont.substring(0, x) + cont.substring(y + 1);
					i = 0;
				} else
					break;
			}
		}

		if (isValid)
			return cont;
		else // return empty string if the string is not valid
			return "";
	}

	private boolean checkExpression(String exp) 
	{
		String[] vars = exp.split("=|\\+|\\-|\\*|/|%|\\(|\\)");
		for (int i = 0; i < vars.length; i++)
			if (checkVarname(vars[i], true, true) == false)
				return false;
		return true;
	}

	private boolean checkArrayIndex(String cont)
	{
		int s, f;
		s = cont.indexOf('[');
		while (s != -1) {
			f = cont.indexOf(']');
			String sub = cont.substring(s + 1, f);
			cont = cont.substring(0, s) + cont.substring(f + 1);
				
			if (checkExpression(sub) == false) {
				return false;
			}
			s = cont.indexOf('[');
		}
		return true;
	}

	private String removeArrayIndex(String cont) {
		int s, f;
		s = cont.indexOf('[');
		while (s != -1) {
			f = cont.indexOf(']');
			cont = cont.substring(0, s) + cont.substring(f + 1);
			
			s = cont.indexOf('[');
		}
		return cont;

	}

	private boolean search(DrawableBlock v)
	{
		if (v == null)
			return false;

		current = v;
		
		boolean ret = false;
		String cont = new String(v.getText().trim());
		int i;
		
		if (visited.contains(v)) {
			return ret;
		}

		visited.add(v);

		if (v.TYPE != BLOCKTYPE.POINT && cont.equals("")) {
			errs.add(new ErrorMessage(6, current));
			ret = true;
		}

		int s, f;
		switch (v.TYPE) {
		case POINT:
			break;
		case END:
			foundEnd = true;
			break;
		case INIT:
		{
			String[] vars = cont.split(",");
			boolean isValid;
			String varname; 
			for (i = 0; i < vars.length; i++) {
				varname = vars[i].trim();
				
				isValid = checkVarname(varname, false, false);

				// 	check if it's an array
				if (varname.matches("[\\p{Alnum}_]+([\\[\\p{Alnum}_]+\\])+") == true) {
					// if it is an array check for the size.				
					if (varname.matches("[\\p{Alnum}_]+(\\[\\d+\\])+") == false) {
						errs.add(new ErrorMessage(7, current));
						isValid = false;
					}
					varname = varname.substring(0, varname.indexOf('['));
				}

				if (isValid == true) {
					// save variable name for lookup
					variables.add(varname);
				} else
					ret = true;
			}

			break;
		}
		case IF:
		{
			cont = removeStrings(cont);
			// process arrays first, because array index
			// can be an expression.
			if (checkArrayIndex(cont) == false) {
				errs.add(new ErrorMessage(17, current));
				ret = true;
			} else {
				cont = removeArrayIndex(cont);
				String[] vars = cont.split("<=|>=|==|<|>|!=|\\+|\\-|\\*|/|%|\\(|\\)");
				Pattern p = Pattern.compile("=|<|>|!");
			
				for (i = 0; i < vars.length; i++) {
					Matcher m = p.matcher(vars[i]);
					if (m.find()) {
						errs.add(new ErrorMessage(10, current));
						ret = true;
					} else if (checkVarname(vars[i], true, true) == false) 
						ret = true;
				}
			}
			
			// If block returns here
			IfBlock block = (IfBlock) v;
			if (ret == false)
				ret = search(block.getNextTrue());
			else
				search(block.getNextTrue());
			
			if (ret == false)
				ret = search(block.getNextFalse());
			else
				search(block.getNextFalse());
			return ret;
		}
		case INPUT:
		{
			cont = removeStrings(cont);
			if (checkArrayIndex(cont) == false) {
				errs.add(new ErrorMessage(17, current));
				ret = true;
			} else {
				cont = removeArrayIndex(cont);
				String[] vars = cont.split(",");
				for (i = 0; i < vars.length; i++) {
					if (checkVarname(vars[i], true, false) == false)
						ret = true;
				}
			}
			break;

		}
		case OUTPUT:
		{
			cont = removeStrings(cont);
			if (checkArrayIndex(cont) == false) {
				errs.add(new ErrorMessage(17, current));
				ret = true;
			} else {
				cont = removeArrayIndex(cont);
				String[] outputs = cont.split(",");

				for (i = 0; i < outputs.length; i++) {
					if (checkExpression(outputs[i]) == false)
						ret = true;
				}
			}

			break;
		}
		case VALUE:

			cont = removeStrings(cont);
			if (checkArrayIndex(cont) == false) {
				Log.log("Array: " + cont);
				errs.add(new ErrorMessage(17, current));
				ret = true;
			} else {
				cont = removeArrayIndex(cont);
				String[] exp = cont.split(",");
				for (i = 0; i < exp.length; i++) {
					if (checkExpression(exp[i]) == false)
						ret = true;
				}
			}

			break;
		
		}
		// it's safe to check here. Because if blocks don't reach here.
		if (v.TYPE != BLOCKTYPE.END && v.getNext() == null) {
			errs.add(new ErrorMessage(11, current));
			return true;
		}

		if (ret == false)
			ret = search(v.getNext());
		else
			search(v.getNext());
		return ret;
	}
	
	private void freeMemory() {
		variables.clear();
		visited.clear();
	}

	private void init() {
		foundEnd = false;
		err = "";
		errs.clear();
	}

	public ErrorCheck() {
		variables = new HashSet<String>();
		visited = new HashSet<DrawableBlock>();
		errs = new ArrayList<ErrorMessage>();
	}

	public boolean checkForErrors(DrawableBlock start) {
		init();
		current = start;
		boolean ret = search(start);
		if (foundEnd == false) {
			err += "Заавал нэг төгсгөл байх ёстой!\n"; 
			ret = true;
		}
		// Need to free memory here.
		freeMemory();
		return ret;
	}

	public ArrayList<ErrorMessage> getErrors() {
		return errs;
	}
}
