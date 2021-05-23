package com.example.localisermonenfant_enfant.activity.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.localisermonenfant_enfant.R;

import java.sql.Time;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {


    private Button add_area;
    private TextView modeAddArea;
    private RadioGroup radioGroup;
    private TimePicker timeStart;
    private TimePicker timeEnd;
    private LinearLayout linearLayout;
    private Fragment fragment ;

    public static ArrayList<areaMap> areaList = new ArrayList<>();
    public static boolean addZone = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        add_area = findViewById(R.id.addAreaButton);
        modeAddArea = findViewById(R.id.addAreatv);
        radioGroup = findViewById(R.id.radioGroup);
        linearLayout = findViewById(R.id.linearLayout2);
        timeStart = (TimePicker) this.findViewById(R.id.hoursStart);
        timeEnd = (TimePicker) this.findViewById(R.id.hoursEnd);
        timeStart.setIs24HourView(true); // 24H Mode.
        timeEnd.setIs24HourView(true); // 24H Mode.


        add_area.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!addZone) {
                    addZone = true;

                    modeAddArea.setVisibility(View.VISIBLE);
                    add_area.setText(R.string.Confirmer);
                    radioGroup.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.VISIBLE);
                    areaMap areaMap = new areaMap();
                    areaList.add(areaMap);

                } else {
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) findViewById(selectedId);
                    addZone = false;
                    modeAddArea.setVisibility(View.GONE);
                    add_area.setText(R.string.Add_area);
                    linearLayout.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    int color = 0x7F00FF0;
                    if (radioButton.getText().toString().equals(getString(R.string.Green_Area))) {
                         color = 0x7F00FF00;
                    } else if (radioButton.getText().toString().equals(getString(R.string.Orange_area))) {
                         color = 0x7FFF7F00;
                    } else if (radioButton.getText().toString().equals(getString(R.string.Red_area))) {
                        color = 0x7FFF2B00;
                    }

                    int hoursStart = timeStart.getHour();
                    int minuteStart = timeStart.getMinute();

                    int hoursEnd = timeEnd.getHour();
                    int minutesEnd = timeEnd.getMinute();
                    areaList.get(areaList.size()-1).setColor(color);
                    areaList.get(areaList.size()-1).setHoursStart(hoursStart);
                    areaList.get(areaList.size()-1).setMinuteStart(minuteStart);
                    areaList.get(areaList.size()-1).setHoursEnd(hoursEnd);
                    areaList.get(areaList.size()-1).setMinutesEnd(minutesEnd);

                }


            }
        });
    }
}
