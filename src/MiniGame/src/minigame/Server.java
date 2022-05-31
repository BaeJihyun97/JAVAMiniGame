package minigame;



import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


import java.nio.ByteBuffer;

@SuppressWarnings("serial")
public class Server extends PageManager{
	Selector selector;
	ServerSocketChannel serverSocket;
	ByteBuffer buffer;
	ArrayList<String> users = new ArrayList<String>();
	static int count = 0;
	String code = "";
	char[] stateArr = new char[361];
	char turn = '0';
	int gamestate = 0;
	int done = 0;
	String curuser;
	String winner;
	
	public Server() {
		
	}
	
	public void init(int port) throws IOException{
		selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
        serverSocket.bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(2048);
        for(int i=0; i < 361; i++) {
        	stateArr[i] = '0';
        }
        System.out.println("Server running...");

	}
	
	public void closeS() throws IOException {
		try {

		    Iterator<SelectionKey> keys = this.selector.keys().iterator();

		    while(keys.hasNext()) {

		        SelectionKey key = keys.next();

		        SelectableChannel channel = key.channel();

		        if(channel instanceof SocketChannel) {

		            SocketChannel socketChannel = (SocketChannel) channel;
		            Socket socket = socketChannel.socket();
		            String remoteHost = socket.getRemoteSocketAddress().toString();

		            System.out.println("closing socket "+ remoteHost);

		            try {

		                socketChannel.close();

		            } catch (IOException e) {

		            	System.out.println("Exception while closing socket");
		            	e.printStackTrace();
		            }

		            key.cancel();
		        }
		    }

		    System.out.println("closing selector");
		    selector.close();

		} catch(Exception ex) {

			System.out.println("Exception while closing selector");
		}
		
		if(this.serverSocket != null && this.serverSocket.isOpen()) {

		    try {

		        this.serverSocket.close();

		    } catch (IOException e) {

		    	System.out.println("Exception while closing server socket");
		    }
		}
	}
	
	private void answerWithEcho(ByteBuffer buffer, SocketChannel client, String user) throws IOException {
       
	        if (new String(buffer.array()).trim().equals("POISON_PILL")) {
	            client.close();
	            System.out.println("Not accepting client messages anymore");
	        }
	        else {
	        	String data = new String("connection allowed" + Integer.toString(PageManager.connection)).trim();
	        	buffer.clear();
	        	writeChunkData(buffer, client, data, user, "0000");
	            buffer.clear();
	        }
	}

    private void register(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {
 
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE; 
        client.register(selector, interestSet);
    }
    
    public String byteToString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b: bytes) {
			sb.append(String.format("%02X", b&0xff));
		}
		return sb.toString();
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
	         System.out.println("Server::Packet Head Corruption response=" + response + " hlen: " + rlen);
			 //throw new IOException("Packet Head Corruption");
		 }
		 try {
			 this.code = new String(hbuffer.array(), 0, 4);
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
		 catch (Exception e){
			 //System.out.println(new String(buffer.array(), curr+64, curr+len));
			 e.printStackTrace();
			 return "";
		 }
		  
	}
	
	
	
	public void writeChunkData(ByteBuffer buffer, SocketChannel client, String data, String user, String code) throws IOException{
		//SocketChannel client = (SocketChannel) key.channel();
		System.out.println("System::sendmsg=" + data);
		buffer.clear();
		//make app data head
		//System.out.println(data);
		String hlen = String.format("%04d", data.trim().length() + 64);
		String data_head = code + hlen;
		byte pad = 0x00;
		//System.out.println(data + " " + data_head);
		
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
	
	public void updateGo(String msg) {
		char[] state = msg.toCharArray();
		System.out.println("System::received=" + msg);
		this.turn = state[1] == '1' ? '2':'1';
		for(int i=0; i < 361; i++) {
			this.stateArr[i] = state[i+2];
		}
	}
	
	public void sendGomsg(SocketChannel client, String user) throws IOException{		
		String smsg = String.valueOf(this.turn) + "0" + String.valueOf(stateArr);
		writeChunkData(this.buffer, client, smsg, user, "2003");
	}

	
	public void startS () throws Exception{
		while (!PageManager.over) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            SocketChannel client = null;
            String user = new String("");
            while (iter.hasNext()) {

                SelectionKey key = iter.next();         
                try {
	                if (key.isAcceptable()) {
	                    register(selector, serverSocket);             
	                }
                
                
                	if (key.isReadable()) {
                    	client = (SocketChannel) key.channel();
            	        
            	        String msg = readChunkData(buffer, client);
            	        user = this.curuser;
            	        if (msg == "") continue; //non blocking so it just return 0 when there is no packets
            	        //System.out.println(msg);
                    	if(PageManager.gameN == 6) {
                    		if(PageManager.connection < 2 && msg.equals("hello")) { //before game start
                    			PageManager.connection += 1;
                    			this.users.add(user);
                    			
                    			System.out.println("Server::connections : " +PageManager.connection);
                    			buffer.clear();
                        		answerWithEcho(buffer, client, user);
                        		if(PageManager.connection == 2) {   
                        			this.gamestate = 1;
                        			Thread.sleep(100);
                        			writeChunkData(buffer, client, "start", "Server", "2001");
                        			this.done += 1;
                        			iter.remove();
//                        			while(iter.hasNext()) {
//                        				SelectionKey key2 = iter.next();
//                        				System.out.println(i);
//                        				if(key2.isWritable()) {
//                        					SocketChannel clientr = (SocketChannel) key2.channel();
//                            				writeChunkData(buffer, clientr, "start", "Server", "2001");                              				
//                        				}
//                        				iter.remove();
//                        				this.done += 1;
//                        				if (this.done == 2) this.gamestate = 3;
//                        			}
                        			this.turn = '1';
                        			System.out.println("Server::game2 start!!");
                        			continue;
                        		}
                   
                    		}
                    		else if(user != "" && users.contains(user) && PageManager.connection == 2){ //running on the game
                    			//implement game logic
                    			//System.out.println("System:: code " + this.code + " msg= " +msg);
                    			if(this.code.equals("2003")) {
                    				updateGo(msg);
                    				//System.out.println("Server::recivedmsg="+msg);
                        			sendGomsg(client, user);
                    			}
                    			else if(this.code.equals("2004")) {
                    				updateGo(msg);
                    				this.gamestate = 4;
                    				writeChunkData(buffer, client, user, "Server", "2004");
                    				this.winner = user;
                    				this.done += 1;
                    				client.close();
                            		iter.remove();                        			
                        			continue;
                    				
                    			}
                    		}	
                        	else { //out of connection
                        		System.out.println(user + " " + users.contains(user));
                        		Iterator<String> iterator = users.iterator();
                                while (iterator.hasNext()) {
                                    System.out.print(iterator.next() + "  ");
                                }
                        		writeChunkData(buffer, client, "connection is refused", "Server", "0000");
                        		client.close();
                        		continue;
                        	}
                      	
                        }
                        
                        else { //if not page6
                        	PageManager.page = 1;
                        	return;
                        }
           	
                    }
                	else if (key.isWritable()) {
                		client = (SocketChannel) key.channel();
                		if(this.gamestate == 1) {
                			writeChunkData(buffer, client, "start", "Server", "2001");
                			this.done += 1;
            				if (this.done == 2) this.gamestate = 3; this.done = 0;
                		} 
                		else if(this.gamestate == 3) {
                			sendGomsg(client, user);
                		}
                		else if(this.gamestate == 4) {
                			writeChunkData(buffer, client, this.winner, "Server", "2004");
                			this.done += 1;
                			if (this.done == 2) { 
                				this.gamestate = 3; 
                				this.done = 0;
                				PageManager.over = true;
                			}
                			client.close();
                		}
	                }
                	
                }
                catch(Exception e) {
                	count += 1;
                	if(client != null) {
                		e.printStackTrace();
                		
                		PageManager.connection = 0;
                		//PageManager.page = 1;
                		PageManager.over = true;

                		client.close();
                		iter.remove();
            			while(iter.hasNext()) {
            				//System.out.println(i);
            				SelectionKey keyr = iter.next();
            				if(key.isWritable()) {
            					SocketChannel clientr = (SocketChannel) keyr.channel();
                				writeChunkData(buffer, clientr, "end", "Server", "2002");
                				clientr.close();
            				}
            				iter.remove();
            			}
            			
            			continue;
                	}
                	else e.printStackTrace();
                }

                
                iter.remove();
            }
            
            if(count > 10) return;
            Thread.sleep(500);
		}
		
		PageManager.page = 1;
		closeS();
		System.out.println("Server close.");
	}
	
	
}
