import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SharedFolderHandler implements SimpleMessageHandler, Runnable {
	
	MuxDemuxSimple md;
	
	@Override
	public void run() {
		
		while(true) {
			
			try {
				Thread.sleep(Test.FOLDERCHECKINTERVAL);
				
				File folder = new File(Test.SHAREDFOLDERPATH);

				File[] listOfFiles = folder.listFiles();
				
				List<String> names = new LinkedList<String>(); // list of the actual filenames in my shared folder
								
				// check equality between myDatabase.stringQueue and listOfFiles
				
				boolean hasChanged = false;
				
				for (File file: listOfFiles) {
					
					names.add(file.getName());
					
					// new file in the folder, not in the database ?
					
					if ( ! md.myDatabase.stringQueue.contains(file.getName())) {
						md.myDatabase.stringQueue.add(file.getName());
						
						hasChanged = true;
					}					
				}
				
				List<String> toBeRemoved = new LinkedList<String>();
				
				for (String fileName: md.myDatabase.stringQueue) {
						
					// filename has been deleted ?
					if(! names.contains(fileName)) {
						
						toBeRemoved.add(fileName);
						
						hasChanged = true;
						
					}
					
				}
				
				for(String fileName : toBeRemoved) {
					md.myDatabase.stringQueue.remove(fileName);
				}
				
				if(hasChanged) {
					if (Test.DEBUG) System.out.println("Database/shared folder update...");
					md.myDatabase.update(); // increases sequence number of my database
				}
				
				if(Test.DEBUG) {
					System.out.println("---------------\nCurrent database : seq# = " + md.myDatabase.getDatabaseSequenceNumber());
					for (String fileName: md.myDatabase.stringQueue) System.out.println("| " + fileName);
					System.out.println("---------------");
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
