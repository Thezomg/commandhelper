/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.laytonsmith.core.functions;

import com.laytonsmith.core.CHVersion;
import com.laytonsmith.core.Env;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.api;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.Exceptions.ExceptionType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Layton
 */
public class Regex {
    
    public static String docs(){
        return "This class provides regular expression functions. For more details, please see the page on "
                + "[[CommandHelper/Regex|regular expressions]]";
    }
    
    @api public static class reg_match extends AbstractFunction{

        public String getName() {
            return "reg_match";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "array {pattern, subject} Searches for the given pattern, and returns an array with the results. Captures are supported."
                    + " If the pattern is not found anywhere in the subject, an empty array is returned. The indexes of the array"
                    + " follow typical regex fashion; the 0th element is the whole match, and 1-n are the captures specified in"
                    + " the regex.";
        }

        public ExceptionType[] thrown() {
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
            return CHVersion.V3_2_0;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Env env, Construct... args) throws ConfigRuntimeException {
            Pattern pattern = getPattern(args[0], t);
            String subject = args[1].val();
            CArray ret = new CArray(t);
            Matcher m = pattern.matcher(subject);
            if(m.find()){
                ret.push(new CString(m.group(0), t));

                for(int i = 1; i <= m.groupCount(); i++){
                    if(m.group(i) == null){
                        ret.push(new CNull(t));
                    } else {
                        ret.push(Static.resolveConstruct(m.group(i), t));
                    }
                }
            }
            return ret;
        }
        
    }
    
    @api public static class reg_match_all extends AbstractFunction{

        public String getName() {
            return "reg_match_all";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "array {pattern, subject} Searches subject for all matches to the regular expression given in pattern, unlike reg_match,"
                    + " which just returns the first match.";
        }

        public ExceptionType[] thrown() {
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
            return CHVersion.V3_2_0;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Env env, Construct... args) throws ConfigRuntimeException {
            Pattern pattern = getPattern(args[0], t);
            String subject = args[1].val();
            CArray fret = new CArray(t);
            Matcher m = pattern.matcher(subject);
            while(m.find()){
                CArray ret = new CArray(t);
                ret.push(new CString(m.group(0), t));

                for(int i = 1; i <= m.groupCount(); i++){
                    ret.push(new CString(m.group(i), t));
                }
                fret.push(ret);
            }
            return fret;
        }
        
    }
    
    @api public static class reg_replace extends AbstractFunction{

        public String getName() {
            return "reg_replace";
        }

        public Integer[] numArgs() {
            return new Integer[]{3};
        }

        public String docs() {
            return "string {pattern, replacement, subject} Replaces any occurances of pattern with the replacement in subject."
                    + " Back references are allowed.";
        }

        public ExceptionType[] thrown() {
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
            return CHVersion.V3_2_0;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Env env, Construct... args) throws ConfigRuntimeException {
            Pattern pattern = getPattern(args[0], t);
            String replacement = args[1].val();
            String subject = args[2].val();
            String ret = "";
            
            ret = pattern.matcher(subject).replaceAll(replacement);
            
            return new CString(ret, t);
        }
        
    }
    
    @api public static class reg_split extends AbstractFunction{

        public String getName() {
            return "reg_split";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "array {pattern, subject} Splits a string on the given regex, and returns an array of the parts. If"
                    + " nothing matched, an array with one element, namely the original subject, is returned.";
        }

        public ExceptionType[] thrown() {
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
            return CHVersion.V3_2_0;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Env env, Construct... args) throws ConfigRuntimeException {
            Pattern pattern = getPattern(args[0], t);
            String subject = args[1].val();
            String [] rsplit = pattern.split(subject);
            CArray ret = new CArray(t);
            for(String split : rsplit){
                ret.push(new CString(split, t));
            }
            return ret;
        }
        
        
    }  
    
    @api public static class reg_count extends AbstractFunction{

        public String getName() {
            return "reg_count";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "int {pattern, subject} Counts the number of occurances in the subject.";
        }

        public ExceptionType[] thrown() {
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
            return CHVersion.V3_2_0;
        }

        public Boolean runAsync() {
            return null;
        }

        public Construct exec(Target t, Env env, Construct... args) throws ConfigRuntimeException {
            Pattern pattern = getPattern(args[0], t);
            String subject = args[1].val();
            long ret = 0;
            Matcher m = pattern.matcher(subject);
            while(m.find()){
                ret++;
            }
            return new CInt(ret, t);
        }
        
    }
    
    private static Pattern getPattern(Construct c, Target t){
        String regex = "";
        int flags = 0;
        String sflags = "";
        if(c instanceof CArray){
            CArray ca = (CArray)c;
            regex = ca.get(0, t).val();
            sflags = ca.get(1, t).val();
            for(int i = 0; i < sflags.length(); i++){
                if(sflags.toLowerCase().charAt(i) == 'i'){
                    flags |= Pattern.CASE_INSENSITIVE;
                } else if(sflags.toLowerCase().charAt(i) == 'm'){
                    flags |= Pattern.MULTILINE;
                } else if(sflags.toLowerCase().charAt(i) == 's'){
                    flags |= Pattern.DOTALL;
                } else {
                    throw new ConfigRuntimeException("Unrecognized flag: " + sflags.toLowerCase().charAt(i), ExceptionType.FormatException, t);
                }
            }
        } else {
            regex = c.val();
        }
        return Pattern.compile(regex, flags);
    }
}
