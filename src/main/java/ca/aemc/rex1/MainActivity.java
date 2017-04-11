package ca.aemc.rex1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.media.ToneGenerator;

public class MainActivity extends AppCompatActivity implements SensorEventListener
{
    RexModel model;
    ScoreModel score;
    ToneGenerator tone;
    double ax,ay,az;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new RexModel();
        score = new ScoreModel();
        tone = new ToneGenerator(AudioManager.STREAM_RING, 200);

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        newRegex();
        checkedListener();

        ((EditText) findViewById(R.id.string)).getText().clear();
    }

    public void onSensorChanged(SensorEvent event)
    {
        ax = event.values[0];
        ay = event.values[1];
        az = event.values[2];

        double root = Math.pow(ax, 2) + Math.pow(ay, 2) + Math.pow(az, 2);
        double length = Math.sqrt(root);

        if (length > 14) ((EditText) findViewById(R.id.string)).setText("");
    }

    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
    }

    public void checkedListener()
    {
        final CheckBox dig = (CheckBox) findViewById(R.id.digit);
        final CheckBox letter = (CheckBox) findViewById(R.id.letter);
        final CheckBox anc = (CheckBox) findViewById(R.id.anchor);

        letter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    model.setLetter(true);
                    newRegex();
                }
                else
                {
                    model.setLetter(false);
                    newRegex();
                }
            }
        });

        dig.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    model.setDigit(true);
                    newRegex();
                }
                else
                {
                    model.setDigit(false);
                    newRegex();
                }
            }
        });

        anc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (((CheckBox) view).isChecked())
                {
                    model.setAnchor(true);
                    newRegex();
                }
                else
                {
                    model.setAnchor(false);
                    newRegex();
                }
            }
        });
    }

    private void newRegex()
    {
        model.generate(2);
        String reg = model.getRex();
        TextView generate = (TextView) findViewById(R.id.regex);
        generate.setText(reg);
    }

    public String convertTime()
    {
        long newScore = score.getElapsedTime();
        if (newScore >= 60)
        {
            long newScore2 = newScore / 60;
            long newScore3 = newScore2 % 60;
            return newScore2 + "mins " + newScore3 + "secs";
        }
        else
        {
            return newScore + "secs";
        }
    }

    public void buttonClicked2(View v)
    {
        View strView = findViewById(R.id.string);
        EditText strEdit = (EditText) strView;
        String input = strEdit.getText().toString();

        TextView log = ((TextView) findViewById(R.id.log));
        String newLine = "regex = " + model.getRex() + ", string = " + input + "---> " + model.doesMatch(input) + "\n";
        String newText = newLine + log.getText().toString();
        log.setText(newText);

        if (model.doesMatch(input) == false) {
            tone.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 200);
            score.record(false);
        }
        else if (model.doesMatch(input))
        {
            newRegex();
            score.record(true);
        }

        String result = "Score= " + score.getSuccess() + " " + ((int) score.getAverageScore()) + "% (" + score.getAttempts() + " attempts in " + convertTime() + ")";
        ((TextView) findViewById(R.id.result)).setText(result);
    }
}
