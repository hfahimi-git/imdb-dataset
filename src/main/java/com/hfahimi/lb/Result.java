package com.hfahimi.lb;

import java.util.Map;

public record Result(Map<String, String> result, int first, int previous, int current, int next, int lastPage) {
}
