package com.elo7.nightfall.riposte.task;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RiposteTaskTest {

	@InjectMocks
	private RiposteTask subject;
	@Mock
	private Dataset<Row> dataset;
	@Mock
	private RiposteConfiguration configuration;
	@Mock
	private DatasetConsumer consumer;
	@Mock
	private SparkSession session;

	@Test
	public void shouldPrintSchemaWhenItIsEnabled() {
		when(configuration.printSchema()).thenReturn(true);

		subject.process();

		verify(dataset).printSchema();
	}

	@Test
	public void shouldNotPrintSchemaWhenItIsDisabled() {
		when(configuration.printSchema()).thenReturn(false);

		subject.process();

		verify(dataset, never()).printSchema();
	}

	@Test
	public void shouldConsumeDataset() {
		when(configuration.sql()).thenReturn("sql");
		when(session.sql(anyString())).thenReturn(dataset);

		subject.process();

		verify(consumer).consume(dataset);
	}
}