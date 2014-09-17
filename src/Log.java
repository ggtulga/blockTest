
public abstract class Log
{
	public static final boolean ENABLELOGGING = true;

	public static void log(Object msg) {
		if (ENABLELOGGING) {
			System.out.println(msg);
		}
	}

	public static void log(Throwable t) {
		if (ENABLELOGGING) {
			t.printStackTrace();
		}
	}
}
