import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class FileServer implements Runnable {
	
	ServerSocket serverSocket;
	
	FileServer() {
		
		try {
			serverSocket = new ServerSocket(Test.PORTNO); // same port number but over TCP instead of UDP
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	@Override
	public void run() {
		
		byte[] buffer = new byte[255]; // buffer size can be increased
		
		while(true) {
			
			try {
				Socket connectionSocket = serverSocket.accept();
				
				if(Test.DEBUG) System.out.println("TCP connection made with host " + connectionSocket.getInetAddress() );
				
				InputStream connectionInputStream = connectionSocket.getInputStream();
				OutputStream connectionOutputStream = connectionSocket.getOutputStream();

				
				connectionInputStream.read(buffer); // blocks until data is received
				
				String message = new String(buffer); // create a String out of the buffer
				
				String[] tmp = message.trim().split(" +", 2);
				
				if(tmp[0].equals("get")) {
					
					if(Test.DEBUG) System.out.println("checking that file " + tmp[1] + " exists in my shared folder...");
					
					File toBeServed = new File(Test.SHAREDFOLDERPATH + tmp[1]);
					if(toBeServed.exists()) { // check file actually exists in shared folder
						
						if(Test.DEBUG) System.out.println("Serving file...");
						
						connectionOutputStream.write((tmp[1] + "\n" + toBeServed.length()+"\n").getBytes());
						connectionOutputStream.write(Files.readAllBytes(toBeServed.toPath())); // second call to write = OK ???
						
					}

					serverSocket.close();
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
		}
	}

}
