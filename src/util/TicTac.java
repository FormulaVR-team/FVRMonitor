package util;

import java.util.TimerTask;

public class TicTac extends TimerTask {
	
	private final String tag = this.getClass().getSimpleName();
	private static String m_fullPathFilename;

	@Override
	public void run() {
		System.out.println("Chequeando " + m_fullPathFilename);

	}

	public TicTac( String fullPathFilename ) {
		super();
		m_fullPathFilename = fullPathFilename;
	}

}
