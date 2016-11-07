package com.elo7.nightfall.di.providers.file;

import com.elo7.nightfall.di.commons.datapoint.DataPoint;
import com.elo7.nightfall.di.commons.json.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import javax.inject.Singleton;
import java.lang.reflect.Type;

/**
 * File provider. Creates a {@link JavaRDD} from a Any Source.
 */
class FileRDDProvider implements Provider<JavaRDD<DataPoint<String>>> {

    private final JavaSparkContext context;
    private final FileConfiguration configuration;
    private final FileFilter fileFilter;
    private static final Type TYPE = new TypeToken<DataPoint<String>>() {}.getType();

    @Inject
    FileRDDProvider(JavaSparkContext context, FileConfiguration configuration, FileFilter fileFilter) {
        this.context = context;
        this.configuration = configuration;
        this.fileFilter = fileFilter;
    }

    @Override
    @Provides
    @Singleton
    public JavaRDD<DataPoint<String>> get() {

        configuration.getAWSKeys().ifPresent(keys -> {
            context.hadoopConfiguration().set("fs.s3.awsAccessKeyId", keys.getAccessId());
            context.hadoopConfiguration().set("fs.s3.awsSecretAccessKey", keys.getSecret());
        });

        String path = fileFilter.filterSource(configuration.getSource());

        return context.textFile(path)
                .map(item -> JsonParser.fromJson(item, TYPE));
    }
}
