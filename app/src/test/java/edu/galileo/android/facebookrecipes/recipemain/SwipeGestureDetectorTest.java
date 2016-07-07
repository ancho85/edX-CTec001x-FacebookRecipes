package edu.galileo.android.facebookrecipes.recipemain;

import android.view.MotionEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import edu.galileo.android.facebookrecipes.BaseTest;
import edu.galileo.android.facebookrecipes.BuildConfig;
import edu.galileo.android.facebookrecipes.recipemain.ui.SwipeGestureDetector;
import edu.galileo.android.facebookrecipes.recipemain.ui.SwipeGestureListener;

import static org.mockito.Mockito.verify;

/**
 * Created by carlos.gomez on 06/07/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SwipeGestureDetectorTest extends BaseTest {
    @Mock
    private SwipeGestureListener listener;

    private SwipeGestureDetector detector;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        detector = new SwipeGestureDetector(listener);
    }

    @Test
    public void testSwipeRight_shouldCallKeepOnListener() throws Exception {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = 500;
        float yEnd = 250;
        float velocityX = 120;

        MotionEvent e1 = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE, xStart, yStart, 0);
        MotionEvent e2 = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, xEnd, yEnd, 0);

        detector.onFling(e1, e2, velocityX, 0);
        verify(listener).onKeep();
    }

    @Test
    public void testSwipeLeft_shouldCallDismissOnListener() throws Exception {
        long downTime = 0;
        long moveTime = downTime + 500;
        long upTime = downTime + 1000;
        float xStart = 200;
        float yStart = 200;
        float xEnd = -500;
        float yEnd = 250;
        float velocityX = 120;

        MotionEvent e1 = MotionEvent.obtain(downTime, moveTime, MotionEvent.ACTION_MOVE, xStart, yStart, 0);
        MotionEvent e2 = MotionEvent.obtain(downTime, upTime, MotionEvent.ACTION_UP, xEnd, yEnd, 0);

        detector.onFling(e1, e2, velocityX, 0);
        verify(listener).onDismiss();
    }
}
