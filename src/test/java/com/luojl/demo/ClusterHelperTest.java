package com.luojl.demo;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ClusterHelperTest {

    @Test
    public void testDemoMock() {
        IConnection connMock = mock(IConnection.class);
        ClusterHelper helper = new ClusterHelper(connMock);
        when(connMock.query("vm1", "CPU")).thenReturn(0.3);
        when(connMock.query("vm2", "CPU")).thenReturn(0.7);
        List<String> vmIds = Arrays.asList("vm1", "vm2");
        Assertions.assertEquals(0.3, helper.getSingleUsage("vm1", "CPU"));
        Assertions.assertEquals(0.5, helper.getAverageUsage(vmIds, "CPU"));
        Assertions.assertEquals(0.7, helper.getMaxUsage(vmIds, "CPU"));
    }

    @Test
    public void testDemoArgumentMatcher() {
        IConnection connMock = mock(IConnection.class);
        ClusterHelper helper = new ClusterHelper(connMock);
        when(connMock.query(anyString(), eq("CPU"))).thenReturn(0.1);
        when(connMock.query(anyString(), eq("Memory"))).thenReturn(0.3);
        Assertions.assertEquals(0.1, helper.getSingleUsage("vmAny", "CPU"));
        Assertions.assertEquals(0.3, helper.getSingleUsage("vmAny", "Memory"));
    }

    @Test
    public void testDemoThrowException() {
        IConnection connMock = mock(IConnection.class);
        ClusterHelper helper = new ClusterHelper(connMock);
        when(connMock.query(any(), any())).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> helper.getSingleUsage("vmId", "category"));
    }

    @Test
    public void testDemoSpy() {
        IConnection connMock = mock(IConnection.class);
        ClusterHelper helper = new ClusterHelper(connMock);
        ClusterHelper helperSpy = spy(helper);
        when(connMock.query("vm1", "CPU")).thenReturn(0.4);
        when(connMock.query("vm2", "CPU")).thenReturn(0.3);

        List<String> vmIds = Arrays.asList("vm1", "vm2");
        Assertions.assertEquals(0.35, helperSpy.getAverageUsage(vmIds, "CPU"));
        Assertions.assertEquals(0.4, helperSpy.getMaxUsage(vmIds, "CPU"));

        when(helperSpy.getMaxUsage(vmIds, "CPU")).thenReturn(1.0);
        Assertions.assertEquals(1.0, helperSpy.getMaxUsage(vmIds, "CPU"));
    }

    @Test
    public void testDemoVerify() {
        IConnection connMock = mock(IConnection.class);
        ClusterHelper helper = new ClusterHelper(connMock);
        when(connMock.query(any(), any())).thenReturn(0.5);
        List<String> vmIds = Arrays.asList("vm1", "vm2", "vm3");
        verify(connMock, never()).query(any(), any());
        helper.getAverageUsage(vmIds, "CPU");
        verify(connMock, times(3)).query(any(), any());
        verify(connMock, times(1)).query("vm1", "CPU");
        verify(connMock, times(1)).query("vm2", "CPU");
        verify(connMock, times(1)).query("vm3", "CPU");
    }
}
