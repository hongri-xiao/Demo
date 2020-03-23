package com.xiao.lib.utils;

public class PermissionUtils {

       public static final String TAG="PermissionUtils";

       /* public static void requestPermission(FragmentActivity activity, OnRequestPermissionListener listener, String... permissions) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {

                if (AndPermission.hasPermissions(activity, permissions)) {
                    if (listener != null)
                        listener.onGranted();
                    return;
                }

                AndPermission.with(activity)
                        .runtime()
                        .permission(permissions)
                        .onGranted(data -> {
                            listener.onGranted();
                        })
                        .onDenied(data -> {


                            //申请失败
                            if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {

                                  new AlertDialog.Builder(activity,R.style.Theme_AppCompat_Light_Dialog_Alert)
                                          .setTitle(R.string.tip_title)
                                          .setMessage(R.string.tip_permission_again)
                                          .setPositiveButton(R.string.btn_ok,(dialog,witch)->{
                                              requestAgain(activity,listener,permissions);

                                             })
                                          .setNegativeButton(R.string.btn_cancel,(dialog,witch)->{
                                              listener.onDenied(true);

                                            })
                                          .setCancelable(false)
                                          .create()
                                          .show();


                            }else{
                                listener.onDenied(false);
                            }


                        })
                        .start();
            } else {
                listener.onGranted();
            }
        }*/

    public interface OnRequestPermissionListener {
        void onGranted();

        void onDenied(boolean isAlways);

    }



   /* public static void requestAgain(FragmentActivity activity, OnRequestPermissionListener listener, String... permissions){
        // 这些权限被用户总是拒绝。
              AndPermission.with(activity)
                     .runtime()
                      .setting()
                      .onComeback(new Setting.Action() {
                          @Override
                          public void onAction() {
                              requestPermission(activity, listener, permissions);
                              // 用户从设置回来了。
                          }
                      })
                      .start();
    }*/


}
