import java.util.concurrent.LinkedBlockingDeque;

public class SynReceiver implements SimpleMessageHandler, Runnable  {
	
	private LinkedBlockingDeque<String> incoming = new LinkedBlockingDeque<String>(20);
	private MuxDemuxSimple myMuxDemux = null;
		
	
	
	@Override
	public void run() {
		
		// we need to check that m corresponds to a SYN message
		// maybe change the structure and add a simple Message class ?
		
		try {
			SynMessage synMessage = new SynMessage(incoming.take());
			
			// todo
			
		} catch (Exception e) {
			// useless : SynMessage constructor receiving a HelloMessage string is normal
			// another idea ? maybe SynMessage should raise a different exception when not the right type of message, so we can do nothing in that case
			e.printStackTrace();
		}
	}

	@Override
	public void handleMessage(String m) {
		
		
		
		
		try {
			incoming.put(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void setMuxDemux(MuxDemuxSimple muxDemuxSimple) {
		myMuxDemux = muxDemuxSimple;
		
	}

}
