import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

public class PerformAllSimulations {

	public Random random;

	public PerformAllSimulations(Data data, ProblemParameters problemParameters) throws IOException {
		this.random = new Random();
		this.random.setSeed(problemParameters.seedNumberforDemandGeneration);
		for (DistributionType distributionType : DistributionType.values()) {
			for (Scenario s : data.scenario) {
				s.deprivationCost.put(distributionType, new ArrayList<Double>());
				s.referralCost.put(distributionType, new ArrayList<Double>());
				s.holdingCost.put(distributionType, new ArrayList<Double>());
				s.cycleLengths.put(distributionType, new ArrayList<ArrayList<Double>>());
				for (int replication = 0; replication < problemParameters.numberOfReplications; replication++) {
					SimulateOneScenarioDist simulate = new SimulateOneScenarioDist(s, problemParameters,
							distributionType, this.random);
					simulate.run();
				}
			}
		}

		FileWriter fileWriter = new FileWriter(problemParameters.outFile);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		bufferedWriter.write("ScenarioID,distribution,replication,deprivationCost,referralCost,holdingCost,replenishmentCycleLengths\n");
		for (DistributionType distributionType : DistributionType.values()) {
			for (Scenario s : data.scenario) {
				for (int replication = 0; replication < problemParameters.numberOfReplications; replication++) {
					bufferedWriter.write(s.ScenarioID + "," + distributionType + "," + (replication + 1) + ","
							+ s.deprivationCost.get(distributionType).get(replication) + ","
							+ s.referralCost.get(distributionType).get(replication) + ","
							+ s.holdingCost.get(distributionType).get(replication));
					ArrayList<Double>printThis = s.cycleLengths.get(distributionType).get(replication);
					for(Double d:printThis)
					{
						bufferedWriter.write(","+d);
					}
					bufferedWriter.write("\n");
				}
			}
		}
		bufferedWriter.close();
		fileWriter.close();
	}
}
