package org.rrd4j.data;

import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.Util;

import java.io.IOException;

class Def extends Source {
    private final String path, dsName, backend;
    private final ConsolFun consolFun;
    private FetchData fetchData;

    Def(String name, FetchData fetchData) {
        this(name, name, fetchData);
    }

    Def(String name, String dsName, FetchData fetchData) {
        this(name,
                fetchData.getRequest().getParentDb().getPath(),
                dsName, fetchData.getRequest().getConsolFun(),
                fetchData.getRequest().getParentDb().getRrdBackend().getFactory().getName()
                );
        this.fetchData = fetchData;
    }

    Def(String name, String path, String dsName, ConsolFun consolFunc) {
        this(name, path, dsName, consolFunc, null);
    }

    Def(String name, String path, String dsName, ConsolFun consolFunc, String backend) {
        super(name);
        this.path = path;
        this.dsName = dsName;
        this.consolFun = consolFunc;
        this.backend = backend;
    }

    String getPath() {
        return path;
    }

    String getCanonicalPath() throws IOException {
        return Util.getCanonicalPath(path);
    }

    String getDsName() {
        return dsName;
    }

    ConsolFun getConsolFun() {
        return consolFun;
    }

    String getBackend() {
        return backend;
    }

    boolean isCompatibleWith(Def def) throws IOException {
        return getCanonicalPath().equals(def.getCanonicalPath()) &&
                getConsolFun() == def.consolFun &&
                ((backend == null && def.backend == null) ||
                        (backend != null && def.backend != null && backend.equals(def.backend)));
    }

    void setFetchData(FetchData fetchData) {
        this.fetchData = fetchData;
    }

    long[] getRrdTimestamps() {
        return fetchData.getTimestamps();
    }

    double[] getRrdValues() {
        return fetchData.getValues(dsName);
    }

    long getArchiveEndTime() {
        return fetchData.getArcEndTime();
    }

    long getFetchStep() {
        return fetchData.getStep();
    }

    Aggregates getAggregates(long tStart, long tEnd) {
        long[] t = getRrdTimestamps();
        double[] v = getRrdValues();
        return new Aggregator(t, v).getAggregates(tStart, tEnd);
    }

    double getPercentile(long tStart, long tEnd, double percentile) {
        long[] t = getRrdTimestamps();
        double[] v = getRrdValues();
        return new Aggregator(t, v).getPercentile(tStart, tEnd, percentile);
    }

    boolean isLoaded() {
        return fetchData != null;
    }
}
