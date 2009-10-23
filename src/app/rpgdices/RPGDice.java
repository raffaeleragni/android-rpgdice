package app.rpgdices;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RPGDice extends Activity
{
	private ArrayList<Integer> rolled;

	private static Random random;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		random = new Random(System.currentTimeMillis());
		rolled = new ArrayList<Integer>();

		
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

	public String roll_and_display(int _n)
	{
		String result = null;
		int sum = 0;
		rolled.add((random.nextInt(_n) + 1));
		for (Integer n : rolled)
		{
			if (n != null)
			{
				result = result == null ? "" + n : result + " + " + n;
				sum += n;
			}
		}
		result = result == null ? "" + sum : result + " = (" + sum + ")";
		
		return result;
	}

	public static int roll(int n)
	{
		return (random.nextInt(n) + 1);
	}
}