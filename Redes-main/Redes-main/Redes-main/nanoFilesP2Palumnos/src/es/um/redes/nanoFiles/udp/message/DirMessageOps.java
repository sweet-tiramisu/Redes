package es.um.redes.nanoFiles.udp.message;

public class DirMessageOps {

	/*
	 * TODO: (Boletín MensajesASCII) Añadir aquí todas las constantes que definen
	 * los diferentes tipos de mensajes del protocolo de comunicación con el
	 * directorio (valores posibles del campo "operation").
	 */

	// OPERACIONES
	public static final String OPERATION_INVALID = "invalid_operation";
	public static final String OPERATION_PING = "ping";
	public static final String OPERATION_ERROR = "error";
	public static final String OPERATION_FILELIST = "filelist";
	public static final String OPERATION_SERVE = "serve";
	public static final String OPERATION_ASK_INFO = "askinfo";
	public static final String OPERATION_UPLOAD = "upload";
	public static final String OPERATION_UPDATE = "update";
	public static final String OPERATION_QUIT = "quit";
	public static final String OPERATION_STOPSERVER = "stopserver";
	
	// RESPUESTAS
	public static final String OPERATION_PING_OK = "pingok";
	public static final String OPERATION_PING_DENIED = "pingdenied";
	public static final String OPERATION_AVAILABLE_FILES = "availablefiles";
	public static final String OPERATION_FILELIST_ERROR = "filelisterror";
	public static final String OPERATION_SERVE_OK= "serveok";
	public static final String OPERATION_SERVE_DENIED = "servedenied";
	public static final String OPERATION_ASK_INFO_OK = "askinfook";
	public static final String OPERATION_ASK_INFO_MULTIPLEOPTIONS = "askinfomultipleoptions";
	public static final String OPERATION_ASK_INFO_FILENOTFOUND = "askinfofilenotfound";
	public static final String OPERATION_UPLOAD_OK = "uploadok";
	public static final String OPERATION_UPLOAD_DUPLICATE = "uploadduplicate";
	public static final String OPERATION_UPDATE_OK = "updateok";
	public static final String OPERATION_UPDATE_DENIED = "updatedenied";
	public static final String OPERATION_UPDATE_FILENOTFOUND = "updatefilenotfound";
	public static final String OPERATION_QUIT_OK = "quitok";
	public static final String OPERATION_STOPSERVER_OK = "stopserverok";
	
	

}
