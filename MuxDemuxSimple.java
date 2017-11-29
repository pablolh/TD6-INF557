/* Le Hénaff Pablo ; Basudan Hossam*/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.exit;

public class MuxDemuxSimple implements Runnable {
    private DatagramSocket mySocket = null;
    private SimpleMessageHandler[] myMessageHandlers;
    private LinkedBlockingDeque<String> outgoing = new LinkedBlockingDeque<String>(20);
    private LinkedBlockingDeque<PeerRecord> peerTable = new LinkedBlockingDeque<PeerRecord>(20);
    private String myID =Test.MYID;
    
    LinkedBlockingDeque<DownloadObject> downloadQueue = new LinkedBlockingDeque<DownloadObject>(20);
    
    //TODO questions:
    //              "create and test the databases of my peers?"
    //              H1 : Do we all have the same database with different versions? --> only update
    //              H2 : Each machine is responsible of a small part of the database and I keep a backup?
    //              I will code ListReceiver based on H2 after discussion robin
    public Database myDatabase = new Database(20);
    private ConcurrentHashMap<String, Database> othersDatabases= new ConcurrentHashMap<String, Database> ();


    public MuxDemuxSimple (SimpleMessageHandler[] h, DatagramSocket s){

        if(s==null)
            try {
                exit(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        mySocket = s;
        myMessageHandlers = h;
        Thread senderThread = new Thread(new Runnable() {
            @Override
            public void run() {
                byte [] byteArray;
                DatagramPacket dp = null;
                String msg = "";
                while (true){
                    try {
                        msg = outgoing.take();
                        //InetAddress.getLocalHost();

                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                    byteArray =msg.getBytes();
                    try {
                        dp = new DatagramPacket(byteArray, byteArray.length, InetAddress.getByName("255.255.255.255"), Test.PORTNO);
                    } catch (UnknownHostException e) {
                        System.err.println(e);
                    }
                    try {
                        mySocket.send(dp);
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }
        });
        senderThread.start();
    }


    //Receiver Thread
    public void run(){
        for (int i=0; i<myMessageHandlers.length; i++){
            myMessageHandlers[i].setMuxDemux(this);
        }
        byte[] buffer = new byte[1024];

        DatagramPacket receiver = new DatagramPacket(buffer, buffer.length);
        while(true) {
            try {
                mySocket.receive(receiver);
                //TODO how to ip adresse to peer record

                String data = new String(receiver.getData(),0, receiver.getLength(), StandardCharsets.UTF_8);
                for (int i=0; i<myMessageHandlers.length; i++){
                    myMessageHandlers[i].handleMessage( data+ receiver.getAddress().toString());
//                    myMessageHandlers[i].handleMessage( data);
                }
            } catch (Exception e) {
                System.err.println(e);
            }

        }
        //TODO Close the socket
//        try{
//            mySocket.close();
//        }catch(IOException e){ }
    }

    public void send(String s){
        try {
            outgoing.put(s);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }


    //TODO verifie loop
    public  synchronized LinkedBlockingDeque<PeerRecord> getPeerTable(){
        for(PeerRecord pr : this.peerTable) {
            if (pr.getExpirationTime().compareTo(Instant.now())<0) {
                peerTable.remove(pr);
                if(Test.DEBUG)
                    System.out.println("In_getPeerTable()_Deleting " + pr.getPeerID() + " peer record (expired).");
            }
        }
        return this.peerTable;
    }

    public String getMyID(){
        return this.myID;
    }

    public  synchronized PeerRecord getbyID(String ID){
        for(PeerRecord pr : this.getPeerTable()) {
            if (pr.getPeerID().equals(ID)) return pr;
        }
        return null;
    }

    public  String toStringPeerTable(){
        String res="\n**********************************************************************\n";
        res+="peerID;peerIPAddress;peerSeqNum;expirationTime;peerState\n";
        res+="**********************************************************************\n";
        for(PeerRecord pr : this.peerTable) {
            res+=pr.toString();
        }
        res+="**********************************************************************\n\n";
        return res;
    }

    public ConcurrentHashMap<String, Database> getOthersDatabases() {
        return othersDatabases;
    }

    public String toStringOthersDatabases(){
        String s="";
        if(othersDatabases.size()>=1){
            s= "\n\n***********************************************************************\n";
            for(Map.Entry<String,Database> db : othersDatabases.entrySet()){
                s+="<"+db.getKey()+";"+db.getValue().getDatabaseSequenceNumber()+">\n";
                s+= db.getValue().toStringofStringQueue();
            }
            s+= "***********************************************************************\n\n";
        }

        return s;
    }
}