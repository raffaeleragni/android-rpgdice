package app.rpgdices;

import app.rpgdices.data.DiceSet;
import app.rpgdices.data.DieConfiguration;
import app.rpgdices.data.TargetStrategies;

public class DefaultDiceSets
{
	public static final DiceSet dnd = new DiceSet
	(
		new DieConfiguration(20, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(4, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(6, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(8, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(10, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(12, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(100, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(2, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(3, 1, 0, TargetStrategies.NONE),
		new DieConfiguration(30, 1, 0, TargetStrategies.NONE)
	);
	
	public static final DiceSet ww = new DiceSet
	(
		new DieConfiguration(10, 6, 1, TargetStrategies.TARGET_AT_LEAST),
		new DieConfiguration(10, 6, 2, TargetStrategies.TARGET_AT_LEAST),
		new DieConfiguration(10, 6, 4, TargetStrategies.TARGET_AT_LEAST),
		new DieConfiguration(10, 6, 6, TargetStrategies.TARGET_AT_LEAST),
		new DieConfiguration(10, 6, 8, TargetStrategies.TARGET_AT_LEAST),
		new DieConfiguration(10, 6, 4, TargetStrategies.TARGET_AT_LEAST),
		new DieConfiguration(10, 6, 10, TargetStrategies.TARGET_AT_LEAST)
	);
}
