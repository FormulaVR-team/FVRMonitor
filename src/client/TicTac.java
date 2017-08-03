package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.TimerTask;

import net.sf.json.JSONObject;
import util.Subrutinas;

public class TicTac extends TimerTask {

	private final String tag = this.getClass().getSimpleName();

	private static long contador_de_llamadas_pendientes_al_Server = 0;
	
	private static String m_fullPathFilenameToObserve;
	private static String m_url;
	private static String m_pathBuffer;
	private static FileTime recu_lastModifiedTime = null;
	private static long recu_size = 0;

	public TicTac( String fullPathFilename, String url ) {
		super();
		m_fullPathFilenameToObserve = fullPathFilename;
		m_url = url;
		m_pathBuffer = new File(".").getAbsolutePath();
		m_pathBuffer = m_pathBuffer.substring(0,m_pathBuffer.length()-1);
	}

	@Override
	public void run() {

		new Thread( new Runnable() {
			public void run() {
				pasoPeriodico();
			}
		} ).start();

	}

	private void pasoPeriodico() {
		//////////////////////////////
		// Procesar si:
		//		el fichero existe.
		//		su fecha y hora de modificación es diferente a la de la lectura anterior.
		//		su tamaño es diferente a la de la lectura anterior.
		//	Proceso:
		//		1 . Grabar como mensaje pendiente de enviar al server.
		//		2 . Buscar mensajes pendientes de enviar al server y tratar de enviar ordenadamente.
		//////////////////////////////

//		System.out.println("Chequeando " + m_fullPathFilename);
//		System.out.print(".");

		File f = new File( m_fullPathFilenameToObserve );
		if ( f.isFile() && f.exists() ) {
			try {
				Path path = f.toPath();
				BasicFileAttributes atributos = Files.readAttributes(path, BasicFileAttributes.class);
				if ( recu_lastModifiedTime == null || atributos.lastModifiedTime().compareTo( recu_lastModifiedTime ) != 0 ) {
					if ( atributos.size() != recu_size ) {
						persistirFichero( f, atributos.lastModifiedTime() );
					}
				}
				recu_lastModifiedTime = atributos.lastModifiedTime();
				recu_size = atributos.size();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		contador_de_llamadas_pendientes_al_Server++;
//		for ( int i= 0; i < contador_de_llamadas_pendientes_Server; i++) { System.out.print(" "); }
//		System.out.println( "Antes   " + contador_de_llamadas_pendientes_Server );
		
		if ( contador_de_llamadas_pendientes_al_Server < 2) {
			procesarFicherosPendientes();
		} else {
//			System.out.println( "BUSY" );
			System.out.print( "." );
		}

//		for ( int i= 0; i < contador_de_llamadas_pendientes_Server; i++) { System.out.print(" "); }
//		System.out.println( "Después " + contador_de_llamadas_pendientes_Server );
		contador_de_llamadas_pendientes_al_Server--;
	}

	private boolean persistirFichero(File f, FileTime lastModifiedTime) {
		byte[] contenido = Subrutinas.readFileBin(null, m_fullPathFilenameToObserve);
		if ( contenido != null && contenido.length > 0 ) {
			String nombre = lastModifiedTime.toString() + "_file.snd";
			nombre = m_pathBuffer + Subrutinas.neutralizarCaracteres_CompatibilizarConFileNames(nombre, '-');
			Subrutinas.grabFile(null, nombre, contenido);
			System.out.println( "Push storage: " + nombre );
		}
		return false;
	}

	private synchronized void procesarFicherosPendientes() {

//		String url = "http://localhost:8080/FormulaVR/FvrServlet?ACC=FVRMonitor";
		/////////////
		// Lista de ficheros pendientes de enviar al server:
		File dir = new File(m_pathBuffer);
		File[] lista = Subrutinas.getFiles_endsWith(dir, ".snd");

		if ( lista != null ) {
			long inicio = Subrutinas.getDateInMills();
			String contenido = null;
			JSONObject json = new JSONObject();
			for ( File item : lista ) {
				
				contenido = Subrutinas.readFile( null, item.getAbsolutePath() );
				json.clear();
				json.put("name", item.getName());
				json.put("content", contenido);

				if ( contenido != null && contenido.trim().length() > 0 ) {
					///////////////
					String[] resHttp_code_msg = new String[2];
					try {
						Subrutinas.send_http(m_url, json.toString(), resHttp_code_msg);
						if ( resHttp_code_msg != null ) {
							int code = Subrutinas.parse_integer( resHttp_code_msg[0] );
							if ( code == 200 ) {
								System.out.println( resHttp_code_msg[0] + " : " + resHttp_code_msg[1] );
								json = null;
								try { json = JSONObject.fromObject( resHttp_code_msg[1] ); } catch (Exception e) {;}
								if ( json != null ) {
									String rc = null;
									try { rc = json.getString("rc"); } catch (Exception e) {;}
									if ( "OK".equalsIgnoreCase( rc ) ) {
										item.delete();
									}
								}

							}
						}
					} catch (Exception e) {
						System.err.println( e.getCause() + e.getMessage() );
					}
					///////////////
				}

			}
			System.out.println( tag + ".procesarFicherosPendientes( " + lista.length + " regs ) " + ((Subrutinas.getDateInMills() - inicio) / 1000.0) + " segundos" );
		}

		/////////////

	}

}
