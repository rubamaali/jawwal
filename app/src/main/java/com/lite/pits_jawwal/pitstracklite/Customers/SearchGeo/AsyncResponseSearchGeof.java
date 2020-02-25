package com.lite.pits_jawwal.pitstracklite.Customers.SearchGeo;


import com.lite.pits_jawwal.pitstracklite.Categories.PlaceData;

import java.util.ArrayList;

public interface AsyncResponseSearchGeof {
    void processFinish(ArrayList<PlaceData> result);
}
