/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.abstraction;

import com.laytonsmith.abstraction.blocks.MCBlock;

/**
 *
 * @author layton
 */
public interface MCLocation {    
    public double getX();
    public double getY();
    public double getZ();
    public MCWorld getWorld();
    public float getYaw();
    public float getPitch();
    public int getBlockX();
    public int getBlockY();
    public int getBlockZ();
    public MCBlock getBlock();
    
    public void setPitch(float p);
    public void setYaw(float y);
    
    public MCLocation clone();
}
