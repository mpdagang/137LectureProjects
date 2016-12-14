/*
Marion Paulo A. Dagang
December 14, 2016
*/
import java.io.*;
import java.net.*;
import java.util.*;

class UDPClient {
	public static void main(String args[]) throws Exception {

		int serverPort = 9876;

		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];


		SimpleTCP SimpleTCP = new SimpleTCP(0,0,0,1,0,0);
		String sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
		clientSocket.send(sendPacket);
		

		Arrays.fill(receiveData, (byte)0);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		Thread.sleep(2000);
		System.out.println("FROM SERVER - " + modifiedSentence.trim());

		SimpleTCP = new SimpleTCP(0,0,1,0,0,0);
		sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
		clientSocket.send(sendPacket);

		byte[] header = new byte[512];
		byte[] data = new byte[512];
		while(true){

			Arrays.fill(receiveData, (byte)0);
			receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			byte[] temp = receivePacket.getData();

			Arrays.fill(header, (byte)0);
			Arrays.fill(data, (byte)0);
			header = Arrays.copyOfRange(temp, 0, 512);
			data = Arrays.copyOfRange(temp, 512, 1024);


			SimpleTCP SimpleTCPH = decode((new String(header)).trim());


			Thread.sleep(2000);
			System.out.println("");
			System.out.println("FROM SERVER");
			System.out.println(SimpleTCPH.stringify());
			System.out.print((new String(data)).trim());
			System.out.println("");


			SimpleTCP = new SimpleTCP(0,SimpleTCPH.seqNum,0,0,0,0);
			sentence = SimpleTCP.stringify();
			sendData = sentence.getBytes();
			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
			clientSocket.send(sendPacket);


			//sentence is finished send fin to server
			if((new String(data)).indexOf(".") != -1){
				break;
			}

		}

		
		Thread.sleep(2000);
		System.out.println("Start closing connection");
		Thread.sleep(2000);


		//send fin to server
		SimpleTCP = new SimpleTCP(0,0,0,0,1,0);
		sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
		clientSocket.send(sendPacket);

		//recieve ack from server
		Arrays.fill(receiveData, (byte)0);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		SimpleTCP SimpleTCPH = decode((new String(receivePacket.getData())).trim());
		Thread.sleep(2000);
		System.out.println("FROM SERVER");
		System.out.println(SimpleTCPH.stringify());

		//recieve fin from server
		Arrays.fill(receiveData, (byte)0);
		receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		SimpleTCPH = decode((new String(receivePacket.getData())).trim());
		Thread.sleep(2000);
		System.out.println("FROM SERVER");
		System.out.println(SimpleTCPH.stringify());

		//send ack to server
		SimpleTCP = new SimpleTCP(0,0,1,0,0,0);
		sentence = SimpleTCP.stringify();
		sendData = sentence.getBytes();
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, serverPort);
		clientSocket.send(sendPacket);

		Thread.sleep(10000);

		clientSocket.close();
		System.out.println("Connection closed");


	}
	public static SimpleTCP decode(String s){
		String parts[] = s.split(",");
		return new SimpleTCP(Integer.parseInt(parts[0].split(":")[1].trim()), Integer.parseInt(parts[1].split(":")[1].trim()), Integer.parseInt(parts[2].split(":")[1].trim()), Integer.parseInt(parts[3].split(":")[1].trim()), Integer.parseInt(parts[4].split(":")[1].trim()), Integer.parseInt(parts[5].split(":")[1].trim()));
	}


}