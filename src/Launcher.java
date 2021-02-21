import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Launcher {

	public static void main(String[] args) throws IOException {
		double[] perturbValues= {0.05,0.1,0.15,0.2};
		Data copyData=null;
		
		for(SensitivityType st : SensitivityType.values())
		{
			for(double perturbValue : perturbValues)
			{
				System.out.print("Sensitivity "+st);
				ProblemParameters problemParameters = new ProblemParameters();
				Data data = new Data(problemParameters.seedNumberforDemandGeneration);
				CopyAllReplenishments(copyData,data);
				problemParameters.st = st;
				if (st.equals(SensitivityType.OPTIMAL)) {
					problemParameters.perturbationRatio = 0;	
					System.out.println();
					copyData=perturbAndRun(data,problemParameters);
					break;
				}
				else
				{
					problemParameters.perturbationRatio = perturbValue;	
					System.out.println(" perturbed "+perturbValue);
					copyData=perturbAndRun(data,problemParameters);
				}
			}
		}
		
	}

	private static void CopyAllReplenishments(Data copyData, Data data) {
		if(copyData!=null)
		{
			for(Scenario s : data.scenario)
			{
				s.cycleLengths=FindReplenishments(copyData.scenario,s.ScenarioID);
				s.copied=true;
			}
		}
		
	}

	private static HashMap<DistributionType, ArrayList<ArrayList<Double>>> FindReplenishments(
			ArrayList<Scenario> scenario, String scenarioID) {
		for(Scenario s : scenario)
		{
			if(s.ScenarioID.equals(scenarioID))//same scenario found
			{
				return s.cycleLengths;
			}
		}
		return null;
	}

	private static Data perturbAndRun(Data data, ProblemParameters problemParameters) throws IOException {
		data.perturb(problemParameters);
		PerformAllSimulations simulation = new PerformAllSimulations(data, problemParameters);
		return data;
	}
}
