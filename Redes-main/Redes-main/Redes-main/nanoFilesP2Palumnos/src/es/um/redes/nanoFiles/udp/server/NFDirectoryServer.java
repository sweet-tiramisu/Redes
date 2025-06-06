package es.um.redes.nanoFiles.udp.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import es.um.redes.nanoFiles.application.NanoFiles;
import es.um.redes.nanoFiles.udp.message.DirMessage;
import es.um.redes.nanoFiles.udp.message.DirMessageOps;
import es.um.redes.nanoFiles.util.FileInfo;

public class NFDirectoryServer {
	/**
	 * Número de puerto UDP en el que escucha el directorio
	 */
	public static final int DIRECTORY_PORT = 6868;

	// Irene -> Cte para definir el tamaño del buffer, revisar.
	public final int MAX_BUF_SIZE_BYTES = 1024;

	/**
	 * Socket de comunicación UDP con el cliente UDP (DirectoryConnector)
	 */
	private DatagramSocket socket = null;

	/*
	 * TODO: Añadir aquí como atributos las estructuras de datos que sean necesarias
	 * para mantener en el directorio cualquier información necesaria para la
	 * funcionalidad del sistema nanoFilesP2P: ficheros publicados, servidores
	 * registrados, etc.
	 */

	private HashMap<String, List<FileInfo>> filesList;
	private HashMap<String, FileInfo> files;
	

	/**
	 * Probabilidad de descartar un mensaje recibido en el directorio (para simular
	 * enlace no confiable y testear el código de retransmisión)
	 */
	private double messageDiscardProbability;

	public NFDirectoryServer(double corruptionProbability) throws SocketException {
		/*
		 * Guardar la probabilidad de pérdida de datagramas (simular enlace no
		 * confiable)
		 */
		messageDiscardProbability = corruptionProbability;
		/*
		 * TODO: (Boletín SocketsUDP) Inicializar el atributo socket: Crear un socket
		 * UDP ligado al puerto especificado por el argumento directoryPort en la
		 * máquina local,
		 */
		/*
		 * TODO: (Boletín SocketsUDP) Inicializar atributos que mantienen el estado del
		 * servidor de directorio: ficheros, etc.)
		 */

		files = new HashMap<String, FileInfo>();

		// Irenee
		this.socket = new DatagramSocket(DIRECTORY_PORT);

		if (NanoFiles.testModeUDP && socket == null) {
			System.err.println("[testMode] NFDirectoryServer: code not yet fully functional.\n"
					+ "Check that all TODOs in its constructor and 'run' methods have been correctly addressed!");
			System.exit(-1);
		}
	}

	public DatagramPacket receiveDatagram() throws IOException {

		/*
		 * TODO: (Boletín SocketsUDP) Crear un búfer para recibir datagramas y un
		 * datagrama asociado al búfer (datagramReceivedFromClient)
		 */
		/*
		 * TODO: (Boletín SocketsUDP) Recibimos a través del socket un datagrama
		 */

		// Irene
		byte[] bufDatagram = new byte[MAX_BUF_SIZE_BYTES]; // El tamaño lo he definido yo, tal vez deberíamos de hacer
															// referencia a una cte ya establecida.
		DatagramPacket datagramReceivedFromClient = new DatagramPacket(bufDatagram, bufDatagram.length);
		socket.receive(datagramReceivedFromClient);

		// ya implementado
		boolean datagramReceived = false;
		while (!datagramReceived) {

			if (datagramReceivedFromClient == null) {
				System.err.println("[testMode] NFDirectoryServer.receiveDatagram: code not yet fully functional.\n"
						+ "Check that all TODOs have been correctl<y addressed!");
				System.exit(-1);
			} else {
				// Vemos si el mensaje debe ser ignorado (simulación de un canal no confiable)
				double rand = Math.random();
				if (rand < messageDiscardProbability) {
					System.err.println(
							"Directory ignored datagram from " + datagramReceivedFromClient.getSocketAddress());
				} else {
					datagramReceived = true;
					System.out
							.println("Directory received datagram from " + datagramReceivedFromClient.getSocketAddress()
									+ " of size " + datagramReceivedFromClient.getLength() + " bytes.");
				}
			}

		}

		return datagramReceivedFromClient;
	}

	public void runTest() throws IOException {

		System.out.println("[testMode] Directory starting...");

		System.out.println("[testMode] Attempting to receive 'ping' message...");
		DatagramPacket rcvDatagram = receiveDatagram();
		sendResponseTestMode(rcvDatagram);

		System.out.println("[testMode] Attempting to receive 'ping&PROTOCOL_ID' message...");
		rcvDatagram = receiveDatagram();
		sendResponseTestMode(rcvDatagram);
	}

	private void sendResponseTestMode(DatagramPacket pkt) throws IOException {
		/*
		 * TODO: (Boletín SocketsUDP) Construir un String partir de los datos recibidos
		 * en el datagrama pkt. A continuación, imprimir por pantalla dicha cadena a
		 * modo de depuración.
		 */

		/*
		 * TODO: (Boletín SocketsUDP) Después, usar la cadena para comprobar que su
		 * valor es "ping"; en ese caso, enviar como respuesta un datagrama con la
		 * cadena "pingok". Si el mensaje recibido no es "ping", se informa del error y
		 * se envía "invalid" como respuesta.
		 */

		// Irene
		// Primer TODO
		String messageFromClient = new String(pkt.getData(), pkt.getOffset(), pkt.getLength());
		System.out.println("Data received: " + messageFromClient);

		// Segundo TODO
		String response;
		if (messageFromClient.startsWith("ping&"))
			response = messageFromClient.equals("ping&".concat(NanoFiles.PROTOCOL_ID)) ? "welcome" : "denied";
		else
			response = messageFromClient.equals("ping") ? "pingok" : "invalid";

		if (response.equals("invalid"))
			System.err.println("Invalid message recieved.");
		else if (response.equals("denied"))
			System.err.println("Message received denied.");
		else
			System.out.println("Response sent: " + response);

		// Se envía la respuesta
		byte[] responseData = response.getBytes();
		DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, pkt.getAddress(),
				pkt.getPort());
		this.socket.send(responsePacket);

	}

	/*
	 * TODO: (Boletín Estructura-NanoFiles) Ampliar el código para que, en el caso
	 * de que la cadena recibida no sea exactamente "ping", comprobar si comienza
	 * por "ping&" (es del tipo "ping&PROTOCOL_ID", donde PROTOCOL_ID será el
	 * identificador del protocolo diseñado por el grupo de prácticas (ver
	 * NanoFiles.PROTOCOL_ID). Se debe extraer el "protocol_id" de la cadena
	 * recibida y comprobar que su valor coincide con el de NanoFiles.PROTOCOL_ID,
	 * en cuyo caso se responderá con "welcome" (en otro caso, "denied").
	 */

	public void run() throws IOException {

		System.out.println("Directory starting...");

		while (true) { // Bucle principal del servidor de directorio
			DatagramPacket rcvDatagram = receiveDatagram();

			sendResponse(rcvDatagram);

		}
	}

	private void sendResponse(DatagramPacket pkt) throws IOException {
		/*
		 * TODO: (Boletín MensajesASCII) Construir String partir de los datos recibidos
		 * en el datagrama pkt. A continuación, imprimir por pantalla dicha cadena a
		 * modo de depuración. Después, usar la cadena para construir un objeto
		 * DirMessage que contenga en sus atributos los valores del mensaje. A partir de
		 * este objeto, se podrá obtener los valores de los campos del mensaje mediante
		 * métodos "getter" para procesar el mensaje y consultar/modificar el estado del
		 * servidor.
		 */

		String messageFromClient = new String(pkt.getData(), pkt.getOffset(), pkt.getLength());
		System.out.println(messageFromClient);

		DirMessage dir = DirMessage.fromString(messageFromClient);

		/*
		 * TODO: Una vez construido un objeto DirMessage con el contenido del datagrama
		 * recibido, obtener el tipo de operación solicitada por el mensaje y actuar en
		 * consecuencia, enviando uno u otro tipo de mensaje en respuesta.
		 */
		String operation = dir.getOperation(); // TODO: Cambiar!

		/*
		 * TODO: (Boletín MensajesASCII) Construir un objeto DirMessage (msgToSend) con
		 * la respuesta a enviar al cliente, en función del tipo de mensaje recibido,
		 * leyendo/modificando según sea necesario el "estado" guardado en el servidor
		 * de directorio (atributos files, etc.). Los atributos del objeto DirMessage
		 * contendrán los valores adecuados para los diferentes campos del mensaje a
		 * enviar como respuesta (operation, etc.)
		 */

		switch (operation) {
		case DirMessageOps.OPERATION_PING: {

			/*
			 * TODO: (Boletín MensajesASCII) Comprobamos si el protocolId del mensaje del
			 * cliente coincide con el nuestro.
			 */
			/*
			 * TODO: (Boletín MensajesASCII) Construimos un mensaje de respuesta que indique
			 * el éxito/fracaso del ping (compatible, incompatible), y lo devolvemos como
			 * resultado del método.
			 */
			/*
			 * TODO: (Boletín MensajesASCII) Imprimimos por pantalla el resultado de
			 * procesar la petición recibida (éxito o fracaso) con los datos relevantes, a
			 * modo de depuración en el servidor
			 */
			String protocolId = dir.getProtocolId();
			String resultado;
			if (protocolId.equals(NanoFiles.PROTOCOL_ID)) {
				resultado = "pingOk";
				System.out.println(resultado);
			} else {
				resultado = "pingDenied";
				System.out.println(resultado + ": expected protocolId was " + NanoFiles.PROTOCOL_ID
						+ " but protocolId received was " + protocolId);
			}

			DirMessage msgToSend = new DirMessage(resultado);
			byte[] responseData = msgToSend.toString().getBytes();
			DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, pkt.getAddress(),
					pkt.getPort());
			this.socket.send(responsePacket);

			break;
		}
		case DirMessageOps.OPERATION_FILELIST: {
//			FileInfo[] fileList = new FileInfo[files.size()];
//			int i = 0;
//			for(FileInfo f : files.values()) {
//				fileList[i] = f;
//				i++;
//			}
//			msgToSend.setFileList(fileList);
			break;
		}

		default:
			System.err.println("Unexpected message operation: \"" + operation + "\"");
			System.exit(-1);
		}

		/*
		 * TODO: (Boletín MensajesASCII) Convertir a String el objeto DirMessage
		 * (msgToSend) con el mensaje de respuesta a enviar, extraer los bytes en que se
		 * codifica el string y finalmente enviarlos en un datagrama
		 */

	}
}
