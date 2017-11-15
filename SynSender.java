/* Le Hénaff Pablo ; Basudan Hossam*/
public class SynSender implements SimpleMessageHandler, Runnable {

	MuxDemuxSimple myMuxDemux;
	
	@Override
	public void run() {
		// periodically send SYN messages to peers to become synchronized


		
		while(true) {
			
			try {
				Thread.sleep(Test.SENDINGPERIOD);

                for (PeerRecord pr : myMuxDemux.getPeerTable()) {
                	
                	// checks whether pr has state = synchronized
                    if (!pr.getPeerState().equals("synchronized")) {

                    	// senderId = myID
                        SynMessage toBeSent = new SynMessage(myMuxDemux.getMyID(), pr.getPeerID() ,pr.getPeerSeqNum());
                        
                        myMuxDemux.send(toBeSent.getSynMessageAsEncodedString());
                    }
                    
                }
                
			} catch (Exception e) { // catches InterruptedException and exceptions for badly formated SynMessage
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
	}

	@Override
	public void handleMessage(String m) {

		// nothing to do here
	}

	@Override
	public void setMuxDemux(MuxDemuxSimple muxDemuxSimple) {
		
		// we need to store a reference to our main component in order to access the peertable
		myMuxDemux = muxDemuxSimple;
		
	}

}
