import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;

class Main extends JPanel
{	
	static int sleep_time;
	static int width = 700, height = 500;
	Jumper j = new Jumper();
	Neural_Network nn;
	int generation;
	int input_size;
	
	Main()
	{
		input_size = 5;
		nn = new Neural_Network(input_size, 3);
		sleep_time = 0;
	}
	
	public static void main(String args[]) throws InterruptedException
	{
		//Set frame preferences
		Main m = new Main();
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(width, height));
		frame.add(m);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Infinite loop for animation and calculations
		while(true)
		{
			Thread.sleep(sleep_time);
			m.step();
			frame.repaint();
		}
	}
	public int update_neural()
	{
		//Update neural network
		double input[] = new double[input_size];
		input[0] = Math.abs((Math.cos(j.get_angle()/57.33/2)));
		input[1] = Calculate.sigmoid(j.get_speed());
		input[2] = (j.get_my()-100.0)/(400.0);
		input[3] = (250.0-(Math.abs(j.get_ox()-j.get_mx())))/250.0;
		input[4] = ((j.get_oy()-100.0)/400.0);
		return nn.update_program(input)-1;
	}
	public void step()
	{
		int action = j.take_step(update_neural());
		if(action == 0)
		{
			return;
		}
		else if(action == 1)
		{
			generation++;
			if(nn.update_1)
				nn.update_weights(nn.w1);
			else
				nn.update_weights(nn.w2);
		}
		else if(action == 2)
		{
			generation++;
			nn.cancel_update();;
		}
		else if(action == 3)
		{
			generation++;
		}
		//Fast forward until a certain point
		if(generation%20000 < 5 || j.best == true)
		{
			sleep_time = 7;
		}else
		{
			sleep_time = 0;
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//Background paint
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, width, height);
		
		//Paint enemy block
		g.setColor(Color.ORANGE);
		g.fillRect(j.get_ox(), j.get_oy(), j.get_rw(), j.get_rh());
		
		//Fill stage
		g.setColor(Color.WHITE);
		g.drawRect(100, 100, 500, 300);//stage
		
		//Draw jumper
		g.setColor(Color.WHITE);
		g.drawLine(j.get_mx(), j.get_my(), j.get_rlx(), j.get_rly());
		g.drawLine(j.get_mx(), j.get_my(), j.get_llx(), j.get_lly());
		g.setColor(Color.RED);
		g.fillOval(j.get_mx()-4, j.get_my()-4, 8, 8);
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.WHITE);
		g2.drawString("Generation: " + generation, 10, 15);
		g2.drawString("Speed: " + ((sleep_time == 0)? "Infinity":10/sleep_time), 10, 35);
	}
}