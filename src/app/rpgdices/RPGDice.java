package app.rpgdices;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import app.rpgdices.data.DiceSet;
import app.rpgdices.data.DieConfiguration;
import app.rpgdices.data.TargetStrategies;

public class RPGDice extends Activity
{
	private static final int DIALOG_CHANGE_SET = 0;

	private static final int DIALOG_NEW_SET = 1;

	private static final int DIALOG_NEW_DIE_ONLY_ON_CUSTOM = 2;

	private static final int DIALOG_NEW_SET_ALREADY_EXISTS = 3;

	private static final int DIALOG_DELETE_SET_ONLY_ON_CUSTOM = 5;
	
	private static final int DIALOG_DELETE_SET = 4;

	private static final int DEFAULT_DICE_SET_DND = 0;

	private static final int DEFAULT_DICE_SET_WW = 1;

	private static final int DEFAULT_DICE_SET_COUNT = 2;

	private ArrayList<DieRow> rows = new ArrayList<DieRow>();

	private TreeMap<String, DiceSet> custom_dice_sets = new TreeMap<String, DiceSet>();

	private TextView result;

	private int selected_dice_set = 0;

	private String selected_dice_set_key = null;

	private boolean custom = false;

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

		String[] sets = settings.getString("sets", "").split("\\|");
		for (String set : sets)
		{
			if (set != null && set.length() > 0)
			{
				int num = settings.getInt("set_" + set + "_dicenum", 0);
				DiceSet diceset = new DiceSet();
				for (int i = 0; i < num; i++)
				{
					diceset.set.add(new DieConfiguration(settings.getInt("set_" + set + "_" + i + "_die", 6), settings.getInt(
							"set_" + set + "_" + i + "_count", 1), settings.getInt("set_" + set + "_" + i + "_sum_or_target", 0),
							settings.getInt("set_" + set + "_" + i + "_strategy", TargetStrategies.NONE)));
				}
				custom_dice_sets.put(set, diceset);
			}
		}

		String used_set = settings.getString("used_set", "dnd");

		if ("dnd".equals(used_set))
		{
			selected_dice_set = 0;
			loadDiceSet(selected_dice_set_key, DefaultDiceSets.dnd);
		}
		else if ("ww".equals(used_set))
		{
			selected_dice_set = 1;
			loadDiceSet(selected_dice_set_key, DefaultDiceSets.ww);
		}
		else
		{
			DiceSet diceset = custom_dice_sets.get(used_set);
			if (diceset != null)
			{
				int pos = 0;
				for (String k : custom_dice_sets.keySet())
				{
					if (k.equals(used_set))
					{
						break;
					}
					pos++;
				}
				selected_dice_set = pos + DEFAULT_DICE_SET_COUNT;
				selected_dice_set_key = used_set;
				loadDiceSet(selected_dice_set_key, diceset);
			}
			else
			{
				selected_dice_set = 0;
				loadDiceSet(selected_dice_set_key, DefaultDiceSets.dnd);
			}
		}
	}

	private void visualChangeDiceSet(String dicesetname, int num)
	{
		selected_dice_set = num;
		switch (num)
		{
			case DEFAULT_DICE_SET_DND:
			{
				custom = false;
				selected_dice_set_key = null;
				loadDiceSet(selected_dice_set_key, DefaultDiceSets.dnd);
				break;
			}
			case DEFAULT_DICE_SET_WW:
			{
				custom = false;
				selected_dice_set_key = null;
				loadDiceSet(selected_dice_set_key, DefaultDiceSets.ww);
				break;
			}
			default:
			{
				custom = true;
				selected_dice_set_key = dicesetname;
				loadDiceSet(selected_dice_set_key, custom_dice_sets.get(selected_dice_set_key));
				break;
			}
		}
	}

	private void createAndLoadDiceSet(String dicesetname)
	{
		DiceSet set = new DiceSet();

		custom_dice_sets.put(dicesetname, set);
		int pos = 0;
		for (String k : custom_dice_sets.keySet())
		{
			if (k.equals(dicesetname))
			{
				break;
			}
			pos++;
		}
		custom = true;
		selected_dice_set = pos;
		selected_dice_set_key = dicesetname;

		String sets = settings.getString("sets", "");
		settings_editor.putString("sets", sets + "|" + dicesetname);
		settings_editor.putInt("set_" + dicesetname + "_dicenum", 0);

		settings_editor.commit();

		visualChangeDiceSet(dicesetname, pos + 2);
	}

	private void deleteDiceSet(String dicesetname)
	{
		int num = settings.getInt("set_" + dicesetname + "_dicenum", 0);
		for (int i = 0; i < num; i++)
		{
			settings_editor.remove("set_" + dicesetname + "_" + i + "_die");
			settings_editor.remove("set_" + dicesetname + "_" + i + "_count");
			settings_editor.remove("set_" + dicesetname + "_" + i + "_sum_or_target");
			settings_editor.remove("set_" + dicesetname + "_" + i + "_strategy");
		}
		settings_editor.remove("set_" + dicesetname + "_dicenum");
		String sets = settings.getString("sets", "");
		sets = sets.replaceAll("\\|*" + dicesetname + "\\|*", "");
		settings_editor.putString("sets", sets + "|" + dicesetname);
		
		custom_dice_sets.remove(dicesetname);

		selected_dice_set = 0;
		selected_dice_set_key = null;
		loadDiceSet(selected_dice_set_key, DefaultDiceSets.dnd);
		
		settings_editor.commit();
	}

	private void createDie(String dicesetname)
	{
		int num = settings.getInt("set_" + dicesetname + "_dicenum", 0);
		settings_editor.putInt("set_" + dicesetname + "_" + num + "_die", 6);
		settings_editor.putInt("set_" + dicesetname + "_" + num + "_count", 1);
		settings_editor.putInt("set_" + dicesetname + "_" + num + "_sum_or_target", 0);
		settings_editor.putInt("set_" + dicesetname + "_" + num + "_strategy", TargetStrategies.NONE);
		num++;
		settings_editor.putInt("set_" + dicesetname + "_dicenum", num);

		custom_dice_sets.get(dicesetname).set.add(new DieConfiguration(6, 1, 0, TargetStrategies.NONE));

		rows.add(newDieRow(dicesetname, 6, 1, 0, TargetStrategies.NONE));

		settings_editor.commit();
	}

	private void updateDie(String dicesetname, int num)
	{
		DiceSet ds = custom_dice_sets.get(dicesetname);
		if (num < ds.set.size() && num < rows.size())
		{
			DieRow row = rows.get(num);
			DieConfiguration dc = ds.set.get(num);

			int die = 6, count = 1, sum_or_target = 0;
			try
			{
				die = Integer.parseInt(row.die.getText().toString());
			}
			catch (NumberFormatException e)
			{
			}
			try
			{
				count = Integer.parseInt(row.count.getText().toString());
			}
			catch (NumberFormatException e)
			{
			}
			try
			{
				sum_or_target = Integer.parseInt(row.sum.getText().toString());
			}
			catch (NumberFormatException e)
			{
			}
			int strategy = row.strategy.getSelectedItemPosition();

			dc.die = die;
			dc.count = count;
			dc.sum_or_target = sum_or_target;
			dc.target_strategy = strategy;

			settings_editor.putInt("set_" + dicesetname + "_" + num + "_die", die);
			settings_editor.putInt("set_" + dicesetname + "_" + num + "_count", count);
			settings_editor.putInt("set_" + dicesetname + "_" + num + "_sum_or_target", sum_or_target);
			settings_editor.putInt("set_" + dicesetname + "_" + num + "_strategy", strategy);
			settings_editor.commit();
		}
	}

	private void loadDiceSet(String dicesetname, DiceSet set)
	{
		TableLayout tl = (TableLayout) findViewById(R.id.dice_table);
		tl.removeAllViews();
		rows.clear();

		for (DieConfiguration c : set.set)
		{
			rows.add(newDieRow(dicesetname, c.die, c.count, c.sum_or_target, c.target_strategy));
		}
	}

	@SuppressWarnings("unchecked")
	private DieRow newDieRow(String dicesetname, int ndie, int ncount, int nsum_or_target, int target_strategy)
	{
		DieRow row = new DieRow();

		TableLayout tl = (TableLayout) findViewById(R.id.dice_table);

		TableRow tr_labels = new TableRow(this);
		TableRow tr_controls = new TableRow(this);

		TextView label_0 = new TextView(this);
		label_0.setText("  ");
		tr_labels.addView(label_0);

		TextView label_1 = new TextView(this);
		label_1.setText("  " + getResources().getString(R.string.LABEL_1));
		tr_labels.addView(label_1);

		TextView label_2 = new TextView(this);
		label_2.setText("  " + getResources().getString(R.string.LABEL_2));
		tr_labels.addView(label_2);

		TextView label_3 = new TextView(this);
		if (target_strategy == TargetStrategies.TARGET_AT_LEAST)
		{
			label_3.setText("  " + getResources().getString(R.string.LABEL_3_T));
		}
		else
		{
			label_3.setText("  " + getResources().getString(R.string.LABEL_3));
		}
		tr_labels.addView(label_3);

		TextView label_4 = new TextView(this);
		label_4.setText("  " + getResources().getString(R.string.LABEL_4));
		tr_labels.addView(label_4);

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

		die.setOnKeyListener(row.onedit_changed);
		count.setOnKeyListener(row.onedit_changed);
		sum.setOnKeyListener(row.onedit_changed);
		roll_button.setOnClickListener(row.roll_clicked);
		strategy.setOnItemSelectedListener(row.strategy_changed);
		row.dicesetname = dicesetname;
		row.die = die;
		row.sum = sum;
		row.count = count;
		row.strategy = strategy;
		row.sumlabel = label_3;

		tl.addView(tr_labels);
		tl.addView(tr_controls);

		return row;
	}

	private class DieRow
	{
		public String dicesetname;
		public EditText die;
		public EditText sum;
		public EditText count;
		public Spinner strategy;
		public TextView sumlabel;

		public OnKeyListener onedit_changed = new OnKeyListener()
		{
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				//				if (!(keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9))
				//				{
				//					// invalidate the key, how?
				//					return true;
				//				}

				if (custom)
				{
					int idx = rows.indexOf(this);
					if (idx != -1)
					{
						updateDie(dicesetname, idx);
					}
				}

				return false;
			}
		};

		public OnItemSelectedListener strategy_changed = new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				if (position == TargetStrategies.TARGET_AT_LEAST)
				{
					sumlabel.setText("  " + getResources().getString(R.string.LABEL_3_T));
				}
				else
				{
					sumlabel.setText("  " + getResources().getString(R.string.LABEL_3));
				}

				if (custom)
				{
					int idx = rows.indexOf(this);
					if (idx != -1)
					{
						updateDie(dicesetname, idx);
					}
				}
			}

			public void onNothingSelected(AdapterView<?> parent)
			{
			}
		};

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
				showDialog(DIALOG_CHANGE_SET);
				break;
			}
			case R.menu_id.new_set:
			{
				showDialog(DIALOG_NEW_SET);
				break;
			}
			case R.menu_id.delete_set:
			{
				if (!custom)
				{
					showDialog(DIALOG_DELETE_SET_ONLY_ON_CUSTOM);
					break;
				}
				
				showDialog(DIALOG_DELETE_SET);
				break;
			}
			case R.menu_id.new_die:
			{
				if (!custom)
				{
					showDialog(DIALOG_NEW_DIE_ONLY_ON_CUSTOM);
					break;
				}

				createDie(selected_dice_set_key);

				break;
			}
		}

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case DIALOG_NEW_DIE_ONLY_ON_CUSTOM:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getResources().getText(R.string.NEW_DIE_ONLY_ON_CUSTOM));
				return builder.create();
			}
			
			case DIALOG_DELETE_SET_ONLY_ON_CUSTOM:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getResources().getText(R.string.DELETE_SET_ONLY_ON_CUSTOM));
				return builder.create();
			}

			case DIALOG_NEW_SET_ALREADY_EXISTS:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getResources().getText(R.string.NEW_SET_ALREADY_EXISTS));
				return builder.create();
			}

			case DIALOG_NEW_SET:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				builder.setTitle(R.string.DIALOG_NEW_SET_TITLE);
				builder.setMessage(R.string.DIALOG_NEW_SET_MESSAGE);

				final EditText input = new EditText(this);
				builder.setView(input);
				builder.setPositiveButton(R.string.DIALOG_NEW_SET_OK, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						if (!custom_dice_sets.containsKey(input.getText().toString()))
						{
							createAndLoadDiceSet(input.getText().toString());
						}
						else
						{
							showDialog(DIALOG_NEW_SET_ALREADY_EXISTS);
						}
						dialog.cancel();
					}
				});
				builder.setNegativeButton(R.string.DIALOG_NEW_SET_CANCEL, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int whichButton)
					{
						dialog.cancel();
					}
				});

				return builder.create();
			}

			case DIALOG_CHANGE_SET:
			{
				ArrayList<CharSequence> sets = new ArrayList<CharSequence>();
				sets.add(getResources().getString(R.string.SET_DND));
				sets.add(getResources().getString(R.string.SET_WW));
				for (String k : custom_dice_sets.keySet())
				{
					sets.add(k);
				}
				final CharSequence[] items = sets.toArray(new CharSequence[] {});

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose a dice set");
				builder.setSingleChoiceItems(items, selected_dice_set, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						dialog.cancel();
						visualChangeDiceSet(items[item].toString(), item);
					}
				});
				return builder.create();
			}

			case DIALOG_DELETE_SET:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(getResources().getText(R.string.DIALOG_DELETE_SET_TITLE));
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.DIALOG_DELETE_SET_YES, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						deleteDiceSet(selected_dice_set_key);
					}
				});
				builder.setNegativeButton(R.string.DIALOG_DELETE_SET_NO, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				});
				return builder.create();
			}
		}

		return super.onCreateDialog(id);
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

	public void toast(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}