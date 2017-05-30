package com.elo7.nightfall.di.commons.datapoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataPointValidatorTest {

	private static final String TYPE = "TYPE";

	@Mock
	private DataPoint<Object> mockedDataPoint;

	@Before
	public void setup() {
		when(mockedDataPoint.isType(TYPE)).thenReturn(true);
	}

	@Test
	public void shouldReturnFalseWhenDataPointIsNull() {
		assertFalse(DataPointValidator.isValid(null));
	}

	@Test
	public void shouldReturnFalseWhenDataPointAndTypeAreNotNullAndPayloadIsNull() {
		when(mockedDataPoint.getPayload()).thenReturn(null);
		when(mockedDataPoint.getType()).thenReturn(TYPE);
		assertFalse(DataPointValidator.isValid(mockedDataPoint));
	}

	@Test
	public void shouldReturnFalseWhenDataPointIsNotNullButTypeIsBlank() {
		when(mockedDataPoint.getType()).thenReturn(" ");
		assertFalse(DataPointValidator.isValid(mockedDataPoint));
	}

	@Test
	public void shouldReturnTrueWhenDataPointAndPayloadAreNotNullAndTypeIsNotBlank() {
		when(mockedDataPoint.getPayload()).thenReturn("payload");
		when(mockedDataPoint.getType()).thenReturn(TYPE);
		assertTrue(DataPointValidator.isValid(mockedDataPoint));
	}

	@Test
	public void shouldReturnFalseWhenDataPointIsNotOfSpecifiedType() {
		when(mockedDataPoint.getPayload()).thenReturn("payload");
		when(mockedDataPoint.getType()).thenReturn(TYPE);
		assertFalse(DataPointValidator.isValidForType(mockedDataPoint, "other_type"));
	}

	@Test
	public void shouldReturnTrueWhenDataPointIsOfSpecifiedType() {
		when(mockedDataPoint.getPayload()).thenReturn("payload");
		when(mockedDataPoint.getType()).thenReturn(TYPE);
		assertTrue(DataPointValidator.isValidForType(mockedDataPoint, TYPE));
	}

	@Test
	public void shouldReturnFalseWhenDataPointIsNotAnyOfSpecifiedTypes() {
		when(mockedDataPoint.getPayload()).thenReturn("payload");
		when(mockedDataPoint.getType()).thenReturn(TYPE);
		assertFalse(DataPointValidator.isValidForAnyType(mockedDataPoint, "other_type", "third"));
	}

	@Test
	public void shouldReturnTrueWhenDataPointIsAnyOfSpecifiedTypes() {
		when(mockedDataPoint.getPayload()).thenReturn("payload");
		when(mockedDataPoint.getType()).thenReturn(TYPE);
		assertTrue(DataPointValidator.isValidForAnyType(mockedDataPoint, "other_type", TYPE));
	}
}
