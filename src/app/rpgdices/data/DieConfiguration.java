package app.rpgdices.data;

public class DieConfiguration
{
	public int die;
	public int sum;
	public int count;
	public boolean count_with_sum;
	public int target_strategy;
	public int target;
	
	public DieConfiguration()
	{
		die = 6;
		sum = 0;
		count = 1;
		count_with_sum = false;
		target_strategy = TargetStrategies.NONE;
		target = 1;
	}

	public DieConfiguration(int die, int sum, int count, boolean count_with_sum, int target_strategy, int target)
	{
		this.die = die;
		this.sum = sum;
		this.count = count;
		this.count_with_sum = count_with_sum;
		this.target_strategy = target_strategy;
		this.target = target;
	}
}