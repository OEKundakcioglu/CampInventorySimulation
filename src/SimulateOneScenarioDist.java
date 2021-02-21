import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class SimulateOneScenarioDist {
	private Scenario s;
	private ProblemParameters problemParameters;
	private DistributionType distributionType;
	private ArrayList<Double> ReplenishmentCycleLengths;
	private Random random;
	private int replication;

	public double deprivationCost, referralCost, holdingCost;

	public SimulateOneScenarioDist(Scenario s, ProblemParameters problemParameters, DistributionType distributionType,
			Random random, int replication) {
		this.s = s;
		this.problemParameters = problemParameters;
		this.distributionType = distributionType;
		this.random = random;
		this.deprivationCost = 0;
		this.referralCost = 0;
		this.holdingCost = 0;
		this.replication = replication;
		if(!s.copied)
		{
			GenerateReplenishmentTimes();
			s.cycleLengths.get(distributionType).add(this.ReplenishmentCycleLengths);	
		}
		else
		{
			this.ReplenishmentCycleLengths = s.cycleLengths.get(distributionType).get(replication);
		}
	}

	private void GenerateReplenishmentTimes() {
		int numberOfCycles = this.problemParameters.numberofCycles;
		double rate = s.mu;
		DistributionType distribution = this.distributionType;
		// generate numberOfCycles RVs with rate and distribution
		// uniform would be different as incremental times will be used.

		this.ReplenishmentCycleLengths = new ArrayList<Double>();

		if (distribution == DistributionType.EXPONENTIAL) {
			for (int i = 0; i < numberOfCycles; i++) {
				this.ReplenishmentCycleLengths.add(getNextExponential(rate));
			}
		}
		if (distribution == DistributionType.LOGNORMAL) {
			for (int i = 0; i < numberOfCycles; i++) {
				this.ReplenishmentCycleLengths.add(getNextLogNormal(rate));
			}
		}
		if (distribution == DistributionType.UNIFORMFROMGENERATED) {
			this.ReplenishmentCycleLengths = getAllUniforms(numberOfCycles, sumAllCycles(this.s.cycleLengths.get(DistributionType.EXPONENTIAL).get(replication)));
		}
		if (distribution == DistributionType.UNIFORMFROMEXPO) {
			this.ReplenishmentCycleLengths = getAllUniforms(numberOfCycles, getNextExponential(rate*numberOfCycles));
		}
	}

	private double sumAllCycles(ArrayList<Double> arrayList) {
		double sum=0;
		for(double d : arrayList) {
			sum+=d;
		}
		return sum;
	}

	public void run() {
		int counter = 0;
		for (double cycleLength : this.ReplenishmentCycleLengths) {
			HashMap<Camp, ArrayList<Double>> InternalDemand = new HashMap<Camp, ArrayList<Double>>();
			HashMap<Camp, ArrayList<Double>> ExternalDemand = new HashMap<Camp, ArrayList<Double>>();
			for (Camp c : s.camp) {
				InternalDemand.put(c, GenerateArrivals(c.lambdaC, cycleLength));
				ExternalDemand.put(c, GenerateArrivals(c.lambdaS, cycleLength));
			}
			RunOneCycle cycle = new RunOneCycle(cycleLength, InternalDemand, ExternalDemand, s.alpha, s.deltaD,
					s.deltaR);
			this.deprivationCost += cycle.cycleDeprivationCost;
			this.referralCost += cycle.cycleReferralCost;
			this.holdingCost += cycle.cycleHoldingCost;
		}
		this.s.deprivationCost.get(this.distributionType).add(this.deprivationCost);
		this.s.referralCost.get(this.distributionType).add(this.referralCost);
		this.s.holdingCost.get(this.distributionType).add(this.holdingCost);
	}

	private ArrayList<Double> getAllUniforms(int numberOfCycles, double totalHorizonLength) {
		ArrayList<Double> occurrences = new ArrayList<Double>();
		
		for (int i = 0; i < numberOfCycles - 1; i++) {
			occurrences.add(this.random.nextDouble() * totalHorizonLength);
		}
		Collections.sort(occurrences);

		return computeInterarrivals(occurrences, totalHorizonLength);
	}

	private ArrayList<Double> computeInterarrivals(ArrayList<Double> occurrences, double totalHorizonLength) {
		ArrayList<Double> cycleLengths = new ArrayList<Double>();
		cycleLengths.add(occurrences.get(0));

		for (int i = 1; i < occurrences.size(); i++) {
			cycleLengths.add(occurrences.get(i) - occurrences.get(i - 1));
		}

		cycleLengths.add(totalHorizonLength - occurrences.get(occurrences.size() - 1));

		return cycleLengths;
	}

	private Double getNextLogNormal(double rate) {
		// we'd like to make sure mean is 1/rate, std dev is 1/rate
		// in case of LogNormal mean is exp(mu+sigma^2/2)
		// variance is [exp(sigma^2)-1]exp(2mu+sigma^2) = [exp(sigma^2)-1]mean^2 =
		// [exp(sigma^2)-1]1/rate^2 should be equal to 1/rate^2
		// exp(sigma^2) = 2 -> sigma = sqrt(ln(2))
		// in that case mean is exp(mu+ln(2)/2)=sqrt(2) e^mu
		// for this to be 1/rate -> mu=-ln([sqrt(2)rate])
		double mu = -Math.log(Math.sqrt(2) * rate);
		double sigma = Math.sqrt(Math.log(2));
		double stdNormal = this.random.nextGaussian();
		return Math.exp(sigma * stdNormal + mu);
	}

	private ArrayList<Double> GenerateArrivals(double rate, double cycleLength) {
		// generate Poisson arrivals with rate until cycleLength
		// DO NOT EXCEED CYCLE LENGTH!
		ArrayList<Double> arrivals = new ArrayList<Double>();
		double time = 0;
		do {
			double nextArrival = getNextExponential(rate);
			nextArrival = nextArrival + time;
			arrivals.add(nextArrival);
			time = nextArrival;
		} while (time < cycleLength);

		arrivals.remove(arrivals.size() - 1);
		Collections.reverse(arrivals); // reversed so that efficiently removed from the end of the list

		return arrivals;
	}

	private double getNextExponential(double rate) {
		return Math.log(1 - this.random.nextDouble()) / (-rate);
	}
}
