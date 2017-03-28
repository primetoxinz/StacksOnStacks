package com.tierzero.stacksonstacks.pile;

import net.minecraft.util.EnumFacing;

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
    }

    private final EnumFacing.Axis axis;
    private EnumGrid gridX, gridY, gridZ;
    private final double x, y, z;

    public static RelativeBlockPos[] positions = new RelativeBlockPos[64];

    static {
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new RelativeBlockPos(i);
        }
    }

    public static RelativeBlockPos fromSlot(int slotIndex) {
        if (positions[slotIndex] != null)
            return positions[slotIndex];
        return new RelativeBlockPos(slotIndex);
    }

    public RelativeBlockPos(double x, double y, double z, EnumFacing.Axis axis) {
        this.axis = axis;
        findGrid();
        this.x = gridX.round(x);
        this.y = gridY.round(y);
        this.z = gridZ.round(z);
    }

    private RelativeBlockPos(int slotIndex) {
        this.axis = EnumFacing.Axis.X;
        findGrid();
        double xLayer = ((int) (slotIndex / gridZ.divisor)) % gridX.divisor / gridX.divisor;
        double zLayer = slotIndex % gridZ.divisor / gridZ.divisor;
        this.y = ((int) (slotIndex / gridY.divisor)) % gridY.divisor / gridY.divisor;
        this.x = xLayer;
        this.z = zLayer;
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

    /**
     * Converts the position into a integer in the range (0, 64) representing the item slot
     *
     * @return
     */
    public int toSlotIndex() {
        for (int i = 0; i < positions.length; i++) {
            if (positions[i].equals(this))
                return i;
        }
        return -1;
    }


}
