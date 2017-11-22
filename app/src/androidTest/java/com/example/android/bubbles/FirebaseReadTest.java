package com.example.android.bubbles;

import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class FirebaseReadTest extends AppCompatActivity{

    @Test
    public void testReadingFromDatabase() throws Exception {
        final String BLUE_DB_BUB = "BlueBubble";
        final String GREEN_DB_BUB = "GreenBubble";
        final String RED_DB_BUB = "RedBubble";

        final String expectedBlueTime = "99";
        final String expectedGreenTime = "3";
        final String expectedRedTime = "99";

        final CountDownLatch readSignal = new CountDownLatch(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference levelTwoReference = database.getReference("LevelTwo");

        levelTwoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assertEquals(expectedBlueTime, dataSnapshot.child(BLUE_DB_BUB).getValue(Long.class).toString());
                assertEquals(expectedGreenTime, dataSnapshot.child(GREEN_DB_BUB).getValue(Long.class).toString());
                assertEquals(expectedRedTime, dataSnapshot.child(RED_DB_BUB).getValue(Long.class).toString());
                readSignal.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        readSignal.await(10, TimeUnit.SECONDS);
    }

}
