package client;

import java.util.Calendar;
import java.util.Timer;

import server.Server;
import util.Subrutinas;

public class Client {
	
	private static String tag = "Client";

	private final static long fONCE_PER_DAY    = 1000*60*60*24;
    private final static long fONCE_PER_HOUR   = 1000*60*60;
    private final static long fONCE_PER_MINUTE = 1000*60;
    private final static long fONCE_PER_SECOND = 1000;
    private static long segundos = -1;
    private static int port = 0;
	private static TicTac _mTicTac;
	private static Timer  _mTimer  = new Timer( tag + "_scheduler" );

	public static void main(String[] args) {
		long inicio = Subrutinas.getDateInMills();
		boolean seguir = true;
		System.out.println(">>>>>>>>>>");
		
		// EJEMPLO DE PARÁMETROS:
		//		java -jar FVRMonitor.jar "C:\d\Java\jdk1.8.0_131\COPYRIGHT" 5 http://localhost:8080/FormulaVR/FvrServlet?ACC=FVRMonitor 47000

		if ( args.length < 4 ) {
			System.err.println("Error en argumentos del programa... son necesarios:");
			System.err.println("\t fullPathFilename_toObserve");
			System.err.println("\t period_in_secs");
			System.err.println("\t url_remote_server_listener");
			System.err.println("\t local_port_listen");
			seguir = false;
		}

		if (seguir) {
			segundos = Subrutinas.parse_long( args[1] );
			if ( segundos < 1 ) {
				System.err.println("Error en argumentos del programa... son necesarios:");
				System.err.println("\t fullPathFilename_toObserve");
				System.err.println("\t period_in_secs");
				System.err.println("\t url_remote_server_listener");
				System.err.println("\t local_port_listen");
				seguir = false;
			}
			port = Subrutinas.parse_integer( args[3] );
			if ( port < 1 ) {
				System.err.println("Error en argumentos del programa... son necesarios:");
				System.err.println("\t fullPathFilename_toObserve");
				System.err.println("\t period_in_secs");
				System.err.println("\t url_remote_server_listener");
				System.err.println("\t local_port_listen");
				seguir = false;
			}
		}

		if (seguir) {
			System.out.println( "\t* fullPathFilename: " + args[0] );
			System.out.println( "\t* secs: " + args[1] );
			System.out.println( "\t* local_port_listen: " + args[3] );
			System.out.println( "\t* url_remote_server_listener: " + args[2] );

			_mTicTac = new TicTac( args[0], args[2] );	// fullPathFilename

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

			Server.listen(port);

			///////////////////////////////
		}

		System.out.println("<<<<<<<<<< " + tag + " " + ((Subrutinas.getDateInMills() - inicio) / 1000.0) + " segundos" );
		System.out.println();
	}

}
