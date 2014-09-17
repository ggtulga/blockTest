
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
