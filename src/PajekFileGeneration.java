import java.io.File;
import Jama.Matrix;

public class PajekFileGeneration {
	MarchovClusteringAlgorithm MCLObj = new MarchovClusteringAlgorithm();

	private void pajekGenerator(Matrix adjMatr) {
		MCLObj.printer(adjMatr);
	}
public void driverMethod(File fileName,int power , int inflationParameter){
	Matrix adjMatr = MCLObj.driverMethod(fileName, power, inflationParameter);
	pajekGenerator(adjMatr);
	
	
}
	public static void main(String[] args) {
		File fileName = new File(
				"/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/new_att.txt");

		PajekFileGeneration obj = new PajekFileGeneration();
		obj.driverMethod(fileName , 2,2);

	}

}
