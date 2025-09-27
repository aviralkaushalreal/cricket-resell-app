package com.aviral.batball.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.aviral.batball.dataObjects.Product;
import com.aviral.batball.Adapter.ProductAdapter;
import com.aviral.batball.R;
import com.aviral.batball.Logic.Slide;
import com.aviral.batball.Adapter.SlideAdapter;
import com.aviral.batball.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        List<Slide> slideList = new ArrayList<>();
        slideList.add(new Slide(R.drawable.cricketgood_img, "Cricket Goods"));
        slideList.add(new Slide(R.drawable.streaming_img, "Streaming Services"));
        slideList.add(new Slide(R.drawable.cricketnews_img, "News"));

        SlideAdapter adapter = new SlideAdapter(slideList);
        ViewPager2 viewPager2 = root.findViewById(R.id.sliderViewPager);
        viewPager2.setAdapter(adapter);

        RecyclerView recyclerView = root.findViewById(R.id.itemrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<Product> productList = new ArrayList<>();
        productList.add(new Product(R.drawable.bat, "Bat"));
        productList.add(new Product(R.drawable.ball, "Ball"));
        productList.add(new Product(R.drawable.kit, "Complete Kit"));
        productList.add(new Product(R.drawable.gloves, "Gloves"));
        productList.add(new Product(R.drawable.pads, "Pads"));
        productList.add(new Product(R.drawable.shoes, "Shoes"));
        productList.add(new Product(R.drawable.helmet, "Helmet"));
        ProductAdapter adapter_ = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter_);

        List<Product> streamList = new ArrayList<>();
        streamList.add(new Product(R.drawable.hotstar, "Hotstar"));
        streamList.add(new Product(R.drawable.primevideo, "Prime Video"));
        streamList.add(new Product(R.drawable.sonyliv, "Sony LIV"));
        streamList.add(new Product(R.drawable.skysports, "Sky Sports"));
        ProductAdapter streamAdapter = new ProductAdapter(streamList);

        RecyclerView streamRecycler = root.findViewById(R.id.streamRecycler);
        streamRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        streamRecycler.setAdapter(streamAdapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}