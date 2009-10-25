package app.rpgdices.data;

public class DieConfiguration
{
	public int die;
	public int count;
	public int sum_or_target;
	public int target_strategy;
	
	public DieConfiguration()
	{
		die = 6;
		sum_or_target = 0;
		count = 1;
		target_strategy = TargetStrategies.NONE;
	}

	public DieConfiguration(int die, int count, int sum_or_target, int target_strategy)
	{
		this.die = die;
		this.sum_or_target = sum_or_target;
		this.count = count;
		this.target_strategy = target_strategy;
	}
}