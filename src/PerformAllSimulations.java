import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
public class PerformAllSimulations {

	public Random random;
	
	public PerformAllSimulations(Data data, ProblemParameters problemParameters) throws IOException {
		this.random = new Random();
		this.random.setSeed(problemParameters.seedNumberforDemandGeneration);
		for (DistributionType distributionType : DistributionType.values()) {
			for (Scenario s : data.scenario) {
				SimulateOneScenarioDist simulate = new SimulateOneScenarioDist(s, problemParameters, distributionType, this.random);
				simulate.run();
			}
		}
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(problemParameters.outFile));
		bufferedWriter.write("ScenarioID,deprivationCost,referralCost,holdingCost\n");
		
		for (Scenario s : data.scenario) {
			for (DistributionType distributionType : DistributionType.values())
			{
				bufferedWriter.write(s.ScenarioID+","+s.deprivationCost.get(distributionType)+","+s.referralCost.get(distributionType)+","+s.holdingCost.get(distributionType)+"\n");
			}
		}
		
	}
}
