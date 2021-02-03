import java.util.Random;
public class PerformAllSimulations {

	private Random random;
	
	public PerformAllSimulations(Data data, ProblemParameters problemParameters) {
		this.random = new Random();
		this.random.setSeed(problemParameters.seedNumberforDemandGeneration);
		for (DistributionType distributionType : DistributionType.values()) {
			for (Scenario s : data.scenario) {
				SimulateOneScenarioAndDistribution simulate = new SimulateOneScenarioAndDistribution(s, problemParameters, distributionType, this.random);
				simulate.run();
			}
		}
	}
}
