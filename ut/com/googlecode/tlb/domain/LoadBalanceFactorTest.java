package com.googlecode.tlb.domain;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import com.googlecode.tlb.domain.NullRange;
import com.googlecode.tlb.domain.Range;

public class LoadBalanceFactorTest {
    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanBeDevidedEvenly() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 3);
        assertThat(balanceFactor.getRangeOfResources(30), is(new Range(0, 10)));
        balanceFactor = new LoadBalanceFactor(2, 3);
        assertThat(balanceFactor.getRangeOfResources(30), is(new Range(10, 10)));
        balanceFactor = new LoadBalanceFactor(3, 3);
        assertThat(balanceFactor.getRangeOfResources(30), is(new Range(20, 10)));
    }

    @Test
    public void shouldPerformBalanceWhenJobsAreMoreThanTests() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 5);
        assertThat(balanceFactor.getRangeOfResources(3), is(new Range(0, 1)));
        balanceFactor = new LoadBalanceFactor(2, 5);
        assertThat(balanceFactor.getRangeOfResources(3), is(new Range(1, 1)));
        balanceFactor = new LoadBalanceFactor(3, 5);
        assertThat(balanceFactor.getRangeOfResources(3), is(new Range(2, 1)));
        balanceFactor = new LoadBalanceFactor(4, 5);
        assertThat(balanceFactor.getRangeOfResources(3), is(instanceOf(NullRange.class)));
        balanceFactor = new LoadBalanceFactor(5, 5);
        assertThat(balanceFactor.getRangeOfResources(3), is(instanceOf(NullRange.class)));
    }

    @Test
    public void shouldPerformBalanceWhenJobsAreEqualsToTests() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 5);
        assertThat(balanceFactor.getRangeOfResources(5), is(new Range(0, 1)));
        balanceFactor = new LoadBalanceFactor(2, 5);
        assertThat(balanceFactor.getRangeOfResources(5), is(new Range(1, 1)));
        balanceFactor = new LoadBalanceFactor(3, 5);
        assertThat(balanceFactor.getRangeOfResources(5), is(new Range(2, 1)));
        balanceFactor = new LoadBalanceFactor(4, 5);
        assertThat(balanceFactor.getRangeOfResources(5), is(new Range(3, 1)));
        balanceFactor = new LoadBalanceFactor(5, 5);
        assertThat(balanceFactor.getRangeOfResources(5), is(new Range(4, 1)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 3);
        assertThat(balanceFactor.getRangeOfResources(20), is(new Range(0, 7)));
        balanceFactor = new LoadBalanceFactor(2, 3);
        assertThat(balanceFactor.getRangeOfResources(20), is(new Range(7, 7)));
        balanceFactor = new LoadBalanceFactor(3, 3);
        assertThat(balanceFactor.getRangeOfResources(20), is(new Range(14, 6)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly2() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 4);
        assertThat(balanceFactor.getRangeOfResources(18), is(new Range(0, 5)));
        balanceFactor = new LoadBalanceFactor(2, 4);
        assertThat(balanceFactor.getRangeOfResources(18), is(new Range(5, 5)));
        balanceFactor = new LoadBalanceFactor(3, 4);
        assertThat(balanceFactor.getRangeOfResources(18), is(new Range(10, 4)));
        balanceFactor = new LoadBalanceFactor(4, 4);
        assertThat(balanceFactor.getRangeOfResources(18), is(new Range(14, 4)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly3() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 4);
        assertThat(balanceFactor.getRangeOfResources(11), is(new Range(0, 3)));
        balanceFactor = new LoadBalanceFactor(2, 4);
        assertThat(balanceFactor.getRangeOfResources(11), is(new Range(3, 3)));
        balanceFactor = new LoadBalanceFactor(3, 4);
        assertThat(balanceFactor.getRangeOfResources(11), is(new Range(6, 3)));
        balanceFactor = new LoadBalanceFactor(4, 4);
        assertThat(balanceFactor.getRangeOfResources(11), is(new Range(9, 2)));
    }


    @Test(expected = Exception.class)
    public void shouldThrowExceptionAtZeroJobs() {
        final LoadBalanceFactor balanceFactor = new LoadBalanceFactor(2, 0);
        balanceFactor.getRangeOfResources(20);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionAtZeroJobIndex() {
        final LoadBalanceFactor balanceFactor = new LoadBalanceFactor(0, 2);
        balanceFactor.getRangeOfResources(20);
    }
}



