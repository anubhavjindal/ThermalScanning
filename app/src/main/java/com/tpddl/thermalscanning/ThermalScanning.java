package com.tpddl.thermalscanning;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ThermalScanning extends AppCompatActivity {

    protected File file;
    FileOutputStream os = null;
    String feederName = "report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thermal_scanning);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Bundle bundle = getIntent().getExtras();
        feederName = bundle.getString("fn");

        final Spinner kvaSpinner = (Spinner) findViewById(R.id.kvaSpinner);
        final Spinner goSwitchSpinner = (Spinner) findViewById(R.id.goSwitchSpinner);
        final Spinner ddAssemblySpinner = (Spinner) findViewById(R.id.ddAssemblySpinner);
        final Spinner hotspotSpinner = (Spinner) findViewById(R.id.hotspotSpinner);
        final Spinner earthingSpinner = (Spinner) findViewById(R.id.earthingSpinner);

        final EditText locationText = (EditText) findViewById(R.id.locationText);
        final EditText remarksText = (EditText) findViewById(R.id.remarksText);
        final EditText imageNumberText = (EditText) findViewById(R.id.imageNumberText);
        final EditText temperatureText = (EditText) findViewById(R.id.temperatureText);
        final EditText hotspotText = (EditText) findViewById(R.id.hotspotText);

        final TextView feederText = (TextView) findViewById(R.id.feederText);

        final RadioGroup ddAssemblyRadioGroup = (RadioGroup) findViewById(R.id.ddAssemblyRadioGroup);
        final RadioGroup oilLevelRadioGroup = (RadioGroup) findViewById(R.id.oilLevelRadioGroup);
        final RadioGroup mccbRadioGroup = (RadioGroup) findViewById(R.id.mccbRadioGroup);
        final RadioGroup fencingRadioGroup = (RadioGroup) findViewById(R.id.fencingRadioGroup);
        final RadioGroup breatherRadioGroup = (RadioGroup) findViewById(R.id.breatherRadioGroup);
        final RadioGroup lvCoverRadioGroup = (RadioGroup) findViewById(R.id.lvCoverRadioGroup);
        final RadioGroup barrelRadioGroup = (RadioGroup) findViewById(R.id.barrelRadioGroup);
        final RadioGroup polyproRadioGroup = (RadioGroup) findViewById(R.id.polyproRadioGroup);
        final RadioGroup phaseRadioGroup = (RadioGroup) findViewById(R.id.phaseRadioGroup);

        final LinearLayout circuitLayout = (LinearLayout) findViewById(R.id.circuitLayout);
        final LinearLayout phaseLayout = (LinearLayout) findViewById(R.id.phaseLayout);

        final ToggleButton earthingToggle = (ToggleButton) findViewById(R.id.earthingToggle);
        final ToggleButton treeTrimmingToggle = (ToggleButton) findViewById(R.id.treeTrimmingToggle);
        final ToggleButton polyproToggle = (ToggleButton) findViewById(R.id.polyproToggle);
        final ToggleButton circuitToggle = (ToggleButton) findViewById(R.id.circuitToggle);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        Button emailButton = (Button) findViewById(R.id.emailButton);
        Button openFileButton = (Button) findViewById(R.id.openFileButton);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.kva_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.goSwitch_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.ddAssembly_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.hotspot_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this, R.array.earthing_array, android.R.layout.simple_spinner_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        feederText.setText(feederName);

        kvaSpinner.setAdapter(adapter1);
        goSwitchSpinner.setAdapter(adapter2);
        ddAssemblySpinner.setAdapter(adapter3);
        hotspotSpinner.setAdapter(adapter4);
        earthingSpinner.setAdapter(adapter5);


        hotspotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    phaseLayout.setVisibility(View.VISIBLE);
                    circuitLayout.setVisibility(View.VISIBLE);
                    hotspotText.setVisibility(View.VISIBLE);
                } else if (position == 0) {
                    phaseLayout.setVisibility(View.GONE);
                    circuitLayout.setVisibility(View.GONE);
                    hotspotText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ddAssemblySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 3) {
                    ddAssemblyRadioGroup.setVisibility(View.VISIBLE);
                }
                if (position == 2) {
                    ddAssemblyRadioGroup.setVisibility(View.GONE);
                }
                if (position == 1) {
                    ddAssemblyRadioGroup.setVisibility(View.GONE);
                }
                if (position == 0) {
                    ddAssemblyRadioGroup.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(parent.getContext(), "No DD Assembly Status selected!", Toast.LENGTH_SHORT).show();
            }
        });

        polyproToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    polyproRadioGroup.setVisibility(View.VISIBLE);
                }
                if (!isChecked) {
                    polyproRadioGroup.setVisibility(View.GONE);
                }
            }
        });

        earthingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    earthingSpinner.setVisibility(View.VISIBLE);
                }
                if (!isChecked) {
                    earthingSpinner.setVisibility(View.GONE);
                }
            }
        });


        final Workbook wb = new HSSFWorkbook();

        Cell c = null;

        //Styling
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.YELLOW.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        final Sheet sheet1 = wb.createSheet(feederName);

        //Generate Column Headings
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("S.No");
        c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("Location");
        c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("KVA");
        c.setCellStyle(cs);

        c = row.createCell(3);
        c.setCellValue("Hotspot");
        c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("Temperature");
        c.setCellStyle(cs);

        c = row.createCell(5);
        c.setCellValue("G.O Switch Status");
        c.setCellStyle(cs);

        c = row.createCell(6);
        c.setCellValue("D.D Assembly Status");
        c.setCellStyle(cs);

        c = row.createCell(7);
        c.setCellValue("D.D Required");
        c.setCellStyle(cs);

        c = row.createCell(8);
        c.setCellValue("Oil Level Status");
        c.setCellStyle(cs);

        c = row.createCell(9);
        c.setCellValue("Breather Status");
        c.setCellStyle(cs);

        c = row.createCell(10);
        c.setCellValue("L.V Cover Status");
        c.setCellStyle(cs);

        c = row.createCell(11);
        c.setCellValue("MCCB Status");
        c.setCellStyle(cs);

        c = row.createCell(12);
        c.setCellValue("Fencing Status");
        c.setCellStyle(cs);

        c = row.createCell(13);
        c.setCellValue("Barrel Status");
        c.setCellStyle(cs);

        c = row.createCell(14);
        c.setCellValue("Earthing Status");
        c.setCellStyle(cs);

        c = row.createCell(15);
        c.setCellValue("Tree Trimming");
        c.setCellStyle(cs);

        c = row.createCell(16);
        c.setCellValue("Polypro Required");
        c.setCellStyle(cs);

        c = row.createCell(17);
        c.setCellValue("Image Number");
        c.setCellStyle(cs);

        c = row.createCell(18);
        c.setCellValue("Remarks");
        c.setCellStyle(cs);

        sheet1.setColumnWidth(0, (15 * 100));
        sheet1.setColumnWidth(1, (15 * 350));
        sheet1.setColumnWidth(2, (15 * 100));
        sheet1.setColumnWidth(3, (15 * 450));
        sheet1.setColumnWidth(4, (15 * 200));
        sheet1.setColumnWidth(5, (15 * 400));
        sheet1.setColumnWidth(6, (15 * 350));
        sheet1.setColumnWidth(7, (15 * 300));
        sheet1.setColumnWidth(8, (15 * 300));
        sheet1.setColumnWidth(9, (15 * 350));
        sheet1.setColumnWidth(10, (15 * 300));
        sheet1.setColumnWidth(11, (15 * 350));
        sheet1.setColumnWidth(12, (15 * 250));
        sheet1.setColumnWidth(13, (15 * 250));
        sheet1.setColumnWidth(14, (15 * 250));
        sheet1.setColumnWidth(15, (15 * 250));
        sheet1.setColumnWidth(16, (15 * 350));
        sheet1.setColumnWidth(17, (15 * 250));
        sheet1.setColumnWidth(18, (15 * 600));

        final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        file = new File(this.getExternalFilesDir(null), "ThermalScanningReport_" + timeStamp + ".xls");
        if (file.exists() == false && !locationText.getText().toString().isEmpty()) {
            os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Log.w("FileUtils", "Writing file" + file);
            } catch (IOException e) {
                Log.w("FileUtils", "Error writing " + file, e);
            } catch (Exception e) {
                Log.w("FileUtils", "Failed to save file", e);
            }
        }


        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file.exists()) {
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.ms-excel");
                    startActivityForResult(intent, 10);
                } else if (!file.exists()) {
                    Toast.makeText(ThermalScanning.this, "You haven't submitted any records yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    // set the type to 'email'
                    emailIntent.setType("vnd.android.cursor.dir/email");
                    String to[] = {"sanjeev.jindal@tatapower-ddl.com"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    // the attachment
                    emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                    // the mail subject
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Thermal Scanning Report");
                    // the mail body
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Thermal Scanning Report for " + timeStamp + " has been attached with this email.");
                    startActivity(Intent.createChooser(emailIntent, "Send Report via Email..."));
                } else if (!file.exists()) {
                    Toast.makeText(getApplicationContext(), "No report has been generated yet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {

            int n = 1;

            @Override
            public void onClick(View v) {


                if (locationText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Location is required!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(ThermalScanning.this, "Response submitted successfully!", Toast.LENGTH_SHORT).show();
                Row rown = sheet1.createRow(n);

                String ddSt, olSt, breatherSt, lvcSt, mccbSt, fencingSt, barrelSt, polyproSt, phaseSt,earthingSt;
                int selectedDDRequired = ddAssemblyRadioGroup.getCheckedRadioButtonId();
                if (selectedDDRequired == -1) {
                    ddSt = "NA";
                } else {
                    RadioButton selectedDDradioButton = (RadioButton) findViewById(selectedDDRequired);
                    ddSt = selectedDDradioButton.getText().toString();
                }

                int selectedPhase = phaseRadioGroup.getCheckedRadioButtonId();
                if (selectedPhase == -1) {
                    phaseSt = "R";
                } else {
                    RadioButton selectedPhaseRadio = (RadioButton) findViewById(selectedPhase);
                    phaseSt = selectedPhaseRadio.getText().toString();
                }

                int selectedOilLevel = oilLevelRadioGroup.getCheckedRadioButtonId();
                if (selectedOilLevel == -1) {
                    olSt = "OK";
                } else {
                    RadioButton selectedOilLvelRadioButton = (RadioButton) findViewById(selectedOilLevel);
                    olSt = selectedOilLvelRadioButton.getText().toString();
                }

                int breatherStatus = breatherRadioGroup.getCheckedRadioButtonId();
                if (breatherStatus == -1) {
                    breatherSt = "OK";
                } else {
                    RadioButton selectedBreatherradioButton = (RadioButton) findViewById(breatherStatus);
                    breatherSt = selectedBreatherradioButton.getText().toString();
                }

                int lvCoverStatus = lvCoverRadioGroup.getCheckedRadioButtonId();
                if (lvCoverStatus == -1) {
                    lvcSt = "NA";
                } else {
                    RadioButton selectedLVCoverradioButton = (RadioButton) findViewById(lvCoverStatus);
                    lvcSt = selectedLVCoverradioButton.getText().toString();
                }

                int mccbStatus = mccbRadioGroup.getCheckedRadioButtonId();
                if (mccbStatus == -1) {
                    mccbSt = "OK";
                } else {
                    RadioButton selectedMCCBradioButton = (RadioButton) findViewById(mccbStatus);
                    mccbSt = selectedMCCBradioButton.getText().toString();
                }

                int fencingStatus = fencingRadioGroup.getCheckedRadioButtonId();
                if (fencingStatus == -1) {
                    fencingSt = "OK";
                } else {
                    RadioButton selectedFencingradioButton = (RadioButton) findViewById(fencingStatus);
                    fencingSt = selectedFencingradioButton.getText().toString();
                }
                int barrelStatus = barrelRadioGroup.getCheckedRadioButtonId();
                if (barrelStatus == -1) {
                    barrelSt = "OK";
                } else {
                    RadioButton selectedBarrelradioButton = (RadioButton) findViewById(barrelStatus);
                    barrelSt = selectedBarrelradioButton.getText().toString();
                }
                int polyproStatus = polyproRadioGroup.getCheckedRadioButtonId();
                if(earthingToggle.isChecked()){
                    earthingSt = earthingSpinner.getSelectedItem().toString();
                }
                else {
                    earthingSt = "OK";
                }

                if (polyproStatus == -1) {
                    polyproSt = "No";
                } else {
                    RadioButton selectedPolyPro = (RadioButton) findViewById(polyproStatus);
                    polyproSt = selectedPolyPro.getText().toString();
                }

                Cell cn0 = rown.createCell(0);
                cn0.setCellValue(n);
                Cell cn1 = rown.createCell(1);
                cn1.setCellValue(locationText.getText().toString());
                Cell cn2 = rown.createCell(2);
                cn2.setCellValue(kvaSpinner.getSelectedItem().toString());
                Cell cn3 = rown.createCell(3);
                if (hotspotSpinner.getSelectedItem().toString().equals("N.A")) {
                    cn3.setCellValue(hotspotSpinner.getSelectedItem().toString());
                } else {
                    cn3.setCellValue(hotspotSpinner.getSelectedItem().toString()
                            + "  Phase:" + phaseSt
                            + "  Circuit:" + circuitToggle.getText().toString()
                            + " " + hotspotText.getText().toString());
                }
                Cell cn4 = rown.createCell(4);
                if(!temperatureText.getText().toString().isEmpty()) {
                    cn4.setCellValue(temperatureText.getText().toString() + " °C");
                }
                else {
                    cn4.setCellValue("");
                }
                Cell cn5 = rown.createCell(5);
                cn5.setCellValue(goSwitchSpinner.getSelectedItem().toString());
                Cell cn6 = rown.createCell(6);
                cn6.setCellValue(ddAssemblySpinner.getSelectedItem().toString());
                Cell cn7 = rown.createCell(7);
                cn7.setCellValue(ddSt);
                Cell cn8 = rown.createCell(8);
                cn8.setCellValue(olSt);
                Cell cn9 = rown.createCell(9);
                cn9.setCellValue(breatherSt);
                Cell cn10 = rown.createCell(10);
                cn10.setCellValue(lvcSt);
                Cell cn11 = rown.createCell(11);
                cn11.setCellValue(mccbSt);
                Cell cn12 = rown.createCell(12);
                cn12.setCellValue(fencingSt);
                Cell cn13 = rown.createCell(13);
                cn13.setCellValue(barrelSt);
                Cell cn14 = rown.createCell(14);
                cn14.setCellValue(earthingSt);
                Cell cn15 = rown.createCell(15);
                cn15.setCellValue(treeTrimmingToggle.getText().toString());
                Cell cn16 = rown.createCell(16);
                cn16.setCellValue(polyproSt);
                Cell cn17 = rown.createCell(17);
                cn17.setCellValue(imageNumberText.getText().toString());
                Cell cn18 = rown.createCell(18);
                cn18.setCellValue(remarksText.getText().toString());

                FileOutputStream os2 = null;

                try {
                    os2 = new FileOutputStream(file);
                    wb.write(os2);
                    Log.w("FileUtils", "Writing file" + file);
                } catch (IOException e) {
                    Log.w("FileUtils", "Error writing " + file, e);
                } catch (Exception e) {
                    Log.w("FileUtils", "Failed to save file", e);
                }
                n++;

                locationText.setText(locationText.getText().toString());
                hotspotSpinner.setSelection(0);
                imageNumberText.setText("");
                temperatureText.setText("");
                hotspotText.setText("");
                ddAssemblySpinner.setSelection(0);
                goSwitchSpinner.setSelection(0);
                earthingSpinner.setSelection(0);
                kvaSpinner.setSelection(0);
                barrelRadioGroup.clearCheck();
                breatherRadioGroup.clearCheck();
                ddAssemblyRadioGroup.clearCheck();
                fencingRadioGroup.clearCheck();
                lvCoverRadioGroup.clearCheck();
                mccbRadioGroup.clearCheck();
                oilLevelRadioGroup.clearCheck();
                polyproRadioGroup.clearCheck();
                earthingToggle.setChecked(false);
                treeTrimmingToggle.setChecked(false);
                polyproToggle.setChecked(false);
                circuitToggle.setChecked(false);
                remarksText.setText("");
                locationText.requestFocus();
                locationText.setSelected(true);

            }

        });


        try {
            if (null != os)
                os.close();
        }
        catch (Exception ex) {
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(ThermalScanning.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm Exit")
                .setMessage("Do you really want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (file.exists()) {
                            Toast.makeText(getApplicationContext(), "Report was saved to device storage at Android/data/com.tpddl.thermalscanning/files", Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else if (!file.exists()) {
                            Toast.makeText(getApplicationContext(), "You did not submit any data this time. See you again :)", Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
