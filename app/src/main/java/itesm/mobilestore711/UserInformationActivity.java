package itesm.mobilestore711;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class UserInformationActivity extends AppCompatActivity {

    private ImageButton ib_back_to_home;
    private TextView tv_name;
    private TextView tv_username;

    // Save User information
    private MobileStoreApp app;
    private UserInformation userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        ib_back_to_home = (ImageButton) findViewById(R.id.ib_back_to_home_userInfo);
        tv_name = (TextView) findViewById(R.id.tv_userInfo_name);
        tv_username = (TextView) findViewById(R.id.tv_userInfo_username);

        // Get UserInfo
        app = (MobileStoreApp) getApplicationContext();
        userInfo = app.getUserInformation();

        // Set user info
        tv_name.setText(userInfo.getName());
        tv_username.setText(userInfo.getUsername());

        // Back to Cart button
        ib_back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(UserInformationActivity.this, HomePageActivity.class);
                UserInformationActivity.this.startActivity(homeIntent);
            }
        });
    }
}
