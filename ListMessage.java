/* Le Hénaff Pablo ; Basudan Hossam*/

public class ListMessage {
    private String senderID;
    private String peerID;
    private int sequenceNo;
    private int TotalParts;
    private int partNo;
    private String data;

    //ListMessage PARSER
    public ListMessage(String s)  throws Exception {
        s = s.replaceAll("[\\t\\n\\r]+", "");
        String [] tmp = s.split(";");
        //ERROR HANDLING
        if(!tmp[0].equals("LIST"))
        	throw new NotRightTypeException("ListMessage PARSER : not a LIST message");
        if (tmp.length<7)
            throw new Exception("ListMessage PARSER : incorrect format of SynMessage less than 7 elements");
        if(tmp[1].length() > 16)
            throw new Exception("ListMessage PARSER : incorrect format of SynMessage senderID more than 16 char");
        if(tmp[2].length() > 16)
            throw new Exception("ListMessage PARSER : incorrect format of SynMessage peerID more than 16 char");
        if(Integer.parseInt(tmp[3]) < 0 || Integer.parseInt(tmp[3])>255)
            throw new Exception("ListMessage PARSER : incorrect format of SynMessage sequence number bounders");
        if(tmp[6].length()>255)
            throw new Exception("ListMessage PARSER : incorrect format of data sequence number bounders");
        if(Integer.parseInt(tmp[5])>Integer.parseInt(tmp[4]))
            throw new Exception("ListMessage PARSER : partNo > TotalParts ");
        this.senderID = tmp[1];
        this.peerID= tmp[2];
        this.sequenceNo = Integer.parseInt(tmp[3]);
        this.TotalParts=Integer.parseInt(tmp[4]);
        this.partNo=Integer.parseInt(tmp[5]);
        this.data=tmp[6];

    }


    // TO PABLO : WHY WE NEED GENERATORS? -> because we need to send List messages upon receipt of a syn message
    //ListMessage GENERATOR
    public ListMessage(String senderID, String peerID, int sequenceNo, int TotalParts) throws Exception{
        if(senderID.length() > 16)
            throw new Exception("ListMessage GENERATOR : incorrect format of SynMessage senderID more than 16 char");
        if(peerID.length() > 16)
            throw new Exception("ListMessage GENERATOR : incorrect format of SynMessage peerID more than 16 char");
        if(sequenceNo < 0 || sequenceNo>255)
            throw new Exception("ListMessage GENERATOR : incorrect format of SynMessage sequence number bounders");
        this.senderID=senderID;
        this.peerID=peerID;
        this.sequenceNo= sequenceNo;
        this.TotalParts=TotalParts;
    }

    public void setPartNoAndData(int partNo, String data ){
        if(data.length()>255)
            System.err.println("setPartNoAndData : incorrect format of data sequence number bounders");
        this.partNo=partNo;
        this.data=data;
    }

    public String getListMessageAsEncodedString(){
        return "SYN;" + this.senderID + ";" + this.peerID + ";" + this.sequenceNo +
                ";"+ this.TotalParts + ";" + this.partNo + ";" + this.data;
    }

    @Override
    public String toString() {
        return "ListMessage{" +
                "senderID='" + senderID + '\'' +
                ", peerID='" + peerID + '\'' +
                ", sequenceNo=" + sequenceNo +
                ", TotalParts=" + TotalParts +
                ", partNo=" + partNo +
                ", data='" + data + '\'' +
                '}';
    }


	public String getSenderID() {
		return senderID;
	}


	public String getPeerID() {
		return peerID;
	}


	public int getSequenceNo() {
		return sequenceNo;
	}


	public int getTotalParts() {
		return TotalParts;
	}


	public int getPartNo() {
		return partNo;
	}


	public String getData() {
		return data;
	}
}
