package app.rpgdices;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DNDStatsGeneratorActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats_generator);

		((Button) findViewById(R.stats_id.roll)).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				roll_stats((TextView) findViewById(R.stats_id.result1));
				roll_stats((TextView) findViewById(R.stats_id.result2));
			}
		});
	}

	private void roll_stats(TextView view)
	{
		int rolled = 0, bonus = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 6; i++)
		{
			rolled = roll_stat();
			bonus = (rolled - 10) / 2;
			sb.append("\n" + rolled + "  [ " + (bonus > 0 ? "+" : "") + bonus + " ]");
		}
		view.setText(sb.toString());
	}

	private int roll_stat()
	{
		int rolled = 0, sum = 0, lowest = 99;
		for (int i = 0; i < 4; i++)
		{
			rolled = RPGDices.roll(6);
			lowest = rolled < lowest ? rolled : lowest;
			sum += rolled;
		}

		return sum - lowest;
	}
}
