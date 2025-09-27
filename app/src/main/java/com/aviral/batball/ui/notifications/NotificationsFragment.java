package com.aviral.batball.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aviral.batball.Logic.BuyItem;
import com.aviral.batball.Adapter.CartAdapter;
import com.aviral.batball.R;
import com.aviral.batball.databinding.FragmentNotificationsBinding;
import com.aviral.batball.dataObjects.userProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    TextView tcostext,btnBuy;

    RecyclerView cartRecycler;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    CartAdapter adapter;

    private int tcost;

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cartRecycler = root.findViewById(R.id.cartRecycler);
        tcostext=root.findViewById(R.id.tcost);
        btnBuy=root.findViewById(R.id.btnBuy);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<userProduct> productList = new ArrayList<>();
        adapter = new CartAdapter(productList);
        cartRecycler.setAdapter(adapter);



        btnBuy.setOnClickListener(v -> {
            ArrayList<Map<String,String>> items=new ArrayList();

            for(userProduct it : productList)
            {

                Map<String,String> item= new HashMap<>();
                item.put(it.itemKey,it.cost);
                items.add(item);
            }

            Intent intent1=new Intent(getContext(), BuyItem.class);
            intent1.putExtra("items",items);
            startActivity(intent1);
        });


        firestore.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            ArrayList<String> ids = new ArrayList<>();

            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) documentSnapshot.get("cart");
            if (cartItems != null) {
                for (Map<String, Object> item : cartItems) {
                    // each item is a map
                    for (Map.Entry<String, Object> entry : item.entrySet()) {
                        String id = entry.getKey();
                        ids.add(id);
                        String cost = entry.getValue().toString();

                    }
                }
            }


            for (String s : ids) {
                firestore.collection("products").get().addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        if (!doc.exists()) continue;

                        Map<String, Object> data = doc.getData();
                        if (data == null) continue;

                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            String fieldName = entry.getKey();
                            Object fieldValue = entry.getValue();

                            // Skip itemno or non-map entries
                            if ("itemno".equals(fieldName) || !(fieldValue instanceof Map))
                                continue;

                            Map<String, Object> itemMap;
                            try {
                                itemMap = (Map<String, Object>) fieldValue;
                            } catch (ClassCastException e) {
                                Log.e("ProductViewActivity", "Skipping invalid field: " + fieldName);
                                continue;
                            }


                            Object typeObj = itemMap.get("itemKey");
                            if (typeObj == null || !s.equals(typeObj.toString()) || "sold".equals(itemMap.get("status")))
                                continue;
                            String title = itemMap.get("title") != null ? itemMap.get("title").toString() : "";
                            String cost = itemMap.get("cost") != null ? itemMap.get("cost").toString() : "";
                            String desc = itemMap.get("desc") != null ? itemMap.get("desc").toString() : "";
                            String img = itemMap.get("img") != null ? itemMap.get("img").toString() : "";
                            String cat = itemMap.get("cat") != null ? itemMap.get("cat").toString() : "";
                            String itemKey = itemMap.get("itemKey") != null ? itemMap.get("itemKey").toString() : "";
                            String status = itemMap.get("status") != null ? itemMap.get("status").toString() : "";
                            tcost += Integer.parseInt(cost);
                            tcostext.setText("Total Cost : ₹"+tcost);
                            List<String> urls = new ArrayList<>();
                            Object urlsObj = itemMap.get("urls");
                            if (urlsObj instanceof List) {
                                for (Object url : (List<?>) urlsObj) {
                                    if (url != null) urls.add(url.toString());
                                }
                            }

                            userProduct userP = new userProduct(title, cost, cat, img, urls, desc, itemKey, status);
                            productList.add(userP);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}