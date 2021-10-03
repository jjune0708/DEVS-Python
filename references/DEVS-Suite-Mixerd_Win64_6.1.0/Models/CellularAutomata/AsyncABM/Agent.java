package CellularAutomata.AsyncABM;

import java.util.ArrayList;
import java.util.Random;
import model.modeling.message;
import model.modeling.CAModels.TwoDimCell;

public class Agent extends TwoDimCell {
	private static final int rate = 20;
	protected inputEntity msg;
	// 8 neighbors and 1 itself
	protected String[] goingDirections = { "outN", "outNE", "outE", "outSE", "outS", "outSW", "outW", "outNW", "Stay" };
	protected String[] neighborIn = { "inN", "inNE", "inE", "inSE", "inS", "inSW", "inW", "inNW" };
	protected String[] neighborOut = { "outN", "outNE", "outE", "outSE", "outS", "outSW", "outW", "outNW" };
	protected String[] comingDirections = { "No", "No", "No", "No", "No", "No", "No", "No" };
	protected ArrayList<Integer> comingShuffle = new ArrayList<Integer>();
	Random r = new Random();

	// status is the phase
	private String status;
	private String goDirection = "Stay";
	private String comeDirection = "No";
	private String finalDirection = "No";

	private int step;
	// protected boolean clocked = true;

	public Agent() {
		this(0, 0);
	}

	public Agent(int xcoord, int ycoord) {
		super(xcoord, ycoord);
		status = "EMPTY";
		step = 1;
	}

	public Agent(int xcoord, int ycoord, String _status) {
		super(xcoord, ycoord);
		status = _status;
		step = 1;
	}

	public Agent(int xcoord, int ycoord, String _status, int _step) {
		super(xcoord, ycoord);
		status = _status;
		step = _step;
		addInport("inTrans");
		addOutport("outTrans");
	}

	/**
	 * Initialization method
	 */
	public void initialize() {
		super.initialize();
		if (status == "EMPTY") {
			holdIn("EMPTY", INFINITY);
		} else {
			holdIn("CELL: " + status, step);
		}
		// Define the Phase Color for CA Display
		AgentUI.setPhaseColor();
	}

	/**
	 * External Transition Function
	 */

	public void deltext(double e, message x) {

		Continue(e);
		for (int i = 0; i < x.getLength(); i++) {
			for (int j = 0; j < neighborIn.length; j++) {
				if (somethingOnPort(x, neighborIn[j])) {
					msg = (inputEntity)x.getValOnPort(neighborIn[j], i);
					if (msg != null && msg.getInput().contains("CELL")) {
						if (status == "EMPTY")
							comingDirections[j] = "WANTMOVE";
					} else if (msg != null && msg.getInput().contains("COMING")) {
						finalDirection = neighborOut[j];
					} else if (msg != null) {
						phase = "CELL: " + msg.getInput();
						status = msg.getInput();
						//agent has constant speed not cell
						step = msg.getSigma();
					}
				}
			}
		}

		for (int i = 0; i < comingDirections.length; i++) {
			if (comingDirections[i] != "No") {
				comingShuffle.add(i);
			}
		}
		int pickCome = 0;
		if (comingShuffle.size() > 0) {
			pickCome = r.nextInt(comingShuffle.size());
			comeDirection = neighborIn[comingShuffle.get(pickCome)];
		}

		if (goDirection == "Stay" && comeDirection == "No" && finalDirection == "No") {
			holdIn(phase, step);
		} else if (comeDirection != "No") {
			holdIn("COMING:- " + comeDirection, 0);
		} else if (finalDirection != "No") {
			holdIn(status + ":- MOVING " + finalDirection, 0);
		}

		// reset the direction array
		for (int i = 0; i < comingDirections.length; i++) {
			comingDirections[i] = "No";
		}
		comingShuffle.clear();

		// System.out.println(getXcoord() + ", " + getYcoord() + ": " + phase);

	}

	/*
	 * Internal Transition Function
	 */

	public void deltint() {

		if (status == "EMPTY") {
			holdIn("EMPTY", INFINITY);
		} else if (phase.contains("CELL")) {
			holdIn(phase, step);
		} else {
			holdIn(phase, INFINITY);
		}

	}

	public void deltcon(double e, message x) {
		deltint();
		deltext(0, x);
	}

	/*
	 * Message out Function
	 */
	public message out() {

		message m = new message();
		
		if (phase.contains("CELL")) {
			goDirection = goingDirections[r.nextInt(goingDirections.length)];
			for (int i = 0; i < neighborOut.length; i++) {
				if (goDirection == neighborOut[i]) {
					m.add(makeContent(neighborOut[i], new inputEntity(phase, step)));
					break;
				}
			}
			
		} else if (phase.contains("COMING")) {
			for (int i = 0; i < neighborOut.length; i++) {
				if (comeDirection == neighborIn[i]) {
					m.add(makeContent(neighborOut[i], new inputEntity(phase, step)));
					break;
				}
			}
		} else if (phase.contains("MOVING")) {
			for (int i = 0; i < neighborOut.length; i++) {
				if (finalDirection == neighborOut[i]) {
					m.add(makeContent(neighborOut[i], new inputEntity(status, step)));
					status = "EMPTY";
					break;
				}
			}

		}

		goDirection = "Stay";
		comeDirection = "No";
		finalDirection = "No";

		return m;

	}

	public String getStatus() {
		return status;
	}

	public void setInitialStatus(String status) {
		this.status = status;
	}

}
