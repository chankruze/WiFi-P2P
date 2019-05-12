package in.geekofia.wifip2p;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnWifiToggle, btnDiscover;
    ImageButton btnSend;
    ListView listPeers;
    TextView readMessages, connectionStats;
    EditText writeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
    }

    private void initUi() {
        btnWifiToggle = (Button) findViewById(R.id.wifiToggle);
        btnWifiToggle.setText("Turn WiFi On");

        btnDiscover = (Button) findViewById(R.id.discoverPeers);
        btnDiscover.setText("Discover Peers");

        btnSend = (ImageButton) findViewById(R.id.sendMessage);

        listPeers = (ListView) findViewById(R.id.peersList);

        readMessages = (TextView) findViewById(R.id.readMessages);
        connectionStats = (TextView) findViewById(R.id.connectionStatus);

        writeMessage = (EditText) findViewById(R.id.writeMessage);
    }
}
