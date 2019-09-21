/*
 *  Copyright Denny Hermawanto (d_3_nny@yahoo.com) - 2006
 *	Genetic Algorithm to determine variable a,b,c,d
 *	that satisfied equation a+2b+3c+4d=30
 *
 */
class GAProject{

	private double RandomNumberGenerator(){
		java.util.Random rnd = new java.util.Random();//new Date().getTime());
		return rnd.nextDouble();
	}

	private double[][] InitializePopulation(){
		double[][] result = new double[numberofpopulation][chromosomelength];
		for(int i=0;i<numberofpopulation;i++){
			for(int j=0;j<chromosomelength;j++){
				result[i][j] = RandomNumberGenerator()/100;
			}
		}
		return result;
	}

	private double[][] ApplyConstraints(double[][] chromosomes){
		double[][] result = new double[numberofpopulation][chromosomelength];
		for(int i=0;i<numberofpopulation;i++){
			for(int j=0;j<chromosomelength;j++){
				result[i][0] = Math.floor(chromosomes[i][0]*Integer.MAX_VALUE) % 30;
				result[i][1] = Math.floor(chromosomes[i][1]*Integer.MAX_VALUE) % 10;
				result[i][2] = Math.floor(chromosomes[i][2]*Integer.MAX_VALUE) % 10;
				result[i][3] = Math.floor(chromosomes[i][3]*Integer.MAX_VALUE) % 10;
			}
		}
		return result;
	}

	private double ObjectiveFunction(double[] chromosome){
		double result;
		result = chromosome[0] + (2*chromosome[1]) + (3*chromosome[2]) + (4*chromosome[3]);
		return result;
	}

	private double[] ComputeFitnesses(double[][] chromosome){
		double[] result = new double[numberofpopulation];
		for(int i=0;i<numberofpopulation;i++){
			result[i] = Math.abs(ObjectiveFunction(chromosome[i]) - 30);
//			System.out.println("Fit:"+result[i]);
		}
		return result;
	}

	private double ComputeSingleFitnesses(double[] chromosome){
		double result;
		result = Math.abs(ObjectiveFunction(chromosome) - 30);
		return result;
	}

	private double[] ComputeSelectionProbability(double[] fitnessvalue){
		double[] result = new double[numberofpopulation];
		double totalfitness = 0;

		for(int i=0;i<fitnessvalue.length;i++){
			totalfitness += 1/fitnessvalue[i];
		}

		for(int i=0;i<numberofpopulation;i++){
			//result[i] = fitnessvalue[i] / totalfitness;
			//the minimum fitness has bigger prob
			result[i] = ((1/fitnessvalue[i]) / totalfitness);
			//System.out.println("Selec prob:"+result[i]);
		}
		return result;
	}

	private double[][] SelectionProcess(double[][] chromosome, double[] selectionprobability){
		double[][] result = new double[numberofpopulation][chromosomelength];

		double[] cumulativeprobability = new double[numberofpopulation];
		double tempofcumulative = 0;
		for(int i=0;i<numberofpopulation;i++){
			cumulativeprobability[i] = selectionprobability[i] + tempofcumulative;
			tempofcumulative = cumulativeprobability[i];
			//System.out.println("Cum:"+tempofcumulative);
		}

		double temp;
		int index = 0;
		for(int i=0;i<numberofpopulation;i++){
			temp = RandomNumberGenerator();
			while(temp>cumulativeprobability[index]){
				index++;
			}
			result[i] = chromosome[index];
		}
		return result;
	}

	private double[][] Crossover(double[][] chromosome){
		double[][] result = new double[numberofpopulation][chromosomelength];
		double[] r = new double[numberofpopulation];
		int[] parent = new int[numberofpopulation];
		int counter = 0;

		for(int i=0;i<numberofpopulation;i++){
			r[i] = RandomNumberGenerator();
			if(r[i]<crossoverprobability){
				counter++;
				parent[counter] = i;
			}
			result[i] = chromosome[i];
		}
		if(counter>1){
			for(int i=0;i<counter;i++){
				int cutpoint = (int)(Math.random()*Integer.MAX_VALUE)%(chromosomelength-1);
				if(i==counter-1){
					for(int j=cutpoint;j<chromosomelength;j++){
						result[parent[i]][j] = chromosome[parent[1]][j];
					}
				}
				else{
					for(int j=cutpoint;j<chromosomelength;j++){
						result[parent[i]][j] = chromosome[parent[i+1]][j];
					}
				}
			}
		}
		return result;
	}

	private double[][] Mutation(double[][] chromosome){
		double[][] result = new double[numberofpopulation][chromosomelength];
		int totalsubchromosome=0;
		int[] subchromosomenumber;
		int[][] mutatedchromosome;
		int index = 0;

		totalsubchromosome = numberofpopulation * chromosomelength;
		subchromosomenumber = new int[totalsubchromosome];

		for(int i=0;i<totalsubchromosome;i++){
			double r = RandomNumberGenerator();
			if(r<mutationprobability){
				subchromosomenumber[index] = i;
				index++;
				//System.out.println("Mutation");
			}
		}
		mutatedchromosome = new int[index][2];
		//int chromnum,mutateposition;
		for(int i=0;i<index;i++){
			mutatedchromosome[i][0] = subchromosomenumber[i] / chromosomelength;
			//chromnum = mutatedchromosome[i][0];
			mutatedchromosome[i][1] = subchromosomenumber[i] % chromosomelength;
			//mutateposition = mutatedchromosome[i][1];
		}
		for(int i=0;i<numberofpopulation;i++){
			for(int j=0;j<chromosomelength;j++){
				result[i][j] = chromosome[i][j];
			}
		}
		for(int i=0;i<index;i++){
			result[mutatedchromosome[i][0]][mutatedchromosome[i][1]]=Math.floor(RandomNumberGenerator()*Integer.MAX_VALUE) % 30;
		}
		return result;
	}

/*	private double[] SortChromosome(double[] chromosomefitness){
		int totalnumber =chromosomefitness.length;
		double[] result = new double[totalnumber];
		double temp;
		for(int i=0;i<totalnumber;i++){
			result[i] = chromosomefitness[i];
		}
		for(int i=1;i<=totalnumber-1;i++){
			for(int j=i;j<totalnumber;j++){
				if(result[i-1]>result[j]){
					temp = result[i-1];
					result[i-1] = result[j];
					result[j] = temp;
				}
			}
		}
		return result;
	} */

	private double[][] SortChromosome(double[][] chromosome){
		double[][] result = new double[numberofpopulation][chromosomelength];
		//double fitnessvaluebefore;
		//double fitnessvaluecurrent;
		double[] tempchromosome = new double[chromosomelength];

		for(int i=0;i<numberofpopulation;i++){
			for(int j=0;j<chromosomelength;j++){
				result[i][j] = chromosome[i][j];
			}
		}
		for(int i=1;i<=numberofpopulation-1;i++){
			for(int j=i;j<numberofpopulation;j++){
				double fitnessvaluebefore = ComputeSingleFitnesses(result[i-1]);
				double fitnessvaluecurrent = ComputeSingleFitnesses(result[j]);
				if(fitnessvaluebefore>fitnessvaluecurrent){
					tempchromosome = result[i-1];
					result[i-1] = result[j];
					result[j] = tempchromosome;
				}
			}
		}
		return result;
	}

	public void PrintChromosome(double[][] chromosome){
		for(int i=0;i<numberofpopulation;i++){
			for(int j=0;j<chromosomelength;j++){
				System.out.print(chromosome[i][j]+";");
			}
			System.out.println("");
		}
	}

	public void PrintFitnesses(double[] chromosomefitness){
		//for(int i=0;i<chromosomefitness.length;i++){
		System.out.print("Best Fitness:"+chromosomefitness[0]);
		//}
		System.out.println("");
	}

	public void PrintBestChromosome(double[] bestchromosome){
		System.out.println("Result:");
		System.out.println("A=" + bestchromosome[0]);
		System.out.println("B=" + bestchromosome[1]);
		System.out.println("C=" + bestchromosome[2]);
		System.out.println("D=" + bestchromosome[3]);
		System.out.println("");
		double result = bestchromosome[0]+(2*bestchromosome[1])+(3*bestchromosome[2])+(4*bestchromosome[3]);
		System.out.println("("+bestchromosome[0]+")+2("+bestchromosome[1]+")+3("+
		bestchromosome[2]+")+4("+bestchromosome[3]+")="+result);
	}

	public void RunGA(){
		chromosomes = InitializePopulation();
		chromosomes = ApplyConstraints(chromosomes);
		System.out.println("Init Population");
		PrintChromosome(chromosomes);
		for(int i=0;i<numberofgeneration;i++){
			chromosomefitness = ComputeFitnesses(chromosomes);
			chromosomeselectionprobability = ComputeSelectionProbability(chromosomefitness);
			newchromosome = SelectionProcess(chromosomes, chromosomeselectionprobability);
			newchromosome = Crossover(newchromosome);
			newchromosome = Mutation(newchromosome);
			chromosomes = newchromosome;
		}
		SortChromosome(chromosomes);
		//chromosomefitness = SortChromosome(chromosomefitness);
		System.out.println("Mature Population");
		//PrintChromosome(chromosomes);
		System.out.println("");
		//PrintFitnesses(chromosomefitness);
		PrintBestChromosome(chromosomes[0]);
		System.out.println("Best Fitness:"+ComputeSingleFitnesses(chromosomes[0]));
	}

	//GA operator variables
	private double[][] chromosomes;
	private int chromosomelength;
	private double[] chromosomefitness;
	private double[] chromosomeselectionprobability;
	private double[][] newchromosome;

	//GA behaviour definition
	private int numberofpopulation;
	private int numberofgeneration;
	private double crossoverprobability;
	private double mutationprobability;

	public static void main(String[] args){
		GAProject myGA = new GAProject();
		myGA.numberofpopulation = 45;
		myGA.numberofgeneration = 50;
		myGA.chromosomelength = 4;
		myGA.crossoverprobability = 0.5;
		myGA.mutationprobability = 0.01;
		myGA.RunGA();
	}
}