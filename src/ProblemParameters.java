public class ProblemParameters {

	// parameters for random generation
	public int seedNumberforDemandGeneration = 13;
	public int numberofCycles = 10;
	public int numberOfReplications=1000;
	public double epsilon = 0.0001;
	public double perturbationRatio = 0;
	public SensitivityType st = SensitivityType.OPTIMAL;

	public String outFile = "output.csv";
}
