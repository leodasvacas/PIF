package projects.PIF.nodes.timers;

import projects.PIF.nodes.nodeImplementations.PIFNode;
import sinalgo.nodes.Node;
import sinalgo.nodes.timers.Timer;

public class PIF_FeedbackTimer extends Timer {
	
	private PIFNode.TNO tno;
	public boolean canceled;
	PIFNode n;
	public PIF_FeedbackTimer (PIFNode n, PIFNode.TNO tno){
		this.tno = tno;
		this.n = n;
	}


	@Override
	public void fire() {
		if (!canceled)
			n.timeout(tno);
	}
	
	public void tnoStartRelative(double time, Node n, PIFNode.TNO tno){
		this.tno=tno;
		super.startRelative(time, n);
	}
	public void tnoStartGlobalRelative(double time, PIFNode.TNO tno){
		this.tno=tno;
		super.startGlobalTimer(time);
	}
	public void tnoStartAbsolute(double time, Node n, PIFNode.TNO tno){
		this.tno=tno;
		super.startAbsolute(time, n);
	}

}




	
	
	


