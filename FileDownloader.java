import java.io.File;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;



public class FileDownloader implements SimpleMessageHandler, Runnable {
	
	MuxDemuxSimple md;
	
	HashMap<String, Socket> sockets = new HashMap<String, Socket>();
	
	public void run() {
		
		// take files to be downloaded from the download queue and download them from the appropriate peer, in the appropriate folder in the sharedfolder
		
		byte[] buffer = new byte[Test.FILEDOWNLOADERBUFFERSIZE]; // buffer size to be changed and adapted
		
		try {
			DownloadObject mydo = md.downloadQueue.take();
			
			Socket socketToSender;
			if(!sockets.containsKey(mydo.senderID)) {
				
				// create new socket corresponding to the sender's IP address
				
				InetAddress peerInetAddress = InetAddress.getByAddress(md.getbyID(mydo.senderID).getPeerIPAddress().getBytes());
				socketToSender = new Socket(peerInetAddress, Test.PORTNO);
				
				sockets.put(mydo.senderID, socketToSender);
				
			} else socketToSender = sockets.get(mydo.senderID);
			
			// useless sockets need to be removed... how to do that ? when peers are no longer synchronized
						
			socketToSender.getOutputStream().write(("get " + mydo.fileName).getBytes()); // send the appropriate request for mydo.fileName
			
			socketToSender.getInputStream().read(buffer);
			
			// then write buffer in the corresponding file
			
			// create folder if not exist
			File senderFolder = new File(Test.SHAREDFOLDERPATH + mydo.senderID);
			if(!senderFolder.exists()) {								
				senderFolder.mkdir();						
			}
			
			File fileToWrite = new File(Test.SHAREDFOLDERPATH + mydo.senderID + "/" + mydo.fileName);
			
			fileToWrite.createNewFile(); // not necessary
			
//			Files.write(fileToWrite.getAbsolutePath(), buffer); // append with several buffers ?
			
		} catch (Exception e) {}		
	}

	@Override
	public void handleMessage(String m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMuxDemux(MuxDemuxSimple muxDemuxSimple) {
		md = muxDemuxSimple;
		
	}
	

	
}