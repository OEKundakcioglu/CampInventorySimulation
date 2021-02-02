import java.util.ArrayList;
import java.util.HashMap;

public class RunOneCycle {
	private HashMap<Camp, Integer> inventoryLevel;

	public RunOneCycle(double cycleLength, HashMap<Camp, ArrayList<Double>> internalDemand,
			HashMap<Camp, ArrayList<Double>> externalDemand, double alpha, int deltaD, int deltaR) {
		inventoryLevel=new HashMap<Camp, Integer>();
		for(Camp c:internalDemand.keySet())
		{
			inventoryLevel.put(c, (int) Math.round(c.allocation));
		}
		

	}

}
