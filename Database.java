import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class Database {

	private int databaseSequenceNumber = 0;

	//changed linkedBlockingQueue to synchronizedList which is a synchronized variant of arrayList
	// one caution each time we iterate on this kind list we need to synchronize
	// for easier update process
	public List<String> stringQueue;
	
	Database() {
		
		stringQueue	=	Collections.synchronizedList(new ArrayList<String>());
		
	}
	
	
	public  int getDatabaseSequenceNumber() {
		return databaseSequenceNumber;
	}
	
	// will be implemented differently. must take LIST messages and append them
	public void update(ListMessage listMessage) {

		//TODO questions:
		//				I don't think it is incremental, Imagine I am a new arrival and the database version
		//				of all other peers is in [100, 102] I wont get the version 1 but someting in the interval
		//				I think it is the version number recieved in the List message ?
		//				Why we call it Seq#

		//Overwrite my won database
		stringQueue.set(listMessage.getPartNo(),listMessage.getData());

		//when receiving last element I remove the more than needed ones!
		//and then I update the version number of my database
		if(listMessage.getTotalParts()==listMessage.getPartNo() &&
				size()>listMessage.getTotalParts()){
			synchronized (stringQueue){
				for (int i=listMessage.getTotalParts(); i<stringQueue.size();i++)
					stringQueue.remove(i);
			}
			databaseSequenceNumber= listMessage.getSequenceNo();
		}

	}


	public int size() {
		return stringQueue.size();
	}
}
