package com.sereneoasis.listeners;


import io.netty.channel.*;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketListener {

    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
            return null;
        });
    }

    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                //Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "PACKET READ: " + ChatColor.RED + packet.toString());

//                if (packet instanceof ServerboundPlayerInputPacket moveInputPacket){
//                    ServerPlayer nmsPlayer = ((CraftPlayer)player).getHandle();
//
////                    Player spigotPlayer = player.getPlayer();
////                    if (spigotPlayer == null) {
////                        return;
////                    }
//
//
//                    float sidewards = moveInputPacket.getXxa(); // Sidewards
//                    float forewards = moveInputPacket.getZza(); //forewards
//
//
//                    if (VehicleRepresentation.hasVehicle(player)) {
//                        VehicleRepresentation representation = VehicleRepresentation.getPlayerVehicle(player);
//                        Bukkit.getScheduler().runTask(Vehicles.plugin, () -> {
//
//                            // Check for acceleration based on forward input
//                            if (forewards > 0) {
//                                representation.accelerate(); // Accelerate the vehicle
//                            } else if (forewards < 0) {
//                                representation.brake(); // Apply brake when moving backwards
//                            }
//
//                            // Check for turning based on sideways input
//                            if (sidewards > 0) {
//                                representation.turnRight(); // Turn right
//                            } else if (sidewards < 0) {
//                                representation.turnLeft(); // Turn left
//                            }
//
//                            VehicleRepresentation.moveVehicle(player);
//                        });
//                    }
//
//                }

                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
//                if (packet instanceof ClientboundMoveEntityPacket clientboundMoveEntityPacket) {
//                    if (VehicleRepresentation.hasVehicle(player)){
//                        if (clientboundMoveEntityPacket instanceof  ClientboundMoveEntityPacketFixed clientboundMoveEntityPacketFixed)
//                        {
//                            if (clientboundMoveEntityPacketFixed.getEntityId() == VehicleRepresentation.getEntityId(player)) {
//
//                            } else {
//                                return;
//                            }
//                        } else {
//                            return;
//                        }
//                    }
//                }
                super.write(channelHandlerContext, packet, channelPromise);

            }



        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);

    }
}