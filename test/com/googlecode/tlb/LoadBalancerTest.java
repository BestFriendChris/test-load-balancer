package com.googlecode.tlb;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.hamcrest.core.Is;

/**
 *
 */
public class LoadBalancerTest {
    @Test
    public void shouldReturn2AsJobIndex() {
        LoadBalanceFactor factor = new LoadBalancer("[linux-job-1, linux-job-2]", "linux-job-2").balance();
        assertThat(factor, Is.is(new LoadBalanceFactor("2", "2")));
    }

    @Test
    public void shouldReturn1AsJobIndex() {
        LoadBalanceFactor factor = new LoadBalancer("[linux-job-1, linux-job-2]", "linux-job-1").balance();
        assertThat(factor, Is.is(new LoadBalanceFactor("2", "2")));
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionIfTheJobNotExistInTheGroup() {
        new LoadBalancer("[linux-job-1, linux-job-2]", "linux-job-x").balance();
    }


    @Test(expected = Exception.class)
    public void shouldThrowExceptionIfTheJobIsNotExistInTheGroup() {
        new LoadBalancer("[linux-job-1, linux-job-2]", "linux-job-x").balance();
    }
}
