import java.io.*;
import Jama.Matrix;
import java.util.*;
import java.util.Map.Entry;

public class PajekFileGeneration {
	public static int clusterCount = 0;
	public static int arrayDim = 0;
	MarchovClusteringAlgorithm MCLObj = new MarchovClusteringAlgorithm();
	HashMap<HashSet, Integer> mpOfClusters = new HashMap<HashSet, Integer>();

	private void intraSumCalculator(
			HashMap<ArrayList<Integer>, Integer> mapOfClusters, Matrix adjMatr) {
		int intraSum = 0;
		for (Map.Entry<ArrayList<Integer>, Integer> entrySet : mapOfClusters
				.entrySet()) {
			for (int i = 0; i < entrySet.getKey().size(); i++) {
				for (int j = i + 1; j < entrySet.getKey().size(); j++) {
						if(adjMatr.get(i, j)==1){
							intraSum++;
						}
				}
			}

		}
		System.out.println("intra Sum = "+intraSum);

	}

	private HashMap pajekGenerator(
			HashMap<ArrayList<Integer>, Integer> mapOfClusters) {
		// System.out.println(arrayDim);
		// MCLObj.printer(adjMatr);
		// int arrayDim =
		HashMap<Integer, ArrayList<Integer>> nodeToClusterMap = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i < arrayDim; i++) {
			ArrayList<Integer> clusters = new ArrayList<Integer>();
			for (Map.Entry<ArrayList<Integer>, Integer> ent : mapOfClusters
					.entrySet()) {
				ArrayList<Integer> list = ent.getKey();

				if (list.contains(i))
					clusters.add(ent.getValue());
			}
			// if(!clusters.isEmpty())
			nodeToClusterMap.put(i, clusters);
		}
		System.out.println("NodeToClusterMap size:" + nodeToClusterMap.size());
		System.out.println(nodeToClusterMap);
		return nodeToClusterMap;
	}

	private void mapToFileWriter(
			HashMap<Integer, ArrayList<Integer>> nodeToClusterMap) {
		File file = new File(
				"/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/PajekResultFile.txt");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("*Partition PartitionName\n");
			bw.write("*Vertices ");
			Integer vertices = arrayDim - 1;
			String str = vertices.toString();
			bw.write(str + "\n");

			for (Map.Entry<Integer, ArrayList<Integer>> entry : nodeToClusterMap
					.entrySet()) {
				// System.out.println(" Value : " + entry.getValue());

				String data = (entry.getValue().get(0).toString());
				bw.write(data + "\n");
				// if (entry.getValue() != null) {
				// int data = entry.getValue().get(0);
				// // System.out.println(data);
				// bw.write(data);
				// }
			}

			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private HashMap clusterMaker(Matrix adjMatr) {
		int clusterId = 0;
		arrayDim = adjMatr.getColumnDimension();
		HashMap<ArrayList<Integer>, Integer> mapOfClusters = new HashMap<ArrayList<Integer>, Integer>();
		for (int i = 0; i < arrayDim; i++) {
			ArrayList<Integer> list = new ArrayList<Integer>();
			for (int j = 0; j < arrayDim; j++) {
				if (adjMatr.get(i, j) == 1) {
					list.add(j);
				}

			}
			if (!list.isEmpty()) {
				if (!mapOfClusters.containsKey(list))
					mapOfClusters.put(list, clusterId++);
			}
		}
		// System.out.println("Total number of clusters:" +
		// mapOfClusters.size());
		// System.out.println(mapOfClusters);
		return mapOfClusters;

	}

	public void driverMethod(File fileName, int power, int inflationParameter) {
		Matrix adjMatr = MCLObj.driverMethod(fileName, power,
				inflationParameter);
		clusterCount = MCLObj.clusterCounter(adjMatr);
		;
		mapToFileWriter(pajekGenerator(clusterMaker(adjMatr)));
		intraSumCalculator(clusterMaker(adjMatr), adjMatr);

	}

	public static void main(String[] args) {
		File fileName = new File(
				"/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/new_att.txt");

		PajekFileGeneration obj = new PajekFileGeneration();
		obj.driverMethod(fileName, 2, 2);

	}

}
