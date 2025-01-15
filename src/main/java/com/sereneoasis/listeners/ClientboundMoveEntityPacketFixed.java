package com.sereneoasis.listeners;

import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;

public class ClientboundMoveEntityPacketFixed extends ClientboundMoveEntityPacket.PosRot {
    public ClientboundMoveEntityPacketFixed(int entityId, short deltaX, short deltaY, short deltaZ, byte yaw, byte pitch, boolean onGround) {
        super(entityId, deltaX, deltaY, deltaZ, yaw, pitch, onGround);
    }

    public int getEntityId(){
        return this.entityId;
    }
}
