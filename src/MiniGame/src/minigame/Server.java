package minigame;



import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;


import java.nio.ByteBuffer;


public class Server {
	Selector selector;
	ServerSocketChannel serverSocket;
	ByteBuffer buffer;
	
	public Server() {
		
	}
	
	public void init(int port) throws IOException{
		selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(2048);

        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {

                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }

                if (key.isReadable()) {
                	//have to change this part
                    answerWithEcho(buffer, key);
                }
                iter.remove();
            }
        }

	}
	
	private void answerWithEcho(ByteBuffer buffer, SelectionKey key) throws IOException {
		 
	        SocketChannel client = (SocketChannel) key.channel();
	        readChunkData(buffer, client);
	        
	        
	        //client.read(buffer);
	        if (new String(buffer.array()).trim().equals("POISON_PILL")) {
	            client.close();
	            System.out.println("Not accepting client messages anymore");
	        }
	        else {
	        	String data = new String(buffer.array(), buffer.position(), buffer.limit()).trim();
	        	buffer.clear();
	        	System.out.println(data);
	        	writeChunkData(buffer, client, data);
	            buffer.clear();
	        }
	}

    private void register(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {
 
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }
	
	public void readChunkData(ByteBuffer buffer, SocketChannel client) throws IOException{
		 //SocketChannel client = (SocketChannel) key.channel();
		 //read head
		 ByteBuffer hbuffer = ByteBuffer.allocate(8);
		 int rlen = client.read(hbuffer);
		 if (rlen == -1) {
			 throw new IOException("Socket closed");
		 }
		 else if (rlen != 8) {
			 String response = new String(hbuffer.array()).trim();
	         System.out.println("response=" + response + " hlen: " + rlen);
			 throw new IOException("Packet Head Corruption");
		 }
		 int len =  Integer.parseInt((new String(hbuffer.array())).substring(4));
		 int size = 0;
		 
		 //read data length
		 //buffer = ByteBuffer.allocate(2048);
		 while(size < len) {
			 if((rlen = client.read(buffer)) == -1) {
				 throw new IOException("Socket closed");
			 }
			 size += rlen;
		 }
		 
		 //make read mode
		 buffer.flip();
		 
	}
	
	public void writeChunkData(ByteBuffer buffer, SocketChannel client, String data) throws IOException{
		//SocketChannel client = (SocketChannel) key.channel();

		//make app data head
		System.out.println(data);
		String hlen = String.format("%04d", data.trim().length());
		data = "0000" + hlen + data;
		//System.out.println(data);
		
		buffer.put(data.getBytes());
		buffer.flip();
		
		
		while(buffer.hasRemaining()) {
			client.write(buffer);
		}
	}
	
	
}
