
import LoggerType
import sys
sys.path.append('Lib')
import traceback
import types
import cStringIO
import bdb
import re

DEBUG = True
MAX_LINES = 500
	
IGNORE_VARS = set(('__init_array__', '__builtins__', 'sys', 'JOptionPane'))

def unshared_copy(inList):
    if isinstance(inList, list):
        return list( map(unshared_copy, inList) )
    return inList

class Logger(bdb.Bdb, LoggerType):

    def __init__(self):
        bdb.Bdb.__init__(self)
        self.globals = {}
        self.trace = []
        self.lineno = -1

    def user_line(self, frame):
        self.parse(frame)

    def run_script(self, script):
        user_stdout = cStringIO.StringIO()

        old_stdout = sys.stdout
        
        sys.stdout = user_stdout

        user_globals = {
            "__user_stdout__" : user_stdout
            }

        try:
            self.run(script, user_globals, user_globals)
        except SystemExit:
            pass
        except:
            if DEBUG:
                traceback.print_exc()

            (exc_type, exc_val, exc_tb) = sys.exc_info()
            exc = traceback.format_exception_only(exc_type, exc_val)

            lineno = re.search(r'line\ ([0-9]*)', exc[0]).group(1)

            entry = [lineno]
            entry.append({'__error__' : exc[1] })
            
            self.trace.append(entry)

            
        sys.stdout = old_stdout
        # print self.trace
        return self.trace
        
        # for i in self.trace:
        #     print >> sys.stderr, i


    def parse(self, frame):
        if frame.f_code.co_filename != '<string>':
            return

        if frame.f_lineno == self.lineno:
            return

        self.lineno = frame.f_lineno

        if len(self.trace) > MAX_LINES:
            self.globals['__error_max_line__'] = True
            self.trace.append([self.lineno, dict.copy(self.globals)])
            raise bdb.BdbQuit
        
        for i, v in frame.f_globals.items():
	    if i in IGNORE_VARS:
		continue

            if i == '__user_stdout__':
                self.globals[i] = v.getvalue()
            elif '__block_' in i:
                continue    # exclude __init_array__ variable used for internal
            else:
                self.globals[i] = unshared_copy(v);
        
        self.trace.append([self.lineno, dict.copy(self.globals)])
        
# file = open('../tests/err.py', 'r')
# data = file.read()

# logger = Logger()
# print logger.run_script(data)
