import java.util.Random;
import javax.swing.JPanel;

class Jumper extends JPanel
{
	private double llx, lly, rlx, rly, mx, my, lenl, lenr;
	private double rh, rw, oy, ox, osx, angle;
	private double wspeed;
	private double gravity, acc;
	private double nowcost, newcost, lastcost, total_cost;
	private int cost_count;
	public boolean best = false;
	
	private int init_my = 300, init_mx = 250, init_angle = 200;
	
	public Jumper()
	{
		//Set initial last and new cost
		lastcost = newcost = nowcost = 100.0;
		
		//Calculate cost every few generations by average
		cost_count = 100;
		
		//Length of sticks
		lenl = lenr = 60;
		
		//Set initial acceleration
		acc = 0.1;
		
		//Spawn initial actors
		new_actor();
		new_block();
	}
	private void update_left_stick(double angle)
	{
		angle /= 57.2958;
		llx = mx+(double) (lenl*Math.cos(angle));
		lly = my+(double) (-lenl*Math.sin(angle));
	}
	private void update_right_stick(double angle)
	{
		angle /= 57.2958;
		rlx = mx+(double) (lenr*Math.cos(angle));
		rly = my+(double) (-lenr*Math.sin(angle));
	}
	private void new_block()
	{
		//Decrease cost
		nowcost /= 1.1;
		
		//Slow down when cost is under a certain point
		if(nowcost < 20)
			best = true;
		else
			best = false;
		
		//Length&Width of block
		rh = 100;
		rw = 20;
		
		//Height of block
		oy = 250 + new Random().nextInt(50);
		//x position of block
		ox = 600-rw;
		//Speed of block
		osx = -3;
	}
	private void new_actor()
	{
		//Calculate the cost by every 10
		if(cost_count-- > 0)
		{
			total_cost += nowcost;
			nowcost = 100.0;
		}else
		{
			cost_count = 100;
			lastcost = newcost;
			newcost = total_cost/(double)cost_count;
			total_cost = 0;
			nowcost = 100.0;
		}
		
		//Initial angles
		angle = init_angle;
	
		//Initial position
		mx = init_mx;
		my = init_my;
		
		//Initial speeds
		wspeed = 3;
	}
	public double get_cost()
	{
		return newcost;
	}
	public int get_speed()
	{
		return (int)wspeed;
	}
	public int get_angle()
	{
		return (int)angle;
	}
	public int get_mx()
	{
		return (int)mx;
	}
	public int get_my()
	{
		return (int)my;
	}
	public int get_rh()
	{
		return (int)rh;
	}
	public int get_rw()
	{
		return (int)rw;
	}
	public int get_oy()
	{
		return (int)oy;
	}
	public int get_ox()
	{
		return (int)ox;
	}
	public int get_llx()
	{
		return (int)llx;
	}
	public int get_lly()
	{
		return (int)lly;
	}
	public int get_rlx()
	{
		return (int)rlx;
	}
	public int get_rly()
	{
		return (int)rly;
	}
	
	//Return 0-Taking steps 1-Good update 2-Cancel update 3-Block hit
	public int take_step(int state)
	{	
		//Add speed to sticks
		angle += wspeed*state;
		
		//add gravity
		my += gravity;
		
		//Move block
		ox += osx;
		
		//Refresh left and right
		update_left_stick(angle+180);
		update_right_stick(angle);
		
		//Spawn new block
		if(ox < 100 || ox > 600-rw)
			new_block();
		
		//Check if block hit actor*******
		if(ox <= mx && ox >= (mx-rw) && oy <= my && oy >= (my-rh))
		{
			new_block();
			new_actor();
			//Check if cancel update or continue to update
			if(newcost <= lastcost && cost_count == 10)
				return 1;
			else if(cost_count == 10)
				return 2;
			else 
				return 3;
		}
		
		//Check if sticks hit ground********
		if(my > 400 || rly > 400 || lly > 400)
		{
			
			if(my > 400)
			{
				gravity *= -0.6;//drag when bounce back
				rly -= (my-400);
				lly -= (my-400);
				my = 400;
			}
			
			if(lly > 400)
			{
				gravity *= -0.6;
				gravity -= 0.5*Math.abs(wspeed)*(1-Math.sin((angle+180)/57.2958));
				
				my -= (lly-400);
				rly -= (lly-400);
				lly = 400;
			}
			
			if(rly > 400)
			{
				gravity *= -0.6;
				gravity -= 0.5*Math.abs(wspeed)*(1-Math.sin((angle+180)/57.2958));
				my -= (rly-400);
				lly -= (rly-400);
				rly = 400;
				
			}
		}
		else//if actor is floating
		{
			gravity += acc;
		}
		return 0;
	}
}