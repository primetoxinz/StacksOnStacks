package com.primetoxinz.stacksonstacks.capability;

/**
 * Created by tyler on 7/20/16.
 */
public interface IIngotCount {
    public void addIngot();
    public void removeIngot();
    public int spaceRemaining();
    public boolean isFull();
    public boolean isEmpty();
    public int getCount();
    public int getMax();
    public void setCount(int count);
    public void setMax(int max);
}

