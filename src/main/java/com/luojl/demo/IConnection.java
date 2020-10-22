package com.luojl.demo;

interface IConnection {
    // could be database connection or network connection

    double query(String machineId, String category);
}