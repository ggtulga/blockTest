
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class ErrorCheck {

	String err;
	boolean foundEnd;
	HashSet<String> vars;
	HashSet<DrawableBlock> visited;
	ArrayList<ErrorMessage> errs;
	DrawableBlock current;

	private boolean checkVarname(String varname, boolean isDeclared) {
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

			if (vars.contains(varname) == false) {
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

	private boolean checkString(String cont) {
		int i; 
		for (i = 0; i < cont.length(); i++) {
			if (cont.charAt(i) == '\'') {
				while (true) {
					i = cont.indexOf('\'', i + 1);
					if (i == -1) {
						errs.add(new ErrorMessage(4, current));
						return false;
					} else if (cont.charAt(i - 1) != '\\') // if it's escaped search again.
						break;
				}
			} else if (cont.charAt(i) == '"') {
				while (true) {
					i = cont.indexOf('"', i + 1);
					if (i == -1) {
						errs.add(new ErrorMessage(5, current));
						return false;
					} else if (cont.charAt(i - 1) != '\\') // if it's escaped search again.
						break;
				}
			}
		}

		return true;
	}

	private boolean search(DrawableBlock v)
	{
		if (v == null)
			return false;

		current = v;
		
		boolean ret = false;
		boolean isValid;
		String cont = new String(v.getText().trim());
		String varname;
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
			s = f = 0;
			while (true) {
				f = cont.indexOf(",", s);
				if (f == -1) {
					f = cont.length();
				}
				varname = cont.substring(s, f).trim();
				isValid = true;

				isValid = checkVarname(varname, false);

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
					vars.add(varname);
				} else
					ret = true;

				if (f == cont.length())
					break;

				s = f + 1;
			}
			break;

		case INPUT:

			if (cont.matches("[^\\s,]*") == false) {
				errs.add(new ErrorMessage(8, current));
				ret = true;
			} else if (checkVarname(cont, true) == false)
				ret = true;
			break;

		case IF:

			if (checkString(cont) == true) {
				// Remove strings from the contents
				s = f = 0;
				while (true) {
					if (cont.charAt(s) == '\'') {
						while (true) {
							f = cont.indexOf('\'', s + 1);
							if (f == -1) {
								errs.add(new ErrorMessage(9, current));
								ret = true;
								break;
							}
							if (cont.charAt(f - 1) != '\\') {
								cont = cont.substring(0, s) + cont.substring(f + 1);
								break;
							}
						}
						if (f == -1)
							break;
						s = f + 1;
					} else if (cont.charAt(s) == '"') {
						while (true) {
							f = cont.indexOf('"', s + 1);
							if (f == -1) {
								errs.add(new ErrorMessage(9, current));
								ret = true;
								break;
							}
							if (cont.charAt(f - 1) != '\\') {
								cont = cont.substring(0, s) + cont.substring(f + 1);
								break;
							}
						}
						if (f == -1)
							break;
						s = f + 1;
					} else
						s++;

					if (s >= cont.length())
						break;
				}

				String[] names = cont.split("<=|>=|==|<|>|!=|\\+|\\-|\\*|/|%");
				Pattern p = Pattern.compile("=|<|>|!");

				for (i = 0; i < names.length; i++) {
					Matcher m = p.matcher(names[i]);
					if (m.find()) {
						errs.add(new ErrorMessage(10, current));
						ret = true;
					} else if (checkVarname(names[i], true) == false) 
						ret = true;
				}
			} else
				ret = true;
			
			// If block returns here
			IfBlock block = (IfBlock) v;
			ret = search(block.getNextTrue());
			if (ret == false)
				ret = search(block.getNextFalse());
			else
				search(block.getNextFalse());
			return ret;

		case OUTPUT:

			if (checkString(cont) == true) {
				String[] out = cont.split(",");
				for (i = 0; i < out.length; i++) {
					if (checkVarname(out[i], true) == false)
						ret = true;
				}
			} else
				ret = true;
			break;

		case VALUE:
			if (checkString(cont) == true) {
				String[] vars = cont.split("=|\\+|\\-|\\*|/|%");
				for (i = 0; i < vars.length; i++)
					if (checkVarname(vars[i], true) == false)
						ret = true;

			} else
				ret = true;
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
		vars.clear();
		visited.clear();
	}

	private void init() {
		foundEnd = false;
		err = "";
		errs.clear();
	}

	public ErrorCheck() {
		vars = new HashSet<String>();
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
