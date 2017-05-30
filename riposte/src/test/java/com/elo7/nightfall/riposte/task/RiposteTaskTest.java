package com.elo7.nightfall.riposte.task;

import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.any;
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
	private RelationalGroupedDataset groupedDataset;

	@Before
	public void setup() {
		when(configuration.query()).thenReturn(Optional.empty());
		when(configuration.filter()).thenReturn(Optional.empty());
		when(configuration.groupBy()).thenReturn(Optional.empty());
	}

	@Test
	public void should_not_apply_select_when_it_is_missing() {
		subject.process();

		verify(dataset, never()).select(any(Column[].class));
	}

	@Test
	public void should_apply_select_when_it_is_present() {
		String[] columns = {"col"};
		when(configuration.query()).thenReturn(Optional.of(columns));

		subject.process();

		verify(dataset).selectExpr(columns);
	}

	@Test
	public void should_not_apply_filter_when_it_is_missing() {
		subject.process();

		verify(dataset, never()).filter(anyString());
	}

	@Test
	public void should_apply_filter_when_it_is_present() {
		when(configuration.filter()).thenReturn(Optional.of("filter"));

		subject.process();

		verify(dataset).filter("filter");
	}

	@Test
	public void should_not_apply_group_by_when_it_is_missing() {
		subject.process();

		verify(dataset, never()).groupBy(any(Column.class));
	}

	@Test
	public void should_apply_group_by_when_it_is_present() {
		Column groupBy = new Column("groupBy");
		when(dataset.groupBy(groupBy)).thenReturn(groupedDataset);
		when(configuration.groupBy()).thenReturn(Optional.of(groupBy));

		subject.process();

		verify(dataset).groupBy(groupBy);
		verify(groupedDataset).count();
	}

	@Test
	public void should_consume_dataset() {
		subject.process();

		verify(consumer).consume(dataset);
	}
}