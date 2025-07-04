package me.turbokot.fisherbot.bot.data;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;

public class Vector3D {

    public double x;
    public double y;
    public double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (int)x;
        hash = 31 * hash + (int)y;
        hash = 31 * hash + (int)z;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3D)) {
            return false;
        }
        Vector3D co = (Vector3D) obj;
        return co.x == x && co.y == y && co.z == z;
    }

    public Position translate() {
        return new Position((int)Math.floor(x),(int)Math.floor(y),(int)Math.floor(z));
    }

    public Vector3D down() {
        return new Vector3D(x,y-1,z);
    }

    public Vector3D up() {
        return new Vector3D(x,y+1,z);
    }


    /**
     * возвратит true если блок под этой п0зицией идеально подходит для того чтобы стоять на нем
     */

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public double getPosX() {
        return this.x;
    }

    public double getPosY() {
        return this.y;
    }

    public double getPosZ() {
        return this.z;
    }

    public double getBlockX() {
        return this.x;
    }

    public double getBlockY() {
        return this.y;
    }

    public double getBlockZ() {
        return this.z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void addX(double i) {
        this.x +=i;
    }

    public void addY(double i) {
        this.y +=i;
    }

    public void addZ(double i) {
        this.z +=i;
    }
}