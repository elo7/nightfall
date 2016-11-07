package com.elo7.nightfall.persistence.cassandra;

import com.datastax.driver.core.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import scala.Tuple2;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CassandraRDDPairConsumerTest {

    @InjectMocks
    private CassandraRDDPairConsumer<String, String> subject;
    @Mock
    private CassandraConfiguration configuration;
    @Mock
    private CassandraConnection connection;
    @Mock
    private Session session;
    @Mock
    private CassandraPairConsumer<String, String> consumer;


    @Before
    public void setup() {
        when(connection.getSession()).thenReturn(session);
    }

    @Test
    public void shouldConsumeItemsTwiceWhenItemsSizeIsGreaterThanBatchSizeAndBatchSplitIsEnabled() {
        when(configuration.isBatchSplitEnabled()).thenReturn(true);
        when(configuration.getBatchSize()).thenReturn(2);
        Iterator<Tuple2<String, String>> items = createIterator("A", "B", "C");

        subject.consumeItemsSplitted(consumer, items, connection);
        verify(consumer, times(2)).consume(anyObject(), any(CassandraConnection.class));
    }

    @Test
    public void shouldConsumeItemsOnceWhenItemsSizeIsGreaterThanBatchSizeAndBatchSplitIsDisabled() {
        when(configuration.isBatchSplitEnabled()).thenReturn(false);
        when(configuration.getBatchSize()).thenReturn(2);
        Iterator<Tuple2<String, String>> items = createIterator("A", "B", "C");

        subject.consumePartition(consumer, items, connection);
        verify(consumer).consume(anyObject(), any(CassandraConnection.class));
    }

    @Test
    public void shouldConsumeItemsOnceWhenItemsSizeIsEqualToBatchSizeAndBatchSplitIsEnabled() {
        when(configuration.isBatchSplitEnabled()).thenReturn(true);
        when(configuration.getBatchSize()).thenReturn(2);
        Iterator<Tuple2<String, String>> items = createIterator("A", "B");

        subject.consumeItemsSplitted(consumer, items, connection);
        verify(consumer).consume(anyObject(), any(CassandraConnection.class));
    }

    private Iterator<Tuple2<String, String>> createIterator(String... items) {
        List<Tuple2<String, String>> itemList = new LinkedList<>();

        for (String item : items) {
            itemList.add(new Tuple2<>(item, "VALUE"));
        }

        return itemList.iterator();
    }

}
