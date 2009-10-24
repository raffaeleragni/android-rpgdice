package app.rpgdices;

import app.rpgdices.data.DiceSet;
import app.rpgdices.data.DieConfiguration;
import app.rpgdices.data.TargetStrategies;

public class DefaultDiceSets
{
	public static final DiceSet dnd = new DiceSet
	(
		new DieConfiguration(20, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(4, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(6, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(8, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(10, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(12, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(100, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(2, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(3, 0, 1, true, TargetStrategies.NONE, 1),
		new DieConfiguration(30, 0, 1, true, TargetStrategies.NONE, 1)
	);
	
	public static final DiceSet ww = new DiceSet
	(
		new DieConfiguration(10, 0, 1, true, TargetStrategies.TARGET_AT_LEAST, 6),
		new DieConfiguration(10, 0, 2, true, TargetStrategies.TARGET_AT_LEAST, 6),
		new DieConfiguration(10, 0, 4, true, TargetStrategies.TARGET_AT_LEAST, 6),
		new DieConfiguration(10, 0, 6, true, TargetStrategies.TARGET_AT_LEAST, 6),
		new DieConfiguration(10, 0, 8, true, TargetStrategies.TARGET_AT_LEAST, 6),
		new DieConfiguration(10, 0, 4, true, TargetStrategies.TARGET_AT_LEAST, 6),
		new DieConfiguration(10, 0, 10, true, TargetStrategies.TARGET_AT_LEAST, 6)
	);
}
