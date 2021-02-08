import java.util.ArrayList;
import java.util.HashMap;

public class RunOneCycle {
	public double cycleDeprivationCost, cycleReferralCost, cycleHoldingCost;

	public RunOneCycle(double cycleLength, HashMap<Camp, ArrayList<Double>> internalDemand,
			HashMap<Camp, ArrayList<Double>> externalDemand, double alpha, int deltaD, int deltaR) {
		this.cycleDeprivationCost = 0;
		this.cycleReferralCost = 0;
		this.cycleHoldingCost = 0;

		for (Camp c : internalDemand.keySet()) {
			int inventoryLevel = (int) Math.round(c.allocation);
			ArrayList<Double> internalArrivals = internalDemand.get(c);
			ArrayList<Double> externalArrivals = externalDemand.get(c);

			double timeOfPrevArrival=0;
			while (internalArrivals.size()+externalArrivals.size()>0) {
				if (next(internalArrivals,cycleLength) < next(externalArrivals,cycleLength))
				{
					double timeOfArrival = pop(internalArrivals);
					inventoryLevel = arrival(timeOfArrival, timeOfPrevArrival, inventoryLevel, cycleLength, alpha, deltaD);
					timeOfPrevArrival = timeOfArrival;
				} 
				else {
					double timeOfArrival = pop(externalArrivals);
					inventoryLevel = arrival(timeOfArrival, timeOfPrevArrival, inventoryLevel, c.omega, deltaR);
					timeOfPrevArrival = timeOfArrival;
				}
			}
		}
	}

	private double pop(ArrayList<Double> arrivals) {
		double value=arrivals.get(arrivals.size()-1);
		arrivals.remove(arrivals.size()-1);
		return value;
	}

	private double next(ArrayList<Double> arrivals, double cycleLength) {
		if(arrivals.size()==0)
			return cycleLength+1;
		return arrivals.get(arrivals.size()-1);
	}

	private int arrival(double timeOfArrival, double timeOfPrevArrival, int inventoryLevel, double omega, int deltaR) {
		// external arrival
		if (inventoryLevel <= omega) {
			// refer an external arrival elsewhere
			this.cycleReferralCost = this.cycleReferralCost + deltaR;
			this.cycleHoldingCost=this.cycleHoldingCost+inventoryLevel*(timeOfArrival-timeOfPrevArrival);
		}
		else
		{	
			if (inventoryLevel > 0) {
				this.cycleHoldingCost=this.cycleHoldingCost+inventoryLevel*(timeOfArrival-timeOfPrevArrival);
				inventoryLevel = inventoryLevel - 1;
			}
		}
		return inventoryLevel;
	}

	private int arrival(double timeOfArrival, double timeOfPrevArrival, int inventoryLevel, double cycleLength, double alpha, int deltaD) {
		// internal arrival
		if (inventoryLevel > 0) {
			this.cycleHoldingCost=this.cycleHoldingCost+inventoryLevel*(timeOfArrival-timeOfPrevArrival);
			inventoryLevel = inventoryLevel - 1;
		} else {
			// deprivation until the end of the cycle
			this.cycleDeprivationCost = this.cycleDeprivationCost
					+ deltaD * (Math.exp(alpha * (cycleLength - timeOfArrival)) - 1);
		}
		return inventoryLevel;
	}

}
