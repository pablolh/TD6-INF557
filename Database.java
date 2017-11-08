import java.util.concurrent.LinkedBlockingDeque;

public class Database {
	
	private int databaseSequenceNumber = 0;
	
	private LinkedBlockingDeque<String> stringQueue;
	
	Database() {
		
		stringQueue = new LinkedBlockingDeque<String>(20);
		
	}
	
	
	public int getDatabaseSequenceNumber() {
		return databaseSequenceNumber;
	}
	
	// will be implemented differently. must take LIST messages and append them
	public void update() {
		
		databaseSequenceNumber ++;
	}
}
