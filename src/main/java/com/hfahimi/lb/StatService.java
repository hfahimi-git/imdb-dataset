package com.hfahimi.lb;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatService {
    private static final Map<String, Long> urlCounters = new ConcurrentHashMap<>();

    public void hit(String requestURI) {
        long newCount = 1;
        if (urlCounters.containsKey(requestURI)) {
            newCount += urlCounters.get(requestURI);
        }
        urlCounters.put(requestURI, newCount);
    }

    public Map<String, Long> get() {
        return Collections.unmodifiableMap(urlCounters);
    }

}
