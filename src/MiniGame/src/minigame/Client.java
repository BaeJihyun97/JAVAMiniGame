package minigame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;


public class Client {
	private static SocketChannel client;
    private static ByteBuffer buffer;
	
    public Client() {
        
    }
	
	public void init(String ip, int port) throws IOException{
	
        client = SocketChannel.open(new InetSocketAddress(ip, port));
        buffer = ByteBuffer.allocate(2048);
        
	}

    public String sendMessage(String msg) {
        //buffer = ByteBuffer.wrap(msg.getBytes());
        String response = null;
        try {
            //client.write(buffer);
            writeChunkData(buffer, client, msg);
            buffer.clear();
            //client.read(buffer);
            readChunkData(buffer, client);
            response = new String(buffer.array(), buffer.position(), buffer.limit()).trim();
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }
	
    public void readChunkData(ByteBuffer buffer, SocketChannel client) throws IOException{
		 //read head
		 ByteBuffer hbuffer = ByteBuffer.allocate(8);
		 buffer.clear();
		 int rlen = client.read(hbuffer);
		 if (rlen == -1) {
			 throw new IOException("Socket closed");
		 }
		 else if (rlen != 8) {
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
		 System.out.println(size);
		 //make read mode
		 buffer.flip();
		 
	}
	
	public void writeChunkData(ByteBuffer buffer, SocketChannel client, String data) throws IOException{
		
		//make app data head
		String hlen = String.format("%04d", data.length());
		data = "0000" + hlen + data;
		//System.out.println(data);
		
		buffer.put(data.getBytes());
		buffer.flip();
		
		
		while(buffer.hasRemaining()) {
			client.write(buffer);
		}
		buffer.clear();
	}
	

}
