package kr.co.company.sw_team4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

// 인터넷 연결 확인
// 와이파이, 데이터에 연결 되면 1로 변환, 인터넷 연결이 안 되면 0으로 변환
public class Internet {
    public static int Get_Internet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 1;
            }
        }
        return 0;
    }
}
