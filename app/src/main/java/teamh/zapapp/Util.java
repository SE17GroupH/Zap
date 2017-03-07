package teamh.zapapp;

/**
 * Created by kaustubh on 3/7/17.
 */

 class Util{

    protected static boolean emailIsValid(String email){
        if (!email.contains("@")) {
            //Toast.makeText(context, "Invalid Email!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected static boolean passwordIsValid(String passwd){
        if(passwd.length() < 6){
            //Toast.makeText(context, "Password too small!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    protected static  boolean passwordsMatch(String passwd, String rpasswd){
        if(!passwd.equals(rpasswd)){
            //Toast.makeText(context, "Password don't match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
