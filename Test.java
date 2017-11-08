/* Le Hénaff Pablo ; Basudan Hossam*/
import java.net.*;


public class Test {

    public static final boolean DEBUG = true;
    public static final int PORTNO = 4241;
    public static void main (String[] args){

        //EXO3
//        HelloMessage h = new HelloMessage("HELLO;Bob;42;60;2;Kan;Pab");
//        h.addPeer("hoss");
//        System.out.println(h.getHelloMessageAsEncodedString());
//        System.out.println(h); TEST DE MODIF

        //EXO6
        DatagramSocket mySocket = null;
        try {
            mySocket = new DatagramSocket(PORTNO);
            mySocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        SimpleMessageHandler[] handlers = new SimpleMessageHandler[3];
        handlers[0]= new HelloSender();
        //handlers[0]= new HelloReceiver();
        handlers[1]= new HelloReceiver();
//        handlers[1]= new HelloReceiver();
        handlers[2]= new DebugReceiver();
        MuxDemuxSimple dm = new MuxDemuxSimple(handlers, mySocket);

        new Thread(dm).start();
        dm.send("HELLO;"+dm.getMyID()+";42;60;0");
        for( int i = 0 ; i<handlers.length ; i++){
            new Thread((Runnable) handlers[i]).start();
        }
    }
}
