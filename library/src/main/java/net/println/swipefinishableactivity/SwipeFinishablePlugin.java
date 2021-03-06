package net.println.swipefinishableactivity;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by benny on 9/22/16.
 */
public class SwipeFinishablePlugin {
    public static final String TAG = "SwipeFinishablePlugin";

    private Activity swipableActivity;
    private ActivityRootLayout activityRootLayout;

    public SwipeFinishablePlugin(Activity swipableActivity) {
        if(swipableActivity instanceof ActivityController.SwipableActivity) {
            this.swipableActivity = swipableActivity;
        }else{
            throw new UnsupportedOperationException("Activity passed in is not a instance of SwipeActivity.");
        }
    }

    public ActivityRootLayout getDecorView(){
        return activityRootLayout;
    }

    //region animator
    public interface OnTranslationUpdateListener {
        void onUpdate(float progress);
    }

    private OnTranslationUpdateListener onTranslationUpdateListener;

    public void setOnTranslationUpdateListener(OnTranslationUpdateListener onTranslationUpdateListener){
        this.onTranslationUpdateListener = onTranslationUpdateListener;
    }

    public void setTranslationX(float translationX){
        if(this.onTranslationUpdateListener != null){
            this.onTranslationUpdateListener.onUpdate(translationX);
        }
//        getDecorView().setTranslationX(translationX);
        //getDecorView().offsetLeftAndRight((int) translationX - getDecorView().getLeft());
        getDecorView().setOffset(translationX);
        getDecorView().invalidate();
    }

    public float getTranslationX(){
        return getDecorView().getOffset();
    }

    public int getWidth(){
        return getDecorView().getWidth();
    }

    public void onCreate(){
        activityRootLayout = new ActivityRootLayout(swipableActivity);
    }

    public void onPostCreate(){
        ViewGroup rootView = (ViewGroup) swipableActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        if(activityRootLayout.getParent() == rootView) {
            Log.d(TAG, "onPostCreate() called, in layout.");
        }else {
            View[] children = new View[rootView.getChildCount()];
            Log.d(TAG, "onPostCreate() called = " + rootView.getChildCount());
            for (int i = 0; i < rootView.getChildCount(); i++) {
                children[i] = rootView.getChildAt(i);
            }
            rootView.removeAllViews();
            rootView.addView(activityRootLayout, -1, -1);
            for (View child : children) {
                activityRootLayout.addView(child);
            }
        }
    }

    public void onStart() {
        getDecorView().setAtTop(true);
    }

    public void onStop() {
        getDecorView().setAtTop(false);
    }

    public void finish() {
        ActivityController.INSTANCE.finishCurrentActivity();
    }

    void finishThisActivity(){
        ((ActivityController.SwipableActivity) swipableActivity).finishThisActivity();
    }
}
