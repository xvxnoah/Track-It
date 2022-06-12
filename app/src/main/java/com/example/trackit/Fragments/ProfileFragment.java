package com.example.trackit.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trackit.Account.AuthActivity;
import com.example.trackit.Model.UserInfo;
import com.example.trackit.R;
import com.example.trackit.ViewModel.AboutUs;
import com.example.trackit.initActivities.Info_Welcome_Page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final int RESULT_OK = -1;
    // creating a variable for our object class
    com.example.trackit.Model.UserInfo User;

    private Uri imageToUploadUri;

    // views for button
    private Button btnSelect, btnCamera;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;


    View view, view2;
    ImageView profilePic;
    FirebaseStorage storage;
    StorageReference storageReference;


    com.example.trackit.Model.UserInfo userInfo;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView mailID, userID;
    SwitchCompat protection;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        view2 = inflater.inflate(R.layout.bottom_edit_profile, container, false);
        btnSelect = view2.findViewById(R.id.optionGallery);

        storage = FirebaseStorage.getInstance("gs://track-it-86761.appspot.com");
        storageReference = storage.getReference();

        Button logOut = (Button)view.findViewById(R.id.logOut);
        Button aboutUs = (Button)view.findViewById(R.id.about_us_btn);
        profilePic = view.findViewById(R.id.profile_pic);

        mailID = view.findViewById(R.id.mail_id);
        userID = view.findViewById(R.id.profile_name);

        protection = view.findViewById(R.id.password_switch_profile);

        checkForProtection();

        userID.setText(UserInfo.getInstance().getName());

        firebaseDatabase = FirebaseDatabase.getInstance("https://track-it-86761-default-rtdb.europe-west1.firebasedatabase.app/");

        // Getting text from our edittext fields.
        SharedPreferences preferences = getActivity().getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);
        String email = preferences.getString(AuthActivity.USER, null);
        mailID.setText(email);
        email = email.replace('.', ',');

        userInfo = com.example.trackit.Model.UserInfo.getInstance();

        // Below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference("users/" + email);

        // Attach a listener to read the data at our posts reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userInfo = dataSnapshot.getValue(com.example.trackit.Model.UserInfo.class);
                com.example.trackit.Model.UserInfo.setUniqueInstance(userInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        StorageReference mImageStorage = FirebaseStorage.getInstance("gs://track-it-86761.appspot.com").getReference();
        if(userInfo.getImageStr() != null){
            StorageReference ref = mImageStorage.child("images/" + userInfo.getImageStr());

            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        String imageUrl = downUri.toString();
                        new DownloadImageTask((ImageView) view.findViewById(R.id.profile_pic)).execute(imageUrl);
                    }else{
                        Toast.makeText(view.getContext(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences saveSession = getActivity().getSharedPreferences(AuthActivity.CREDENTIALS, Context.MODE_PRIVATE);

                UserInfo.setUniqueInstance(null);

                saveSession.edit().clear().commit();

                FirebaseAuth.getInstance().signOut();

                if(!protection.isChecked()){
                    SharedPreferences clearCredentials = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                    clearCredentials.edit().clear().commit();

                    SharedPreferences.Editor profileCheck = getActivity().getSharedPreferences("checked", Context.MODE_PRIVATE).edit();
                    profileCheck.putString("check", "false");
                    profileCheck.apply();
                } else{
                    SharedPreferences.Editor profileCheck = getActivity().getSharedPreferences("checked", Context.MODE_PRIVATE).edit();
                    profileCheck.putString("check", "true");
                    profileCheck.apply();
                }
                Intent intent = new Intent(getActivity(), Info_Welcome_Page.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
            }
        });

        FloatingActionButton edit = view.findViewById(R.id.editProfile);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(R.layout.bottom_edit_profile);

                bottomSheetDialog.show();

                bottomSheetDialog.findViewById(R.id.optionCamera).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });

                bottomSheetDialog.findViewById(R.id.optionGallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
                uploadImage(userInfo.getEmail());
            }
        });
        return view;
    }

    private void checkForProtection() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("checked", Context.MODE_PRIVATE);

        String isChecked = sharedPreferences.getString("check", null);

        if(isChecked != null){
            if(isChecked.equals("true")){
                protection.setChecked(true);
            } else{
                protection.setChecked(false);
            }
        }
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Selecciona una imatge..."),
                22);
    }

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if(requestCode == 111 && resultCode == RESULT_OK){

        }
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            Button camera = view.findViewById(R.id.cameraProfile);
            camera.setVisibility(View.INVISIBLE);

            Button galllery = view.findViewById(R.id.galleryProfile);
            galllery.setVisibility(View.INVISIBLE);
            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContext().getContentResolver(),
                                filePath);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage(String email)
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(view.getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            String imageStr = email;
            User.setImageStr(imageStr) ;

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/" + imageStr);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(view.getContext(),
                                                    "Imatge de perfil pujada",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(view.getContext(),
                                            "Error " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Pujat "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }



}
