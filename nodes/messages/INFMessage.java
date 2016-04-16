package projects.PIF.nodes.messages;

import sinalgo.nodes.messages.Message;

public class INFMessage extends Message {
	public int senderID;
	public int senderFather;
	public int num;
	
	public INFMessage(int n, int senderID, int senderFather) {
		num = n;
		this.senderID = senderID;
		this.senderFather = senderFather;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new INFMessage(num, this.senderID, this.senderFather);
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}
	
}
