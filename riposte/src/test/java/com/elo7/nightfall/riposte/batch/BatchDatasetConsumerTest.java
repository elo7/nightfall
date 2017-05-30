package com.elo7.nightfall.riposte.batch;

import com.elo7.nightfall.riposte.task.RiposteConfiguration;
import org.apache.spark.sql.DataFrameWriter;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BatchDatasetConsumerTest {

	@InjectMocks
	private BatchDatasetConsumer subject;
	@Mock
	private RiposteConfiguration configuration;
	@Mock
	private Dataset<Row> dataset;
	@Mock
	private DataFrameWriter<Row> writer;

	@Before
	public void setup() {
		when(configuration.writerFormat()).thenReturn("writer");
		when(dataset.write()).thenReturn(writer);
		when(writer.format(anyString())).thenReturn(writer);
		when(writer.options(anyMap())).thenReturn(writer);
	}

	@Test
	public void should_use_dataset_show_when_writer_format_is_console() {
		when(configuration.writerFormat()).thenReturn("console");

		subject.consume(dataset);

		verify(dataset).show();
	}

	@Test
	public void should_configure_writer_format() {
		when(configuration.writerFormat()).thenReturn("writer");

		subject.consume(dataset);

		verify(writer).options(anyMap());
	}

	@Test
	public void should_configure_writer_options() {
		when(configuration.writerOptions()).thenReturn(new HashMap<>());

		subject.consume(dataset);

		verify(writer).options(anyMap());
	}

	@Test
	public void should_save_with_path_when_path_is_present() {
		when(configuration.writerPath()).thenReturn(Optional.of("path"));

		subject.consume(dataset);

		verify(writer).save("path");
	}

	@Test
	public void should_save_without_path_when_path_is_not_present() {
		when(configuration.writerPath()).thenReturn(Optional.empty());

		subject.consume(dataset);

		verify(writer).save();
	}
}