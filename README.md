# Zap
[![Build Status](https://travis-ci.org/SE17GroupH/Zap.svg?branch=master)](https://travis-ci.org/SE17GroupH/Zap)

An Android application showcasing 3 methods of authentication:
- Fingerprint
- Voice via Speaker Recognition
- via Token(or Magic) link 

### App Screenshots

![homepage] (/screenshots/homepage.png)
![register] (/screenshots/register.png)  
![login] (/screenshots/login.png)
![voicedna] (/screenshots/voicedna.png)  

  
### Mircosoft API (for speaker recognition) usage:

- Create a file named **SubKey.java** which will contain your Subscription Keys [generated](https://cognitive.uservoice.com/knowledgebase/articles/864225-how-do-i-get-subscription-keys) and put it [here with the source files](https://github.com/SE17GroupH/Zap/tree/master/app/src/main/java/teamh/zapapp):

```Java
package teamh.zapapp;

public class SubKey {
            public final static String sub_key1 = "abcd123abc123abc123abc123abc0000";
            public final static String sub_key2 = "abcd123abc123abc123abc123abc0000";
}
```
 
