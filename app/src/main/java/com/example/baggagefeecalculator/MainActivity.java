package com.example.baggagefeecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // declare variables
    TextView notice, noticeDescription, etWeight, etClass, etRate, results, otWeight, otCharge, otTotal;
    EditText inWeight, inRate;
    Button btCalculate;
    RadioButton radioEconomy, radioBusiness;
    RadioGroup rgTicketClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // tambah toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Initialize variables
        notice = findViewById(R.id.notice);
        noticeDescription = findViewById(R.id.noticeDescription);
        etWeight = findViewById(R.id.etWeight);
        etClass = findViewById(R.id.etClass);
        etRate = findViewById(R.id.etRate);
        results = findViewById(R.id.results);
        otWeight = findViewById(R.id.otWeight);
        otCharge = findViewById(R.id.otCharge);
        otTotal = findViewById(R.id.otTotal);

        inWeight = findViewById(R.id.inWeight);
        inRate = findViewById(R.id.inRate);

        btCalculate = findViewById(R.id.btCalculate);

        radioEconomy = findViewById(R.id.radioEconomy);
        radioBusiness = findViewById(R.id.radioBusiness);

        rgTicketClass = findViewById(R.id.rgTicketClass);

        btCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weight = inWeight.getText().toString();
                String rate = inRate.getText().toString();

                // Validate empty inputs
                if (weight.isEmpty()) {
                    inWeight.setError("Please enter baggage weight");
                    inWeight.requestFocus();
                    return;
                }
                if (rate.isEmpty()) {
                    inRate.setError("Please enter rate per kg");
                    inRate.requestFocus();
                    return;
                }

                double baggageWeight = 0;
                double baggageRate = 0;

                //error handling
                try {
                    baggageWeight = Double.parseDouble(weight);
                } catch (NumberFormatException e) {
                    inWeight.setError("Invalid input for baggage weight");
                    inWeight.requestFocus();
                    return;
                }
                try {
                    baggageRate = Double.parseDouble(rate);
                } catch (NumberFormatException e) {
                    inRate.setError("Invalid input for rate per kg");
                    inRate.requestFocus();
                    return;
                }
                //validate negative or zero value
                if (baggageWeight <= 0) {
                    inWeight.setError("Weight must be greater than 0");
                    inWeight.requestFocus();
                    return;
                }
                if (baggageRate <= 0) {
                    inRate.setError("Rate must be greater than 0");
                    inRate.requestFocus();
                    return;
                }

                //determine x
                double x = 0;
                if (radioEconomy.isChecked()) {
                    x = 20;
                } else if (radioBusiness.isChecked()) {
                    x = 40;
                } else {
                    etClass.setError("Please select ticket class");
                    return;
                }

                //calculate excess baggage weight
                double excessWeight = Math.max(0, baggageWeight - x);
                //calculate excess baggage charge
                double excessCharge = excessWeight * baggageRate;
                //calculate total payable amount
                double totalPayable = excessCharge * 1.08;

                //output kita
                otWeight.setText("Excess Baggage Weight: " + String.format("%.2f", excessWeight) + " kg");
                otCharge.setText("Excess Baggage Charge: RM " + String.format("%.2f", excessCharge));
                otTotal.setText("Total Amount Payable: RM " + String.format("%.2f", totalPayable));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // share button
        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");

            String shareBody = "Check out Baggage Fee Calculator app! Download/View here: https://github.com/Munirah12/Baggage_Fee_Calculator.git";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Baggage Fee Calculator");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

            startActivity(Intent.createChooser(shareIntent, "Share Application URL via"));
            return true;
        }
        // redirect
        else if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}