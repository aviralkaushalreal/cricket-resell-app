package com.aviral.batball.Logic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.aviral.batball.R;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddImage extends AppCompatActivity {
    String name, desc, item, cost;

    ImageView img1, img2, img3, img4;
    TextView publish;

    ImageView targetImageView;
    private Uri selectedImageUri;

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    Uri[] images = new Uri[4];
    List<String> imagesUrl = new ArrayList<>(Arrays.asList("", "", "", ""));

    private int uploadedCount = 0;
    private int totalToUpload = 0;

    private final ActivityResultLauncher<String> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null && targetImageView != null) {
                    selectedImageUri = uri;
                    targetImageView.setImageURI(uri);

                    int id = targetImageView.getId();
                    if (id == R.id.im1) images[0] = selectedImageUri;
                    else if (id == R.id.im2) images[1] = selectedImageUri;
                    else if (id == R.id.im3) images[2] = selectedImageUri;
                    else if (id == R.id.im4) images[3] = selectedImageUri;
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        name = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        item = intent.getStringExtra("item");
        cost = intent.getStringExtra("cost");

        img1 = findViewById(R.id.im1);
        img2 = findViewById(R.id.im2);
        img3 = findViewById(R.id.im3);
        img4 = findViewById(R.id.im4);
        publish = findViewById(R.id.btnPublish);

        img1.setOnClickListener(v -> { targetImageView = img1; galleryLauncher.launch("image/*"); });
        img2.setOnClickListener(v -> { targetImageView = img2; galleryLauncher.launch("image/*"); });
        img3.setOnClickListener(v -> { targetImageView = img3; galleryLauncher.launch("image/*"); });
        img4.setOnClickListener(v -> { targetImageView = img4; galleryLauncher.launch("image/*"); });

        publish.setOnClickListener(v -> {
            uploadedCount = 0;
            totalToUpload = 0;
            imagesUrl = new ArrayList<>(Arrays.asList("", "", "", ""));

            for (Uri img : images) if (img != null) totalToUpload++;

            if (totalToUpload == 0) {
                Toast.makeText(this, "Please select at least one image", Toast.LENGTH_SHORT).show();
                return;
            }

            for (Uri img : images) if (img != null) uploadToCloudinary(img);
        });
    }

    private void uploadToCloudinary(Uri img) {
        MediaManager.get().upload(img)
                .unsigned("batball_preset")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(AddImage.this, "Uploading...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) { }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = resultData.get("secure_url").toString();
                        for (int i = 0; i < imagesUrl.size(); i++) {
                            if (imagesUrl.get(i).isEmpty()) {
                                imagesUrl.set(i, imageUrl);
                                break;
                            }
                        }

                        uploadedCount++;
                        if (uploadedCount == totalToUpload) saveProductToFirestore();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(AddImage.this, "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        uploadedCount++;
                        if (uploadedCount == totalToUpload) saveProductToFirestore();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) { }
                }).dispatch();
    }

    private void saveProductToFirestore() {
        firestore.collection("products")
                .document(auth.getCurrentUser().getUid())
                .get().addOnSuccessListener(documentSnapshot -> {
                    long itemno = 1;
                    if (documentSnapshot.exists() && documentSnapshot.contains("itemno")) {
                        itemno = documentSnapshot.getLong("itemno") + 1;
                        documentSnapshot.getReference().update("itemno", FieldValue.increment(1));
                    } else {
                        documentSnapshot.getReference().set(new HashMap<String,Object>() {{
                            put("itemno", 1);
                        }}, SetOptions.merge());
                    }

                    String itemKey = "item" + itemno;

                    // Pick first non-empty image as thumbnail
                    String thumbnail = "";
                    for (String url : imagesUrl) {
                        if (!url.isEmpty()) {
                            thumbnail = url;
                            break;
                        }
                    }

                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("title", name);
                    productMap.put("cost", cost);
                    productMap.put("cat", item);
                    productMap.put("img", thumbnail);
                    productMap.put("urls", imagesUrl);
                    productMap.put("desc", desc);
                    productMap.put("status", "pending");
                    productMap.put("itemKey", auth.getCurrentUser().getUid()+itemKey);

                    documentSnapshot.getReference()
                            .set(new HashMap<String, Object>() {{
                                put(itemKey, productMap);
                            }}, SetOptions.merge())
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(AddImage.this, "Product Published!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, SellSuccessActivity.class);
                                intent.putStringArrayListExtra("urls", new ArrayList<>(imagesUrl));
                                intent.putExtra("title", name);
                                intent.putExtra("cost", cost);
                                intent.putExtra("type", item);
                                intent.putExtra("desc", desc);
                                intent.putExtra("form", "preview");
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(AddImage.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                });
    }
}
