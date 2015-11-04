import java.util.*;
import java.io.*;

public class MarchovClusteringAlgorithm {

	/**
	 * @param args
	 */

	private void printmatrix(int[][] adjMatrix) {
		int arrayDim = adjMatrix.length;

//		System.out.println(arrayDim + " " + arrayDim);
		for (int i = 0; i < arrayDim; i++) {
			for (int j = 0; j < arrayDim; j++) {
				if (adjMatrix[i][j] > 0 && adjMatrix[i][j] < 1)
					System.out.print(adjMatrix[i][j] + " ");
			}
//			System.out.println();
		}
	}

	private int maxSizeFinder(File fileName) {
		int maxElement = 0;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String[] strArry = line.split("\\s+");
				if (maxElement < Integer.parseInt(strArry[0])) {
					maxElement = Integer.parseInt(strArry[0]);

				}
				if (maxElement < Integer.parseInt(strArry[1])) {
					maxElement = Integer.parseInt(strArry[1]);
				}

			}
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maxElement;
	}

	private void matrixNormalizer(int[][] adjMatrix) {

		int arrayDim = adjMatrix.length;
		for (int i = 0; i < arrayDim; i++) {
			int sum = 0;
			for (int j = 0; j < arrayDim; j++) {
				sum = sum + adjMatrix[j][i];
			}
			for (int j = 0; j < arrayDim; j++) {
				if (adjMatrix[i][0] > 0)
					adjMatrix[j][i] = adjMatrix[j][i] / sum;
			}

		}

	}

	public void adjacencyMatrixCreator(File fileName) {
		int arrayDim = maxSizeFinder(fileName) + 1;// here arrayDim is
													// incremented because the
		System.out.println(arrayDim);
		String line = null;
		int[][] adjMatrix = new int[arrayDim][arrayDim];
		for (int i = 0; i < arrayDim; i++) {
			for (int j = 0; j < arrayDim; j++) {
				adjMatrix[i][j] = 0;
			}
		}
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));

			while ((line = bufferedReader.readLine()) != null) {

				String[] strArry = line.split("\\s+");

				adjMatrix[Integer.parseInt(strArry[0])][Integer
						.parseInt(strArry[1])] = 1;
			}

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

		matrixNormalizer(adjMatrix);
		printmatrix(adjMatrix);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// The name of the file to open.
		float startTime = System.currentTimeMillis();
		File fileName = new File(
				"/home/jagvir/Desktop/Datamining/Data_For_HW3/attweb_net.txt");
		MarchovClusteringAlgorithm obj = new MarchovClusteringAlgorithm();
		obj.adjacencyMatrixCreator(fileName);
		float stopTime = System.currentTimeMillis();
		System.out.println("Time taken :- " + (stopTime - startTime));

	}
}
