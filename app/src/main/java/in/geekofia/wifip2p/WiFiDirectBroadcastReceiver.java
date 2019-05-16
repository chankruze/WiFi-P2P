package in.geekofia.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mWifiP2pManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager mWifiP2pManager, WifiP2pManager.Channel mChannel, MainActivity mActivity) {
        this.mWifiP2pManager = mWifiP2pManager;
        this.mChannel = mChannel;
        this.mActivity = mActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String mAction = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(mAction)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "WiFi is ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WiFi is OFF", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(mAction)) {
            // TO DO
            if (mWifiP2pManager != null) {
                mWifiP2pManager.requestPeers(mChannel, mActivity.mPeerListListener);
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(mAction)) {
            // TO DO
            if (mWifiP2pManager != null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                mWifiP2pManager.requestConnectionInfo(mChannel, mActivity.connectionInfoListener);
            } else {
                mActivity.connectionStats.setText("Disconnected");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(mAction)) {
            // TO DO
        }
    }
}
