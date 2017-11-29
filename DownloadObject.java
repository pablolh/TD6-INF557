import java.net.InetAddress;

class DownloadObject {
	private String senderID;
	private String fileName;
	
	DownloadObject(String senderID, String fileName) {
		this.senderID = senderID;
		this.fileName = fileName;
	}

	public String getSenderID() {
		return senderID;
	}

	public String getFileName() {
		return fileName;
	}
}