/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.core;

/**
 *
 * @author Layton
 */
public interface MethodScriptComplete {
    /**
     * This function is called when the MethodScript has finished. Any output generated
     * by the script is sent here. If the script generated an error, null is sent.
     * If the script ran successfully, but did not return any output, an empty string
     * is sent.
     */
    public void done(String output);
}
