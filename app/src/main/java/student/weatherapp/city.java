package student.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class city extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        final EditText editText=findViewById(R.id.searchCity);
        ImageView backButton=findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> finish());

        editText.setOnEditorActionListener((v, actionId, event) -> {
            String newCity= editText.getText().toString();
            Intent intent=new Intent(city.this,MainActivity.class);
            intent.putExtra("City",newCity);
            startActivity(intent);

            return false;
        });





    }
}