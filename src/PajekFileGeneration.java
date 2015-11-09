import java.io.*;
import Jama.Matrix;
import java.util.*;
import java.util.Map.Entry;

public class PajekFileGeneration {
	public static int clusterCount = 0;
	public static int arrayDim = 0;
	public static int power = 0;
	public static double inflationFactor = 0;
	public static int choice = 0;

	MarchovClusteringAlgorithm MCLObj = new MarchovClusteringAlgorithm();
	public static Matrix initialMatrix = null;
	HashMap<HashSet, Integer> mpOfClusters = new HashMap<HashSet, Integer>();

	private double intraSumCalculator(
			HashMap<ArrayList<Integer>, Integer> mapOfClusters, Matrix adjMatr) {
		double intraSum = 0;
		for (Map.Entry<ArrayList<Integer>, Integer> entrySet : mapOfClusters
				.entrySet()) {
			for (int i = 0; i < entrySet.getKey().size(); i++) {
				for (int j = i + 1; j < entrySet.getKey().size(); j++) {
					if (initialMatrix.get(i, j) == 1) {
						intraSum++;
					}
				}
			}

		}
		return intraSum;
	}

	private HashMap mapReverser(
			HashMap<ArrayList<Integer>, Integer> mapOfClusters) {
		HashMap<Integer, ArrayList<Integer>> revClusterMap = new HashMap<Integer, ArrayList<Integer>>();
		for (Map.Entry<ArrayList<Integer>, Integer> entrySet : mapOfClusters
				.entrySet()) {
			revClusterMap.put(entrySet.getValue(), entrySet.getKey());
		}
		return revClusterMap;
	}

	private double interSumCalculator(
			HashMap<ArrayList<Integer>, Integer> mapOfClusters, Matrix adjMatr) {

		double sumInter = 0.0;
		HashMap<Integer, ArrayList<Integer>> clusterMap = new HashMap<Integer, ArrayList<Integer>>();
		for (Map.Entry<ArrayList<Integer>, Integer> ent : mapOfClusters
				.entrySet()) {
			clusterMap.put(ent.getValue(), ent.getKey());
		}
		Set<Integer> keys = clusterMap.keySet();
		int keyArr[] = new int[keys.size()];
		int i = 0;
		Iterator<Integer> iter = keys.iterator();
		while (iter.hasNext()) {
			keyArr[i] = iter.next();
			i++;
		}
		i = 0;
		for (i = 0; i < keyArr.length - 1; i++) {
			for (int j = i + 1; j < keyArr.length; j++) {
				ArrayList<Integer> listI = clusterMap.get(keyArr[i]);
				ArrayList<Integer> listJ = clusterMap.get(keyArr[j]);
				for (int k = 0; k < listI.size(); k++) {
					for (int l = 0; l < listJ.size(); l++) {
						if (initialMatrix.get(listI.get(k), listJ.get(l)) > 0.0001) {
							sumInter++;
						}
					}
				}
			}
		}
		return sumInter;

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
			nodeToClusterMap.put(i, clusters);
		}
		return nodeToClusterMap;
	}

	private void mapToFileWriter(
			HashMap<Integer, ArrayList<Integer>> nodeToClusterMap) {
		File file = new File(
				"/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/PajekResultFile_"
						+ power + "_" + inflationFactor + "_" + choice + ".txt");

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
				if (!entry.getValue().isEmpty()) {
					String data = (entry.getValue().get(0).toString());
					bw.write(data + "\n");
				}

			}

			bw.close();
		} catch (IOException e) {
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
		return mapOfClusters;

	}

	public void driverMethod(File fileName, int power, double inflationParameter) {
		initialMatrix = MCLObj.adjacencyMatrixCreator(fileName);
		Matrix adjMatr = MCLObj.driverMethod(fileName, power,
				inflationParameter);
		clusterCount = MCLObj.clusterCounter(adjMatr);
		;

		mapToFileWriter(pajekGenerator(clusterMaker(adjMatr)));
		;
		mapReverser(clusterMaker(adjMatr));
		;
		double modularity = Math.abs((interSumCalculator(clusterMaker(adjMatr),
				adjMatr))
				- (intraSumCalculator(clusterMaker(adjMatr), adjMatr)));
		System.out.println("The Modularity is :- " + modularity);
		System.out.println("clusterCount :-" + clusterCount);
	}

	public static void main(String[] args) {
		float startTime = System.currentTimeMillis();
		Scanner input = new Scanner(System.in);
		System.out.println("Enter power/Expansion factor:- ");
		power = input.nextInt();
		System.out.println("Enter Inflation factor:- ");
		inflationFactor = input.nextDouble();
		System.out.println("Press 1 for new_att.txt");
		System.out.println("Press 2 for new_collaboration.txt");
		System.out.println("Press 3 for new_yeast.txt");
		choice = 0;
		choice = input.nextInt();
		String file = "";
		if (choice == 1)
			file = "new_att.txt";
		else if (choice == 2)
			file = "new_collaboration.txt";
		else if (choice == 3)
			file = "new_yeast.txt";
		PajekFileGeneration obj = new PajekFileGeneration();
		String str = "/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/"
				+ file;
		File fileName = new File(str);
		obj.driverMethod(fileName, power, inflationFactor);
		float stopTime = System.currentTimeMillis();
		System.out.println();
		System.out.println("Time taken :- " + (stopTime - startTime));

	}

}
