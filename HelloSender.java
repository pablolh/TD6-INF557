/* Le Hénaff Pablo ; Basudan Hossam*/

import java.util.concurrent.LinkedBlockingDeque;

public class HelloSender implements SimpleMessageHandler, Runnable{
    private MuxDemuxSimple myMuxDemux = null;

    public void setMuxDemux(MuxDemuxSimple md){
        myMuxDemux = md;
    }

    public void handleMessage(String m){
        //ON FAIT RIEN
    }

    //Sender Thread
    public void run(){
        while (true){
            //Handle message (send)
            try {
                Thread.sleep(5000);
                int counter = 0;
                String peerList = "";
                for (PeerRecord pr : myMuxDemux.getPeerTable()) {

                    if (pr.getPeerState().equals("heard")) {
                        counter++;
                        peerList += ";" + pr.getPeerID();
                    }
                    HelloMessage toBeSent = new HelloMessage("HELLO;" + myMuxDemux.getMyID() + ";42;60;" + counter + peerList);
                    myMuxDemux.send(toBeSent.getHelloMessageAsEncodedString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
