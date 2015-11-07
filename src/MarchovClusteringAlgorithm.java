import java.util.*;
import java.io.*;
import Jama.Matrix;

public class MarchovClusteringAlgorithm {

	/**
	 * @param args
	 */
	private int power = 1;

	public void printer(Matrix adjMatr) {
		printmatrix(adjMatr);
	}

	public void printmatrix(Matrix adjMatr) {
		int counter = 0;
		int arrayDim = adjMatr.getColumnDimension();
		for (int i = 0; i < arrayDim; i++) {
			for (int j = 0; j < arrayDim; j++) {
				if (adjMatr.get(i, j) != 0
				// // && adjMatrix[i][j] < 1
				) {
					counter++;
					System.out.print(adjMatr.get(i, j) + "\t");
				}
			}
			System.out.println();
		}

		System.out.println();
		// System.out.println("counter:- " + counter);
	}

	public int clusterCounter(Matrix adjMatr) {
		int clusterCounter = 0;
		int arrayDim = adjMatr.getColumnDimension();
		for (int i = 0; i < arrayDim; i++) {
			for (int j = 0; j < arrayDim; j++) {
				if (adjMatr.get(i, j) > 0) {
					clusterCounter++;
					break;
				}

			}
		}
		return clusterCounter;
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

	private Matrix matrixNormalizer(Matrix adjMatr) {
		int arrayDim = adjMatr.getColumnDimension();
		// System.out.println(arrayDim);
		for (int i = 0; i < arrayDim; i++) {
			double sum = 0.0;
			for (int j = 0; j < arrayDim; j++) {
				sum = sum + adjMatr.get(j, i);
				// System.out.print("j , i " + j + " " + i + "  value = "
				// + adjMatr.get(j, i) + " ");
			}
			// System.out.println("  sum = " + sum);
			for (int j = 0; j < arrayDim; j++) {
				if (sum > 0)
					adjMatr.set(j, i, (adjMatr.get(j, i) / sum));
				// double round = (adjMatr.get(j, i) * 1000) / 1000;
				double round = Math.round(adjMatr.get(j, i) * 10000.0) / 10000.0;
				adjMatr.set(j, i, round);

			}

		}
		return adjMatr;

	}

	private Matrix matrixMultiplicator(Matrix adjMatr, int power) {
		Matrix baseMatrix = adjMatr;
		Matrix result = null;
		int k = 1;
		while (k < power) {
			Matrix adj = adjMatr;
			result = baseMatrix.times(adj);
			baseMatrix = result;
			k++;
		}

		return baseMatrix;
	}

	private Matrix matrixInflator(Matrix adjMatr, double inflationParameter) {

		int arrayDim = adjMatr.getColumnDimension();
		// System.out.println(arrayDim);
		for (int i = 0; i < arrayDim; i++) {
			double sumOfProduct = 0.0;
			for (int j = 0; j < arrayDim; j++) {
				sumOfProduct = sumOfProduct
						+ Math.pow(adjMatr.get(j, i), inflationParameter);

			}
			// System.out.println("  sum = " + sum);
			for (int j = 0; j < arrayDim; j++) {
				if (sumOfProduct > 0)

					adjMatr.set(
							j,
							i,
							(Math.pow((adjMatr.get(j, i)), inflationParameter) / sumOfProduct));
			}

		}
		// printer(adjMatr);
		return adjMatr;
	}

	public Matrix adjacencyMatrixCreator(File fileName) {// Matrix is created
															// and the mirroring
															// is performed as
															// well
		int arrayDim = maxSizeFinder(fileName) + 1;
		String line = null;
		double[][] adjMatrix = new double[arrayDim][arrayDim];
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
				adjMatrix[Integer.parseInt(strArry[1])][Integer
						.parseInt(strArry[0])] = 1;
			}

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		Matrix adjMatr = new Matrix(adjMatrix);
		adjMatr = identityMatrixAdder(adjMatr);
		return adjMatr;

	}

	private Matrix identityMatrixAdder(Matrix adjMatr) {
		int arrayDim = adjMatr.getColumnDimension();
		for (int i = 0; i < arrayDim; i++) {
			adjMatr.set(i, i, 1);
		}
		return adjMatr;
	}

	private boolean isMatrixEqual(Matrix initialMatrix, Matrix adjMatr) {
		int arrayDim = initialMatrix.getColumnDimension();
		for (int i = 0; i < arrayDim; i++) {
			for (int j = 0; j < arrayDim; j++) {

				if (initialMatrix.get(i, j) != adjMatr.get(i, j)) {
					return false;
				}
			}
		}
		return true;
	}

	public Matrix driverMethod(File fileName, int power,
			double inflationParameter) {

		Matrix adjMatr = adjacencyMatrixCreator(fileName);
		adjMatr = matrixNormalizer(adjMatr);

		adjMatr = converganceFinder(adjMatr, power, inflationParameter, 0);
		return adjMatr;

	}

	private Matrix converganceFinder(Matrix initialMatrix, int power,
			double inflationParameter, int counter) {

		Matrix adjMatr = matrixMultiplicator(initialMatrix, power);

		adjMatr = matrixInflator(adjMatr, inflationParameter);
		adjMatr = matrixNormalizer(adjMatr);
		if (!isMatrixEqual(initialMatrix, adjMatr)) {
			counter++;
			adjMatr = converganceFinder(adjMatr, power, inflationParameter,
					counter);
		}

		return adjMatr;

	}

	public static void main(String[] args) {
		float startTime = System.currentTimeMillis();

		File fileName = new File(
				"/home/jagvir/DataMining_Marchov_clustering_algorithm/dataFiles/new_att.txt");

		MarchovClusteringAlgorithm obj = new MarchovClusteringAlgorithm();
		obj.driverMethod(fileName, 2, 1.3);
		float stopTime = System.currentTimeMillis();
		System.out.println();
		System.out.println("Time taken :- " + (stopTime - startTime));

	}
}
