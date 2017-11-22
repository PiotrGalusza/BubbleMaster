package com.example.android.bubbles;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.junit.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FirebaseWriteTest {

    @Test
    public void testWritingReadingToDatabase() throws Exception {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference levelTwoReference = database.getReference("LevelThree");
        long newBlueBubble = 30;
        long newGreenBubble = 20;
        long newRedBubble = 130;

        levelTwoReference.child("BlueBubble").setValue(newBlueBubble);
        levelTwoReference.child("GreenBubble").setValue(newGreenBubble);
        levelTwoReference.child("RedBubble").setValue(newRedBubble).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
                assert true;
            }
        });

        writeSignal.await(10, TimeUnit.SECONDS);
    }
}
