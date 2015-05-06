package com.amik;

import com.griddynamics.jagger.engine.e1.Provider;
import com.griddynamics.jagger.engine.e1.collector.AvgMetricAggregatorProvider;
import com.griddynamics.jagger.engine.e1.collector.MetricDescription;
import com.griddynamics.jagger.engine.e1.collector.test.TestInfo;
import com.griddynamics.jagger.engine.e1.collector.test.TestListener;
import com.griddynamics.jagger.engine.e1.services.ServicesAware;import java.lang.Override;import java.lang.String;

public class QpsTestListener extends ServicesAware implements Provider<TestListener> {

    private String metricId = "QPS_trololo";

    /** Method is executed single time when listener is created */
    @Override
    protected void init() {
        getMetricService().createMetric(new MetricDescription(metricId)
                .plotData(true)
                .showSummary(true)
                .addAggregator(new AvgMetricAggregatorProvider()));
    }

    /** Method is providing listener to Jagger that will trigger listener methods during test run*/
    @Override
    public TestListener provide() {
        return new TestListener() {

            private int boo = -1;
            @Override
            public void onStart(TestInfo testInfo) {}

            @Override
            public void onStop(TestInfo testInfo) {}

            @Override
            public void onRun(TestInfo status) {

                if (boo < 0) {
                    boo = status.getStartedSamples();
                    getMetricService().saveValue(metricId, status.getStartedSamples());
                } else {
                    int tt = status.getStartedSamples() - boo;
                    boo += tt;
                    getMetricService().saveValue(metricId, tt);
                }

            }
        };
    }
}
