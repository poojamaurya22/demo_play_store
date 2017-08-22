## HOW TO

#### Permissions
The following permissions are needed in Android Manifest:

 * android:name="android.permission.INTERNET"
 * android:name="android.permission.ACCESS_NETWORK_STATE"
 * android:name="android.permission.ACCESS_WIFI_STATE"

### Start updater client

The updater client typically starts during application initialization, simply by creating a new instance.

```java
AutoUpdateApk aua = new AutoUpdateApk(getApplicationContext(), UPDATE_URL);
```

where UPDATE_URL is your update server URL, eg.  
```java
public final static String UPDATE_URL = "http://avinashsingh.co/updater";
```

### Configuration

Updater configuration may be any time after its creation.

```java
// auto update every day
aua.setUpdateInterval(AutoUpdateApk.DAYS * 1);
```

