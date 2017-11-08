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
                //TODO change the seqenceNo to be sent
                HelloMessage toBeSent = new HelloMessage(myMuxDemux.getMyID(), 42 ,Test.HELLOINTERVAL);
                for (PeerRecord pr : myMuxDemux.getPeerTable()) {
                    if (pr.getPeerState().equals("heard")) {
                        toBeSent.addPeer(pr.getPeerID());
                    }
                    myMuxDemux.send(toBeSent.getHelloMessageAsEncodedString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
