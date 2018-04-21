package org.disasatermngt4a;

import org.disasatermngt4a.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

public class ReportFilterActivity extends Activity {
    private Spinner mDisasterSpinner;
    private Spinner mReportSpinner;
    private LinearLayout mResourceOrDamageLayout;
    private TextView mResourceOrDamageLabel;
    private Spinner mResourceOrDamageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_filter);

        mDisasterSpinner = (Spinner) findViewById(R.id.disaster_type_spinner);
        setSpinnerResource(mDisasterSpinner,R.array.disaster_type_spinner);

        mReportSpinner = (Spinner) findViewById(R.id.report_type_spinner);
        setReportSpinnerResource(mReportSpinner,R.array.report_type_spinner);

        mResourceOrDamageLayout = (LinearLayout) findViewById(R.id.resource_or_damage_type_layout);
        mResourceOrDamageLabel = (TextView) findViewById(R.id.resource_or_damage_type_text_view);
        mResourceOrDamageSpinner = (Spinner) findViewById(R.id.resource_or_damage_type_spinner);


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

    public void submitQuery(View view) {
        int p1 = mDisasterSpinner.getSelectedItemPosition();
        String p2 = mReportSpinner.getSelectedItem().toString();

        Intent returnIntent = new Intent();
        HashMap<String, String> data = new HashMap<String, String >();
        data.put("tab_id", "1");
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

