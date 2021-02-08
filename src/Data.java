import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class Data {
	public ArrayList<Scenario> scenario = new ArrayList<Scenario>();

	public Data()
	{
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
		c.allocation=Double.parseDouble(thisRow[18]);
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
}