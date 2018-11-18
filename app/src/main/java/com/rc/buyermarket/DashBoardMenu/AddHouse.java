package com.rc.buyermarket.DashBoardMenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.rc.buyermarket.R;

public class AddHouse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);


        Spinner PurchaseType = findViewById(R.id.purchasetype);
        ArrayAdapter<CharSequence> PuAdapter = ArrayAdapter.createFromResource(this,R.array.purchase_type, android.R.layout.simple_spinner_item);
        PuAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        PurchaseType.setAdapter(PuAdapter);

        Spinner PreAppoved = findViewById(R.id.buy_preap);
        ArrayAdapter<CharSequence> preAdapter = ArrayAdapter.createFromResource(this,R.array.pre_approved, android.R.layout.simple_spinner_item);
        PuAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        PreAppoved.setAdapter(preAdapter );


        Spinner Credit= findViewById(R.id.buy_ceditSco);
        ArrayAdapter<CharSequence> CreAdapter = ArrayAdapter.createFromResource(this,R.array.buy_Credit, android.R.layout.simple_spinner_item);
        CreAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Credit.setAdapter(CreAdapter );

        Spinner Price= findViewById(R.id.buy_price);
        ArrayAdapter<CharSequence> priceAdapter = ArrayAdapter.createFromResource(this,R.array.buy_price, android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Price.setAdapter(priceAdapter );

        Spinner Propertytype = findViewById(R.id.buy_propertytype);
        ArrayAdapter<CharSequence> protypeAdapter = ArrayAdapter.createFromResource(this,R.array.buy_property, android.R.layout.simple_spinner_item);
        protypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Propertytype.setAdapter(protypeAdapter );

        Spinner State = findViewById(R.id.buy_state);
        ArrayAdapter<CharSequence>stateAdapter = ArrayAdapter.createFromResource(this,R.array.buy_State, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        State.setAdapter(stateAdapter );

        Spinner City = findViewById(R.id.buy_city);
        ArrayAdapter<CharSequence>cityAdapter = ArrayAdapter.createFromResource(this,R.array.buy_City, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        City.setAdapter(cityAdapter );

        Spinner BedRoom= findViewById(R.id.buy_bedrooms);
        ArrayAdapter<CharSequence>bedRAdapter = ArrayAdapter.createFromResource(this,R.array.Buy_bedroom, android.R.layout.simple_spinner_item);
        bedRAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        BedRoom.setAdapter(bedRAdapter);

        Spinner Bathroom = findViewById(R.id.buy_bathroom);
        ArrayAdapter<CharSequence>bathRAdapter = ArrayAdapter.createFromResource(this,R.array.buy_bath, android.R.layout.simple_spinner_item);
        bathRAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Bathroom.setAdapter(bathRAdapter);

        Spinner Basement= findViewById(R.id.buy_basement);
        ArrayAdapter<CharSequence>basemRAdapter = ArrayAdapter.createFromResource(this,R.array.buy_basement, android.R.layout.simple_spinner_item);
        basemRAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Basement.setAdapter(basemRAdapter);

        Spinner Garage = findViewById(R.id.buy_garage);
        ArrayAdapter<CharSequence>grageAdapter = ArrayAdapter.createFromResource(this,R.array.buy_Garage, android.R.layout.simple_spinner_item);
        grageAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Garage.setAdapter(grageAdapter);

        Spinner Style = findViewById(R.id.buy_style);
        ArrayAdapter<CharSequence>styleAdapter = ArrayAdapter.createFromResource(this,R.array.buy_Style, android.R.layout.simple_spinner_item);
        styleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Style.setAdapter(styleAdapter);

        Spinner Exterior = findViewById(R.id.buy_exterior);
        ArrayAdapter<CharSequence>exAdapter = ArrayAdapter.createFromResource(this,R.array.buy_Exterior, android.R.layout.simple_spinner_item);
        exAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Exterior.setAdapter(exAdapter);
    }
}
