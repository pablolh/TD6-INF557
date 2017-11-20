/* Le Hénaff Pablo ; Basudan Hossam*/


public class HelloSender implements SimpleMessageHandler, Runnable{
    private MuxDemuxSimple myMuxDemux = null;

    public void setMuxDemux(MuxDemuxSimple md){
        myMuxDemux = md;
    }

    public void handleMessage(String m){
        //Nothing to do
    }


    public void run(){
        while (true){
            try {
                Thread.sleep(Test.SENDINGPERIOD);
                HelloMessage toBeSent = new HelloMessage(myMuxDemux.getMyID()
                        , myMuxDemux.myDatabase.getDatabaseSequenceNumber() ,Test.HELLOINTERVAL);
                for (PeerRecord pr : myMuxDemux.getPeerTable()) {
                    toBeSent.addPeer(pr.getPeerID());
                }
                myMuxDemux.send(toBeSent.getHelloMessageAsEncodedString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
