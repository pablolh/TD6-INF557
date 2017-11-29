import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;




public class FileDownloader implements SimpleMessageHandler, Runnable {
	
	MuxDemuxSimple md;

	
	public void run() {

		while(true){

			// take files to be downloaded from the download queue and download them from the appropriate peer, in the appropriate folder in the sharedfolder

			try {
				DownloadObject mydo = md.downloadQueue.take();


				if(md.getbyID(mydo.getSenderID()).getPeerState().equals("synchronized")){

					// create new socket corresponding to the sender's IP address
					InetAddress peerInetAddress = InetAddress.getByName(md.getbyID(mydo.getSenderID()).getPeerIPAddress());
					Socket socketToSender = new Socket(peerInetAddress, Test.PORTNO);

					// send the appropriate request for mydo.fileName
					socketToSender.getOutputStream().write(("get " + mydo.getFileName()+"\n").getBytes());


					// then write buffer in the corresponding file
//                    BufferedReader br = new BufferedReader(new InputStreamReader(socketToSender.getInputStream()));
//					socketToSender.getInputStream().read(buffer);

//                    String line="";
					String filePath = Test.ROOTFOLDERERPATH + mydo.getSenderID() + "/" + mydo.getFileName();
//                    PrintWriter out = new PrintWriter(filePath);
//                    while (line!=null){
//                        line=br.readLine();
//                        out.write(line);
//
//                    }

                    InputStream in = socketToSender.getInputStream();

                    // Writing the file to disk
                    // Instantiating a new output stream object
                    OutputStream output = new FileOutputStream(filePath);

//                    byte[] buffer = new byte[1024];
                    
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    
                   	bufferedReader.readLine();
                    
                    int fileSizeInBytes = Integer.parseInt(bufferedReader.readLine());
                    
                    byte[] buffer = new byte[fileSizeInBytes];
                    
                    in.read(buffer);
                    
                    output.write(buffer, 0, fileSizeInBytes);

                    // Closing the FileOutputStream handle
                    output.close();

					socketToSender.close();

				}
			}
			catch (Exception e) {
				if(Test.DEBUG)
					e.printStackTrace();
			}

		}
		

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