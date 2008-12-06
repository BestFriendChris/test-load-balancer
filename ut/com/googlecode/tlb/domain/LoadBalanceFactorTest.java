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
        assertThat(new LoadBalanceFactor(30, 3, 1).getRangeOfResources(), is(new Range(0, 10)));
        assertThat(new LoadBalanceFactor(30, 3, 2).getRangeOfResources(), is(new Range(10, 10)));
        assertThat(new LoadBalanceFactor(30, 3, 3).getRangeOfResources(), is(new Range(20, 10)));
    }

    @Test
    public void shouldPerformBalanceWhenJobsAreMoreThanTests() {
        assertThat(new LoadBalanceFactor(3, 5, 1).getRangeOfResources(), is(new Range(0, 1)));
        assertThat(new LoadBalanceFactor(3, 5, 2).getRangeOfResources(), is(new Range(1, 1)));
        assertThat(new LoadBalanceFactor(3, 5, 3).getRangeOfResources(), is(new Range(2, 1)));
        assertThat(new LoadBalanceFactor(3, 5, 4).getRangeOfResources(), is(instanceOf(NullRange.class)));
        assertThat(new LoadBalanceFactor(3, 5, 5).getRangeOfResources(), is(instanceOf(NullRange.class)));
    }

    @Test
    public void shouldPerformBalanceWhenJobsAreEqualsToTests() {
        assertThat(new LoadBalanceFactor(5, 5, 1).getRangeOfResources(), is(new Range(0, 1)));
        assertThat(new LoadBalanceFactor(5, 5, 2).getRangeOfResources(), is(new Range(1, 1)));
        assertThat(new LoadBalanceFactor(5, 5, 3).getRangeOfResources(), is(new Range(2, 1)));
        assertThat(new LoadBalanceFactor(5, 5, 4).getRangeOfResources(), is(new Range(3, 1)));
        assertThat(new LoadBalanceFactor(5, 5, 5).getRangeOfResources(), is(new Range(4, 1)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly() {
        assertThat(new LoadBalanceFactor(20, 3, 1).getRangeOfResources(), is(new Range(0, 7)));
        assertThat(new LoadBalanceFactor(20, 3, 2).getRangeOfResources(), is(new Range(7, 7)));
        assertThat(new LoadBalanceFactor(20, 3, 3).getRangeOfResources(), is(new Range(14, 6)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly2() {
        assertThat(new LoadBalanceFactor(18, 4, 1).getRangeOfResources(), is(new Range(0, 5)));
        assertThat(new LoadBalanceFactor(18, 4, 2).getRangeOfResources(), is(new Range(5, 5)));
        assertThat(new LoadBalanceFactor(18, 4, 3).getRangeOfResources(), is(new Range(10, 4)));
        assertThat(new LoadBalanceFactor(18, 4, 4).getRangeOfResources(), is(new Range(14, 4)));
    }

    @Test
    public void shouldReturnFilteredResourceNumberIfResourcesCanNotBeDevidedEvenly3() {
        assertThat(new LoadBalanceFactor(11, 4, 1).getRangeOfResources(), is(new Range(0, 3)));
        assertThat(new LoadBalanceFactor(11, 4, 2).getRangeOfResources(), is(new Range(3, 3)));
        assertThat(new LoadBalanceFactor(11, 4, 3).getRangeOfResources(), is(new Range(6, 3)));
        assertThat(new LoadBalanceFactor(11, 4, 4).getRangeOfResources(), is(new Range(9, 2)));
    }

    @Test
    public void shouldNotDoLoadBalanceWhenOnly1Piece() {
        assertThat(new LoadBalanceFactor(11, 1, -1).getRangeOfResources(), is(new Range(0, 11)));
        assertThat(new LoadBalanceFactor(11, 1, 0).getRangeOfResources(), is(new Range(0, 11)));
        assertThat(new LoadBalanceFactor(11, 1, 1).getRangeOfResources(), is(new Range(0, 11)));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionAtZeroJobs() {
        new LoadBalanceFactor(20, 0, 2).getRangeOfResources();
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionAtZeroJobIndex() {
        new LoadBalanceFactor(20, 2, 0).getRangeOfResources();
    }
}



