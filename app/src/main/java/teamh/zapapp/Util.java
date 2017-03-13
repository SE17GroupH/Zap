package teamh.zapapp;

/**
 * Created by kaustubh on 3/7/17.
 */

 public class Util{

    protected static boolean emailIsValid(String email){
        if (!email.contains("@")) {
            return false;
        }
        return true;
    }

    protected static boolean passwordIsValid(String passwd){
        if(passwd.length() < 6){
            return false;
        }
        return true;
    }

    protected static  boolean passwordsMatch(String passwd, String rpasswd){
        if(!passwd.equals(rpasswd)){
            return false;
        }
        return true;
    }

}
