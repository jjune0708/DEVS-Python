package CellularAutomata.AsyncABM;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

import model.modeling.CAModels.TwoDimCellSpace;
import view.modeling.ViewableComponent;

public class AgentSpace extends TwoDimCellSpace {

	private final String[] statusCXCL12 = { "EMPTY", "EMPTY", "EMPTY", "CXCL12" };
	private final String[] statusCXCLR4 = { "EMPTY", "EMPTY", "EMPTY", "CXCR4" };
	private final String[] statusCXCLR7 = { "EMPTY", "EMPTY", "EMPTY", "CXCR7" };

	public AgentSpace() {
		this(50, 50);
	}

	public AgentSpace(int xDim, int yDim) {
		super("Chemotaxis", xDim, yDim);

		this.numCells = xDim * yDim;
		for (int i = 0; i < xDimCellspace; i++) {
			for (int j = 0; j < yDimCellspace; j++) {
				Random randomno = new Random();
				Agent cell;
				if (i <= xDimCellspace / 3) {
					cell = new Agent(i, j, statusCXCL12[randomno.nextInt(statusCXCL12.length)], 32);
				} else if (i >= xDimCellspace / 3 * 2) {
					cell = new Agent(i, j, statusCXCLR4[randomno.nextInt(statusCXCLR4.length)], 51);
				} else {
					cell = new Agent(i, j, statusCXCLR7[randomno.nextInt(statusCXCLR7.length)], 73);
				}
				addCell(cell);
				cell.initialize();
			}
		}

		doNeighborToNeighborCoupling();

	}

    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(942, 744);
        ((ViewableComponent)withName("Cell: 1, 0")).setPreferredLocation(new Point(603, 0));
        ((ViewableComponent)withName("Cell: 3, 0")).setPreferredLocation(new Point(598, 182));
        ((ViewableComponent)withName("Cell: 5, 0")).setPreferredLocation(new Point(605, 367));
        ((ViewableComponent)withName("Cell: 3, 1")).setPreferredLocation(new Point(21, 535));
        ((ViewableComponent)withName("Cell: 2, 1")).setPreferredLocation(new Point(18, 326));
        ((ViewableComponent)withName("Cell: 1, 1")).setPreferredLocation(new Point(208, 335));
        ((ViewableComponent)withName("Cell: 0, 1")).setPreferredLocation(new Point(445, 35));
        ((ViewableComponent)withName("Cell: 4, 1")).setPreferredLocation(new Point(408, 217));
        ((ViewableComponent)withName("Cell: 2, 0")).setPreferredLocation(new Point(418, 403));
        ((ViewableComponent)withName("Cell: 5, 1")).setPreferredLocation(new Point(12, 100));
        ((ViewableComponent)withName("Cell: 0, 0")).setPreferredLocation(new Point(589, 558));
        ((ViewableComponent)withName("Cell: 4, 0")).setPreferredLocation(new Point(226, 88));
    }
}