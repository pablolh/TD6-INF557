import java.io.File;

public class SharedFolderHandler implements SimpleMessageHandler, Runnable {
	
	MuxDemuxSimple md;
	
	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(Test.FOLDERCHECKINTERVAL);
				
				File folder = new File(Test.SHAREDFOLDERPATH);

				File[] listOfFiles = folder.listFiles();

				    for (int i = 0; i < listOfFiles.length; i++) {

				      if (listOfFiles[i].isFile()) {

				        if(Test.DEBUG) System.out.println("File " + listOfFiles[i].getName());

				      } else if (listOfFiles[i].isDirectory()) {

				    	  if(Test.DEBUG) System.out.println("Directory " + listOfFiles[i].getName());

				      }

				    }
				
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
		this.md = muxDemuxSimple;
		
	}
	
}
