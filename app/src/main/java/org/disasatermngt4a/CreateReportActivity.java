package org.disasatermngt4a;

import org.disasatermngt4a.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class CreateReportActivity extends Activity {
    private EditText mfirstname;
    private EditText mlastname;
    private Spinner mDisasterSpinner;
    private Spinner mReportSpinner;
    private LinearLayout mResourceOrDamageLayout;
    private TextView mResourceOrDamageLabel;
    private Spinner mResourceOrDamageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        mDisasterSpinner = (Spinner) findViewById(R.id.disaster_type_spinner);
        setSpinnerResource(mDisasterSpinner,R.array.disaster_type_spinner);

        mReportSpinner = (Spinner) findViewById(R.id.report_type_spinner);
        setReportSpinnerResource(mReportSpinner,R.array.report_type_spinner);

        mResourceOrDamageLayout = (LinearLayout) findViewById(R.id.resource_or_damage_type_layout);
        mResourceOrDamageLabel = (TextView) findViewById(R.id.resource_or_damage_type_text_view);
        mResourceOrDamageSpinner = (Spinner) findViewById(R.id.resource_or_damage_type_spinner);

        mfirstname = (EditText) findViewById(R.id.first_name);
        mlastname = (EditText) findViewById(R.id.last_name);


    }

    private void setSpinnerResource(Spinner spinner, int srcId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, srcId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setReportSpinnerResource(Spinner spinner, int srdId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, srdId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (parent.getItemAtPosition(i).toString().equals("request") || parent.getItemAtPosition(i).toString().equals("donation")) {
                    mResourceOrDamageLayout.setVisibility(view.VISIBLE);
                    mResourceOrDamageLabel.setText("Resource Type:");
                    setSpinnerResource(mResourceOrDamageSpinner, R.array.resource_type_spinner);
                } else if (parent.getItemAtPosition(i).toString().equals("damage")) {
                    mResourceOrDamageLayout.setVisibility(view.VISIBLE);
                    mResourceOrDamageLabel.setText("Damage Type:");
                    setSpinnerResource(mResourceOrDamageSpinner, R.array.damage_type_spinner);
                } else {
                    mResourceOrDamageLayout.setVisibility(view.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void createReport(View view) {
        String t1 = mfirstname.getText().toString();
        String t2 = mlastname.getText().toString();
        int p1 = mDisasterSpinner.getSelectedItemPosition();
        String p2 = mReportSpinner.getSelectedItem().toString();

        Intent returnIntent = new Intent();
        HashMap<String, String> data = new HashMap<String, String >();
        data.put("tab_id", "0");
        if (t1.length() > 0) {
            data.put("fN", t1);
        }
        if (t2.length() > 0) {
            data.put("lN", t2);
        }
        if (p1 != 0) {
            String text = mDisasterSpinner.getSelectedItem().toString();
            data.put("disaster_type", text);
        }
        if (p2.equals("request") || p2.equals("donation") || p2.equals("damage")) {
            String text = mReportSpinner.getSelectedItem().toString();
            data.put("report_type", text);

            if (p2.equals("request")|| p2.equals("donation")) {
                String p3 = mResourceOrDamageSpinner.getSelectedItem().toString();
                data.put("resource_type", p3);
            }
            if (p2.equals("damage")) {
                String p3 = mResourceOrDamageSpinner.getSelectedItem().toString();
                data.put("damage_type", p3);
            }
        }

        returnIntent.putExtra("data", data);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
