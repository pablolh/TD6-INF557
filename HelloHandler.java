/* Le Hénaff Pablo ; Basudan Hossam*/
import java.util.concurrent.LinkedBlockingDeque;

public class HelloHandler implements SimpleMessageHandler, Runnable{

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

    public void run(){
        while (true){
            try {
                String msg = incoming.take();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            // Handle message
            // Or Generate message
            myMuxDemux.send("This is the message to send");
            //
        }
    }
}