package com.primetoxinz.stacksonstacks;

import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by tyler on 5/28/16.
 */
class ItemIngotPlacer extends ItemMultiPart {
    private Ingot ingot;
    ItemIngotPlacer(Ingot ingot) {
        this.ingot = ingot;
    }

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        Vec3d vec = getPositionFromHit(hit,pos);
//        System.out.println(vec.toString());
        return new PartIngot(ingot,vec);
    }

    private Vec3d getPositionFromHit(Vec3d hit, BlockPos pos) {
        if(Math.abs(pos.getY()) > Math.abs(hit.yCoord))
            pos=pos.down(2);
        int x1 = Math.abs(pos.getX());
        int y1 = Math.abs(pos.getY());
        int z1 = Math.abs(pos.getZ());
        System.out.println("y:"+hit.yCoord+","+y1);
        double x2 = Math.abs(hit.xCoord)-x1;
        double y2 = Math.abs(hit.yCoord)-y1;
        double z2 = Math.abs(hit.zCoord)-z1;

        x2 = Math.ceil(x2*4d);
        y2 = Math.ceil(y2*8d);
        z2 = Math.ceil(z2*2d);

        return new Vec3d(x2,y2,z2);
    }

}
