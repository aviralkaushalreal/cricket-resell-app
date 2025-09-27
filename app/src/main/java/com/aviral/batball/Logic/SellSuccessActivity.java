package com.aviral.batball.Logic;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.batball.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellSuccessActivity extends AppCompatActivity {

    TextView title, desc, type, cost, name, btnBuy, btnAddCart;
    ImageView img1, img2, img3, img4;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String itemID;

    private int count = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sell_success);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        btnBuy = findViewById(R.id.btnBuy);
        btnAddCart = findViewById(R.id.btnAddCart);
        title = findViewById(R.id.display_title);

        String form = intent.getStringExtra("form");
        if (form.equals("preview")) {
            btnBuy.setVisibility(GONE);
            btnAddCart.setVisibility(GONE);
            title.setVisibility(VISIBLE);
        }


        name = findViewById(R.id.display_name);
        cost = findViewById(R.id.display_cost);
        desc = findViewById(R.id.display_desc);
        type = findViewById(R.id.display_item);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        String[] urls = intent.getStringArrayListExtra("urls").toArray(new String[0]);

        for (int i = 0; i < urls.length; i++) {
            String s = urls[i];
            if (s == null || s.isEmpty()) continue; // skip empty entries

            if (i == 0) {
                Glide.with(this)
                        .load(s)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img1);
            } else if (i == 1) {
                Glide.with(this)
                        .load(s)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img2);
            } else if (i == 2) {
                Glide.with(this)
                        .load(s)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img3);
            } else if (i == 3) {
                Glide.with(this)
                        .load(s)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img4);
            }
        }

        name.setText(intent.getStringExtra("title"));
        cost.setText(intent.getStringExtra("cost"));
        desc.setText(intent.getStringExtra("desc"));
        type.setText(intent.getStringExtra("type"));
        itemID = intent.getStringExtra("itemKey");


        btnAddCart.setOnClickListener(v -> {
            Map<String,String> item= new HashMap<>();
            item.put(itemID,cost.getText().toString());
            firestore.collection("users")
                    .document(auth.getCurrentUser().getUid())
                    .update("cart", FieldValue.arrayUnion(item))
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Map<String, Object> cart = new HashMap<>();
                        cart.put("cart", java.util.Arrays.asList(item));
                        firestore.collection("users")
                                .document(auth.getCurrentUser().getUid())
                                .set(cart, com.google.firebase.firestore.SetOptions.merge());
                    });
        });

        btnBuy.setOnClickListener(v -> {
            Map<String,String> item= new HashMap<>();
            ArrayList<Map<String,String>> items=new ArrayList();
            item.put(itemID,cost.getText().toString());
            items.add(item);
            Intent intent1=new Intent(this, BuyItem.class);
            intent1.putExtra("items",items);
            startActivity(intent1);
        });
    }
}