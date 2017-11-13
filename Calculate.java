public final class Calculate
{
	private Calculate() {}
	
	//Find the maximum of the array given the length - return index to biggest
	public static int find_max(double n[], int length)
	{
		int max=0;
		for(int i=0; i<length; i++)
		{
			if(n[max] < n[i])
			{
				max = i;
				i=0;
			}
		}
		return max;
	}
	
	//Get an array of doubles and return their average
	double average(double n[])
	{
		double sum=0;
		for(int i=0; i<n.length; i++)
		{
			sum += n[i];
		}
		return sum/n.length;
	}
	
	//Sigmoid normalizer - return normalized value
	public static double sigmoid(double n)
	{
		return 1/(1+Math.pow(Math.E,-n));
	}
	
	//Distance between two points
	public static double dis(double ax, double bx, double ay, double by)
	{
		return Math.sqrt((ax+bx)*(ax+bx)+(ay+by)*(ay+by));
	}
}