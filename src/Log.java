
public abstract class Log
{
	public static final boolean ENABLELOGGING = true;

	public static void log(Object msg) {
		if (ENABLELOGGING) {
			System.out.println("DEBUG: " + msg);
		}
	}

	public static void log(Throwable t) {
		if (ENABLELOGGING) {
			System.out.println("DEBUG:");
			t.printStackTrace();
		}
	}
}
