
import LoggerType

import sys
sys.path.append('./lib/')
import traceback
import types
import cStringIO
import bdb
import copy

DEBUG = True
MAX_LINES = 300

class Logger(bdb.Bdb, LoggerType):

    def __init__(self):
        bdb.Bdb.__init__(self)
        self.globals = {}
        # self.order = []
        self.trace = []
        self.lineno = -1

    def user_line(self, frame):
        self.parse(frame)

    def run_script(self, script):
        user_stdout = cStringIO.StringIO()

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

            trace_entry = dict(event='uncaught_exception')
                          
            (exc_type, exc_val, exc_tb) = sys.exc_info()
            if hasattr(exc_val, 'lineno'):
                trace_entry['line'] = exc_val.lineno
            if hasattr(exc_val, 'offset'):
                trace_entry['offset'] = exc_val.offset

            trace_entry['exception_msg'] = type(exc_val).__name__ + ": " +  str(exc_val)

            # SUPER SUBTLE! if this exact same exception has already been
            # recorded by the program, then DON'T record it again as an
            # uncaught_exception
            already_caught = False
            for e in self.trace:
                if e['event'] == 'exception' and e['exception_msg'] == trace_entry['exception_msg']:
                    already_caught = True
                    break

            if not already_caught:
                self.trace.append(trace_entry)

            raise bdb.BdbQuit # need to forceably STOP execution
        
        
        return self.trace
        
        # for i in self.trace:
        #     print >> sys.stderr, i

        # print >> sys.stderr, self.order

    def parse(self, frame):
        if frame.f_code.co_filename != '<string>':
            return

        if frame.f_lineno == self.lineno:
            return

        self.lineno = frame.f_lineno

        if len(self.trace) > MAX_LINES:
            self.globals["__error_max_line__"] = True
            raise bdb.BdbQuit
        
        for i, v in frame.f_globals.items():
            if i == '__builtins__' or i == 'sys' or i == 'JOptionPane':
                continue

            if i == '__user_stdout__':
                self.globals[i] = v.getvalue()
            elif i == '__init_array__':
                continue    # exclude __init_array__ variable used for internal
            else:
                self.globals[i] = copy.copy(v);
        
        self.trace.append([self.lineno, dict.copy(self.globals)])
        
            
# file = open('test/for_loop.py', 'r')
# data = file.read()

# logger = Logger()
# logger._run_script(data)
