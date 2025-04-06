package es.um.redes.nanoFiles.udp.message;

import java.util.HashMap;
import java.util.LinkedList;

import es.um.redes.nanoFiles.util.FileInfo;

/**
 * Clase que modela los mensajes del protocolo de comunicación entre pares para
 * implementar el explorador de ficheros remoto (servidor de ficheros). Estos
 * mensajes son intercambiados entre las clases DirectoryServer y
 * DirectoryConnector, y se codifican como texto en formato "campo:valor".
 * 
 * @author rtitos
 *
 */
public class DirMessage {
	public static final int PACKET_MAX_SIZE = 65507; // 65535 - 8 (UDP header) - 20 (IP header)

	private static final char DELIMITER = ':'; // Define el delimitador
	private static final char END_LINE = '\n'; // Define el carácter de fin de línea

	/**
	 * Nombre del campo que define el tipo de mensaje (primera línea)
	 */
	private static final String FIELDNAME_OPERATION = "operation";
	private static final String FIELDNAME_PROTOCOL = "protocol";
	private static final String FIELDNAME_PORT = "port";
	private static final String FIELDNAME_NFICHS = "n_fichs";
	private static final String FIELDNAME_FICHNAME = "fich_name";
	private static final String FIELDNAME_FICHHASH = "fich_hash";
	private static final String FIELDNAME_FICHSIZE = "fich_size";
	// ¿ Tengo que poner una lista para los servidores ?
	private static final String FIELDNAME_SERVER = "server";

	/*
	 * TODO: (Boletín MensajesASCII) Definir de manera simbólica los nombres de
	 * todos los campos que pueden aparecer en los mensajes de este protocolo
	 * (formato campo:valor)
	 */
	// private static final String FIELDNAME_OPERATION = "operation";

	/**
	 * Tipo del mensaje, de entre los tipos definidos en PeerMessageOps.
	 */
	private String operation = DirMessageOps.OPERATION_PING;
	/**
	 * Identificador de protocolo usado, para comprobar compatibilidad del
	 * directorio.
	 */

	/*
	 * TODO: (Boletín MensajesASCII) Crear un atributo correspondiente a cada uno de
	 * los campos de los diferentes mensajes de este protocolo.
	 */

	// Irene
	private String protocolId;
	private int serverPort;
	private String fileName;
	private FileInfo file;
	private FileInfo[] files;
	private LinkedList<Integer> servidores;

	/*
	 * TODO: (Boletín MensajesASCII) Crear diferentes constructores adecuados para
	 * construir mensajes de diferentes tipos con sus correspondientes argumentos
	 * (campos del mensaje)
	 */

	// Irene

	// Constructor
	public DirMessage(String op) {
		this.operation = op;
		this.protocolId = null;
		this.serverPort = -1;
		this.fileName = null;
		this.files = null;
		this.file = null;
		this.servidores = new LinkedList<Integer>();
	}

	// Constructor de availableFiles (filelistok -> availableFiles)
	public DirMessage(String op, FileInfo[] files, LinkedList<Integer> servidores) {
		this.operation = op;
		this.protocolId = null;
		this.serverPort = -1;
		this.fileName = null;
		this.files = files;
		this.file = null;
		this.servidores = servidores;
	}

	// Constructor de ask_infoMultipleOptions (downloadMO)
	public DirMessage(String op, FileInfo[] file) {
		this.operation = op;
		this.protocolId = null;
		this.serverPort = -1;
		this.fileName = null;
		this.files = file;
		this.file = null;
		this.servidores = new LinkedList<Integer>();
		;
	}

	// Constructor para comandos con protocolo || nombre de fichero : ping, ask_info
	public DirMessage(String op, String m) {
		if (op == "ping") {
			this.protocolId = m;
			this.fileName = null;
		} else { // En el caso de que sea el comando : ask_info
			this.protocolId = null;
			this.fileName = m;
		}
		this.operation = op;
		this.serverPort = -1;
		this.files = null;
		this.file = null;
		this.servidores = new LinkedList<Integer>();
	}

	// Constructor para comandos con puerto : serve
	public DirMessage(String op, int port) {
		this.operation = op;
		this.protocolId = null;
		this.serverPort = port;
		this.fileName = null;
		this.files = null;
		this.file = null;
		this.servidores = new LinkedList<Integer>();
	}

	// Constructor para comandos con puerto y FileInfo : upload || update
	public DirMessage(String op, int port, FileInfo file) { // Preguntar si se pueden actualizar varios ficheros al
															// mismo tiempo o tienen que ser de uno en uno.
		this.operation = op;
		this.protocolId = null;
		this.serverPort = port;
		this.fileName = null;
		this.files = null;
		this.file = file;
		this.servidores = new LinkedList<Integer>();
	}

	// Métodos de consulta -> getters
	public String getOperation() {
		return operation;
	}

	public FileInfo getFile() {
		return file;
	}

	public String getFileName() {
		return fileName;
	}

	public FileInfo[] getFiles() {
		return files;
	}

	public int getServerPort() {
		return serverPort;
	}

	public LinkedList<Integer> getServidores() {
		return servidores;
	}

	/*
	 * TODO: (Boletín MensajesASCII) Crear métodos getter y setter para obtener los
	 * valores de los atributos de un mensaje. Se aconseja incluir código que
	 * compruebe que no se modifica/obtiene el valor de un campo (atributo) que no
	 * esté definido para el tipo de mensaje dado por "operation".
	 */

	// Métodos de establecimiento -> setters
	public void setServerPort(int serverPort) {
		if (!operation.equals(DirMessageOps.OPERATION_SERVE)) {
			throw new RuntimeException(
					"DirMessage: setServerPort called for message of unexpected type (" + operation + ")");
		}
		this.serverPort = serverPort;
	}

	public void setProtocolID(String protocolIdent) {
		if (!operation.equals(DirMessageOps.OPERATION_PING)) {
			throw new RuntimeException(
					"DirMessage: setProtocolId called for message of unexpected type (" + operation + ")");
		}
		protocolId = protocolIdent;
	}

	public void setFileName(String fileName) {
		if (!operation.equals(DirMessageOps.OPERATION_ASK_INFO)) {
			throw new RuntimeException(
					"DirMessage: setFileName called for message of unexpected type (" + operation + ")");
		}
		this.fileName = fileName;
	}

	public void setFile(FileInfo file) {
		if (!operation.equals(DirMessageOps.OPERATION_UPLOAD) || !operation.equals(DirMessageOps.OPERATION_UPDATE)) {
			throw new RuntimeException("DirMessage: setFile called for message of unexpected type (" + operation + ")");
		}
		this.file = file;
	}

	public void setFiles(FileInfo[] files) {
		if (!operation.equals(DirMessageOps.OPERATION_ASK_INFO_MULTIPLEOPTIONS)) {
			throw new RuntimeException(
					"DirMessage: setFiles called for message of unexpected type (" + operation + ")");
		}
		this.files = files;
	}

	public void setServidores(LinkedList<Integer> servidores) {
		if (!operation.equals(DirMessageOps.OPERATION_AVAILABLE_FILES)) {
			throw new RuntimeException(
					"DirMessage: setServidores called for message of unexpected type (" + operation + ")");
		}
		this.servidores = servidores;
	}

	public String getProtocolId() {

		return protocolId;
	}

	/**
	 * Método que convierte un mensaje codificado como una cadena de caracteres, a
	 * un objeto de la clase PeerMessage, en el cual los atributos correspondientes
	 * han sido establecidos con el valor de los campos del mensaje.
	 * 
	 * @param message El mensaje recibido por el socket, como cadena de caracteres
	 * @return Un objeto PeerMessage que modela el mensaje recibido (tipo, valores,
	 *         etc.)
	 */
	public static DirMessage fromString(String message) {
		/*
		 * TODO: (Boletín MensajesASCII) Usar un bucle para parsear el mensaje línea a
		 * línea, extrayendo para cada línea el nombre del campo y el valor, usando el
		 * delimitador DELIMITER, y guardarlo en variables locales.
		 */

		// System.out.println("DirMessage read from socket:");
		// System.out.println(message);
		String[] lines = message.split(END_LINE + "");
		// Local variables to save data during parsing
		DirMessage m = null;

		for (String line : lines) {
			int idx = line.indexOf(DELIMITER); // Posición del delimitador
			String fieldName = line.substring(0, idx).toLowerCase(); // minúsculas
			String value = line.substring(idx + 1).trim();

			switch (fieldName) {
			case FIELDNAME_OPERATION: {
				assert (m == null);
				m = new DirMessage(value);
				break;
			}

			default:
				System.err.println("PANIC: DirMessage.fromString - message with unknown field name " + fieldName);
				System.err.println("Message was:\n" + message);
				System.exit(-1);
			}
		}

		return m;
	}

	/**
	 * Método que devuelve una cadena de caracteres con la codificación del mensaje
	 * según el formato campo:valor, a partir del tipo y los valores almacenados en
	 * los atributos.
	 * 
	 * @return La cadena de caracteres con el mensaje a enviar por el socket.
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append(FIELDNAME_OPERATION + DELIMITER + this.operation + END_LINE); // Construimos el campo
		/*
		 * TODO: (Boletín MensajesASCII) En función de la operación del mensaje, crear
		 * una cadena la operación y concatenar el resto de campos necesarios usando los
		 * valores de los atributos del objeto.
		 */

		// Ireneee
		switch (operation) {
		case DirMessageOps.OPERATION_PING: {
			sb.append(DirMessage.FIELDNAME_PROTOCOL + DELIMITER + this.protocolId + END_LINE); // ¿El 'this. ' es necesario?
			break;
		}
		case DirMessageOps.OPERATION_PING_OK:
		case DirMessageOps.OPERATION_PING_DENIED :
		case DirMessageOps.OPERATION_FILELIST :
		case DirMessageOps.OPERATION_FILELIST_ERROR :
		case DirMessageOps.OPERATION_SERVE_OK :
		case DirMessageOps.OPERATION_SERVE_DENIED :
		case DirMessageOps.OPERATION_ASK_INFO_FILENOTFOUND :
		case DirMessageOps.OPERATION_UPLOAD_OK :
		case DirMessageOps.OPERATION_UPLOAD_DUPLICATE : 
		case DirMessageOps.OPERATION_UPDATE_OK :
		case DirMessageOps.OPERATION_UPDATE_DENIED :
		case DirMessageOps.OPERATION_UPDATE_FILENOTFOUND :
		case DirMessageOps.OPERATION_QUIT :
		case DirMessageOps.OPERATION_QUIT_OK : {
			break; // No tiene más información
		}
		case DirMessageOps.OPERATION_AVAILABLE_FILES: {
			sb.append(DirMessage.FIELDNAME_NFICHS + DELIMITER + this.files + END_LINE);
			for(FileInfo f : this.files) {
				sb.append(DirMessage.FIELDNAME_FICHNAME + DELIMITER + f.getFileName() + END_LINE);
				sb.append(DirMessage.FIELDNAME_FICHHASH + DELIMITER + f.getFileHash() + END_LINE);
				sb.append(DirMessage.FIELDNAME_FICHSIZE + DELIMITER + f.getFileSize() + END_LINE);
				for(int i = 0; i < this.servidores.size(); i++) {
					sb.append(DirMessage.FIELDNAME_SERVER + DELIMITER + this.servidores.get(i) + END_LINE);
				}
			}
			break;
		}
		

		}

		sb.append(END_LINE); // Marcamos el final del mensaje
		return sb.toString();
	}

}
