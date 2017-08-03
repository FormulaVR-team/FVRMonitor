package util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.http.NameValuePair;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import util.http.client.IHttpClient;
import util.http.client.IHttpMethods;
import util.http.client.ProxyHttpMethods;
import util.http.client.TLSHttpClient;

/**
 * @author Emilio Estecha 2017
 *
 */
public class Subrutinas {
	private final static String tag = "Subrutinas";	//this.getClass().getSimpleName();
	
	////////////////////////
	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}
	public static String padLeft(String s, int n) {
		return String.format("%1$#" + n + "s", s);
	}
	public static String padLeftCeros(long dato, int n) {
		return String.format("%0" + n + "d", dato);
	}
	////////////////////////
	public static int    parse_integer( String s ) {
		int res = 0;
		try { res = Integer.parseInt(s); } catch (NumberFormatException ex) {;}
		return res;
	}
	public static long   parse_long( String s ) {
		long res = 0;
		try { res = Long.parseLong(s); } catch (NumberFormatException ex) {;}
		return res;
	}
	public static double parse_double( String s ) {
		double res = 0.0;
		try { res = Double.parseDouble(s); } catch (NumberFormatException ex) {;}
		return res;
	}
	public static String bytesToHex(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j=0; j<b.length; j++) {
           buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
           buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
     }
    public static String getRandomHashCode() {
    	String resultado = "???";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			// byte[] bytes = digest.digest( usr.getBytes() );	// Siewmpre devuelve el mismo valor.
			Integer pito = (int) (Math.random() * 10000);		// hashCode aleatorio
			byte[] bytes = digest.digest( pito.toString().getBytes() );
			resultado = Subrutinas.bytesToHex( bytes );
		} catch (NoSuchAlgorithmException e) {;}
		
		return resultado;
    }
    public static String getComputername() {
    	String computername = "SinNombre";
    	try { computername = InetAddress.getLocalHost().getHostName();} catch (UnknownHostException e) {;}
    	return computername;
    }
	public static String get_CORS_incomingURLs() {
//		String msgId = "CORS.incomingURLs";
		String r = "?";//org.apache.struts.util.MessageResources.getMessageResources(Subrutinas.archivo_config).getMessage(msgId);
//		if (r == null) {
//			r = "";
//			System.err.println("Error al recuperar del archivo de propiedades '" + Subrutinas.archivo_es + "' en key: '" + msgId + "'");
//		}
		return r;
	}
    public static String getHashFromRandomCode() {
    	String resultado = "???";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			// byte[] bytes = digest.digest( usr.getBytes() );	// Siewmpre devuelve el mismo valor.
			Integer pito = (int) (Math.random() * 10000);		// hashCode aleatorio
			byte[] bytes = digest.digest( pito.toString().getBytes() );
			resultado = Subrutinas.bytesToHex( bytes );
		} catch (NoSuchAlgorithmException e) {;}
		
		return resultado;
    }
    public static String getHashFromString( String dato ) {
    	String resultado = "getHashFromString_error";
    	
    	// GENERA UN HASH: "SHA-1"
    	// SHA-1 produces a 160-bit (20-byte) hash value
    	// El resultado es una cadena de 40 caracteres.
    	
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			byte[] bytes = digest.digest( dato.getBytes() );	// Siewmpre devuelve el mismo valor.
			resultado = Subrutinas.bytesToHex( bytes );
		} catch (NoSuchAlgorithmException e) {;}
		
		return resultado;
    }
    public static String getHashMD5FromString( String dato ) {
    	String resultado = "getHashMD5FromString_error";
    	
    	// GENERA UN HASH: "SHA-1"
    	// SHA-1 produces a 160-bit (20-byte) hash value
    	// El resultado es una cadena de 40 caracteres.
    	
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest( dato.getBytes() );	// Siewmpre devuelve el mismo valor.
			resultado = Subrutinas.bytesToHex( bytes );
		} catch (NoSuchAlgorithmException e) {;}
		
		return resultado;
    }

	public static String neutralizarCaracteresEspeciales(String inOut) {
	    // La representaciÛn o descomposiciÛn canÛnica consiste en la descomposiciÛn del car·cter en 2 partes:
		//- Parte 1: Letra base
		//- Parte 2: Acento
		// DescomposiciÛn canÛnica
	    inOut = Normalizer.normalize(inOut, Normalizer.Form.NFD);
	    // Nos quedamos ˙nicamente con los caracteres ASCII
	    Pattern pattern = Pattern.compile("\\P{ASCII}+");
	    inOut = pattern.matcher(inOut).replaceAll(""); 
	    pattern = null;
	    return inOut;
	}
	public static String neutralizarCaracteresEspeciales_bis(String inOut) {
	    String original = "·‡‰ÈËÎÌÏÔÛÚˆ˙˘¸Ò¡¿ƒ…»ÀÕÃœ”“÷⁄Ÿ‹—Á«";
	    String ascii    = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    for (int i=0; i<original.length(); i++) {
	        inOut = inOut.replace(original.charAt(i),ascii.charAt(i));
	    }
	    return inOut;
	}
	public static String neutralizarCaracteres_CompatibilizarConFileNames(String inOut, char caracterSustituto) {
		//    \ / : * ? " < > |
	    inOut = inOut.replace('\\', caracterSustituto);
	    inOut = inOut.replace( '/', caracterSustituto);
	    inOut = inOut.replace( ':', caracterSustituto);
	    inOut = inOut.replace( '*', caracterSustituto);
	    inOut = inOut.replace( '?', caracterSustituto);
	    inOut = inOut.replace( '"', caracterSustituto);
	    inOut = inOut.replace( '<', caracterSustituto);
	    inOut = inOut.replace( '>', caracterSustituto);
	    inOut = inOut.replace( '|', caracterSustituto);

	    // agregado 2016-06-23:
	    inOut = inOut.replace('\t', caracterSustituto);
	    inOut = inOut.replace('\r', caracterSustituto);
	    inOut = inOut.replace('\n', caracterSustituto);
	    
	    return inOut;
	}
	public static String neutralizarCaracteres_CompatibilizarCon_emailName_LOCALPART(String inOut) {
		// SACADO DE: https://en.wikipedia.org/wiki/Email_address#Local_part
//		Local part[edit]
//		The local-part of the email address may use any of these ASCII characters.[4] RFC 6531 permits Unicode characters beyond the ASCII range:
//
//		Uppercase and lowercase Latin letters (AñZ, añz) (ASCII: 65ñ90, 97ñ122)
//		Digits 0 to 9 (ASCII: 48ñ57)
//		These special characters: # - _ ~ ! $ & ' ( ) * + , ; = : and percentile encoding i.e. %20
//		Character . (dot, period, full stop), ASCII 46, provided that it is not the first or last character, and provided also that it does not appear consecutively (e.g. John..Doe@example.com is not allowed).
//		Special characters are allowed with restrictions. They are:
//		Space and "(),:;<>@[\] (ASCII: 32, 34, 40, 41, 44, 58, 59, 60, 62, 64, 91ñ93)
//		Comments are allowed with parentheses at either end of the local part; e.g. john.smith(comment)@example.com and (comment)john.smith@example.com are both equivalent to john.smith@example.com.
//		International characters above U+007F, encoded as UTF-8, are permitted by RFC 6531, though mail systems may restrict which characters to use when assigning local parts.
//		A quoted string may exist as a dot separated entity within the local-part, or it may exist when the outermost quotes are the outermost characters of the local-part (e.g., abc."defghi".xyz@example.com or "abcdefghixyz"@example.com are allowed. Conversely, abc"defghi"xyz@example.com is not; neither is abc\"def\"ghi@example.com). Quoted strings and characters however, are not commonly used. RFC 5321 also warns that "a host that expects to receive mail SHOULD avoid defining mailboxes where the Local-part requires (or uses) the Quoted-string form".
//
//		The local-part postmaster is treated speciallyñit is case-insensitive, and should be forwarded to the domain email administrator. Technically all other local-parts are case-sensitive, therefore jsmith@example.com and JSmith@example.com specify different mailboxes; however, many organizations treat uppercase and lowercase letters as equivalent.
//
//		Most organizations do not allow use of many of the technically valid special characters. Organizations may restrict the form of an email addresses as desired, e.g., Windows Live Hotmail only allows creation of email addresses using alphanumerics, dot (.), underscore (_) and hyphen (-).[5]
		
		// "(),:;<>@[\] 
	    String original = "\"(),:;<>@[]{} ";
	    String ascii    =  "___...........";
	    for (int i=0; i<original.length(); i++) {
	        inOut = inOut.replace(original.charAt(i),ascii.charAt(i));
	    }
	    return inOut;
	}
	public static String neutralizarNIF(String nif) {
		nif = nif.toUpperCase();
		Subrutinas.neutralizarCaracteresEspeciales( nif );
		Subrutinas.neutralizarCaracteres_CompatibilizarCon_emailName_LOCALPART( nif );
		nif = nif.replace("/","");
		nif = nif.replace("-","");
		nif = nif.replace(".","");
		nif = nif.replace("_","");
		nif = nif.replace("=","");
		nif = nif.replace("%","");
		
		return nif;
	}

	public byte[] base64_decode( String texto_B64 ) {
		byte[] byteArray = null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byteArray = decoder.decodeBuffer(texto_B64);
		} catch (IOException e) { System.out.println( "base64_decode() : " + e.getMessage() ); }
		return byteArray;
	}
	public String base64_encode( byte[] datos ) {
		String resultado = null;
		if ( datos != null ) {
			resultado = new BASE64Encoder().encode(datos);
		}
        return resultado;
	}
    
    //////////////////////
	// FICHEROS
	public static int ZIP_addFiles         ( final StringBuffer logVar_o_null, final String[] fileNamesList,  final String nombreZipFicheroCompleto ) {
		int resultado = 0;
		if(logVar_o_null!=null) logVar_o_null.append("\r\n" + "ZIP_addFiles( " + nombreZipFicheroCompleto + " ) >>>>>>>>>>>>>>");

		if ( fileNamesList == null || fileNamesList.length < 1 ) return resultado;

		try {
			File file = new File( nombreZipFicheroCompleto );
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ZipOutputStream  zos = new ZipOutputStream(fos);
			try {
				for (int i = 0; i < fileNamesList.length; ++i) {
					byte[] bytes = readFileBin( logVar_o_null, fileNamesList[i] );
					try {
						if(logVar_o_null!=null) logVar_o_null.append("\r\n" + "ZIP_addFiles() ADD '" + fileNamesList[i] + "'...");
						zos.putNextEntry( new ZipEntry( fileNamesList[i] ) );
						zos.write(bytes);
						++resultado;
					} catch (IOException e) {
						if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
					} finally {
						zos.closeEntry();
						bytes = null;
					}
				}
			} catch (IOException e) {
				if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
			} finally {
				zos.close();
			}
		} catch (IOException e) {
			if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
		}
		if(logVar_o_null!=null) logVar_o_null.append("\r\n" + "ZIP_addFiles( " + nombreZipFicheroCompleto + " ) <<<<<<<<<<<<<<");
		return resultado;
	}
	public static int ZIP_extraerConFiltro ( final StringBuffer logVar_o_null, final String nombreZipFicheroCompleto, final String dirDestino, final String filtroDeNombres_patron) {
		int numExtraidos = 0;
		String patron = (filtroDeNombres_patron==null)?"":filtroDeNombres_patron.trim();
		FileInputStream fis = null;
		ZipInputStream zis = null;
		ZipEntry ze = null;
		File zipFile = new File( nombreZipFicheroCompleto );
		//////////////////////////////////////
		if ( zipFile.exists() && zipFile.canRead() ) {
			try {
				fis = new FileInputStream( zipFile );
				zis = new ZipInputStream( new BufferedInputStream(fis) );
				try {
					try {
						String nomFicComp = null;
						String filename  = null;
						while ((ze = zis.getNextEntry()) != null) {

							filename = ze.getName().replace('\\', '/');

							if ( !ze.isDirectory() && filename.indexOf( patron ) > -1 ) {
								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								byte[] buffer = new byte[65536];
								int count;
								while ( (count = zis.read(buffer)) != -1 ) {
									baos.write(buffer, 0, count);
								}
								//////////////////////////////////////////
								nomFicComp = dirDestino + File.separator + filename;
								if ( grabFile( logVar_o_null, nomFicComp, baos.toByteArray() ) ) {
									if(logVar_o_null!=null) logVar_o_null.append("\r\n" + nomFicComp);
									numExtraidos++;
								}
								//////////////////////////////////////////
							}

						}
					} catch (IOException e) {
						if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
						e.printStackTrace();
					}
				} finally {
					try {
						if ( zis != null ) zis.close();
					} catch (IOException e) {
						if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
				e.printStackTrace();
			}
		} else {
			if(logVar_o_null!=null) logVar_o_null.append("\r\n" + "El fichero \n\r\t" + nombreZipFicheroCompleto + "\n\r no existe o no se puede leer.");
		}
		//////////////////////////////////////
		return numExtraidos;
	}
	public static boolean grabFile         ( final StringBuffer logVar_o_null, final String nombreFicheroCompleto, final byte[] contenido ) {
		boolean resultado = false;

		String nomCamino = nombreFicheroCompleto.replace('\\', '/');
		int idx = nomCamino.lastIndexOf( '/' );
		if (idx > -1 ) nomCamino = nomCamino.substring( 0, idx );

		crtDir(logVar_o_null, nomCamino);

		FileOutputStream fos = null;
		try {
			File file = new File( nombreFicheroCompleto );
			file.delete();
			file.createNewFile();
			fos = new FileOutputStream(file);
			fos.write( contenido );
			resultado = true;
		} catch (FileNotFoundException e) {
			if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return resultado;
	}
	public static String  readFile         ( final StringBuffer logVar_o_null, final String nombreFicheroCompleto ) {
		String contenido = "";
		try {
			byte[] buff = readFileBin( logVar_o_null, nombreFicheroCompleto );
			if ( buff != null ) {
				contenido = new String( buff , "ISO-8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
		}
		return contenido;
	}
	public static byte[]  readFileBin      ( final StringBuffer logVar_o_null, final String nombreFicheroCompleto ) {

		byte[] bytes = null;

		File fichero = new File ( nombreFicheroCompleto );
		int lenFic = (int) fichero.length();
		bytes = new byte[ lenFic ];

		FileInputStream fis = null;
		DataInputStream dis = null;
		try {
			fis = new FileInputStream(fichero);
			dis = new DataInputStream( fis );
			dis.readFully(bytes, 0, lenFic);
		} catch (IOException e) {
			if(logVar_o_null!=null) logVar_o_null.append("\r\n" + e.getMessage());
		} finally {
			try { if (fis!=null) { fis.close(); } } catch (IOException e) {;}
		}

		return bytes;
	}
	public static boolean crtDir           ( final StringBuffer logVar_o_null, final String nombreDirectorio ) {
		boolean resultado = false;
		//////////////////////////////////
		File dir = new File( nombreDirectorio );
		if ( !dir.exists() ) dir.mkdirs();
		if ( dir.exists() && dir.canWrite() ) resultado = true;
		//////////////////////////////////
		return resultado;
	}
	public static File[] getFiles_endsWith ( File dir, final String sufijo ) {
		// Por ejemplo que acaban en ".pdf"
	    return dir.listFiles(new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().endsWith( sufijo.toLowerCase() );
	        }
	    });
	}
	public static File[] getFiles_startsWith(File dir, final String prefijo ) {
		// Por ejemplo que empiezan por "123^321^"
	    return dir.listFiles(new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	            return name.toLowerCase().startsWith( prefijo.toLowerCase() );
	        }
	    });
	}	
	public static File[] getFiles_deNombre ( File dir, final String nombre ) {
		final String nomSinExt;
    	int idx = nombre.lastIndexOf('.');
    	if ( idx > -1 ) {
    		nomSinExt = nombre.substring(0, idx);
    	} else {
    		nomSinExt = nombre;
    	}
	    return dir.listFiles(new FilenameFilter() {
	        public boolean accept(File dir, String name) {
	        	String nombreActualSinExt = name;
	        	int idx = name.lastIndexOf('.');
	        	if ( idx > -1 ) {
	        		nombreActualSinExt = name.substring(0, idx);
	        	}
	        	if ( nombreActualSinExt.equalsIgnoreCase( nomSinExt ) ) {
	        		return true;
	        	}
	            return false;
	        }
	    });
	}	
	public static void   copyFile( final String origen, final String destino) throws IOException {
        Path FROM = Paths.get(origen);
        Path TO = Paths.get(destino);
        //sobreescribir el fichero de destino, si existe, y copiar
        // los atributos, incluyendo los permisos rwx
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        };
        Files.copy(FROM, TO, options);
    }
	////////////////////////
	// FECHAS / HORAS
	public static Date addDays(Date date, int numDiasConSigno) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, numDiasConSigno);
		return cal.getTime();
	}
	public static Date addHours(Date date, int numHorasConSigno) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, numHorasConSigno);
		return cal.getTime();
	}
	public static Date addMinutes(Date date, int numMinutosConSigno) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, numMinutosConSigno);
		return cal.getTime();
	}
	
	public static String getFechaHumana() {
		return getFechaHumana(new Date());
	}
	public static String getFecha_aammdd() {
		return getFecha_aammdd(new Date());
	}
	public static String getFecha_aaaa_mm_dd() {
		return getFecha_aaaa_mm_dd(new Date());
	}
	public static String getHora_HHMMSS() {
		return getHora_HHMMSS(new Date());
	}
	public static String getHora_HHMMSSDDD() {
		return getHora_HHMMSSDDD(new Date());
	}
	public static String getFechaHumana(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.getTime().toString();
	}
	public static String getFecha_aammdd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String aaaa = "" + cal.get(Calendar.YEAR);
		String mm = padLeftCeros(cal.get(Calendar.MONTH)+1,2);
		String dd = padLeftCeros(cal.get(Calendar.DAY_OF_MONTH),2);
		return aaaa.substring(2) + mm + dd;
	}
	public static String getFecha_aaaa_mm_dd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String aaaa = "" + cal.get(Calendar.YEAR);
		String mm = padLeftCeros(cal.get(Calendar.MONTH)+1,2);
		String dd = padLeftCeros(cal.get(Calendar.DAY_OF_MONTH),2);
		return aaaa + "-" + mm + "-" + dd;
	}
	public static String getHora_HHMMSS(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String hh = padLeftCeros(cal.get(Calendar.HOUR_OF_DAY),2);
		String mm = padLeftCeros(cal.get(Calendar.MINUTE),2);
		String ss = padLeftCeros(cal.get(Calendar.SECOND),2);
		return hh + mm + ss;
	}
	public static String getHora_HHMMSSDDD(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String hh  = padLeftCeros(cal.get(Calendar.HOUR_OF_DAY),2);
		String mm  = padLeftCeros(cal.get(Calendar.MINUTE),2);
		String ss  = padLeftCeros(cal.get(Calendar.SECOND),2);
		String ddd = padLeftCeros(cal.get(Calendar.MILLISECOND),3);
		return hh + mm + ss + ddd;
	}
	public static long   getDateInMills() {
		return Calendar.getInstance().getTimeInMillis();
	}
	public static String getDateAuditoria() {
		// aammddhhMMssddd
		return getDateAuditoria(new Date());
	}
	public static String getDateAuditoria(Date date) {
		// aammddhhMMssddd
		return getFecha_aammdd(date) + getHora_HHMMSSDDD(date);
	}
	
	public static String cvtFec_dd_mm_aa__saammdd(String dd_mm_aa) {
		String res = dd_mm_aa;
		if ( res != null && res.trim().length() == 8 ) {
			res = res.trim();
			res = "1" + res.substring(6) + res.substring(3,5) + res.substring(0,2);
		}
		return res;
	}
	public static String cvtFec_dd_mm_aa__aammdd(String dd_mm_aa) {
		String res = dd_mm_aa;
		if ( res != null && res.trim().length() == 8 ) {
			res = res.trim();
			res = res.substring(6) + res.substring(3,5) + res.substring(0,2);
		}
		return res;
	}
	public static String cvtFec_saammdd__dd_mm_aa(String saammdd) {
		String res = saammdd;
		if ( res != null && res.trim().length() == 7 ) {
			res = res.trim();
			res = res.substring(5) + "/" + res.substring(3,5) + "/" + res.substring(1,3);
		}
		return res;
	}
	public static String cvtFec_dd_mm_aa__aaaa_mm_dd(String dd_mm_aa) {
		String res = dd_mm_aa;
		if ( res != null && res.trim().length() == 8 ) {
			res = res.trim();
			res = "20" + res.substring(6) + "-" + res.substring(3,5) + "-" + res.substring(0,2);
		}
		return res;
	}
	public static String cvtFec_aaaa_mm_dd__dd_mm_aa(String aaaa_mm_dd) {
		String res = aaaa_mm_dd;
		if ( res != null && res.trim().length() == 10 ) {
			res = res.trim();
			res = res.substring(8) + "/" + res.substring(5,7) + "/" + res.substring(2,4);
		}
		return res;
	}
	////////////////////////

	public static void send_http( String url, String mensaje, String resHttp_code_msg[] ) {

//		Logger logger =  Logger.newLogger("PaypalMethods|SetExpressCheckout");
		String resp = "";
		final String charset = "UTF-8";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		Map<String,String> headers = new HashMap<String, String>();

//		params.add(new BasicNameValuePair("USER", "el user"));
//		headers.put("","");
		String payload = mensaje;

		IHttpClient clientBuilder = new TLSHttpClient("TLSv1.2");
		IHttpMethods httpMethods = new ProxyHttpMethods(charset);
		try {
			resp = httpMethods.doPost(clientBuilder, url, params, headers, payload);
			//200^TOKEN=EC%2d6H461454JC6519307&TIMESTAMP=2016%2d06%2d16T22%3a43%3a57Z&CORRELATIONID=c462c0774dac4&ACK=Success&VERSION=78&BUILD=000000
			if ( resp instanceof String ) {
				String [] trozos = resp.split("\\^"); 
				if ( trozos != null && trozos.length == 2 ) {
					resHttp_code_msg[0] = trozos[0];
					resHttp_code_msg[1] = trozos[1];
				}
			}
		} catch (Exception e) {
			// System.err.println( e.getMessage() );
			// logger.a(e.getMessage()).pln();
		}
		httpMethods = null;
		// logger.a(resp).pln();
	}

	////////////////////////
	
}
