package com.primetoxinz.stacksonstacks.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * Created by tyler on 7/20/16.
 */
public class IngotCount implements IIngotCount {
	
    private int count;
    private int max;
    
    public IngotCount(int max) {
        this.max = max;
    }
    
    @Override
    public void addIngot() {
        if(count < max) {
            count++;
        }
    }

    @Override
    public void removeIngot() {
        if(count > 0) {
            count--;
        }         
    }
    
    @Override
    public int spaceRemaining() {
    	return getMax() - getCount();
    }

    @Override
    public boolean isFull() {
        return count == max;
    }
    
	@Override
	public boolean isEmpty() {
		return count == 0;
	}

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public int getMax() {
        return max;
    }

	@Override
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public void setMax(int max) {
		this.max = max;
	}



}
