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

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DNDStatsGeneratorActivity extends Activity
{
	private static Random random;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_generator);
		
		((Button) findViewById(R.stats_id.roll)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				random = new Random(System.currentTimeMillis());
				roll_stats((TextView) findViewById(R.stats_id.result1));
				roll_stats((TextView) findViewById(R.stats_id.result2));
			}
		});
	}

	private void roll_stats(TextView view)
	{
		int rolled = 0, bonus = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 6; i++)
		{
			rolled = roll_stat();
			if (rolled >= 10)
			{
				bonus = (rolled - 10) / 2;
			}
			else
			{
				bonus = (rolled - 11) / 2;	
			}
			sb.append("\n" + rolled + "  [ " + (bonus > 0 ? "+" : "") + bonus + " ]");
		}
		view.setText(sb.toString());
	}

	private int roll_stat()
	{
		int rolled = 0, sum = 0, lowest = 99;
		for (int i = 0; i < 4; i++)
		{
			rolled = roll(6);
			lowest = rolled < lowest ? rolled : lowest;
			sum += rolled;
		}

		return sum - lowest;
	}
	
	private static int roll(int die)
	{
		// TODO
		return (random.nextInt(die) + 1);
	}
}
