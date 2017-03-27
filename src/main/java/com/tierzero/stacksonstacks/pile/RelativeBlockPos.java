package com.tierzero.stacksonstacks.pile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class RelativeBlockPos {
    private enum EnumGrid {
        TWO(2),
        FOUR(4),
        EIGHT(8);
        private double divisor;

        EnumGrid(double divisor) {
            this.divisor = divisor;
        }

        public double round(double num) {
            return bound(relative((int) (num * (int) (divisor)) / divisor));
        }

        private double relative(double num) {
            double pos = ((num > 0) ? Math.floor(num) : Math.ceil(num));
            return Math.abs(num - pos);
        }

        private double bound(double num) {
            return num > 1 ? 1 : num < 0 ? 0 : num;
        }

        public boolean isMax(double num) {
            return (num * divisor) + (1d / divisor) == divisor;
        }

        public double next(double num) {
            return num + (1 / divisor);
        }
    }

    private static final String NBT_TAG_POS_X = "posX";
    private static final String NBT_TAG_POS_Y = "posY";
    private static final String NBT_TAG_POS_Z = "posZ";
    private static final String NBT_TAG_AXIS = "axis";

    private final EnumFacing.Axis axis;
    private EnumGrid gridX, gridY, gridZ;
    private final double x, y, z;

    public RelativeBlockPos(double x, double y, double z, EnumFacing.Axis axis) {
        this.axis = axis;
        findGrid();
        System.out.printf("%s,%s,%s\n", x, y, z);
        this.x = gridX.round(x);
        this.y = gridY.round(y);
        this.z = gridZ.round(z);
    }

    public void findGrid() {
        switch (axis) {
            case X:
                this.gridX = EnumGrid.TWO;
                this.gridY = EnumGrid.EIGHT;
                this.gridZ = EnumGrid.FOUR;
                break;
            case Z:
                this.gridX = EnumGrid.FOUR;
                this.gridY = EnumGrid.EIGHT;
                this.gridZ = EnumGrid.TWO;
                break;
            default:
                this.gridX = null;
                this.gridY = null;
                this.gridZ = null;
        }
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble(NBT_TAG_POS_X, x);
        tag.setDouble(NBT_TAG_POS_Y, y);
        tag.setDouble(NBT_TAG_POS_Z, z);
        if (axis != null) {
            tag.setInteger(NBT_TAG_AXIS, axis.ordinal());
        }
        return null;
    }

    public static RelativeBlockPos getFromDeserializeNBT(NBTTagCompound tag) {
        EnumFacing.Axis axis = EnumFacing.Axis.X;
        if (tag.hasKey(NBT_TAG_AXIS)) {
            axis = EnumFacing.Axis.values()[tag.getInteger(NBT_TAG_AXIS)];
        }
        return new RelativeBlockPos(tag.getDouble(NBT_TAG_POS_X), tag.getDouble(NBT_TAG_POS_Y), tag.getDouble(NBT_TAG_POS_Z), axis);
    }

    public boolean equals(RelativeBlockPos pos) {
        return (pos.x == this.x && pos.y == this.y && pos.z == this.z && pos.axis == this.axis);
    }

    @Override
    public String toString() {
        return String.format("RelativeBlockPos{%s,%s,%s : %s}", x, y, z, axis);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Nullable
    public RelativeBlockPos next() {
        double x = this.x, y = this.y, z = this.z;
        if(gridX.isMax(x)) {
            z=gridZ.next(z);
            if(gridZ.isMax(z)) {
                y=gridY.next(y);
                z=0;
            }
            x=0;
        } else {
            x=gridX.next(x);
        }
//        if (!gridX.isMax(this.x))
//            x = gridX.next(this.x);
//        else if (!gridY.isMax(this.y))
//            y = gridY.next(this.y);
//        else if (!gridZ.isMax(this.z))
//            z = gridZ.next(this.z);
        return new RelativeBlockPos(x, y, z, axis);
    }
}
