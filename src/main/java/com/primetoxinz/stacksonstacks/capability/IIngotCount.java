package com.primetoxinz.stacksonstacks.capability;

/**
 * Created by tyler on 7/20/16.
 */
public interface IIngotCount {
    public void addIngot();
    public void removeIngot();
    public boolean isFull();
    public int getIngotCount();
    public int getMaxIngotCount();

}

