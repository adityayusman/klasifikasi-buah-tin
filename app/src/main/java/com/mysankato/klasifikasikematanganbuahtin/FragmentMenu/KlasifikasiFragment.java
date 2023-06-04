package com.mysankato.klasifikasikematanganbuahtin.FragmentMenu;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mysankato.klasifikasikematanganbuahtin.DataModel.HasilKlasifikasi;
import com.mysankato.klasifikasikematanganbuahtin.DataModel.HasilKlasifikasiDao;
import com.mysankato.klasifikasikematanganbuahtin.DataModel.ImageConverter;
import com.mysankato.klasifikasikematanganbuahtin.DataModel.RoomDB;
import com.mysankato.klasifikasikematanganbuahtin.R;
import com.mysankato.klasifikasikematanganbuahtin.ml.MobileNetV2FigRipeness2;
import com.theartofdev.edmodo.cropper.CropImage;

import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KlasifikasiFragment extends Fragment {

    ImageView imgTin;
    ImageButton addImgButton;
    TextView hasil;
    View view;
    int imgSize = 224;
    int channels = 3;
    String format;
    DateFormat df;
    Date t;
    String time;
    HasilKlasifikasiDao hasilKlasifikasiDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        view =  inflater.inflate(R.layout.fragment_klasifikasi, container, false);

        imgTin = view.findViewById(R.id.imgTin);
        addImgButton = view.findViewById(R.id.addImgButton);
        hasil = view.findViewById(R.id.hasil);

        format = "dd/MM/yyyy HH:mm";
        df = new SimpleDateFormat(format);
        t = Calendar.getInstance().getTime();
        time = df.format(t);

        hasilKlasifikasiDao = RoomDB.getInstance(this.getContext()).hasilKlasifikasiDao();

        // Click Listener to add image.
        addImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(requireContext(), KlasifikasiFragment.this);
            }
        });
        return view;
    }

    // Method to classify fig ripeness.
    public void klasifikasi(Bitmap image) {
        try {
            MobileNetV2FigRipeness2 model = MobileNetV2FigRipeness2.newInstance(this.requireContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, imgSize, imgSize, channels}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imgSize * imgSize * channels);
            byteBuffer.order(ByteOrder.nativeOrder());

            //Get 1D array of 224 * 224 pixels in image
            Bitmap input = Bitmap.createScaledBitmap(image, 224, 224,true);
            int[] intValue = new int[imgSize * imgSize];
            image.getPixels(intValue, 0, input.getWidth(), 0, 0, input.getWidth(), input.getHeight());

            //iterate over pixels and extract RGB value, add to bytebuffer
            int pixel = 0;
            for (int i = 0; i < imgSize; i++) {
                for (int j = 0; j < imgSize; j++) {
                    int val = intValue[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16 ) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8 ) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            MobileNetV2FigRipeness2.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();

            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Belum matang", "Matang", "Tidak Teridentifikasi"};
            hasil.setText(classes[maxPos]);
            
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                // Get Uri image.
                assert result != null;
                Uri dat = result.getUri();
                // Convert Uri to Bitmap.
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.requireContext().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgTin.setImageBitmap(image);
                // Scaling Bitmap.
                image = Bitmap.createScaledBitmap(image, imgSize, imgSize, false);
                // Method for classificattion.
                klasifikasi(image);

                // Save classification result to database.
                HasilKlasifikasi hasilKlasifikasi = new HasilKlasifikasi();
                hasilKlasifikasi.setFigImage(ImageConverter.convertImage2ByteArray(image));
                hasilKlasifikasi.setResult(hasil.getText().toString());
                hasilKlasifikasi.setTime(time);
                hasilKlasifikasiDao.insertHasilKlasifikasi(hasilKlasifikasi);

                // Toast when save is successfully.
                Toast.makeText(getContext(), "Insert Image Successfully", Toast.LENGTH_SHORT).show();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
            }

        }
    }
}