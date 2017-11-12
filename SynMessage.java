/* Le Hénaff Pablo ; Basudan Hossam*/

public class SynMessage {
    private String senderID;
    private String peerID;
    private int sequenceNo;

    //SynMessage PARSER
    public SynMessage(String s)  throws Exception {
        s = s.replaceAll("[\\t\\n\\r]+", "");
        String [] tmp = s.split(";");
        //ERROR HANDLING
        if(!tmp[0].equals("SYN"))
        	throw new Exception("SynMessage PARSER : not a SYN message");
        if (tmp.length<4)
            throw new Exception("SynMessage PARSER : incorrect format of SynMessage less than 4 elements");
        if(tmp[1].length() > 16)
            throw new Exception("SynMessage PARSER : incorrect format of SynMessage senderID more than 16 char");
        if(tmp[2].length() > 16)
            throw new Exception("SynMessage PARSER : incorrect format of SynMessage peerID more than 16 char");
        if(Integer.parseInt(tmp[2]) < 0 || Integer.parseInt(tmp[2])>255)
            throw new Exception("SynMessage PARSER : incorrect format of SynMessage sequence number bounders");
        this.senderID = tmp[1];
        this.peerID= tmp[2];
        this.sequenceNo = Integer.parseInt(tmp[3]);

    }

    //SynMessage GENERATOR
    public SynMessage(String senderID, String peerID, int sequenceNo) throws Exception{
        if(senderID.length() > 16)
            throw new Exception("SynMessage GENERATOR : incorrect format of SynMessage senderID more than 16 char");
        if(peerID.length() > 16)
            throw new Exception("SynMessage GENERATOR : incorrect format of SynMessage peerID more than 16 char");
        if(sequenceNo < 0 || sequenceNo>255)
            throw new Exception("SynMessage GENERATOR : incorrect format of SynMessage sequence number bounders");
        this.senderID=senderID;
        this.peerID=peerID;
        this.sequenceNo= sequenceNo;
    }

    public String getSynMessageAsEncodedString(){
        return "SYN;" + this.senderID + ";" + this.peerID + ";" + this.sequenceNo + ";";
    }

    @Override
    public String toString() {
        return "SynMessage{" +
                "senderID='" + senderID + '\'' +
                ", peerID='" + peerID + '\'' +
                ", sequenceNo=" + sequenceNo +
                '}';
    }

}
