package com.github.wmarkow.distiller.domain.model;

import java.util.Objects;

public class BleScanResult {
    private String address;

    public BleScanResult(String address)
    {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BleScanResult that = (BleScanResult) o;
        return Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
