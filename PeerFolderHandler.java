import java.io.File;

public class PeerFolderHandler implements SimpleMessageHandler, Runnable{

    MuxDemuxSimple myMuxDemux;
    private String senderID;

    public PeerFolderHandler(String senderID){
        this.senderID=senderID;
    }


    public void run() {

//        //Create a folder for this peer if not exists
//        File senderFolder = new File(Test.ROOTFOLDERERPATH + senderID);
//        if(!senderFolder.exists()) {
//            senderFolder.mkdir();
//        }
//
//        // ADD TO DOWNLOADQUEUE
//        if(Test.DEBUG)
//            System.out.println("PeerFolderHandler : "+myMuxDemux.getOthersDatabases().get(senderID));
//        for (String fileName : myMuxDemux.getOthersDatabases().get(senderID).stringQueue) {
//
//            File file = new File(Test.ROOTFOLDERERPATH + senderID + "/" + fileName);
//            if(!file.exists()) {
//                DownloadObject dO = new DownloadObject(senderID,fileName);
//                myMuxDemux.downloadQueue.add(dO);
//                if(Test.DEBUG)
//                    System.out.println("LISTRECEIVER : added DownloadObject "+dO+" to downloadQueue");
//
//            }
//
//        }
//
//        //REMOVE ALL REMOVED FILES
//        File[] listOfFiles = senderFolder.listFiles();
//
//        for (File file: listOfFiles) {
//            if(myMuxDemux.getOthersDatabases().get(senderID).stringQueue.contains(file.getName()))
//                file.delete();
//        }
    }

    @Override
    public void handleMessage(String m) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setMuxDemux(MuxDemuxSimple muxDemuxSimple) {
        this.myMuxDemux = muxDemuxSimple;

    }

}
