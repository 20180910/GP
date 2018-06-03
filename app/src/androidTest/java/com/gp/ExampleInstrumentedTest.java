package com.gp;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.gp.tools.CopyFile;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("mygp.gp", appContext.getPackageName());
    }
    @Test
    public void sdf() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

//        assertEquals("mygp.gp", appContext.getPackageName());
        String pathDatabase=appContext.getDatabasePath("MyGP").getPath();
        System.out.println("path="+pathDatabase);

        String newPath=Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+Constant.rootFileName+"/" + "MyGP"+System.currentTimeMillis();

        int copy = CopyFile.copySdcardFile(pathDatabase, newPath);
        System.out.println("path=="+copy);
    }
}
