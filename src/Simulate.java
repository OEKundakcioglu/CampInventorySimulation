import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Simulate {
	private Scenario s;
	private ProblemParameters problemParameters;
	private DistributionType distributionType;
	private ArrayList<Double> ReplenishmentCycleLengths;

	public Simulate(Scenario s, ProblemParameters problemParameters, DistributionType distributionType) {
		this.s = s;
		this.problemParameters = problemParameters;
		this.distributionType = distributionType;
		GenerateReplenishmentTimes();
	}

	private void GenerateReplenishmentTimes() {
		int numberOfCycles = this.problemParameters.replication;
		double rate = s.mu;
		DistributionType distribution = this.distributionType;
		// generate numberOfCycles RVs with rate and distribution
		// uniform would be different as incremental times will be used.
		// ...
	}

	public void run() {
		for (double cycleLength : ReplenishmentCycleLengths) {
			HashMap<Camp, ArrayList<Double>> InternalDemand = new HashMap<Camp, ArrayList<Double>>();
			HashMap<Camp, ArrayList<Double>> ExternalDemand = new HashMap<Camp, ArrayList<Double>>();
			for (Camp c : s.camp) {
				InternalDemand.put(c, GenerateArrivals(c.lambdaC, cycleLength));
				ExternalDemand.put(c, GenerateArrivals(c.lambdaS, cycleLength));
			}
			RunOneCycle cycle = new RunOneCycle(cycleLength, InternalDemand, ExternalDemand, s.alpha, s.deltaD,
					s.deltaR);
		}
	}

	private ArrayList<Double> GenerateArrivals(double rate, double cycleLength) {
		// generate Poisson arrivals with rate until cycleLength
		ArrayList<Double> arrivals = new ArrayList<Double>();
		// ...

		Collections.reverse(arrivals); // reversed so that efficiently removed from the end of the list
		return arrivals;
	}

	public void reportKPI() {
		// TODO Auto-generated method stub

	}

}
