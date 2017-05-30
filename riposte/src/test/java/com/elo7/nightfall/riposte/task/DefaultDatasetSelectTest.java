package com.elo7.nightfall.riposte.task;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDatasetSelectTest {

	@InjectMocks
	private DefaultDatasetSelect subject;
	@Mock
	private Dataset<Row> dataset;
	@Mock
	private RiposteConfiguration configuration;

	@Test
	public void should_not_apply_select_when_it_is_missing() {
		when(configuration.query()).thenReturn(Optional.empty());

		subject.apply(dataset);

		verify(dataset, never()).selectExpr(any(String[].class));
	}

	@Test
	public void should_apply_select_when_it_is_present() {
		String[] columns = {"col"};
		when(configuration.query()).thenReturn(Optional.of(columns));

		subject.apply(dataset);

		verify(dataset).selectExpr(columns);
	}
}