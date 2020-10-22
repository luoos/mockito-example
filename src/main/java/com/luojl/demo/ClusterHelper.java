package com.luojl.demo;

import java.util.List;

class ClusterHelper {
    IConnection conn;

    ClusterHelper() {}

    ClusterHelper(IConnection conn) {
        this.conn = conn;
    }

    double getSingleUsage(String machineId, String category) {
        return this.conn.query(machineId, category);
    }

    double getAverageUsage(List<String> machineIds, String category) {
        double averageUsage = machineIds.stream()
                    .map(id -> this.conn.query(id, category))
                    .mapToDouble(d -> d)
                    .average()
                    .getAsDouble();
        return averageUsage;
    }

    double getMaxUsage(List<String> machineIds, String category) {
        double maxUsage = machineIds.stream()
                .map(id -> this.conn.query(id, category))
                .max((i, j) -> i.compareTo(j))
                .get();
        return maxUsage;
    }

    int getInt() {
        return 1;
    }
}
