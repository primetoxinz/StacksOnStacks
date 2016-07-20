package com.primetoxinz.stacksonstacks.logic;

import com.primetoxinz.stacksonstacks.SoS;
import com.primetoxinz.stacksonstacks.capability.IIngotCount;
import com.primetoxinz.stacksonstacks.capability.IngotCapabilities;
import com.primetoxinz.stacksonstacks.capability.IngotCountProvider;
import com.primetoxinz.stacksonstacks.ingot.DummyStack;
import com.primetoxinz.stacksonstacks.ingot.IngotLocation;
import com.primetoxinz.stacksonstacks.ingot.IngotType;
import com.primetoxinz.stacksonstacks.ingot.PartIngot;
import lib.utils.RenderUtils;
import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.IMultipart;
import mcmultipart.multipart.IMultipartContainer;
import mcmultipart.multipart.MultipartHelper;
import mcmultipart.multipart.MultipartRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static mcmultipart.multipart.MultipartHelper.getPartContainer;
import static net.minecraft.util.EnumActionResult.FAIL;
import static net.minecraft.util.EnumActionResult.SUCCESS;

public class IngotPlacer {
    public static ArrayList<DummyStack> ingotRegistry = new ArrayList<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    @SideOnly(Side.CLIENT)
    public final void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = event.getPlayer();
        if (canBeIngot(player.getHeldItemMainhand()) || canBeIngot(player.getHeldItemOffhand())) {
            RenderUtils.drawSelectionBox(player, event.getTarget(), 0, event.getPartialTicks());
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public final void placeIngot(PlayerInteractEvent.RightClickBlock e) {
        onItemUse(e.getItemStack(), e.getEntityPlayer(), e.getWorld(), e.getPos(), e.getHand(), e.getFace(), e.getHitVec());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void attachCapability(AttachCapabilitiesEvent.TileEntity e) {
        if (e.getTileEntity() instanceof TileMultipartContainer) {
            TileMultipartContainer container = (TileMultipartContainer) e.getTileEntity();
            if (!container.hasCapability(IngotCapabilities.CAPABILITY_INGOT, null)) {
                System.out.println("doesn't have,adding");
                e.addCapability(new ResourceLocation(SoS.MODID, "ingot_capability"), new IngotCountProvider());
            }
        }
    }
    private static EnumActionResult onItemUse(final ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, Vec3d hit) {
        if (canBeIngot(stack) && player.canPlayerEdit(pos, side, stack)) {
            place(world, pos, side, hit, stack, player);
            return SUCCESS;
        }
        return FAIL;
    }
    
    public static boolean canBeIngot(ItemStack stack) {
        if (stack != null) {
	        DummyStack dummy = new DummyStack(stack);
	        if(ingotRegistry.contains(dummy)) {
	            return true;
	        } else {
	        	String ingotName = OreDictUtil.getOreDictionaryNameStartingWith(stack, "ingot");
	        	
	        	if(ingotName != null) {
	        		ingotRegistry.add(dummy);
	        		return true;
	        	}
	        }
        }
        
        return false;
    }

    private static boolean canAddPart(World world, BlockPos pos, PartIngot ingot) {
        IMultipartContainer container = getPartContainer(world, pos);
        if (container == null) {
            List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
            for (AxisAlignedBB bb : list)
                if (!world.checkNoEntityCollision(bb.offset(pos.getX(), pos.getY(), pos.getZ()))) return false;
            Collection<? extends IMultipart> parts = MultipartRegistry.convert(world, pos, true);
            if (parts != null && !parts.isEmpty()) {
                TileMultipartContainer tmp = new TileMultipartContainer();
                for (IMultipart p : parts)
                    tmp.getPartContainer().addPart(p, false, false, false, false, UUID.randomUUID());
                return tmp.canAddPart(ingot);
            }
            return true;
        } else {
            return container.canAddPart(ingot);
        }
    }

    private static Vec3d nextHit(Vec3d hit) {
        double x = hit.xCoord;
        double y = hit.yCoord;
        double z = hit.zCoord;
        if(x>.5f) {
            z+=.25f;
            if(z > .75f) {
                y+=.125f;
                z=0;
            }
            x=0;
        } else {
            x+=.5f;
        }
        return new Vec3d(x,y,z);
    }



    private static void place(World world, BlockPos pos, EnumFacing side, Vec3d hit, ItemStack stack, EntityPlayer player) {
        IMultipartContainer container = MultipartHelper.getPartContainer(world, pos);
        BlockPos place = pos;
        boolean full = isContainerFull((TileMultipartContainer) container);
        if (container != null) {

            if(full) {
                place=pos.up();
                IMultipartContainer next = MultipartHelper.getPartContainer(world, place);
                if(next != null) {
                    place(world,place,side,hit,stack,player);
                }
            }
        } else {
            place=pos.offset(side);
        }
        if (player.isSneaking()) {
            placeAll(world, place, stack, player);
        } else if(!full){
            place(world, place, hit, stack, player);
        }
    }

    private static void placeAll(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            Vec3d hit = new Vec3d(0, 0, 0);
            while (stack.stackSize > 0 && (System.currentTimeMillis() - startTime) < 2000) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                place(world, pos, hit, stack, player);
                hit = nextHit(hit);
            }
        }).start();
    }

    private static void place(World world, BlockPos pos, Vec3d hit, ItemStack stack, EntityPlayer player) {
        IngotLocation location = IngotLocation.fromHit(hit, player.getHorizontalFacing().getAxis());
        PartIngot part = new PartIngot(location, new IngotType(stack));
        if (canAddPart(world, pos, part)) {
            if (!world.isRemote) {
                try {
                    MultipartHelper.addPart(world, pos, part);
                } catch (Throwable e) {
                }
            }
            consumeItem(player, stack);
        }
    }

    public static boolean isContainerFull(TileMultipartContainer container) {

        if (container != null && container.hasCapability(IngotCapabilities.CAPABILITY_INGOT, null)) {
            IIngotCount cap = container.getCapability(IngotCapabilities.CAPABILITY_INGOT, null);
            System.out.println(cap.isFull());
            return cap.isFull();
        }
        return false;
    }

    private static void consumeItem(EntityPlayer player, ItemStack stack) {
        if (!player.isCreative())
            stack.stackSize--;
        if (stack.stackSize <= 0 && player.getActiveHand() != null) {
            player.setHeldItem(player.getActiveHand(), null);
        }
    }


}