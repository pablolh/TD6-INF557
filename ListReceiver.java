import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingDeque;

public class ListReceiver implements SimpleMessageHandler, Runnable  {
	
	private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
	//no need to be synchronized only the run thread use them
	private MuxDemuxSimple myMuxDemux = null;
	private HashMap<String, Database> images= new HashMap<String, Database> ();
	//I use trackImages to keep track of received messages
	//allows us to verify if all list messages were received
	//There should be a better solution!
	private HashMap<String, HashSet<Integer>> trackImages= new HashMap<String, HashSet<Integer>>();

	
	@Override
	public void run() {
		while(true) {
			try {
				ListMessage listMessage = new ListMessage(incoming.take());
				String senderID =listMessage.getSenderID();


				//What about ignoring all list messages from peer if we already synchronized?
				// the message is for me : let me update my database
				// also: sequence numbers correspond
				if(myMuxDemux.getbyID(senderID).getPeerState().equals("synchronized") &&
						!listMessage.getPeerID().equals(myMuxDemux.getMyID()) &&
						listMessage.getSequenceNo() == myMuxDemux.getbyID(senderID).getPeerSeqNum() ) {

					// TODO questions:
					// 				for seqNo we check equality? I think of it as, i receive the same version as
					//				the one I asked for from the peer that told me by a hello message that he has the
					//				version x
					//				how to verify that I received all list messages?


					if(images.containsKey(senderID)){

						//the last used seq# for this senderId
						int oldSeqNo = images.get(senderID).getDatabaseSequenceNumber();
						
						//the SeqNo hasn't changed means that there is not a new version
						// of the peer database and I still deal with the old one
						if(oldSeqNo==listMessage.getSequenceNo()){
							//Haven't received the same element twice with the same SeqNo
							if(!trackImages.get(senderID).contains(listMessage.getPartNo())){
								images.get(senderID).stringQueue.set(listMessage.getPartNo(),listMessage.getData());
								trackImages.get(senderID).add(listMessage.getPartNo());
							}
						}

						// I remove the old image and the old track and start again
						else{
							images.remove(senderID);
							trackImages.remove(senderID);

							Database newDatabase = new Database(listMessage.getTotalParts());
							newDatabase.stringQueue.set(listMessage.getPartNo(),listMessage.getData());
							images.put(senderID,newDatabase);


							HashSet<Integer> newTrack = new  HashSet<Integer>();
							newTrack.add(listMessage.getPartNo());
							trackImages.put(senderID,newTrack);

						}


					}
					//create an image of the peer database if not already exists
					else{
						Database newDatabase = new Database(listMessage.getTotalParts());
						newDatabase.stringQueue.set(listMessage.getPartNo(),listMessage.getData());
						images.put(senderID,newDatabase);


						HashSet<Integer> newTrack = new  HashSet<Integer>();
						newTrack.add(listMessage.getPartNo());
						trackImages.put(senderID,newTrack);
					}

					//when receiving all the elements update the real database
					//if in my databases overwrite and remove the image and the track
					if(trackImages.get(senderID).size()==listMessage.getTotalParts()){
						myMuxDemux.getOthersDatabases().replace(senderID,images.get(senderID));
						images.remove(senderID);
						trackImages.remove(senderID);
						//update  peerState
						myMuxDemux.getbyID(listMessage.getSenderID()).setPeerState("synchronized");

						//
						//update expiration time
						//How about the listInterval?
						//int listInterval = 60; // in s
						//myMuxDemux.getbyID(listMessage.getSenderID()).setExpirationTime(Instant.now().plusSeconds(listInterval));
					}

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
