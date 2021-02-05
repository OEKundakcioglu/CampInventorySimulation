import java.util.ArrayList;
import java.util.HashMap;

public class Scenario {
	public String ScenarioID;
	public ArrayList<Camp> camp;
	public double mu;
	public int deltaD, deltaR;
	public double alpha;
	public HashMap<DistributionType,Double> deprivationCost, referralCost, holdingCost;
	public Scenario()
	{
		this.deprivationCost=new HashMap<DistributionType,Double>();
		this.referralCost=new HashMap<DistributionType,Double>();
		this.holdingCost=new HashMap<DistributionType,Double>();
	}
}
