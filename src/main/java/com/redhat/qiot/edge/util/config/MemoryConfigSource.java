package com.redhat.qiot.edge.util.config;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.spi.ConfigSource;

public class MemoryConfigSource implements ConfigSource {
    
        public static final String NAME = "MemoryConfigSource";
        private static final Map<String,String> PROPERTIES = new HashMap<>();

        @Override
        public int getOrdinal() {
            return 900;
        }

        @Override
        public Map<String, String> getProperties() {
            return PROPERTIES;
        }

        @Override
        public String getValue(String key) {
            if(PROPERTIES.containsKey(key)){
                return PROPERTIES.get(key);
            }
            return null;
        }

        @Override
        public String getName() {
            return NAME;
        }
    }
