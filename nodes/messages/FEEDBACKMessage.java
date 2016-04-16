package projects.PIF.nodes.messages;
import java.util.HashSet;

import sinalgo.nodes.messages.Message;

public class FEEDBACKMessage extends Message {
	public int destinationID;
	public int senderID;
	public HashSet<Integer> sourceFeedbackIDs;

	public FEEDBACKMessage(int sourceFeedbackID, int senderID, int destinationID) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackIDs = new HashSet<Integer>();
		this.sourceFeedbackIDs.add(sourceFeedbackID);
	}
	
	public FEEDBACKMessage(HashSet<Integer> sourceFeedbackIDs, int senderID, int destinationID) {
		this.destinationID = destinationID;
		this.senderID = senderID;
		this.sourceFeedbackIDs = sourceFeedbackIDs;
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new FEEDBACKMessage(this.sourceFeedbackIDs, this.senderID, this.destinationID);
	}

	public HashSet<Integer> getSourceFeedbackIDs() {
		return sourceFeedbackIDs;
	}

	public void addSourceFeedbackID(int sourceFeedbackID) {
		this.sourceFeedbackIDs.add(sourceFeedbackID);
	}

	public void appendSourceFeedbackID(HashSet<Integer> newSourceFeedbackIDs)
	{
		this.sourceFeedbackIDs.addAll(newSourceFeedbackIDs);
	}
	
	public int getDestinationID() {
		return destinationID;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}
	
}
