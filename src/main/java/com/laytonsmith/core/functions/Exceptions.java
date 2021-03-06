/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.core.functions;

import com.laytonsmith.core.*;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.exceptions.CancelCommandException;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Layton
 */
public class Exceptions {
    public static String docs(){
        return "This class contains functions related to Exception handling in MethodScript";
    }
    public enum ExceptionType{
        /**
         * This exception is thrown if a value cannot be cast into an appropriate type. Functions that require
         * a numeric value, for instance, would throw this if the string "hi" were passed in.
         */
        CastException,
        /**
         * This exception is thrown if a value is requested from an array that is above the highest index of the array,
         * or a negative number.
         */
        IndexOverflowException,
        /**
         * This exception is thrown if a function expected a numeric value to be in a particular range, and it wasn't
         */
        RangeException,
        /**
         * This exception is thrown if a function expected the length of something to be a particular value, but it was not.
         */
        LengthException,
        /**
         * This exception is thrown if the user running the command does not have permission to run the function
         */
        InsufficientPermissionException,
        /**
         * This exception is thrown if a function expected an online player, but that player was offline, or the
         * command is being run from somewhere not in game, and the function was trying to use the current player.
         */
        PlayerOfflineException, 
        /**
         * Some var arg functions may require at least a certain number of arguments to be passed to the function
         */
        InsufficientArgumentsException, 
        /**
         * This exception is thrown if a function expected a string to be formatted in a particular way, but it could not interpret the 
         * given value.
         */
        FormatException,
        /**
         * This exception is thrown if a procedure is used without being defined, or if a procedure name does not follow proper naming
         * conventions.
         */
        InvalidProcedureException, 
        /**
         * This exception is thrown if there is a problem with an include. This is thrown if there is
         * a compile error in the included script.
         */
        IncludeException,
        /**
         * This exception is thrown if a script tries to read or write to a location of the filesystem that is not allowed.
         */
        SecurityException, 
        /**
         * This exception is thrown if a file cannot be read or written to.
         */
        IOException, 
        /**
         * This exception is thrown if a function uses an external plugin, and that plugin is not loaded, 
         * or otherwise unusable.
         */
        InvalidPluginException,
        /**
         * This exception is thrown when a plugin is loaded, but a call to the plugin failed, usually
         * for some reason specific to the plugin. Check the error message for more details about this
         * error.
         */
        PluginInternalException,
        /**
         * If a function requests a world, and the world given doesn't exist, this is thrown
         */
        InvalidWorldException,
        /**
         * This exception is thrown if an error occurs when trying to bind() an event, or if a event framework
         * related error occurs.
         */
        BindException,
        /**
         * If an enchantment is added to an item that isn't supported, this is thrown.
         */
        EnchantmentException,
        /**
         * If an untameable mob is attempted to be tamed, this exception is thrown
         */
        UntameableMobException,
    }
    @api public static class _try extends AbstractFunction{      
        
        public String getName() {
            
            return "try";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2, 3, 4};
        }

        public String docs() {
            return "void {tryCode, [varName, catchCode, [exceptionTypes]] | tryCode, catchCode} This function works similar to a try-catch block in most languages. If the code in"
                    + " tryCode throws an exception, instead of killing the whole script, it stops running, and begins running the catchCode."
                    + " var should be an ivariable, and it is set to an array containing the following information about the exception:"
                    + " 0 - The class of the exception; 1 - The message generated by the exception; 2 - The file the exception was generated from; 3 - The line the exception"
                    + " occured on. If exceptionTypes is provided, it should be an array of exception types, or a single string that this try function is interested in."
                    + " If the exception type matches one of the values listed, the exception will be caught, otherwise, the exception will continue up the stack."
                    + " If exceptionTypes is missing, it will catch all exceptions."
                    + " PLEASE NOTE! This function will not catch exceptions thrown by CommandHelper, only built in exceptions. "
                    + " Please see [[CommandHelper/Exceptions|the wiki page on exceptions]] for more information about what possible "
                    + " exceptions can be thrown and where, and examples.";
        }
        
        public ExceptionType[] thrown(){
            return new ExceptionType[]{ExceptionType.CastException, ExceptionType.FormatException};
        }

        public boolean isRestricted() {
            return false;
        }

        public boolean preResolveVariables() {
            return false;
        }

        public CHVersion since() {
            return CHVersion.V3_1_2;
        }

        public Boolean runAsync() {
            return null;
        }        

        @Override
        public Construct execs(Target t, Env env, Script that, GenericTreeNode<Construct>... nodes) {
            GenericTreeNode<Construct> tryCode = nodes[0];
            GenericTreeNode<Construct> varName = null;
            GenericTreeNode<Construct> catchCode = null;
            GenericTreeNode<Construct> types = null;
            if(nodes.length == 2){
                catchCode = nodes[1];
            } else if(nodes.length == 3){
                varName = nodes[1];
                catchCode = nodes[2];
            } else if(nodes.length == 4){
                varName = nodes[1];
                catchCode = nodes[2];
                types = nodes[3];
            }
            
            IVariable ivar = null;
            if(varName != null){
                Construct pivar = that.eval(varName, env);
                if(pivar instanceof IVariable){
                    ivar = (IVariable)pivar;
                } else {
                    throw new ConfigRuntimeException("Expected argument 2 to be an IVariable", ExceptionType.CastException, t);
                }
            }
            List<String> interest = new ArrayList<String>();
            if(types != null){
                Construct ptypes = that.seval(types, env);
                if(ptypes instanceof CString){
                    interest.add(ptypes.val());
                } else if(ptypes instanceof CArray){
                    CArray ca = (CArray)ptypes;
                    for(int i = 0; i < ca.size(); i++){
                        interest.add(ca.get(i, t).val());
                    }
                } else {
                    throw new ConfigRuntimeException("Expected argument 4 to be a string, or an array of strings.", 
                            ExceptionType.CastException, t);
                }
            }
            
            for(String in : interest){
                try{
                    ExceptionType.valueOf(in);
                } catch(IllegalArgumentException e){
                    throw new ConfigRuntimeException("Invalid exception type passed to try():" + in, 
                            ExceptionType.FormatException, t);
                }
            }
            
            try{
                that.eval(tryCode, env);
            } catch (ConfigRuntimeException e){
                if(Prefs.DebugMode()){
                    System.out.println("[CommandHelper]: Exception thrown -> " + e.getMessage() + " :: " + e.getExceptionType() + ":" + e.getFile() + ":" + e.getLineNum());
                }
                if(e.getExceptionType() != null  && (interest.isEmpty() || interest.contains(e.getExceptionType().toString()))){
                    if(catchCode != null){
                        CArray ex = new CArray(t);
                        ex.push(new CString(e.getExceptionType().toString(), t));
                        ex.push(new CString(e.getMessage(), t));
                        ex.push(new CString((e.getFile()!=null?e.getFile().getAbsolutePath():"null"), t));
                        ex.push(new CInt(e.getLineNum(), t));
                        ivar.setIval(ex);
                        env.GetVarList().set(ivar);
                        that.eval(catchCode, env);
                    }
                } else {
                    throw e;
                }
            }            
            
            return new CVoid(t);
        }
        public Construct exec(Target t, Env env, Construct... args) throws CancelCommandException, ConfigRuntimeException {
            return new CVoid(t);
        }
        
        @Override
        public boolean useSpecialExec() {
            return true;
        }
        
    }
    
    @api public static class _throw extends AbstractFunction{

        public String getName() {
            return "throw";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "nothing {exceptionType, msg} This function causes an exception to be thrown. If the exception type is null,"
                    + " it will be uncatchable. Otherwise, exceptionType may be any valid exception type.";
        }
        
        public ExceptionType[] thrown(){
            return new ExceptionType[]{ExceptionType.FormatException};
        }

        public boolean isRestricted() {
            return false;
        }

        public void varList(IVariableList varList) {}

        public boolean preResolveVariables() {
            return true;
        }

        public CHVersion since() {
            return CHVersion.V3_1_2;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Env env, Construct... args) throws CancelCommandException, ConfigRuntimeException {
            try{
                ExceptionType c = null;
                if(!(args[0] instanceof CNull)){
                    c = ExceptionType.valueOf(args[0].val());
                }
                throw new ConfigRuntimeException(args[1].val(), c, t);
            } catch(IllegalArgumentException e){
                throw new ConfigRuntimeException("Expected a valid exception type", ExceptionType.FormatException, t);
            }
        }
        
    }
}
