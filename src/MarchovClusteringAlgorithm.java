import java.util.*;
import java.io.*;

import Jama.Matrix;

public class MarchovClusteringAlgorithm {

	/**
	 * @param args
	 */
	private int power = 1;

	private void printmatrix(float[][] adjMatrix) {
		int arrayDim = adjMatrix.length;
		for (int i = 0; i < arrayDim; i++) {
			for (int j = 0; j < arrayDim; j++) {
				// if (adjMatrix[i][j] > 0
				// // && adjMatrix[i][j] < 1
				// )
				System.out.print(adjMatrix[i][j] + "\t");
			}
			System.out.println();
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

	private float[][] matrixNormalizer(float[][] adjMatrix) {
		int arrayDim = adjMatrix.length;
		// System.out.println(arrayDim);
		for (int i = 0; i < arrayDim; i++) {
			float sum = 0;
			for (int j = 0; j < arrayDim; j++) {
				sum = sum + adjMatrix[j][i];
				// System.out.println("j , i "+j+" "+i);
			}
			// System.out.println("sum = "+sum);
			for (int j = 0; j < arrayDim; j++) {
				if (sum > 0)
					adjMatrix[j][i] = adjMatrix[j][i] / sum;
			}

		}
		// printmatrix(adjMatrix);
		return adjMatrix;

	}

	private Matrix matrixMultiplicator(double[][] adjMatrix, int power) {
		Matrix  baseMatrix = new Matrix(adjMatrix);
		Matrix result= null;
		int k= 1;
		while(k<power){
			Matrix adj = new Matrix(adjMatrix);
			result = baseMatrix.times(adj);
			baseMatrix = result;
			k++;
		}
		
		return baseMatrix;
	}

	private void matrixInflator() {
	}

	public void adjacencyMatrixCreator(File fileName) {
		int arrayDim = maxSizeFinder(fileName) + 1;// here arrayDim is

		// incremented because the
		// System.out.println(arrayDim);
		System.out.println("Max element is :- " + arrayDim);
		String line = null;
		float[][] adjMatrix = new float[arrayDim][arrayDim];
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
		// printmatrix(adjMatrix);
		adjMatrix = matrixNormalizer(adjMatrix);
		printmatrix(adjMatrix);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// The name of the file to open.
		float startTime = System.currentTimeMillis();
		// File fileName = new File(
		// "/home/jagvir/Desktop/Datamining/Data_For_HW3/yeast_undirected_metabolic.txt");
		// File fileName = new File(
		// "/home/jagvir/Desktop/Datamining/Data_For_HW3/attweb_net.txt");
		File fileName = new File(
				"/home/jagvir/Desktop/Datamining/Data_For_HW3/temp.txt");

		MarchovClusteringAlgorithm obj = new MarchovClusteringAlgorithm();
		obj.adjacencyMatrixCreator(fileName);
		float stopTime = System.currentTimeMillis();
		System.out.println("Time taken :- " + (stopTime - startTime));

	}
}
