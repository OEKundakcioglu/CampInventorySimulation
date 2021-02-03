public class PerformAllSimulations {

	public PerformAllSimulations(Data data, ProblemParameters problemParameters) {
		for (Scenario s : data.scenario) {
			for (DistributionType distributionType : DistributionType.values()) {
				Simulate simulate = new Simulate(s, problemParameters, distributionType);
				simulate.run();
			}
		}
	}
}
