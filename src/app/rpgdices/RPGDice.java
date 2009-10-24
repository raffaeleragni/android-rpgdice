package app.rpgdices;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
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

	private static Random random;

	private ArrayList<DieRow> rows = new ArrayList<DieRow>();

	private TextView result;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		result = (TextView) findViewById(R.id.result);
		random = new Random(System.currentTimeMillis());

		// TODO: use profiles
		loadDiceSet(DefaultDiceSets.ww);
	}

	private void loadDiceSet(DiceSet set)
	{
		TableLayout tl = (TableLayout) findViewById(R.id.dice_table);
		tl.removeAllViews();
		rows.clear();

		for (DieConfiguration c : set.set)
		{
			rows.add(newDieRow(c.die, c.sum, c.count, c.target_strategy, c.target));
		}
	}

	@SuppressWarnings("unchecked")
	private DieRow newDieRow(int ndie, int nsum, int ncount, int target_strategy, int target)
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

		EditText sum = new EditText(this);
		sum.setText("" + nsum);
		sum.setWidth(60);
		tr_controls.addView(sum);

		EditText count = new EditText(this);
		count.setText("" + ncount);
		count.setWidth(60);
		tr_controls.addView(count);

		Spinner strategy = new Spinner(this);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.STRATEGIES, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		strategy.setAdapter(adapter);
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
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private static String roll(int die, int sum_or_target, int count, int strategy)
	{
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
					total += result + sum_or_target;
				}
				for (Integer k : results.keySet())
				{
					String sresult = k + "(+" + sum_or_target + ")";
					sresults = sresults == null ? sresult : sresults + ", " + sresult;
				}
				sresults = sresults == null ? "" : sresults;
				sresults += "\nTotal: " + total;

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