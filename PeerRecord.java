/* Le Hénaff Pablo ; Basudan Hossam*/
import java.net.InetAddress;
import java.time.Instant;

public class PeerRecord {

    private String peerID;
    private String peerIPAddress;
    private int peerSeqNum;
    private Instant expirationTime;
    private String peerState;

    public PeerRecord(HelloMessage helloMessage, String IPAddress){
        peerID = helloMessage.getSenderID();
        peerIPAddress = IPAddress;
        //TODO verify problem sending first hello
        peerSeqNum =-1;
        peerSeqNum =-1;
        expirationTime= Instant.now().plusSeconds(helloMessage.getHelloInterval());
        peerState = "heard";
    }


//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        PeerRecord that = (PeerRecord) o;
//        return (peerID != null ? !peerID.equals(that.peerID) : that.peerID != null);
//    }


    public String getPeerID() {
        return peerID;
    }

    public String getPeerIPAddress() {
        return peerIPAddress;
    }

    public int getPeerSeqNum() {
        return peerSeqNum;
    }

    public void setPeerSeqNum(int peerSeqNum) {
        this.peerSeqNum = peerSeqNum;
    }

    public Instant getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(Instant expirationTime) {
         this.expirationTime=expirationTime;
    }

    public String getPeerState() {
        return peerState;
    }

    public void setPeerState(String peerState){
        this.peerState= peerState;
    }



    @Override
    public String toString() {
        return "PeerRecord{" +
                "peerID='" + peerID + '\'' +
                ", peerIPAddress='" + peerIPAddress + '\'' +
                ", peerSeqNum=" + peerSeqNum +
                ", expirationTime=" + expirationTime +
                ", peerState='" + peerState + '\'' +
                '}';
    }

}