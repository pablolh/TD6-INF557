import java.util.concurrent.LinkedBlockingDeque;

public class ListReceiver implements SimpleMessageHandler, Runnable  {
	
	private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
	private MuxDemuxSimple myMuxDemux = null;
	
	
	@Override
	public void run() {
		while(true) {
			try {
				ListMessage listMessage = new ListMessage(incoming.take());
				
				if(!listMessage.getPeerID().equals(myMuxDemux.getMyID()) &&
						listMessage.getSequenceNo() != myMuxDemux.getbyID(listMessage.getSenderID()).getPeerSeqNum() ) {
					// the message is for me : let me update my database
					// also: sequence numbers correspond
					
					
					
					
					
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
