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

			byte[] buffer = new byte[Test.FILEDOWNLOADERBUFFERSIZE]; // buffer size to be changed and adapted

			try {
				DownloadObject mydo = md.downloadQueue.take();


				if(md.getbyID(mydo.getSenderID()).getPeerState().equals("synchronized")){

					// create new socket corresponding to the sender's IP address
					InetAddress peerInetAddress = InetAddress.getByName(md.getbyID(mydo.getSenderID()).getPeerIPAddress());
					Socket socketToSender = new Socket(peerInetAddress, Test.PORTNO);

					// send the appropriate request for mydo.fileName
					socketToSender.getOutputStream().write(("get " + mydo.getFileName()+"\n").getBytes());


					// then write buffer in the corresponding file
					socketToSender.getInputStream().read(buffer);

					String filePath = Test.ROOTFOLDERERPATH + mydo.getSenderID() + "/" + mydo.getFileName();

//					FileOutputStream out = new FileOutputStream(filePath);
//					out.write(new String(buffer).getBytes());
//					out.close();

					Path file = Paths.get(filePath);
					Files.write(file, buffer);


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