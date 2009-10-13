package app.rpgdices;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RPGDices extends Activity
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

		findViewById(R.id.d4).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(4));
			}
		});

		findViewById(R.id.d6).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(6));
			}
		});
		findViewById(R.id.d8).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(8));
			}
		});

		findViewById(R.id.d10).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(10));
			}
		});

		findViewById(R.id.d12).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(12));
			}
		});

		findViewById(R.id.d20).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(20));
			}
		});

		findViewById(R.id.d100).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(100));
			}
		});

		findViewById(R.id.clear).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				rolled.clear();
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText("");
			}
		});
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
		rolled.add(roll(_n));
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