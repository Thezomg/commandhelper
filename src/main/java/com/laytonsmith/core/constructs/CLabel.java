/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.core.constructs;

import com.laytonsmith.core.constructs.Construct.ConstructType;

/**
 *
 * @author Layton
 */
public class CLabel extends Construct{
    Construct label;
    public CLabel(Construct value){
        super(value.val(), ConstructType.LABEL, value.target); 
        label = value;
    }
    
    public Construct cVal(){
        return label;
    }
}
