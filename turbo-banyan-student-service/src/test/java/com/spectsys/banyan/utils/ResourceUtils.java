package com.spectsys.banyan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceUtils {

    public static String getFileAsStringFromResource(
        final String resourcePath
    ) {
        final StringBuilder resultStringBuilder = new StringBuilder();
        try (
            final InputStream inputStream = ResourceUtils.class.getResourceAsStream(resourcePath);
            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append('\n');
            }
        } catch (final IOException e) {
            throw new RuntimeException("Unexpected exception: ", e);
        }
        return resultStringBuilder.toString();
    }
}
