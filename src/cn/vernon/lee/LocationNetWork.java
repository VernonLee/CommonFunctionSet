package cn.vernon.lee;

import java.util.List;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.neocross.gateway.R;
import com.neocross.gateway.utils.AMapUtil;
import com.neocross.gateway.utils.ToastUtil;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

/**
 * 描述：自动定位获取天气
 * 实现方式：第三方 高德地图
 * 具体实现：
 *   导入官方jar包 AMap_services_vx.xx  Android_Location_vxxx
 *   下载链接：http://lbs.amap.com/api/android-location-sdk/down/
 *                   http://lbs.amap.com/api/android-sdk/down/ 
 *    申请key 并在manifest文件中的<meta-data>加入
 *    
 * @since 2014-9-23
 * @author huailiang
 */
public class AMapHomePageActivity extends Activity implements 
				AMapLocationListener, AMapLocalWeatherListener, Runnable {
	private TextView tvCity, tvTempture, tvDetail;
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation; // 用于判断超时定位
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		initViews();
	}
	
	private void initViews() {
		tvCity = (TextView) findViewById(R.id.txt_location_city);
		tvTempture = (TextView) findViewById(R.id.txt_location_temp);
 		tvDetail = (TextView) findViewById(R.id.txt_location_detail);
 		
 		aMapLocManager = LocationManagerProxy.getInstance(this);
 		aMapLocManager.requestLocationUpdates(
 				LocationProviderProxy.AMapNetwork,12*1000, 0, this);
 		// 设置12秒后没有定位 就停止定位
 		handler.postDelayed(this, 12*1000);
 		
 		aMapLocManager.requestWeatherUpdates(
                 LocationManagerProxy.WEATHER_TYPE_LIVE, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 停止定位
		stopLocation();
	}
	
	private void stopLocation() {
		if(aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}
	
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void run() {
		if(aMapLocation == null) {
			ToastUtil.show(this, "12秒内还没有定位成功，停止定位");
			tvCity.setText("12秒内还没有定位成功，停止定位");
			stopLocation();// 销毁掉定位
		}
	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if(location != null) {
			this.aMapLocation = location;
			Double latitude = location.getLatitude();
			Double longitude = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + longitude + "," + latitude + ")"
					+ "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n定位方式:" + location.getProvider() + "\n定位时间:"
					+ AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
					+ cityCode + "\n位置描述:" + desc + "\n省:"
					+ location.getProvince() + "\n市:" + location.getCity()
					+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
					.getAdCode());
			tvCity.setText(str);
		}
	}
	
	// 天气预报
	@Override
	public void onWeatherForecaseSearched(AMapLocalWeatherForecast aMapLocalWeatherForecast) {
		if(aMapLocalWeatherForecast != null && aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0){
			 
	        List<AMapLocalDayWeatherForecast> forcasts = aMapLocalWeatherForecast
	                .getWeatherForecast();
	        for (int i = 0; i < forcasts.size(); i++) {
	            AMapLocalDayWeatherForecast forcast = forcasts.get(i);
	            switch (i) {
	            //今天天气
	            case 0:
	                                    //城市
	                String city = forcast.getCity();
	                                    String today = "今天 ( "+ forcast.getDate() + " )";
	                String todayWeather = forcast.getDayWeather() + "    "
	                        + forcast.getDayTemp() + "/" + forcast.getNightTemp()
	                        + "    " + forcast.getDayWindPower();
	                break;
	            //明天天气
	            case 1:
	                 
	                String tomorrow = "明天 ( "+ forcast.getDate() + " )";
	                String tomorrowWeather = forcast.getDayWeather() + "    "
	                        + forcast.getDayTemp() + "/" + forcast.getNightTemp()
	                        + "    " + forcast.getDayWindPower();
	                break;
	            //后天天气
	            case 2:
	                 
	                String aftertomorrow = "后天( "+ forcast.getDate() + " )";
	                String aftertomorrowWeather = forcast.getDayWeather() + "    "
	                        + forcast.getDayTemp() + "/" + forcast.getNightTemp()
	                        + "    " + forcast.getDayWindPower();
	                break;
	            }
	        }
	    }else{
	        // 获取天气预报失败
	    	 ToastUtil.show(this,"获取天气预报失败:"+ aMapLocalWeatherForecast.getAMapException().getErrorMessage());
	    }
	}

	// 实时天气
	@Override
	public void onWeatherLiveSearched(AMapLocalWeatherLive  aMapLocalWeatherLive) {
		if(aMapLocalWeatherLive != null && aMapLocalWeatherLive.getAMapException().getErrorCode() ==0) {
				String city = aMapLocalWeatherLive.getCity();//城市
		        String weather = aMapLocalWeatherLive.getWeather();//天气情况
		        String windDir = aMapLocalWeatherLive.getWindDir();//风向
		        String windPower = aMapLocalWeatherLive.getWindPower();//风力
		        String humidity = aMapLocalWeatherLive.getHumidity();//空气湿度
		        String reportTime = aMapLocalWeatherLive.getReportTime();//数据发布时间
		        String str = "城市：" + city + "\n天气情况" + weather + "\n风向" + windDir
		        				   + "\n风力" + windPower + "\n空气湿度" + humidity + "\n发布时间" + reportTime; 
		        tvTempture.setText(str);
		}
		 // 获取天气预报失败
        ToastUtil.show(this,"获取天气预报失败:"+ aMapLocalWeatherLive.getAMapException().getErrorMessage());
	}
}
