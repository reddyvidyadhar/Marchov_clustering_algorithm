import java.io.File;
import java.io.IOException;

import Jama.Matrix;
import java.util.*;

public class PajekFileGeneration {
	public static int arrayDim = 0;
	MarchovClusteringAlgorithm MCLObj = new MarchovClusteringAlgorithm();
	HashMap<HashSet, Integer> mpOfClusters = new HashMap<HashSet, Integer>();

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
		File file = new File("PajekResultFile");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
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
		;
		mapToFileWriter(pajekGenerator(clusterMaker(adjMatr)));

	}

	public static void main(String[] args) {
		File fileName = new File(
				"/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/new_att.txt");

		PajekFileGeneration obj = new PajekFileGeneration();
		obj.driverMethod(fileName, 2, 2);

	}

}
