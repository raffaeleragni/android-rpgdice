package app.rpgdices;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class RPGDices extends Activity
{
	private ArrayList<Integer> rolled;
	
	private Random random;
	
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
				t.setText(roll(4));
			}
		});

		findViewById(R.id.d6).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll(6));
			}
		});
		findViewById(R.id.d8).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll(8));
			}
		});

		findViewById(R.id.d10).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll(10));
			}
		});

		findViewById(R.id.d12).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll(12));
			}
		});

		findViewById(R.id.d20).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll(20));
			}
		});

		findViewById(R.id.d100).setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				TextView t = (TextView) findViewById(R.id.text_output);
				t.setText(roll(100));
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

	public String roll(int _n)
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
}