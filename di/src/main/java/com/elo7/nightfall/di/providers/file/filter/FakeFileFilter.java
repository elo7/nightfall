package com.elo7.nightfall.di.providers.file.filter;

import com.elo7.nightfall.di.Component;
import com.elo7.nightfall.di.providers.file.FileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FakeFileFilter implements FileFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FakeFileFilter.class);

	@Override
	public String filterSource(String source) {
		LOGGER.warn("No File filter configured, returning source!");
		return source;
	}
}
