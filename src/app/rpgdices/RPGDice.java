package app.rpgdices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import app.rpgdices.data.DiceSet;
import app.rpgdices.data.DieConfiguration;
import app.rpgdices.data.TargetStrategies;

public class RPGDice extends Activity
{
	private static final int[] LABEL_CODES = new int[] { R.string.LABEL_0, R.string.LABEL_1, R.string.LABEL_2, R.string.LABEL_3,
			R.string.LABEL_4 };

	private ArrayList<DieRow> rows = new ArrayList<DieRow>();

	private HashMap<String, DiceSet> custom_dice_sets = new HashMap<String, DiceSet>();

	private TextView result;

	private SharedPreferences settings;

	private SharedPreferences.Editor settings_editor;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		result = (TextView) findViewById(R.id.result);

		settings = getPreferences(MODE_PRIVATE);
		settings_editor = settings.edit();

		String[] sets = settings.getString("sets", "").split("|");
		for (String set : sets)
		{
			if (set != null && set.length() > 0)
			{
				int num = settings.getInt("set_" + set + "_dicenum", 0);
				DiceSet diceset = new DiceSet();
				for (int i = 0; i < num; i++)
				{
					diceset.set.add(new DieConfiguration(settings.getInt("set_" + set + "_" + num + "_die", 6), settings.getInt(
							"set_" + set + "_" + num + "_count", 1), settings.getInt("set_" + set + "_" + num + "_sum_or_target",
							0), settings.getInt("set_" + set + "_" + num + "_strategy", TargetStrategies.NONE)));
				}
				custom_dice_sets.put(set, diceset);
			}
		}

		String used_set = settings.getString("used_set", "dnd");

		if ("dnd".equals(used_set))
		{
			loadDiceSet(DefaultDiceSets.dnd);
		}
		else if ("ww".equals(used_set))
		{
			loadDiceSet(DefaultDiceSets.ww);
		}
		else
		{
			DiceSet diceset = custom_dice_sets.get(used_set);
			if (diceset != null)
			{
				loadDiceSet(diceset);
			}
			else
			{
				loadDiceSet(DefaultDiceSets.dnd);
			}
		}
	}

	private void loadDiceSet(DiceSet set)
	{
		TableLayout tl = (TableLayout) findViewById(R.id.dice_table);
		tl.removeAllViews();
		rows.clear();

		for (DieConfiguration c : set.set)
		{
			rows.add(newDieRow(c.die, c.count, c.sum_or_target, c.target_strategy));
		}
	}

	@SuppressWarnings("unchecked")
	private DieRow newDieRow(int ndie, int ncount, int nsum_or_target, int target_strategy)
	{
		DieRow row = new DieRow();

		TableLayout tl = (TableLayout) findViewById(R.id.dice_table);

		TableRow tr_labels = new TableRow(this);
		TableRow tr_controls = new TableRow(this);

		for (int code : LABEL_CODES)
		{
			TextView label = new TextView(this);
			label.setText("  " + getResources().getString(code));
			tr_labels.addView(label);
		}

		Button roll_button = new Button(this);
		roll_button.setText(getResources().getString(R.string.ROLL));
		roll_button.setWidth(50);
		roll_button.setHeight(50);
		tr_controls.addView(roll_button);

		EditText die = new EditText(this);
		die.setText("" + ndie);
		die.setWidth(60);
		tr_controls.addView(die);

		EditText count = new EditText(this);
		count.setText("" + ncount);
		count.setWidth(60);
		tr_controls.addView(count);

		EditText sum = new EditText(this);
		sum.setText("" + nsum_or_target);
		sum.setWidth(60);
		tr_controls.addView(sum);

		Spinner strategy = new Spinner(this);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.STRATEGIES, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		strategy.setAdapter(adapter);
		strategy.setSelection(target_strategy);
		tr_controls.addView(strategy);

		roll_button.setOnClickListener(row.roll_clicked);
		row.die = die;
		row.sum = sum;
		row.count = count;
		row.strategy = strategy;

		tl.addView(tr_labels);
		tl.addView(tr_controls);

		return row;
	}

	private class DieRow
	{
		public EditText die;
		public EditText sum;
		public EditText count;
		public Spinner strategy;

		public OnClickListener roll_clicked = new OnClickListener()
		{
			public void onClick(View v)
			{
				int ndie = 0, nsum = 0, ncount = 0;
				try
				{
					ndie = Integer.parseInt(die.getText().toString());
				}
				catch (NumberFormatException e)
				{
				}
				try
				{
					nsum = Integer.parseInt(sum.getText().toString());
				}
				catch (NumberFormatException e)
				{
				}
				try
				{
					ncount = Integer.parseInt(count.getText().toString());
				}
				catch (NumberFormatException e)
				{
				}

				result.setText(roll(ndie, nsum, ncount, strategy.getSelectedItemPosition()));
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.menu_id.dnd_character_stats:
			{
				Intent i = new Intent();
				i.setClass(this, DNDStatsGeneratorActivity.class);
				startActivity(i);
				break;
			}
			case R.menu_id.change_set:
			{

				break;
			}
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private static String roll(int die, int sum_or_target, int count, int strategy)
	{
		Random random = new Random(System.currentTimeMillis());

		switch (strategy)
		{
			case TargetStrategies.NONE:
			{
				int total = 0;
				TreeMap<Integer, Integer> results = new TreeMap<Integer, Integer>();
				String sresults = null;
				for (int i = 0; i < count; i++)
				{
					int result = random.nextInt(die) + 1;
					if (!results.containsKey(result))
					{
						results.put(result, 1);
					}
					else
					{
						results.put(result, results.get(result) + 1);
					}
					total += result;
				}
				for (Integer k : results.keySet())
				{
					String sresult = results.get(k) + "x" + k;
					sresults = sresults == null ? sresult : sresults + ", " + sresult;
				}
				sresults = sresults == null ? "" : sresults;
				sresults += "\nTotal: " + total + "+" + sum_or_target + " = " + (total + sum_or_target);

				return sresults;
			}
			case TargetStrategies.TARGET_AT_LEAST:
			{
				TreeMap<Integer, Integer> results = new TreeMap<Integer, Integer>();
				int successes = 0;
				String sresults = null;
				for (int i = 0; i < count; i++)
				{
					int result = random.nextInt(die) + 1;
					if (!results.containsKey(result))
					{
						results.put(result, 1);
					}
					else
					{
						results.put(result, results.get(result) + 1);
					}
					if (result >= sum_or_target)
					{
						successes++;
					}

				}
				for (Integer k : results.keySet())
				{
					String sresult = k + "(" + results.get(k) + ")";
					sresults = sresults == null ? sresult : sresults + ", " + sresult;
				}
				sresults = sresults == null ? "" : sresults;
				sresults += "\nSuccesses: " + successes;

				return sresults;
			}
		}

		return "";
	}
}