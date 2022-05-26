package minigame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@SuppressWarnings("serial")
public class Client extends PageManager{
	SocketChannel client;
    ByteBuffer buffer;
    String curuser =new String("");
    //using pageManger.conde
	
    public Client() {
        
    }
	
	public void init(String ip, int port) throws IOException{
	
        client = SocketChannel.open(new InetSocketAddress(ip, port));
        buffer = ByteBuffer.allocate(2048);
        
	}
	
	public void close() throws IOException{
		this.client.close();
	}

    public void sendMessage(String msg, String user, String code) {
        //buffer = ByteBuffer.wrap(msg.getBytes());
        try {
            writeChunkData(buffer, client, msg, user, code);
            System.out.println("Client:: user=" + user + " sendMessage="+msg);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String receiveMessage() {
    	String response = null;
        try {
            buffer.clear();
            while((response = readChunkData(buffer, client)) == "") {
            	Thread.sleep(500);
            }
            System.out.println("Client::receiveMessage=" + response);
            buffer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	
    public String readChunkData(ByteBuffer buffer, SocketChannel client) throws IOException{		
		ByteBuffer hbuffer = ByteBuffer.allocate(8);
		buffer.clear();
		
		
		int rlen = client.read(hbuffer);
		 if (rlen == -1) {
			 throw new IOException("Socket closed");
		 }
		 else if(rlen == 0) {
			 return "";
		 }
		 else if (rlen != 8) {
			 String response = new String(hbuffer.array()).trim();
	         System.out.println("Packet Head Corruption response=" + response + " hlen: " + rlen);
			 throw new IOException("Packet Head Corruption");
		 }
		 PageManager.code = new String(hbuffer.array(), 0, 4);
		 int len =  Integer.parseInt((new String(hbuffer.array())).substring(4));	 
		 
		 len -= 64;		 
		 int size = 0;

		 //read data length	 
		 while(size < len) {
 			 if((rlen = client.read(buffer)) == -1) {
				 throw new IOException("Socket closed");
			 }
			 size += rlen;
		 }
		 
		 //make read mode
		 buffer.flip();
		 int curr = buffer.position();
		 this.curuser = new String(buffer.array(), curr, curr+64);
		 String msg = new String(buffer.array(), curr+64, curr+len);
		 return msg.trim();
	}
	
	
	public void writeChunkData(ByteBuffer buffer, SocketChannel client, String data, String user, String code) throws IOException{
		//make app data head
		//System.out.println(data);
		String hlen = String.format("%04d", data.trim().length() + 64) ;
		String data_head = code + hlen;
		byte pad = 0x00;
		//System.out.println(data);
		
		buffer.put(data_head.getBytes());
		buffer.put(user.getBytes());
		int rest = 64 - user.getBytes().length;
		for(int i=0; i < rest; i++) buffer.put(pad);
		buffer.put(data.getBytes());

		buffer.flip();
		//System.out.println(buffer);
		//System.out.println(byteToString(buffer.array()));
		while(buffer.hasRemaining()) {
			client.write(buffer);
		}
	}
	
	public String byteToString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b: bytes) {
			sb.append(String.format("%02X", b&0xff));
		}
		return sb.toString();
	}
	
	
}
