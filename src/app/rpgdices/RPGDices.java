/*
 *   wardrive - android wardriving application
 *   Copyright (C) 2009 Raffaele Ragni
 *   http://code.google.com/p/wardrive-android/
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *   
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

		findViewById(R.id.d30).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll_and_display(30));
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