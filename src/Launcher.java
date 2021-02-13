import java.io.IOException;

public class Launcher {

	public static void main(String[] args) throws IOException {
		ProblemParameters problemParameters = new ProblemParameters();
		Data data = new Data(problemParameters.seedNumberforDemandGeneration);
		data.perturb(problemParameters);
		PerformAllSimulations simulation = new PerformAllSimulations(data, problemParameters);
	}
}
