package com.aviral.batball.Logic;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.batball.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyItem extends AppCompatActivity {
    private int totalCost = 0;
    TextView tcost;


    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ArrayList<Map<String, String>> items = (ArrayList<Map<String, String>>) getIntent().getSerializableExtra("items");

        ListView listView = findViewById(R.id.itemsList);

        ArrayList<String> items_ = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.buy_list_item, items_);

        listView.setAdapter(adapter);

        for (Map<String, String> item : items) {
            for (Map.Entry<String, String> entry : item.entrySet()) {
                String id = entry.getKey();
                String cost = entry.getValue();
                int c = Integer.parseInt(cost);
                getName(id, nameFetched -> {
                    items_.add(nameFetched + " - ₹" + cost);
                    adapter.notifyDataSetChanged();
                });

                totalCost += c;
                adapter.notifyDataSetChanged();
            }
        }

        tcost = findViewById(R.id.buy_cost);
        tcost.setText("Total Cost: ₹ " + totalCost);
    }


    private void getName(String itemID, NameCallback callback) {
        firestore.collection("products").get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot doc : querySnapshot) {
                if (!doc.exists()) continue;

                Map<String, Object> data = doc.getData();
                if (data == null) continue;

                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String fieldName = entry.getKey();
                    Object fieldValue = entry.getValue();

                    // Skip itemno or non-map entries
                    if ("itemno".equals(fieldName) || !(fieldValue instanceof Map)) continue;

                    Map<String, Object> itemMap;
                    try {
                        itemMap = (Map<String, Object>) fieldValue;
                    } catch (ClassCastException e) {
                        Log.e("ProductViewActivity", "Skipping invalid field: " + fieldName);
                        continue;
                    }

                    // Check category
                    Object typeObj = itemMap.get("itemKey");
                    if (typeObj == null || !itemID.equals(typeObj.toString())) continue;

                    // Safe extraction of fields
                    String name = itemMap.get("title") != null ? itemMap.get("title").toString() : "";
                    String cost = itemMap.get("cost") != null ? itemMap.get("cost").toString() : "";
                    String desc = itemMap.get("desc") != null ? itemMap.get("desc").toString() : "";
                    String img = itemMap.get("img") != null ? itemMap.get("img").toString() : "";
                    String cat = itemMap.get("cat") != null ? itemMap.get("cat").toString() : "";
                    String itemKey = itemMap.get("itemKey") != null ? itemMap.get("itemKey").toString() : "";
                    callback.onNameFetched(name != null ? name : "");
                    List<String> urls = new ArrayList<>();
                    Object urlsObj = itemMap.get("urls");
                    if (urlsObj instanceof List) {
                        for (Object url : (List<?>) urlsObj) {
                            if (url != null) urls.add(url.toString());
                        }
                    }
                }
            }
        });


    }

    public interface NameCallback {
        void onNameFetched(String name);
    }
}



