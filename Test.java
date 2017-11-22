/* Le Hénaff Pablo ; Basudan Hossam*/
import java.net.*;


public class Test {

    public static final boolean DEBUG = false;
    public static final int PORTNO = 4242;
    public static final int HELLOINTERVAL = 60;     //in s
    public static final int SENDINGPERIOD = 5000;    //in ms
    public static final String MYID = "ack";
    public static void main (String[] args){

        //GIT TEST HOSSAM
        //EXO3
//        HelloMessage h = new HelloMessage("HELLO;Bob;42;60;2;Kan;Pab");
//        h.addPeer("hoss");
//        System.out.println(h.getHelloMessageAsEncodedString());
//        System.out.println(h);

        //EXO6
        DatagramSocket mySocket = null;
        try {
            mySocket = new DatagramSocket(PORTNO);
            mySocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        SimpleMessageHandler[] handlers = new SimpleMessageHandler[6];
        handlers[0]= new HelloSender();
        handlers[1]= new HelloReceiver();
        handlers[2]= new DebugReceiver();
        handlers[3]= new SynReceiver();
        handlers[4]= new SynSender();
        handlers[5]= new ListReceiver();
        MuxDemuxSimple dm = new MuxDemuxSimple(handlers, mySocket);
        dm.myDatabase.update("Yes");
        dm.myDatabase.update("Hola");
        dm.myDatabase.update("Yahooooo");

        new Thread(dm).start();
        for( int i = 0 ; i<handlers.length ; i++){
            new Thread((Runnable) handlers[i]).start();
        }
    }
}
