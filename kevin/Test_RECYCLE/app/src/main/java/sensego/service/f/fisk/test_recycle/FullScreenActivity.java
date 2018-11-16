package sensego.service.f.fisk.test_recycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Objects;

public class FullScreenActivity extends AppCompatActivity {

    private String img;


    private  final Integer image_ids[] = {
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5
    };

    private  final String image_titles[] = {
            "img1",
            "img2",
            "img3",
            "img4",
            "img5"
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img2) {
        this.img = (String) img2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        ImageView imageView1 = (ImageView) findViewById(R.id.full_screen_img);
        int i = 0;
        Bundle bundle = getIntent().getExtras();
        String lol = bundle.getString("hey");
        int z = 0;
        while (z == 0 && i < 4) {
            Toast.makeText(this, img, Toast.LENGTH_LONG).show();
            if (image_titles[i].equals(lol)) {
                z = 1;
            }
            i++;
        }
        imageView1.setImageResource(image_ids[i - 1]);
    }
}
