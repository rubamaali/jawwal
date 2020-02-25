package com.lite.pits_jawwal.pitstracklite.Customers;


import android.content.Context;


import com.lite.pits_jawwal.pitstracklite.R;

import java.util.ArrayList;

public class LocationData {
     public static ArrayList<Location_type> getCategories(Context context) {
        ArrayList<Location_type> type_loc = new ArrayList<Location_type>();
        type_loc.add(new Location_type(context.getResources().getString(R.string.ATM),"101",R.drawable.atm,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Bank),"102",R.drawable.bank,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Cinema),"103",R.drawable.cinema,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Clothing_Store),"104",R.drawable.clothing_store,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Coffee_Shop),"105",R.drawable.coffee_shop,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Company),"106",R.drawable.company,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Court),"107",R.drawable.court,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Factory),"108",R.drawable.factory,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Fitness_Center),"9",R.drawable.fitness_center,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Home),"110",R.drawable.home,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Hospital),"111",R.drawable.hospital,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Hotel),"112",R.drawable.hotel,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Library),"113",R.drawable.library,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Medical_Laboratory),"114",R.drawable.medical_laboratory,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Mosque),"115",R.drawable.mosque,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Museum),"116",R.drawable.museum,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Park),"117",R.drawable.park,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Petrol_Station),"118",R.drawable.petrol_station,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Pharmacy),"119",R.drawable.pharmacy,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Physician),"120",R.drawable.physician,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Police_Office),"121",R.drawable.police_office,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Coffee_Shop),"122",R.drawable.restaurant,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.School),"123",R.drawable.school,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Shopping_Center),"124",R.drawable.shopping_center,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Square_Roundabout),"125",R.drawable.square_roundabout,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Stadium),"126",R.drawable.stadium,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Street),"127",R.drawable.street,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.Supermarket),"128",R.drawable.supermarket,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.University),"129",R.drawable.university,0));
        type_loc.add(new Location_type(context.getResources().getString(R.string.All),"0",R.drawable.ic_all,0));
        return type_loc;
    }

}
