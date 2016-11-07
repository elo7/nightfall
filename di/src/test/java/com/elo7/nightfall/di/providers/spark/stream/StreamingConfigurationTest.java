package com.elo7.nightfall.di.providers.spark.stream;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StreamingConfigurationTest {

    private StreamingConfiguration subject;

    @Before
    public void setup() {
        subject = new StreamingConfiguration();
    }

    @Test
    public void shouldReturnTrueWhenCheckpointsAndWriteAheadLogAreEnabled() {
        subject.setCheckpointDir("/check/points/dir");
        subject.setWriteAheadLog(true);

        assertEquals(Boolean.TRUE.toString(), subject.isWriteAheadLog());
    }

    @Test
    public void shouldReturnFalseWhenCheckpointsIsDisabledAndWriteAheadLogIsEnabled() {
        subject.setWriteAheadLog(true);

        assertEquals(Boolean.FALSE.toString(), subject.isWriteAheadLog());
    }

    @Test
    public void shouldReturnFalseWhenCheckpointsIsEnabledAndWriteAheadLogIsDisabled() {
        subject.setCheckpointDir("/check/points/dir");
        subject.setWriteAheadLog(false);

        assertEquals(Boolean.FALSE.toString(), subject.isWriteAheadLog());
    }
}
