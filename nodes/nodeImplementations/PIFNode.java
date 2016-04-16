package projects.PIF.nodes.nodeImplementations;
import java.awt.Color;
import java.awt.Graphics;

import projects.PIF.nodes.messages.FEEDBACKMessage;
import projects.PIF.nodes.messages.INFMessage;
import projects.PIF.nodes.timers.PIF_FeedbackTimer;
import projects.PIF.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

public class PIFNode extends Node {
	private boolean reached = false;
	private int nextHopToSource;
	private boolean sentMyFeedback;
	MessageTimer msgFeedbackTimer;
	FEEDBACKMessage msgFeedback;
	public static int sentINF = 0;
	public static int sentFeedback = 0;
	public static int receivedFeedback = 0;
	
	public enum TNO {TNO_FEEDBACK };
	private PIF_FeedbackTimer feedbackTimer;
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		int sender;
		
		while(inbox.hasNext()) {
			Message msg = inbox.next();
			sender = inbox.getSender().ID;
			
			//N� recebeu uma mensagem INF	
			if(msg instanceof INFMessage) {
				INFMessage msgINF = (INFMessage) msg;
				System.out.println("Node: "+this.ID+" recebeu INF " + msgINF.num + " do Node "+sender);
				
				if(!this.reached)
				{
					this.setColor(Color.GREEN);
					this.reached = true;	
					this.nextHopToSource = msgINF.getSenderID();
					msgINF.setSenderID(this.ID);
					msgINF.senderFather = this.nextHopToSource;
					MessageTimer infMSG = new MessageTimer(msgINF);
					infMSG.startRelative(0.1,this);
					
					//Agenda o FEEDBACK
					feedbackTimer = new PIF_FeedbackTimer(this, TNO.TNO_FEEDBACK);
					feedbackTimer.tnoStartRelative(10, this, TNO.TNO_FEEDBACK);	
				}
				// If received inf from child, wait for its feedback.
				else if(msgINF.senderFather == this.ID)
				{
					if (feedbackTimer != null)
						feedbackTimer.canceled = true;
				}
				
			}
			
			//Mensagem de Confirma��o
			if(msg instanceof FEEDBACKMessage) {
				FEEDBACKMessage newFeedback = (FEEDBACKMessage) msg;
				
				if (this.ID != 1){
					if(newFeedback.getDestinationID() == this.ID){

						// if already fired, make new buffer.
						if (msgFeedbackTimer == null || msgFeedbackTimer.fired == true)
						{
							msgFeedback = newFeedback;
							msgFeedback.setDestinationID(this.nextHopToSource);
							if (!sentMyFeedback)
							{
								msgFeedback.addSourceFeedbackID(this.ID);
								sentMyFeedback = true;
							}
							msgFeedbackTimer = new MessageTimer(msgFeedback);
						}
						else  //else insert in buffer.
							msgFeedback.appendSourceFeedbackID(newFeedback.getSourceFeedbackIDs());
						
						msgFeedbackTimer.startRelative(0.1, this);
						System.out.println("Node: "+this.ID+" Recebeu Feedback do Node "+ msgFeedback.getSourceFeedbackIDs() + " encaminhada pelo Node " +msgFeedback.getSenderID());	
					}
				}else{ 
					System.out.println("Source node recebeu Feedback do Node "+ newFeedback.getSourceFeedbackIDs());
					receivedFeedback = receivedFeedback + newFeedback.getSourceFeedbackIDs().size();
					System.out.println("\n\nMensagens INF transmitidas: "+ PIFNode.sentINF);
					System.out.println("Mensagens FEEDBACK transmitidas: "+ PIFNode.sentFeedback);
					System.out.println("Source recebeu FEEDBACK de: "+ PIFNode.receivedFeedback+ " nodes");
				}
			}
		}
	}
			
		
	public void feedbackStart(){
		MessageTimer feedbackMSG = new MessageTimer (new FEEDBACKMessage(this.ID, this.ID, this.nextHopToSource));
	  	feedbackMSG.startRelative(0.1, this);		
	}
	
	public void timeout(TNO tno){
		switch(tno){
			case TNO_FEEDBACK:
				feedbackStart();
				break;	
		}
	}

    @Override
	public void init() {
		//Considerando que o n� 1 tem a mensagem inf
		if (this.ID==1){
			this.setColor(Color.RED);
			this.nextHopToSource = this.ID;
			this.reached = true;
			
			InstanceTimer infMSG = new InstanceTimer (1, this);
	  		infMSG.startRelative(4.9, this);
		}
	}
    
	@Override
	public void postStep() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		// TODO Auto-generated method stub
		//if (this.ID == 1) highlight = true;
		super.drawNodeAsDiskWithText(g, pt, highlight, Integer.toString(this.ID), 6, Color.WHITE);
		
		
	}
	
	public void fireMessage(int n) {
		MessageTimer infMSG = new MessageTimer (new INFMessage(n, this.ID, this.nextHopToSource));
  		infMSG.startRelative(0.1, this);
  		
  		if (n < 1000)
  		{
  			InstanceTimer instMSG = new InstanceTimer (n+1, this);
  			instMSG.startRelative(4.9, this);
  		}
	}


	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
	}
}


class InstanceTimer extends Timer {
	
	public int n;
	PIFNode node;
	
	public InstanceTimer (int number, PIFNode nod){
		n = number;
		node = nod;
	}


	@Override
	public void fire() {
		node.fireMessage(n);
	}

}
