package app.rpgdices.data;

import java.util.ArrayList;

public class DiceSet
{
	public ArrayList<DieConfiguration> set;
	
	public DiceSet(DieConfiguration... params)
	{
		set = new ArrayList<DieConfiguration>();
		for (DieConfiguration c : params)
		{
			set.add(c);
		}
	}
}
