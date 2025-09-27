package com.aviral.batball.Logic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.batball.Adapter.ProductItemAdapter;
import com.aviral.batball.R;
import com.aviral.batball.dataObjects.userProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductViewActivity extends AppCompatActivity {
    private String categ;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private ProductItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        categ = intent.getStringExtra("type");

        List<userProduct> up = new ArrayList<>();
        RecyclerView rvMovies = findViewById(R.id.trending_recycler);
        adapter = new ProductItemAdapter(up);
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvMovies.setAdapter(adapter);

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


                    Object typeObj = itemMap.get("cat");
                    if (typeObj == null || !categ.equals(typeObj.toString()) || "sold".equals(itemMap.get("status"))) continue;
                    String title = itemMap.get("title") != null ? itemMap.get("title").toString() : "";
                    String cost = itemMap.get("cost") != null ? itemMap.get("cost").toString() : "";
                    String desc = itemMap.get("desc") != null ? itemMap.get("desc").toString() : "";
                    String img = itemMap.get("img") != null ? itemMap.get("img").toString() : "";
                    String cat = itemMap.get("cat") != null ? itemMap.get("cat").toString() : "";
                    String itemKey = itemMap.get("itemKey") != null ? itemMap.get("itemKey").toString() : "";
                    String status = itemMap.get("status") != null ? itemMap.get("status").toString() : "";
                    List<String> urls = new ArrayList<>();
                    Object urlsObj = itemMap.get("urls");
                    if (urlsObj instanceof List) {
                        for (Object url : (List<?>) urlsObj) {
                            if (url != null) urls.add(url.toString());
                        }
                    }

                    userProduct userP = new userProduct(title, cost, cat, img, urls, desc, itemKey,status);
                    up.add(userP);
                }
            }

            adapter.notifyDataSetChanged();
        });

    }
}
