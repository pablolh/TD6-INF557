import java.util.concurrent.LinkedBlockingDeque;

public class SynReceiver implements SimpleMessageHandler, Runnable  {
	
	private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
	private MuxDemuxSimple myMuxDemux = null;
	
	
	
	@Override
	public void run() {
		
		// we need to check that m corresponds to a SYN message
		// maybe change the structure and add a simple Message class ? or just handled through custom exceptions, as explained below
		
		try {
			SynMessage synMessage = new SynMessage(incoming.take());
			
			// TODO: how to ignore syn messages received while sending list messages to that peer ?
			// ie how to check that the last LIST message has already been sent ? -> peer state updated ?
			
			synchronized (myMuxDemux.myDatabase) { // used this new design: database is public and we need to add synchronized block each time we access it
				
				int index = 0;
				for(String dataString : myMuxDemux.myDatabase.stringQueue) {
					
					ListMessage listMessage = new ListMessage(myMuxDemux.getMyID(), synMessage.getPeerId(), synMessage.getSequenceN(), myMuxDemux.myDatabase.size());
					
					// tell to listMessage what is the data and which part it contains
					listMessage.setPartNoAndData((index++), dataString);
					
					myMuxDemux.send(listMessage.getListMessageAsEncodedString());
				}
			}
			
			
			
		} catch (Exception e) {
			// useless : SynMessage constructor receiving a HelloMessage string is normal
			// another idea ? maybe SynMessage should raise a different exception when not the right type of message, so we can do nothing in that case
			e.printStackTrace();
		}
	}

	@Override
	public void handleMessage(String m) {
		
		
		
		
		try {
			incoming.put(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void setMuxDemux(MuxDemuxSimple muxDemuxSimple) {
		myMuxDemux = muxDemuxSimple;
		
	}

}
