/* Le Hénaff Pablo ; Basudan Hossam*/
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class Database {

	private int databaseSequenceNumber = 0;

	//changed linkedBlockingQueue to synchronizedList which is a synchronized variant of arrayList
	// one caution each time we iterate on this kind list we need to synchronize
	// I use it to keep the order of received messages
	public List<String> stringQueue;
	
	Database(int size) {
		
		stringQueue	=	Collections.synchronizedList(new ArrayList<String>(size));
//		for(int i = 0 ; i<size ; i++){
//			stringQueue.add("");
//		}
		
	}
	
	
	public  int getDatabaseSequenceNumber() {
		return databaseSequenceNumber;
	}
	
	// will be implemented differently. must take LIST messages and append them
	public void update(String str) {

            stringQueue.add(str);
			databaseSequenceNumber++;

	}

	@Override
	public String toString() {
		return "Database{" +
				"databaseSequenceNumber=" + databaseSequenceNumber +
				", stringQueue=" + stringQueue +
				'}';
	}

	public int size() {
		return stringQueue.size();
	}

	public void setDatabaseSequenceNumber(int databaseSequenceNumber){
	    this.databaseSequenceNumber=databaseSequenceNumber;
    }

    public String toStringofStringQueue(){
		String res = "";
		for(String str : stringQueue){
			res+="*****"+str+"\n";
		}
		return res;
	}
}
