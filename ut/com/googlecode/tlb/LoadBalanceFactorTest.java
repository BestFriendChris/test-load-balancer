package com.googlecode.tlb;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class LoadBalanceFactorTest {
    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanBeDevidedEvenly() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 3);
        assertThat(balanceFactor.amountOfTests(30), is(new Range(0, 10)));
        balanceFactor = new LoadBalanceFactor(2, 3);
        assertThat(balanceFactor.amountOfTests(30), is(new Range(10, 10)));
        balanceFactor = new LoadBalanceFactor(3, 3);
        assertThat(balanceFactor.amountOfTests(30), is(new Range(20, 10)));
    }

    @Test
    public void shouldPerformBalanceWhenJobsAreMoreThanTests() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 5);
        assertThat(balanceFactor.amountOfTests(3), is(new Range(0, 1)));
        balanceFactor = new LoadBalanceFactor(2, 5);
        assertThat(balanceFactor.amountOfTests(3), is(new Range(1, 1)));
        balanceFactor = new LoadBalanceFactor(3, 5);
        assertThat(balanceFactor.amountOfTests(3), is(new Range(2, 1)));
        balanceFactor = new LoadBalanceFactor(4, 5);
        assertThat(balanceFactor.amountOfTests(3), is(instanceOf(NullRange.class)));
        balanceFactor = new LoadBalanceFactor(5, 5);
        assertThat(balanceFactor.amountOfTests(3), is(instanceOf(NullRange.class)));
    }

    @Test
    public void shouldPerformBalanceWhenJobsAreEqualsToTests() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 5);
        assertThat(balanceFactor.amountOfTests(5), is(new Range(0, 1)));
        balanceFactor = new LoadBalanceFactor(2, 5);
        assertThat(balanceFactor.amountOfTests(5), is(new Range(1, 1)));
        balanceFactor = new LoadBalanceFactor(3, 5);
        assertThat(balanceFactor.amountOfTests(5), is(new Range(2, 1)));
        balanceFactor = new LoadBalanceFactor(4, 5);
        assertThat(balanceFactor.amountOfTests(5), is(new Range(3, 1)));
        balanceFactor = new LoadBalanceFactor(5, 5);
        assertThat(balanceFactor.amountOfTests(5), is(new Range(4, 1)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly() {
        LoadBalanceFactor balanceFactor = new LoadBalanceFactor(1, 3);
        assertThat(balanceFactor.amountOfTests(20), is(new Range(0, 6)));
        balanceFactor = new LoadBalanceFactor(2, 3);
        assertThat(balanceFactor.amountOfTests(20), is(new Range(6, 6)));
        balanceFactor = new LoadBalanceFactor(3, 3);
        assertThat(balanceFactor.amountOfTests(20), is(new Range(12, 8)));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionAtZeroJobs() {
        final LoadBalanceFactor balanceFactor = new LoadBalanceFactor(2, 0);
        balanceFactor.amountOfTests(20);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionAtZeroJobIndex() {
        final LoadBalanceFactor balanceFactor = new LoadBalanceFactor(0, 2);
        balanceFactor.amountOfTests(20);
    }
}



