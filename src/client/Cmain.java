package client;

import java.util.Calendar;
import java.util.Timer;

import util.Subrutinas;
import util.TicTac;

public class Cmain {
	
	private static String tag = "Cmain";

	private final static long fONCE_PER_DAY    = 1000*60*60*24;
    private final static long fONCE_PER_HOUR   = 1000*60*60;
    private final static long fONCE_PER_MINUTE = 1000*60;
    private final static long fONCE_PER_SECOND = 1000;
    private static long segundos = -1;
	private static TicTac _mTicTac;
	private static Timer  _mTimer  = new Timer( tag + "_scheduler" );

	public static void main(String[] args) {
		long inicio = Subrutinas.getDateInMills();
		boolean seguir = true;
		System.out.println(">>>>>>>>>>");

		if ( args.length < 2 ) {
			System.err.println("Error en argumentos del programa... son necesarios:");
			System.err.println("\t fullPathFilename");
			System.err.println("\t secs");
			seguir = false;
		}

		if (seguir) {
			segundos = Subrutinas.parse_long( args[1] );
			if ( segundos < 1 ) {
				System.err.println("Error en argumentos del programa... son necesarios:");
				System.err.println("\t fullPathFilename");
				System.err.println("\t secs");
				seguir = false;
			}
		}

		if (seguir) {
			System.out.println( "\t* fullPathFilename: " + args[0] );
			System.out.println( "\t* secs: " + args[1] );

			_mTicTac = new TicTac( args[0] );	// fullPathFilename

//			new Thread( new Runnable() {
//				public void run() {
					///////////////////////////////
					Calendar date = Calendar.getInstance();
					// TIEMPO PARA ESTABILIZAR. Iniciará dentro de 3 segundos:
					date.add( Calendar.SECOND, 3 );	    	
					System.out.println("\t* >>Planificando función TicTac( " + segundos + " segundos )...");
					_mTimer.schedule(
						_mTicTac,						// Task
					    date.getTime(),					// First time
					    (fONCE_PER_SECOND * segundos)	// Period
					    );
					System.out.println("\t* <<Función TicTac() planificada.");
					///////////////////////////////
//				}
//			} ).start();
			
		}

		System.out.println("<<<<<<<<<< " + tag + " " + ((Subrutinas.getDateInMills() - inicio) / 1000.0) + " segundos" );
		System.out.println();
	}

}
