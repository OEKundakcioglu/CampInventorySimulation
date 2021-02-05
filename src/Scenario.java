import java.util.ArrayList;
import java.util.HashMap;

public class Scenario {
	public String ScenarioID;
	public ArrayList<Camp> camp;
	public double mu;
	public int deltaD, deltaR;
	public double alpha;
	public HashMap<DistributionType,ArrayList<Double>> deprivationCost, referralCost, holdingCost;
	public HashMap<DistributionType,ArrayList<ArrayList<Double>>> cycleLengths;
	
	public Scenario()
	{
		this.deprivationCost=new HashMap<DistributionType,ArrayList<Double>>();
		this.referralCost=new HashMap<DistributionType,ArrayList<Double>>();
		this.holdingCost=new HashMap<DistributionType,ArrayList<Double>>();
		this.cycleLengths=new HashMap<DistributionType,ArrayList<ArrayList<Double>>>();
	}
}
