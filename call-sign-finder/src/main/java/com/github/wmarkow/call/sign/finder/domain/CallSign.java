package com.github.wmarkow.call.sign.finder.domain;

public class CallSign
{
    private String prefix;
    private String sign;
    private int morseDitLength = 0;

    public CallSign( String prefix, String sign )
    {
        this.prefix = prefix;
        this.sign = sign;
    }

    public String getFullSign()
    {
        return prefix + sign;
    }

    public int getMorseDitLength()
    {
        return morseDitLength;
    }

    public void setMorseDitLength( int morseDitLength )
    {
        this.morseDitLength = morseDitLength;
    }

    @Override
    public String toString()
    {
        return "CallSign [prefix=" + prefix + ", sign=" + sign + ", morseDitLength=" + morseDitLength + "]";
    }
    
}
