package com.example.agolovin.myconnectionmanager;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.TelephonyProperties;

public class MainActivity extends AppCompatActivity {

    private static final String APP_NAME = "CM+";
    private static final int INFO_DEVICE_INFO_INDEX = 0;

    /*
     * array of ids
     */
    private static final int[] info_ids = {
            R.id.deviceInfo
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTextViewText(info_ids[INFO_DEVICE_INFO_INDEX], displayPhoneSettings(-1));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //setTextViewText(info_ids[INFO_DEVICE_INFO_INDEX], "aaaaaaaaaaaaa");
            return true;
        }

        if (id == R.id.action_reload) {
            setTextViewText(info_ids[INFO_DEVICE_INFO_INDEX], displayPhoneSettings(-1));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /*
     * This method puts text information to the field
     */
    private void setTextViewText ( int id, String text ) {
        ((TextView) findViewById(id)).setText(text);
    }

    /*
     * This sub logs info
     */
    private int logString(String message) {
        return Log.i(APP_NAME, message);
    }

    /*
     * This function converts integer phone type to String
     */
    private String getPhoneTypeString (int type) {
        String typeStr = "Unknown";

        switch (type)
        {
            case TelephonyManager.PHONE_TYPE_GSM: typeStr = "GSM"; break;
            case TelephonyManager.PHONE_TYPE_NONE: typeStr = "NONE"; break;
            default: typeStr = "UNKNOWN"; break;
        }
        return typeStr;
    }

    /*
     * This sub gets phone settings that are not available in SDK
     */
    private String getInfoForSubId(int subId) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SubscriptionManager sm = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        SubscriptionInfo mSubInfo = (SubscriptionInfo) sm.getActiveSubscriptionInfo(subId);

        /*
        This section describes subscription info from class SubscriptionInfo
         */
        String deviceInfo = ("------- for subscription " + subId +" -------\n");
        deviceInfo += ("the subscription ID: " + mSubInfo.getSubscriptionId() + "\n");
        deviceInfo += ("the ICC ID: " + mSubInfo.getIccId() + "\n");
        deviceInfo += ("the slot index of this Subscription's SIM card: " + mSubInfo.getSimSlotIndex() + "\n");
        deviceInfo += ("the name displayed to the user that identifies this subscription: " + mSubInfo.getDisplayName() + "\n");
        deviceInfo += ("the name displayed to the user that identifies Subscription provider name: " + mSubInfo.getCarrierName() + "\n");
        deviceInfo += ("the source of the name: " + mSubInfo.getNameSource() + "\n");
        deviceInfo += ("the number of this subscription: " + mSubInfo.getNumber() + "\n");
        deviceInfo += ("the data roaming state for this subscription: " + mSubInfo.getDataRoaming() + "\n");
        deviceInfo += ("MCC: " + mSubInfo.getMcc() + mSubInfo.getMnc() + "\n");

        deviceInfo += ("Phone ID: " + sm.getPhoneId(subId) + "\n");
        deviceInfo += ("Get slotId associated with the subscription: " + sm.getSlotId(subId) + "\n");

        deviceInfo += ("the current phone type: " + tm.getCurrentPhoneType(subId) + "\n");
        deviceInfo += ("name of current registered operator: " + tm.getNetworkOperatorName(subId) + "\n");
        deviceInfo += ("Network Type: " + tm.getNetworkType(subId) + "\n");
        deviceInfo += ("Network Type Name: " + tm.getNetworkTypeName(tm.getNetworkType(subId)) + "\n");
        deviceInfo += ("Network Data Type: " + tm.getDataNetworkType(subId) + "\n");
        deviceInfo += ("Network Data Type Class: " + tm.getNetworkClass(tm.getDataNetworkType(subId)) + "\n");
        deviceInfo += ("Network Voice Type: " + tm.getVoiceNetworkType(subId) + "\n");
        deviceInfo += ("the MCC+MNC: " + tm.getSimOperator(subId) + "\n");
        deviceInfo += ("the MCC+MNC: " + tm.getSimOperatorNumericForSubscription(subId) + "\n");
        deviceInfo += ("Service Provider Name (SPN): " + tm.getSimOperatorNameForSubscription(subId) + "\n");
        deviceInfo += ("getLteOnCdmaMode: " + tm.getLteOnCdmaMode(subId) + "\n");
        deviceInfo += ("Subscriber ID (IMSI): " + tm.getSubscriberId(subId) + "\n");
        deviceInfo += ("Group Identifier Level1 for a GSM phone: " + tm.getGroupIdLevel1(subId) + "\n");
        deviceInfo += ("MSISDN: " + tm.getLine1NumberForSubscriber(subId) + "\n");
        deviceInfo += ("MSISDN: " + tm.getMsisdn(subId) + "\n");
        deviceInfo += ("alphabetic identifier: " + tm.getLine1AlphaTagForSubscriber(subId) + "\n");

        int mgetPreferredNetworkType = tm.getPreferredNetworkType();
        deviceInfo += ("getPreferredNetworkType: " + mgetPreferredNetworkType + "\n");
        //deviceInfo += ("getCellNetworkScanResults: " + tm.getCellNetworkScanResults() + "\n");

        ITelephony telephony = ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));

        /*int mgetPreferredNetworkType = 0;
        try {
            ITelephony mtelephony = ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
            if (telephony != null)
                mgetPreferredNetworkType = mtelephony.getPreferredNetworkType();
        } catch (RemoteException ex) {
            Rlog.e("asasas", "getPreferredNetworkType RemoteException", ex);
        } catch (NullPointerException ex) {
            Rlog.e("asdasas", "getPreferredNetworkType NPE", ex);
        }*/

        //int mgetPreferredNetworkType = telephony.getPreferredNetworkType(subId);
        //tm.setPreferredNetworkType()
        //deviceInfo += ("getPreferredNetworkType: " + mgetPreferredNetworkType + "\n");


        return deviceInfo;
    }

    /*
     * This sub gets phone settings that are not available in SDK
     */
    private String getInfoForPhoneSlotId(int slotId) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SubscriptionManager sm = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
       // SubscriptionInfo mSubInfo = (SubscriptionInfo) sm.getActiveSubscriptionInfo(subId);

        String deviceInfo = ("------- for slot " + slotId +" -------\n");
        deviceInfo += ("IMEI for slot: " + tm.getDeviceId(slotId) + "\n");
        deviceInfo += ("Subscription ID for slot: " + sm.getSubId(slotId) + "\n");

        deviceInfo += ("the state of the device SIM card in a slot: " + tm.getSimState(slotId) + "\n");
        deviceInfo += ("SW Version IMEI/SV for GSM phone: " + tm.getDeviceSoftwareVersion(slotId) + "\n");
        deviceInfo += ("IMEI/SV for GSM phone: " + tm.getImei(slotId) + "\n");
        deviceInfo += ("Nai for GSM phone: " + tm.getNai(slotId) + "\n");

        deviceInfo += ("getPhoneTypeFromProperty: " + tm.getTelephonyProperty(slotId, TelephonyProperties.CURRENT_ACTIVE_PHONE, null) + "\n");
        deviceInfo += ("getPhoneTypeFromNetworkType: " + tm.getTelephonyProperty(slotId, "ro.telephony.default_network", null) + "\n");

        deviceInfo += ("getNetworkOperatorForPhone: " + tm.getNetworkOperatorForPhone(slotId) + "\n");
        deviceInfo += ("the MCC+MNC: " + tm.getSimOperatorNumericForPhone(slotId) + "\n");
        deviceInfo += ("Service Provider Name (SPN): " + tm.getSimOperatorNameForPhone(slotId) + "\n");



        return deviceInfo;
    }

    /*
     * This sub displays phone settings of the phone
     */
    public String displayPhoneSettings (int phoneId) {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        SubscriptionManager sm = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

        int phoneCount = tm.getPhoneCount();
        GsmCellLocation loc = (GsmCellLocation) tm.getCellLocation();
        int cellid = loc.getCid();
        int lac = loc.getLac();
        int defaultSIM = tm.getDefaultSim();
        int defaultSubId = sm.getDefaultSubId();
        int defaultSubDataId = sm.getDefaultDataSubId();
        int defaultSubVoiceId = sm.getDefaultVoiceSubId();

        String deviceInfo = "------- for default subscription -------\n";
        deviceInfo += ("Phone Count: " + phoneCount + "\n");
        deviceInfo += ("CellID: " + cellid + "\n");
        deviceInfo += ("LAC: " + lac + "\n");
        //deviceInfo += ("Phone Number: " + tm.getLine1Number() + "\n");
        //deviceInfo += ("SW Version: " + tm.getDeviceSoftwareVersion() + "\n");
        //deviceInfo += ("SIM Country Code: " + tm.getSimCountryIso() + "\n");
        deviceInfo += ("Operator Name: " + tm.getNetworkOperatorName() + "\n"); // get operator name getDefaultSubId
        deviceInfo += ("SIM Operator: " + tm.getSimOperator() + "\n"); // get MNC MCC getDefaultSubId
        deviceInfo += ("SIM Serial Number: " + tm.getSimSerialNumber() + "\n"); // get SIM serial getDefaultSubId
        deviceInfo += ("Subscriber ID (IMSI): " + tm.getSubscriberId() + "\n"); // get IMSI getDefaultSubId
        deviceInfo += ("Network Type: " + tm.getNetworkTypeName() + "\n"); // get NET type getDefaultSubId
        deviceInfo += ("Phone Type: " + getPhoneTypeString(tm.getPhoneType()) + "\n"); // get phone type (GSM/LTE)

        deviceInfo += ("Default SIM (slotId/phoneId): " + defaultSIM + "\n");
        deviceInfo += ("Default Data Phone ID: " + sm.getDefaultDataPhoneId() + "\n");
        deviceInfo += ("Default Voice Phone ID: " + sm.getDefaultVoicePhoneId() + "\n");
        deviceInfo += ("Default SMS Phone ID: " + sm.getDefaultSmsPhoneId() + "\n");

        deviceInfo += ("Default Subscription ID: " + defaultSubId + "\n");
        deviceInfo += ("Default Data Subscription ID: " + defaultSubDataId + "\n");
        deviceInfo += ("Default Voice Subscription ID: " + defaultSubVoiceId + "\n");
        deviceInfo += ("Default SMS Subscription ID: " + sm.getDefaultSmsSubId() + "\n");

        deviceInfo += ("Multi Sim Configuration: " + tm.getMultiSimConfiguration() + "\n");
        deviceInfo += ("Multi Sim Configuration: " + SystemProperties.get(TelephonyProperties.PROPERTY_MULTI_SIM_CONFIG) + "\n");
        deviceInfo += ("if the current radio is LTE on CDMA: " + tm.getLteOnCdmaModeStatic() + "\n");

        for (int i = 1; i <= phoneCount; i++ ) {
            deviceInfo += getInfoForSubId(i);
            deviceInfo += getInfoForPhoneSlotId(i - 1);
        }

        logString(deviceInfo);
        return deviceInfo;








        /*
        public static int getPhoneType(int networkMode)
        !!!!! public String getNetworkOperatorName(int subId) {
        return slotId >= 0 && slotId < TelephonyManager.getDefault().getSimCount();
        return phoneId >= 0 && phoneId < TelephonyManager.getDefault().getPhoneCount();
        public static boolean isValidSlotId(int slotId)
        public static boolean isUsableSubIdValue(int subId) {
        public static boolean isValidPhoneId(int phoneId) {
        public static void putPhoneIdAndSubIdExtra(Intent intent, int phoneId, int subId)
        public static void setTelephonyProperty(int phoneId, String property, String value)
        setNetworkSelectionModeAutomatic(int subId) {
        setNetworkSelectionModeManual(int subId, OperatorInfo operator
        public CellNetworkScanResult getCellNetworkScanResults(int subId)
        public boolean setPreferredNetworkType(int subId, int networkType)
        public boolean setPreferredNetworkTypeToGlobal() {

        setPreferredNetworkType(getDefaultSubscription(),
                RILConstants.NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA);
                public void setDataEnabled(int subId, boolean enable)

                                    public boolean getDataEnabled(int subId) {
void setSimOperatorNameForPhone(int phoneId, String name)
public void setSimStateForPhone(int phoneId, String state) {
public void setNetworkOperatorNameForPhone(int phoneId, String name) {
public void setNetworkOperatorNumericForPhone(int phoneId, String numeric) {

public void setDataNetworkTypeForPhone(int phoneId, int type) {
         */
    }
}