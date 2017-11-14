import java.util.concurrent.LinkedBlockingDeque;

public class SynReceiver implements SimpleMessageHandler, Runnable  {
	
	private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
	private MuxDemuxSimple myMuxDemux = null;
	
	
	
	@Override
	public void run() {
		
		// we need to check that m corresponds to a SYN message
		// maybe change the structure and add a simple Message class ? or just handled through custom exceptions, as explained below
		while(true){
			try {
				String[] split = incoming.take().split("/");
				SynMessage synMessage = new SynMessage(split[1]);
				// TODO: how to ignore syn messages received while sending list messages to that peer ?
				// ie how to check that the last LIST message has already been sent ? -> peer state updated ?
				// Hossam : if the syn message received while we send List messages it will be ignored be the process
				// 			busy sending the list messages, after sending the list messages we change the state
				// 			We check the state before processing to ignore already syn peers?

				//IS SYN message for me?
				//Are't we already synchronized?
				//Do I have the  version he needs?
				if(synMessage.getPeerId().equals(myMuxDemux.getMyID()) &&
						!myMuxDemux.getbyID(synMessage.getPeerId()).getPeerState().equals("synchronized")&&
						synMessage.getSequenceN()==myMuxDemux.myDatabase.getDatabaseSequenceNumber()) {


					synchronized (myMuxDemux.myDatabase.stringQueue) { // used this new design: database is public and we need to add synchronized block each time we access it

						int index = 0;
						for (String dataString : myMuxDemux.myDatabase.stringQueue) {

							ListMessage listMessage = new ListMessage(myMuxDemux.getMyID(), synMessage.getPeerId(), synMessage.getSequenceN(), myMuxDemux.myDatabase.size());

							// tell to listMessage what is the data and which part it contains
							listMessage.setPartNoAndData((index++), dataString);

							myMuxDemux.send(listMessage.getListMessageAsEncodedString());
						}

						//After sending we update peer state without verifying that the messages were really sent!
						//TODO questions:
						//				Do I need to update the peerSyn# in my peerTable?
						//				how do we get out of synchronized? only way by hello message to "inconsistent"
						//				how do we get from inconsistent to heard ? after expiration, we remove then we receive new hello message?
						//				what about dying? why do we use it? isn't dying means remove? or it is more than that?
						myMuxDemux.getbyID(synMessage.getPeerId()).setPeerState("synchronized");
					}
				}


			} catch (Exception e) {
				// useless : SynMessage constructor receiving a HelloMessage string is normal
				// another idea ? maybe SynMessage should raise a different exception when not the right type of message, so we can do nothing in that case
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
