/* Le Hénaff Pablo ; Basudan Hossam*/
import java.util.Vector;

public class HelloMessage {
    private String senderID;
    private int sequenceNo;
    private int helloInterval;
    private int numPeers;
    private Vector<String> peer;


    public HelloMessage(String s)  throws Exception {
        s = s.replaceAll("[\\t\\n\\r]+", "");
        String [] tmp = s.split(";");

        //ERROR HANDLING
        if (tmp.length<5)
            throw new Exception("incorrect format of HelloMessage less than 5 elements");
        if(tmp[1].length() > 16)
            throw new Exception("incorrect format of HelloMessage id plus than 16 char");
        if(Integer.parseInt(tmp[3]) < 0 || Integer.parseInt(tmp[3])>255)
            throw new Exception("incorrect format of HelloMessage sequence number bounders");
        if(Integer.parseInt(tmp[4]) < 0 || Integer.parseInt(tmp[4])>255)
            throw new Exception("Number of peers exceeded 255");
        this.senderID = tmp[1];
        this.sequenceNo = Integer.parseInt(tmp[2]);
        this.helloInterval = Integer.parseInt(tmp[3]);
        this.numPeers = Integer.parseInt(tmp[4]);
        this.peer = new Vector<String>();
        for (int i = 5 ; i < this.numPeers+5 ; i++){
            this.peer.add(tmp[i]);
        }

    }

    public HelloMessage (String senderID, int sequenceNo, int HelloInterval) {
        if(senderID.length() > 16)
            System.err.println("incorrect format of HelloMessage ");
        if(HelloInterval < 0 || HelloInterval>255)
            System.err.println("incorrect format of HelloMessage ");
        this.senderID=senderID;
        this.sequenceNo= sequenceNo;
        this.helloInterval=HelloInterval;
        this.numPeers=0;
        this.peer = new Vector<String>();

    }

    public void addPeer(String peerID){
        if (this.numPeers<255){
            this.numPeers++;
            this.peer.add(peerID);
        }
        else
            System.err.println("Number of peers exceeded 255");

    }

    public String getHelloMessageAsEncodedString(){
        String s= "HELLO;" + this.senderID + ";" + this.sequenceNo + ";" +
                this.helloInterval + ";" + this.numPeers;
        if(this.numPeers>0){
            for (int i= 0 ; i<this.peer.size(); i++){
                s+= ";" + this.peer.get(i);
            }
        }
        return s;
    }

    @Override
    public String toString() {
        return "HelloMessage{" +
                "senderID='" + senderID + '\'' +
                ", sequenceNo=" + sequenceNo +
                ", helloInterval=" + helloInterval +
                ", numPeers=" + numPeers +
                ", peer=" + peer +
                '}';
    }



    public String getSenderID() {
        return senderID;
    }


    public int getSequenceNo() {
        return sequenceNo;
    }


    public int getHelloInterval() { return helloInterval; }


    public Vector<String> getPeer() {
        return peer;
    }


}
