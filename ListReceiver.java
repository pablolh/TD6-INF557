import java.time.Instant;
import java.util.concurrent.LinkedBlockingDeque;

public class ListReceiver implements SimpleMessageHandler, Runnable  {
	
	private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
	private MuxDemuxSimple myMuxDemux = null;
	
	
	@Override
	public void run() {
		while(true) {
			try {
				ListMessage listMessage = new ListMessage(incoming.take());

				//What about ignoring all list messages from peer if we already synchronized?
				if(myMuxDemux.getbyID(listMessage.getSenderID()).getPeerState().equals("synchronized") &&
						!listMessage.getPeerID().equals(myMuxDemux.getMyID()) &&
						listMessage.getSequenceNo() == myMuxDemux.getbyID(listMessage.getSenderID()).getPeerSeqNum() ) {
					// the message is for me : let me update my database
					// also: sequence numbers correspond
					// TODO questions:
					// 				for seqNo we check equality? I think of it as, i receive the same version as
					//				the one I asked for from the peer that told me by a hello message that he has the
					//				version x
					//				how to verify that I received all list messages?



					//Overwrite each element in my database with the corresponding data of part#
					//if part# message lost I keep my own chunk of database
					//I add the new elements
					//the total size of my database will be = TotalParts
					myMuxDemux.myDatabase.update(listMessage);


					//if all list message received
						//update  peerState
						//myMuxDemux.getbyID(listMessage.getSenderID()).setPeerState("synchronized");
						//update expiration time
						//How about the listInterval?
						//int listInterval = 60; // in s
						//myMuxDemux.getbyID(listMessage.getSenderID()).setExpirationTime(Instant.now().plusSeconds(listInterval));

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
