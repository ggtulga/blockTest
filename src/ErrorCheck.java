
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorCheck {
	
    String err;
    boolean foundEnd;
    HashSet<String> vars;
	
    private boolean checkVarname(String varname, boolean isDeclared) {
	boolean ret = true;
		
	if (isDeclared == true) {
	    if (varname.matches("^[0-9]+") == true)
		return true;
	    if (varname.matches("^{\"|\'}\\S+{\"|\'}$") == true)
		return true;
	}
		
	if (varname.matches("[^\\s]*") == false) {
	    err += "Хувьсагчдын нэрүүд таслалаар тусгаарлагдах ба нэр нь хоосон зай агуулахгүй!\n";
	    ret = false;
	}
	
	// validate variable name.
	if (varname.matches("^[A-Za-z][A-Za-z0-9\\[\\]]*") == false) {
	    err += "Хувьсагчийн нэр зөвхөн Англи цагаан толгойн үсгүүд болон тоог агуулсан байна. Гэхдээ тоогоор эхлэж болохгүй!\n";
	    ret = false;
	}
		
	// if varname is okay, then check for declarations
	if (ret == true) {
	    if (isDeclared == true) {
		if (vars.contains(varname) == false) {
		    err += varname + "хувьсагч зарлагдаагүй байна!\n";
		    ret = false;
		}
	    }
			
	}
		
	return ret;
    }
	
    private boolean search(DrawableBlock v)
    {
	boolean ret = true;
	boolean isValid;
	String cont = v.getText().trim();
	String varname;
	int i;
		
	if (v == null)
	    return true;
		
	//		if (v.TYPE != BLOCKTYPE.END && v.getNext() == null) {
	//			err += "Бүх болокууд хоорондоо холбогдсон байх ёстой ба хамгийн сүүлд төгсгөлийн блоктой холбогдоно!\n";
	//			ret = false;
	//		}
		
	if (cont == "") {
	    err += "Блок хоосон байж болохгүй!\n";
	    ret = false;
	}
		
	switch (v.TYPE) {
	case END:
	    foundEnd = true;
	    break;
	case INIT:
	    int s = 0, f;
			
	    while (true) {
		f = cont.indexOf(",", s);
		if (f == -1) {
		    f = cont.length();
		}
		varname = cont.substring(s, f).trim();
		isValid = true;
				
		isValid = checkVarname(varname, false);
				
		// check if it's an array
		if (varname.matches("\\p{Alnum}+\\[\\p{Alnum}*\\]") == true) {
		    // if it is an array check for the size.				
		    if (varname.matches("\\p{Alnum}+\\[\\p{Alnum}+\\]") == false || varname.matches("\\p{Alnum}+\\[\\d+\\]") == false) {
			err += "Хүснэгтийн хэмжээ тогтмол тоо байна.!\n";
			isValid = false;
		    }
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
		err += "Хэрэглэгчээс нэг дор зөвхөн нэг л хувьсагчид утга авна!\n";
		ret = true;
	    } else if (checkVarname(cont, true) == false)
		ret = true;
	    break;
			
	case IF:
			
	    String[] names = cont.split("<=|>=|==|<|>|!=");
	    Pattern p = Pattern.compile("=|<|>|!");
			
	    for (i = 0; i < names.length; i++) {
		Matcher m = p.matcher(names[i]);
		if (m.find()) {
		    err += "Зөвхөн <=, >=, ==, <, >, != харьцуулах операторуудыг хэрэглэнэ үү.";
		    ret = true;
		} else if (checkVarname(names[i], true) == false) 
		    ret = true;
	    }
	    break;
		
	case OUTPUT:
			
	    String[] out = cont.split(",");
	    for (i = 0; i < out.length; i++) {
		if (checkVarname(out[i], true) == false)
		    ret = true;
	    }
	    break;
			
	case VALUE:
			
			
	}
		
	return ret;
    }
	
    private void init() {
	foundEnd = false;
	err = "";
	vars.clear();
    }
	
    public ErrorCheck() {
		
    }
	
    public boolean checkForErrors(DrawableBlock start) {
	init();
	return search(start);
    }
}
