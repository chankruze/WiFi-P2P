package in.geekofia.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnWifiToggle, btnDiscover;
    ImageButton btnSend;
    ListView listPeers;
    TextView readMessages, connectionStats;
    EditText writeMessage;

    WifiManager mWifiManager;
    WifiP2pManager mWifiP2pManager;
    WifiP2pManager.Channel mChannel;

    BroadcastReceiver mReceiver;
    IntentFilter mFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] mDeviceNameArray;
    WifiP2pDevice[] mDeviceArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        initButtons();
    }

    private void initButtons() {
        btnWifiToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWifiManager.isWifiEnabled()){
                    Toast.makeText(getApplicationContext(),"Turning Off WiFi ...", Toast.LENGTH_SHORT).show();
                    btnWifiToggle.setText("Turn WiFi On");
                    mWifiManager.setWifiEnabled(false);
                }else {
                    Toast.makeText(getApplicationContext(),"Turning On WiFi ...", Toast.LENGTH_SHORT).show();
                    btnWifiToggle.setText("Turn WiFi Off");
                    mWifiManager.setWifiEnabled(true);
                }
            }
        });

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWifiP2pManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectionStats.setText("Scanning");
                    }

                    @Override
                    public void onFailure(int reason) {
                        connectionStats.setText("Scan  Failed");
                    }
                });
            }
        });
    }

    private void initUi() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        btnWifiToggle = (Button) findViewById(R.id.wifiToggle);

        if(mWifiManager.isWifiEnabled()){
            Toast.makeText(getApplicationContext(),"WiFi is already enabled", Toast.LENGTH_LONG).show();
            btnWifiToggle.setText("Turn WiFi Off");
        }else {
            Toast.makeText(getApplicationContext(),"Turn On WiFi", Toast.LENGTH_LONG).show();
            btnWifiToggle.setText("Turn WiFi Off");
            mWifiManager.setWifiEnabled(true);
        }

        btnDiscover = (Button) findViewById(R.id.discoverPeers);
        btnDiscover.setText("Discover Peers");

        btnSend = (ImageButton) findViewById(R.id.sendMessage);

        listPeers = (ListView) findViewById(R.id.peersList);

        readMessages = (TextView) findViewById(R.id.readMessages);
        connectionStats = (TextView) findViewById(R.id.connectionStatus);
        connectionStats.setText("Ready");

        writeMessage = (EditText) findViewById(R.id.writeMessage);

        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), null);

        mReceiver = new WiFiDirectBroadcastReceiver(mWifiP2pManager, mChannel, this);
        mFilter = new IntentFilter();
        mFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener mPeerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                mDeviceNameArray = new String[peerList.getDeviceList().size()];
                mDeviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];

                int index = 0;

                for(WifiP2pDevice device:peerList.getDeviceList()){
                    mDeviceNameArray[index] = device.deviceName;
                    mDeviceArray[index] = device;
                    index++;

                }

                ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mDeviceNameArray );
                listPeers.setAdapter(mAdapter);

                if(peers.size() == 0){
                    Toast.makeText(getApplicationContext(), "No Peers Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause(){
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
