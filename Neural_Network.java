import java.util.Random;

import com.sun.org.apache.bcel.internal.generic.NEWARRAY;

public class Neural_Network 
{
	//Initial variables
	public double w1[][];
	public double w2[][];
	private double input[];
	private double hidden_layer[];
	private double output[];
	private int division_factor;
	
	//Which one to update
	public boolean update_1 = true;
	//Weight index counter
	private int w1i, w1j, w2i, w2j;
	//Random factors
	public double sw1, sw2;
	
	//Constructor
	public Neural_Network(int input_size, int output_size) 
	{
		//How small will the change be 1/division_factor
		division_factor = 1;
		
		//Hidden layer size is the average of input+output
		int hidden_layer_size = (input_size+output_size)/2;
		
		//Initialize layers
		input = new double[input_size];
		hidden_layer = new double[hidden_layer_size];
		output = new double[output_size];
		
		w1 = new double[input.length][hidden_layer.length];
		w2 = new double[hidden_layer.length][output.length];

		start_weights(w1);
		start_weights(w2);
	}
	
	//Calculate weights and update
	public void update_weights(double w[][])
	{
		w1j %= w1[0].length;
		w2j %= w2[0].length;
		
		if(update_1)
		{
			w1[w1i][w1j] += sw1 = (new Random().nextBoolean()? -1:1)/division_factor;
			w1j++;
			if(w1j == w1[0].length-1)
			{
				w1j = 0;
				w1i++;
			}
			if(w1i >= w1.length)
			{
				w1i = w1j = 0;
				update_1 = false;
			}
		}else
		{	
			w2[w2i][w2j] += sw2 = (new Random().nextBoolean()? -1:1)/division_factor;
			w2j++;
			if(w2j == w2[0].length-1)
			{
				w2j = 0;
				w2i++;
			}
			if(w2i >= w2.length)
			{
				w2i = w2j = 0;
				update_1 = true;
			}
		}
		
	}
	
	//Cancel last weights update
	public void cancel_update()
	{
		if(update_1)
		{
			w1[w1i][w1j] -= sw1;
			update_weights(w1);
		}else
		{
			w2[w2i][w2j] -= sw2;
			update_weights(w2);
		}
	}
	
	//Update program according to new weights - input must be normalized
	public int update_program(double in[])
	{
		//Validate the input is legitimate
		if(in.length != (input.length))
			System.exit(-1);
		
		//Zero out hidden layer and output
		for(int i=0; i<hidden_layer.length; i++)
			hidden_layer[i] = 0;
		for(int i=0; i<output.length; i++)
			output[i] = 0;
		
		//Sum of input layer * weights -> hidden layer & Normalize it
		for(int i=0; i<hidden_layer.length; i++)
		{
			for(int j=0; j<in.length; j++)
			{
				hidden_layer[i] += in[j]*w1[j][i];
			}
			hidden_layer[i] = Calculate.sigmoid(hidden_layer[i]);
		}
		
		//Sum of hidden layer * weights -> output layer & Normalize it
		for(int i=0; i<output.length; i++)
		{
			for(int j=0; j<hidden_layer.length; j++)
			{
				output[i] += hidden_layer[j]*w2[j][i];
			}
			output[i] = Calculate.sigmoid(output[i]);
		}
		return Calculate.find_max(output, output.length);
	}
	
	//Activation function
	//Find maximum of the two numbers, return biggest one
	private static double max(double a, double b)
	{
		if(a > b)
			return a;
		else
			return b;
	}
	
	//Set random integers to weights from -1 to 1
	public void start_weights(double w[][])
	{
		for(int i=0; i<w.length; i++)
		{
			for(int j=0; j<w[0].length; j++)
			{
				w[i][j] = new Random().nextDouble()*2-1;
			}
		}
	}

}
