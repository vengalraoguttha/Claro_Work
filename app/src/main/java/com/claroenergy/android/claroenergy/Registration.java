package com.claroenergy.android.claroenergy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener {

    Button next1, next2, complete, add;
    LinearLayout l1, l2, l3;
    Customer customer;
    EditText nameCustomer;
    EditText fathNameCustomer;
    EditText Address;
    EditText contactNo;
    EditText AdharNo;
    EditText monthExp;
    EditText previousCrop;
    EditText nextCrop;
    EditText irrigationCost;
    EditText incomeFromLand;
    EditText totalLandHolding;
    EditText seed, fertilizer, other, ravi, kharif, relName, relContact, job, disease, loan, age, yearlyIncome, govtCardHolder, doneBy;
    RadioButton gender, toH, mtoS, vec;
    RadioGroup genderGroup, toH_group, mtoS_group, vec_group;
    Spinner s1, s2;
    int count = 0;
    GPSTracker gps;
    int i=0;
    List<Person> list;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private LocationRequest locationRequest;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST=112;

    public void bindViews() {
        nameCustomer = (EditText) findViewById(R.id.name_customer);
        fathNameCustomer = (EditText) findViewById(R.id.father_name_customer);
        Address = (EditText) findViewById(R.id.address_customer);
        contactNo = (EditText) findViewById(R.id.contact_customer);
        AdharNo = (EditText) findViewById(R.id.adhar_number);
        monthExp = (EditText) findViewById(R.id.month_expense_customer);
        previousCrop = (EditText) findViewById(R.id.previous_crop_customer);
        nextCrop = (EditText) findViewById(R.id.next_crop_customer);
        irrigationCost = (EditText) findViewById(R.id.irrigation_cost_customer);
        incomeFromLand = (EditText) findViewById(R.id.income_from_land);
        totalLandHolding = (EditText) findViewById(R.id.total_land_holding);
        seed = (EditText) findViewById(R.id.seed);
        fertilizer = (EditText) findViewById(R.id.fertilizer);
        other = (EditText) findViewById(R.id.other);
        ravi = (EditText) findViewById(R.id.ravi);
        kharif = (EditText) findViewById(R.id.kharif);
        add = (Button) findViewById(R.id.add);
        relName = (EditText) findViewById(R.id.rel_name);
        relContact = (EditText) findViewById(R.id.rel_contact);
        job = (EditText) findViewById(R.id.job);
        disease = (EditText) findViewById(R.id.diseases);
        loan = (EditText) findViewById(R.id.loan);
        genderGroup = (RadioGroup) findViewById(R.id.gender_group);
        toH_group = (RadioGroup) findViewById(R.id.toh_group);
        mtoS_group = (RadioGroup) findViewById(R.id.mtoS_group);
        age = (EditText) findViewById(R.id.age);
        vec_group = (RadioGroup) findViewById(R.id.vec_group);
        s1 = (Spinner) findViewById(R.id.spinner);
        s2 = (Spinner) findViewById(R.id.education_spinner);
        yearlyIncome = (EditText) findViewById(R.id.yearly_income);
        govtCardHolder = (EditText) findViewById(R.id.gov_card_holder);
        doneBy = (EditText) findViewById(R.id.done_by);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        next1 = (Button) findViewById(R.id.next_1);
        next2 = (Button) findViewById(R.id.next_2);
        complete = (Button) findViewById(R.id.complete);
        l1 = (LinearLayout) findViewById(R.id.layout_1);
        l2 = (LinearLayout) findViewById(R.id.layout_2);
        l3 = (LinearLayout) findViewById(R.id.layout_3);
        customer = new Customer();
        bindViews();

        List<String> list1 = new ArrayList<>();
        list1.add("Customer");
        list1.add("HUSBAND");
        list1.add("WIFE");
        list1.add("SON");
        list1.add("DAUGHTER");

        List<String> list2 = new ArrayList<>();
        list2.add("below 10th standard");
        list2.add("10th standard");
        list2.add("12th standard");
        list2.add("Undergraduate");
        list2.add("Graduate");
        list2.add("Post Graduate");

        list = new ArrayList<>();


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(dataAdapter1);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(dataAdapter2);

        if(checkPlayServices()){
            //Toast.makeText(this,"play services present",Toast.LENGTH_LONG).show();
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
        }else {
            Toast.makeText(this,"play services not present",Toast.LENGTH_LONG).show();
        }

        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l1.getVisibility() == View.VISIBLE) {
                    l1.setVisibility(View.GONE);
                    l3.setVisibility(View.VISIBLE);
                    customer.customer_name = nameCustomer.getText().toString();
                    customer.customer_father_name = fathNameCustomer.getText().toString();
                    customer.customer_address = Address.getText().toString();
                    customer.customer_contactno = contactNo.getText().toString();
                    customer.customer_adhar_no = AdharNo.getText().toString();
                    customer.cutomer_yearly_income = yearlyIncome.getText().toString();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                Person person = new Person();
                person.name = relName.getText().toString();
                person.age = age.getText().toString();
                person.contact = relContact.getText().toString();
                person.job = job.getText().toString();
                person.diseases = disease.getText().toString();
                person.anyLoan = loan.getText().toString();
                int selectGenId = genderGroup.getCheckedRadioButtonId();
                gender = (RadioButton) findViewById(selectGenId);
                person.sex = gender.getText().toString();
                int selectVecId = vec_group.getCheckedRadioButtonId();
                vec = (RadioButton) findViewById(selectVecId);
                person.vechile = vec.getText().toString();
                person.relation = (String) s1.getSelectedItem();
                person.education = (String) s2.getSelectedItem();

                list.add(person);
                clearAll();
            }
        });
        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l3.getVisibility() == View.VISIBLE) {
                    l3.setVisibility(View.GONE);
                    l2.setVisibility(View.VISIBLE);
                    customer.family = list;
                }
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer.customer_monthly_exp = monthExp.getText().toString();
                customer.previousCrop = previousCrop.getText().toString();
                customer.nextCrop = nextCrop.getText().toString();
                customer.irrigationCost = irrigationCost.getText().toString();
                customer.incomeFromLand = incomeFromLand.getText().toString();
                customer.totalLandHolding = totalLandHolding.getText().toString();
                BuyFrom buyFrom = new BuyFrom();
                buyFrom.seed = seed.getText().toString();
                buyFrom.fertilizers = fertilizer.getText().toString();
                buyFrom.other = other.getText().toString();
                customer.buyFrom = buyFrom;
                SeasonCrop seasonCrop = new SeasonCrop();
                seasonCrop.ravi = ravi.getText().toString();
                seasonCrop.kharif = kharif.getText().toString();
                customer.seasonCrop = seasonCrop;
                int selectToHId = toH_group.getCheckedRadioButtonId();
                toH = (RadioButton) findViewById(selectToHId);
                customer.typeOfHouse = toH.getText().toString();
                int selectMtoSId = mtoS_group.getCheckedRadioButtonId();
                mtoS = (RadioButton) findViewById(selectMtoSId);
                customer.whereToSale = mtoS.getText().toString();
                customer.govtCardHolder = govtCardHolder.getText().toString();
                customer.doneBy = doneBy.getText().toString();

                Intent intent = new Intent(Registration.this, PhotoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected void onStart() {
        if(mGoogleApiClient!=null)
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkPlayServices();
        super.onResume();
    }

    protected void onStop() {
        if(mGoogleApiClient!=null)
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void clearAll() {
        relName.setText("");
        age.setText("");
        relContact.setText("");
        job.setText("");
        disease.setText("");
        loan.setText("");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null){
            if(i==0){
                customer.longitude=location.getLongitude()+"";
                customer.latitude=location.getLatitude()+"";
                Toast.makeText(this,customer.latitude+" "+customer.longitude,Toast.LENGTH_LONG).show();
            }
        }
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        Log.d("xxxxxxxxx","out");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, Locate.this);


        }
        //Toast.makeText(this,"came to f",Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this,"came to p",Toast.LENGTH_LONG).show();
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this,"loaction change ",Toast.LENGTH_LONG).show();
        if (location != null){
            if(i==0){
                i=1;
                customer.longitude=location.getLongitude()+"";
                customer.latitude=location.getLatitude()+"";
                Toast.makeText(this,customer.latitude+" "+customer.longitude,Toast.LENGTH_LONG).show();
            }
        }
    }
}
