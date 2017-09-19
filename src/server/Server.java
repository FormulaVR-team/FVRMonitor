package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import net.sf.json.JSONObject;
import util.Subrutinas;

public class Server {
	private static final String m_tag = "FVRMonitor - Server";

	public static void listen( int port ) {
		while (true) {
			try {
				System.out.println(m_tag + " >>>>>>>>>>");
				listener( port );
				System.out.println(m_tag + " <<<<<<<<<<");
			} catch (Exception e) {
				System.err.println( e.getMessage() );
			}
		}
	}

	private static void listener(int port)throws Exception {
		ServerSocket server = new ServerSocket(port);
		Socket conn = server.accept();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		OutputStream out = conn.getOutputStream();
		int count = 0;
		int contentLength = 0;
		while (true) {
			count++;
			String line = reader.readLine();
			if (line == null) { break; }
//			System.out.println("" + count + ": " + line);
			
			if ( line.toLowerCase().contains("content-length") ) {
				String[] trozos = line.split(":");
				if ( trozos.length == 2 ) {
					try { contentLength = Integer.parseInt( trozos[1].trim() ); } catch (Exception e) {;}
				}
			}

			if (line.equals("")) {
				if ( contentLength > 0 ) {
					char cbuf[] = new char[contentLength];
					int char_readed = reader.read(cbuf);
					responder(out, true, "Bytes received: " + char_readed);
					if ( char_readed > 0 ) {
						insertarFichero( cbuf );
					}
				} else {
					responder(out, false, "NO POST DATA RCEIVED");
				}
			}
		}
		server.close();
	}

	private static void responder(OutputStream out, boolean rc, String texto)throws IOException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("server", getComputername());
		jsonObject.put("class", m_tag);
		jsonObject.put("rc", rc ? "OK" : "KO");
		jsonObject.put("text", texto);

		byte[]response = jsonObject.toString().getBytes("ASCII");

		// headers:
		String statusLine = "HTTP/1.1 200 OK\r\n";
		String contentType = "Content-Type: " + "application/json" + "\r\n";
		String contentLength = "Content-Length: " + response.length + "\r\n";
		out.write(statusLine.getBytes("ASCII"));
		out.write(contentType.getBytes("ASCII"));
		out.write(contentLength.getBytes("ASCII"));
		// signal end of headers:
		out.write("\r\n".getBytes("ASCII"));
		// response:
		out.write(response);
		out.flush();

//		System.out.println("RESPONSE:" + jsonObject.toString());
	
	}

	private static String getComputername() {
		String computername = "SinNombre";
		try { computername = InetAddress.getLocalHost().getHostName(); } catch (UnknownHostException e) { ; }
		return computername;
	}

	private static void insertarFichero(char[] cbuf) {
		if ( cbuf != null && cbuf.length > 0 ) {
			String jsonText = new String(cbuf);
			if ( jsonText != null && jsonText.trim().length() > 0 ) {
				JSONObject json = null;
				try { json = JSONObject.fromObject(jsonText); } catch (Exception e) {;}
				
				if ( json != null ) {
					String filename = null;
					String filecontent = null;
					try { filename = json.getString("filename"); } catch (Exception e) {;}
					try { filecontent = json.getString("filecontent"); } catch (Exception e) {;}
					if ( filename != null && filecontent != null ) {
						if ( filename != null && filename.trim().length() > 0 ) {

							Subrutinas.grabFile(null, filename, filecontent.getBytes());
							System.out.println("\nFICHERO RECIBIDO: " + filename);
							
						}
					}
				}

			}
		}
	}

}
