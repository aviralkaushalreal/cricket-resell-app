package com.aviral.batball.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.aviral.batball.Logic.AddImage;
import com.aviral.batball.R;
import com.aviral.batball.databinding.FragmentDashboardBinding;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

public class DashboardFragment extends Fragment {
    String item;

    TextView title,desc,btnNext,cost;


    private FragmentDashboardBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Spinner spinner = root.findViewById(R.id.itemTypeSpinner);
        title = root.findViewById(R.id.sell_name);
        desc = root.findViewById(R.id.sell_desc);
        cost = root.findViewById(R.id.sell_cost);
        btnNext = root.findViewById(R.id.btnNextStep);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.item_types,
                android.R.layout.simple_spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        btnNext.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), AddImage.class);
            intent.putExtra("title",title.getText().toString());
            intent.putExtra("desc",desc.getText().toString());
            intent.putExtra("item",item);
            intent.putExtra("cost",cost.getText().toString());
            startActivity(intent);
        });




        return root;
    }
}

