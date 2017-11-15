/* Le Hénaff Pablo ; Basudan Hossam*/
import java.time.Instant;
import java.util.concurrent.LinkedBlockingDeque;

public class HelloReceiver implements SimpleMessageHandler, Runnable {

    private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
    private MuxDemuxSimple myMuxDemux = null;

    public void setMuxDemux(MuxDemuxSimple md){
        myMuxDemux = md;
    }

    public void handleMessage(String m){
        try {
            incoming.put(m);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    //Sender Thread
    public void run(){
        while (true){
            String msg = null;
            try {
                msg = incoming.take();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            // Handle message (receive)
            String[] split = msg.split("/");

            HelloMessage helloMessage = null;
            try {
                helloMessage = new HelloMessage(split[0]);

                String IPAddress = split[1];

                //if different from myID
                if (!helloMessage.getSenderID().equals(myMuxDemux.getMyID())) {
                    //If not in the peerTable
                    //PeerRecord existingPeerRecord = myMuxDemux.getbyID(myMuxDemux.getPeerTable(),helloMessage.getSenderID());
                    PeerRecord existingPeerRecord = myMuxDemux.getbyID(helloMessage.getSenderID());
                    if (existingPeerRecord == null) {
                        try {
                            existingPeerRecord = new PeerRecord(helloMessage, IPAddress);
                            myMuxDemux.getPeerTable().put(existingPeerRecord);
                        } catch (InterruptedException e) {
                            System.err.println(e);
                        }
                    } else {
                        if (Test.DEBUG)
                            System.out.println("updating expiration time for record " + existingPeerRecord.getPeerID());
                        existingPeerRecord.setExpirationTime(Instant.now().plusSeconds(helloMessage.getHelloInterval()));

                    }

                    // If in the peerTable
                    //if in the helloMessage peers
                    if (Test.DEBUG)
                        System.out.println("peers" + helloMessage.getPeer() + "my id " + myMuxDemux.getMyID() + " is in the peer list of the message received.");
                    if (helloMessage.getPeer().contains(myMuxDemux.getMyID())) {

                        if (existingPeerRecord.getPeerSeqNum() != helloMessage.getSequenceNo()) {
                            existingPeerRecord.setPeerState("inconsistent");
                        }

                        if (existingPeerRecord.getPeerState().equals("synchronized") && existingPeerRecord.getPeerSeqNum() == helloMessage.getSequenceNo()) {
                            existingPeerRecord.setPeerState("synchronized");
                        }
                        if (Test.DEBUG)
                            System.out.println("updating PeerSeqNum for record " + existingPeerRecord.getPeerID());
                        if (existingPeerRecord != null) existingPeerRecord.setPeerSeqNum(helloMessage.getSequenceNo());
                    }
                }
            } catch (NotRightTypeException e) {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
