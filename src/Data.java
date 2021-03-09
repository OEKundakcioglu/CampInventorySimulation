import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;

public class Data {
	public ArrayList<Scenario> scenario = new ArrayList<Scenario>();
	Random random;

	public Data(int seed)
	{
		this.random = new Random();
		this.random.setSeed(seed);
			
		try {
			FileReader fileReader = new FileReader("inputData.csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			
			line= bufferedReader.readLine();
			while(line!=null)
			{
				String[] names=line.split(",");
				Scenario thisScenario = findScenario(names);
				populateRow(thisScenario,names);
				line= bufferedReader.readLine();
			}
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	}

	private void populateRow(Scenario thisScenario,String[] thisRow) {
		Camp c = new Camp();
		c.campName=thisRow[1];
		c.lambdaC=Double.parseDouble(thisRow[6]);
		c.lambdaS=Double.parseDouble(thisRow[7]);
		c.omega=Double.parseDouble(thisRow[8]);
		c.allocation=Double.parseDouble(thisRow[10]);
		thisScenario.camp.add(c);
	}

	private Scenario findScenario(String[] thisRow) {
		Scenario thisScenario=null;
		for(Scenario s : this.scenario)
		{
			if(s.ScenarioID.equals(thisRow[0]))
			{
				thisScenario = s;
				break;
			}
		}
	
		if(thisScenario == null)
		{
			thisScenario=createScenario(thisRow);
		}
		return thisScenario;
	}

	private Scenario createScenario(String[] thisRow) {
		Scenario s = new Scenario();
		s.ScenarioID = thisRow[0];
		s.mu = Double.parseDouble(thisRow[2]);
		s.deltaD = (int) Math.round(Double.parseDouble(thisRow[3]));
		s.alpha = Double.parseDouble(thisRow[4]);
		s.deltaR = (int) Math.round(Double.parseDouble(thisRow[5]));
		s.camp=new ArrayList<Camp>();
		this.scenario.add(s);
		return s;
	}

	public void perturb(ProblemParameters problemParameters) {
		if(problemParameters.st!=SensitivityType.OPTIMAL)
		{
			double ratio=problemParameters.perturbationRatio;
			
			problemParameters.outFile="perturbed"+ratio+problemParameters.st+problemParameters.outFile;
				
			if(problemParameters.st.equals(SensitivityType.RANDOM)) {
				for(Scenario s:this.scenario) {
					double toBeMoved=0;
					int numberofCamps=s.camp.size();
					for(Camp c:s.camp) {
						toBeMoved+=c.allocation;
					}
					toBeMoved=toBeMoved*ratio;
					
					ArrayList<Camp> givers = new ArrayList<Camp>();
					
					// at least half of the camps are chosen randomly, such that their allocations exceed the amount to be moved 
					double giversTotalAllocation=0;
					while(givers.size()<numberofCamps/2 || giversTotalAllocation<toBeMoved) {
						Camp addCamp = RandomlyChooseExcluding(s.camp,givers);
						giversTotalAllocation+=addCamp.allocation;
						givers.add(addCamp);
					}
					
					// based on the selection, we determine the ratio of inventory for all camps to be moved out
					double updatedRatio = toBeMoved / giversTotalAllocation;
					for(Camp c:givers) {
						int moveAmount = (int) (updatedRatio * c.allocation); 
						c.allocation-=moveAmount;
						Camp receiver = RandomlyChooseExcluding(s.camp,givers);
						receiver.allocation+=moveAmount;
					}
				}
			}
			else
			{
				for(Scenario s:this.scenario) {
					int numberofCamps=s.camp.size();
					ArrayList<Camp> givers = new ArrayList<Camp>();
					ArrayList<Camp> receivers = new ArrayList<Camp>();
					for(int i=0;i<numberofCamps/2;i++) {
						Camp giver = FindMaxExcludingandUpdateList(s.camp,givers);
						Camp receiver = FindMinExcludingandUpdateList(s.camp,receivers);
						int moveAmount = (int) (ratio * giver.allocation); 
						giver.allocation-=moveAmount;
						receiver.allocation+=moveAmount;
					}
					
				}
			}
		}		
	}

	private Camp FindMinExcludingandUpdateList(ArrayList<Camp> camp, ArrayList<Camp> receivers) {
		Camp thisCamp=null;
		double minValue=Integer.MAX_VALUE;
		for(Camp c:camp)
		{
			if(c.allocation<minValue && !receivers.contains(c))
			{
				minValue=c.allocation;
				thisCamp=c;
			}
		}
		receivers.add(thisCamp);
		return thisCamp;
	}

	private Camp FindMaxExcludingandUpdateList(ArrayList<Camp> camp, ArrayList<Camp> givers) {
		Camp thisCamp=null;
		double maxValue=0;
		for(Camp c:camp)
		{
			if(c.allocation>maxValue && !givers.contains(c))
			{
				maxValue=c.allocation;
				thisCamp=c;
			}
		}
		givers.add(thisCamp);
		return thisCamp;
	}

	private Camp RandomlyChooseExcluding(ArrayList<Camp> camp, ArrayList<Camp> givers) {
		Camp thisCamp=null;
		do {
			int selection = this.random.nextInt(camp.size());
			thisCamp=camp.get(selection);
		}while(givers.contains(thisCamp));
		return thisCamp;
	}
}