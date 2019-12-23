package cn.endv.mytestapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.EditText;

import androidx.drawerlayout.widget.DrawerLayout;

import java.util.LinkedList;
import java.util.List;

// 继承百度移动统计 FrontiaApplication
public class App extends Application {
    private static App instance;
    //    private static Context _context;
    private static EditText editText;
    private static DrawerLayout mNavigationView;
    private static Activity activity;
    private static Context context;
    // 运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    private int score;

    // 构造方法
    // 实例化一次
    public synchronized static App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;
    }

    public static Context getContext() {
        if (instance == null) {
            instance = getInstance();
        }
        return context;
    }

    public static void setContext(Context score) {
        context = score;
    }

    public static String getHJYCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))

//			File directory_pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            return Environment.getExternalStorageDirectory().toString() + "/Health/Cache";
        else
            return "/System/cn.endv.tianyun/Cache";
    }

    public static DrawerLayout getNavigationView() {
        return mNavigationView;
    }

    public void setNavigationView(DrawerLayout score) {
        mNavigationView = score;
    }

    public void onCreate() {
        super.onCreate();
        if (context == null) {
            context = getApplicationContext();
        }
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null) //取消广播接收者的注册
//					unregisterReceiver(mMyBroadcastRecvier);
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
//       在activity里
//              //设置分数
//              ((App)getApplication()).setScore(90)
//              //获取分数
//              ((App)getApplication()).getScore();
//        在view里 Java代码：
//              ((App)getContext().getApplicationContext()).getScore()
// //
//@SuppressLint("NewApi")
//public boolean getMainTopApp( ) {
//	String lockAppName = "cn.endv.ProgressMainActivity";
//	String topActivityName = "";
//	ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//
//	List<ActivityManager.RunningTaskInfo> runningTasks = am
//			.getRunningTasks(1);
//	if (runningTasks != null && !runningTasks.isEmpty()) {
//		ActivityManager.RunningTaskInfo taskInfo = runningTasks.get(0);
//		topActivityName = taskInfo.topActivity.getClassName();
//	}
//	if (lockAppName.equals(topActivityName)) {
//		return true;
//	}
//	return false;
//}

    /**
     *
     * 判断某activity是否处于栈顶
     * @return true在栈顶 false不在栈顶
     */
//	@RequiresApi(api = Build.VERSION_CODES.Q)
//	private boolean isActivityTop(Class cls, Context context){
//		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		 String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
//		return name.equals(cls.getName());
//	}
//	static class RecentUseComparator implements Comparator<UsageStats> {
//
//		@Override
//		public int compare(UsageStats lhs,UsageStats rhs) {
//			return (lhs.getLastTimeUsed()> rhs.getLastTimeUsed()) ? -1 : (lhs
//					.getLastTimeUsed()== rhs.getLastTimeUsed()) ? 0 : 1;
//		}
//	}
//
//
//
//	public String getTopPackage() {
//		long ts = System.currentTimeMillis();
//		RecentUseComparator mRecentComp = new RecentUseComparator();
//		@SuppressLint("WrongConstant")
//		UsageStatsManager mUsageStatsManager =(UsageStatsManager) getSystemService("cn.endv.tianyun");
//		List<UsageStats> usageStats =mUsageStatsManager.queryUsageStats(
//				UsageStatsManager.INTERVAL_BEST,ts - 10000, ts); //查询ts-10000 到ts这段时间内的UsageStats，由于要设定时间限制，所以有可能获取不到
//
//		if (usageStats == null)
//			return "";
//		if (usageStats.size() == 0)
//			return "";
//		Collections.sort(usageStats,mRecentComp);
//
//
//
////		Log.d(TAG,"====usageStats.get(0).getPackageName()"+ usageStats.get(0).getPackageName());
//
//
//
//		return usageStats.get(0).getPackageName();
//
//	}

}
