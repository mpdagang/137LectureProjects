/*
Marion Paulo A. Dagang
December 14, 2016
*/
import java.io.*;
import java.net.*;
import java.util.*;

class UDPServer {
	public static void main(String args[]) throws Exception {

		int dropRate = 0;
		int ws = 5;
		String message = "Test Message";


		ArrayList<Integer> clients = new ArrayList<Integer>();
		int serverPort = 9876;
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		DatagramSocket serverSocket = new DatagramSocket(serverPort);
		

		Arrays.fill(receiveData, (byte)0);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		String sentence = new String(receivePacket.getData());
		Thread.sleep(2000);
		System.out.println("FROM CLIENT - " + sentence.trim());

		InetAddress IPAddress = receivePacket.getAddress();
		int port = receivePacket.getPort();
		SimpleTCP SimpleTCP = new SimpleTCP(0,0,1,1,0,0);
		sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);

		Arrays.fill(receiveData, (byte)0);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		sentence = new String(receivePacket.getData());
		Thread.sleep(2000);
		System.out.println("FROM CLIENT - " + sentence.trim());


		System.out.println("Sending string \""+message+".\" to client.");
		message += ".";

		byte[] messageBytes = message.getBytes();


		byte[] header = new byte[512];
		byte[] data = new byte[512];
		int sq = 0;
		for(int i=0; i<messageBytes.length; ){
			

			Thread.sleep(2000);
			SimpleTCP SimpleTCPH = new SimpleTCP(sq++,0,0,0,0,0);
			Arrays.fill(header, (byte)0);
			Arrays.fill(data, (byte)0);
			byte[] headerTemp = SimpleTCPH.stringify().getBytes();


			byte[] dataTemp = Arrays.copyOfRange(messageBytes, i, i+ws);



			for(int j=0; j<headerTemp.length;j++){
				header[j] = headerTemp[j];
			}
			for(int j=0; j<dataTemp.length; j++){
				data[j] = dataTemp[j];
			}


			//packet dropping
			Random r = new Random();
			if(r.nextInt(100) >= dropRate){
				sendData = concat(header, data);
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);	
			}
			else{
				System.out.println("Dropping packet.");
			}


			//Thread.sleep(4000);

			serverSocket.setSoTimeout(4000);   // set the timeout in millisecounds.

	        while(true){        // recieve data until timeout
	            try {
					Arrays.fill(receiveData, (byte)0);
	                serverSocket.receive(receivePacket);
					SimpleTCPH = decode((new String(receivePacket.getData())).trim());
					Thread.sleep(2000);
					System.out.println("FROM CLIENT - " + SimpleTCPH.stringify());
	                i+=ws;
	                break;

	            }
	            catch (SocketTimeoutException e) {
	                System.out.println("Timeout reached. Resending Data.");
	                sq--;
	                break;
	            }
	        }
			
	       
		}

		//recieve fin from client
		Arrays.fill(receiveData, (byte)0);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		SimpleTCP SimpleTCPH = decode((new String(receivePacket.getData())).trim());
		Thread.sleep(2000);
		System.out.println("FROM CLIENT");
		System.out.println(SimpleTCPH.stringify());


		//send ack to client
		SimpleTCP = new SimpleTCP(0,0,1,0,0,0);
		sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);

		
		Thread.sleep(2000);

		//send fin to client
		SimpleTCP = new SimpleTCP(0,0,0,0,1,0);
		sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		serverSocket.send(sendPacket);


		//recieve ack from server
		Arrays.fill(receiveData, (byte)0);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		serverSocket.receive(receivePacket);
		SimpleTCPH = decode((new String(receivePacket.getData())).trim());
		Thread.sleep(2000);
		System.out.println("FROM CLIENT");
		System.out.println(SimpleTCPH.stringify());


		serverSocket.close();
		System.out.println("Connection closed");
		

	}

	public static byte[] concat(byte[] a, byte[] b) {
	   int aLen = a.length;
	   int bLen = b.length;
	   byte[] c= new byte[aLen+bLen];
	   System.arraycopy(a, 0, c, 0, aLen);
	   System.arraycopy(b, 0, c, aLen, bLen);
	   return c;
	}

	public static SimpleTCP decode(String s){
		String parts[] = s.split(",");
		return new SimpleTCP(Integer.parseInt(parts[0].split(":")[1].trim()), Integer.parseInt(parts[1].split(":")[1].trim()), Integer.parseInt(parts[2].split(":")[1].trim()), Integer.parseInt(parts[3].split(":")[1].trim()), Integer.parseInt(parts[4].split(":")[1].trim()), Integer.parseInt(parts[5].split(":")[1].trim()));
	}
}