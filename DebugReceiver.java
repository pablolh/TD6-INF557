/* Le Hénaff Pablo ; Basudan Hossam*/
import java.util.concurrent.LinkedBlockingDeque;

public class DebugReceiver implements SimpleMessageHandler, Runnable {
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
        while (true) {

            String msg = null;
            try {
                msg = incoming.take();
            } catch (InterruptedException e) {
                System.err.println(e);
            }

            // Handle message (debug)
            String[] split = msg.split("/");
            if(Test.DEBUG)
                System.out.println("DebugReceiver_rawMessage = " + split[0]);
            System.out.println(myMuxDemux.toStringPeerTable());
            System.out.println(myMuxDemux.toStringOthersDatabases());


//            othersDatabases.put("ok", new Database(10));
//            othersDatabases.get("ok").stringQueue.add("trying");

        }
    }
}
